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
    public boolean checkDraftStoreId(String draftStoreId) {
        DBDraftStore ds = findOne(draftStoreId);
        return updateLastVerificationDate(ds);
    }

    /**
     * Update draft store last verification date
     *
     * @param draftStore
     * @return true or false if draftStore is null
     */
    public boolean updateLastVerificationDate(DBDraftStore draftStore) {
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

    /**
     * Count draftstore in database
     */
    public int countDraftStore() {
        return Math.toIntExact(draftStoreRepository.count());
    }

}
