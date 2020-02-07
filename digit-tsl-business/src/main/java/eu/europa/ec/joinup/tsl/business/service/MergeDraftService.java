package eu.europa.ec.joinup.tsl.business.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.Load;
import eu.europa.ec.joinup.tsl.business.dto.TrustedListsReport;
import eu.europa.ec.joinup.tsl.business.dto.merge.MergeEntryDTO;
import eu.europa.ec.joinup.tsl.business.dto.merge.MergeResultDTO;
import eu.europa.ec.joinup.tsl.business.dto.merge.MergeStatus;
import eu.europa.ec.joinup.tsl.business.dto.merge.TLMergeResultDTO;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLDistributionPoint;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLElectronicAddress;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLInformationUri;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLName;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPostalAddress;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLSchemeInformation;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLSchemePolicy;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLSchemeTypeCommunityRule;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;
import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

@Service
public class MergeDraftService {

    @Autowired
    private TLService tlService;

    @Autowired
    private TLDraftService draftService;

    @Autowired
    private TrustedListJaxbService jaxbService;

    @Autowired
    private FileService fileService;

    @Autowired
    private TLRepository tlRepository;

    private static final ResourceBundle bundle = ResourceBundle.getBundle("messages");

    private static final Logger LOGGER = LoggerFactory.getLogger(MergeDraftService.class);


    /**
     * Get list of changes between PROD and a list of drafts. Changes are group by location with the related drafts TL
     * 
     * @param countryCode
     * @param drafts
     */
    public TLMergeResultDTO getTLMergeResult(String countryCode, List<TrustedListsReport> drafts) {
        TLMergeResultDTO result = new TLMergeResultDTO();
        TL prodTL = tlService.getPublishedTLByCountryCode(countryCode);
        for (TrustedListsReport draft : drafts) {
            TL tl = tlService.getTL(draft.getId());
            // Detect Scheme information merge action
            getSIMerge(result.getSiResult(), prodTL, tl);
            // Detect PTOTLs merge action
            getPTOTLsMerge(result.getPointerResult(), prodTL, tl);
            // Detect TPSs merge action
            getTSPsMerge(result.getTspResult(), prodTL, tl);
            result.getDraftIds().add(draft.getId());

        }
        return result;
    }

    /**
     * Get merge action to perform on Scheme information. Detect update on Operator && Scheme name/Postal && Electronic address/Status
     * determination/Information URI/Community rules/Legal notices/Distribution points
     * 
     * @param prodTL
     * @param tl
     */
    private void getSIMerge(MergeResultDTO siResult, TL prodTL, TL tl) {
        TLSchemeInformation tlSI = tl.getSchemeInformation();
        TLSchemeInformation prodSI = prodTL.getSchemeInformation();
        if (!prodSI.getSchemeOpeName().equals(tlSI.getSchemeOpeName())) {
            siResult.addEntry(bundle.getString("schemeOpeName"), Tag.SCHEME_OPERATOR_NAME, MergeStatus.UPDATE, tlSI.getSchemeOpeName(), tl.getDbName());
        }
        if (!prodSI.getSchemeOpePostal().equals(tlSI.getSchemeOpePostal())) {
            siResult.addEntry(bundle.getString("tlBrowser.postalAddress"), Tag.POSTAL_ADDRESSES, MergeStatus.UPDATE, tlSI.getSchemeOpePostal(), tl.getDbName());
        }
        if (!prodSI.getSchemeOpeElectronic().equals(tlSI.getSchemeOpeElectronic())) {
            siResult.addEntry(bundle.getString("tlBrowser.electronicAddress"), Tag.ELECTRONIC_ADDRESS, MergeStatus.UPDATE, tlSI.getSchemeOpeElectronic(), tl.getDbName());
        }
        if (!prodSI.getSchemeName().equals(tlSI.getSchemeName())) {
            siResult.addEntry(bundle.getString("schemeInfo.schemeName"), Tag.SCHEME_NAME, MergeStatus.UPDATE, tlSI.getSchemeName(), tl.getDbName());
        }
        if (!prodSI.getSchemeInfoUri().equals(tlSI.getSchemeInfoUri())) {
            siResult.addEntry(bundle.getString("serviceProvider.informationURI"), Tag.SCHEME_INFORMATION_URI, MergeStatus.UPDATE, tlSI.getSchemeInfoUri(), tl.getDbName());
        }
        if (!prodSI.getStatusDetermination().equals(tlSI.getStatusDetermination())) {
            siResult.addEntry(bundle.getString("schemeInfo.statusDetermination"), Tag.STATUS_DETERMINATION, MergeStatus.UPDATE, tlSI.getStatusDetermination(), tl.getDbName());
        }
        if (!prodSI.getSchemeTypeCommRule().equals(tlSI.getSchemeTypeCommRule())) {
            siResult.addEntry(bundle.getString("schemeInfo.communityRule"), Tag.SCHEME_TYPE_COMMUNITY_RULES, MergeStatus.UPDATE, tlSI.getSchemeTypeCommRule(), tl.getDbName());
        }
        if (!prodSI.getSchemePolicy().equals(tlSI.getSchemePolicy())) {
            siResult.addEntry(bundle.getString("schemeInfo.legalNotice"), Tag.POLICY_OR_LEGAL_NOTICE, MergeStatus.UPDATE, tlSI.getSchemePolicy(), tl.getDbName());
        }
        if (!prodSI.getDistributionPoint().equals(tlSI.getDistributionPoint())) {
            siResult.addEntry(bundle.getString("schemeInfo.distributionList"), Tag.DISTRIBUTION_LIST, MergeStatus.UPDATE, tlSI.getDistributionPoint(), tl.getDbName());
        }
    }

