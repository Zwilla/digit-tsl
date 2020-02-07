package eu.europa.ec.joinup.tsl.business.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import eu.europa.ec.joinup.tsl.model.DBApplicationProperty;

public interface ApplicationPropertyRepository extends CrudRepository<DBApplicationProperty, Integer> {

    @Override
    List<DBApplicationProperty> findAll();

    List<DBApplicationProperty> findByTypeNotIn(List<String> types);

    DBApplicationProperty findByType(String type);
}
