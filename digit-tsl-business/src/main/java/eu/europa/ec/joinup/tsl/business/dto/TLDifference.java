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

public class TLDifference {

    private String id;
    private String publishedValue;
    private String currentValue;
    private String hrLocation;

    public TLDifference() {
    }

    //	public TLDifference(Tag id, String oldValue) {
    //		this.setId(id.toString());
    //		this.setOldValue(oldValue);
    //	}

    //	public TLDifference(String parent, String oldValue) {
    //		this.setId(parent);
    //		this.setOldValue(oldValue);
    //	}

    public TLDifference(String parent, String publishedValue, String currentValue) {
        this.setId(parent);
        this.setPublishedValue(publishedValue);
        this.setCurrentValue(currentValue);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublishedValue() {
        return publishedValue;
    }

    public void setPublishedValue(String oldValue) {
        this.publishedValue = oldValue;
    }

    public String getHrLocation() {
        return hrLocation;
    }

    public void setHrLocation(String hrLocation) {
        this.hrLocation = hrLocation;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

}
