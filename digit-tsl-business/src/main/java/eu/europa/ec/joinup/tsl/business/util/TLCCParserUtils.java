package eu.europa.ec.joinup.tsl.business.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.joinup.tsl.business.dto.CheckResultDTO;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.Check;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.CriteriaList;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.DigitalId;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.DocumentationReference;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.DocumentationReferences;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.ElectronicAddress;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.Extension;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.Identifier;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.KeyUsage;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.KeyUsageBit;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.Name;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.OtherInformation;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.OtherTSLPointer;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.PointersToOtherTSL;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.PolicyIdentifier;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.PolicySet;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.PostalAddress;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.PostalAddresses;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.QualificationElement;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.Qualifier;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.SchemeInformation;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.ServiceDigitalIdentity;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.ServiceHistoryInstance;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.ServiceInformationExtensions;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.ServiceSupplyPoint;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.Signature;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.TLCCResults;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.TSLLegalNotice;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.TSLTag;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.TSPService;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.Transform;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.TrustServiceProvider;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.TrustServiceProviderList;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.URI;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

/**
 * Parse TLCC XML result and convert to CheckResultDTO
 */
public class TLCCParserUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TLCCParserUtils.class);

    private static final String separator = "_";
    private static final String execution_error_status = "ExecutionError";

    public static List<CheckResultDTO> parseAllTLCC(TLCCResults tlcc, String tlId) {
        List<CheckResultDTO> cR = new ArrayList<>();
        if ((tlId == null) || (tlcc == null)) {
            throw new IllegalStateException("TLCC must be init before call");
        }
        if (tlcc.getTrustServiceStatusList() != null) {
            // Workaround for Signature check on TrustServiceStatusList child (Ask JC for update of TLCC)
            if (!CollectionUtils.isEmpty(tlcc.getTrustServiceStatusList().getCheck())) {
                for (Check check : tlcc.getTrustServiceStatusList().getCheck()) {
                    if ("TSLRules.SIG_MUST_BE_PRESENT".equals(check.getCheckId())) {
                        cR.add(new CheckResultDTO(tlId + separator + Tag.SIGNATURE, check));
                    } else {
                        cR.add(new CheckResultDTO(tlId + separator + Tag.SCHEME_INFORMATION + separator + Tag.Structure, check));
                    }
                }
            }
            if (tlcc.getTrustServiceStatusList().getSignature() != null) {
                cR.addAll(parseSignature(tlcc.getTrustServiceStatusList().getSignature(), tlId));
            }

            if (tlcc.getTrustServiceStatusList().getTSLTag() != null) {
                cR.addAll(parseTSLTag(tlcc.getTrustServiceStatusList().getTSLTag(), tlId));
            }
            if (tlcc.getTrustServiceStatusList().getSchemeInformation() != null) {
                cR.addAll(parseSchemeInformation(tlcc.getTrustServiceStatusList().getSchemeInformation(), tlId));

                if (tlcc.getTrustServiceStatusList().getSchemeInformation().getPointersToOtherTSL() != null) {
                    cR.addAll(parsePointerToOtherTSL(tlcc.getTrustServiceStatusList().getSchemeInformation().getPointersToOtherTSL(), tlId));
                }
            }
            if (tlcc.getTrustServiceStatusList().getTrustServiceProviderList() != null) {
                cR.addAll(parseTrustServiceProvider(tlcc.getTrustServiceStatusList().getTrustServiceProviderList(), tlId));
            }
        }
        return cR;
    }

    public static List<CheckResultDTO> parseSignature(Signature signature, String tlId) {
        List<CheckResultDTO> cR = new ArrayList<>();
        String baseId = tlId + separator + Tag.SIGNATURE;
        addAllChecks(baseId, signature.getCheck(), cR);
        // Signature Value
        if (signature.getSignatureValue() != null) {
            addAllChecks(baseId, signature.getSignatureValue().getCheck(), cR);
        }
        // Signed Info
        if (signature.getSignedInfo() != null) {
            if (signature.getSignedInfo().getCanonicalization() != null) {
                addAllChecks(baseId, signature.getSignedInfo().getCanonicalization().getCheck(), cR);
            }
            if (signature.getSignedInfo().getReference() != null) {
                addAllChecks(baseId, signature.getSignedInfo().getReference().getCheck(), cR);
                if (signature.getSignedInfo().getReference().getTransforms() != null) {
                    addAllChecks(baseId, signature.getSignedInfo().getReference().getTransforms().getCheck(), cR);
                    for (Transform transform : signature.getSignedInfo().getReference().getTransforms().getTransform()) {
                        addAllChecks(baseId, transform.getCheck(), cR);
                    }
                }
            }
        }
        // Key Info
        if ((signature.getKeyInfo() != null) && (signature.getKeyInfo().getX509Data() != null)) {
            addAllChecks(baseId, signature.getKeyInfo().getX509Data().getCheck(), cR);
            if (signature.getKeyInfo().getX509Data().getX509Certificate() != null) {
                addAllChecks(baseId, signature.getKeyInfo().getX509Data().getX509Certificate().getCheck(), cR);
            }
        }
        return cR;
    }

    public static List<CheckResultDTO> parseTSLTag(TSLTag tT, String tlId) {
        List<CheckResultDTO> cR = new ArrayList<>();
        addAllChecks(tlId + separator + Tag.TSL_TAG, tT.getCheck(), cR);
        return cR;
    }

    public static List<CheckResultDTO> parseSchemeInformation(SchemeInformation sI, String tlId) {
        List<CheckResultDTO> cR = new ArrayList<>();
        String baseId = tlId + separator + Tag.SCHEME_INFORMATION;
        // TSL Identifier
        if (sI.getTSLVersionIdentifier() != null) {
            addAllChecks(baseId + separator + Tag.TSL_IDENTIFIER, sI.getTSLVersionIdentifier().getCheck(), cR);
        }
        // Sequence Number
        if (sI.getTSLSequenceNumber() != null) {
            addAllChecks(baseId + separator + Tag.SEQUENCE_NUMBER, sI.getTSLSequenceNumber().getCheck(), cR);
        }
        // TSL Type
        if (sI.getTSLType() != null) {
            addAllChecks(baseId + separator + Tag.TSL_TYPE, sI.getTSLType().getCheck(), cR);
        }
        // Scheme Territory
        if (sI.getSchemeTerritory() != null) {
            addAllChecks(baseId + separator + Tag.TERRITORY, sI.getSchemeTerritory().getCheck(), cR);
        }
        // Scheme Operator Name
        if (sI.getSchemeOperatorName() != null) {
            addAllChecks(baseId + separator + Tag.SCHEME_OPERATOR_NAME, sI.getSchemeOperatorName().getCheck(), cR);
            addAllNameChecks(sI.getSchemeOperatorName().getName(), cR, baseId + separator + Tag.SCHEME_OPERATOR_NAME);
        }
        // Scheme Operator Addresses
        if (sI.getSchemeOperatorAddress() != null) {
            // Electronic Addresses
            if (sI.getSchemeOperatorAddress().getElectronicAddress() != null) {
                addAllElectronicAddressesChecks(sI.getSchemeOperatorAddress().getElectronicAddress(), cR, baseId);
            }
            // Postal Addresses
            if (sI.getSchemeOperatorAddress().getPostalAddresses() != null) {
                addAllPostalAddressesChecks(sI.getSchemeOperatorAddress().getPostalAddresses(), cR, baseId);
            }
        }
        // Scheme Name
        if (sI.getSchemeName() != null) {
            addAllChecks(baseId + separator + Tag.SCHEME_NAME, sI.getSchemeName().getCheck(), cR);
            addAllNameChecks(sI.getSchemeName().getName(), cR, baseId + separator + Tag.SCHEME_NAME);
        }
        // Scheme Information URI
        if (sI.getSchemeInformationURI() != null) {
            addAllChecks(baseId + separator + Tag.SCHEME_INFORMATION_URI, sI.getSchemeInformationURI().getCheck(), cR);
            addAllUriChecks(sI.getSchemeInformationURI().getURI(), cR, baseId + separator + Tag.SCHEME_INFORMATION_URI);
        }
        // Status Determination Approach
        if (sI.getStatusDeterminationApproach() != null) {
            addAllChecks(baseId + separator + Tag.STATUS_DETERMINATION, sI.getStatusDeterminationApproach().getCheck(), cR);
        }
        // Scheme Type Community Rules
        if (sI.getSchemeTypeCommunityRules() != null) {
            addAllChecks(baseId + separator + Tag.SCHEME_TYPE_COMMUNITY_RULES, sI.getSchemeTypeCommunityRules().getCheck(), cR);
            addAllUriChecks(sI.getSchemeTypeCommunityRules().getURI(), cR, baseId + separator + Tag.SCHEME_TYPE_COMMUNITY_RULES);
        }
        // Policy or Legal Notice
        if (sI.getPolicyOrLegalNotice() != null) {
            addAllChecks(baseId + separator + Tag.POLICY_OR_LEGAL_NOTICE, sI.getPolicyOrLegalNotice().getCheck(), cR);
            for (TSLLegalNotice lN : sI.getPolicyOrLegalNotice().getTSLLegalNotice()) {
                addAllChecks(baseId + separator + Tag.POLICY_OR_LEGAL_NOTICE + separator + lN.getIndex() + separator + lN.getLanguage(), lN.getCheck(), cR);
            }
        }
        // Historical Information Period
        if (sI.getHistoricalInformationPeriod() != null) {
            addAllChecks(baseId + separator + Tag.HISTORICAL_PERIOD, sI.getHistoricalInformationPeriod().getCheck(), cR);
        }
        // Next Update
        if (sI.getNextUpdate() != null) {
            addAllChecks(baseId + separator + Tag.NEXT_UPDATE, sI.getNextUpdate().getCheck(), cR);
        }
        // Distribution Points (specific URI, no language)
        if (sI.getDistributionPoints() != null) {
            for (URI uri : sI.getDistributionPoints().getURI()) {
                addAllChecks(baseId + separator + Tag.DISTRIBUTION_LIST + separator + uri.getIndex(), uri.getCheck(), cR);
            }
        }
        return cR;
    }

    public static List<CheckResultDTO> parsePointerToOtherTSL(PointersToOtherTSL pTots, String tlId) {
        List<CheckResultDTO> cR = new ArrayList<>();
        addAllChecks(tlId + separator + Tag.POINTERS_TO_OTHER_TSL, pTots.getCheck(), cR);

        for (OtherTSLPointer ptot : pTots.getOtherTSLPointer()) {
            String baseId = tlId + separator + Tag.POINTERS_TO_OTHER_TSL + separator + ptot.getIndex();
            // Pointer
            addAllChecks(baseId, ptot.getCheck(), cR);
            // TSL Location
            if (ptot.getTSLLocation() != null) {
                addAllChecks(baseId + separator + Tag.POINTER_LOCATION, ptot.getTSLLocation().getCheck(), cR);
            }
            // Additional Information
            if (ptot.getAdditionalInformation() != null) {
                for (OtherInformation oI : ptot.getAdditionalInformation().getOtherInformation()) {
                    addAllChecks(baseId, oI.getCheck(), cR);
                    // TSL Type
                    if (oI.getTSLType() != null) {
                        addAllChecks(baseId + separator + Tag.TSL_TYPE, oI.getTSLType().getCheck(), cR);
                    }
                    // Scheme Territory
                    if (oI.getSchemeTerritory() != null) {
                        addAllChecks(baseId + separator + Tag.POINTER_SCHEME_TERRITORY, oI.getSchemeTerritory().getCheck(), cR);
                    }
                    // Mime Type
                    if (oI.getMimeType() != null) {
                        addAllChecks(baseId + separator + Tag.POINTER_MIME_TYPE, oI.getMimeType().getCheck(), cR);
                    }
                    // Scheme Operator Name
                    if (oI.getSchemeOperatorName() != null) {
                        addAllChecks(baseId + separator + Tag.SCHEME_OPERATOR_NAME, oI.getSchemeOperatorName().getCheck(), cR);
                        addAllNameChecks(oI.getSchemeOperatorName().getName(), cR, baseId + separator + Tag.SCHEME_OPERATOR_NAME);
                    }
                    if (oI.getSchemeTypeCommunityRules() != null) {
                        addAllChecks(baseId + separator + Tag.POINTER_COMMUNITY_RULE, oI.getSchemeTypeCommunityRules().getCheck(), cR);
                        addAllUriChecks(oI.getSchemeTypeCommunityRules().getURI(), cR, baseId + separator + Tag.POINTER_COMMUNITY_RULE);
                    }
                }
            }
            // Service Digital Identities
            if (ptot.getServiceDigitalIdentities() != null) {
                for (ServiceDigitalIdentity sdi : ptot.getServiceDigitalIdentities().getServiceDigitalIdentity()) {
                    String sdiBaseId = baseId + separator + Tag.SERVICE_DIGITAL_IDENTITY + separator;
                    for (DigitalId di : sdi.getDigitalId()) {
                        addAllDigitalIdChecks(di, cR, sdiBaseId + sdi.getIndex());
                    }
                    addAllChecks(sdiBaseId, sdi.getCheck(), cR);
                }
            }
        }
        return cR;
    }

    public static List<CheckResultDTO> parseTrustServiceProvider(TrustServiceProviderList tsps, String tlId) {
        List<CheckResultDTO> cR = new ArrayList<>();
        String baseId = tlId + separator + Tag.TSP_SERVICE_PROVIDER;
        for (TrustServiceProvider tsp : tsps.getTrustServiceProvider()) {
            // Trust Service Provider
            String tspBaseId = baseId + separator + tsp.getIndex();
            if (tsp.getTSPInformation() != null) {
                // TSP Name
                if (tsp.getTSPInformation().getTSPName() != null) {
                    addAllChecks(tspBaseId + separator + Tag.TSP_NAME, tsp.getTSPInformation().getTSPName().getCheck(), cR);
                    addAllNameChecks(tsp.getTSPInformation().getTSPName().getName(), cR, tspBaseId + separator + Tag.TSP_NAME);
                }
                // TSP TradeName
                if (tsp.getTSPInformation().getTSPTradeName() != null) {
                    addAllChecks(tspBaseId + separator + Tag.TSP_TRADE_NAME, tsp.getTSPInformation().getTSPTradeName().getCheck(), cR);
                    addAllNameChecks(tsp.getTSPInformation().getTSPTradeName().getName(), cR, tspBaseId + separator + Tag.TSP_TRADE_NAME);
                }
                // TSP Addresses
                if (tsp.getTSPInformation().getTSPAddress() != null) {
                    // Electronic Addresses
                    if (tsp.getTSPInformation().getTSPAddress().getElectronicAddress() != null) {
                        addAllElectronicAddressesChecks(tsp.getTSPInformation().getTSPAddress().getElectronicAddress(), cR, tspBaseId);
                    }
                    // Postal Addresses
                    if (tsp.getTSPInformation().getTSPAddress().getPostalAddresses() != null) {
                        addAllPostalAddressesChecks(tsp.getTSPInformation().getTSPAddress().getPostalAddresses(), cR, tspBaseId);
                    }
                }
                // Information URI
                if (tsp.getTSPInformation().getTSPInformationURI() != null) {
                    addAllChecks(tspBaseId + separator + Tag.TSP_INFORMATION_URI, tsp.getTSPInformation().getTSPInformationURI().getCheck(), cR);
                    addAllUriChecks(tsp.getTSPInformation().getTSPInformationURI().getURI(), cR, tspBaseId + separator + Tag.TSP_INFORMATION_URI);
                }
                // TSPInformationExtensions
                if (tsp.getTSPInformation().getTSPInformationExtensions() != null) {
                    for (Extension ext : tsp.getTSPInformation().getTSPInformationExtensions().getExtension()) {
                        addAllChecks(tspBaseId + separator + Tag.TSP_INFORMATION_EXT + separator + ext.getIndex(), ext.getCheck(), cR);
                    }
                }
            }
            // Service Provider
            if (tsp.getTSPServices() != null) {
                for (TSPService sp : tsp.getTSPServices().getTSPService()) {
                    cR.addAll(parseServiceProvider(sp, tspBaseId));
                }
            }
        }
        return cR;
    }

    /**
     * @param nodeId
     *            TrustServiceProvider parent id
     */
    public static List<CheckResultDTO> parseServiceProvider(TSPService sp, String parentId) {
        List<CheckResultDTO> cR = new ArrayList<>();
        String baseId = parentId + separator + Tag.TSP_SERVICE + separator + sp.getIndex();

        if (sp.getServiceInformation() != null) {
            addAllChecks(baseId, sp.getServiceInformation().getCheck(), cR);
            // Service Name
            if (sp.getServiceInformation().getServiceName() != null) {
                addAllChecks(baseId + separator + Tag.SERVICE_NAME, sp.getServiceInformation().getServiceName().getCheck(), cR);
                addAllNameChecks(sp.getServiceInformation().getServiceName().getName(), cR, baseId + separator + Tag.SERVICE_NAME);
            }
            // Service Status
            if (sp.getServiceInformation().getServiceStatus() != null) {
                addAllChecks(baseId + separator + Tag.SERVICE_STATUS, sp.getServiceInformation().getServiceStatus().getCheck(), cR);
            }
            // Service Type Identifier
            if (sp.getServiceInformation().getServiceTypeIdentifier() != null) {
                addAllChecks(baseId + separator + Tag.SERVICE_TYPE_IDENTIFIER, sp.getServiceInformation().getServiceTypeIdentifier().getCheck(), cR);
            }
            // Status Starting Time
            if (sp.getServiceInformation().getStatusStartingTime() != null) {
                addAllChecks(baseId + separator + Tag.SERVICE_STATUS_STARTING_TIME, sp.getServiceInformation().getStatusStartingTime().getCheck(), cR);
            }
            // Scheme Service Definition URI
            if (sp.getServiceInformation().getSchemeServiceDefinitionURI() != null) {
                addAllChecks(baseId + separator + Tag.SCHEME_SERVICE_DEFINITION_URI, sp.getServiceInformation().getSchemeServiceDefinitionURI().getCheck(), cR);
                addAllUriChecks(sp.getServiceInformation().getSchemeServiceDefinitionURI().getURI(), cR, baseId + separator + Tag.SCHEME_SERVICE_DEFINITION_URI);
            }
            // Service Supply Points
            if (sp.getServiceInformation().getServiceSupplyPoints() != null) {
                for (ServiceSupplyPoint supply : sp.getServiceInformation().getServiceSupplyPoints().getServiceSupplyPoint()) {
                    addAllChecks(baseId + separator + Tag.SERVICE_SUPPLY_POINT + separator + supply.getIndex(), supply.getCheck(), cR);
                }
            }
            if (sp.getServiceInformation().getTSPServiceDefinitionURI() != null) {
                addAllChecks(baseId + separator + Tag.TSP_SERVICE_DEFINITION_URI, sp.getServiceInformation().getTSPServiceDefinitionURI().getCheck(), cR);
                addAllUriChecks(sp.getServiceInformation().getTSPServiceDefinitionURI().getURI(), cR, baseId + separator + Tag.TSP_SERVICE_DEFINITION_URI);

            }
            // Service Information Extensions
            if (sp.getServiceInformation().getServiceInformationExtensions() != null) {
                addAllServiceExtensionChecks(cR, baseId, sp.getServiceInformation().getServiceInformationExtensions());
            }
            // Service Digital Identity
            if (sp.getServiceInformation().getServiceDigitalIdentity() != null) {
                String sdiBaseId = baseId + separator + Tag.SERVICE_DIGITAL_IDENTITY;
                for (DigitalId di : sp.getServiceInformation().getServiceDigitalIdentity().getDigitalId()) {
                    addAllDigitalIdChecks(di, cR, sdiBaseId + separator + 1);
                }
                addAllChecks(sdiBaseId, sp.getServiceInformation().getServiceDigitalIdentity().getCheck(), cR);
            }
        }
        // Service History
        if ((sp.getServiceHistory() != null) && (sp.getServiceHistory().getServiceHistoryInstance() != null)) {
            for (ServiceHistoryInstance sh : sp.getServiceHistory().getServiceHistoryInstance()) {
                cR.addAll(parseServiceHistory(sh, baseId));
            }
        }

        return cR;
    }

    /**
     * @param nodeId
     *            ServiceProvider parent id
     */
    public static List<CheckResultDTO> parseServiceHistory(ServiceHistoryInstance sh, String parentId) {
        List<CheckResultDTO> cR = new ArrayList<>();
        String baseId = parentId + separator + Tag.SERVICE_HISTORY + separator + sh.getIndex();

        addAllChecks(baseId, sh.getCheck(), cR);
        // History Digital Identities
        if (sh.getServiceDigitalIdentity() != null) {
            String diBaseId = baseId + separator + Tag.SERVICE_DIGITAL_IDENTITY + separator + "1";
            for (DigitalId di : sh.getServiceDigitalIdentity().getDigitalId()) {
                addAllDigitalIdChecks(di, cR, diBaseId);
            }
        }
        // History Type Identifier
        if (sh.getServiceTypeIdentifier() != null) {
            addAllChecks(baseId + separator + Tag.SERVICE_TYPE_IDENTIFIER, sh.getServiceTypeIdentifier().getCheck(), cR);

        }
        // History Name
        if (sh.getServiceName() != null) {
            addAllChecks(baseId + separator + Tag.SERVICE_NAME, sh.getServiceName().getCheck(), cR);
            addAllNameChecks(sh.getServiceName().getName(), cR, baseId + separator + Tag.SERVICE_NAME);
        }
        // History Status
        if (sh.getServiceStatus() != null) {
            addAllChecks(baseId + separator + Tag.SERVICE_STATUS, sh.getServiceStatus().getCheck(), cR);
        }
        // History Starting Time
        if (sh.getStatusStartingTime() != null) {
            addAllChecks(baseId + separator + Tag.SERVICE_STATUS_STARTING_TIME, sh.getStatusStartingTime().getCheck(), cR);
        }
        // History Informations Extension
        if (sh.getServiceInformationExtensions() != null) {
            addAllServiceExtensionChecks(cR, baseId, sh.getServiceInformationExtensions());
        }
        return cR;
    }

    /* GENERIC METHOD */

    /**
     * Get Name checks
     */
    private static void addAllNameChecks(List<Name> names, List<CheckResultDTO> cR, String baseId) {
        for (Name name : ListUtils.emptyIfNull(names)) {
            addAllChecks(baseId + separator + name.getIndex() + separator + name.getLanguage(), name.getCheck(), cR);
        }
    }

    /**
     * Get ElectronicAddresses checks
     */
    private static void addAllElectronicAddressesChecks(ElectronicAddress eAs, List<CheckResultDTO> cR, String baseId) {
        addAllChecks(baseId + separator + Tag.ELECTRONIC_ADDRESS, eAs.getCheck(), cR);
        addAllUriChecks(eAs.getURI(), cR, baseId + separator + Tag.ELECTRONIC_ADDRESS);
    }

    /**
     * Get URI checks
     */
    private static void addAllUriChecks(List<URI> uris, List<CheckResultDTO> cR, String baseId) {
        for (URI uri : ListUtils.emptyIfNull(uris)) {
            addAllChecks(baseId + separator + uri.getIndex() + separator + uri.getLanguage(), uri.getCheck(), cR);
        }
    }

    /**
     * Get PostalAddresses checks
     */
    private static void addAllPostalAddressesChecks(PostalAddresses pAs, List<CheckResultDTO> cR, String baseId) {
        addAllChecks(baseId + separator + Tag.POSTAL_ADDRESSES, pAs.getCheck(), cR);
        for (PostalAddress pA : ListUtils.emptyIfNull(pAs.getPostalAddress())) {
            String pAId = baseId + separator + Tag.POSTAL_ADDRESSES + separator + pA.getIndex() + separator + pA.getLanguage();
            addAllChecks(pAId, pA.getCheck(), cR);
            if (pA.getCountryName() != null) {
                addAllChecks(pAId + separator + Tag.POSTAL_ADDRESS_COUNTRY, pA.getCountryName().getCheck(), cR);
            }
            if (pA.getLocality() != null) {
                addAllChecks(pAId + separator + Tag.POSTAL_ADDRESS_LOCALITY, pA.getLocality().getCheck(), cR);
            }
            if (pA.getPostalCode() != null) {
                addAllChecks(pAId + separator + Tag.POSTAL_ADDRESS_CODE, pA.getPostalCode().getCheck(), cR);
            }
            if (pA.getStreetAddress() != null) {
                addAllChecks(pAId + separator + Tag.POSTAL_ADDRESS_STREET, pA.getStreetAddress().getCheck(), cR);
            }
            if (pA.getStateOrProvince() != null) {
                addAllChecks(pAId + separator + Tag.POSTAL_ADDRESS_PROVINCE, pA.getStateOrProvince().getCheck(), cR);
            }
        }
    }

    /**
     * Get DigitalId checks
     */
    private static void addAllDigitalIdChecks(DigitalId di, List<CheckResultDTO> cR, String diBaseId) {
        // Checks
        addAllChecks(diBaseId, di.getCheck(), cR);

        // X509 Certificate
        if (di.getX509Certificate() != null) {
            addAllChecks(diBaseId + separator + Tag.X509_CERTIFICATE + separator + di.getIndex(), di.getX509Certificate().getCheck(), cR);
        }
        // X509 SKI
        if (di.getX509SKI() != null) {
            addAllChecks(diBaseId + separator + Tag.X509_SKI + separator + di.getIndex(), di.getX509SKI().getCheck(), cR);
        }
        // X509 Subject Name
        if (di.getX509SubjectName() != null) {
            addAllChecks(diBaseId + separator + Tag.X509_SUBJECT_NAME + separator + di.getIndex(), di.getX509SubjectName().getCheck(), cR);
        }

        // Other
        if (di.getOther() != null) {
            addAllChecks(diBaseId + separator + Tag.OTHER, di.getOther().getCheck(), cR);
        }
    }

    /**
     * Get ServiceExtension checks
     */
    private static void addAllServiceExtensionChecks(List<CheckResultDTO> cR, String baseId, ServiceInformationExtensions sie) {
        for (Extension ext : sie.getExtension()) {
            String extBaseId = baseId + separator + Tag.SERVICE_EXTENSION + separator + ext.getIndex();
            // Taken Over By
            if (ext.getTakenOverBy() != null) {
                String tobBaseId = extBaseId + separator + Tag.SERVICE_TAKEN_OVER_BY;
                addAllChecks(tobBaseId, ext.getTakenOverBy().getCheck(), cR);
                // TOB : Scheme Territory
                if (ext.getTakenOverBy().getSchemeTerritory() != null) {
                    addAllChecks(tobBaseId + separator + Tag.TERRITORY, ext.getTakenOverBy().getSchemeTerritory().getCheck(), cR);
                }
                // TOB : TSP Name
                if (ext.getTakenOverBy().getTSPName() != null) {
                    addAllChecks(tobBaseId, ext.getTakenOverBy().getTSPName().getCheck(), cR);
                    addAllNameChecks(ext.getTakenOverBy().getTSPName().getName(), cR, tobBaseId + separator + Tag.TSP_NAME);
                }
                // TOB : URI
                if (ext.getTakenOverBy().getURI() != null) {
                    addAllUriChecks(ext.getTakenOverBy().getURI(), cR, tobBaseId + separator + Tag.INFORMATION_URI);
                }
                // TOB : Operator Name
                if (ext.getTakenOverBy().getSchemeOperatorName() != null) {
                    addAllChecks(tobBaseId, ext.getTakenOverBy().getSchemeOperatorName().getCheck(), cR);
                    addAllNameChecks(ext.getTakenOverBy().getSchemeOperatorName().getName(), cR, tobBaseId + separator + Tag.SCHEME_OPERATOR_NAME);
                }
            }
            // Additional Service Information (specific URI , no index)
            if ((ext.getAdditionalServiceInformation() != null) && (ext.getAdditionalServiceInformation().getURI() != null)) {
                addAllChecks(extBaseId + separator + Tag.SERVICE_ADDITIONNAL_EXT, ext.getAdditionalServiceInformation().getURI().getCheck(), cR);
            }
            // ExpiredCertsRevocationInfo
            if (ext.getExpiredCertsRevocationInfo() != null) {
                addAllChecks(extBaseId + separator + Tag.SERVICE_EXPIRED_CERT_REVOCATION, ext.getExpiredCertsRevocationInfo().getCheck(), cR);
            }
            // Qualifications
            if ((ext.getQualifications() != null) && (ext.getQualifications().getQualificationElement() != null)) {
                // Qualifiers
                for (QualificationElement qe : ext.getQualifications().getQualificationElement()) {
                    if (qe.getQualifiers() != null) {
                        for (Qualifier qualifier : qe.getQualifiers().getQualifier()) {
                            addAllUriChecks(qualifier.getURI(), cR, extBaseId + separator + Tag.SERVICE_QUALIFICATION_QUALIFIERS + separator + qualifier.getIndex());
                        }
                    }
                    // Criteria
                    if (qe.getCriteriaList() != null) {
                        addAllCriteriaListChecks(cR, qe.getCriteriaList(), extBaseId);
                    }

                    // Checks
                    if (!CollectionUtils.isEmpty(qe.getCheck())) {
                        addAllChecks(extBaseId + separator + Tag.SERVICE_QUALIFICATION_QUALIFIERS, qe.getCheck(), cR);
                    }
                }
            }

        }
    }

    /**
     * Get CriteriaList checks
     */
    private static void addAllCriteriaListChecks(List<CheckResultDTO> cR, CriteriaList criteriaList, String extBaseId) {
        addAllChecks(extBaseId, criteriaList.getCheck(), cR);
        // KeyUsage or PolicyList
        for (Object criteria : criteriaList.getCriteriaListOrKeyUsageOrPolicySet()) {
            if (criteria instanceof KeyUsage) {
                KeyUsage ks = (KeyUsage) criteria;
                for (KeyUsageBit kub : ks.getKeyUsageBit()) {
                    addAllChecks(extBaseId, kub.getCheck(), cR);
                }
            } else if (criteria instanceof PolicySet) {
                PolicySet ps = (PolicySet) criteria;
                for (PolicyIdentifier pi : ps.getPolicyIdentifier()) {
                    // Identifier
                    for (Identifier id : pi.getIdentifier()) {
                        addAllChecks(extBaseId, id.getCheck(), cR);
                    }

                    for (DocumentationReferences drs : pi.getDocumentationReferences()) {
                        for (Object codr : drs.getCheckOrDocumentationReference()) {
                            if (codr instanceof DocumentationReference) {
                                @SuppressWarnings("unused")
                                DocumentationReference dr = (DocumentationReference) codr;
                            } else if (codr instanceof Check) {
                                cR.add(new CheckResultDTO(extBaseId, (Check) codr));
                            }
                        }
                    }
                }

            } else if (criteria instanceof CriteriaList) {
                CriteriaList cl = (CriteriaList) criteria;
                addAllCriteriaListChecks(cR, cl, extBaseId);
            }
        }
        // Other Criteria
        if (criteriaList.getOtherCriteriaList() != null) {
            // CertSubjectDNAttribute
            if ((criteriaList.getOtherCriteriaList().getCertSubjectDNAttribute() != null) && (criteriaList.getOtherCriteriaList().getCertSubjectDNAttribute().getAttributeOID() != null)) {
                for (Identifier id : criteriaList.getOtherCriteriaList().getCertSubjectDNAttribute().getAttributeOID().getIdentifier()) {
                    addAllChecks(extBaseId, id.getCheck(), cR);
                }
            }
            // ExtendedKeyUsage
            if ((criteriaList.getOtherCriteriaList().getExtendedKeyUsage() != null) && (criteriaList.getOtherCriteriaList().getExtendedKeyUsage().getKeyPurposeId() != null)) {
                for (Identifier id : criteriaList.getOtherCriteriaList().getExtendedKeyUsage().getKeyPurposeId().getIdentifier()) {
                    addAllChecks(extBaseId, id.getCheck(), cR);
                }
            }
        }
    }

    /**
     * Add list of TLCC Check
     *
     * @param id
     * @param checks
     * @param cR
     */
    private static void addAllChecks(String id, List<Check> checks, List<CheckResultDTO> cR) {
        for (Check check : checks) {
            if (check.getStatus().equals(execution_error_status)) {
                LOGGER.debug("Execution error - id : " + id + "; check : " + check.toString());
            } else {
                cR.add(new CheckResultDTO(id, check));
            }
        }
    }

}
