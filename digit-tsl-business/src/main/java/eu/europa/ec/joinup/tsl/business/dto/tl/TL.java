package eu.europa.ec.joinup.tsl.business.dto.tl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import eu.europa.ec.joinup.tsl.business.dto.TLDifference;
import eu.europa.ec.joinup.tsl.business.util.LocationUtils;
import eu.europa.ec.joinup.tsl.model.enums.AvailabilityStatus;
import eu.europa.ec.joinup.tsl.model.enums.CheckStatus;
import eu.europa.ec.joinup.tsl.model.enums.SignatureStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.Tag;
import eu.europa.esig.jaxb.v5.tsl.OtherTSLPointersTypeV5;
import eu.europa.esig.jaxb.v5.tsl.TrustServiceProviderListTypeV5;
import eu.europa.esig.jaxb.v5.tsl.TrustStatusListTypeV5;

@XStreamAlias("tl")
public class TL extends AbstractTLDTO {

    private String tslId;
    private String dbName;
    private TLStatus dbStatus;
    private String dbCountryName;
    private boolean isUserEditable;
    private String tslTag;
    private SignatureStatus sigStatus;
    private AvailabilityStatus availabilityStatus;
    private CheckStatus checkStatus;
    private TLSchemeInformation schemeInformation;
    private List<TLPointersToOtherTSL> pointers;
    private List<TLServiceProvider> serviceProviders;
    private Date lastEdited;
    private Date checkEdited;
    private Date firstScanDate;
    private boolean checkToRun;

    private static final ResourceBundle bundle = ResourceBundle.getBundle("messages");

    public TL() {
    }

    public TL(int iddb, String tslId, String tslTag, TLSchemeInformation schemeInfo, List<TLPointersToOtherTSL> pointers, List<TLServiceProvider> serviceProviders) {
        super(iddb, String.valueOf(iddb));
        this.tslId = tslId;
        this.tslTag = tslTag;
        schemeInformation = schemeInfo;
        this.pointers = pointers;
        fixExtensionsLanguage(serviceProviders);
        this.serviceProviders = serviceProviders;
    }

