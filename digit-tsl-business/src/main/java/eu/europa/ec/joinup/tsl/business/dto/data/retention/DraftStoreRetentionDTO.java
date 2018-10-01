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
package eu.europa.ec.joinup.tsl.business.dto.data.retention;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eu.europa.ec.joinup.tsl.model.DBDraftStore;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;

public class DraftStoreRetentionDTO implements Serializable {

    private static final long serialVersionUID = 3963763939421753224L;

    private String draftStoreId;
    private Date lastVerification;
    private List<TrustedListRetentionDTO> tls;

    public DraftStoreRetentionDTO() {
        super();
    }

    public DraftStoreRetentionDTO(DBDraftStore dbDS) {
        super();
        draftStoreId = dbDS.getDraftStoreId();
        lastVerification = dbDS.getLastVerification();
        tls = new ArrayList<>();
        for (DBTrustedLists dbTL : dbDS.getDraftList()) {
            tls.add(new TrustedListRetentionDTO(dbTL));
        }
    }

    public DraftStoreRetentionDTO(RetentionCriteriaDTO criteria, List<DBTrustedLists> dbTLs) {
        draftStoreId = criteria.getTarget().toString();
        lastVerification = criteria.getDate();
        tls = new ArrayList<>();
        for (DBTrustedLists dbTL : dbTLs) {
            tls.add(new TrustedListRetentionDTO(dbTL));
        }
    }

    public String getDraftStoreId() {
        return draftStoreId;
    }

    public void setDraftStoreId(String draftStoreId) {
        this.draftStoreId = draftStoreId;
    }

    public Date getLastVerification() {
        return lastVerification;
    }

    public void setLastVerification(Date lastVerification) {
        this.lastVerification = lastVerification;
    }

    /**
     * Init if list null
     */
    public List<TrustedListRetentionDTO> getTls() {
        if (tls == null) {
            tls = new ArrayList<>();
        }
        return tls;
    }

    public void setTls(List<TrustedListRetentionDTO> tls) {
        this.tls = tls;
    }

}
