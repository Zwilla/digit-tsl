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

import java.util.Date;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLSchemeInformation;
import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

/**
 * TL scheme information edition/deletion management
 */
@Service
@Transactional(value = TxType.REQUIRED)
public class TlEditSchemeInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TlEditSchemeInfoService.class);

    @Autowired
    private TLRepository tlRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private TLService tlService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private TrustedListJaxbService jaxbService;

    public TLSchemeInformation edit(int id, TLSchemeInformation schemeInfo, String editedTag, String tslIdentifier) {
        DBTrustedLists tldb = tlRepository.findOne(id);
        if (TLStatus.DRAFT.equals(tldb.getStatus())) {
            // CHANGE IN DB
            switch (Tag.valueOf(editedTag)) {
            case TERRITORY:
                DBCountries country = countryService.getCountryByTerritory(schemeInfo.getTerritory());
                tldb.setTerritory(country);
                break;
            case ISSUE_DATE:
                tldb.setIssueDate(schemeInfo.getIssueDate());
                break;
            case NEXT_UPDATE:
                tldb.setNextUpdateDate(schemeInfo.getNextUpdateDate());
                break;
            case SEQUENCE_NUMBER:
                tldb.setSequenceNumber(schemeInfo.getSequenceNumber());
                break;
            default:
                break;
            }

            // CHANGE SN IN XML FILE
            TL tl = tlService.getTL(id);
            switch (Tag.valueOf(editedTag)) {
            case TERRITORY:
                tl.getSchemeInformation().setTerritory(schemeInfo.getTerritory());
                break;
            case ISSUE_DATE:
                tl.getSchemeInformation().setIssueDate(schemeInfo.getIssueDate());
                break;
            case SEQUENCE_NUMBER:
                tl.getSchemeInformation().setSequenceNumber(schemeInfo.getSequenceNumber());
                break;
            case NEXT_UPDATE:
                tl.getSchemeInformation().setNextUpdateDate(schemeInfo.getNextUpdateDate());
                break;
            case SCHEME_INFORMATION_URI:
                // UPDATE OF ALL THE SCHEME INFO URI
                tl.getSchemeInformation().setSchemeInfoUri(schemeInfo.getSchemeInfoUri());
                break;
            case SCHEME_OPERATOR_NAME:
                tl.getSchemeInformation().setSchemeOpeName(schemeInfo.getSchemeOpeName());
                break;
            case POSTAL_ADDRESSES:
                tl.getSchemeInformation().setSchemeOpePostal(schemeInfo.getSchemeOpePostal());
                break;
            case ELECTRONIC_ADDRESS:
                tl.getSchemeInformation().setSchemeOpeElectronic(schemeInfo.getSchemeOpeElectronic());
                break;
            case TSL_TYPE:
                tl.getSchemeInformation().setType(schemeInfo.getType());
                break;
            case STATUS_DETERMINATION:
                tl.getSchemeInformation().setStatusDetermination(schemeInfo.getStatusDetermination());
                break;
            case SCHEME_TYPE_COMMUNITY_RULES:
                tl.getSchemeInformation().setSchemeTypeCommRule(schemeInfo.getSchemeTypeCommRule());
                break;
            case SCHEME_NAME:
                tl.getSchemeInformation().setSchemeName(schemeInfo.getSchemeName());
                break;
            case POLICY_OR_LEGAL_NOTICE:
                tl.getSchemeInformation().setSchemePolicy(schemeInfo.getSchemePolicy());
                break;
            case DISTRIBUTION_LIST:
                tl.getSchemeInformation().setDistributionPoint(schemeInfo.getDistributionPoint());
                break;
            case TSL_IDENTIFIER:
                tl.setTslId(tslIdentifier);
                break;
            default:
                LOGGER.error("Edited Tag [" + editedTag + "] not supported in Scheme Information Edition");
                break;
            }

            byte[] updatedTL = jaxbService.marshallToBytesAsV5(tl);
            if (ArrayUtils.isNotEmpty(updatedTL)) {
                DBFiles xmlFile = tldb.getXmlFile();
                xmlFile.setLocalPath(fileService.storeNewDraftTL(xmlFile.getMimeTypeFile(), updatedTL, tldb.getTerritory().getCodeTerritory(), xmlFile.getLocalPath()));

                // tldb.setXmlFile(xmlFile);
                tldb.setLastEditedDate(new Date());
                tlRepository.save(tldb);
                return tl.getSchemeInformation();
            }

        }
        return null;
    }

}
