package eu.europa.ec.joinup.tsl.business.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import eu.europa.ec.joinup.tsl.model.DBAudit;

public interface AuditRepository extends CrudRepository<DBAudit, Integer> {

    List<DBAudit> findAllByOrderByDateDesc();

}
