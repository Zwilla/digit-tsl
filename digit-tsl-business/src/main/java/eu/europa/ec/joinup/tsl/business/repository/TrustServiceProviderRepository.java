package eu.europa.ec.joinup.tsl.business.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import eu.europa.ec.joinup.tsl.model.DBTrustServiceProvider;

public interface TrustServiceProviderRepository extends CrudRepository<DBTrustServiceProvider, Integer> {

    @Override
    List<DBTrustServiceProvider> findAll();

    List<DBTrustServiceProvider> findDistinctByCountryCodeInAndServicesQServiceTypesInOrderByCountryCode(List<String> countries, Set<String> types);

    List<DBTrustServiceProvider> findAllByCountryCode(String countryCode);

    void deleteAllByCountryCode(String countryCode);

}
