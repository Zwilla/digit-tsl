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

import eu.europa.ec.joinup.tsl.business.dto.tlcc.Check;
import eu.europa.ec.joinup.tsl.model.DBCheck;
import eu.europa.ec.joinup.tsl.model.DBCheckResult;
import eu.europa.ec.joinup.tsl.model.enums.CheckStatus;

/**
 * @author simon.ghisalberti
 *
 */
public class CheckResultDTO {

    private String id;
    private String checkId;
    private String location;
    private String description;
    private String translation;
    private String standardReference;
    private CheckStatus status;

    public CheckResultDTO(String id, CheckDTO check, boolean isSuccess) {
        super();
        this.id = id;
        this.checkId = check.getId();
        this.location = "";

        if (isSuccess) {
            this.status = CheckStatus.SUCCESS;
        } else {
            this.status = check.getStatus();
        }

        this.description = check.getDescription();
        this.translation = check.getTranslation();
        this.standardReference = check.getStandardReference();
    }

    public CheckResultDTO(String id, DBCheck dbCheck) {
        this.id = id;
        this.checkId = dbCheck.getId();
        this.description = dbCheck.getDescription();
        this.status = dbCheck.getPriority();
        this.translation = dbCheck.getTranslation();
        this.standardReference = dbCheck.getStandardReference();
    }

    public CheckResultDTO(String id, Check tlccCheck) {
        super();
        this.id = id;
        checkId = tlccCheck.getCheckId();
        location = "";

        switch (tlccCheck.getStatus()) {
        case "SUCCESS":
            status = CheckStatus.SUCCESS;
            break;
        case "ERROR":
            status = CheckStatus.ERROR;
            break;
        case "WARNING":
            status = CheckStatus.WARNING;
            break;
        case "INFO":
            status = CheckStatus.INFO;
            break;
        case "IGNORE":
            status = CheckStatus.IGNORE;
            break;
        default:
            status = CheckStatus.IGNORE;
            break;
        }
        description = tlccCheck.getContent();
    }

    public CheckResultDTO(DBCheckResult dbResult) {
        this.id = dbResult.getLocation();
        this.checkId = dbResult.getCheck().getId();
        this.location = dbResult.getHrLocation();
        this.description = dbResult.getDescription();
        this.translation = dbResult.getCheck().getTranslation();
        this.status = dbResult.getStatus();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CheckStatus getStatus() {
        return status;
    }

    public void setStatus(CheckStatus status) {
        this.status = status;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getStandardReference() {
        return standardReference;
    }

    public void setStandardReference(String standardReference) {
        this.standardReference = standardReference;
    }

    @Override
    public String toString() {
        return "CheckResultDTO [id=" + id + ", checkId=" + checkId + ", location=" + location + ", description=" + description + ", status=" + status + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((checkId == null) ? 0 : checkId.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        result = prime * result + ((standardReference == null) ? 0 : standardReference.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((translation == null) ? 0 : translation.hashCode());
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
        CheckResultDTO other = (CheckResultDTO) obj;
        if (checkId == null) {
            if (other.checkId != null)
                return false;
        } else if (!checkId.equals(other.checkId))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (location == null) {
            if (other.location != null)
                return false;
        } else if (!location.equals(other.location))
            return false;
        if (standardReference == null) {
            if (other.standardReference != null)
                return false;
        } else if (!standardReference.equals(other.standardReference))
            return false;
        if (status != other.status)
            return false;
        if (translation == null) {
            if (other.translation != null)
                return false;
        } else if (!translation.equals(other.translation))
            return false;
        return true;
    }

}