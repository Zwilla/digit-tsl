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
package eu.europa.ec.joinup.tsl.business.rules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.CheckDTO;
import eu.europa.ec.joinup.tsl.business.dto.CheckResultDTO;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLCertificate;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLDigitalIdentification;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLName;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLSchemeTypeCommunityRule;
import eu.europa.ec.joinup.tsl.business.service.ApplicationPropertyService;
import eu.europa.ec.joinup.tsl.business.service.CertificateService;
import eu.europa.ec.joinup.tsl.business.service.CountryService;
import eu.europa.ec.joinup.tsl.business.service.PropertiesService;
import eu.europa.ec.joinup.tsl.business.service.SignersService;
import eu.europa.ec.joinup.tsl.business.service.TLService;
import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.enums.CheckName;
import eu.europa.ec.joinup.tsl.model.enums.MimeType;
import eu.europa.ec.joinup.tsl.model.enums.Tag;
import eu.europa.esig.dss.x509.CertificateToken;

@Service
public class PointersToOtherTSLValidator extends AbstractCheckValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(PointersToOtherTSLValidator.class);
    @Value("${lotl.territory}")
    private String lotlTerritory;

    private static final Set<CheckName> SUPPORTED_CHECK_NAMES;

    static {
        SUPPORTED_CHECK_NAMES = new HashSet<>();

        SUPPORTED_CHECK_NAMES.add(CheckName.IS_PRESENT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_LIST_NOT_EMPTY);

        SUPPORTED_CHECK_NAMES.add(CheckName.IS_LOCATION_PRESENT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_LOCATION_VALID);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_LOCATION_ACCESSIBLE);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_LOCATION_CORRECT_VALUES);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_LOCATION_PUBLISHED_LOTL);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_LOCATION_PUBLISHED_TL);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_MIME_TYPE_PRESENT);

        SUPPORTED_CHECK_NAMES.add(CheckName.IS_SCHEME_TERRITORY_PRESENT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_SCHEME_TERRITORY_CORRECT_VALUE);

        SUPPORTED_CHECK_NAMES.add(CheckName.IS_TLS_TYPE_PRESENT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_TLS_TYPE_CORRECT_VALUE);

        SUPPORTED_CHECK_NAMES.add(CheckName.IS_SCHEME_TYPE_COMMUNITY_RULES_CORRECT_VALUES);

        SUPPORTED_CHECK_NAMES.add(CheckName.IS_SERVICE_DIGITAL_IDENTITIES_PRESENT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_DIGITAL_IDS_ALLOWED);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_DIGITAL_IDS_FROM_OJ_PRESENT);

        SUPPORTED_CHECK_NAMES.add(CheckName.IS_X509CERTIFICATE_CONTAINS_CORRECT_KEY_USAGES);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_X509CERTIFICATE_CONTAINS_BASIC_CONSTRAINT_CA_FALSE);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_X509CERTIFICATE_CONTAINS_TSLSIGNING_EXT_KEY_USAGE);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_X509CERTIFICATE_CONTAINS_SUBJECT_KEY_IDENTIFIER);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_X509CERTIFICATE_COUNTRY_CODE_MATCH);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_X509CERTIFICATE_ORGANIZATION_MATCH);

        SUPPORTED_CHECK_NAMES.add(CheckName.IS_SERVICE_DIGITAL_IDENTITIES_CORRECT);
    }

    @Autowired
    private SignersService signersService;

    @Autowired
    private GenericValidator genericValidator;

    @Autowired
    private URIValidator uriValidator;

    @Autowired
    private TLService tlService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private PropertiesService propertiesService;

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private ApplicationPropertyService applicationPropertyService;

    @Override
    public boolean isSupported(CheckDTO check) {
        return Tag.POINTERS_TO_OTHER_TSL.equals(check.getTarget()) && SUPPORTED_CHECK_NAMES.contains(check.getName());
    }

    @Override
    public List<CheckResultDTO> validate(CheckDTO check, TL tl) {
        List<CheckResultDTO> results = new ArrayList<>();
        boolean isLOTL = tlService.isLOTL(tl.getTlId());
        validatePointerList(tl.getTlId(), check, results, isLOTL, tl.getPointers());
        return results;
    }

    private void validatePointerList(int tlId, CheckDTO check, List<CheckResultDTO> results, boolean isLOTL, List<TLPointersToOtherTSL> pointers) {
        switch (check.getName()) {
            case IS_PRESENT:
                addResult(check, tlId + "_" + Tag.POINTERS_TO_OTHER_TSL, genericValidator.isPresent(pointers), results);
                break;
            case IS_LIST_NOT_EMPTY:
                addResult(check, tlId + "_" + Tag.POINTERS_TO_OTHER_TSL, genericValidator.isCollectionNotEmpty(pointers), results);
                break;
            case IS_LOCATION_PRESENT:
                checkAllLocationsPresent(check, pointers, results);
                break;
            case IS_LOCATION_VALID:
                checkAllLocationsValid(check, pointers, results);
                break;
            case IS_LOCATION_ACCESSIBLE:
                checkAllLocationsAccessible(check, pointers, results);
                break;
            case IS_LOCATION_CORRECT_VALUES:
                checkAllLocationsCorrectValue(check, tlId + "_" + Tag.POINTERS_TO_OTHER_TSL, pointers, isLOTL, results);
                break;
            case IS_LOCATION_PUBLISHED_LOTL:
                if (!isLOTL) {
                    checkAllLocationsCorrectPublished(check, tlId + "_" + Tag.POINTERS_TO_OTHER_TSL, pointers, isLOTL, results);
                }
                break;
            case IS_LOCATION_PUBLISHED_TL:
                if (isLOTL) {
                    checkAllLocationsCorrectPublished(check, tlId + "_" + Tag.POINTERS_TO_OTHER_TSL, pointers, isLOTL, results);
                }
                break;
            case IS_SCHEME_TERRITORY_PRESENT:
                checkAllSchemeTerritoryPresent(check, pointers, results);
                break;
            case IS_SCHEME_TERRITORY_CORRECT_VALUE:
                checkAllSchemeTerritoryCorrectValue(check, pointers, isLOTL, results);
                break;

            case IS_MIME_TYPE_PRESENT:
                checkAllMimeTypePresent(check, pointers, results);
                break;

            case IS_TLS_TYPE_PRESENT:
                checkAllSchemeTSLTypePresent(check, pointers, results);
                break;
            case IS_TLS_TYPE_CORRECT_VALUE:
                checkAllSchemeTSLTypeCorrectValue(check, pointers, isLOTL, results);
                break;

            case IS_SCHEME_TYPE_COMMUNITY_RULES_CORRECT_VALUES:
                checkAllSchemeCommunityRulesCorrectValue(check, pointers, isLOTL, results);
                break;

            case IS_SERVICE_DIGITAL_IDENTITIES_PRESENT:
                checkAllServicesDigitalIdentitiesPresent(check, pointers, results);
                break;
            case IS_DIGITAL_IDS_ALLOWED:
                checkAllDigitalIdsAllowed(check, pointers, isLOTL, results);
                break;
            case IS_DIGITAL_IDS_FROM_OJ_PRESENT:
                checkAllDigitalIdsFromOJPresent(check, pointers, isLOTL, results);
                break;
            case IS_X509CERTIFICATE_CONTAINS_CORRECT_KEY_USAGES:
                if (isLOTL) {
                    checkAllDigitalIdsKeyUsages(check, pointers, results);
                }
                break;
            case IS_X509CERTIFICATE_CONTAINS_BASIC_CONSTRAINT_CA_FALSE:
                if (isLOTL) {
                    checkAllDigitalIdsBasicConstraintCaFalse(check, pointers, results);
                }
                break;
            case IS_X509CERTIFICATE_CONTAINS_TSLSIGNING_EXT_KEY_USAGE:
                if (isLOTL) {
                    checkAllDigitalIdsExtendedKeyUsage(check, pointers, results);
                }
                break;
            case IS_X509CERTIFICATE_CONTAINS_SUBJECT_KEY_IDENTIFIER:
                if (isLOTL) {
                    checkAllDigitalIdsSubjectKeyIdentifier(check, pointers, results);
                }
                break;
            case IS_X509CERTIFICATE_COUNTRY_CODE_MATCH:
                if (isLOTL) {
                    checkAllDigitalIdsCountryCodeMatch(check, pointers, isLOTL, results);
                }
                break;
            case IS_X509CERTIFICATE_ORGANIZATION_MATCH:
                if (isLOTL) {
                    checkAllDigitalIdsOrganizationMatch(check, pointers, results);
                }
                break;
            case IS_SERVICE_DIGITAL_IDENTITIES_CORRECT:
                checkAllServicesDigitalIdentitiesCorrect(check, pointers, results);
                break;

            default:
                LOGGER.warn("Unsupported check " + check.getName());
                break;
        }
    }

    public void runValidation(TLPointersToOtherTSL pointer, List<CheckDTO> checkList, List<CheckResultDTO> results) {
        //METHOD USE FOR EDITION
        boolean isLOTL = tlService.isLOTL(pointer.getTlId());
        if (CollectionUtils.isNotEmpty(checkList)) {
            for (CheckDTO check : checkList) {
                if (pointer.getMimeType() != null) {
                    if (pointer.getMimeType().equals(MimeType.XML)) {
                        results.addAll(validatePointer(pointer, check));
                    } else {
                        //PDF
                        if (!check.getName().equals(CheckName.IS_LOCATION_CORRECT_VALUES)) {
                            results.addAll(validatePointer(pointer, check));
                        } else {
                            if (!isLOTL) {
                                addResult(check, pointer.getTlId() + "_" + Tag.POINTERS_TO_OTHER_TSL, false, results);
                            }
                        }
                    }
                }
            }
        }
    }

    public List<CheckResultDTO> validatePointer(TLPointersToOtherTSL pointer, CheckDTO check) {
        List<CheckResultDTO> results = new ArrayList<>();

        List<TLPointersToOtherTSL> pointers = new ArrayList<>();
        pointers.add(pointer);

        boolean isLOTL = tlService.isLOTL(pointer.getTlId());
        validatePointerList(pointer.getTlId(), check, results, isLOTL, pointers);
        return results;
    }

    private void checkAllLocationsPresent(CheckDTO check, List<TLPointersToOtherTSL> pointers, List<CheckResultDTO> results) {
        if (CollectionUtils.isNotEmpty(pointers)) {
            for (TLPointersToOtherTSL pointer : pointers) {
                addResult(check, pointer.getId() + "_" + Tag.POINTER_LOCATION, genericValidator.isNotEmpty(pointer.getTlLocation()), results);
            }
        }
    }

    private void checkAllLocationsValid(CheckDTO check, List<TLPointersToOtherTSL> pointers, List<CheckResultDTO> results) {
        if (CollectionUtils.isNotEmpty(pointers)) {
            for (TLPointersToOtherTSL pointer : pointers) {
                if (pointer.getTlLocation() == null) {
                    addResult(check, pointer.getId() + "_" + Tag.POINTER_LOCATION, false, results);
                } else {
                    addResult(check, pointer.getId() + "_" + Tag.POINTER_LOCATION, uriValidator.isCorrectUrl(pointer.getTlLocation()), results);
                }
            }
        }
    }

    private void checkAllLocationsAccessible(CheckDTO check, List<TLPointersToOtherTSL> pointers, List<CheckResultDTO> results) {
        if (CollectionUtils.isNotEmpty(pointers)) {
            for (TLPointersToOtherTSL pointer : pointers) {
                if (pointer.getTlLocation() == null) {
                    addResult(check, pointer.getId() + "_" + Tag.POINTER_LOCATION, false, results);
                } else {
                    runAsync(check, pointer.getId() + "_" + Tag.POINTER_LOCATION, uriValidator.isAccessibleUri(pointer.getTlLocation()), results);
                }
            }
        }
    }

    private void checkAllLocationsCorrectValue(CheckDTO check, String location, List<TLPointersToOtherTSL> pointers, boolean isLOTL, List<CheckResultDTO> results) {
        boolean result = true;
        if (!isLOTL) {
            if (CollectionUtils.size(pointers) != 1) {
                result = false; // pointer to XML and PDF
                LOGGER.error("MORE THEN 1 POINTER-> " + CollectionUtils.size(pointers));
            } else {
                boolean foundXml = false;
                for (TLPointersToOtherTSL pointer : pointers) {
                    if (pointer.getMimeType().equals(MimeType.XML) && StringUtils.equals(applicationPropertyService.getLOTLUrl(), pointer.getTlLocation())) {
                        foundXml = true;
                    }
                }
                result = foundXml;
            }
        }
        addResult(check, location, result, results);
    }

    private void checkAllLocationsCorrectPublished(CheckDTO check, String string, List<TLPointersToOtherTSL> pointers, boolean isLOTL, List<CheckResultDTO> results) {
        if (CollectionUtils.isNotEmpty(pointers)) {
            for (TLPointersToOtherTSL pointer : pointers) {
                if (isLOTL && !pointer.getSchemeTerritory().equals(lotlTerritory)) {
                    if ((pointer.getMimeType() != null) && pointer.getMimeType().equals(MimeType.XML)) {
                        addResult(check, pointer.getId() + "_" + Tag.POINTER_LOCATION, uriValidator.isSecureURI(pointer.getTlLocation()), results);
                    }
                } else if (!isLOTL) {
                    addResult(check, pointer.getId() + "_" + Tag.POINTER_LOCATION, uriValidator.isSecureURI(pointer.getTlLocation()), results);
                }
            }
        }
    }

    private void checkAllSchemeTerritoryPresent(CheckDTO check, List<TLPointersToOtherTSL> pointers, List<CheckResultDTO> results) {
        if (CollectionUtils.isNotEmpty(pointers)) {
            for (TLPointersToOtherTSL pointer : pointers) {
                addResult(check, pointer.getId() + "_" + Tag.TERRITORY, genericValidator.isNotEmpty(pointer.getSchemeTerritory()), results);
            }
        }
    }

    private void checkAllSchemeTerritoryCorrectValue(CheckDTO check, List<TLPointersToOtherTSL> pointers, boolean isLOTL, List<CheckResultDTO> results) {
        if (CollectionUtils.isNotEmpty(pointers)) {
            for (TLPointersToOtherTSL pointer : pointers) {
                addResult(check, pointer.getId() + "_" + Tag.TERRITORY, isSchemeTerritoryCorrectValue(pointer.getSchemeTerritory(), isLOTL), results);
            }
        }
    }

    private boolean isSchemeTerritoryCorrectValue(String countryCode, boolean isLOTL) {
        if (StringUtils.isNotEmpty(countryCode)) {
            if (isLOTL) {
                // LOTL has pointers to others countries
                return countryService.isExist(countryCode);
            } else {
                // TL has pointers to the LOTL
                DBCountries lotlCountry = countryService.getLOTLCountry();
                return countryCode.equals(lotlCountry.getCodeTerritory());
            }
        }
        return false;
    }

    private void checkAllSchemeTSLTypePresent(CheckDTO check, List<TLPointersToOtherTSL> pointers, List<CheckResultDTO> results) {
        if (CollectionUtils.isNotEmpty(pointers)) {
            for (TLPointersToOtherTSL pointer : pointers) {
                addResult(check, pointer.getId() + "_" + Tag.POINTER_TYPE, genericValidator.isNotEmpty(pointer.getTlType()), results);
            }
        }
    }

    private void checkAllMimeTypePresent(CheckDTO check, List<TLPointersToOtherTSL> pointers, List<CheckResultDTO> results) {
        if (CollectionUtils.isNotEmpty(pointers)) {
            for (TLPointersToOtherTSL pointer : pointers) {
                if (pointer.getMimeType() == null) {
                    addResult(check, pointer.getId() + "_" + Tag.POINTER_MIME_TYPE, false, results);
                }
            }
        }
    }

    private void checkAllSchemeTSLTypeCorrectValue(CheckDTO check, List<TLPointersToOtherTSL> pointers, boolean isLOTL, List<CheckResultDTO> results) {
        if (CollectionUtils.isNotEmpty(pointers)) {
            for (TLPointersToOtherTSL pointer : pointers) {
                addResult(check, pointer.getId() + "_" + Tag.POINTER_TYPE, isTSLTypeCorrectValue(pointer.getTlType(), isLOTL, pointer.getSchemeTerritory()), results);
            }
        }
    }

    private boolean isTSLTypeCorrectValue(String tlType, boolean isLOTL, String territoryCode) {
        String expectedValue;
        if (isLOTL && !territoryCode.equals(lotlTerritory)) {
            expectedValue = propertiesService.getTLTSLTypeValue();
        } else {
            expectedValue = propertiesService.getLOTLTSLTypeValue();
        }
        return genericValidator.isEquals(expectedValue, tlType);
    }

    private void checkAllSchemeCommunityRulesCorrectValue(CheckDTO check, List<TLPointersToOtherTSL> pointers, boolean isLOTL, List<CheckResultDTO> results) {
        if (CollectionUtils.isNotEmpty(pointers)) {
            for (TLPointersToOtherTSL pointer : pointers) {
                List<TLSchemeTypeCommunityRule> schemeTypeCommunityRules = pointer.getSchemeTypeCommunity();
                if (isLOTL && !pointer.getSchemeTerritory().equals(lotlTerritory)) {
                    //	http://uri.etsi.org/TrstSvc/TrustedList/schemerules/EUcommon
                    //	http://uri.etsi.org/TrstSvc/TrustedList/schemerules/HR
                    if (CollectionUtils.size(schemeTypeCommunityRules) == 2) {
                        List<String> schemeCommunityRulesValues = propertiesService.getTLSchemeCommunityRulesValues();
                        boolean findAllInDB = true;
                        boolean findCorrectCountry = false;
                        for (TLSchemeTypeCommunityRule rule : schemeTypeCommunityRules) {
                            String ruleUri = rule.getValue();
                            findAllInDB &= schemeCommunityRulesValues.contains(ruleUri);
                            if (ruleUri.endsWith("schemerules/" + pointer.getSchemeTerritory())) {
                                findCorrectCountry = true;
                            }
                        }
                        addResult(check, pointer.getId() + "_" + Tag.POINTER_COMMUNITY_RULE, findAllInDB && findCorrectCountry, results);
                    } else {
                        addResult(check, pointer.getId() + "_" + Tag.POINTER_COMMUNITY_RULE, false, results);
                    }
                } else {
                    // TL must points to http://uri.etsi.org/TrstSvc/TrustedList/schemerules/EUlistofthelists
                    if (CollectionUtils.size(schemeTypeCommunityRules) == 1) {
                        TLSchemeTypeCommunityRule communityRule = schemeTypeCommunityRules.get(0);
                        addResult(check, pointer.getId() + "_" + Tag.POINTER_COMMUNITY_RULE,
                                genericValidator.isEquals(propertiesService.getLOTLSchemeCommunityRulesValue(), communityRule.getValue()), results);
                    } else {
                        addResult(check, pointer.getId() + "_" + Tag.POINTER_COMMUNITY_RULE, false, results);
                    }
                }
            }
        }
    }

    private void checkAllServicesDigitalIdentitiesPresent(CheckDTO check, List<TLPointersToOtherTSL> pointers, List<CheckResultDTO> results) {
        for (TLPointersToOtherTSL pointer : pointers) {
            addResult(check, pointer.getId() + "_" + Tag.SERVICE_DIGITAL_IDENTITY, genericValidator.isCollectionNotEmpty(pointer.getServiceDigitalId()), results);
        }
    }

    private void checkAllDigitalIdsAllowed(CheckDTO check, List<TLPointersToOtherTSL> pointers, boolean isLOTL, List<CheckResultDTO> results) {
        // TL must contains LOTL certificates
        for (TLPointersToOtherTSL pointer : pointers) {
            if (!isLOTL) {
                List<TLDigitalIdentification> serviceDigitalId = pointer.getServiceDigitalId();
                if (pointer.getMimeType() != null) {
                    switch (pointer.getMimeType()) {
                        case XML:
                        case PDF:
                            if (CollectionUtils.isNotEmpty(serviceDigitalId)) {
                                List<CertificateToken> lotlCertificates = signersService.getCertificatesFromKeyStore();
                                for (TLDigitalIdentification digitalIdentification : serviceDigitalId) {
                                    if (CollectionUtils.isNotEmpty(digitalIdentification.getCertificateList())) {
                                        for (TLCertificate cert : digitalIdentification.getCertificateList()) {
                                            if (cert.getToken() == null) {
                                                cert.setTokenFromEncoded();
                                            }
                                            if (!lotlCertificates.contains(cert.getToken())) {
                                                addResult(check, digitalIdentification.getId(), false, results);
                                                LOGGER.debug("Not allowed certificate " + digitalIdentification.getId() + " / " + cert.getToken());
                                            } else {
                                                addResult(check, digitalIdentification.getId(), true, results);
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                        default:
                            LOGGER.debug("Unsupported type " + pointer.getMimeType());
                            break;
                    }
                }
            }
        }

    }

    private void checkAllDigitalIdsFromOJPresent(CheckDTO check, List<TLPointersToOtherTSL> pointers, boolean isLOTL, List<CheckResultDTO> results) {

        List<CertificateToken> lotlCertificates = signersService.getCertificatesFromKeyStore();
        for (TLPointersToOtherTSL pointer : pointers) {
            if (!isLOTL) {
                if ((pointer.getMimeType() != null)) {
                    boolean findAllLOTLCertificates = true;
                    for (CertificateToken lotlCert : lotlCertificates) {
                        boolean findCert = false;
                        List<TLDigitalIdentification> serviceDigitalId = pointer.getServiceDigitalId();
                        if (CollectionUtils.isNotEmpty(serviceDigitalId)) {
                            for (TLDigitalIdentification digitalIdentification : serviceDigitalId) {
                                if ((digitalIdentification != null) && (digitalIdentification.getCertificateList() != null)) {
                                    for (TLCertificate cert : digitalIdentification.getCertificateList()) {
                                        if (cert.getToken() == null) {
                                            cert.setTokenFromEncoded();
                                        }
                                        if (lotlCert.equals(cert.getToken())) {
                                            findCert = true;
                                            break;
                                        }
                                    }
                                }
                            }
                            findAllLOTLCertificates &= findCert;
                        }
                    }
                    addResult(check, pointer.getId() + "_" + Tag.SERVICE_DIGITAL_IDENTITY, findAllLOTLCertificates, results);
                }
            }
        }
    }

    private void checkAllDigitalIdsKeyUsages(CheckDTO check, List<TLPointersToOtherTSL> pointers, List<CheckResultDTO> results) {
        for (TLPointersToOtherTSL pointer : pointers) {

            if ((!pointer.getSchemeTerritory().equals(lotlTerritory)) && (pointer.getMimeType() != null) && MimeType.XML.equals(pointer.getMimeType())) {
                List<TLDigitalIdentification> serviceDigitalIds = pointer.getServiceDigitalId();
                if (CollectionUtils.isNotEmpty(serviceDigitalIds)) {
                    for (TLDigitalIdentification digitalIdentification : serviceDigitalIds) {
                        if ((digitalIdentification != null) && (digitalIdentification.getCertificateList() != null)) {
                            for (TLCertificate cert : digitalIdentification.getCertificateList()) {
                                if (cert.getToken() == null) {
                                    cert.setTokenFromEncoded();
                                }
                                addResult(check, cert.getId(), certificateService.hasAllowedKeyUsagesBits(cert.getToken()), results);
                            }
                        }
                    }
                }
            }
        }
    }

    private void checkAllDigitalIdsBasicConstraintCaFalse(CheckDTO check, List<TLPointersToOtherTSL> pointers, List<CheckResultDTO> results) {
        for (TLPointersToOtherTSL pointer : pointers) {
            if ((!pointer.getSchemeTerritory().equals(lotlTerritory)) && (pointer.getMimeType() != null) && MimeType.XML.equals(pointer.getMimeType())) {
                List<TLDigitalIdentification> serviceDigitalIds = pointer.getServiceDigitalId();
                if (CollectionUtils.isNotEmpty(serviceDigitalIds)) {
                    for (TLDigitalIdentification digitalIdentification : serviceDigitalIds) {
                        if ((digitalIdentification != null) && (digitalIdentification.getCertificateList() != null)) {
                            for (TLCertificate cert : digitalIdentification.getCertificateList()) {
                                if (cert.getToken() == null) {
                                    cert.setTokenFromEncoded();
                                }
                                addResult(check, cert.getId(), certificateService.isBasicConstraintCaFalse(cert.getToken()), results);
                            }
                        }
                    }
                }
            }
        }
    }

    private void checkAllDigitalIdsExtendedKeyUsage(CheckDTO check, List<TLPointersToOtherTSL> pointers, List<CheckResultDTO> results) {
        for (TLPointersToOtherTSL pointer : pointers) {
            if ((!pointer.getSchemeTerritory().equals(lotlTerritory)) && (pointer.getMimeType() != null) && MimeType.XML.equals(pointer.getMimeType())) {
                List<TLDigitalIdentification> serviceDigitalIds = pointer.getServiceDigitalId();
                if (CollectionUtils.isNotEmpty(serviceDigitalIds)) {
                    for (TLDigitalIdentification digitalIdentification : serviceDigitalIds) {
                        if ((digitalIdentification != null) && (digitalIdentification.getCertificateList() != null)) {
                            for (TLCertificate cert : digitalIdentification.getCertificateList()) {
                                if (cert.getToken() == null) {
                                    cert.setTokenFromEncoded();
                                }
                                addResult(check, cert.getId(), certificateService.hasTslSigningExtendedKeyUsage(cert.getToken()), results);
                            }
                        }
                    }
                }
            }
        }
    }

    private void checkAllDigitalIdsSubjectKeyIdentifier(CheckDTO check, List<TLPointersToOtherTSL> pointers, List<CheckResultDTO> results) {
        for (TLPointersToOtherTSL pointer : pointers) {
            if ((!pointer.getSchemeTerritory().equals(lotlTerritory)) && (pointer.getMimeType() != null) && MimeType.XML.equals(pointer.getMimeType())) {
                List<TLDigitalIdentification> serviceDigitalIds = pointer.getServiceDigitalId();
                if (CollectionUtils.isNotEmpty(serviceDigitalIds)) {
                    for (TLDigitalIdentification digitalIdentification : serviceDigitalIds) {
                        if ((digitalIdentification != null) && (digitalIdentification.getCertificateList() != null)) {
                            for (TLCertificate cert : digitalIdentification.getCertificateList()) {
                                if (cert.getToken() == null) {
                                    cert.setTokenFromEncoded();
                                }
                                byte[] subjectKeyIdentifier = certificateService.getSubjectKeyIdentifier(cert.getToken());
                                addResult(check, cert.getId(), ArrayUtils.isNotEmpty(subjectKeyIdentifier), results);
                            }
                        }
                    }
                }
            }
        }
    }

    private void checkAllDigitalIdsCountryCodeMatch(CheckDTO check, List<TLPointersToOtherTSL> pointers, boolean isLOTL, List<CheckResultDTO> results) {
        if (isLOTL) {
            for (TLPointersToOtherTSL pointer : pointers) {
                if (!pointer.getSchemeTerritory().equals(lotlTerritory)) {
                    String expectedCountryCode = pointer.getSchemeTerritory();
                    if ((pointer.getMimeType() != null) && MimeType.XML.equals(pointer.getMimeType())) {
                        List<TLDigitalIdentification> serviceDigitalIds = pointer.getServiceDigitalId();
                        if (CollectionUtils.isNotEmpty(serviceDigitalIds)) {
                            for (TLDigitalIdentification digitalIdentification : serviceDigitalIds) {
                                if ((digitalIdentification != null) && (digitalIdentification.getCertificateList() != null)) {
                                    for (TLCertificate cert : digitalIdentification.getCertificateList()) {
                                        if (cert.getToken() == null) {
                                            cert.setTokenFromEncoded();
                                        }
                                        String certificateCountryCode = certificateService.getCountryCode(cert.getToken());
                                        addResult(check, cert.getId(), genericValidator.isEquals(expectedCountryCode, certificateCountryCode), results);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void checkAllDigitalIdsOrganizationMatch(CheckDTO check, List<TLPointersToOtherTSL> pointers, List<CheckResultDTO> results) {
        for (TLPointersToOtherTSL pointer : pointers) {
            List<TLName> schemeOperatorNames = pointer.getSchemeOpeName();
            if ((!pointer.getSchemeTerritory().equals(lotlTerritory)) && (pointer.getMimeType() != null) && MimeType.XML.equals(pointer.getMimeType())) {
                List<TLDigitalIdentification> serviceDigitalIds = pointer.getServiceDigitalId();
                if (CollectionUtils.isNotEmpty(serviceDigitalIds)) {
                    for (TLDigitalIdentification digitalIdentification : serviceDigitalIds) {
                        if ((digitalIdentification != null) && (digitalIdentification.getCertificateList() != null)) {
                            for (TLCertificate cert : digitalIdentification.getCertificateList()) {
                                if (cert.getToken() == null) {
                                    cert.setTokenFromEncoded();
                                }
                                String organization = certificateService.getOrganization(cert.getToken());
                                addResult(check, cert.getId(), isMatch(schemeOperatorNames, organization), results);
                            }
                        }
                    }
                }
            }
        }
    }

    private void checkAllServicesDigitalIdentitiesCorrect(CheckDTO check, List<TLPointersToOtherTSL> pointers, List<CheckResultDTO> results) {
        for (TLPointersToOtherTSL pointer : pointers) {
            List<TLDigitalIdentification> serviceDigitalIds = pointer.getServiceDigitalId();
            if (CollectionUtils.isNotEmpty(serviceDigitalIds)) {
                for (TLDigitalIdentification digitalIdentification : serviceDigitalIds) {
                    addResult(check, digitalIdentification.getId(), genericValidator.isCollectionNotEmpty(digitalIdentification.getCertificateList()), results);
                }
            }

        }
    }

    private boolean isMatch(List<TLName> schemeOperatorNames, String organization) {
        if (CollectionUtils.isNotEmpty(schemeOperatorNames) && StringUtils.isNotEmpty(organization)) {
            for (TLName schemeOperatorName : schemeOperatorNames) {
                if (StringUtils.equalsIgnoreCase(schemeOperatorName.getValue(), organization)) {
                    return true;
                }
            }
        }
        return false;
    }

}
