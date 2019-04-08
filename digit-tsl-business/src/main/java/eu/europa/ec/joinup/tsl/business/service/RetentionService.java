/*******************************************************************************
 * DIGIT-TSL - Trusted List Manager
 * Copyright (C) 2018 European Commission, provided under the CEF E-Signature programme
 *  
 * This file is part of the "DIGIT-TSL - Trusted List Manager" project.
 *  
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 *  
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
package eu.europa.ec.joinup.tsl.business.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.data.retention.CronRetention;
import eu.europa.ec.joinup.tsl.business.dto.data.retention.DraftStoreRetentionDTO;
import eu.europa.ec.joinup.tsl.business.dto.data.retention.RetentionAlertDTO;
import eu.europa.ec.joinup.tsl.business.dto.data.retention.RetentionCriteriaDTO;
import eu.europa.ec.joinup.tsl.business.dto.data.retention.RetentionTarget;
import eu.europa.ec.joinup.tsl.business.dto.data.retention.TrustedListRetentionDTO;
import eu.europa.ec.joinup.tsl.business.util.CronUtils;
import eu.europa.ec.joinup.tsl.business.util.DateUtils;
import eu.europa.ec.joinup.tsl.model.DBDraftStore;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.AuditAction;
import eu.europa.ec.joinup.tsl.model.enums.AuditStatus;
import eu.europa.ec.joinup.tsl.model.enums.AuditTarget;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;

/**
 * Retention policy manager (get data, clean, retention job)
 */
@Service
@Transactional
public class RetentionService {

    private static ResourceBundle bundle = ResourceBundle.getBundle("messages");

    @Autowired
    private DraftStoreService draftStoreService;

    @Autowired
    private TLService tlService;

    @Autowired
    private AuditService auditService;

    @Autowired
    private AbstractAlertingService alertingService;

    @PersistenceContext
    private EntityManager em;

    @Value("${cron.retention.job}")
    private String cronRetentionJob;

    /**
     * Delete a draftStore with all the TL linked
     *
     * @param draftStoreRetention
     */
    public void cleanDraftStore(DraftStoreRetentionDTO draftStoreRetention) {
        for (TrustedListRetentionDTO tl : draftStoreRetention.getTls()) {
            tlService.deleteDraft(tl.getId());
        }
        draftStoreService.deleteDraftStore(draftStoreRetention.getDraftStoreId());
    }

    /**
     * Delete a trusted list
     *
     * @param trustedList
     */
    public void cleanTrustedlist(TrustedListRetentionDTO trustedList) {
        DBTrustedLists dbTL = tlService.getDbTL(trustedList.getId());
        if (dbTL.getStatus().equals(TLStatus.PROD) && !dbTL.isArchive()) {
            throw new IllegalStateException(bundle.getString(""));
        }
        tlService.deleteDraft(dbTL);
    }

    /**
     * Get Draftstore and linked TL that haven't been accessed since a @Date
     *
     * @param date
     */
    public List<DraftStoreRetentionDTO> getDraftStoreRetentionSince(Date date) {
        List<DBDraftStore> dbDraftStores = draftStoreService.getDraftStoreNotVerifiedSince(date);
        List<DraftStoreRetentionDTO> retentionResults = new ArrayList<>();
        for (DBDraftStore dbDS : dbDraftStores) {
            retentionResults.add(new DraftStoreRetentionDTO(dbDS));
        }
        return retentionResults;
    }

    /**
     * Search for data that will be deleted on the next retention cron iteration (Draftstores + Draft TLs)
     */
    public CronRetention searchNextCronRetention() {
        CronRetention cronRetentionDTO = new CronRetention();
        // Next execution
        Date nextCron = CronUtils.getDateFromExpression(new Date(), cronRetentionJob);

        // Last access date
        Calendar c = Calendar.getInstance();
        c.setTime(nextCron);
        c.add(Calendar.MONTH, -2);
        Date lastAccessDate = c.getTime();

        cronRetentionDTO.setLastAccessDate(lastAccessDate);
        cronRetentionDTO.setNextCron(nextCron);

        cronRetentionDTO.setDraftstores(getDraftStoreRetentionSince(lastAccessDate));

        RetentionCriteriaDTO draftTLCriteria = new RetentionCriteriaDTO(RetentionTarget.DRAFT, lastAccessDate);
        List<DraftStoreRetentionDTO> draftTL = mapDBTLinDTO(draftTLCriteria, findCriteriaBuilder(draftTLCriteria));
        if (!CollectionUtils.isEmpty(draftTL)) {
            cronRetentionDTO.setDraftTL(draftTL.get(0));
        }

        return cronRetentionDTO;
    }

