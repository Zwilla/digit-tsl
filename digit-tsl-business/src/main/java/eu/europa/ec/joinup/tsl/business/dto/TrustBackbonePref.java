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
package eu.europa.ec.joinup.tsl.business.dto;

import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.CheckStatus;

public class TrustBackbonePref {

    private int id;
    private String name;
    private String territoryCode;
    private int sequenceNumber;
    private CheckStatus tbStatus;

    public TrustBackbonePref() {
    }

    public TrustBackbonePref(DBTrustedLists tl) {
        this.setId(tl.getId());
        this.setName(tl.getName());
        this.setTerritoryCode(tl.getTerritory().getCodeTerritory());
        this.setSequenceNumber(tl.getSequenceNumber());
    }

    @Override
    public String toString() {
        return String.format("TrustBackbonePref[id=%d; Name = '%s', Territory = '%s']", getId(), getName(), getTerritoryCode());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTerritoryCode() {
        return territoryCode;
    }

    public void setTerritoryCode(String territoryCode) {
        this.territoryCode = territoryCode;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public CheckStatus getTbStatus() {
        return tbStatus;
    }

    public void setTbStatus(CheckStatus tbStatus) {
        this.tbStatus = tbStatus;
    }

}
