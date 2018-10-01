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
package eu.europa.ec.joinup.tsl.business.dto.pdf;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.springframework.util.StringUtils;

import eu.europa.ec.joinup.tsl.business.dto.TLSignature;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.model.enums.SignatureStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;

public class PDFInfoTL {

    private String name;

    private String sequenceNumber;

    private String issueDate;

    private String nextUpdate;

    private String lastTimeChecked;

    private TLStatus status;

    private TLSignature signature;

    private String availability;

    public PDFInfoTL() {
        super();
    }

    public PDFInfoTL(TL tl, TLSignature signature) {
        super();
        name = tl.getDbName() + " (Sn" + tl.getSchemeInformation().getSequenceNumber() + ")";
        sequenceNumber = tl.getSchemeInformation().getSequenceNumber().toString();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        issueDate = formatter.format(tl.getSchemeInformation().getIssueDate());
        nextUpdate = formatter.format(tl.getSchemeInformation().getNextUpdateDate());
        if (tl.getCheckEdited() != null) {
            lastTimeChecked = formatter.format(tl.getCheckEdited());
        }
        status = tl.getDbStatus();
        if ((signature == null) || StringUtils.isEmpty(signature.getIndication())) {
            signature = new TLSignature();
            signature.setIndication(SignatureStatus.NOT_SIGNED);
        }
        this.signature = signature;
        if (tl.getAvailabilityStatus() == null) {
            availability = "/";
        } else {
            availability = tl.getAvailabilityStatus().name();
        }

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getNextUpdate() {
        return nextUpdate;
    }

    public void setNextUpdate(String nextUpdate) {
        this.nextUpdate = nextUpdate;
    }

    public String getLastTimeChecked() {
        return lastTimeChecked;
    }

    public void setLastTimeChecked(String lastTimeChecked) {
        this.lastTimeChecked = lastTimeChecked;
    }

    public TLStatus getStatus() {
        return status;
    }

    public void setStatus(TLStatus status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public TLSignature getSignature() {
        return signature;
    }

    public void setSignature(TLSignature signature) {
        this.signature = signature;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

}
