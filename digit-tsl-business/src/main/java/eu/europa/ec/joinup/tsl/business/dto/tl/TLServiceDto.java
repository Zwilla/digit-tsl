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
package eu.europa.ec.joinup.tsl.business.dto.tl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TimeZone;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import eu.europa.ec.joinup.tsl.business.dto.TLDifference;
import eu.europa.ec.joinup.tsl.business.util.ChangeUtils;
import eu.europa.ec.joinup.tsl.business.util.TLQServiceTypeUtils;
import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.ec.joinup.tsl.model.enums.Tag;
import eu.europa.esig.jaxb.tsl.ExtensionType;
import eu.europa.esig.jaxb.tsl.MultiLangNormStringType;
import eu.europa.esig.jaxb.tsl.NonEmptyMultiLangURIType;
import eu.europa.esig.jaxb.tsl.ServiceHistoryInstanceType;
import eu.europa.esig.jaxb.tsl.ServiceHistoryType;
import eu.europa.esig.jaxb.tsl.TSPServiceInformationType;
import eu.europa.esig.jaxb.tsl.TSPServiceType;
import eu.europa.esig.jaxb.v5.tsl.AdditionalServiceInformationTypeV5;
import eu.europa.esig.jaxb.v5.tsl.ExtensionTypeV5;
import eu.europa.esig.jaxb.v5.tsl.ExtensionsListTypeV5;
import eu.europa.esig.jaxb.v5.tsl.InternationalNamesTypeV5;
import eu.europa.esig.jaxb.v5.tsl.MultiLangNormStringTypeV5;
import eu.europa.esig.jaxb.v5.tsl.NonEmptyMultiLangURIListTypeV5;
import eu.europa.esig.jaxb.v5.tsl.NonEmptyMultiLangURITypeV5;
import eu.europa.esig.jaxb.v5.tsl.ServiceHistoryInstanceTypeV5;
import eu.europa.esig.jaxb.v5.tsl.ServiceHistoryTypeV5;
import eu.europa.esig.jaxb.v5.tsl.ServiceSupplyPointsTypeV5;
import eu.europa.esig.jaxb.v5.tsl.TSPServiceInformationTypeV5;
import eu.europa.esig.jaxb.v5.tsl.TSPServiceTypeV5;

public class TLServiceDto extends AbstractTLDTO {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("messages");

    private String typeIdentifier;
    private List<TLName> serviceName;
    private List<TLDigitalIdentification> serviceDigitalIdentification;
    private String currentStatus;
    private Date currentStatusStartingDate;
    private List<TLDefinitionUri> schemeDefinitionUri;
    private List<TLDefinitionUri> tspDefinitionUri;
    private List<TLSupplyPoint> supplyPoint;
    private List<TLServiceExtension> extension;
    private List<TLServiceHistory> history;
    private List<String> qServiceTypes;

    public TLServiceDto() {
    }

    public TLServiceDto(int iddb, String location, TSPServiceType serviceType) {
        super(iddb, location);

        TSPServiceInformationType serviceInformation = serviceType.getServiceInformation();

        int i;
        if (serviceInformation != null) {
            setTypeIdentifier(serviceInformation.getServiceTypeIdentifier());
            setCurrentStatus(serviceInformation.getServiceStatus());
            setCurrentStatusStartingDate(TLUtils.toDate(serviceInformation.getStatusStartingTime()));

            List<TLName> serviceNames = new ArrayList<>();
            if (serviceInformation.getServiceName() != null) {
                i = 0;
                for (MultiLangNormStringType name : serviceInformation.getServiceName().getName()) {
                    i++;
                    serviceNames.add(new TLName(iddb, location + "_" + Tag.SERVICE_NAME + "_" + i, name));
                }
            }
            setServiceName(serviceNames);

            List<TLDigitalIdentification> digitalId = new ArrayList<>();
            if (serviceInformation.getServiceDigitalIdentity() != null) {
                digitalId.add(new TLDigitalIdentification(iddb, location + "_" + Tag.SERVICE_DIGITAL_IDENTITY + "_1", serviceInformation.getServiceDigitalIdentity()));
            }
            setDigitalIdentification(digitalId);

            List<TLDefinitionUri> schemeDefinitions = new ArrayList<>();
            if (serviceInformation.getSchemeServiceDefinitionURI() != null) {
                i = 0;
                for (NonEmptyMultiLangURIType definitionUri : serviceInformation.getSchemeServiceDefinitionURI().getURI()) {
                    i++;
                    schemeDefinitions.add(new TLDefinitionUri(iddb, location + "_" + Tag.SCHEME_SERVICE_DEFINITION_URI + "_" + i, definitionUri));
                }
            }
            setSchemeDefinitionUri(schemeDefinitions);

            List<TLDefinitionUri> tspDefinitions = new ArrayList<>();
            if (serviceInformation.getTSPServiceDefinitionURI() != null) {
                i = 0;
                for (NonEmptyMultiLangURIType definitionUri : serviceInformation.getTSPServiceDefinitionURI().getURI()) {
                    i++;
                    tspDefinitions.add(new TLDefinitionUri(iddb, location + "_" + Tag.TSP_SERVICE_DEFINITION_URI + "_" + i, definitionUri));
                }
            }
            setTSPDefinitionUri(tspDefinitions);

            List<TLSupplyPoint> supplyPoints = new ArrayList<>();
            if (serviceInformation.getServiceSupplyPoints() != null) {
                i = 0;
                for (String supply : serviceInformation.getServiceSupplyPoints().getServiceSupplyPoint()) {
                    i++;
                    supplyPoints.add(new TLSupplyPoint(iddb, location + "_" + Tag.SERVICE_SUPPLY_POINT + "_" + i, supply));
                }
            }
            setSupplyPoint(supplyPoints);

            List<TLServiceExtension> extensions = new ArrayList<>();
            if (serviceInformation.getServiceInformationExtensions() != null) {
                i = 0;
                for (ExtensionType extensionType : serviceInformation.getServiceInformationExtensions().getExtension()) {
                    i++;
                    extensions.add(new TLServiceExtension(iddb, location + "_" + Tag.SERVICE_EXTENSION + "_" + i, extensionType));
                }
            }
            setExtension(extensions);

        }

        ServiceHistoryType serviceHistory = serviceType.getServiceHistory();
        List<TLServiceHistory> tlServiceHistory = new ArrayList<>();
        if (serviceHistory != null) {
            i = 0;
            for (ServiceHistoryInstanceType svcHistory : serviceHistory.getServiceHistoryInstance()) {
                i++;
                tlServiceHistory.add(new TLServiceHistory(iddb, location + "_" + Tag.SERVICE_HISTORY + "_" + i, svcHistory));
            }
        }
        setHistory(tlServiceHistory);
    }

