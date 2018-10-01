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
package eu.europa.ec.joinup.tsl.business.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.repository.DraftStoreRepository;
import eu.europa.ec.joinup.tsl.model.DBDraftStore;

@Service
public class DraftStoreService {

    @Autowired
    private DraftStoreRepository draftStoreRepository;

    public DBDraftStore findOne(String draftStoreId) {
        return draftStoreRepository.findOne(draftStoreId);
    }

    /**
     * Get DBDraftStore by ID and initialize TL persistence bag
     *
     * @param draftStoreId
     * @return
     */
    @Transactional
    public DBDraftStore findOneInitialized(String draftStoreId) {
        DBDraftStore dbDS = draftStoreRepository.findOne(draftStoreId);
        Hibernate.initialize(dbDS.getDraftList());
        return dbDS;
    }

    /**
     * Create a new draftStore with a UUID unique and not already existing in database
     */
    public String getNewDraftStore() {
        String uuid = UUID.randomUUID().toString();
        while (checkDraftStoreId(uuid)) {
            uuid = UUID.randomUUID().toString();
        }
        DBDraftStore ds = new DBDraftStore();
        ds.setDraftStoreId(uuid);
        ds.setLastVerification(new Date());
        draftStoreRepository.save(ds);
        return ds.getDraftStoreId();
    }

    /**
     * Get draftStore by ID and update last verification date
     *
     * @param draftStoreId
     * @return true or false if draftStore is null
     */
    public Boolean checkDraftStoreId(String draftStoreId) {
        DBDraftStore ds = findOne(draftStoreId);
        return updateLastVerificationDate(ds);
    }

    /**
     * Update draft store last verification date
     *
     * @param draftStore
     * @return true or false if draftStore is null
     */
    public Boolean updateLastVerificationDate(DBDraftStore draftStore) {
        if (draftStore != null) {
            draftStore.setLastVerification(new Date());
            draftStoreRepository.save(draftStore);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get list of draft store not verified since a given date
     *
     * @param lastVerification
     * @return
     */
    public List<DBDraftStore> getDraftStoreNotVerifiedSince(Date lastVerification) {
        return draftStoreRepository.findByLastVerificationBeforeOrderByLastVerificationDesc(lastVerification);
    }

    public void deleteDraftStore(String draftStoreId) {
        draftStoreRepository.delete(draftStoreId);
    }

}
