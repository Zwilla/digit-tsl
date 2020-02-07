package eu.europa.ec.joinup.tsl.business.repository;

import org.springframework.data.repository.CrudRepository;

import eu.europa.ec.joinup.tsl.model.DBColumnAvailable;

public interface ColumnsRepository extends CrudRepository<DBColumnAvailable, Integer> {

    DBColumnAvailable findByCode(String name);
}