    public TLServiceDto(int iddb, String location, TSPServiceTypeV5 serviceType) {
        super(iddb, location);

        TSPServiceInformationTypeV5 serviceInformation = serviceType.getServiceInformation();

        int i;
        if (serviceInformation != null) {
            setTypeIdentifier(serviceInformation.getServiceTypeIdentifier());
            setCurrentStatus(serviceInformation.getServiceStatus());
            setCurrentStatusStartingDate(TLUtils.toDate(serviceInformation.getStatusStartingTime()));

            List<TLName> serviceNames = new ArrayList<>();
            if (serviceInformation.getServiceName() != null) {
                i = 0;
                for (MultiLangNormStringTypeV5 name : serviceInformation.getServiceName().getName()) {
                    i++;
                    serviceNames.add(new TLName(iddb, location + "_" + Tag.SERVICE_NAME + "_" + i, name));
                }
            }
            setServiceName(serviceNames);

            if (serviceInformation.getServiceDigitalIdentity() != null) {
                List<TLDigitalIdentification> digitalId = new ArrayList<>();
                digitalId.add(new TLDigitalIdentification(iddb, location + "_" + Tag.SERVICE_DIGITAL_IDENTITY + "_1", serviceInformation.getServiceDigitalIdentity()));
                setDigitalIdentification(digitalId);
            }

            List<TLDefinitionUri> schemeDefinitions = new ArrayList<>();
            if (serviceInformation.getSchemeServiceDefinitionURI() != null) {
                i = 0;
                for (NonEmptyMultiLangURITypeV5 definitionUri : serviceInformation.getSchemeServiceDefinitionURI().getURI()) {
                    i++;
                    schemeDefinitions.add(new TLDefinitionUri(iddb, location + "_" + Tag.SCHEME_SERVICE_DEFINITION_URI + "_" + i, definitionUri));
                }
            }
            setSchemeDefinitionUri(schemeDefinitions);

            List<TLDefinitionUri> tspDefinitions = new ArrayList<>();
            if (serviceInformation.getTSPServiceDefinitionURI() != null) {
                i = 0;
                for (NonEmptyMultiLangURITypeV5 definitionUri : serviceInformation.getTSPServiceDefinitionURI().getURI()) {
                    i++;
                    tspDefinitions.add(new TLDefinitionUri(iddb, location + "_" + Tag.TSP_SERVICE_DEFINITION_URI + "_" + i, definitionUri));
                }
            }
            setTSPDefinitionUri(tspDefinitions);

            List<TLSupplyPoint> supplyPoints = new ArrayList<>();
            if (serviceInformation.getServiceSupplyPoints() != null) {
                i = 0;
                for (String supply : serviceInformation.getServiceSupplyPoints().getServiceSupplyPoint()) {
                    i++;
                    supplyPoints.add(new TLSupplyPoint(iddb, location + "_" + Tag.SERVICE_SUPPLY_POINT + "_" + i, supply));
                }
            }
            setSupplyPoint(supplyPoints);

            List<TLServiceExtension> extensions = new ArrayList<>();
            if (serviceInformation.getServiceInformationExtensions() != null) {
                i = 0;
                for (ExtensionTypeV5 extensionType : serviceInformation.getServiceInformationExtensions().getExtension()) {
                    i++;
                    extensions.add(new TLServiceExtension(iddb, location + "_" + Tag.SERVICE_EXTENSION + "_" + i, extensionType));
                }
            }
            setExtension(extensions);

        }

        ServiceHistoryTypeV5 serviceHistory = serviceType.getServiceHistory();
        if (serviceHistory != null) {
            List<TLServiceHistory> tlServiceHistory = new ArrayList<>();
            i = 0;
            for (ServiceHistoryInstanceTypeV5 svcHistory : serviceHistory.getServiceHistoryInstance()) {
                i++;
                tlServiceHistory.add(new TLServiceHistory(iddb, location + "_" + Tag.SERVICE_HISTORY + "_" + i, svcHistory));
            }
            setHistory(tlServiceHistory);
        }
    }

