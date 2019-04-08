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

import java.util.ArrayList;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "TL_TRUST_SERVICE_PROVIDER")
public class DBTrustServiceProvider {

    @Id
    @Column(name = "ID", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "TSP_ID", unique = true, nullable = false, length = 40)
    private String tspId;

    @Column(name = "COUNTRY_CODE", nullable = false, length = 2)
    private String countryCode;

    @Column(name = "SEQUENCE_NUMBER", nullable = false)
    private int sequenceNumber;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "TL_TSP_TRADE_NAME", joinColumns = @JoinColumn(name = "TSP_ID"))
    @Column(name = "TSP_TRADE_NAME")
    private Set<String> tspTradeNames;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "TL_TSP_NAME", joinColumns = @JoinColumn(name = "TSP_ID"))
    @Column(name = "TSP_NAME")
    private Set<String> tspNames;

    @OneToMany(mappedBy = "trustServiceProvider", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DBService> services;

    public DBTrustServiceProvider() {
        super();
        tspNames = new HashSet<>();
        tspTradeNames = new HashSet<>();
        services = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTspId() {
        return tspId;
    }

    public void setTspId(String tspId) {
        this.tspId = tspId;
    }

    public Set<String> getTspTradeNames() {
        return tspTradeNames;
    }

    public void setTspTradeNames(Set<String> tspTradeNames) {
        this.tspTradeNames = tspTradeNames;
    }

    public Set<String> getTspNames() {
        return tspNames;
    }

    public void setTspNames(Set<String> tspNames) {
        this.tspNames = tspNames;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public List<DBService> getServices() {
        return services;
    }

    public void setServices(List<DBService> services) {
        this.services = services;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + id;
        result = (prime * result) + ((tspId == null) ? 0 : tspId.hashCode());
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
        DBTrustServiceProvider other = (DBTrustServiceProvider) obj;
        if (id != other.id) {
            return false;
        }
        if (tspId == null) {
            if (other.tspId != null) {
                return false;
            }
        } else if (!tspId.equals(other.tspId)) {
            return false;
        }
        return true;
    }

}
