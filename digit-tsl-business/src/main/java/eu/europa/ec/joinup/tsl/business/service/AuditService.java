package eu.europa.ec.joinup.tsl.business.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import eu.europa.ec.joinup.tsl.business.dto.NewDTO;
import eu.europa.ec.joinup.tsl.business.dto.audit.Audit;
import eu.europa.ec.joinup.tsl.business.dto.audit.AuditCriteriaDTO;
import eu.europa.ec.joinup.tsl.business.dto.audit.AuditSearchDTO;
import eu.europa.ec.joinup.tsl.business.repository.AuditRepository;
import eu.europa.ec.joinup.tsl.business.util.DateUtils;
import eu.europa.ec.joinup.tsl.model.DBAudit;
import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.AuditAction;
import eu.europa.ec.joinup.tsl.model.enums.AuditStatus;
import eu.europa.ec.joinup.tsl.model.enums.AuditTarget;

/**
 * Manage audit log
 */
@Service
@Transactional(value = TxType.REQUIRED)
public class AuditService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditService.class);

    private static final ResourceBundle bundle = ResourceBundle.getBundle("messages");

    @Autowired
    private AuditRepository auditRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private TLService tlService;

    @Autowired
    private CountryService countryService;

    @Value("${mail.flood.limit}")
    private int floodLimit;

    @PersistenceContext
    private EntityManager em;

    /**
     * Get all audit order by date
     *
     * @return
     */
    public List<Audit> getAllAuditOrderByDateDesc() {
        List<Audit> auditList = new ArrayList<>();
        List<DBAudit> dbList = auditRepository.findAllByOrderByDateDesc();
        for (DBAudit dbAudit : dbList) {
            auditList.add(new Audit(dbAudit));
        }
        return auditList;

    }

    /**
     * Add new audit entry in database
     *
     * @param target
     * @param action
     * @param status
     * @param cc
     * @param fileId
     * @param username
     * @param infos
     * @return
     */
    @Transactional(value = TxType.REQUIRES_NEW)
    public Audit addAuditLog(AuditTarget target, AuditAction action, AuditStatus status, String cc, int fileId, String username, String infos) {
        LOGGER.debug("addAuditLog for " + target);
        DBAudit dbAudit = new DBAudit();

        dbAudit.setTarget(target);
        dbAudit.setAction(action);
        dbAudit.setStatus(status);
        dbAudit.setDate(new Date());
        dbAudit.setCountryCode(cc);
        dbAudit.setUsername(username);
        dbAudit.setInfos(infos);

        if (fileId > 0) {
            dbAudit.setFileId(fileId);
            DBFiles file = fileService.getFileById(fileId);
            if (file != null) {
                dbAudit.setFileDigest(file.getDigest());
            }
        }

        Audit audit = new Audit(auditRepository.save(dbAudit));
        LOGGER.debug("** audit id is " + audit.getId());
        return audit;
    }

    /**
     * Get number of prod tl *NB* created for current day by country and return true if *NB* > floodLimit;
     *
     * <pre>
     * floodLimit : defined in application properties
     * </pre>
     *
     * @param countryCode
     * @return
     */
    public boolean isFloodLimitReach(String countryCode) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 1);
        Date today = c.getTime();

        AuditSearchDTO dto = new AuditSearchDTO();
        dto.setCountryCode(countryCode);
        dto.setTarget(AuditTarget.PROD_TL);
        dto.setAction(AuditAction.CREATE);
        dto.setStartDate(today);

        List<Audit> auditList = searchAuditByCriteria(dto);
        return auditList.size() > floodLimit;
    }

    /**
     * Get trusted list ID by fileId of audit log
     *
     * @param fileId
     * @return
     */
    public Integer findTlIDByAuditXmlFileId(int fileId) {
        DBTrustedLists dbTL = tlService.findByXmlFileId(fileId);
        if (dbTL != null) {
            return dbTL.getId();
        }
        return null;
    }

    /**
     * Get last 3 production trustedList created
     *
     * @return
     */
    public List<NewDTO> getLatestNews() {
        AuditSearchDTO searchDTO = new AuditSearchDTO();
        searchDTO.setTarget(AuditTarget.PROD_TL);
        searchDTO.setAction(AuditAction.CREATE);
        searchDTO.setMaxResult(3);
        List<Audit> audits = searchAuditByCriteria(searchDTO);
        if (CollectionUtils.isEmpty(audits)) {
            return Collections.emptyList();
        }
        return processAuditToNews(audits);
    }

    /**
     * Get last production trusted list created by country/last date
     *
     * @param searchCriteria
     * @return List<NewDTO>
     */
    public List<NewDTO> getFilteredNews(AuditSearchDTO searchDTO) {
        searchDTO.setAction(AuditAction.CREATE);
        searchDTO.setTarget(AuditTarget.PROD_TL);
        List<Audit> audits = searchAuditByCriteria(searchDTO);
        if (CollectionUtils.isEmpty(audits)) {
            return Collections.emptyList();
        }
        return processAuditToNews(audits);
    }

    /**
     * Get audit list and process result to TL news informations
     *
     * @param audits
     * @return List<NewDTO>
     */
    private List<NewDTO> processAuditToNews(List<Audit> audits) {
        List<NewDTO> news = new ArrayList<>();
        DBCountries country;
        DBTrustedLists dbTl;
        String infos;
        String tlInfo;
        for (Audit audit : audits) {
            country = countryService.getCountryByTerritory(audit.getCountryCode());
            dbTl = tlService.findByXmlFileId(audit.getFileId());
            if ((country != null) && (dbTl != null)) {
                infos = bundle.getString("news.prod.create").replace("%CC%", country.getCountryName());
                tlInfo = country.getCodeTerritory() + " (Sn" + dbTl.getSequenceNumber() + ")";
                news.add(new NewDTO(audit.getDate(), country.getCodeTerritory(), country.getCountryName(), infos, tlInfo, dbTl.getId()));
            } else {
                LOGGER.error("Get News, Country or DBTrustedList not found for audit : " + audit);
            }
            country = null;
            dbTl = null;
            infos = null;
        }
        return news;
    }

    /**
     * Get audit by criteria (all fields are optionals)
     *
     * <pre>
     * AuditSearchDTO :
     * - Country Code
     * - Target
     * - Action
     * - Date
     * - MaxResult
     * </pre>
     *
     * @param auditSearch
     * @return search result
     */
    public List<Audit> searchAuditByCriteria(AuditSearchDTO auditSearch) {
        List<Audit> auditList = new ArrayList<>();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<DBAudit> criteriaQuery = criteriaBuilder.createQuery(DBAudit.class);
        Root<DBAudit> criteriaRoot = criteriaQuery.from(DBAudit.class);
        List<Predicate> predicates = new ArrayList<>();
        if (auditSearch != null) {
            if (!StringUtils.isEmpty(auditSearch.getCountryCode())) {
                predicates.add(criteriaBuilder.equal(criteriaRoot.get("countryCode"), auditSearch.getCountryCode()));
            }
            if (auditSearch.getAction() != null) {
                predicates.add(criteriaBuilder.equal(criteriaRoot.get("action"), auditSearch.getAction()));
            }
            if (auditSearch.getTarget() != null) {
                predicates.add(criteriaBuilder.equal(criteriaRoot.get("target"), auditSearch.getTarget()));
            }
            if (auditSearch.getStartDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(criteriaRoot.<Date> get("date"), DateUtils.getStartOfDay(auditSearch.getStartDate())));
            }
            if (auditSearch.getEndDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(criteriaRoot.<Date> get("date"), DateUtils.getEndOfDay(auditSearch.getEndDate())));
            }
        }
        criteriaQuery.select(criteriaRoot).where(predicates.toArray(new Predicate[] {})).orderBy(criteriaBuilder.desc(criteriaRoot.get("date")));
        List<DBAudit> entity = new ArrayList<>();
        assert auditSearch != null;
        if (auditSearch.getMaxResult() > 0) {
            entity = em.createQuery(criteriaQuery).setMaxResults(auditSearch.getMaxResult()).getResultList();
        } else {
            entity = em.createQuery(criteriaQuery).getResultList();
        }

        for (DBAudit dbAudit : entity) {
            auditList.add(new Audit(dbAudit));
        }
        return auditList;
    }

    /**
     * Init criterias list that can be used for criteria search
     *
     * @return
     */
    public AuditCriteriaDTO initCriteria() {
        return new AuditCriteriaDTO(countryService.getAllCountryCode());
    }

}
