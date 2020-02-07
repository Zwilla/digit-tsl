package eu.europa.ec.joinup.tsl.business.repository;

import org.springframework.data.repository.CrudRepository;

import eu.europa.ec.joinup.tsl.model.DBRole;

public interface RoleRepository extends CrudRepository<DBRole, Integer> {

    DBRole findByCode(String code);
}
