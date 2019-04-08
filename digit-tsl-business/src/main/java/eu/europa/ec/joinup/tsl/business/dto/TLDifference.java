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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((currentValue == null) ? 0 : currentValue.hashCode());
        result = prime * result + ((hrLocation == null) ? 0 : hrLocation.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((publishedValue == null) ? 0 : publishedValue.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TLDifference other = (TLDifference) obj;
        if (currentValue == null) {
            if (other.currentValue != null)
                return false;
        } else if (!currentValue.equals(other.currentValue))
            return false;
        if (hrLocation == null) {
            if (other.hrLocation != null)
                return false;
        } else if (!hrLocation.equals(other.hrLocation))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (publishedValue == null) {
            if (other.publishedValue != null)
                return false;
        } else if (!publishedValue.equals(other.publishedValue))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "TLDifference [id=" + id + ", publishedValue=" + publishedValue + ", currentValue=" + currentValue + ", hrLocation=" + hrLocation + "]";
    }

}