    // JIRA TLM-45 (Due to a bug in the first delivery)
    private void fixExtensionsLanguage(List<TLServiceProvider> serviceProviders) {
        if ((serviceProviders != null) && CollectionUtils.isNotEmpty(serviceProviders)) {
            for (TLServiceProvider serviceProvider : serviceProviders) {
                if ((serviceProvider.getTSPServices() != null) && CollectionUtils.isNotEmpty(serviceProvider.getTSPServices())) {
                    for (TLServiceDto service : serviceProvider.getTSPServices()) {
                        if ((service.getExtension() != null) && CollectionUtils.isNotEmpty(service.getExtension())) {
                            for (TLServiceExtension ext : service.getExtension()) {
                                if (ext.getAdditionnalServiceInfo() != null) {
                                    if (StringUtils.isEmpty(ext.getAdditionnalServiceInfo().getLanguage())) {
                                        ext.getAdditionnalServiceInfo().setLanguage("en");
                                    }
                                }
                            }
                        }
                        if ((service.getHistory() != null) && CollectionUtils.isNotEmpty(service.getHistory())) {
                            for (TLServiceHistory svcHistory : service.getHistory()) {
                                if ((svcHistory.getExtension() != null) && CollectionUtils.isNotEmpty(svcHistory.getExtension())) {
                                    for (TLServiceExtension ext : svcHistory.getExtension()) {
                                        if (ext.getAdditionnalServiceInfo() != null) {
                                            if (StringUtils.isEmpty(ext.getAdditionnalServiceInfo().getLanguage())) {
                                                ext.getAdditionnalServiceInfo().setLanguage("en");
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    public TrustStatusListTypeV5 asTSLTypeV5() {
        TrustStatusListTypeV5 tslType = new TrustStatusListTypeV5();
        tslType.setId(getTslId());
        if (StringUtils.isEmpty(getTslTag())) {
            tslType.setTSLTag(null);
        } else {
            tslType.setTSLTag(getTslTag());
        }
        if (getSchemeInformation() != null) {
            tslType.setSchemeInformation(getSchemeInformation().asTSLTypeV5());
        }

        if (getPointers() != null) {
            OtherTSLPointersTypeV5 tslPointersList = new OtherTSLPointersTypeV5();
            for (TLPointersToOtherTSL tlPointers : getPointers()) {
                tslPointersList.getOtherTSLPointer().add(tlPointers.asTSLTypeV5());
            }
            tslType.getSchemeInformation().setPointersToOtherTSL(tslPointersList);
        }

        if ((getServiceProviders() != null) && CollectionUtils.isNotEmpty(getServiceProviders())) {
            TrustServiceProviderListTypeV5 tslServiceProvider = new TrustServiceProviderListTypeV5();
            for (TLServiceProvider serviceP : getServiceProviders()) {
                if (serviceP != null) {
                    tslServiceProvider.getTrustServiceProvider().add(serviceP.asTSLTypeV5());
                }
            }
            tslType.setTrustServiceProviderList(tslServiceProvider);
        }

        return tslType;
    }

    public void toV5FromV4() {
        schemeInformation.toV5FromV4();

        if (getServiceProviders() != null) {
            for (TLServiceProvider serviceP : getServiceProviders()) {
                serviceP.toV5FromV4();
            }
        }

    }

    public List<TLDifference> asPublishedDiff(TL publishedTl) {
        List<TLDifference> diffList = new ArrayList<>();
        if (publishedTl != null) {
            if (!StringUtils.equalsIgnoreCase(publishedTl.getTslId(), getTslId())) {
                diffList.add(new TLDifference(getId() + "_" + Tag.SCHEME_INFORMATION + "_" + Tag.TSL_IDENTIFIER, publishedTl.getTslId(), getTslId()));
            }

            if (schemeInformation != null) {
                List<TLDifference> schemeInfoDiffList = schemeInformation.asPublishedDiff(publishedTl.getSchemeInformation());
                if (CollectionUtils.isNotEmpty(schemeInfoDiffList)) {
                    diffList.addAll(schemeInfoDiffList);
                }
            } else {
                if (publishedTl.getSchemeInformation() != null) {
                    diffList.add(new TLDifference(getId() + "_" + Tag.SCHEME_INFORMATION, "", ""));
                }
            }

            for (TLDifference dif : diffList) {
                dif.setHrLocation(LocationUtils.idUserReadable(this, dif.getId()));
            }
        }

        return diffList;
    }

    public List<TLDifference> getPointersDiff(List<TLPointersToOtherTSL> publishedList, String parentId) {
        List<TLDifference> diffList = new ArrayList<>();

        // COPY VALUE OF DRAFT DATA
        List<TLPointersToOtherTSL> tmp = new ArrayList<>();
        for (TLPointersToOtherTSL draft : getPointers()) {
            tmpPointersList(tmp, draft);
        }

        List<TLPointersToOtherTSL> tmpPublished = new ArrayList<>();
        for (TLPointersToOtherTSL draft : publishedList) {
            tmpPointersList(tmpPublished, draft);
        }

        // DELETE EQUALS TLNAME
        for (int i = 0; i < getPointers().size(); i++) {
            if (tmpPublished.contains(getPointers().get(i))) {
                tmp.remove(getPointers().get(i));
                tmpPublished.remove(getPointers().get(i));
            }
        }

        // CHECK OTHERS TLNAME
        for (TLPointersToOtherTSL draft : tmp) {
            boolean find = false;
            // LANGUAGE AND VALUE NOT PRESENT
            if (draft.getMimeType() != null) {
                List<TLDifference> pointersTmpDifList = new ArrayList<>();
                for (TLPointersToOtherTSL published : tmpPublished) {
                    if (published.getMimeType() != null) {
                        if ((draft.getSchemeTerritory() == null) && (published.getSchemeTerritory() != null)) {
                            diffList.add(new TLDifference(draft.getId() + "_" + Tag.POINTER_SCHEME_TERRITORY, published.getSchemeTerritory(), ""));
                        } else if (draft.getSchemeTerritory().equalsIgnoreCase(published.getSchemeTerritory()) && draft.getMimeType().toString().equalsIgnoreCase(published.getMimeType().toString())) {
                            // SAME MIME TYPE
                            List<TLDifference> tmpDifList = draft.asPublishedDiff(published);
                            if ((tmpDifList != null) && !tmpDifList.isEmpty()) {
                                pointersTmpDifList.addAll(tmpDifList);
                                tmpPublished.remove(published);
                                find = true;
                                break;
                            }
                        }
                    } else {
                        // NO LANGUAGE --> NO CHANGE
                        diffList.add(new TLDifference(draft.getId() + "_" + Tag.POINTER_MIME_TYPE, bundle.getString("changes.noMimeType"), draft.getMimeType().toString()));
                    }
                }

                if (!pointersTmpDifList.isEmpty()) {
                    diffList.addAll(pointersTmpDifList);
                }

                // IF NOT FIND LANGUAGE --> NEW LANGUAGE
                if (!find) {
                    diffList.add(new TLDifference(draft.getId(), "", draft.getMimeType() + " - " + draft.getTlLocation()));
                }

            } else {
                // NO LANGUAGE --> NO CHANGE
                diffList.add(new TLDifference(draft.getId(), "", bundle.getString("changes.noMimeType")));
            }
        }

        // DELETE ITEM IN DIFF LIST
        for (TLPointersToOtherTSL published : tmpPublished) {
            diffList.add(new TLDifference(parentId, published.getMimeType() + " - " + published.getTlLocation(), ""));
        }

        for (TLDifference dif : diffList) {
            dif.setHrLocation(LocationUtils.idUserReadable(this, dif.getId()));
        }

        return diffList;
    }

    private void tmpPointersList(List<TLPointersToOtherTSL> tmp, TLPointersToOtherTSL draft) {
        TLPointersToOtherTSL newTmp = new TLPointersToOtherTSL();
        newTmp.setId(draft.getId());
        newTmp.setMimeType(draft.getMimeType());
        newTmp.setSchemeOpeName(draft.getSchemeOpeName());
        newTmp.setSchemeTerritory(draft.getSchemeTerritory());
        newTmp.setSchemeTypeCommunity(draft.getSchemeTypeCommunity());
        newTmp.setServiceDigitalId(draft.getServiceDigitalId());
        newTmp.setTlId(draft.getTlId());
        newTmp.setTlLocation(draft.getTlLocation());
        newTmp.setTlType(draft.getTlType());
        tmp.add(newTmp);
    }

    public List<TLDifference> getTrustServiceProvidersDiff(List<TLServiceProvider> publishedServicesProvider, String parentId) {
        List<TLDifference> diffList = new ArrayList<>();

        // COPY VALUE OF DRAFT DATA
        List<TLServiceProvider> tmp = new ArrayList<>(getServiceProviders());
        List<TLServiceProvider> tmpPublished = new ArrayList<>(publishedServicesProvider);

        // Delete draft TSPs who match published TSPs
        for (int i = 0; i < getServiceProviders().size(); i++) {
            if (tmpPublished.contains(getServiceProviders().get(i))) {
                tmp.remove(getServiceProviders().get(i));
                tmpPublished.remove(getServiceProviders().get(i));
            }
        }

        // Check remaining draft TSPs
        for (TLServiceProvider draft : tmp) {
            boolean find = false;
            if (CollectionUtils.isNotEmpty(draft.getTSPName())) {
                List<TLDifference> tspDiffs = new ArrayList<>();
                for (TLServiceProvider published : tmpPublished) {
                    if (draft.getTSPName().equals(published.getTSPName())) {
                        List<TLDifference> tmpDifList = draft.asPublishedDiff(published, draft.getId());
                        if ((tmpDifList != null) && !tmpDifList.isEmpty()) {
                            tspDiffs.addAll(tmpDifList);
                        }
                        tmpPublished.remove(published);
                        find = true;
                        break;
                    }
                }

                if (CollectionUtils.isNotEmpty(tspDiffs)) {
                    diffList.addAll(tspDiffs);
                }

                // No published TSPs match current draft TSP
                if (!find) {
                    diffList.add(new TLDifference(draft.getId(), "", draft.getTSPName().get(0).getLanguage() + " - " + draft.getTSPName().get(0).getValue()));
                }
            } else {
                // Draft TSP name undefined
                diffList.add(new TLDifference(draft.getId(), "", bundle.getString("changes.noTSPName")));
            }
        }

        // DELETE ITEM IN DIFF LIST
        for (TLServiceProvider published : tmpPublished) {
            diffList.add(new TLDifference(parentId, published.getTSPName().get(0).getLanguage() + " - " + published.getTSPName().get(0).getValue(), ""));
        }

        for (TLDifference dif : diffList) {
            dif.setHrLocation(LocationUtils.idUserReadable(this, dif.getId()));
        }

        return diffList;
    }

    /*
     * GETTERS AND SETTERS
     */
    public String getTslId() {
        return tslId;
    }

    public void setTslId(String tslId) {
        this.tslId = tslId;
    }

    public String getTslTag() {
        return tslTag;
    }

    public void setTslTag(String tslTag) {
        this.tslTag = tslTag;
    }

    public TLSchemeInformation getSchemeInformation() {
        return schemeInformation;
    }

    public void setSchemeInformation(TLSchemeInformation schemeInformation) {
        this.schemeInformation = schemeInformation;
    }

    public List<TLPointersToOtherTSL> getPointers() {
        return pointers;
    }

    public void setPointers(List<TLPointersToOtherTSL> pointers) {
        this.pointers = pointers;
    }

    public List<TLServiceProvider> getServiceProviders() {
        return serviceProviders;
    }

    public void setServiceProviders(List<TLServiceProvider> serviceProviders) {
        this.serviceProviders = serviceProviders;
    }

    public boolean isUserEditable() {
        return isUserEditable;
    }

    public void setUserEditable(boolean isUserEditable) {
        this.isUserEditable = isUserEditable;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public TLStatus getDbStatus() {
        return dbStatus;
    }

    public void setDbStatus(TLStatus dbStatus) {
        this.dbStatus = dbStatus;
    }

    public String getDbCountryName() {
        return dbCountryName;
    }

    public void setDbCountryName(String dbCountryName) {
        this.dbCountryName = dbCountryName;
    }

    public SignatureStatus getSigStatus() {
        return sigStatus;
    }

    public void setSigStatus(SignatureStatus sigStatus) {
        this.sigStatus = sigStatus;
    }

    public AvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public CheckStatus getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(CheckStatus checkStatus) {
        this.checkStatus = checkStatus;
    }

    public Date getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(Date lastEdited) {
        this.lastEdited = lastEdited;
    }

    public boolean isCheckToRun() {
        return checkToRun;
    }

    public void setCheckToRun(boolean checkToRun) {
        this.checkToRun = checkToRun;
    }

    public Date getCheckEdited() {
        return checkEdited;
    }

    public void setCheckEdited(Date checkEdited) {
        this.checkEdited = checkEdited;
    }

    public Date getFirstScanDate() {
        return firstScanDate;
    }

    public void setFirstScanDate(Date firstScanDate) {
        this.firstScanDate = firstScanDate;
    }

}
