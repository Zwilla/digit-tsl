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

import java.io.Serializable;

import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.DBProperties;

public class Properties implements Serializable {

    private static final long serialVersionUID = -3775159015988230085L;

    private int id;
    private String codeList;
    private String label;
    private String description;

    public Properties() {
    }

    public Properties(String codeList, DBProperties dbProp) {
        this.codeList = codeList;
        this.id = dbProp.getId();
        this.label = dbProp.getLabel();
        this.description = dbProp.getDescription();
    }

    public Properties(String codeList, DBCountries dbCountry) {
        this.codeList = codeList;
        this.label = dbCountry.getCodeTerritory();
        this.description = dbCountry.getCountryName();
    }

    @Override
    public String toString() {
        return String.format("Properties[code lsit='%s'; propertiesId = %d]", this.codeList, this.getId());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodeList() {
        return codeList;
    }

    public void setCodeList(String codeList) {
        this.codeList = codeList;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
