package eu.europa.ec.joinup.tsl.business.repository;

import org.springframework.data.repository.CrudRepository;

import eu.europa.ec.joinup.tsl.model.DBProperties;

public interface PropertiesRepository extends CrudRepository<DBProperties, Integer> {

}