    /**
     * Get merge action to perform on PTOTLs. Detect Update/New/Deleted entries
     * 
     * @param prodTL
     * @param tl
     */
    private void getPTOTLsMerge(MergeResultDTO ptotlResult, TL prodTL, TL tl) {
        List<TLPointersToOtherTSL> prodPTOTLs = new ArrayList<>(prodTL.getPointers());
        List<TLPointersToOtherTSL> tlPTOTLs = new ArrayList<>(tl.getPointers());
        // Clean tmp list of PTOTLs
        prodPTOTLs.removeAll(tl.getPointers());
        tlPTOTLs.removeAll(prodTL.getPointers());

        for (TLPointersToOtherTSL tlPTOTL : tlPTOTLs) {
            boolean ptotlFound = false;
            TLPointersToOtherTSL prodPTOTLtoRemove = null;
            String tlPTOTLLocation = getPTOTLLocation(tlPTOTL);
            for (TLPointersToOtherTSL prodPTOTL : prodPTOTLs) {
                if (tlPTOTLLocation.equals(getPTOTLLocation(prodPTOTL))) {
                    ptotlFound = true;
                    prodPTOTLtoRemove = prodPTOTL;
                    break;
                }
            }

            if (ptotlFound) {
                // Updated entry
                ptotlResult.addEntry(tlPTOTLLocation, Tag.POINTERS_TO_OTHER_TSL, MergeStatus.UPDATE, tlPTOTL, tl.getDbName());
                prodPTOTLs.remove(prodPTOTLtoRemove);
            } else {
                // New entry
                ptotlResult.addEntry(tlPTOTLLocation, Tag.POINTERS_TO_OTHER_TSL, MergeStatus.NEW, tlPTOTL, tl.getDbName());
            }
        }

        if (!CollectionUtils.isEmpty(prodPTOTLs)) {
            // Deleted entries (remaining prod PTOTL)
            for (TLPointersToOtherTSL prodPTOTL : prodPTOTLs) {
                ptotlResult.addEntry(getPTOTLLocation(prodPTOTL), Tag.POINTERS_TO_OTHER_TSL, MergeStatus.DELETE, prodPTOTL, tl.getDbName());
            }
        }
    }

    /**
     * Get pointer location (Mime type - Territory)
     * 
     * @param ptotl
     */
    private String getPTOTLLocation(TLPointersToOtherTSL ptotl) {
        return ptotl.getMimeType() + " - " + ptotl.getSchemeTerritory();
    }

