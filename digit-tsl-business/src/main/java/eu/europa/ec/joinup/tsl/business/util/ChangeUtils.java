package eu.europa.ec.joinup.tsl.business.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.StringUtils;

import eu.europa.ec.joinup.tsl.business.dto.TLDifference;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLDefinitionUri;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLDigitalIdentification;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLDistributionPoint;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLElectronicAddress;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLGeneric;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLInformationUri;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLName;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPostalAddress;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLSchemePolicy;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLSchemeTypeCommunityRule;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceExtension;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

/**
 * Calcul TLDifferences of different trusted list objects
 */
public class ChangeUtils {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("messages");

    public static List<TLDifference> diffOfTLNameList(List<TLName> draft, List<TLName> published, String parentId) {
        List<TLDifference> diffList = new ArrayList<>();

        // COPY VALUE OF DRAFT DATA
        List<TLName> tmp = new ArrayList<>(draft);
        List<TLName> tmpPublished = new ArrayList<>();
        if (!CollectionUtils.isEmpty(published)) {
            tmpPublished = new ArrayList<>(published);
        }

        // DELETE EQUALS
        for (TLName tlName : draft) {
            if (tmpPublished.contains(tlName)) {
                tmpPublished.remove(tlName);
                tmp.remove(tlName);
            }
        }

        // CHECK OTHERS
        for (TLName draftName : tmp) {
            boolean find = false;
            // LANGUAGE AND VALUE NOT PRESENT
            if (draftName.getLanguage() != null) {
                List<TLDifference> nameTmpDifList = new ArrayList<>();
                for (TLName publishedName : tmpPublished) {
                    if (draftName.getLanguage().equalsIgnoreCase(publishedName.getLanguage())) {
                        // SAME LANGUAGE
                        TLDifference schemeNameDIff = draftName.asPublishedDiff(publishedName);
                        if (schemeNameDIff != null) {
                            nameTmpDifList.add(schemeNameDIff);
                            tmpPublished.remove(publishedName);
                            find = true;
                            break;
                        }
                    }
                }

                if (!nameTmpDifList.isEmpty()) {
                    diffList.addAll(nameTmpDifList);
                }

                // IF NOT FIND LANGUAGE --> NEW LANGUAGE
                if (!find) {
                    diffList.add(new TLDifference(draftName.getId(), "", draftName.getLanguage() + " - " + draftName.getValue()));
                }

            } else {
                // NO LANGUAGE --> NO CHANGE
                diffList.add(new TLDifference(draftName.getId(), "", bundle.getString("changes.noLanguage")));
            }
        }

        // DELETE ITEM IN DIFF LIST
        for (TLName publishedName : tmpPublished) {
            diffList.add(new TLDifference(parentId, publishedName.getLanguage() + " - " + publishedName.getValue(), ""));
        }

        // ADD ALL CHANGES POINTER DIFF LIST
        return diffList;

    }

    public static List<TLDifference> diffOfPostalList(List<TLPostalAddress> draft, List<TLPostalAddress> published, String parentId) {
        List<TLDifference> diffList = new ArrayList<>();

        // COPY VALUE OF DRAFT DATA
        List<TLPostalAddress> tmp = new ArrayList<>(draft);
        List<TLPostalAddress> tmpPublished = new ArrayList<>(published);

        // DELETE EQUALS
        for (TLPostalAddress tlPostalAddress : draft) {
            if (tmpPublished.contains(tlPostalAddress)) {
                tmpPublished.remove(tlPostalAddress);
                tmp.remove(tlPostalAddress);
            }
        }

        // CHECK OTHERS
        for (TLPostalAddress draftName : tmp) {
            boolean find = false;
            // LANGUAGE AND VALUE NOT PRESENT
            if (draftName.getLanguage() != null) {
                List<TLDifference> nameTmpDifList = new ArrayList<>();
                for (TLPostalAddress publishedName : tmpPublished) {
                    if (draftName.getLanguage().equalsIgnoreCase(publishedName.getLanguage())) {
                        // SAME LANGUAGE
                        TLDifference schemeNameDIff = draftName.asPublishedDiff(publishedName);
                        if (schemeNameDIff != null) {
                            nameTmpDifList.add(schemeNameDIff);
                            tmpPublished.remove(publishedName);
                            find = true;
                            break;
                        }
                    }
                }

                if (!nameTmpDifList.isEmpty()) {
                    diffList.addAll(nameTmpDifList);
                }

                // IF NOT FIND LANGUAGE --> NEW LANGUAGE
                if (!find) {
                    diffList.add(new TLDifference(draftName.getId(), "", draftName.getLanguage() + " - " + draftName.getStreet()));
                }

            } else {
                // NO LANGUAGE --> NO CHANGE
                diffList.add(new TLDifference(draftName.getId(), "", bundle.getString("changes.noLanguage")));
            }
        }

        // DELETE ITEM IN DIFF LIST
        for (TLPostalAddress publishedName : tmpPublished) {
            diffList.add(new TLDifference(parentId, publishedName.getLanguage() + " - " + publishedName.getStreet(), ""));
        }

        // ADD ALL CHANGES POINTER DIFF LIST
        return diffList;

    }

