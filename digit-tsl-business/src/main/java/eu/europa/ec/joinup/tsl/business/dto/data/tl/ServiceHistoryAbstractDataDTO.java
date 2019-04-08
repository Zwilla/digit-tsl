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
package eu.europa.ec.joinup.tsl.business.dto.data.tl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import eu.europa.ec.joinup.tsl.model.DBHistory;
import eu.europa.ec.joinup.tsl.model.DBService;

public class ServiceHistoryAbstractDataDTO {

    private String countryCode;
    private List<String> names;
    private String type;
    private String status;
    private Date startingDate;
    private String takenOverBy;
    private List<String> qTypes;

    public ServiceHistoryAbstractDataDTO() {
        super();
    }

    public ServiceHistoryAbstractDataDTO(DBService dbService) {
        super();
        countryCode = dbService.getCountryCode();
        names = new ArrayList<>(dbService.getServiceNames());
        type = dbService.getType();
        status = dbService.getStatus();
        startingDate = dbService.getStartingDate();
        takenOverBy = dbService.getTakenOverBy();
        qTypes = new ArrayList<>(dbService.getQServiceTypes());
    }

    public ServiceHistoryAbstractDataDTO(DBHistory dbHistory) {
        super();
        countryCode = dbHistory.getCountryCode();
        names = new ArrayList<>(dbHistory.getHistoryNames());
        type = dbHistory.getType();
        status = dbHistory.getStatus();
        startingDate = dbHistory.getStartingDate();
        takenOverBy = dbHistory.getTakenOverBy();
        qTypes = new ArrayList<>(dbHistory.getQHistoryTypes());
    }

    public String getMName() {
        if (CollectionUtils.isEmpty(names)) {
            return "";
        }
        return names.get(0);
    }

    /**
     * Return true when service is taken over by
     */
    public Boolean isTakenOverBy() {
        return !StringUtils.isEmpty(takenOverBy);
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
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

    public List<String> getQTypes() {
        return qTypes;
    }

    public void setQTypes(List<String> qTypes) {
        this.qTypes = qTypes;
    }

}