    /**
     * Search for specific data by @RetentionType and/or @Date; if date is null get current date. DraftStore : based on lastVerificationDate;
     * Production TL : based on XML file firstScanDate; Draft TL : based on lastAccessDate
     *
     * @param retentionCriteria
     */
    public List<DraftStoreRetentionDTO> searchRetentionData(RetentionCriteriaDTO retentionCriteria) throws IllegalArgumentException {
        if ((retentionCriteria == null) || (retentionCriteria.getTarget() == null)) {
            throw new IllegalArgumentException(bundle.getString("error.retention.param.missing"));
        }
        switch (retentionCriteria.getTarget()) {
        case DRAFTSTORE:
            if (retentionCriteria.getDate() == null) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MONTH, -2);
                Date twoMonthAgo = cal.getTime();
                return getDraftStoreRetentionSince(DateUtils.getEndOfDay(twoMonthAgo));
            } else {
                return getDraftStoreRetentionSince(DateUtils.getEndOfDay(retentionCriteria.getDate()));
            }
        case DRAFT:
        case PRODUCTION:
            return mapDBTLinDTO(retentionCriteria, findCriteriaBuilder(retentionCriteria));
        default:
            throw new IllegalStateException(bundle.getString("error.retention.type.default"));
        }
    }

    /**
     * Create a "container" @DraftStoreRetentionDTO entry with @TLType trusted lists
     *
     * @param criteria
     * @param dbTLs
     * @return
     */
    private List<DraftStoreRetentionDTO> mapDBTLinDTO(RetentionCriteriaDTO criteria, List<DBTrustedLists> dbTLs) {
        DraftStoreRetentionDTO container = new DraftStoreRetentionDTO(criteria, dbTLs);
        return new ArrayList<>(Arrays.asList(container));
    }

    /**
     * Find List<@DBTrustedLists> by @RetentionCriteriaDTO
     *
     * @param criteriaDTO
     */
    public List<DBTrustedLists> findCriteriaBuilder(RetentionCriteriaDTO criteriaDTO) {
        if (criteriaDTO == null) {
            throw new IllegalArgumentException(bundle.getString("error.retention.param.missing"));
        } else {
            CriteriaBuilder builder = em.getCriteriaBuilder();

            CriteriaQuery<DBTrustedLists> query = builder.createQuery(DBTrustedLists.class);
            Root<DBTrustedLists> root = query.from(DBTrustedLists.class);

            List<Predicate> predicates = new ArrayList<>();

            Path<Date> criteriaDate = null;
            if (criteriaDTO.getTarget() == null) {
                throw new IllegalArgumentException(bundle.getString("error.retention.param.missing"));
            } else if (criteriaDTO.getTarget().equals(RetentionTarget.DRAFT)) {
                criteriaDate = root.get("lastAccessDate");
            } else if (criteriaDTO.getTarget().equals(RetentionTarget.PRODUCTION)) {
                criteriaDate = root.get("xmlFile").get("firstScanDate");
            } else {
                throw new IllegalStateException(bundle.getString("error.retention.type.default"));
            }

            // Target
            if (criteriaDTO.getTarget().equals(RetentionTarget.DRAFT)) {
                predicates.add(builder.equal(root.get("status"), TLStatus.DRAFT));
            } else if (criteriaDTO.getTarget().equals(RetentionTarget.PRODUCTION)) {
                predicates.add(builder.equal(root.get("status"), TLStatus.PROD));
            }
            // Date
            if (criteriaDTO.getDate() != null) {
                predicates.add(builder.lessThanOrEqualTo(criteriaDate, DateUtils.getEndOfDay(criteriaDTO.getDate())));
            }
            // Territory Code
            if (!StringUtils.isEmpty(criteriaDTO.getTerritoryCode())) {
                predicates.add(builder.equal(root.get("territory").get("codeTerritory"), criteriaDTO.getTerritoryCode()));
            }
            // Order By
            List<Order> orderList = new ArrayList<>();
            orderList.add(builder.desc(criteriaDate));

            // Select where @predicates order by @criteriaDate
            query.select(root).where(predicates.toArray(new Predicate[] {})).orderBy(orderList);
            return em.createQuery(query).getResultList();
        }

    }

    /**
     * Clean Draftstore not accessed since two months with all TL attached & clean trustedList older than 2 months even if draftstore still accessed
     */
    public void retentionClean() {
        // Init params
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -2);
        Date twoMonthAgo = cal.getTime();

        int nbDraftStoreDeleted = 0;
        int nbTrustedListDeleted = 0;

        // Get draftstore(s) older than 2 months and delete
        List<DraftStoreRetentionDTO> draftStoreRetention = getDraftStoreRetentionSince(twoMonthAgo);
        // Clean
        if (!CollectionUtils.isEmpty(draftStoreRetention)) {
            for (DraftStoreRetentionDTO dsRetention : draftStoreRetention) {
                nbTrustedListDeleted = nbTrustedListDeleted + dsRetention.getTls().size();
                cleanDraftStore(dsRetention);
                nbDraftStoreDeleted = nbDraftStoreDeleted + 1;
            }
        }

        // Get trustedlist(s) older than 2 months and delete
        RetentionCriteriaDTO retentionCriteria = new RetentionCriteriaDTO();
        retentionCriteria.setTarget(RetentionTarget.DRAFT);
        retentionCriteria.setDate(twoMonthAgo);
        List<DraftStoreRetentionDTO> trustedListRetention = searchRetentionData(retentionCriteria);
        if ((trustedListRetention != null) && !CollectionUtils.isEmpty(trustedListRetention.get(0).getTls())) {
            for (TrustedListRetentionDTO tlRetention : trustedListRetention.get(0).getTls()) {
                cleanTrustedlist(tlRetention);
                nbTrustedListDeleted = nbTrustedListDeleted + 1;
            }

        }

        RetentionAlertDTO retentionAlert = new RetentionAlertDTO(draftStoreService.countDraftStore(), tlService.countDraftTL(), nbDraftStoreDeleted, nbTrustedListDeleted);

        alertingService.sendRetentionJobReport(retentionAlert);

        auditService.addAuditLog(AuditTarget.JOBS, AuditAction.UPDATE, AuditStatus.SUCCES, "", 0, "SYSTEM",
                "Retention service clean " + nbDraftStoreDeleted + " draftstore and " + nbTrustedListDeleted + " trusted list.");

    }

}