    public static List<TLDifference> diffOfElectronic(List<TLElectronicAddress> draft, List<TLElectronicAddress> published, String parentId) {
        List<TLDifference> diffList = new ArrayList<>();

        // COPY VALUE OF DRAFT DATA
        List<TLElectronicAddress> tmp = new ArrayList<>(draft);
        List<TLElectronicAddress> tmpPublished = new ArrayList<>(published);

        // DELETE EQUALS
        for (TLElectronicAddress tlElectronicAddress : draft) {
            if (tmpPublished.contains(tlElectronicAddress)) {
                tmpPublished.remove(tlElectronicAddress);
                tmp.remove(tlElectronicAddress);
            }
        }

        // CHECK OTHERS
        for (TLElectronicAddress draftName : tmp) {
            boolean find = false;
            // LANGUAGE AND VALUE NOT PRESENT
            if (draftName.getLanguage() != null) {
                List<TLDifference> nameTmpDifList = new ArrayList<>();
                for (TLElectronicAddress publishedName : tmpPublished) {
                    if (draftName.getLanguage().equalsIgnoreCase(publishedName.getLanguage())) {
                        // SAME LANGUAGE
                        TLDifference schemeNameDIff = draftName.asPublishedDiff(publishedName);
                        if (schemeNameDIff != null) {
                            nameTmpDifList.add(schemeNameDIff);
                            tmpPublished.remove(publishedName);
                            find = true;
                            break;
                        }
                    }
                }

                if (!nameTmpDifList.isEmpty()) {
                    diffList.addAll(nameTmpDifList);
                }

                // IF NOT FIND LANGUAGE --> NEW LANGUAGE
                if (!find) {
                    diffList.add(new TLDifference(draftName.getId(), "", draftName.getLanguage() + " - " + draftName.getValue()));
                }

            } else {
                // NO LANGUAGE --> NO CHANGE
                diffList.add(new TLDifference(draftName.getId(), "", bundle.getString("changes.noLanguage")));
            }
        }

        // DELETE ITEM IN DIFF LIST
        for (TLElectronicAddress publishedName : tmpPublished) {
            diffList.add(new TLDifference(parentId, publishedName.getLanguage() + " - " + publishedName.getValue(), ""));
        }

        // ADD ALL CHANGES POINTER DIFF LIST
        return diffList;

    }

    public static List<TLDifference> diffOfTLDefUriList(List<TLDefinitionUri> draft, List<TLDefinitionUri> published, String parentId) {
        List<TLDifference> diffList = new ArrayList<>();

        // COPY VALUE OF DRAFT DATA
        List<TLDefinitionUri> tmp = new ArrayList<>(draft);
        List<TLDefinitionUri> tmpPublished = new ArrayList<>(published);

        // DELETE EQUALS
        for (TLDefinitionUri tlDefinitionUri : draft) {
            if (tmpPublished.contains(tlDefinitionUri)) {
                tmpPublished.remove(tlDefinitionUri);
                tmp.remove(tlDefinitionUri);
            }
        }

        // CHECK OTHERS
        for (TLDefinitionUri draftName : tmp) {
            boolean find = false;
            // LANGUAGE AND VALUE NOT PRESENT
            if (draftName.getLanguage() != null) {
                List<TLDifference> nameTmpDifList = new ArrayList<>();
                for (TLDefinitionUri publishedName : tmpPublished) {
                    if (draftName.getLanguage().equalsIgnoreCase(publishedName.getLanguage())) {
                        // SAME LANGUAGE
                        TLDifference schemeNameDIff = draftName.asPublishedDiff(publishedName);
                        if (schemeNameDIff != null) {
                            nameTmpDifList.add(schemeNameDIff);
                            tmpPublished.remove(publishedName);
                            find = true;
                            break;
                        }
                    }
                }

                if (!nameTmpDifList.isEmpty()) {
                    diffList.addAll(nameTmpDifList);
                }

                // IF NOT FIND LANGUAGE --> NEW LANGUAGE
                if (!find) {
                    diffList.add(new TLDifference(draftName.getId(), "", draftName.getLanguage() + " - " + draftName.getValue()));
                }

            } else {
                // NO LANGUAGE --> NO CHANGE
                diffList.add(new TLDifference(draftName.getId(), "", bundle.getString("changes.noLanguage")));
            }
        }

        // DELETE ITEM IN DIFF LIST
        for (TLDefinitionUri publishedName : tmpPublished) {
            diffList.add(new TLDifference(parentId, publishedName.getLanguage() + " - " + publishedName.getValue(), ""));
        }

        // ADD ALL CHANGES POINTER DIFF LIST
        return diffList;

    }

