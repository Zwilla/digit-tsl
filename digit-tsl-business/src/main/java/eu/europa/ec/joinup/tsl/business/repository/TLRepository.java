package eu.europa.ec.joinup.tsl.business.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;

public interface TLRepository extends CrudRepository<DBTrustedLists, Integer> {

    DBTrustedLists findByTerritoryAndStatusAndArchiveFalse(DBCountries territory, TLStatus status);

    List<DBTrustedLists> findByStatusAndArchiveFalseOrderByNameAsc(TLStatus status);

    List<DBTrustedLists> findAllByArchiveFalseOrderByNameAsc();

    List<DBTrustedLists> findAllByArchiveFalseOrderByStatusDesc();

    List<DBTrustedLists> findByTerritoryAndStatusAndArchiveTrueOrderByIdDesc(DBCountries territory, TLStatus prod);

    List<DBTrustedLists> findByXmlFileDigestAndStatusOrderByNameAsc(String digest, TLStatus status);

    DBTrustedLists findByXmlFileId(int fileId);

    @Modifying
    @Query("UPDATE DBTrustedLists tl SET tl.lastAccessDate = :lastAccessDate WHERE tl.id = :id")
    void updateLastAccessDate(@Param("lastAccessDate") Date lastAccess, @Param("id") int id);

    Long countByStatus(TLStatus status);

}
