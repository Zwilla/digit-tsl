package eu.europa.ec.joinup.tsl.business.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import eu.europa.ec.joinup.tsl.model.DBDraftStore;

public interface DraftStoreRepository extends CrudRepository<DBDraftStore, String> {

    List<DBDraftStore> findByLastVerificationBeforeOrderByLastVerificationDesc(Date lastVerification);

}
