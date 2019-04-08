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
package eu.europa.ec.joinup.tsl.business.dto.audit;

import java.io.Serializable;
import java.util.Date;

import eu.europa.ec.joinup.tsl.model.DBAudit;
import eu.europa.ec.joinup.tsl.model.enums.AuditAction;
import eu.europa.ec.joinup.tsl.model.enums.AuditStatus;
import eu.europa.ec.joinup.tsl.model.enums.AuditTarget;

public class Audit implements Serializable {

    private static final long serialVersionUID = -3775159015988230085L;

    private int id;
    private Date date;
    private AuditTarget target;
    private AuditAction action;
    private AuditStatus status;
    private int fileId;
    private String fileDigest;
    private String countryCode;
    private String username;
    private String infos;

    public Audit() {

    }

    public Audit(DBAudit db) {
        this.setAction(db.getAction());
        this.setCountryCode(db.getCountryCode());
        this.setDate(db.getDate());
        this.setFileDigest(db.getFileDigest());
        this.setFileId(db.getFileId());
        this.setId(db.getId());
        this.setInfos(db.getInfos());
        this.setStatus(db.getStatus());
        this.setTarget(db.getTarget());
        this.setUsername(db.getUsername());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public AuditTarget getTarget() {
        return target;
    }

    public void setTarget(AuditTarget target) {
        this.target = target;
    }

    public AuditAction getAction() {
        return action;
    }

    public void setAction(AuditAction action) {
        this.action = action;
    }

    public AuditStatus getStatus() {
        return status;
    }

    public void setStatus(AuditStatus status) {
        this.status = status;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getFileDigest() {
        return fileDigest;
    }

    public void setFileDigest(String fileDigest) {
        this.fileDigest = fileDigest;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getInfos() {
        return infos;
    }

    public void setInfos(String infos) {
        this.infos = infos;
    }

    @Override
    public String toString() {
        return "Audit [id=" + id + ", date=" + date + ", countryCode=" + countryCode + "]";
    }

}
