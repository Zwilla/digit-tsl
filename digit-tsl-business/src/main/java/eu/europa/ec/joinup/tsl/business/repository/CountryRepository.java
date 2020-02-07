package eu.europa.ec.joinup.tsl.business.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import eu.europa.ec.joinup.tsl.model.DBCountries;

public interface CountryRepository extends CrudRepository<DBCountries, String> {

    List<DBCountries> findAllByOrderByCountryName();

}
