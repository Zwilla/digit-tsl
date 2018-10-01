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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.XmlMappingException;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.TLArchivedDTO;
import eu.europa.ec.joinup.tsl.business.dto.TLLightReport;
import eu.europa.ec.joinup.tsl.business.dto.TLSignature;
import eu.europa.ec.joinup.tsl.business.dto.TrustedListsReport;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.business.util.FileUtils;
import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.DBDraftStore;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBFilesAvailability;
import eu.europa.ec.joinup.tsl.model.DBNotification;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.AuditAction;
import eu.europa.ec.joinup.tsl.model.enums.AuditStatus;
import eu.europa.ec.joinup.tsl.model.enums.AuditTarget;
import eu.europa.ec.joinup.tsl.model.enums.AvailabilityStatus;
import eu.europa.ec.joinup.tsl.model.enums.CheckStatus;
import eu.europa.ec.joinup.tsl.model.enums.ColumnName;
import eu.europa.ec.joinup.tsl.model.enums.MimeType;
import eu.europa.ec.joinup.tsl.model.enums.NotificationStatus;
import eu.europa.ec.joinup.tsl.model.enums.SignatureStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;
import eu.europa.esig.dss.DSSDocument;
import eu.europa.esig.jaxb.tsl.TrustStatusListType;
import eu.europa.esig.jaxb.v5.tsl.TrustStatusListTypeV5;

/**
 * Trusted list management functions
 */