    public static List<TLDifference> diffOfTLInfoUriList(List<TLInformationUri> draft, List<TLInformationUri> published, String parentId) {
        List<TLDifference> diffList = new ArrayList<>();

        // COPY VALUE OF DRAFT DATA
        List<TLInformationUri> tmp = new ArrayList<>(draft);
        List<TLInformationUri> tmpPublished = new ArrayList<>(published);

        // DELETE EQUALS
        for (TLInformationUri tlInformationUri : draft) {
            if (tmpPublished.contains(tlInformationUri)) {
                tmpPublished.remove(tlInformationUri);
                tmp.remove(tlInformationUri);
            }
        }

        // CHECK OTHERS
        for (TLInformationUri draftName : tmp) {
            boolean find = false;
            // LANGUAGE AND VALUE NOT PRESENT
            if (draftName.getLanguage() != null) {
                List<TLDifference> nameTmpDifList = new ArrayList<>();
                for (TLInformationUri publishedName : tmpPublished) {
                    if (draftName.getLanguage().equalsIgnoreCase(publishedName.getLanguage())) {
                        // SAME LANGUAGE
                        TLDifference schemeNameDIff = draftName.asPublishedDiff(publishedName);
                        if (schemeNameDIff != null) {
                            nameTmpDifList.add(schemeNameDIff);
                            tmpPublished.remove(publishedName);
                            find = true;
                            break;
                        }
                    }
                }

                if (!nameTmpDifList.isEmpty()) {
                    diffList.addAll(nameTmpDifList);
                }

                // IF NOT FIND LANGUAGE --> NEW LANGUAGE
                if (!find) {
                    diffList.add(new TLDifference(draftName.getId(), "", draftName.getLanguage() + " - " + draftName.getValue()));
                }

            } else {
                // NO LANGUAGE --> NO CHANGE
                diffList.add(new TLDifference(draftName.getId(), "", bundle.getString("changes.noLanguage")));
            }
        }

        // DELETE ITEM IN DIFF LIST
        for (TLInformationUri publishedName : tmpPublished) {
            diffList.add(new TLDifference(parentId, publishedName.getLanguage() + " - " + publishedName.getValue(), ""));
        }

        // ADD ALL CHANGES POINTER DIFF LIST
        return diffList;

    }

    public static List<TLDifference> diffOfDistributionPoint(List<TLDistributionPoint> draft, List<TLDistributionPoint> published, String parentId) {
        List<TLDifference> diffList = new ArrayList<>();

        // COPY VALUE OF DRAFT DATA
        List<TLDistributionPoint> tmp = new ArrayList<>(draft);
        List<TLDistributionPoint> tmpPublished = new ArrayList<>(published);

        // DELETE EQUALS
        for (TLDistributionPoint tlDistributionPoint : draft) {
            if (tmpPublished.contains(tlDistributionPoint)) {
                tmpPublished.remove(tlDistributionPoint);
                tmp.remove(tlDistributionPoint);
            }
        }

        // CHECK OTHERS
        int i = 0;
        for (TLDistributionPoint draftName : tmp) {
            if ((tmpPublished.size() > i) && (tmpPublished.get(i) != null)) {
                diffList.add(new TLDifference(draftName.getId(), tmpPublished.get(i).getValue(), draftName.getValue()));
                tmpPublished.remove(tmpPublished.get(i));
            } else {
                diffList.add(new TLDifference(draftName.getId(), "", draftName.getValue()));
            }
        }

        // DELETE ITEM IN DIFF LIST
        for (TLDistributionPoint publishedName : tmpPublished) {
            diffList.add(new TLDifference(parentId, publishedName.getValue(), ""));
        }

        // ADD ALL CHANGES DIFF LIST
        return diffList;
    }