    /**
     * Get merge action to perform on TSPs. Detect Update/New/Deleted entries
     * 
     * @param prodTL
     * @param tl
     */
    private void getTSPsMerge(MergeResultDTO tspResult, TL prodTL, TL tl) {
        List<TLServiceProvider> prodTSPs = new ArrayList<>(prodTL.getServiceProviders());
        List<TLServiceProvider> tlTSPs = new ArrayList<>(tl.getServiceProviders());
        // Clean tmp list of TSPs
        prodTSPs.removeAll(tl.getServiceProviders());
        tlTSPs.removeAll(prodTL.getServiceProviders());
        for (TLServiceProvider tlTSP : tlTSPs) {
            boolean tspFound = false;
            TLServiceProvider prodTSPtoRemove = null;
            for (TLServiceProvider prodTSP : prodTSPs) {
                if (tlTSP.getName().equals(prodTSP.getName())) {
                    tspFound = true;
                    prodTSPtoRemove = prodTSP;
                    break;
                }
            }

            if (tspFound) {
                // Updated entry
                tspResult.addEntry(tlTSP.getName(), Tag.TSP_SERVICE_PROVIDER, MergeStatus.UPDATE, tlTSP, tl.getDbName());
                prodTSPs.remove(prodTSPtoRemove);
            } else {
                // New entry
                tspResult.addEntry(tlTSP.getName(), Tag.TSP_SERVICE_PROVIDER, MergeStatus.NEW, tlTSP, tl.getDbName());
            }
        }

        if (!CollectionUtils.isEmpty(prodTSPs)) {
            // Deleted entries (remaining prod TSPs)
            for (TLServiceProvider prodTSP : prodTSPs) {
                tspResult.addEntry(prodTSP.getName(), Tag.TSP_SERVICE_PROVIDER, MergeStatus.DELETE, prodTSP, tl.getDbName());
            }
        }
    }

    /** ----- ----- Merge drafts ----- ----- **/

    public DBTrustedLists mergeDrafts(String countryCode, String cookie, TLMergeResultDTO result) {
        Load load = new Load();
        load.setNew(false);
        DBTrustedLists dbDraft = draftService.cloneTLtoDraft(countryCode, cookie, "test", load);
        TL tlDraft = tlService.getDtoTL(dbDraft);
        // Merge Scheme information
        mergeSchemeInformation(result.getSiResult().getMergeResult(), tlDraft);
        // Merge pointer to other TSLs
        mergePTOTSL(result.getPointerResult().getMergeResult(), tlDraft);
        // Merge trust service providers
        mergeTSPs(result.getTspResult().getMergeResult(), tlDraft);

        byte[] marshallToBytesAsV5 = jaxbService.marshallToBytesAsV5(tlDraft);
        DBFiles xmlFile = dbDraft.getXmlFile();
        xmlFile.setLocalPath(fileService.storeNewDraftTL(xmlFile.getMimeTypeFile(), marshallToBytesAsV5, dbDraft.getTerritory().getCodeTerritory(), xmlFile.getLocalPath()));

        dbDraft.setName("MERGED_" + dbDraft.getTerritory().getCodeTerritory() + "_" + result.getDraftIdsConcat());
        for (Integer tlId : result.getDraftIds()) {
            tlService.deleteDraft(tlId);
        }
        return tlRepository.save(dbDraft);
    }

    /**
     * Merge Scheme information into new draft
     * 
     * @param result
     * @param tlDraft
     */
    @SuppressWarnings("unchecked")
    private void mergeSchemeInformation(Map<String, List<MergeEntryDTO>> siResults, TL tlDraft) {
        for (Entry<String, List<MergeEntryDTO>> siEntry : siResults.entrySet()) {
            if (siEntry.getValue().size() == 1) {
                MergeEntryDTO tmpEntry = siEntry.getValue().get(0);
                switch (tmpEntry.getTag()) {
                case SCHEME_OPERATOR_NAME:
                    tlDraft.getSchemeInformation().setSchemeOpeName((List<TLName>) tmpEntry.getObjectChanged());
                    break;
                case POSTAL_ADDRESSES:
                    tlDraft.getSchemeInformation().setSchemeOpePostal((List<TLPostalAddress>) tmpEntry.getObjectChanged());
                    break;
                case ELECTRONIC_ADDRESS:
                    tlDraft.getSchemeInformation().setSchemeOpeElectronic((List<TLElectronicAddress>) tmpEntry.getObjectChanged());
                    break;
                case SCHEME_NAME:
                    tlDraft.getSchemeInformation().setSchemeName((List<TLName>) tmpEntry.getObjectChanged());
                    break;
                case SCHEME_INFORMATION_URI:
                    tlDraft.getSchemeInformation().setSchemeInfoUri((List<TLInformationUri>) tmpEntry.getObjectChanged());
                    break;
                case STATUS_DETERMINATION:
                    tlDraft.getSchemeInformation().setStatusDetermination((String) tmpEntry.getObjectChanged());
                    break;
                case SCHEME_TYPE_COMMUNITY_RULES:
                    tlDraft.getSchemeInformation().setSchemeTypeCommRule((List<TLSchemeTypeCommunityRule>) tmpEntry.getObjectChanged());
                    break;
                case POLICY_OR_LEGAL_NOTICE:
                    tlDraft.getSchemeInformation().setSchemePolicy((List<TLSchemePolicy>) tmpEntry.getObjectChanged());
                    break;
                case DISTRIBUTION_LIST:
                    tlDraft.getSchemeInformation().setDistributionPoint((List<TLDistributionPoint>) tmpEntry.getObjectChanged());
                    break;
                default:
                    LOGGER.error("Unexpected merge entry on Scheme information. Entry: " + siEntry.getKey() + " - " + siEntry.getValue().get(0));
                    break;
                }
            } else {
                throw new IllegalArgumentException("More than one change detected on the same Scheme information element. Conflict : " + siEntry.getKey());
            }
        }
    }

