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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "TL_SERVICE")
public class DBService {

    @Id
    @Column(name = "ID", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "SERVICE_ID", unique = true, nullable = false, length = 50)
    private String serviceId;

    @Column(name = "COUNTRY_CODE", nullable = false)
    private String countryCode;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "TL_SERVICE_NAME", joinColumns = @JoinColumn(name = "SERVICE_ID"))
    @Column(name = "SERVICE_NAME")
    private Set<String> serviceNames;

    @Column(name = "TYPE", nullable = false, length = 100)
    private String type;

    @Column(name = "STATUS", nullable = false, length = 100)
    private String status;

    @Column(name = "STARTING_DATE", nullable = false)
    private Date startingDate;

    @Column(name = "TAKEN_OVER_BY", nullable = true)
    private String takenOverBy;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "TL_SERVICE_TYPE", joinColumns = @JoinColumn(name = "SERVICE_ID"))
    @Column(name = "SERVICE_TYPE")
    private Set<String> qServiceTypes;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "TSP_ID", nullable = false, updatable = false)
    private DBTrustServiceProvider trustServiceProvider;

    @OneToMany(mappedBy = "trustService", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DBHistory> history;

    public DBService() {
        super();
        serviceNames = new HashSet<>();
        qServiceTypes = new HashSet<>();
    }

    public String getMName() {
        if (serviceNames == null) {
            return "";
        }
        return serviceNames.iterator().next();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Set<String> getServiceNames() {
        return serviceNames;
    }

    public void setServiceNames(Set<String> serviceNames) {
        this.serviceNames = serviceNames;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(Date startingDate) {
        this.startingDate = startingDate;
    }

    public String getTakenOverBy() {
        return takenOverBy;
    }

    public void setTakenOverBy(String takenOverBy) {
        this.takenOverBy = takenOverBy;
    }

    public Set<String> getQServiceTypes() {
        return qServiceTypes;
    }

    public void setQServiceTypes(Set<String> qServiceTypes) {
        this.qServiceTypes = qServiceTypes;
    }

    public DBTrustServiceProvider getTrustServiceProvider() {
        return trustServiceProvider;
    }

    public void setTrustServiceProvider(DBTrustServiceProvider trustServiceProvider) {
        this.trustServiceProvider = trustServiceProvider;
    }

    public List<DBHistory> getHistory() {
        return history;
    }

    public void setHistory(List<DBHistory> history) {
        this.history = history;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + id;
        result = (prime * result) + ((serviceId == null) ? 0 : serviceId.hashCode());
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
        DBService other = (DBService) obj;
        if (id != other.id) {
            return false;
        }
        if (serviceId == null) {
            if (other.serviceId != null) {
                return false;
            }
        } else if (!serviceId.equals(other.serviceId)) {
            return false;
        }
        return true;
    }

}