    public static List<TLDifference> diffOfTLTypeCommunityList(List<TLSchemeTypeCommunityRule> draft, List<TLSchemeTypeCommunityRule> published, String parentId) {
        List<TLDifference> diffList = new ArrayList<>();

        // COPY VALUE OF DRAFT DATA
        List<TLSchemeTypeCommunityRule> tmp = new ArrayList<>(draft);
        List<TLSchemeTypeCommunityRule> tmpPublished = new ArrayList<>(published);

        // DELETE EQUALS
        for (TLSchemeTypeCommunityRule tlSchemeTypeCommunityRule : draft) {
            if (tmpPublished.contains(tlSchemeTypeCommunityRule)) {
                tmpPublished.remove(tlSchemeTypeCommunityRule);
                tmp.remove(tlSchemeTypeCommunityRule);
            }
        }

        // CHECK OTHERS
        for (TLSchemeTypeCommunityRule draftName : tmp) {
            boolean find = false;
            // LANGUAGE AND VALUE NOT PRESENT
            if (draftName.getLanguage() != null) {
                List<TLDifference> nameTmpDifList = new ArrayList<>();
                for (TLSchemeTypeCommunityRule publishedName : tmpPublished) {
                    if (draftName.getLanguage().equalsIgnoreCase(publishedName.getLanguage())) {
                        // SAME LANGUAGE
                        TLDifference schemeNameDIff = draftName.asPublishedDiff(publishedName);
                        if (schemeNameDIff != null) {
                            nameTmpDifList.add(schemeNameDIff);
                            tmpPublished.remove(publishedName);
                            find = true;
                            break;
                        }
                    }
                }

                if (!nameTmpDifList.isEmpty()) {
                    diffList.addAll(nameTmpDifList);
                }

                // IF NOT FIND LANGUAGE --> NEW LANGUAGE
                if (!find) {
                    diffList.add(new TLDifference(draftName.getId(), "", draftName.getLanguage() + " - " + draftName.getValue()));
                }

            } else {
                // NO LANGUAGE --> NO CHANGE
                diffList.add(new TLDifference(draftName.getId(), "", bundle.getString("changes.noLanguage")));
            }
        }

        // DELETE ITEM IN DIFF LIST
        for (TLSchemeTypeCommunityRule publishedName : tmpPublished) {
            diffList.add(new TLDifference(parentId, publishedName.getLanguage() + " - " + publishedName.getValue(), ""));
        }

        // ADD ALL CHANGES POINTER DIFF LIST
        return diffList;

    }

    public static List<TLDifference> diffOfTLPolicyList(List<TLSchemePolicy> draft, List<TLSchemePolicy> published, String parentId) {
        List<TLDifference> diffList = new ArrayList<>();

        // COPY VALUE OF DRAFT DATA
        List<TLSchemePolicy> tmp = new ArrayList<>(draft);
        List<TLSchemePolicy> tmpPublished = new ArrayList<>(published);

        // DELETE EQUALS
        for (TLSchemePolicy tlSchemePolicy : draft) {
            if (tmpPublished.contains(tlSchemePolicy)) {
                tmpPublished.remove(tlSchemePolicy);
                tmp.remove(tlSchemePolicy);
            }
        }

        // CHECK OTHERS
        for (TLSchemePolicy draftName : tmp) {
            boolean find = false;
            // LANGUAGE AND VALUE NOT PRESENT
            if (draftName.getLanguage() != null) {
                List<TLDifference> nameTmpDifList = new ArrayList<>();
                for (TLSchemePolicy publishedName : tmpPublished) {
                    if (draftName.getLanguage().equalsIgnoreCase(publishedName.getLanguage())) {
                        // SAME LANGUAGE
                        TLDifference schemeNameDIff = draftName.asPublishedDiff(publishedName);
                        if (schemeNameDIff != null) {
                            nameTmpDifList.add(schemeNameDIff);
                            tmpPublished.remove(publishedName);
                            find = true;
                            break;
                        }
                    }
                }

                if (!nameTmpDifList.isEmpty()) {
                    diffList.addAll(nameTmpDifList);
                }

                // IF NOT FIND LANGUAGE --> NEW LANGUAGE
                if (!find) {
                    diffList.add(new TLDifference(draftName.getId(), "", draftName.getLanguage() + " - " + draftName.getValue()));
                }

            } else {
                // NO LANGUAGE --> NO CHANGE
                diffList.add(new TLDifference(draftName.getId(), "", bundle.getString("changes.noLanguage")));
            }
        }

        // DELETE ITEM IN DIFF LIST
        for (TLSchemePolicy publishedName : tmpPublished) {
            diffList.add(new TLDifference(parentId, publishedName.getLanguage() + " - " + publishedName.getValue(), ""));
        }

        // ADD ALL CHANGES POINTER DIFF LIST
        return diffList;

    }