    public List<TLDifference> asPublishedDiff(TLServiceDto publishedTlService) {
        List<TLDifference> diffList = new ArrayList<>();

        if (!getCurrentStatus().equalsIgnoreCase(publishedTlService.getCurrentStatus())) {
            diffList.add(new TLDifference(getId() + "_" + Tag.SERVICE_STATUS, publishedTlService.getCurrentStatus(), getCurrentStatus()));
        }

        if (!publishedTlService.getCurrentStatusStartingDate().equals(getCurrentStatusStartingDate())) {
            if (getCurrentStatusStartingDate() == null) {
                diffList.add(new TLDifference(getId() + "_" + Tag.SERVICE_STATUS_STARTING_TIME, TLUtils.toStringFormat(publishedTlService.getCurrentStatusStartingDate()), ""));
            } else {
                diffList.add(new TLDifference(getId() + "_" + Tag.SERVICE_STATUS_STARTING_TIME, TLUtils.toStringFormat(publishedTlService.getCurrentStatusStartingDate()),
                        TLUtils.toStringFormat(getCurrentStatusStartingDate())));
            }
        }

        if (!getTypeIdentifier().equalsIgnoreCase(publishedTlService.getTypeIdentifier())) {
            diffList.add(new TLDifference(getId() + "_" + Tag.SERVICE_TYPE_IDENTIFIER, publishedTlService.getTypeIdentifier(), getTypeIdentifier()));
        }

        /*************************************
         * List<TLServiceExtension> extension;
         *************************************/
        List<TLServiceExtension> tmp = !CollectionUtils.isEmpty(getExtension()) ? new ArrayList<>(getExtension()) : new ArrayList<TLServiceExtension>();
        List<TLServiceExtension> tmpPublished = !CollectionUtils.isEmpty(publishedTlService.getExtension()) ? new ArrayList<>(publishedTlService.getExtension())
                : new ArrayList<TLServiceExtension>();

        // DELETE EQUALS EXTENSION
        if (!CollectionUtils.isEmpty(getExtension())) {
            for (int i = 0; i < getExtension().size(); i++) {
                if (tmpPublished.contains(getExtension().get(i))) {
                    tmp.remove(getExtension().get(i));
                    tmpPublished.remove(getExtension().get(i));
                }
            }
        }

        diffList.addAll(ChangeUtils.diffOfExtension(tmp, tmpPublished, getId()));

        /*************************************
         * List<TLDefinitionUri> schemeDefinitionUri;
         *************************************/
        if (CollectionUtils.isNotEmpty(getSchemeDefinitionUri())) {
            List<TLDifference> changeList = ChangeUtils.diffOfTLDefUriList(getSchemeDefinitionUri(), publishedTlService.getSchemeDefinitionUri(),
                    getId() + "_" + Tag.SCHEME_SERVICE_DEFINITION_URI);
            if (changeList.size() > 0) {
                diffList.addAll(changeList);
            }
        } else {
            if ((publishedTlService.getSchemeDefinitionUri() != null) && CollectionUtils.isNotEmpty(publishedTlService.getSchemeDefinitionUri())) {
                diffList.addAll(ChangeUtils.initEmptyCurrentListDifference(publishedTlService.getSchemeDefinitionUri(), getId() + '_' + Tag.SCHEME_SERVICE_DEFINITION_URI));
            }
        }

        /*************************************
         * List<TLDefinitionUri> tspDefinitionUri;
         *************************************/
        if (CollectionUtils.isNotEmpty(getTSPDefinitionUri())) {
            List<TLDifference> changeList = ChangeUtils.diffOfTLDefUriList(getTSPDefinitionUri(), publishedTlService.getTSPDefinitionUri(), getId() + "_" + Tag.TSP_SERVICE_DEFINITION_URI);
            if (changeList.size() > 0) {
                diffList.addAll(changeList);
            }
        } else {
            if ((publishedTlService.getTSPDefinitionUri() != null) && CollectionUtils.isNotEmpty(publishedTlService.getTSPDefinitionUri())) {
                diffList.addAll(ChangeUtils.initEmptyCurrentListDifference(publishedTlService.getTSPDefinitionUri(), getId() + '_' + Tag.TSP_SERVICE_DEFINITION_URI));
            }
        }

        /*************************************
         * List<TLSupplyPoint> supplyPoint;
         *************************************/
        if (CollectionUtils.isNotEmpty(getSupplyPoint())) {
            for (TLSupplyPoint supply : getSupplyPoint()) {
                if (!publishedTlService.getSupplyPoint().contains(supply)) {
                    diffList.add(new TLDifference(supply.getId(), "", supply.getValue()));
                }
            }
        }

        if (CollectionUtils.isNotEmpty(publishedTlService.getSupplyPoint())) {
            for (TLSupplyPoint supply : publishedTlService.getSupplyPoint()) {
                if (!getSupplyPoint().contains(supply)) {
                    diffList.add(new TLDifference(getId() + "_" + Tag.SERVICE_SUPPLY_POINT, supply.getValue(), ""));
                }
            }
        }

        /*************************************
         * List<TLDigitalIdentification> serviceDigitalIdentification;
         *************************************/
        diffList.addAll(ChangeUtils.diffOfDigitalList(getDigitalIdentification(), publishedTlService.getDigitalIdentification(), getId()));

        /*************************************
         * List<TLName> serviceName;
         *************************************/
        if (CollectionUtils.isNotEmpty(getServiceName())) {
            List<TLDifference> changeList = ChangeUtils.diffOfTLNameList(getServiceName(), publishedTlService.getServiceName(), getId() + "_" + Tag.SERVICE_NAME);
            if (changeList.size() > 0) {
                diffList.addAll(changeList);
            }
        } else {
            if ((publishedTlService.getServiceName() != null) && CollectionUtils.isNotEmpty(publishedTlService.getServiceName())) {
                diffList.addAll(ChangeUtils.initEmptyCurrentListDifference(publishedTlService.getServiceName(), getId() + '_' + Tag.SERVICE_NAME));
            }
        }

        /*************************************
         * List<TLServiceHistory> history;
         *************************************/
        // COPY VALUE OF DRAFT DATA
        List<TLServiceHistory> tmpHisto = new ArrayList<>();
        List<TLServiceHistory> tmpPublishedHitso = null;

        if (publishedTlService.getHistory() != null) {
            tmpPublishedHitso = new ArrayList<>(publishedTlService.getHistory());
        } else {
            tmpPublishedHitso = new ArrayList<>();
        }
        if (getHistory() != null) {

            tmpHisto = new ArrayList<>(getHistory());
            // DELETE EQUALS
            for (int i = 0; i < getHistory().size(); i++) {
                if (tmpPublishedHitso.contains(getHistory().get(i))) {
                    tmpHisto.remove(getHistory().get(i));
                    tmpPublishedHitso.remove(getHistory().get(i));
                }
            }

            // CHECK OTHERS
            for (TLServiceHistory draft : tmpHisto) {
                boolean find = false;
                // LANGUAGE AND VALUE NOT PRESENT
                if ((draft.getCurrentStatusStartingDate() != null) && (draft.getTypeIdentifier() != null)) {
                    List<TLDifference> tmpDifList = new ArrayList<>();
                    List<TLServiceHistory> forTmpPublishedHitso = new ArrayList<>(tmpPublishedHitso);
                    for (TLServiceHistory publishe : forTmpPublishedHitso) {
                        if (draft.getCurrentStatusStartingDate().equals(publishe.getCurrentStatusStartingDate())
                                && draft.getTypeIdentifier().equalsIgnoreCase(publishe.getTypeIdentifier())
                                && draft.getCurrentStatus().equalsIgnoreCase(publishe.getCurrentStatus())) {
                            // SAME
                            List<TLDifference> list = draft.asPublishedDiff(publishe);
                            if (list != null) {
                                tmpDifList.addAll(list);
                            }
                            tmpPublishedHitso.remove(publishe);
                            find = true;
                            break;
                        }
                    }

                    if (!tmpDifList.isEmpty()) {
                        diffList.addAll(tmpDifList);
                    }

                    // IF NOT FIND LANGUAGE --> NEW LANGUAGE
                    if (!find) {
                        diffList.add(new TLDifference(draft.getId(), "", draft.getCurrentStatus() + " - " + draft.getCurrentStatusStartingDate()));
                    }

                } else {
                    // NO LANGUAGE --> NO CHANGE
                    diffList.add(new TLDifference(draft.getId(), "", bundle.getString("changes.noInfo")));
                }
            }

            // CHECK OTHERS
            for (TLServiceHistory draft : tmpPublishedHitso) {
                diffList.add(new TLDifference(getId() + "_" + Tag.SERVICE_HISTORY, draft.getCurrentStatus() + " - " + draft.getCurrentStatusStartingDate(), ""));
            }
        }
        return diffList;

    }

