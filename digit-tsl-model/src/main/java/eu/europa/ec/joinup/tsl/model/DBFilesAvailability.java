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
package eu.europa.ec.joinup.tsl.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import eu.europa.ec.joinup.tsl.model.enums.AvailabilityStatus;

@Entity
@Table(name = "TL_FILES_AVAILABILITY", indexes = { @Index(columnList = "FILE_ID", name = "file_idx"), @Index(columnList = "CHECK_DATE", name = "check_date_idx") })
public class DBFilesAvailability {

    @Id
    @GeneratedValue
    @Column(name = "AVAILABILITY_ID", nullable = false, updatable = false)
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "FILE_ID", nullable = false, updatable = false)
    private DBFiles file;

    @Column(name = "STATUS", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private AvailabilityStatus status;

    @Column(name = "CHECK_DATE", nullable = false, updatable = false)
    private Date checkDate;

    @Column(name = "ALERTED", nullable = true)
    private Boolean alerted;

    public DBFilesAvailability() {
        alerted = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DBFiles getFile() {
        return file;
    }

    public void setFile(DBFiles file) {
        this.file = file;
    }

    public AvailabilityStatus getStatus() {
        return status;
    }

    public void setStatus(AvailabilityStatus status) {
        this.status = status;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public Boolean getAlerted() {
        if (alerted == null) {
            return false;
        }
        return alerted;
    }

    public void setAlerted(Boolean alerted) {
        this.alerted = alerted;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((alerted == null) ? 0 : alerted.hashCode());
        result = (prime * result) + ((checkDate == null) ? 0 : checkDate.hashCode());
        result = (prime * result) + ((file == null) ? 0 : file.hashCode());
        result = (prime * result) + id;
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
        DBFilesAvailability other = (DBFilesAvailability) obj;
        if (alerted == null) {
            if (other.alerted != null) {
                return false;
            }
        } else if (!alerted.equals(other.alerted)) {
            return false;
        }
        if (checkDate == null) {
            if (other.checkDate != null) {
                return false;
            }
        } else if (!checkDate.equals(other.checkDate)) {
            return false;
        }
        if (file == null) {
            if (other.file != null) {
                return false;
            }
        } else if (!file.equals(other.file)) {
            return false;
        }
        if (id != other.id) {
            return false;
        }
        if (status != other.status) {
            return false;
        }
        return true;
    }
}
