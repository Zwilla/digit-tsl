package eu.europa.ec.joinup.tsl.business.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.CheckDTO;
import eu.europa.ec.joinup.tsl.business.dto.CheckResultDTO;
import eu.europa.ec.joinup.tsl.business.repository.CheckRepository;
import eu.europa.ec.joinup.tsl.business.repository.ResultRepository;
import eu.europa.ec.joinup.tsl.model.DBCheck;
import eu.europa.ec.joinup.tsl.model.DBCheckResult;
import eu.europa.ec.joinup.tsl.model.enums.CheckStatus;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

@Service
@Transactional(value = TxType.REQUIRED)
public class CheckService {

    @Autowired
    private CheckRepository checkRepository;

    @Autowired
    private ResultRepository resultRepository;

    private static final List<CheckStatus> errorStatus = new ArrayList<>(Arrays.asList(CheckStatus.ERROR, CheckStatus.WARNING, CheckStatus.INFO));

    /**
     * Get all database checks and store in cache
     */
    @Cacheable(value = "checkCache", key = "#root.methodName")
    public List<CheckDTO> getAll() {
        List<CheckDTO> list = new ArrayList<>();
        List<DBCheck> dbList = checkRepository.findAllByOrderByDescription();
        for (DBCheck dbCheck : dbList) {
            list.add(new CheckDTO(dbCheck));
        }
        return list;
    }

    /**
     * Get DBDCheck by ID
     * 
     * @param id
     */
    public DBCheck getCheckById(String id) {
        return checkRepository.findOne(id);
    }

    /**
     * Get check by @Target and stored in cache
     *
     * @param target
     */
    @Cacheable(value = "checkCache", key = "#target")
    public List<CheckDTO> getTarget(Tag target) {
        List<CheckDTO> list = new ArrayList<>();
        List<DBCheck> dbList = checkRepository.findByTarget(target);
        for (DBCheck dbCheck : dbList) {
            list.add(new CheckDTO(dbCheck));
        }
        return list;
    }

    /**
     * Edit database check
     *
     * @param check
     */
    public DBCheck edit(CheckDTO check) {
        DBCheck checkToEdit = checkRepository.findOne(check.getId());
        checkToEdit.setPriority(check.getStatus());
        if (StringUtils.isNotEmpty(check.getTranslation())) {
            checkToEdit.setTranslation(check.getTranslation());
        } else {
            checkToEdit.setTranslation(null);
        }
        if (StringUtils.isNotEmpty(check.getStandardReference())) {
            checkToEdit.setStandardReference(check.getStandardReference());
        } else {
            checkToEdit.setStandardReference(null);
        }
        return checkRepository.save(checkToEdit);
    }

    /**
     * Delete check results by @DBCheck
     *
     * @param check
     */
    public void deleteCheckResult(DBCheck check) {
        List<DBCheckResult> resultList = resultRepository.findByCheck(check);
        resultRepository.delete(resultList);
    }

    @Transactional(value = TxType.REQUIRES_NEW)
    public void deleteByTrustedListId(int tlId) {
        resultRepository.deleteByTrustedListId(tlId);
    }

    /**
     * @param id
     * @param onlyError
     * @param allResult
     *            - if false, get only 'current' result (No historic)
     * @return
     */
    public List<CheckDTO> getTLChecks(int id) {
        List<CheckDTO> results = new ArrayList<>();
        List<DBCheckResult> dbCheckResultList = resultRepository.findByTrustedListIdAndStatusIn(id, errorStatus);
        for (DBCheckResult dbResult : dbCheckResultList) {
            results.add(new CheckDTO(dbResult));
        }
        return results;
    }

    /**
     * Create DBCheck map by tag
     *
     * @return Map<CheckID, DBCheck>
     */
    public Map<String, DBCheck> getCheckMapByType(Tag tag) {
        return initCheckMap(checkRepository.findByTarget(tag));
    }

    private Map<String, DBCheck> initCheckMap(List<DBCheck> tlccDBCheck) {
        Map<String, DBCheck> checkMap = new HashMap<>();
        if (tlccDBCheck != null) {
            for (DBCheck check : tlccDBCheck) {
                checkMap.put(check.getId(), check);
            }
        }
        return checkMap;
    }

    public List<CheckResultDTO> getTLChecksResuls(int tlId) {
        List<CheckResultDTO> results = new ArrayList<>();
        List<DBCheckResult> dbCheckResultList = resultRepository.findByTrustedListIdAndStatusIn(tlId, errorStatus);
        for (DBCheckResult dbResult : dbCheckResultList) {
            results.add(new CheckResultDTO(dbResult));
        }
        return results;
    }

}