    public TSPServiceTypeV5 asTSLTypeV5() {
        TSPServiceTypeV5 tspService = new TSPServiceTypeV5();
        TSPServiceInformationTypeV5 tspServiceInfo = new TSPServiceInformationTypeV5();

        // SCHEME SERVICE URI
        if ((getSchemeDefinitionUri() != null) && CollectionUtils.isNotEmpty(getSchemeDefinitionUri())) {
            NonEmptyMultiLangURIListTypeV5 langUriList = new NonEmptyMultiLangURIListTypeV5();
            for (TLDefinitionUri tlUri : getSchemeDefinitionUri()) {
                langUriList.getURI().add(tlUri.asTSLTypeV5());
            }
            tspServiceInfo.setSchemeServiceDefinitionURI(langUriList);
        }

        // DIGITAL ID
        if ((getDigitalIdentification() != null) && CollectionUtils.isNotEmpty(getDigitalIdentification())) {
            tspServiceInfo.setServiceDigitalIdentity(getDigitalIdentification().get(0).asTSLTypeV5());
        }

        // SERVICE NAME
        if ((getServiceName() != null) && CollectionUtils.isNotEmpty(getServiceName())) {
            InternationalNamesTypeV5 intServiceName = new InternationalNamesTypeV5();
            for (TLName tlName : getServiceName()) {
                intServiceName.getName().add(tlName.asTSLTypeV5());
            }
            tspServiceInfo.setServiceName(intServiceName);
        }

        tspServiceInfo.setServiceStatus(getCurrentStatus());

        if (getCurrentStatusStartingDate() != null) {
            tspServiceInfo.setStatusStartingTime(TLUtils.toXMLGregorianDate(getCurrentStatusStartingDate()));
        }

        tspServiceInfo.setServiceTypeIdentifier(getTypeIdentifier());

        // TSPDEFINITION URI
        if ((getTSPDefinitionUri() != null) && CollectionUtils.isNotEmpty(getTSPDefinitionUri())) {
            NonEmptyMultiLangURIListTypeV5 langUriList = new NonEmptyMultiLangURIListTypeV5();
            for (TLDefinitionUri tlUri : getTSPDefinitionUri()) {
                langUriList.getURI().add(tlUri.asTSLTypeV5());
            }
            tspServiceInfo.setTSPServiceDefinitionURI(langUriList);
        }

        if ((getSupplyPoint() != null) && CollectionUtils.isNotEmpty(getSupplyPoint())) {
            ServiceSupplyPointsTypeV5 serviceSupply = new ServiceSupplyPointsTypeV5();
            for (TLSupplyPoint supply : getSupplyPoint()) {
                serviceSupply.getServiceSupplyPoint().add(supply.asTSLTypeV5());
            }
            tspServiceInfo.setServiceSupplyPoints(serviceSupply);
        }

        if ((getExtension() != null) && CollectionUtils.isNotEmpty(getExtension())) {
            ExtensionsListTypeV5 extListType = new ExtensionsListTypeV5();
            for (TLServiceExtension serviceExt : getExtension()) {
                extListType.getExtension().add(serviceExt.asTSLTypeV5());
            }
            tspServiceInfo.setServiceInformationExtensions(extListType);
        }

        tspService.setServiceInformation(tspServiceInfo);

        /// HISTORY
        if ((getHistory() != null) && CollectionUtils.isNotEmpty(getHistory())) {
            ServiceHistoryTypeV5 svcHistoryType = new ServiceHistoryTypeV5();
            for (TLServiceHistory serviceHistory : getHistory()) {
                if ((getDigitalIdentification() != null) && !getDigitalIdentification().isEmpty()) {
                    if ((getDigitalIdentification().get(0) != null) && (getDigitalIdentification().get(0).getCertificateList() != null)
                            && !getDigitalIdentification().get(0).getCertificateList().isEmpty()) {
                        svcHistoryType.getServiceHistoryInstance().add(serviceHistory.asTSLTypeV5(getDigitalIdentification().get(0).getCertificateList().get(0)));
                    } else {
                        svcHistoryType.getServiceHistoryInstance().add(serviceHistory.asTSLTypeV5(null));
                    }
                } else {
                    svcHistoryType.getServiceHistoryInstance().add(serviceHistory.asTSLTypeV5(null));
                }
            }
            tspService.setServiceHistory(svcHistoryType);
        }

        return tspService;
    }

