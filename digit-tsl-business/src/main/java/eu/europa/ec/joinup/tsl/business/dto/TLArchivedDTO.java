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

public class TLArchivedDTO {

	private int id;

	private String name;

	private int sequenceNumber;

	private Date issueDate;

	private Date nextUpdate;

	private Date firstScanDate;

	public TLArchivedDTO() {
		super();
	}

	public TLArchivedDTO(DBTrustedLists tl) {
		super();
		this.id = tl.getId();
		this.name = tl.getName();
		this.sequenceNumber = tl.getSequenceNumber();
		this.issueDate = tl.getIssueDate();
		this.nextUpdate = tl.getNextUpdateDate();
		this.firstScanDate = tl.getXmlFile().getFirstScanDate();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Date getNextUpdate() {
		return nextUpdate;
	}

	public void setNextUpdate(Date nextUpdate) {
		this.nextUpdate = nextUpdate;
	}

	public Date getFirstScanDate() {
		return firstScanDate;
	}

	public void setFirstScanDate(Date firstScanDate) {
		this.firstScanDate = firstScanDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
