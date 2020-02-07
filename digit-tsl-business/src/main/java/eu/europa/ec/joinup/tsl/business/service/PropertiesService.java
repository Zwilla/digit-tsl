package eu.europa.ec.joinup.tsl.business.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.Properties;
import eu.europa.ec.joinup.tsl.business.repository.PropertiesListRepository;
import eu.europa.ec.joinup.tsl.business.repository.PropertiesRepository;
import eu.europa.ec.joinup.tsl.model.DBProperties;
import eu.europa.ec.joinup.tsl.model.DBPropertiesList;

/**
 * Trusted list standard properties management
 */
@Service
@Transactional(value = TxType.REQUIRED)
public class PropertiesService {

    private static final String LANGUAGE_PROPERTY_CODE = "LANGUAGES";

    private static final String PREFIX_URI_PROPERTY_CODE = "ADRTYPE";

    private static final String TSL_TAG_VALUE_PROPERTY_CODE = "TSL_TAG_VALUE";

    private static final String TSL_VERSION_IDENTIFIER_VALUE_PROPERTY_CODE = "TSL_VERSION_IDENTIFIER_VALUE";

    private static final String LOTL_TSL_TYPE_PROPERTY_CODE = "LOTL_EU_TYPE";

    private static final String TL_TSL_TYPE_PROPERTY_CODE = "TL_EU_TYPE";

    private static final String LOTL_STATUS_DETERMINATION_APPROACH_PROPERTY_CODE = "LOTL_EU_STATUSDETERM";

    private static final String TL_STATUS_DETERMINATION_APPROACH_PROPERTY_CODE = "TL_STATUS_DETERM_TYPE";

    private static final String LOTL_SCHEME_COMMUNITY_RULES_PROPERTY_CODE = "LOTL_SCHMETYPECOMMUNITY";

    private static final String TL_SCHEME_COMMUNITY_RULES_PROPERTY_CODE = "TL_COMMUNITYRULE";

    private static final String SERVICE_TYPES_IDENTIFIERS_PROPERTY_CODE = "SERVICE_TYPES_IDENTIFIERS";

    private static final String SERVICE_STATUS_PROPERTY_CODE = "SERVICE_STATUS";

    private static final String QUALIFIERS_PROPERTY_CODE = "QUALIFIERS";

    private static final String HISTORICAL_INFO_PERIOD_PROPERTY_CODE = "HISTORICAL_INFO_PERIOD";

    private static final String ENGLISH_SCHEME_NAME_PROPERTY_CODE = "ENGLISH_SCHEME_NAME";

    private static final String ENGLISH_LEGAL_NOTICE_PROPERTY_CODE = "ENGLISH_LEGAL_NOTICE";

    @Autowired
    private PropertiesListRepository propListRepository;

    @Autowired
    private PropertiesRepository propRepo;

    public List<Properties> getProperties() {
        List<Properties> propList = new ArrayList<>();
        List<DBPropertiesList> dbPropListList = (List<DBPropertiesList>) propListRepository.findAll();
        for (DBPropertiesList dbPropList : dbPropListList) {
            convertToDTOs(propList, dbPropList);
        }
        return propList;
    }

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public List<String> getPrefixUris() {
        DBPropertiesList propertiesList = propListRepository.findOne(PREFIX_URI_PROPERTY_CODE);
        return extractLabels(propertiesList);
    }

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public List<String> getServiceTypeIdentifiers() {
        DBPropertiesList propertiesList = propListRepository.findOne(SERVICE_TYPES_IDENTIFIERS_PROPERTY_CODE);
        return extractLabels(propertiesList);
    }

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public List<String> getQualifiers() {
        DBPropertiesList propertiesList = propListRepository.findOne(QUALIFIERS_PROPERTY_CODE);
        return extractLabels(propertiesList);
    }

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public List<String> getServiceStatus() {
        DBPropertiesList propertiesList = propListRepository.findOne(SERVICE_STATUS_PROPERTY_CODE);
        return extractLabels(propertiesList);
    }

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public List<Properties> getLanguages() {
        List<Properties> propList = new ArrayList<>();
        DBPropertiesList propertiesList = propListRepository.findOne(LANGUAGE_PROPERTY_CODE);
        convertToDTOs(propList, propertiesList);
        return propList;
    }

