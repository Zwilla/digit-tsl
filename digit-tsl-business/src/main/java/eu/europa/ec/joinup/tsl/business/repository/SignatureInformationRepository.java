package eu.europa.ec.joinup.tsl.business.repository;

import org.springframework.data.repository.CrudRepository;

import eu.europa.ec.joinup.tsl.model.DBSignatureInformation;

public interface SignatureInformationRepository extends CrudRepository<DBSignatureInformation, Integer> {

}