    public void toV5FromV4() {

        if ((getHistory() != null) && CollectionUtils.isNotEmpty(getHistory())) {
            for (TLServiceHistory tlServiceHistory : getHistory()) {
                tlServiceHistory.toV5FromV4(getDigitalIdentification());
            }
        }

        // getTypeIdentifier();???
        if (getTypeIdentifier() != null) {
            /*
             * 5.5.1.1
             */
            if (getTypeIdentifier().equalsIgnoreCase("http://uri.etsi.org/TrstSvc/Svctype/CA/QC")) {
                addToHistory();
                setCurrentStatus(checkServiceStatus5511(getCurrentStatus()));

                if (getExtension() != null) {
                    // UPDATE EXISTING EXTENSION
                    for (TLServiceExtension serviceExt : getExtension()) {
                        serviceExt.toV5FromV4();
                    }
                }

                List<TLServiceExtension> tmpList = new ArrayList<>();
                if (getExtension() != null) {
                    tmpList = getExtension();
                }
                tmpList.add(getASIForeSignature());

                setExtension(tmpList);

                /*
                 * 5.5.1.1
                 */
            } else if (getTypeIdentifier().equalsIgnoreCase("http://uri.etsi.org/TrstSvc/Svctype/Certstatus/OCSP/QC")
                    || getTypeIdentifier().equalsIgnoreCase("http://uri.etsi.org/TrstSvc/Svctype/Certstatus/CRL/QC")) {
                addToHistory();
                setCurrentStatus(checkServiceStatus5511(getCurrentStatus()));
                List<TLServiceExtension> tmpList = new ArrayList<>();
                if (getExtension() != null) {
                    tmpList = getExtension();
                }
                tmpList.add(getASIForeSignature());
                setExtension(tmpList);

                /*
                 * 5.5.1.1
                 */
            } else if (getTypeIdentifier().equalsIgnoreCase("http://uri.etsi.org/TrstSvc/Svctype/TSA/QTST")
                    || getTypeIdentifier().equalsIgnoreCase("http://uri.etsi.org/TrstSvc/Svctype/EDS/Q")
                    || getTypeIdentifier().equalsIgnoreCase("http://uri.etsi.org/TrstSvc/Svctype/EDS/REM")
                    || getTypeIdentifier().equalsIgnoreCase("http://uri.etsi.org/TrstSvc/Svctype/QESValidation/Q")
                    || getTypeIdentifier().equalsIgnoreCase("http://uri.etsi.org/TrstSvc/Svctype/PSES/Q")) {
                addToHistory();
                setCurrentStatus("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/withdrawn");

                /*
                 * 5.5.2.1
                 */
            } else if (getTypeIdentifier().equalsIgnoreCase("http://uri.etsi.org/TrstSvc/Svctype/CA/PKC")
                    || getTypeIdentifier().equalsIgnoreCase("http://uri.etsi.org/TrstSvc/Svctype/TSA")
                    || getTypeIdentifier().equalsIgnoreCase("http://uri.etsi.org/TrstSvc/Svctype/TSA/TSS-QC")
                    || getTypeIdentifier().equalsIgnoreCase("http://uri.etsi.org/TrstSvc/Svctype/TSA/TSS-AdESQCandQES")
                    || getTypeIdentifier().equalsIgnoreCase("http://uri.etsi.org/TrstSvc/Svctype/Certstatus/CRL")
                    || getTypeIdentifier().equalsIgnoreCase("http://uri.etsi.org/TrstSvc/Svctype/Certstatus/OCSP")
                    || getTypeIdentifier().equalsIgnoreCase("http://uri.etsi.org/TrstSvc/Svctype/EDS")
                    || getTypeIdentifier().equalsIgnoreCase("http://uri.etsi.org/TrstSvc/Svctype/PSES")
                    || getTypeIdentifier().equalsIgnoreCase("http://uri.etsi.org/TrstSvc/Svctype/AdESValidation")
                    || getTypeIdentifier().equalsIgnoreCase("http://uri.etsi.org/TrstSvc/Svctype/AdESGeneration")) {
                addToHistory();
                setCurrentStatus(checkServiceStatus5521(getCurrentStatus()));

                /*
                 * 5.5.2.1
                 */
            } else if (getTypeIdentifier().equalsIgnoreCase("http://uri.etsi.org/TrstSvc/Svctype/REM")) {
                addToHistory();
                setTypeIdentifier("");// Normally /REM not use for the moment
                setCurrentStatus(checkServiceStatus5521(getCurrentStatus()));

                /*
                 * 5.5.3.1
                 */
            } else if (getTypeIdentifier().equalsIgnoreCase("http://uri.etsi.org/TrstSvc/Svctype/RA")
                    || getTypeIdentifier().equalsIgnoreCase("http://uri.etsi.org/TrstSvc/Svctype/RA/nothavingPKIid")
                    || getTypeIdentifier().equalsIgnoreCase("http://uri.etsi.org/TrstSvc/Svctype/ACA")
                    || getTypeIdentifier().equalsIgnoreCase("http://uri.etsi.org/TrstSvc/Svctype/NationalRootCA-QC")
                    || getTypeIdentifier().equalsIgnoreCase("http://uri.etsi.org/TrstSvc/Svctype/Archiv")
                    || getTypeIdentifier().equalsIgnoreCase("http://uri.etsi.org/TrstSvc/Svctype/IdV")
                    || getTypeIdentifier().equalsIgnoreCase("http://uri.etsi.org/TrstSvc/Svctype/KEscrow")
                    || getTypeIdentifier().equalsIgnoreCase("http://uri.etsi.org/TrstSvc/Svctype/PPwd")
                    || getTypeIdentifier().equalsIgnoreCase("http://uri.etsi.org/TrstSvc/Svctype/unspecified")) {
                addToHistory();
                setCurrentStatus(checkServiceStatus5531(getCurrentStatus()));

                /*
                 * 5.5.3.1
                 */
            } else if (getTypeIdentifier().equalsIgnoreCase("http://uri.etsi.org/TrstSvc/Svctype/SignaturePolicyAuthority")) {
                addToHistory();

                setTypeIdentifier("http://uri.etsi.org/TrstSvc/Svctype/SignaturePolicyAuthority");
                setCurrentStatus(checkServiceStatus5531(getCurrentStatus()));

                /*
                 * 5.5.3.1 --> ERROR SVD - SVC... Error in Standard... --> Migrate?
                 */
            } else if (getTypeIdentifier().equalsIgnoreCase("http://uri.etsi.org/TrstSvc/Svctype/TLIssuer")) {

                setTypeIdentifier("http://uri.etsi.org/TrstSvd/Svctype/TLIssuer");
                addToHistory();
                setCurrentStatus(checkServiceStatus5531(getCurrentStatus()));
            }

            //Date UTC format (UTC/GMT+2 -> 2016/07/01)
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            cal.set(Calendar.YEAR, 2016);
            cal.set(Calendar.MONTH, Calendar.JUNE);
            cal.set(Calendar.DAY_OF_MONTH, 30);
            cal.set(Calendar.HOUR_OF_DAY, 22);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            setCurrentStatusStartingDate(cal.getTime());

        }

    }

