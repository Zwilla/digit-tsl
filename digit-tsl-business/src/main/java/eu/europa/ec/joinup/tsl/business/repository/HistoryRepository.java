package eu.europa.ec.joinup.tsl.business.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import eu.europa.ec.joinup.tsl.model.DBHistory;

public interface HistoryRepository extends CrudRepository<DBHistory, Integer> {

    void deleteAllByCountryCode(String countryCode);

    @Override
    List<DBHistory> findAll();

    List<DBHistory> findAllByCountryCode(String countryCode);

}
