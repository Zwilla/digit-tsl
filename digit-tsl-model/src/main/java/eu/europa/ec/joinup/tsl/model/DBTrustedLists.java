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
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import eu.europa.ec.joinup.tsl.model.enums.CheckStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;

@Entity
@Table(name = "TL_TRUSTED_LISTS")
public class DBTrustedLists {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "TERRITORY")
    private DBCountries territory;

    @OneToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "XML_FILE_ID")
    private DBFiles xmlFile;

    @OneToMany(mappedBy = "trustedList", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY/* , orphanRemoval = true */)
    @OrderBy("status")
    private List<DBCheckResult> checkResults;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_TL_ID")
    private DBTrustedLists parent;

    @Column(name = "TL_TYPE")
    @Enumerated(EnumType.STRING)
    private TLType type;

    @Column(name = "TL_STATUS")
    @Enumerated(EnumType.STRING)
    private TLStatus status;

    @Column(name = "SEQUENCE_NUMBER")
    private int sequenceNumber;

    @Column(name = "VERSION_IDENTIFIER")
    private int versionIdentifier;

    @Column(name = "ISSUE_DATE")
    private Date issueDate;

    @Column(name = "NEXT_UPDATE_DATE")
    private Date nextUpdateDate;

    @Column(name = "CHECKOUT_BY")
    private String checkout;

    @Column(name = "ARCHIVE")
    private boolean archive = false;

    @Column(name = "CHECK_STATUS")
    @Enumerated(EnumType.STRING)
    private CheckStatus checkStatus;

    @Column(name = "LAST_EDITED_DATE")
    private Date lastEditedDate;

    @Column(name = "LAST_ACCESS_DATE")
    private Date lastAccessDate;

    @Column(name = "OWNER_ID")
    private String draftStoreId;

    @Column(name = "CHECK_TO_RUN")
    private Boolean checkToRun = true;

    @Column(name = "RUN_RULES_DATE")
    private Date checkDate;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "TL_TRUSTEDLIST_NOTIFICATION", joinColumns = { @JoinColumn(name = "TL_ID") }, inverseJoinColumns = { @JoinColumn(name = "NOTIFICATION_ID") })
    private List<DBNotification> notifications;

    @Column(name = "CREATED_BY", nullable = true)
    private String createdBy;

    public DBTrustedLists() {
    }

    @Override
    public String toString() {
        return String.format("TrustedListsDB[id=%d; Name = '%s', Status = '%s']", getId(), getName(), getStatus().name());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + id;
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
        DBTrustedLists other = (DBTrustedLists) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }

    /*
     * GETTERS AND SETTERS
     */
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

    public DBCountries getTerritory() {
        return territory;
    }

    public void setTerritory(DBCountries territory) {
        this.territory = territory;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public int getVersionIdentifier() {
        return versionIdentifier;
    }

    public void setVersionIdentifier(int versionIdentifier) {
        this.versionIdentifier = versionIdentifier;
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

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    public DBTrustedLists getParent() {
        return parent;
    }

    public void setParent(DBTrustedLists parent) {
        this.parent = parent;
    }

    public TLType getType() {
        return type;
    }

    public void setType(TLType type) {
        this.type = type;
    }

    public TLStatus getStatus() {
        return status;
    }

    public void setStatus(TLStatus status) {
        this.status = status;
    }

    public DBFiles getXmlFile() {
        return xmlFile;
    }

    public void setXmlFile(DBFiles xmlFile) {
        this.xmlFile = xmlFile;
    }

    public List<DBCheckResult> getCheckResults() {
        return checkResults;
    }

    public void setCheckResults(List<DBCheckResult> checkResults) {
        this.checkResults = checkResults;
    }

    public CheckStatus getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(CheckStatus checkStatus) {
        this.checkStatus = checkStatus;
    }

    public Date getLastEditedDate() {
        return lastEditedDate;
    }

    public void setLastEditedDate(Date lastEditedDate) {
        this.lastEditedDate = lastEditedDate;
    }

    public String getDraftStoreId() {
        return draftStoreId;
    }

    public void setDraftStoreId(String draftStoreId) {
        this.draftStoreId = draftStoreId;
    }

    public boolean isCheckToRun() {
        if (checkToRun != null) {
            return checkToRun;
        } else {
            return true;
        }
    }

    public void setCheckToRun(boolean checkToRun) {
        this.checkToRun = checkToRun;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public List<DBNotification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<DBNotification> notifications) {
        this.notifications = notifications;
    }

    public Boolean getCheckToRun() {
        return checkToRun;
    }

    public void setCheckToRun(Boolean checkToRun) {
        this.checkToRun = checkToRun;
    }

    public Date getLastAccessDate() {
        return lastAccessDate;
    }

    public void setLastAccessDate(Date lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

}
