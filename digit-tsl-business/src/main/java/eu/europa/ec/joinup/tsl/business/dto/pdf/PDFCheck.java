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
package eu.europa.ec.joinup.tsl.business.dto.pdf;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import eu.europa.ec.joinup.tsl.business.dto.CheckDTO;
import eu.europa.ec.joinup.tsl.business.dto.CheckResultDTO;
import eu.europa.ec.joinup.tsl.model.enums.CheckStatus;

@XStreamAlias("grouped-checks")
public class PDFCheck {

    private List<CheckDTO> checks;
    private int number;
    private String tlLocationId;

    public PDFCheck() {
        checks = new ArrayList<>();
        number = 0;
    }

    public void add(CheckResultDTO checkResult) {
        CheckDTO checkDTO = new CheckDTO(checkResult);
        checks.add(checkDTO);
        number++;
        tlLocationId = checkResult.getLocation();
    }

    public void add(CheckDTO check) {
        checks.add(check);
        number++;
        tlLocationId = check.getHrLocation();
    }

    public String getLocationId() {
        if (!checks.isEmpty()) {
            return checks.get(0).getId();
        } else {
            return null;
        }
    }

    public CheckStatus getStatus() {
        if (!checks.isEmpty()) {
            return checks.get(0).getStatus();
        } else {
            return null;
        }
    }

    public List<CheckDTO> getChecks() {
        return checks;
    }

    public void setChecks(List<CheckDTO> checks) {
        this.checks = checks;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTlLocationId() {
        return tlLocationId;
    }

    public void setTlLocationId(String tlLocationId) {
        this.tlLocationId = tlLocationId;
    }

}
