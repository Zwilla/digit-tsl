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

import java.util.Date;

import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.AvailabilityStatus;
import eu.europa.ec.joinup.tsl.model.enums.SignatureStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;

public class TLLightReport {

    private int id;
    private String name;
    private String territoryCode;
    private String countryName;
    private int sequenceNumber;
    private Date issueDate;
    private Date nextUpdateDate;
    private TLType tlType;
    private SignatureStatus signature;
    private AvailabilityStatus availability;
    private Date firstScanDate;

    public TLLightReport(DBTrustedLists dbTL, AvailabilityStatus availability, SignatureStatus signature, Date firstScanDate) {
        super();
        id = dbTL.getId();
        name = dbTL.getName();
        territoryCode = dbTL.getTerritory().getCodeTerritory();
        countryName = dbTL.getTerritory().getCountryName();
        sequenceNumber = dbTL.getSequenceNumber();
        issueDate = dbTL.getIssueDate();
        nextUpdateDate = dbTL.getNextUpdateDate();
        tlType = dbTL.getType();
        this.availability = availability;
        this.signature = signature;
        this.firstScanDate = firstScanDate;
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

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getNextUpdateDate() {
        return nextUpdateDate;
    }

    public void setNextUpdateDate(Date nextUpdateDate) {
        this.nextUpdateDate = nextUpdateDate;
    }

    public TLType getTlType() {
        return tlType;
    }

    public void setTlType(TLType tlType) {
        this.tlType = tlType;
    }

    public AvailabilityStatus getAvailability() {
        return availability;
    }

    public void setAvailability(AvailabilityStatus availability) {
        this.availability = availability;
    }

    public SignatureStatus getSignature() {
        return signature;
    }

    public void setSignature(SignatureStatus signature) {
        this.signature = signature;
    }

    public Date getFirstScanDate() {
        return firstScanDate;
    }

    public void setFirstScanDate(Date firstScanDate) {
        this.firstScanDate = firstScanDate;
    }

}
