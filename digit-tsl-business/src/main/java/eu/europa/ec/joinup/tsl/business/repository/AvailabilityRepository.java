package eu.europa.ec.joinup.tsl.business.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import eu.europa.ec.joinup.tsl.model.DBFilesAvailability;

public interface AvailabilityRepository extends CrudRepository<DBFilesAvailability, Integer> {

    /**
     * This method returns the latest record
     */
    DBFilesAvailability findTopByFileIdOrderByCheckDateDesc(int fileId);

    @Query("SELECT f FROM DBFilesAvailability f WHERE f.file.id =:fileId AND f.checkDate BETWEEN :dMin AND :dMax")
    List<DBFilesAvailability> findBetweenTwoDate(@Param("fileId") int fileId, @Param("dMin") Date dMin, @Param("dMax") Date dMax);

}
