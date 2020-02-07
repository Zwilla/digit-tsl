package eu.europa.ec.joinup.tsl.business.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import eu.europa.ec.joinup.tsl.model.DBService;

public interface ServiceRepository extends CrudRepository<DBService, Integer> {

    void deleteAllByCountryCode(String countryCode);

    @Override
    List<DBService> findAll();

    List<DBService> findAllByCountryCode(String countryCode);

    List<DBService> findByCountryCodeInAndQServiceTypesInOrderByCountryCode(List<String> countries, Set<String> types);

}