    private TLServiceExtension getASIForeSignature() {
        TLServiceExtension serviceToAdd = null;
        if (getExtension() != null) {
            serviceToAdd = new TLServiceExtension(getTlId(), getId() + "_" + Tag.SERVICE_EXTENSION + "_" + (getExtension().size() + 1), new ExtensionTypeV5());
        } else {
            serviceToAdd = new TLServiceExtension(getTlId(), getId() + "_" + Tag.SERVICE_EXTENSION + "_" + 1, new ExtensionTypeV5());
        }
        AdditionalServiceInformationTypeV5 aSIType = new AdditionalServiceInformationTypeV5();
        NonEmptyMultiLangURITypeV5 uriType = new NonEmptyMultiLangURITypeV5();
        uriType.setLang("en");
        uriType.setValue("http://uri.etsi.org/TrstSvc/TrustedList/SvcInfoExt/ForeSignatures");
        aSIType.setURI(uriType);
        TLAdditionnalServiceInfo aSIForeSignature = new TLAdditionnalServiceInfo(serviceToAdd.getTlId(), serviceToAdd.getId(), aSIType);
        serviceToAdd.setAdditionnalServiceInfo(aSIForeSignature);
        serviceToAdd.setCritical(true);
        return serviceToAdd;
    }

    private void addToHistory() {

        if (getHistory() != null) {
            getHistory().add(0, new TLServiceHistory(getTlId(), getId() + "_" + Tag.SERVICE_HISTORY + "_" + (getHistory().size() + 1), this));
        } else {
            List<TLServiceHistory> historyList = new ArrayList<>();
            historyList.add(0, new TLServiceHistory(getTlId(), getId() + "_" + Tag.SERVICE_HISTORY + "_" + 1, this));
            setHistory(historyList);
        }
    }