    private void convertToDTOs(List<Properties> propList, DBPropertiesList dbPropertiesList) {
        if ((dbPropertiesList != null) && CollectionUtils.isNotEmpty(dbPropertiesList.getPropertiesInfo())) {
            for (DBProperties dbProp : dbPropertiesList.getPropertiesInfo()) {
                propList.add(new Properties(dbPropertiesList.getCode(), dbProp));
            }
        }
    }

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public String getTSLTagValue() {
        DBPropertiesList propertiesList = propListRepository.findOne(TSL_TAG_VALUE_PROPERTY_CODE);
        return getStringValue(propertiesList);
    }

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public String getLOTLTSLTypeValue() {
        DBPropertiesList propertiesList = propListRepository.findOne(LOTL_TSL_TYPE_PROPERTY_CODE);
        return getStringValue(propertiesList);
    }

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public String getTLTSLTypeValue() {
        DBPropertiesList propertiesList = propListRepository.findOne(TL_TSL_TYPE_PROPERTY_CODE);
        return getStringValue(propertiesList);
    }

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public Integer getHistoryInformationPeriod() {
        DBPropertiesList propertiesList = propListRepository.findOne(HISTORICAL_INFO_PERIOD_PROPERTY_CODE);
        return getIntegerValue(propertiesList);
    }

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public String getLOTLSchemeCommunityRulesValue() {
        DBPropertiesList propertiesList = propListRepository.findOne(LOTL_SCHEME_COMMUNITY_RULES_PROPERTY_CODE);
        return getStringValue(propertiesList);
    }

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public List<String> getTLSchemeCommunityRulesValues() {
        DBPropertiesList propertiesList = propListRepository.findOne(TL_SCHEME_COMMUNITY_RULES_PROPERTY_CODE);
        return extractLabels(propertiesList);
    }

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public String getTSLVersionIdentifier() {
        DBPropertiesList propertiesList = propListRepository.findOne(TSL_VERSION_IDENTIFIER_VALUE_PROPERTY_CODE);
        return getStringValue(propertiesList);
    }

    private List<String> extractLabels(DBPropertiesList propertiesList) {
        List<String> labels = new ArrayList<>();
        for (DBProperties dbProp : propertiesList.getPropertiesInfo()) {
            labels.add(dbProp.getLabel());
        }
        return labels;
    }

    private String getStringValue(DBPropertiesList propertiesList) {
        List<DBProperties> propertiesInfo = propertiesList.getPropertiesInfo();
        if (CollectionUtils.size(propertiesInfo) == 1) {
            return propertiesInfo.get(0).getLabel();
        }
        return StringUtils.EMPTY;
    }

