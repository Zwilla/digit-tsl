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

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.Load;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.AuditAction;
import eu.europa.ec.joinup.tsl.model.enums.AuditStatus;
import eu.europa.ec.joinup.tsl.model.enums.AuditTarget;
import eu.europa.ec.joinup.tsl.model.enums.MimeType;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;
import eu.europa.esig.dss.client.http.DataLoader;
import eu.europa.esig.jaxb.tsl.TrustStatusListType;

/**
 * Trusted list loader Utils
 */
@Service
public class TLLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(TLLoader.class);

    @Autowired
    private DataLoader dataLoader;

    @Autowired
    private TLService tlService;

    @Autowired
    private FileService fileService;

    @Autowired
    private AvailabilityService availibilityService;

    @Autowired
    private TrustedListJaxbService jaxbService;

    @Autowired
    private TLValidator tlValidator;

    @Autowired
    private RulesRunnerService rulesRunner;

    @Autowired
    private AuditService auditService;

    @Autowired
    private AbstractAlertingService alertingService;

    @Autowired
    private TLDataLoaderService tlDataLoaderService;

    @Value("${number.of.reload:5}")
    private int numberOfReload;

    @Value("${timeout.of.reload:500}")
    private long timeoutOfReload;

    @Transactional(value = TxType.REQUIRES_NEW)
    public TrustStatusListType loadTL(String countryCode, String xmlUrl, TLType type, TLStatus status, Load loadObj) {
        TrustStatusListType jaxbTL = null;

        // Get current TL or create a new entry
        DBTrustedLists tl = tlService.getOrCreateTL(countryCode, xmlUrl, type, status);
        loadObj.setTlId(tl.getId());

        // Download XML file from URL and compare current digest with new downloaded digest
        DBFiles xmlFile = tl.getXmlFile();
        byte[] xmlBinaries = loadFile(xmlFile);
        String xmlDigest = TLUtils.getSHA2(xmlBinaries);
        boolean isNewXML = (ArrayUtils.isNotEmpty(xmlBinaries) && !StringUtils.equals(xmlDigest, xmlFile.getDigest()));
        LOGGER.debug("Digest found for the trusted list from " + countryCode + " is different from current one : " + isNewXML);
        loadObj.setNew(isNewXML);

        boolean isNotFirstLoading = xmlFile.getFirstScanDate() != null;

        // Retrieve PROD trusted lists with a similar XML digest from database
        List<DBTrustedLists> matchingTL = tlService.findProdSimilarDigest(xmlDigest);

        // Verify if the new version is not the result of cache issue
        if (isNewXML && isNotFirstLoading) {
            Boolean cacheIssue = false;
            if (!CollectionUtils.isEmpty(matchingTL)) {
                // At least one PROD trusted list has the same digest => newest loaded trusted list is an previous version
                byte[] tmpFile = null;
                String tmpDigest = null;
                for (int i = 1; i <= numberOfReload; i++) {
                    try {
                        tmpFile = dataLoader.get(xmlFile.getUrl(), true);
                        tmpDigest = TLUtils.getSHA2(tmpFile);
                        if ((ArrayUtils.isNotEmpty(tmpFile) && !StringUtils.equals(tmpDigest, xmlDigest))) {
                            // Different version found
                            LOGGER.error("Different version of the newest loaded trusted list from " + countryCode + " found on the " + i + " iteration. False new TL detected.");
                            LOGGER.error("Newest loaded TL digest --> " + xmlDigest);
                            LOGGER.error("Last iteration digest --> " + tmpDigest);
                            cacheIssue = true;
                            alertingService.sendTLCacheIssue(countryCode, xmlDigest, tmpDigest, i);
                            break;
                        }
                        Thread.sleep(timeoutOfReload);
                    } catch (InterruptedException e) {
                        LOGGER.error("Error during trusted list " + countryCode + " reload n°" + i, e);
                    }

                }
            }

            if (cacheIssue) {
                isNewXML = false;
            } else {
                // Archive TL and create a new entry
                LOGGER.info("New trusted list version from " + countryCode + " confirmed. Digest --> " + xmlDigest);
                tlService.archive(tl, true);
                auditService.addAuditLog(AuditTarget.PROD_TL, AuditAction.ARCHIVE, AuditStatus.SUCCES, countryCode, xmlFile.getId(), "SYSTEM", "CLASS:TLLoader.LOADTL_ARCHIVETL,TLID:" + tl.getId());
                tl = tlService.getOrCreateTL(countryCode, xmlUrl, type, status);
                loadObj.setTlId(tl.getId());
            }
        }

        xmlFile = tl.getXmlFile();
        xmlFile.setLastScanDate(new Date());
        if (isNewXML) {
            xmlFile.setDigest(xmlDigest);
            xmlFile.setFirstScanDate(new Date());

            // Check if digest match an existing file
            if (CollectionUtils.isEmpty(matchingTL)) {
                // No digest match => Store the new file
                xmlFile.setLocalPath(fileService.storeNewTL(xmlFile, xmlBinaries, countryCode));
            } else {
                // Digest match => file already stored on disk
                xmlFile.setLocalPath(matchingTL.get(0).getXmlFile().getLocalPath());
            }

            auditService.addAuditLog(AuditTarget.PROD_TL, AuditAction.CREATE, AuditStatus.SUCCES, countryCode, xmlFile.getId(), "SYSTEM",
                    "CLASS:TLLoader.LOADTL_CREATETL,TLID:" + tl.getId() + ",NEWXML:" + isNewXML + ",XMLDIGEST --> " + xmlDigest);
        }

        try {
            if ((xmlFile != null) && StringUtils.isNotEmpty(xmlFile.getLocalPath())) {
                File tslFile = fileService.getTSLFile(xmlFile);
                jaxbTL = jaxbService.unmarshallTSL(tslFile);
                tl.setIssueDate(TLUtils.toDate(jaxbTL.getSchemeInformation().getListIssueDateTime()));
                tl.setSequenceNumber(jaxbTL.getSchemeInformation().getTSLSequenceNumber().intValue());
                if (jaxbTL.getSchemeInformation().getNextUpdate() != null) {
                    tl.setNextUpdateDate(TLUtils.toDate(jaxbTL.getSchemeInformation().getNextUpdate().getDateTime()));
                }
                tl.setVersionIdentifier(tlService.extractVersionFromFile(xmlFile));
            }
        } catch (Exception e) {
            LOGGER.error("Unable to parse TSL for country " + countryCode + " : " + e.getMessage(), e);
        }
        return jaxbTL;
    }

    private byte[] loadFile(DBFiles file) {
        byte[] byteArray = null;
        try {
            if (StringUtils.isEmpty(file.getUrl())) {
                availibilityService.setUnavailable(file);
                availibilityService.triggerAlerting(file.getId());
                return null;
            } else {
                byteArray = dataLoader.get(file.getUrl(), true);
            }
        } catch (Exception e) {
            LOGGER.warn("Unable to load '" + file.getUrl() + "' : " + e.getMessage());
            availibilityService.setUnavailable(file);
            availibilityService.triggerAlerting(file.getId());
            return null;
        }
        // TODO(5.4.RC1): TDEV-818
        if (isTslContent(file, byteArray) || isPdfContent(file.getMimeTypeFile(), byteArray) || isSHA2Content(file.getMimeTypeFile(), byteArray)) {
            availibilityService.setAvailable(file);
        } else {
            availibilityService.setUnsupported(file);
            availibilityService.triggerAlerting(file.getId());
            return null;
        }

        return byteArray;
    }

    private boolean isPdfContent(MimeType mimeType, byte[] content) {
        return MimeType.PDF.equals(mimeType) && TLUtils.isPdf(content);
    }

    private boolean isTslContent(DBFiles file, byte[] content) {
        return MimeType.XML.equals(file.getMimeTypeFile()) && TLUtils.isXml(file.getUrl(), content) && isJaxbProcessableXml(file.getUrl(), content);
    }

    private boolean isSHA2Content(MimeType mimeType, byte[] content) {
        return MimeType.SHA2.equals(mimeType) && TLUtils.isHex(content);
    }

    private boolean isJaxbProcessableXml(String url, byte[] content) {
        try {
            TrustStatusListType jaxbTL = jaxbService.unmarshallTSL(content);
            return jaxbTL != null;
        } catch (Exception e) {
            LOGGER.debug("Unable to unmarshall (url:" + url + ") : " + e.getMessage());
            return false;
        }

    }

    @Transactional(value = TxType.REQUIRES_NEW)
    public void check(Load load) {
        if (load.getTlId() > 0) {

            DBTrustedLists dbTlOld = tlService.getDbTL(load.getTlId());
            DBTrustedLists dbTl = tlService.getPublishedDbTLByCountry(dbTlOld.getTerritory());
            LOGGER.debug("check on " + dbTl.getId() + "[ " + dbTl.getName() + "]");
            TL current = tlService.getPublishedTLByCountry(dbTl.getTerritory());
            if ((current != null) && (current.getTlId() > 0)) {
                TL previous = tlService.getPreviousProduction(dbTl.getTerritory());
                rulesRunner.runAllRules(current, previous);
                auditService.addAuditLog(AuditTarget.PROD_TL, AuditAction.CHECKCONFORMANCE, AuditStatus.SUCCES, dbTl.getTerritory().getCodeTerritory(), dbTl.getXmlFile().getId(), "SYSTEM",
                        "CLASS:TLLoader.CHECK,NAME:" + dbTl.getName() + ",XMLFILEID:" + dbTl.getXmlFile().getId() + ",XMLDIGEST:" + dbTl.getXmlFile().getDigest());
            }
        }
    }

    @Transactional(value = TxType.REQUIRES_NEW)
    public void checkSigLOTL(Load load) {
        if (load.getTlId() > 0) {
            DBTrustedLists dbTlOld = tlService.getDbTL(load.getTlId());
            DBTrustedLists dbTl = tlService.getPublishedDbTLByCountry(dbTlOld.getTerritory());
            LOGGER.debug("checkSigLOTL on " + dbTl.getId() + "[ " + dbTl.getName() + "]");
            tlValidator.checkLOTL(dbTl);
        }
    }

    @Transactional(value = TxType.REQUIRES_NEW)
    public void updateLOTLCertificates() {
        tlDataLoaderService.updateLOTLCertificates();
    }

    @Transactional(value = TxType.REQUIRES_NEW)
    public void updateLOTLLocation() {
        tlDataLoaderService.updateLOTLLocation();
    }

    @Transactional(value = TxType.REQUIRES_NEW)
    public void checkSig(Load load) {
        DBTrustedLists dbTlOld = tlService.getDbTL(load.getTlId());
        DBTrustedLists dbTl = tlService.getPublishedDbTLByCountry(dbTlOld.getTerritory());
        LOGGER.debug("checkSig on " + dbTl.getId() + "[ " + dbTl.getName() + "]");
        tlValidator.checkTLorLOTLWithCurrentProdLOTL(dbTl);
    }

    /**
     * Update DB_SERVICES/DB_CERTIFICATES of current @Load trusted list
     */
    @Transactional(value = TxType.REQUIRES_NEW)
    public void loadServiceList(Load load) {
        if (load.getTlId() > 0) {
            DBTrustedLists dbTl = tlService.getDbTL(load.getTlId());
            if (dbTl == null) {
                LOGGER.error("Loading Job, loadServiceList, trusted list is null for ID :" + load.getTlId());
            } else {
                tlDataLoaderService.updateTrustedListData(dbTl.getTerritory());
            }
        }
    }

}
