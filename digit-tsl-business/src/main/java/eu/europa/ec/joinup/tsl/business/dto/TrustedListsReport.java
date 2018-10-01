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

import eu.europa.ec.joinup.tsl.business.dto.contact.ContactList.TLSOContact;
import eu.europa.ec.joinup.tsl.model.enums.AvailabilityStatus;
import eu.europa.ec.joinup.tsl.model.enums.CheckStatus;
import eu.europa.ec.joinup.tsl.model.enums.SignatureStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;

public class TrustedListsReport {

	private int id;
	private String name;
	private String territoryCode;
	private String countryName;
	private int parentTlId;
	private int sequenceNumber;
	private Date issueDate;
	private Date nextUpdateDate;
	private boolean archive;
	private TLStatus tlStatus;
	private TLType tlType;
	private SignatureStatus sigStatus;
	private Date lastScanDate;
	private AvailabilityStatus availability;
	private boolean pref;
	private CheckStatus tbStatus;
	private CheckStatus checkStatus;
	private TLSOContact contact;
	private boolean checkToRun;

	private boolean migrate;

	public TrustedListsReport() {
	}

	@Override
	public String toString() {
		return String.format("TrustedListsReport[id=%d; Name = '%s', Territory = '%s']", getId(), getName(), getTerritoryCode());
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

	public int getParentTlId() {
		return parentTlId;
	}

	public void setParentTlId(int parentTlId) {
		this.parentTlId = parentTlId;
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

	public boolean isArchive() {
		return archive;
	}

	public void setArchive(boolean archive) {
		this.archive = archive;
	}

	public TLStatus getTlStatus() {
		return tlStatus;
	}

	public void setTlStatus(TLStatus tlStatus) {
		this.tlStatus = tlStatus;
	}

	public SignatureStatus getSigStatus() {
		return sigStatus;
	}

	public void setSigStatus(SignatureStatus sigStatus) {
		this.sigStatus = sigStatus;
	}

	public Date getLastScanDate() {
		return lastScanDate;
	}

	public void setLastScanDate(Date lastScanDate) {
		this.lastScanDate = lastScanDate;
	}

	public AvailabilityStatus getAvailability() {
		return availability;
	}

	public void setAvailability(AvailabilityStatus availability) {
		this.availability = availability;
	}

	public boolean isPref() {
		return pref;
	}

	public void setPref(boolean pref) {
		this.pref = pref;
	}

	public CheckStatus getTbStatus() {
		return tbStatus;
	}

	public void setTbStatus(CheckStatus tbStatus) {
		this.tbStatus = tbStatus;
	}

	public CheckStatus getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(CheckStatus checkStatus) {
		this.checkStatus = checkStatus;
	}

	public boolean isMigrate() {
		return migrate;
	}

	public void setMigrate(boolean migrate) {
		this.migrate = migrate;
	}

	public TLSOContact getContact() {
		return contact;
	}

	public void setContact(TLSOContact contact) {
		this.contact = contact;
	}

	public boolean isCheckToRun() {
		return checkToRun;
	}

	public void setCheckToRun(boolean checkToRun) {
		this.checkToRun = checkToRun;
	}

	public TLType getTlType() {
		return tlType;
	}

	public void setTlType(TLType tlType) {
		this.tlType = tlType;
	}

}
