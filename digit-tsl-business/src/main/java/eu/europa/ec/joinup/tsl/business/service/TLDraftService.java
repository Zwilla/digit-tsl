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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.XmlMappingException;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.Load;
import eu.europa.ec.joinup.tsl.business.dto.TrustedListsReport;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.business.util.FileUtils;
import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.AuditAction;
import eu.europa.ec.joinup.tsl.model.enums.AuditStatus;
import eu.europa.ec.joinup.tsl.model.enums.AuditTarget;
import eu.europa.ec.joinup.tsl.model.enums.MimeType;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;
import eu.europa.esig.jaxb.tsl.TrustStatusListType;
import eu.europa.esig.jaxb.v5.tsl.TrustStatusListTypeV5;

/**
 * Draft management (clone, conflict management, import)
 */
@Service
@Transactional(value = TxType.REQUIRED)
public class TLDraftService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TLDraftService.class);

    @Autowired
    private AuditService auditService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private TrustedListJaxbService jaxbService;

    @Autowired
    private FileService fileService;

    @Autowired
    private TLBuilder tlBuilder;

    @Autowired
    private TLRepository tlRepository;
    @Autowired
    private TLService tlService;

    @Autowired
    private TLValidator tlValidator;

    @Autowired
    private RulesRunnerService rulesRunner;

    @Value("${tsl.folder}")
    private String folderPath;

    @Value("${tmpPrefixFile}")
    private String prefixTmpFile;

    /**
     * Clone @territory production trusted list
     *
     * @param territory
     * @param cookieId
     * @param userName
     * @param migrate
     */
    public DBTrustedLists cloneTLtoDraft(String territory, String cookieId, String userName, Load migrate) {
        DBCountries country = countryService.getCountryByTerritory(territory);
        if (country != null) {
            DBTrustedLists tldb = tlRepository.findByTerritoryAndStatusAndArchiveFalse(country, TLStatus.PROD);
            LOGGER.info("Clone to Draft -> " + country.getCodeTerritory() + " [id = " + tldb.getId() + "]");
            if (tldb != null) {
                DBFiles xmlFile = tldb.getXmlFile();
                DBFiles unpersistedXmlDraft = prepareDraftFromTSL(xmlFile, territory, migrate);

                if (unpersistedXmlDraft != null) {
                    try {
                        File tslFile = fileService.getTSLFile(unpersistedXmlDraft);
                        TrustStatusListTypeV5 tslType = jaxbService.unmarshallTSLV5(tslFile);
                        TL draftTLDTO = tlBuilder.buildTLV5(0, tslType);

                        DBTrustedLists draftTL = new DBTrustedLists();
                        draftTL.setDraftStoreId(cookieId);
                        draftTL.setTerritory(country);
                        draftTL.setStatus(TLStatus.DRAFT);
                        draftTL.setParent(tldb);
                        draftTL.setType(tldb.getType());

                        draftTL.setSequenceNumber(draftTLDTO.getSchemeInformation().getSequenceNumber());
                        draftTL.setIssueDate(draftTLDTO.getSchemeInformation().getIssueDate());
                        draftTL.setNextUpdateDate(draftTLDTO.getSchemeInformation().getNextUpdateDate());
                        draftTL.setVersionIdentifier(tlService.extractVersionFromFile(unpersistedXmlDraft));

                        draftTL.setXmlFile(unpersistedXmlDraft);

                        draftTL.setLastEditedDate(new Date());
                        draftTL.setCreatedBy(userName);

                        tlRepository.save(draftTL);
                        draftTL.setName(getDraftName(country, draftTL.getId()));

                        return draftTL;
                    } catch (Exception e) {
                        LOGGER.error("Unable to create a draft : " + e.getMessage(), e);
                    }
                }
            }
        } else {
            LOGGER.error("Country code does not exist : " + territory);
        }

        return null;
    }

    /**
     * Verify draft (comparison/tlcc checks), set TL status, add audit entry and return TrustedListsReport
     * 
     * @param draft
     * @param name
     *            User ID
     */
    public TrustedListsReport finalizeDraftCreation(DBTrustedLists draft, String name) {
        auditService.addAuditLog(AuditTarget.DRAFT_TL, AuditAction.CREATE, AuditStatus.SUCCES, draft.getTerritory().getCodeTerritory(), draft.getXmlFile().getId(), name, "TLID:" + draft.getId());
        // CHECK SIGNATURE STATUS
        tlValidator.validateTLSignature(draft);
        // EXECUTE ALL CHECK
        TL draftTL = tlService.getTL(draft.getId());
        rulesRunner.runAllRulesByTL(draftTL);

        tlService.setTlCheckStatus(draftTL.getTlId());
        // RETURN TL REPORT
        TrustedListsReport report = tlService.getTLInfo(draft.getId());
        return report;
    }

    /**
     * Create a clone of draft when edition conflict occurs (concurrent edition of the same TL)
     *
     * @param tl
     */
    public DBTrustedLists conflictTLtoDraft(TL tl) {
        if (tl.getDbStatus().equals(TLStatus.DRAFT)) {
            DBTrustedLists dbTl = tlService.getDbTL(tl.getTlId());
            DBTrustedLists newDbTl = tlService.createTL(dbTl.getXmlFile().getUrl(), dbTl.getType(), dbTl.getStatus(), dbTl.getTerritory(), null);
            newDbTl.setName(dbTl.getName() + "_tmp");
            newDbTl.setDraftStoreId(dbTl.getDraftStoreId());
            newDbTl.setNextUpdateDate(tl.getSchemeInformation().getNextUpdateDate());
            newDbTl.setIssueDate(tl.getSchemeInformation().getIssueDate());
            newDbTl.setSequenceNumber(tl.getSchemeInformation().getSequenceNumber());

            DBFiles xmlFile = new DBFiles();
            xmlFile.setMimeTypeFile(MimeType.XML);
            xmlFile.setLocalPath(fileService.storeNewTL(xmlFile, jaxbService.marshallToBytesAsV5(tl), dbTl.getTerritory().getCodeTerritory()));
            newDbTl.setXmlFile(xmlFile);
            newDbTl.setVersionIdentifier(tlService.extractVersionFromFile(xmlFile));

            return newDbTl;
        }
        return null;
    }

    /**
     * Create new draft based on trusted list XML file byte array
     *
     * @param byteArray
     * @param cookieId
     * @param userName
     * @param migrate
     * @throws XmlMappingException
     * @throws IOException
     */
    public DBTrustedLists createDraftFromXML(byte[] byteArray, String cookieId, String userName, Load migrate) throws XmlMappingException, IOException {
        // CREATE FILE FROM BYTE.
        File originalFile = File.createTempFile(prefixTmpFile, "xml");

        FileOutputStream fos = new FileOutputStream(originalFile);
        fos.write(byteArray);
        fos.close();
        // Check INPUT FILE VERSION
        String fileVersion = FileUtils.getTlVersion(originalFile);

        TL draftTLDTO = null;
        DBTrustedLists draftTL = new DBTrustedLists();
        byte[] draftBinaries = null;
        if (fileVersion.equalsIgnoreCase("4")) {
            TrustStatusListType tslType = jaxbService.unmarshallTSL(byteArray);
            LOGGER.debug("Build V4");
            draftTLDTO = tlBuilder.buildTLV4(0, tslType);
            LOGGER.info("Migration V4 to V5");
            draftTLDTO.toV5FromV4();
            migrate.setNew(true);
            draftBinaries = jaxbService.marshallToBytesAsV5(draftTLDTO);
            draftTL.setVersionIdentifier(4);
        } else if (fileVersion.equalsIgnoreCase("5")) {
            TrustStatusListTypeV5 tslTypeV5 = jaxbService.unmarshallTSLV5(byteArray);
            LOGGER.debug("Build V5");
            draftTLDTO = tlBuilder.buildTLV5(0, tslTypeV5);
            draftBinaries = byteArray;
            draftTL.setVersionIdentifier(5);
        } else {
            LOGGER.error("VERSION  [" + fileVersion + "] is not supported");
            return null;
        }

        DBCountries country = countryService.getCountryByTerritory(draftTLDTO.getSchemeInformation().getTerritory());

        if (country != null) {
            DBFiles draftXmlFile = createEmptyFile(MimeType.XML);

            String draftXmlPath = fileService.storeNewDraftTL(MimeType.XML, draftBinaries, country.getCodeTerritory(), null);
            draftXmlFile.setLocalPath(draftXmlPath);
            draftXmlFile.setDigest(TLUtils.getSHA2(byteArray));

            draftTL.setTerritory(country);
            draftTL.setStatus(TLStatus.DRAFT);
            if (countryService.getLOTLCountry().equals(country)) {
                draftTL.setType(TLType.LOTL);
            } else {
                draftTL.setType(TLType.TL);
            }

            draftTL.setDraftStoreId(cookieId);
            draftTL.setSequenceNumber(draftTLDTO.getSchemeInformation().getSequenceNumber());
            draftTL.setIssueDate(draftTLDTO.getSchemeInformation().getIssueDate());
            draftTL.setNextUpdateDate(draftTLDTO.getSchemeInformation().getNextUpdateDate());

            draftTL.setXmlFile(draftXmlFile);
            draftTL.setLastEditedDate(new Date());
            draftTL.setCreatedBy(userName);
            tlRepository.save(draftTL);
            draftTL.setName(getDraftName(country, draftTL.getId()));
            originalFile.delete();
            return draftTL;
        } else {
            LOGGER.error("Country code does not exist : " + draftTLDTO.getSchemeInformation().getTerritory());
        }
        return null;
    }

    private DBFiles prepareDraftFromTSL(DBFiles dbFile, String countryCode, Load migrate) {
        DBFiles draftFile = null;
        try {
            File originalFile = fileService.getTSLFile(dbFile);
            String fileVersion = FileUtils.getTlVersion(originalFile);
            TL tl = null;

            if (fileVersion.equalsIgnoreCase("4")) {
                TrustStatusListType tslType = jaxbService.unmarshallTSL(originalFile);
                LOGGER.debug("Build V4");
                tl = tlBuilder.buildTLV4(0, tslType);
                LOGGER.info("Migration V4 to V5");
                tl.toV5FromV4();
                migrate.setNew(true);

            } else if (fileVersion.equalsIgnoreCase("5")) {
                TrustStatusListTypeV5 tslTypeV5 = jaxbService.unmarshallTSLV5(originalFile);
                LOGGER.debug("Build V5");
                tl = tlBuilder.buildTLV5(0, tslTypeV5);
            } else {
                LOGGER.error("VERSION  [" + fileVersion + "] is not supported");
                return null;
            }

            tl.getSchemeInformation().setSequenceNumber(tl.getSchemeInformation().getSequenceNumber() + 1);

            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            // cal.setTime(new Date());
            Date date = new Date();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, 1);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);

            tl.getSchemeInformation().setIssueDate(cal.getTime());

            cal.add(Calendar.MONTH, 6);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);

            tl.getSchemeInformation().setNextUpdateDate(cal.getTime());

            LOGGER.debug("jaxbService.marshallToBytesAsV5 : " + tl.getId());
            byte[] draftBinaries = jaxbService.marshallToBytesAsV5(tl);

            String draftPath = fileService.storeNewDraftTL(MimeType.XML, draftBinaries, countryCode, null);
            LOGGER.info("Draft local path : " + draftPath);

            draftFile = createEmptyFile(MimeType.XML);
            draftFile.setLocalPath(draftPath);

        } catch (Exception e) {
            LOGGER.error("Unable to create draft from file '" + dbFile.getLocalPath() + "' :" + e.getMessage(), e);
        }

        return draftFile;
    }

    private String getDraftName(DBCountries country, int id) {
        return TLStatus.DRAFT + "_" + country.getCodeTerritory() + "_" + id;
    }

    private DBFiles createEmptyFile(MimeType mimeType) {
        DBFiles draftFile = new DBFiles();
        draftFile.setMimeTypeFile(mimeType);
        return draftFile;
    }

}