    /**
     * Merge PTOTL into new draft
     * 
     * @param mergeResult
     * @param tlDraft
     */
    private void mergePTOTSL(Map<String, List<MergeEntryDTO>> mergeResult, TL tlDraft) {
        List<TLPointersToOtherTSL> ptotlToAdd = new ArrayList<>();
        for (Entry<String, List<MergeEntryDTO>> ptotlEntry : mergeResult.entrySet()) {
            MergeEntryDTO tmpEntry = ptotlEntry.getValue().get(0);
            TLPointersToOtherTSL changedPTOTL = (TLPointersToOtherTSL) tmpEntry.getObjectChanged();
            switch (tmpEntry.getStatus()) {
            case NEW:
                ptotlToAdd.add(changedPTOTL);
                break;
            case DELETE:
                for (TLPointersToOtherTSL tlPTOTL : new ArrayList<>(tlDraft.getPointers())) {
                    if (getPTOTLLocation(tlPTOTL).equals(getPTOTLLocation(changedPTOTL))) {
                        tlDraft.getPointers().remove(tlPTOTL);
                    }
                }
                break;
            case UPDATE:
                for (int i = 0; i < tlDraft.getPointers().size(); i++) {
                    if (getPTOTLLocation(tlDraft.getPointers().get(i)).equals(getPTOTLLocation(changedPTOTL))) {
                        tlDraft.getPointers().set(i, changedPTOTL);
                    }
                }
                break;
            }
        }

        if (!CollectionUtils.isEmpty(ptotlToAdd)) {
            tlDraft.getPointers().addAll(ptotlToAdd);
        }
    }

    /**
     * Merge TSPs into new draft
     * 
     * @param mergeResult
     * @param tlDraft
     */
    private void mergeTSPs(Map<String, List<MergeEntryDTO>> mergeResult, TL tlDraft) {
        List<TLServiceProvider> tspToAdd = new ArrayList<>();
        for (Entry<String, List<MergeEntryDTO>> tspEntry : mergeResult.entrySet()) {
            MergeEntryDTO tmpEntry = tspEntry.getValue().get(0);
            TLServiceProvider changedTSP = (TLServiceProvider) tmpEntry.getObjectChanged();
            switch (tmpEntry.getStatus()) {
            case NEW:
                tspToAdd.add(changedTSP);
                break;
            case DELETE:
                for (TLServiceProvider tlTSP : new ArrayList<>(tlDraft.getServiceProviders())) {
                    if (tlTSP.getName().equals(changedTSP.getName())) {
                        tlDraft.getServiceProviders().remove(changedTSP);
                    }
                }
                break;
            case UPDATE:
                for (int i = 0; i < tlDraft.getServiceProviders().size(); i++) {
                    if (tlDraft.getServiceProviders().get(i).getName().equals(changedTSP.getName())) {
                        tlDraft.getServiceProviders().set(i, changedTSP);
                    }
                }
                break;
            }
        }

        if (!CollectionUtils.isEmpty(tspToAdd)) {
            tlDraft.getServiceProviders().addAll(tspToAdd);
        }
    }

}