    private String checkServiceStatus5511(String publishStatus) {
        String tmpPublishStatus = StringUtils.trimToEmpty(publishStatus);
        String ret = "";
        switch (tmpPublishStatus) {
            case "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/undersupervision":
                ret = "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/granted";
                break;
            case "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/supervisionincessation":
                ret = "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/granted";
                break;
            case "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/accredited":
                ret = "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/granted";
                break;
            case "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/supervisionceased":
                ret = "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/withdrawn";
                break;
            case "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/supervisionrevoked":
                ret = "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/withdrawn";
                break;
            default:
                break;
        }

        return ret;
    }

    private String checkServiceStatus5521(String publishStatus) {
        String tmpPublishStatus = StringUtils.trimToEmpty(publishStatus);
        String ret = "";
        switch (tmpPublishStatus) {
            case "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/undersupervision":
                ret = "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/recognisedatnationallevel";
                break;
            case "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/supervisionincessation":
                ret = "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/recognisedatnationallevel";
                break;
            case "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/accredited":
                ret = "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/recognisedatnationallevel";
                break;
            case "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/supervisionceased":
                ret = "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/deprecatedatnationallevel";
                break;
            case "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/supervisionrevoked":
                ret = "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/deprecatedatnationallevel";
                break;
            default:
                break;
        }

        return ret;
    }

