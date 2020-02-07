package eu.europa.ec.joinup.tsl.business.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import eu.europa.ec.joinup.tsl.model.enums.Tag;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.CheckResultDTO;
import eu.europa.ec.joinup.tsl.model.DBCheck;
import eu.europa.ec.joinup.tsl.model.DBCheckResult;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.CheckStatus;
import eu.europa.ec.joinup.tsl.model.enums.CheckType;

@Service
@Transactional(value = TxType.REQUIRED)
public class CheckResultPersistenceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckResultPersistenceService.class);

    @Autowired
    private TLService tlService;

    @Autowired
    private CheckService checkService;

    /**
     * Compare results with check find by @tlId; Store new checks and update check status of existing ones
     *
     * @param tlId
     * @param currentChecks
     */
    @Transactional(value = TxType.REQUIRES_NEW)
    public void persistAllResults(int tlId, List<CheckResultDTO> currentChecks) {

        Map<String, DBCheck> checkMap = checkService.getCheckMapByType(Tag.TRANSITION_CHECK);

        int nbreUpdated = 0;
        int nbreNew = 0;
        int nbreToSuccess = 0;
        Date now = new Date();
        DBTrustedLists tl = tlService.getDbTL(tlId);

        // get existing CHECK RESULT
        List<DBCheckResult> dbChecks = tl.getCheckResults();

        if (dbChecks == null) {
            dbChecks = new ArrayList<>();
        }

        List<CheckResultDTO> newChecks = new ArrayList<>();
        List<CheckResultDTO> tmpChecks = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(currentChecks)) {
            tmpChecks.addAll(currentChecks);
        } else {
            currentChecks = new ArrayList<>();
        }

        if (CollectionUtils.isNotEmpty(dbChecks)) {
            LOGGER.debug("To persist = " + currentChecks.size() + " <---> Existing = " + dbChecks.size());

            // FOR EACH EXISTING RESULT SEE HOW IT EVOLVE
            for (DBCheckResult dbCheck : dbChecks) {
                if (dbCheck.getCheck() == null) {
                    boolean found = false;
                    for (CheckResultDTO tmpCurrentCheck : tmpChecks) {
                        if (checkMatch(dbCheck, tmpCurrentCheck)) {
                            found = true;
                            if (dbCheck.getStatus().equals(tmpCurrentCheck.getStatus())) {
                                long since = 1; //TODO:fix this MAN!
                                dbCheck.setSince(since);
                                boolean removed = currentChecks.remove(tmpCurrentCheck);
                                if (!removed) {
                                    LOGGER.debug("Check not removed :" + tmpCurrentCheck);
                                }
                                nbreUpdated++;
                                break;
                            } else {
                                // ERROR TO SUCCESS
                                dbCheck.setEndDate(now);
                                currentChecks.remove(tmpCurrentCheck);
                                break;
                            }
                        }
                    }

                    if (!found) {
                        LOGGER.debug("Check " + dbCheck.getCheck() + " not found.");
                    }

                }
            }

            LOGGER.debug("tmpResultToAdd (Success to Error) = " + newChecks.size());
            for (CheckResultDTO result : newChecks) {
                CheckStatus resultStatus = result.getStatus();
                DBCheckResult dbResult = getDbCheckResult(tl, now, result, resultStatus, checkMap);
                if (dbResult != null) {
                    dbChecks.add(dbResult);
                }
            }
        }

        if (!CollectionUtils.isEmpty(currentChecks)) {
            for (CheckResultDTO result : currentChecks) {
                CheckStatus resultStatus = result.getStatus();
                if (!resultStatus.equals(CheckStatus.SUCCESS) && !resultStatus.equals(CheckStatus.IGNORE)) {
                    DBCheckResult dbResult = getDbCheckResult(tl, now, result, resultStatus, checkMap);
                    if (dbResult != null) {
                        dbChecks.add(dbResult);
                        nbreNew++;
                    }
                }
            }
        }

        tlService.updateCheckDate(tlId);
        LOGGER.info("Number of new error result : " + nbreNew);
        LOGGER.info("Number of result updated ('since' value) : " + nbreUpdated);
        LOGGER.info("Number of change from error to success : " + nbreToSuccess);

    }

    /**
     * Compare DB check result VS current check result based on ID/Location/Description
     *
     * @param dbCheck
     * @param tmpCurrentCheck
     */
    private boolean checkMatch(DBCheckResult dbCheck, CheckResultDTO tmpCurrentCheck) {
        return (dbCheck.getCheck().getId().equals(tmpCurrentCheck.getCheckId()) && dbCheck.getLocation().equalsIgnoreCase(tmpCurrentCheck.getId())
                && dbCheck.getDescription().equalsIgnoreCase(tmpCurrentCheck.getDescription()));
    }

    private DBCheckResult getDbCheckResult(DBTrustedLists tl, Date startDate, CheckResultDTO result, CheckStatus resultStatus, Map<String, DBCheck> tlccCheckMap) {
        DBCheck dbCheck = tlccCheckMap.get(result.getCheckId());
        if (dbCheck == null) {
            LOGGER.error("CHECK " + result.getCheckId() + " NOT EXIST IN DB");
            return null;
        } else {
            DBCheckResult dbResult = new DBCheckResult();
            dbResult.setCheck(dbCheck);
            dbResult.setTrustedList(tl);
            if (resultStatus.equals(CheckStatus.SUCCESS)) {
                dbResult.setStatus(resultStatus);
            } else {
                dbResult.setStatus(dbCheck.getPriority());
            }
            dbResult.setLocation(result.getId());
            dbResult.setHrLocation(result.getLocation());
            dbResult.setStartDate(startDate);
            if (dbCheck.getType().equals(CheckType.TLCC)) {
                dbResult.setDescription(result.getDescription());
            } else {
                dbResult.setDescription(dbCheck.getDescription());
            }
            dbResult.setSince(0L);
            return dbResult;
        }

    }
}