    private Integer getIntegerValue(DBPropertiesList propertiesList) {
        List<DBProperties> propertiesInfo = propertiesList.getPropertiesInfo();
        if (CollectionUtils.size(propertiesInfo) == 1) {
            return Integer.parseInt(propertiesInfo.get(0).getLabel());
        }
        return null;
    }

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public String getLOTLStatusDeterminationApproachValue() {
        DBPropertiesList propertiesList = propListRepository.findOne(LOTL_STATUS_DETERMINATION_APPROACH_PROPERTY_CODE);
        return getStringValue(propertiesList);
    }

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public String getTLStatusDeterminationApproachValue() {
        DBPropertiesList propertiesList = propListRepository.findOne(TL_STATUS_DETERMINATION_APPROACH_PROPERTY_CODE);
        return getStringValue(propertiesList);
    }

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public String getServiceTypeNationalRootCAQC() {
        List<String> serviceTypeIdentifiers = getServiceTypeIdentifiers();
        if (CollectionUtils.isNotEmpty(serviceTypeIdentifiers)) {
            for (String typeIdentifier : serviceTypeIdentifiers) {
                if (StringUtils.contains(typeIdentifier, "NationalRootCA-QC")) {
                    return typeIdentifier;
                }
            }
        }
        return StringUtils.EMPTY;
    }

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public List<String> getStatusForNationalRootCAQC() {
        List<String> results = new ArrayList<>();
        List<String> statusList = getServiceStatus();
        if (CollectionUtils.isNotEmpty(statusList)) {
            for (String status : statusList) {
                if (StringUtils.endsWith(status, "bynationallaw")) {
                    results.add(status);
                }
            }
        }
        return results;
    }

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public List<String> getServiceTypeForAsiForeSignatureChecks() {
        List<String> results = new ArrayList<>();
        List<String> serviceTypeIdentifiers = getServiceTypeIdentifiers();
        if (CollectionUtils.isNotEmpty(serviceTypeIdentifiers)) {
            for (String typeIdentifier : serviceTypeIdentifiers) {
                if (StringUtils.endsWith(typeIdentifier, "CA/QC") || StringUtils.endsWith(typeIdentifier, "OCSP/QC") || StringUtils.endsWith(typeIdentifier, "CRL/QC")) {
                    results.add(typeIdentifier);
                }
            }
        }
        return results;
    }

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public List<String> getServiceTypeForAsiForeSignatureSealChecks() {
        List<String> results = new ArrayList<>();
        List<String> serviceTypeIdentifiers = getServiceTypeIdentifiers();
        if (CollectionUtils.isNotEmpty(serviceTypeIdentifiers)) {
            for (String typeIdentifier : serviceTypeIdentifiers) {
                if (StringUtils.endsWith(typeIdentifier, "PSES/Q") || StringUtils.endsWith(typeIdentifier, "QESValidation/Q")) {
                    results.add(typeIdentifier);
                }
            }
        }
        return results;
    }

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public List<String> getServiceTypeForExpiredCertRevocationInfoChecks() {
        List<String> results = new ArrayList<>();
        List<String> serviceTypeIdentifiers = getServiceTypeIdentifiers();
        if (CollectionUtils.isNotEmpty(serviceTypeIdentifiers)) {
            for (String typeIdentifier : serviceTypeIdentifiers) {
                if (StringUtils.endsWith(typeIdentifier, "CA/PKC") || StringUtils.endsWith(typeIdentifier, "OCSP") || StringUtils.endsWith(typeIdentifier, "CRL")
                        || StringUtils.endsWith(typeIdentifier, "CA/QC") || StringUtils.endsWith(typeIdentifier, "NationalRootCA-QC") || StringUtils.endsWith(typeIdentifier, "Certstatus/OCSP/QC")
                        || StringUtils.endsWith(typeIdentifier, "Certstatus/CRL/QC")) {
                    results.add(typeIdentifier);
                }
            }
        }
        return results;
    }

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public List<String> getServiceTypeForAsiForeChecks() {
        List<String> results = new ArrayList<>();
        List<String> serviceTypeIdentifiers = getServiceTypeIdentifiers();
        if (CollectionUtils.isNotEmpty(serviceTypeIdentifiers)) {
            for (String typeIdentifier : serviceTypeIdentifiers) {
                if (StringUtils.endsWith(typeIdentifier, "CA/PKC") || StringUtils.endsWith(typeIdentifier, "OCSP") || StringUtils.endsWith(typeIdentifier, "CRL")
                        || StringUtils.endsWith(typeIdentifier, "PSES") || StringUtils.endsWith(typeIdentifier, "AdESValidation") || StringUtils.endsWith(typeIdentifier, "AdESGeneration")) {
                    results.add(typeIdentifier);
                }
            }
        }
        return results;
    }

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public String getEnglishSchemeNameText() {
        DBPropertiesList propertiesList = propListRepository.findOne(ENGLISH_SCHEME_NAME_PROPERTY_CODE);
        return getStringValue(propertiesList);
    }

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public String getEnglishLegalNoticeText() {
        DBPropertiesList propertiesList = propListRepository.findOne(ENGLISH_LEGAL_NOTICE_PROPERTY_CODE);
        return getStringValue(propertiesList);
    }

    public Properties add(Properties prop) {
        DBProperties dbProp = new DBProperties();
        DBPropertiesList dbList = propListRepository.findOne(prop.getCodeList());
        if (dbList != null) {
            dbProp.setLabel(prop.getLabel());
            dbProp.setDescription(prop.getDescription());
            dbProp.setPropertiesList(dbList);
            propRepo.save(dbProp);
            return new Properties(dbList.getCode(), dbProp);
        } else {
            return null;
        }
    }

    public void delete(Properties prop) {
        propRepo.delete(prop.getId());
    }

}