    private String checkServiceStatus5531(String publishStatus) {
        String tmpPublishStatus = StringUtils.trimToEmpty(publishStatus);
        String ret = "";
        switch (tmpPublishStatus) {
            case "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/undersupervision":
                ret = "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/recognisedatnationallevel";
                break;
            case "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/supervisionincessation":
                ret = "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/recognisedatnationallevel";
                break;
            case "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/accredited":
                ret = "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/recognisedatnationallevel";
                break;
            case "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/supervisionceased":
                ret = "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/deprecatedatnationallevel";
                break;
            case "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/supervisionrevoked":
                ret = "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/deprecatedatnationallevel";
                break;
            case "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/setbynationallaw":
                ret = "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/recognisedatnationallevel";
                break;
            case "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/deprecatedbynationallaw":
                ret = "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/deprecatedatnationallevel";
                break;
            default:
                break;
        }

        return ret;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((currentStatus == null) ? 0 : currentStatus.hashCode());
        result = (prime * result) + ((currentStatusStartingDate == null) ? 0 : currentStatusStartingDate.hashCode());
        result = (prime * result) + ((extension == null) ? 0 : extension.hashCode());
        result = (prime * result) + ((history == null) ? 0 : history.hashCode());
        result = (prime * result) + ((schemeDefinitionUri == null) ? 0 : schemeDefinitionUri.hashCode());
        result = (prime * result) + ((serviceDigitalIdentification == null) ? 0 : serviceDigitalIdentification.hashCode());
        result = (prime * result) + ((serviceName == null) ? 0 : serviceName.hashCode());
        result = (prime * result) + ((supplyPoint == null) ? 0 : supplyPoint.hashCode());
        result = (prime * result) + ((tspDefinitionUri == null) ? 0 : tspDefinitionUri.hashCode());
        result = (prime * result) + ((typeIdentifier == null) ? 0 : typeIdentifier.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TLServiceDto other = (TLServiceDto) obj;
        if (currentStatus == null) {
            if (other.currentStatus != null) {
                return false;
            }
        } else if (!currentStatus.equals(other.currentStatus)) {
            return false;
        }
        if (currentStatusStartingDate == null) {
            if (other.currentStatusStartingDate != null) {
                return false;
            }
        } else if (!currentStatusStartingDate.equals(other.currentStatusStartingDate)) {
            return false;
        }
        if (extension == null) {
            if (other.extension != null) {
                return false;
            }
        } else if (!extension.equals(other.extension)) {
            return false;
        }
        if (history == null) {
            if (other.history != null) {
                return false;
            }
        } else if (!history.equals(other.history)) {
            return false;
        }
        if (schemeDefinitionUri == null) {
            if (other.schemeDefinitionUri != null) {
                return false;
            }
        } else if (!schemeDefinitionUri.equals(other.schemeDefinitionUri)) {
            return false;
        }
        if (serviceDigitalIdentification == null) {
            if (other.serviceDigitalIdentification != null) {
                return false;
            }
        } else if (!serviceDigitalIdentification.equals(other.serviceDigitalIdentification)) {
            return false;
        }
        if (serviceName == null) {
            if (other.serviceName != null) {
                return false;
            }
        } else if (!serviceName.equals(other.serviceName)) {
            return false;
        }
        if (supplyPoint == null) {
            if (other.supplyPoint != null) {
                return false;
            }
        } else if (!supplyPoint.equals(other.supplyPoint)) {
            return false;
        }
        if (tspDefinitionUri == null) {
            if (other.tspDefinitionUri != null) {
                return false;
            }
        } else if (!tspDefinitionUri.equals(other.tspDefinitionUri)) {
            return false;
        }
        if (typeIdentifier == null) {
            if (other.typeIdentifier != null) {
                return false;
            }
        } else if (!typeIdentifier.equals(other.typeIdentifier)) {
            return false;
        }
        return true;
    }

    public String getTypeIdentifier() {
        return typeIdentifier;
    }

    public void setTypeIdentifier(String typeIdentifier) {
        this.typeIdentifier = typeIdentifier;
    }

    public List<TLName> getServiceName() {
        return serviceName;
    }

    public void setServiceName(List<TLName> serviceName) {
        this.serviceName = serviceName;
    }

    public List<TLDigitalIdentification> getDigitalIdentification() {
        return serviceDigitalIdentification;
    }

    public void setDigitalIdentification(List<TLDigitalIdentification> digitalIdentification) {
        serviceDigitalIdentification = digitalIdentification;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public Date getCurrentStatusStartingDate() {
        return currentStatusStartingDate;
    }

    public void setCurrentStatusStartingDate(Date currentStatusStartingDate) {
        this.currentStatusStartingDate = currentStatusStartingDate;
    }

    public List<TLDefinitionUri> getSchemeDefinitionUri() {
        return schemeDefinitionUri;
    }

    public void setSchemeDefinitionUri(List<TLDefinitionUri> schemeDefinitionUri) {
        this.schemeDefinitionUri = schemeDefinitionUri;
    }

    public List<TLDefinitionUri> getTSPDefinitionUri() {
        return tspDefinitionUri;
    }

    public void setTSPDefinitionUri(List<TLDefinitionUri> tSPDefinitionUri) {
        tspDefinitionUri = tSPDefinitionUri;
    }

    public List<TLSupplyPoint> getSupplyPoint() {
        return supplyPoint;
    }

    public void setSupplyPoint(List<TLSupplyPoint> supplyPoint) {
        this.supplyPoint = supplyPoint;
    }

    public List<TLServiceExtension> getExtension() {
        return extension;
    }

    public void setExtension(List<TLServiceExtension> extension) {
        this.extension = extension;
    }

    public List<TLServiceHistory> getHistory() {
        return history;
    }

    public void setHistory(List<TLServiceHistory> history) {
        this.history = history;
    }

    public List<String> getqServiceTypes() {
        return qServiceTypes;
    }

    public void setqServiceTypes(List<String> qServiceTypes) {
        this.qServiceTypes = qServiceTypes;
    }

    public List<String> initQServiceTypes() {
        qServiceTypes = new ArrayList<>(TLQServiceTypeUtils.getServiceTypes(this));
        Collections.sort(qServiceTypes, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        return qServiceTypes;
    }

}
