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
package eu.europa.ec.joinup.tsl.business.util;

import java.util.Comparator;

import eu.europa.ec.joinup.tsl.business.dto.CheckDTO;

/**
 * Order CheckDTO by hrLocation > status > description
 */
public class TLChecksComparatorUtils implements Comparator<CheckDTO> {

    @Override
    public int compare(CheckDTO o1, CheckDTO o2) {
        String location1 = o1.getHrLocation();
        String location2 = o2.getHrLocation();
        int locComp = location1.compareTo(location2);
        if (locComp != 0) {
            return locComp;
        } else {
            String target1 = o1.getStatus().toString();
            String target2 = o2.getStatus().toString();
            int sComp = target1.compareTo(target2);

            if (sComp != 0) {
                return sComp;
            } else {
                String desc1 = o1.getDescription();
                String desc2 = o2.getDescription();
                return desc1.compareTo(desc2);
            }
        }
    }

}