    public static List<TLDifference> diffOfDigitalList(List<TLDigitalIdentification> draft, List<TLDigitalIdentification> publishedTlDigiId, String parentId) {
        List<TLDigitalIdentification> tmpPublished = new ArrayList<>(publishedTlDigiId);
        List<TLDifference> diffList = new ArrayList<>();

        if (draft != null) {
            for (TLDigitalIdentification draftDigit : draft) {
                boolean match = false;
                for (TLDigitalIdentification publishedDigit : publishedTlDigiId) {
                    if (TLDigitalIdentityUtils.matchTLDigitalIdentification(draftDigit, publishedDigit)) {
                        match = true;
                        tmpPublished.remove(publishedDigit);
                        if (!draftDigit.equals(publishedDigit)) {
                            diffList.addAll(draftDigit.asPublishedDiff(publishedDigit));
                        }
                        break;
                    }
                }
                publishedTlDigiId = new ArrayList<>(tmpPublished);
                if (!match) {
                    diffList.add(new TLDifference(draftDigit.getId(), "", draftDigit.getName()));
                }
            }
        } else {
            for (TLDigitalIdentification publishedDigit : publishedTlDigiId) {
                diffList.add(new TLDifference(parentId, publishedDigit.getName(), ""));
            }

        }

        if (!tmpPublished.isEmpty()) {
            for (TLDigitalIdentification tmpDigit : tmpPublished) {
                diffList.add(new TLDifference(parentId + "_" + Tag.SERVICE_DIGITAL_IDENTITY, tmpDigit.getName(), ""));
            }
        }
        return diffList;
    }

    public static <T extends TLGeneric> List<TLDifference> initEmptyCurrentListDifference(List<T> objects, String parent) {
        List<TLDifference> differences = new ArrayList<>();
        for (T object : objects) {
            String language = !StringUtils.isEmpty(object.getLanguage()) ? object.getLanguage() + " - " : "";
            String value = !StringUtils.isEmpty(object.getValue()) ? object.getValue() : "";
            differences.add(new TLDifference(parent, language + value, ""));
        }
        return differences;
    }

    public static List<TLDifference> initEmptyCurrentPostalAddressDifference(List<TLPostalAddress> objects, String parent) {
        List<TLDifference> differences = new ArrayList<>();
        for (TLPostalAddress object : objects) {
            String language = !StringUtils.isEmpty(object.getLanguage()) ? object.getLanguage() + " - " : "";
            String street = !StringUtils.isEmpty(object.getStreet()) ? object.getStreet() : "";
            differences.add(new TLDifference(parent, language + street, ""));
        }
        return differences;
    }

