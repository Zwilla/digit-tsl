/*******************************************************************************
 * DIGIT-TSL - Trusted List Manager
 * Copyright (C) 2018 European Commission, provided under the CEF E-Signature programme
 *  
 * This file is part of the "DIGIT-TSL - Trusted List Manager" project.
 *  
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 *  
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
package eu.europa.ec.joinup.tsl.business.rules;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.joinup.tsl.business.dto.CheckDTO;
import eu.europa.ec.joinup.tsl.business.dto.CheckResultDTO;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.model.enums.CheckStatus;

public abstract class AbstractCheckValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCheckValidator.class);

    public abstract boolean isSupported(CheckDTO check);

    protected void addResult(CheckDTO check, String location, boolean valid, List<CheckResultDTO> results) {
        results.add(new CheckResultDTO(location, check, valid));
    }

    protected void runAsync(CheckDTO check, String location, Future<Boolean> futureValid, List<CheckResultDTO> results) {
        boolean result = false;
        try {
            while (true) {
                if (futureValid.isDone()) {
                    result = futureValid.get();
                    break;
                }
                Thread.sleep(100);
            }
        } catch (Exception e) {
            LOGGER.error("Unable to retrieve future value : " + e.getMessage());
        }
        addResult(check, location, result, results);
    }

    public List<CheckResultDTO> execute(CheckDTO check, TL previous, TL current) {
        if (!CheckStatus.IGNORE.equals(check.getStatus()) && (current != null) && isSupported(check)) {
            return validate(check, current);
        }
        return Collections.emptyList();
    }

    public abstract List<CheckResultDTO> validate(CheckDTO check, TL currentVersion);

}
