package eu.europa.ec.joinup.tsl.business.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.DBFiles;

public interface FileRepository extends CrudRepository<DBFiles, Integer> {

    List<DBFiles> findByDigest(String digest);

    @Query("SELECT f FROM DBFiles f,DBTrustedLists t WHERE f.id = t.xmlFile.id AND t.territory =:tlTerritory AND t.status = 'PROD' ORDER BY f.firstScanDate ASC")
    List<DBFiles> findByPublishedTerritoryOrderByFirstScanDate(@Param("tlTerritory") DBCountries tlTerritory);

}