@Service
@Transactional(value = TxType.REQUIRED)
public class TLService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TLService.class);

    private static ResourceBundle bundle = ResourceBundle.getBundle("messages");

    @Autowired
    private TLRepository tlRepository;

    @Autowired
    private CountryService countryService;

    @Autowired
    private FileService fileService;

    @Autowired
    private TrustedListJaxbService jaxbService;

    @Autowired
    private TLBuilder tlBuilder;

    @Autowired
    private ContactService contactService;

    @Autowired
    private BackboneColumnsService systemService;

    @Autowired
    private DraftStoreService draftStoreService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AuditService auditService;

    @Autowired
    private AbstractAlertingService alertingService;

    @Value("${lotl.territory}")
    private String lotlTerritory;

    /* ----- ----- Get trusted lists (prod/draft) ----- ----- */

    /**
     * Get trust backbone informations and init field depend on @DBColumnAvailable database values
     */
    public List<TrustedListsReport> getAllProdTlReports() {
        List<TrustedListsReport> list = new ArrayList<>();
        List<DBTrustedLists> lst = tlRepository.findByStatusAndArchiveFalseOrderByNameAsc(TLStatus.PROD);
        Boolean isContactVisible = systemService.getColumnVisible(ColumnName.CONTACTS.toString());
        for (DBTrustedLists trustedListsDB : lst) {
            try {
                TrustedListsReport rt = tlReport(trustedListsDB, isContactVisible);
                list.add(rt);
            } catch (Exception e) {
                LOGGER.error("Loading trustbackbone error.", e);
            }
        }
        return list;
    }

    /**
     * Get draft trusted lists of @draftStoreId
     *
     * @param draftStoreId
     */
    public List<TrustedListsReport> getDraft(String draftStoreId) {
        DBDraftStore ds = draftStoreService.findOne(draftStoreId);
        draftStoreService.updateLastVerificationDate(ds);
        List<TrustedListsReport> list = new ArrayList<>();
        if (ds != null) {
            List<DBTrustedLists> lst = ds.getDraftList();
            for (DBTrustedLists trustedListsDB : lst) {
                TrustedListsReport rt = tlReport(trustedListsDB, false);
                list.add(rt);
            }
        }
        return list;
    }

    /**
     * Get trust backbone light information without all the TL informations
     */
    public List<TLLightReport> getLightBackbone() {
        List<TLLightReport> backbone = new ArrayList<>();
        List<DBTrustedLists> dbTLs = tlRepository.findByStatusAndArchiveFalseOrderByNameAsc(TLStatus.PROD);

        SignatureStatus signature;
        Date firstScanDate;
        for (DBTrustedLists dbTL : dbTLs) {
            AvailabilityStatus availability = null;
            DBFiles xmlFile = dbTL.getXmlFile();

            signature = null;
            firstScanDate = null;
            if (xmlFile != null) {
                if ((xmlFile.getSignatureInformation() != null) && (xmlFile.getSignatureInformation().getIndication() != null)) {
                    signature = xmlFile.getSignatureInformation().getIndication();
                }
                firstScanDate = xmlFile.getFirstScanDate();

                List<DBFilesAvailability> aList = xmlFile.getAvailabilityInfos();
                if (!CollectionUtils.isEmpty(aList)) {
                    availability = aList.get(0).getStatus();
                }
            } else {
                signature = SignatureStatus.FILE_NOT_FOUND;
            }
            backbone.add(new TLLightReport(dbTL, availability, signature, firstScanDate));
        }
        return backbone;
    }

    /**
     * Get all DB TrustedList not archived order by status(desc)
     */
    public List<DBTrustedLists> findTLNotArchived() {
        return tlRepository.findAllByArchiveFalseOrderByStatusDesc();
    }

    /**
     * Get all archived trusted list by given @territory
     *
     * @param territory
     */
    public List<TLArchivedDTO> getAllPreviousProduction(DBCountries territory) {
        List<DBTrustedLists> archiveds = tlRepository.findByTerritoryAndStatusAndArchiveTrueOrderByIdDesc(territory, TLStatus.PROD);
        if (CollectionUtils.isNotEmpty(archiveds)) {
            Set<String> tlDigests = new HashSet<>();
            List<TLArchivedDTO> tlArchiveds = new ArrayList<>();
            for (DBTrustedLists dbTl : archiveds) {
                if ((dbTl.getXmlFile() != null) && (dbTl.getXmlFile().getDigest() != null)) {
                    String tmpDigest = dbTl.getXmlFile().getDigest();
                    if (!tlDigests.contains(tmpDigest)) {
                        tlDigests.add(tmpDigest);
                        tlArchiveds.add(new TLArchivedDTO(dbTl));
                    }
                }
            }
            return tlArchiveds;
        }
        return Collections.emptyList();
    }

    /**
     * Init TrustedListsReport from @DBTrustedLists entry
     *
     * @param trustedListsDB
     * @param isContactVisible
     *            (show/hide contact)
     * @return
     */
    private TrustedListsReport tlReport(DBTrustedLists trustedListsDB, Boolean isContactVisible) {
        TrustedListsReport rt = new TrustedListsReport();

        DBFiles xmlFile = trustedListsDB.getXmlFile();
        if (xmlFile != null) {
            if (trustedListsDB.getCheckStatus() != null) {
                if (trustedListsDB.getCheckStatus().equals(CheckStatus.ERROR)) {
                    rt.setCheckStatus(trustedListsDB.getCheckStatus());
                } else {
                    rt.setCheckStatus(TLUtils.getTLStatus(trustedListsDB.getCheckStatus(), xmlFile));
                }
            }

            List<DBFilesAvailability> aList = xmlFile.getAvailabilityInfos();
            if (!CollectionUtils.isEmpty(aList)) {
                rt.setAvailability(aList.get(0).getStatus());
            }

            if (xmlFile.getSignatureInformation() != null) {
                rt.setSigStatus(xmlFile.getSignatureInformation().getIndication());
            }
            rt.setLastScanDate(xmlFile.getLastScanDate());

        }

        rt.setIssueDate(trustedListsDB.getIssueDate());
        rt.setId(trustedListsDB.getId());
        rt.setName(trustedListsDB.getName());
        rt.setArchive(trustedListsDB.isArchive());
        rt.setNextUpdateDate(trustedListsDB.getNextUpdateDate());
        rt.setSequenceNumber(trustedListsDB.getSequenceNumber());
        rt.setCheckToRun(trustedListsDB.getCheckToRun());
        rt.setTlType(trustedListsDB.getType());
        DBCountries country = trustedListsDB.getTerritory();
        if (country != null) {
            rt.setTerritoryCode(country.getCodeTerritory());
            rt.setCountryName(country.getCountryName());
        }
        rt.setTlStatus(trustedListsDB.getStatus());

        if (isContactVisible) {
            rt.setContact(contactService.getAllContactByTerritory(rt.getTerritoryCode()));
        }

        return rt;
    }

    /* ----- ----- Get LOTL ----- ----- */

    /**
     * Get production LOTL as JaxB format
     */
    public TrustStatusListType getLOTLProductionJaxb() {
        TrustStatusListType unmarshallTSL = null;
        DBTrustedLists lotlProd = tlRepository.findByTerritoryAndStatusAndArchiveFalse(countryService.getLOTLCountry(), TLStatus.PROD);
        if ((lotlProd != null) && (lotlProd.getXmlFile() != null)) {
            File tslFile = fileService.getTSLFile(lotlProd.getXmlFile());
            try {
                unmarshallTSL = jaxbService.unmarshallTSL(tslFile);
            } catch (Exception e) {
                LOGGER.error("Unable to unmashall LOTL of production : " + e.getMessage(), e);
            }
        }
        return unmarshallTSL;
    }

    /**
     * Get production LOTL as DBTrustedLists format
     */
    public DBTrustedLists getLOTL() {
        return tlRepository.findByTerritoryAndStatusAndArchiveFalse(countryService.getLOTLCountry(), TLStatus.PROD);
    }

    /**
     * Get database ID of production LOTL
     */
    public int getLOTLId() {
        DBTrustedLists lotlProd = getLOTL();
        return lotlProd.getId();
    }

    /**
     * Verify if trusted list @tlId is a LOTL
     *
     * @param tlId
     */
    public boolean isLOTL(int tlId) {
        DBTrustedLists trustedList = getDbTL(tlId);
        if (trustedList != null) {
            return TLType.LOTL.equals(trustedList.getType());
        }
        return false;
    }

    /* ----- ----- Get trusted list ----- ----- */

    /**
     * Get trusted list by given @id as TL dto format
     *
     * @param id
     */
    public TL getTL(int id) {
        DBTrustedLists tldb = getDbTL(id);
        return getDtoTL(tldb);
    }

    /**
     * Get trusted list by given @id as @DBTrustedLists format
     *
     * @param id
     */
    public DBTrustedLists getDbTL(int id) {
        return tlRepository.findOne(id);
    }

    /**
     * Get trusted list from xml file @id as @DBTrustedLists format
     *
     * @param id
     */
    public DBTrustedLists findByXmlFileId(int id) {
        return tlRepository.findByXmlFileId(id);
    }

    /**
     * Get trusted list as TL dto format from @DBTrustedLists format
     *
     * @param tldb
     * @return
     */
    public TL getDtoTL(DBTrustedLists tldb) {
        TL mytl = null;
        if (tldb != null) {

            DBFiles dbf = tldb.getXmlFile();
            File xml = fileService.getTSLFile(dbf);

            if (tldb.getVersionIdentifier() == 0) {
                LOGGER.error(bundle.getString("error.tl.version.undefined").replace("%TLID%", Integer.toString(tldb.getId())));
                return null;
            } else if (tldb.getVersionIdentifier() == 4) {
                LOGGER.debug("getDtoTL Version 4");
                TrustStatusListType unmarshall;
                try {
                    unmarshall = jaxbService.unmarshallTSL(xml);
                } catch (XmlMappingException | IOException e) {
                    throw new IllegalStateException(bundle.getString("error.proccess.xml.file"));
                }
                if ((unmarshall != null) && (unmarshall.getSchemeInformation() != null)) {
                    mytl = tlBuilder.buildTLV4(tldb.getId(), unmarshall);
                    mytl.setDbName(tldb.getName());
                    mytl.setDbStatus(tldb.getStatus());
                    mytl.setDbCountryName(tldb.getTerritory().getCountryName());
                    mytl.setLastEdited(tldb.getLastEditedDate());
                    mytl.setCheckToRun(tldb.isCheckToRun());
                    mytl.setCheckEdited(tldb.getCheckDate());
                    DBFiles xmlFile = tldb.getXmlFile();
                    if (tldb.getCheckStatus() != null) {
                        if (tldb.getCheckStatus().equals(CheckStatus.ERROR)) {
                            mytl.setCheckStatus(tldb.getCheckStatus());

                        } else {
                            mytl.setCheckStatus(TLUtils.getTLStatus(tldb.getCheckStatus(), xmlFile));
                        }
                    }
                    if (xmlFile != null) {
                        if (xmlFile.getSignatureInformation() != null) {
                            mytl.setSigStatus(xmlFile.getSignatureInformation().getIndication());
                        }
                        if (!CollectionUtils.isEmpty(xmlFile.getAvailabilityInfos())) {
                            mytl.setAvailabilityStatus(xmlFile.getAvailabilityInfos().get(0).getStatus());
                        }
                        mytl.setFirstScanDate(xmlFile.getFirstScanDate());
                    }
                }

            } else if (tldb.getVersionIdentifier() == 5) {
                TrustStatusListTypeV5 unmarshall;
                try {
                    unmarshall = jaxbService.unmarshallTSLV5(xml);
                } catch (XmlMappingException | IOException e) {
                    throw new IllegalStateException(bundle.getString("error.proccess.xml.file"));
                }
                if ((unmarshall != null) && (unmarshall.getSchemeInformation() != null)) {
                    mytl = tlBuilder.buildTLV5(tldb.getId(), unmarshall);
                    mytl.setDbName(tldb.getName());
                    mytl.setDbStatus(tldb.getStatus());
                    mytl.setDbCountryName(tldb.getTerritory().getCountryName());
                    mytl.setLastEdited(tldb.getLastEditedDate());
                    mytl.setCheckToRun(tldb.isCheckToRun());
                    mytl.setCheckEdited(tldb.getCheckDate());
                    DBFiles xmlFile = tldb.getXmlFile();
                    if (tldb.getCheckStatus() != null) {
                        if (tldb.getCheckStatus().equals(CheckStatus.ERROR)) {
                            mytl.setCheckStatus(tldb.getCheckStatus());

                        } else {
                            mytl.setCheckStatus(TLUtils.getTLStatus(tldb.getCheckStatus(), xmlFile));
                        }
                    }

                    if (xmlFile != null) {
                        if (xmlFile.getSignatureInformation() != null) {
                            mytl.setSigStatus(xmlFile.getSignatureInformation().getIndication());
                        }
                        if (!CollectionUtils.isEmpty(xmlFile.getAvailabilityInfos())) {
                            mytl.setAvailabilityStatus(xmlFile.getAvailabilityInfos().get(0).getStatus());
                        }

                        mytl.setFirstScanDate(xmlFile.getFirstScanDate());
                    }
                }
            } else {
                throw new IllegalStateException(bundle.getString("error.version.unsupported").replaceAll("%VERSION%", Integer.toString(tldb.getVersionIdentifier())));
            }
        }
        return mytl;
    }

    /**
     * Get production trusted list by given @territory as DBTrustedLists format
     *
     * @param territory
     */
    public DBTrustedLists getPublishedDbTLByCountry(DBCountries territory) {
        return tlRepository.findByTerritoryAndStatusAndArchiveFalse(territory, TLStatus.PROD);
    }

    /**
     * Get production trusted list by given @territory as TL dto format
     *
     * @param territory
     */
    public TL getPublishedTLByCountry(DBCountries territory) {
        DBTrustedLists productionTL = getPublishedDbTLByCountry(territory);
        return getDtoTL(productionTL);
    }

    /**
     * Get production trusted list by given @countryCode as TL dto format
     *
     * @param countryCode
     */
    public TL getPublishedTLByCountryCode(String countryCode) {
        DBCountries country = countryService.getCountryByTerritory(countryCode);
        TL publishedTl = null;
        if (country != null) {
            DBTrustedLists tldb = tlRepository.findByTerritoryAndStatusAndArchiveFalse(country, TLStatus.PROD);

            if (tldb != null) {
                publishedTl = getTL(tldb.getId());
            }
        }
        return publishedTl;
    }

    /**
     * Get previous production trusted list by given @territory as TL dto format
     *
     * @param territory
     */
    public TL getPreviousProduction(DBCountries territory) {
        List<DBTrustedLists> archiveds = tlRepository.findByTerritoryAndStatusAndArchiveTrueOrderByIdDesc(territory, TLStatus.PROD);
        if (CollectionUtils.isNotEmpty(archiveds)) {
            return getDtoTL(archiveds.get(0));
        }
        return null;
    }

    /**
     * Get trusted list by given @id as @TrustedListsReport format
     *
     * @param id
     * @return
     */
    public TrustedListsReport getTLInfo(int id) {
        DBTrustedLists tldb = getDbTL(id);
        return getTLInfo(tldb, false);
    }

    /**
     * Get trusted list by given @tldb as @TrustedListsReport format
     *
     * @param tldb
     * @param isContactVisible
     *            (show/hide contact column)
     */
    public TrustedListsReport getTLInfo(DBTrustedLists tldb, Boolean isContactVisible) {
        TrustedListsReport rt = tlReport(tldb, isContactVisible);
        return rt;
    }

    /* ----- ----- Trusted list XML ----- ----- */

    /**
     * Get XML version of production trusted list by @countryCode
     *
     * @param countryCode
     */
    public int getVersion(String countryCode) {
        DBCountries country = countryService.getCountryByTerritory(countryCode);
        if (country != null) {
            DBTrustedLists tldb = tlRepository.findByTerritoryAndStatusAndArchiveFalse(country, TLStatus.PROD);
            if (tldb != null) {
                return extractVersionFromFile(tldb.getXmlFile());
            }
        }
        return 0;

    }

    /**
     * Extract file version from TL XML by xml file @dbF
     *
     * @param tldb
     */
    public int extractVersionFromFile(DBFiles dbf) {
        File xml = fileService.getTSLFile(dbf);
        String fileVersion = FileUtils.getTlVersion(xml);
        return Integer.valueOf(fileVersion);
    }

    /**
     * Get XML file signature info by trusted list @id
     *
     * @param id
     * @return
     */
    public TLSignature getSignatureInfo(int id) {
        DBTrustedLists tldb = getDbTL(id);
        if ((tldb.getXmlFile() != null) && (tldb.getXmlFile().getSignatureInformation() != null)) {
            return new TLSignature(tldb.getXmlFile().getSignatureInformation());
        }
        return null;
    }

    /**
     * Get XMLStream & remove signature if exist
     *
     * @param id
     * @return trustedlist xml input stream
     */
    public InputStream getXmlStreamToSign(int id) {
        File xml = getXmlFile(id);
        try {
            return new FileInputStream(xml);
        } catch (FileNotFoundException e) {
            LOGGER.error("XML FILE NOT FOUND FOR TL ID : " + id + " / " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Get trusted list XML of given ID
     *
     * @param id
     */
    public File getXmlFile(int id) {
        DBTrustedLists tldb = getDbTL(id);
        DBFiles dbf = tldb.getXmlFile();
        File xml = fileService.getTSLFile(dbf);
        if (xml != null) {
            return xml;
        }
        return null;
    }

    /**
     * Update @tlId XML file digest based on XML signedDoc value
     *
     * @param signedDoc
     * @param tlId
     */
    public void updateSignedXMLFile(DSSDocument signedDoc, int tlId) {
        DBTrustedLists dbTl = getDbTL(tlId);
        DBFiles xmlFile = dbTl.getXmlFile();
        xmlFile.setLocalPath(fileService.storeNewDraftTL(xmlFile.getMimeTypeFile(), signedDoc, dbTl.getTerritory().getCodeTerritory(), xmlFile.getLocalPath()));
        try {
            xmlFile.setDigest(TLUtils.getSHA2(IOUtils.toByteArray(signedDoc.openStream())));
        } catch (IOException e) {
            LOGGER.error("Update Signed XML File" + e);
        }
        tlRepository.save(dbTl);
        auditService.addAuditLog(AuditTarget.DRAFT_TL, AuditAction.UPDATE, AuditStatus.SUCCES, dbTl.getTerritory().getCodeTerritory(), xmlFile.getId(), "SYSTEM",
                "CLASS:TLSERVICE.UPDATESIGNEDXMLFILE,TLID:" + tlId + ";PREVIOUS_STEP:SIGNED");
    }

    /**
     * Get SHA2 value of trusted list @id XML file
     *
     * @param id
     */
    public String getSha2Value(int id) {
        FileInputStream fileInputStream = null;
        File file = getXmlFile(id);
        byte[] bFile = new byte[(int) file.length()];

        try {
            // convert file into array of bytes
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();

            String digestOfXml = TLUtils.getSHA2(bFile);
            return digestOfXml;
        } catch (Exception e) {
            LOGGER.error("getSha2Value Error");
        }

        return "";
    }

    /* ----- ----- Trusted list informations ----- ----- */

    public int getProdSequenceNumberByTerritory(String countryCode) {
        DBTrustedLists dbTL = tlRepository.findByTerritoryAndStatusAndArchiveFalse(countryService.getCountryByTerritory(countryCode), TLStatus.PROD);
        if (dbTL == null) {
            return 0;
        }
        return dbTL.getSequenceNumber();
    }

    /* ----- ----- Trusted list creation ----- ----- */

    /**
     * Get trusted list or create new entry if production trusted list with same @xmlURL already exist
     *
     * @param countryCode
     * @param xmlUrl
     * @param type
     * @param status
     * @return
     */
    public DBTrustedLists getOrCreateTL(String countryCode, String xmlUrl, TLType type, TLStatus status) {
        DBCountries country = countryService.getCountryByTerritory(countryCode);
        DBTrustedLists tl = null;
        if (country != null) {
            tl = tlRepository.findByTerritoryAndStatusAndArchiveFalse(country, TLStatus.PROD);
            if (tl == null) {
                tl = createTL(xmlUrl, type, status, country, tl);
            } else if ((tl.getXmlFile() != null) && !StringUtils.equals(xmlUrl, tl.getXmlFile().getUrl())) {
                tl.getXmlFile().setUrl(xmlUrl);
            }
        }
        return tl;
    }

    /**
     * Create new trusted list entry
     *
     * @param xmlUrl
     * @param type
     * @param status
     * @param country
     * @param tlCurrent
     * @return
     */
    public DBTrustedLists createTL(String xmlUrl, TLType type, TLStatus status, DBCountries country, DBTrustedLists tlCurrent) {

        DBTrustedLists tl;
        tl = new DBTrustedLists();
        tl.setTerritory(country);
        tl.setName(type + " - " + country.getCodeTerritory());
        tl.setType(type);
        tl.setStatus(status);
        tl.setLastEditedDate(new Date());

        DBFiles xmlFile = new DBFiles();
        if ((tlCurrent != null) && (tlCurrent.getXmlFile() != null)) {
            xmlFile.setDigest(tlCurrent.getXmlFile().getDigest());
            xmlFile.setLocalPath(tlCurrent.getXmlFile().getLocalPath());
            xmlFile.setFirstScanDate(tlCurrent.getXmlFile().getFirstScanDate());
        }
        xmlFile.setUrl(xmlUrl);
        xmlFile.setMimeTypeFile(MimeType.XML);
        tl.setXmlFile(xmlFile);

        tl.setLastEditedDate(new Date());
        tlRepository.save(tl);
        return tl;
    }

    /**
     * Create clone of trusted list @tlId without signature
     *
     * @param tlId
     */
    public void createFileWithoutSignature(int tlId) {
        TL tl = getTL(tlId);
        DBTrustedLists tldb = getDbTL(tlId);

        byte[] updatedTL = jaxbService.marshallToBytesAsV5(tl);
        if (ArrayUtils.isNotEmpty(updatedTL)) {
            DBFiles xmlFile = tldb.getXmlFile();
            if (!xmlFile.getSignatureInformation().getIndication().equals(SignatureStatus.NOT_SIGNED)) {
                xmlFile.setLocalPath(fileService.storeNewDraftTL(xmlFile.getMimeTypeFile(), updatedTL, tldb.getTerritory().getCodeTerritory(), xmlFile.getLocalPath()));
            }
            tldb.setLastEditedDate(new Date());
            tlRepository.save(tldb);
        }
    }

    /* ----- ----- Trusted list deletion ----- ----- */

    /**
     * Delete trusted list by @dbID
     *
     * @param dbId
     */
    public void deleteDraft(int dbId) {
        DBTrustedLists tsl = getDbTL(dbId);
        deleteDraft(tsl);
    }

    /**
     * Delete trusted list by @DBTrustedLists entry
     *
     * @param tsl
     */
    public void deleteDraft(DBTrustedLists tsl) {
        if (tsl.getXmlFile() != null) {
            DBFiles xmlFile = tsl.getXmlFile();
            File originalFile = fileService.getTSLFile(xmlFile);
            originalFile.delete();
            LOGGER.info("FILE " + xmlFile.getLocalPath() + " IS DELETED FROM FILE SYSTEM");
        }
        tlRepository.delete(tsl.getId());
    }

    /* ----- ----- Trusted list - Notification management ----- ----- */

    /**
     * Get @territory pointer(s) from production LOTL
     *
     * @param territory
     */
    public List<TLPointersToOtherTSL> getLOTLPointer(String territory) {
        TL lotl = getPublishedTLByCountryCode(lotlTerritory);
        if ((lotl == null) || CollectionUtils.isEmpty(lotl.getPointers())) {
            return Collections.emptyList();
        }

        List<TLPointersToOtherTSL> pointers = new ArrayList<>();
        for (TLPointersToOtherTSL pointer : lotl.getPointers()) {
            if (pointer.getSchemeTerritory().equalsIgnoreCase(territory)) {
                pointers.add(pointer);
            }
        }
        return pointers;
    }

    /**
     * Detach "IN_DRAFT" notification from draft @dbID. Set notification(s) VALIDATED/PROCESSED depends on @detached
     *
     * @param dbId
     * @param detached
     */
    public void detachedNotification(int dbId, boolean detached) {
        DBTrustedLists tsl = getDbTL(dbId);
        LOGGER.info("DETACHED NOTIFICATION FOR " + tsl.getName() + " / " + tsl.getId() + " is : " + detached);
        List<DBNotification> dbNotificationList = new ArrayList<>(tsl.getNotifications());
        for (DBNotification dbNotif : dbNotificationList) {
            if (detached) {
                notificationService.updateStatus(dbNotif, NotificationStatus.VALIDATED);
                dbNotif.getTls().remove(tsl);
                tsl.getNotifications().remove(dbNotif);
            } else {
                notificationService.updateStatus(dbNotif, NotificationStatus.PROCESSED);
                dbNotif.getTls().remove(tsl);
                tsl.getNotifications().remove(dbNotif);
            }
        }
    }

    /**
     * Attach notification to trusted list @dbID
     *
     * @param dbId
     * @param dbNotificationList
     */
    private void attachedNotification(int dbId, List<DBNotification> dbNotificationList) {
        DBTrustedLists tsl = getDbTL(dbId);
        LOGGER.info("ATTACH NOTIFICATION FOR " + tsl.getName() + " / " + tsl.getId() + " [NUMBER :  " + dbNotificationList.size() + "]");
        for (DBNotification dbNotif : dbNotificationList) {
            dbNotif.getTls().add(tsl);
            tsl.getNotifications().add(dbNotif);
        }
    }

    /**
     * Verify if notification are attached to draft @draftId
     *
     * @param draftId
     */
    public Boolean checkDraftNotification(int draftId) {
        List<DBNotification> notifications = getDraftPointerNotified(draftId);
        if (!notifications.isEmpty()) {
            for (DBNotification notification : notifications) {
                if (notification.getStatus().equals(NotificationStatus.INDRAFT)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Get country code list of pointer notified by draft @draftBy
     *
     * @param draftId
     */
    public List<String> getDraftPointerNotifiedInDraft(int draftId) {
        List<String> notificationsDTO = new ArrayList<>();
        List<DBNotification> notifications = getDraftPointerNotified(draftId);
        for (DBNotification notif : notifications) {
            if (notif.getStatus().equals(NotificationStatus.INDRAFT)) {
                notificationsDTO.add(notif.getTerritory().getCodeTerritory());
            }
        }
        return notificationsDTO;
    }

    /**
     * Get attached notification(s) by draft @draftId
     *
     * @param draftId
     */
    private List<DBNotification> getDraftPointerNotified(int draftId) {
        DBTrustedLists draft = getDbTL(draftId);
        return draft.getNotifications();
    }

    /* ----- ----- Trusted list informations update/get ----- ----- */

    /**
     * Switch checkToRun of trusted list @tlId
     *
     * @param tlId
     */
    public boolean switchCheckToRun(int tlId) {
        DBTrustedLists tl = getDbTL(tlId);
        boolean checkToRun = !tl.isCheckToRun();
        tl.setCheckToRun(checkToRun);
        tl.setLastEditedDate(new Date());
        tlRepository.save(tl);
        return checkToRun;
    }

    /**
     * Update trusted list lastAccessDate by @tlId
     *
     * @param tlId
     */
    public void updateLastAccessDate(int tlId) {
        tlRepository.updateLastAccessDate(new Date(), tlId);
    }

    /**
     * Get lastEditionDate by trusted list @tlId
     *
     * @param tlId
     */
    public Date getEdt(int tlId) {
        DBTrustedLists tldb = getDbTL(tlId);
        return tldb.getLastEditedDate();
    }

    /**
     * Update trusted list checkDate to date by @tlId
     *
     * @param tlId
     */
    public void updateCheckDate(int tlId) {
        DBTrustedLists dbTL = getDbTL(tlId);
        dbTL.setCheckDate(new Date());
        tlRepository.save(dbTL);
    }

    /**
     * Archive trusted list by @DBTrustedLists entry
     *
     * @param tl
     * @param status
     */
    public void archive(DBTrustedLists tl, Boolean status) {
        tl.setArchive(status);
        tlRepository.save(tl);
    }

    /**
     * Update trusted list checkStatus by @tlId
     *
     * @param tlId
     */
    public void setTlCheckStatus(int tlId) {
        CheckStatus tmp = CheckStatus.SUCCESS;
        DBTrustedLists dbTl = getDbTL(tlId);
        tmp = TLUtils.getCheckStatus(dbTl.getCheckResults());
        LOGGER.info("Check status for " + dbTl.getName() + " / " + dbTl.getId() + " is set to  : " + tmp.toString());
        dbTl.setCheckStatus(tmp);
    }

    /**
     * Check if trusted list is production TL or an existing draft TL. True is found
     *
     * @param tlid
     * @param cookie
     */
    public boolean inStoreOrProd(int tlid, String cookie) {
        DBTrustedLists tl = getDbTL(tlid);
        if (tl.getStatus().equals(TLStatus.PROD)) {
            return true;
        }
        return ((cookie != null) && tl.getDraftStoreId().equals(cookie) && draftStoreService.checkDraftStoreId(tl.getDraftStoreId()));
    }

    /* ----- ----- Draft LOTL update ----- ----- */

    /**
     * Trigger when new LOTL is loaded. Delete draft found with same digest;
     * Set notification status from draft LOTL published & Send alert to MS;
     * Error : TL null;
     * Error : TL not a LOTL;
     * Error : File or digest null;
     */
    public void updateDraftLOTL(int dbId) {
        DBTrustedLists dbProd = getDbTL(dbId);
        if (dbProd == null) {
            LOGGER.error("DBTrustedList is null " + dbId);
        } else if (dbProd.getType().equals(TLType.TL)) {
            LOGGER.error("DBTrustedList type is TL :" + dbId);
        } else if ((dbProd.getXmlFile() == null) || (dbProd.getXmlFile().getDigest() == null)) {
            LOGGER.error("DBTrustedList XML file or digest is null :" + dbId);
        } else {
            List<DBTrustedLists> dbDrafts = tlRepository.findByXmlFileDigestAndStatusOrderByNameAsc(dbProd.getXmlFile().getDigest(), TLStatus.DRAFT);
            List<DBNotification> notifications;
            // Set of notification by DBCountries (no duplicate)
            HashMap<DBCountries, Set<DBNotification>> notificationMap = new HashMap<>();
            Set<DBNotification> notificationSet;
            for (DBTrustedLists dbDraft : dbDrafts) {
                notifications = dbDraft.getNotifications();
                for (DBNotification notification : notifications) {
                    notificationSet = notificationMap.get(notification.getTerritory());
                    if (notificationSet == null) {
                        notificationSet = new HashSet<>();
                    }
                    notificationSet.add(notification);
                    notificationMap.put(notification.getTerritory(), notificationSet);
                }
                detachedNotification(dbDraft.getId(), false);
                attachedNotification(dbProd.getId(), notifications);
                deleteDraft(dbDraft.getId());
            }

            // Alert
            for (Entry<DBCountries, Set<DBNotification>> entry : notificationMap.entrySet()) {
                alertingService.sendNotificationPublished(dbProd, entry.getKey(), entry.getValue());
            }
        }

    }

}
