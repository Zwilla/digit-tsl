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
import eu.europa.ec.joinup.tsl.model.enums.CheckStatus;

public class CheckResultDTO {

    private String id;
    private String checkId;
    private String location;
    private String description;
    private CheckStatus status;

    public CheckResultDTO(String id, CheckDTO check, boolean isSuccess) {
        super();
        this.id = id;
        checkId = check.getId();
        location = "";

        if (isSuccess) {
            status = CheckStatus.SUCCESS;
        } else {
            status = check.getStatus();
        }

        description = check.getDescription();
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((id == null) ? 0 : id.hashCode());
        result = (prime * result) + ((description == null) ? 0 : description.hashCode());
        result = (prime * result) + ((location == null) ? 0 : location.hashCode());
        result = (prime * result) + ((status == null) ? 0 : status.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CheckResultDTO other = (CheckResultDTO) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (location == null) {
            if (other.location != null) {
                return false;
            }
        } else if (!location.equals(other.location)) {
            return false;
        }
        if (status != other.status) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CheckResultDTO [id=" + id + ", checkId=" + checkId + ", location=" + location + ", description=" + description + ", status=" + status + "]";
    }

}
