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
package eu.europa.ec.joinup.tsl.web.form;

import java.util.Date;

import eu.europa.ec.joinup.tsl.business.dto.tl.TLSchemeInformation;

public class TLInformationEdition {

    private int tlId;
    private TLSchemeInformation tlSchemeInfoObj;
    private String editAttribute;
    private Date lastEditedDate;
    private String cookie;
    private String tslIdentifier;
    private boolean checkToRun;

    public int getTlId() {
        return tlId;
    }

    public void setTlId(int tlId) {
        this.tlId = tlId;
    }

    public TLSchemeInformation getTlSchemeInfoObj() {
        return tlSchemeInfoObj;
    }

    public void setTlSchemeInfoObj(TLSchemeInformation tlSchemeInfoObj) {
        this.tlSchemeInfoObj = tlSchemeInfoObj;
    }

    public String getEditAttribute() {
        return editAttribute;
    }

    public void setEditAttribute(String editAttribute) {
        this.editAttribute = editAttribute;
    }

    public Date getLastEditedDate() {
        return lastEditedDate;
    }

    public void setLastEditedDate(Date lastEditedDate) {
        this.lastEditedDate = lastEditedDate;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getTslIdentifier() {
        return tslIdentifier;
    }

    public void setTslIdentifier(String tslIdentifier) {
        this.tslIdentifier = tslIdentifier;
    }

    public boolean isCheckToRun() {
        return checkToRun;
    }

    public void setCheckToRun(boolean checkToRun) {
        this.checkToRun = checkToRun;
    }
}