    public static List<TLDifference> diffOfExtension(List<TLServiceExtension> tmp, List<TLServiceExtension> tmpPublished, String parent) {
        List<TLDifference> diffList = new ArrayList<>();

        List<TLServiceExtension> publishedQE = new ArrayList<>();
        List<TLServiceExtension> draftQE = new ArrayList<>();

        // CHECK OTHERS tspServices
        for (TLServiceExtension draft : tmp) {
            if (draft.getAdditionnalServiceInfo() != null) {
                // Additionnal Service Info : value
                String current = !StringUtils.isEmpty(draft.getAdditionnalServiceInfo().getValue()) ? draft.getAdditionnalServiceInfo().getValue() : bundle.getString("change.newAddExt");
                diffList.add(new TLDifference(draft.getId() + "_" + Tag.SERVICE_ADDITIONNAL_EXT, "", current));
            } else if (CollectionUtils.isNotEmpty(draft.getQualificationsExtension())) {
                draftQE.add(draft);
            } else if (draft.getExpiredCertsRevocationDate() != null) {
                // Expired Cert Revocation
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                diffList.add(new TLDifference(draft.getId() + "_" + Tag.SERVICE_EXPIRED_CERT_REVOCATION, "", format.format(draft.getExpiredCertsRevocationDate())));
            } else if (draft.getTakenOverBy() != null) {
                // Taken over by
                String territory = !StringUtils.isEmpty(draft.getTakenOverBy().getTerritory()) ? draft.getTakenOverBy().getTerritory() : bundle.getString("tUndefined");

                String uri;
                if ((draft.getTakenOverBy().getUrl() != null) && !StringUtils.isEmpty(draft.getTakenOverBy().getUrl().getValue())) {
                    uri = draft.getTakenOverBy().getUrl().getValue();
                } else {
                    uri = bundle.getString("tUndefined");
                }
                diffList.add(new TLDifference(draft.getId() + '_' + Tag.SERVICE_TAKEN_OVER_BY, "", territory + " - " + uri));
            } else {
                // Undefined
                diffList.add(new TLDifference(draft.getId(), "", bundle.getString("changes.newExt")));
            }
        }

        // DELETE ITEM IN DIFF LIST
        for (TLServiceExtension published : tmpPublished) {
            String language = bundle.getString("tUndefined");
            String value = bundle.getString("tUndefined");
            // Additionnal Service Info
            if (published.getAdditionnalServiceInfo() != null) {
                language = published.getAdditionnalServiceInfo().getLanguage() != null ? published.getAdditionnalServiceInfo().getLanguage() : bundle.getString("tUndefined");

                value = published.getAdditionnalServiceInfo().getValue() != null ? published.getAdditionnalServiceInfo().getValue() : bundle.getString("tUndefined");

                diffList.add(new TLDifference(parent + "_" + Tag.SERVICE_EXTENSION + "_" + Tag.SERVICE_ADDITIONNAL_EXT, language + " - " + value, ""));
            } else if (CollectionUtils.isNotEmpty(published.getQualificationsExtension())) {
                publishedQE.add(published);
            } else if (published.getTakenOverBy() != null) {
                // Taken over by
                if (!CollectionUtils.isEmpty(published.getTakenOverBy().getOperatorName())) {
                    language = published.getTakenOverBy().getOperatorName().get(0).getLanguage() != null ? published.getTakenOverBy().getOperatorName().get(0).getLanguage()
                            : bundle.getString("tUndefined");

                    value = published.getTakenOverBy().getOperatorName().get(0).getValue() != null ? published.getTakenOverBy().getOperatorName().get(0).getValue() : bundle.getString("tUndefined");
                }
                diffList.add(new TLDifference(parent + "_" + Tag.SERVICE_EXTENSION + "_" + Tag.SERVICE_TAKEN_OVER_BY, language + " - " + value, ""));
            } else if (published.getExpiredCertsRevocationDate() != null) {
                // Expired Cert Revocation
                diffList.add(new TLDifference(parent + "_" + Tag.SERVICE_EXTENSION + "_" + Tag.SERVICE_EXPIRED_CERT_REVOCATION, published.getExpiredCertsRevocationDate().toString(), ""));
            } else {
                // Undefined
                diffList.add(new TLDifference(parent + "_" + Tag.SERVICE_EXTENSION, bundle.getString("tUndefined"), ""));
            }
        }

        if (!CollectionUtils.isEmpty(draftQE) || !CollectionUtils.isEmpty(publishedQE)) {
            for (TLServiceExtension published : publishedQE) {
                boolean publishedMatch = false;
                for (TLServiceExtension draft : draftQE) {
                    if (published.equals(draft)) {
                        publishedMatch = true;
                        break;
                    }
                }
                if (!publishedMatch) {
                    diffList.add(new TLDifference(parent + "_" + Tag.SERVICE_EXTENSION, "Previous entry deleted or updated", ""));
                }
            }

            for (TLServiceExtension draft : draftQE) {
                boolean draftMatch = false;
                for (TLServiceExtension published : publishedQE) {
                    if (draft.equals(published)) {
                        draftMatch = true;
                        break;
                    }
                }
                if (!draftMatch) {
                    diffList.add(new TLDifference(draft.getId() + "_" + Tag.SERVICE_QUALIFICATION_EXT, "", "New entry or previous entry updated"));
                }
            }
        }

        return diffList;
    }

}
