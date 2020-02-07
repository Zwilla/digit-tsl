package eu.europa.ec.joinup.tsl.business.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import eu.europa.ec.joinup.tsl.model.DBCheck;
import eu.europa.ec.joinup.tsl.model.DBCheckResult;
import eu.europa.ec.joinup.tsl.model.enums.CheckStatus;

public interface ResultRepository extends CrudRepository<DBCheckResult, Integer> {

    List<DBCheckResult> findByCheck(DBCheck check);

    List<DBCheckResult> findByCheckAndTrustedListId(DBCheck check, int dbId);

    List<DBCheckResult> findByTrustedListIdAndStatusIn(int tlId, List<CheckStatus> status);

    void deleteByTrustedListId(int tlId);

}
