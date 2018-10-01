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
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.joinup.tsl.business.dto.TLDifference;
import eu.europa.ec.joinup.tsl.business.util.ChangeUtils;
import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.ec.joinup.tsl.model.enums.Tag;
import eu.europa.esig.jaxb.tsl.ExtensionType;
import eu.europa.esig.jaxb.tsl.MultiLangNormStringType;
import eu.europa.esig.jaxb.tsl.NonEmptyMultiLangURIType;
import eu.europa.esig.jaxb.tsl.PostalAddressType;
import eu.europa.esig.jaxb.tsl.TSPInformationType;
import eu.europa.esig.jaxb.tsl.TSPServiceType;
import eu.europa.esig.jaxb.tsl.TSPType;
import eu.europa.esig.jaxb.v5.tsl.AddressTypeV5;
import eu.europa.esig.jaxb.v5.tsl.ElectronicAddressTypeV5;
import eu.europa.esig.jaxb.v5.tsl.ExtensionTypeV5;
import eu.europa.esig.jaxb.v5.tsl.ExtensionsListTypeV5;
import eu.europa.esig.jaxb.v5.tsl.InternationalNamesTypeV5;
import eu.europa.esig.jaxb.v5.tsl.MultiLangNormStringTypeV5;
import eu.europa.esig.jaxb.v5.tsl.NonEmptyMultiLangURIListTypeV5;
import eu.europa.esig.jaxb.v5.tsl.NonEmptyMultiLangURITypeV5;
import eu.europa.esig.jaxb.v5.tsl.PostalAddressListTypeV5;
import eu.europa.esig.jaxb.v5.tsl.PostalAddressTypeV5;
import eu.europa.esig.jaxb.v5.tsl.TSPInformationTypeV5;
import eu.europa.esig.jaxb.v5.tsl.TSPServiceTypeV5;
import eu.europa.esig.jaxb.v5.tsl.TSPServicesListTypeV5;
import eu.europa.esig.jaxb.v5.tsl.TSPTypeV5;

public class TLServiceProvider extends AbstractTLDTO {

    private static final Logger LOGGER = LoggerFactory.getLogger(TLServiceProvider.class);
    private static final ResourceBundle bundle = ResourceBundle.getBundle("messages");

    private List<TLName> tspName;
    private List<TLName> tspTradeName;
    private List<TLPostalAddress> tspPostal;
    private List<TLElectronicAddress> tspElectronic;
    private List<TLInformationUri> tspInfoUri;
    private List<TLServiceDto> tspServices;
    private List<TLInformationExtension> tspExtension;
    private List<String> qServiceTypes;

    public TLServiceProvider() {
    }

    public TLServiceProvider(int iddb, String location, TSPType tslProvider) {
        super(iddb, location);
        int i = 0;
        if (tslProvider.getTSPInformation() != null) {
            TSPInformationType info = tslProvider.getTSPInformation();

            if (info.getTSPName() != null) {
                List<TLName> names = new ArrayList<>();
                i = 0;
                for (MultiLangNormStringType name : info.getTSPName().getName()) {
                    i++;
                    names.add(new TLName(getTlId(), getId() + "_" + Tag.TSP_NAME + "_" + i, name));
                }
                setTSPName(names);
            }

            if (info.getTSPTradeName() != null) {
                List<TLName> tradeNames = new ArrayList<>();
                i = 0;
                for (MultiLangNormStringType name : info.getTSPTradeName().getName()) {
                    i++;
                    tradeNames.add(new TLName(getTlId(), getId() + "_" + Tag.TSP_TRADE_NAME + "_" + i, name));
                }
                setTSPTradeName(tradeNames);
            }

            if ((info.getTSPAddress() != null) && (info.getTSPAddress().getPostalAddresses() != null)) {
                List<TLPostalAddress> postalAddresses = new ArrayList<>();
                i = 0;
                for (PostalAddressType postAdr : info.getTSPAddress().getPostalAddresses().getPostalAddress()) {
                    i++;
                    postalAddresses.add(new TLPostalAddress(getTlId(), getId() + "_" + Tag.POSTAL_ADDRESSES + "_" + i, postAdr));
                }
                setTSPPostal(postalAddresses);
            }

            if ((info.getTSPAddress() != null) && (info.getTSPAddress().getElectronicAddress() != null)) {
                List<TLElectronicAddress> electronicAddresses = new ArrayList<>();
                i = 0;
                for (NonEmptyMultiLangURIType elecAdt : info.getTSPAddress().getElectronicAddress().getURI()) {
                    i++;
                    electronicAddresses.add(new TLElectronicAddress(getTlId(), getId() + "_" + Tag.ELECTRONIC_ADDRESS + "_" + i, elecAdt));
                }
                setTSPElectronic(electronicAddresses);
            }

            if (info.getTSPInformationURI() != null) {
                List<TLInformationUri> infoUris = new ArrayList<>();
                i = 0;
                for (NonEmptyMultiLangURIType uri : info.getTSPInformationURI().getURI()) {
                    i++;
                    infoUris.add(new TLInformationUri(getTlId(), getId() + "_" + Tag.TSP_INFORMATION_URI + "_" + i, uri));
                }
                setTSPInfoUri(infoUris);
            }

            if (info.getTSPInformationExtensions() != null) {
                LOGGER.warn("Nothing done for TSP Information Extensions");
                List<TLInformationExtension> infoExtensions = new ArrayList<>();
                i = 0;
                for (ExtensionType ext : info.getTSPInformationExtensions().getExtension()) {
                    i++;
                    infoExtensions.add(new TLInformationExtension(getTlId(), getId() + "_" + Tag.TSP_INFORMATION_EXT + "_" + i, ext));
                }
                setTSPExtension(infoExtensions);
            }

            if (tslProvider.getTSPServices() != null) {
                List<TLServiceDto> services = new ArrayList<>();
                i = 0;
                for (TSPServiceType serviceType : tslProvider.getTSPServices().getTSPService()) {
                    i++;
                    services.add(new TLServiceDto(getTlId(), getId() + "_" + Tag.TSP_SERVICE + "_" + i, serviceType));
                }
                setTSPServices(services);
            }
        }
    }

    public TLServiceProvider(int iddb, String location, TSPTypeV5 tslProvider) {
        super(iddb, location);
        int i = 0;
        if (tslProvider.getTSPInformation() != null) {
            TSPInformationTypeV5 info = tslProvider.getTSPInformation();

            if (info.getTSPName() != null) {
                List<TLName> names = new ArrayList<>();
                i = 0;
                for (MultiLangNormStringTypeV5 name : info.getTSPName().getName()) {
                    i++;
                    names.add(new TLName(getTlId(), getId() + "_" + Tag.TSP_NAME + "_" + i, name));
                }
                setTSPName(names);
            }

            if (info.getTSPTradeName() != null) {
                List<TLName> tradeNames = new ArrayList<>();
                i = 0;
                for (MultiLangNormStringTypeV5 name : info.getTSPTradeName().getName()) {
                    i++;
                    tradeNames.add(new TLName(getTlId(), getId() + "_" + Tag.TSP_TRADE_NAME + "_" + i, name));
                }
                setTSPTradeName(tradeNames);
            }

            if ((info.getTSPAddress() != null) && (info.getTSPAddress().getPostalAddresses() != null)) {
                List<TLPostalAddress> postalAddresses = new ArrayList<>();
                i = 0;
                for (PostalAddressTypeV5 postAdr : info.getTSPAddress().getPostalAddresses().getPostalAddress()) {
                    i++;
                    postalAddresses.add(new TLPostalAddress(getTlId(), getId() + "_" + Tag.POSTAL_ADDRESSES + "_" + i, postAdr));
                }
                setTSPPostal(postalAddresses);
            }

            if ((info.getTSPAddress() != null) && (info.getTSPAddress().getElectronicAddress() != null)) {
                List<TLElectronicAddress> electronicAddresses = new ArrayList<>();
                i = 0;
                for (NonEmptyMultiLangURITypeV5 elecAdt : info.getTSPAddress().getElectronicAddress().getURI()) {
                    i++;
                    electronicAddresses.add(new TLElectronicAddress(getTlId(), getId() + "_" + Tag.ELECTRONIC_ADDRESS + "_" + i, elecAdt));
                }
                setTSPElectronic(electronicAddresses);
            }

            if (info.getTSPInformationURI() != null) {
                List<TLInformationUri> infoUris = new ArrayList<>();
                i = 0;
                for (NonEmptyMultiLangURITypeV5 uri : info.getTSPInformationURI().getURI()) {
                    i++;
                    infoUris.add(new TLInformationUri(getTlId(), getId() + "_" + Tag.TSP_INFORMATION_URI + "_" + i, uri));
                }
                setTSPInfoUri(infoUris);
            }

            if (info.getTSPInformationExtensions() != null) {
                List<TLInformationExtension> infoExtensions = new ArrayList<>();
                i = 0;
                for (ExtensionTypeV5 ext : info.getTSPInformationExtensions().getExtension()) {
                    i++;
                    infoExtensions.add(new TLInformationExtension(getTlId(), getId() + "_" + Tag.TSP_INFORMATION_EXT + "_" + i, ext));
                }
                setTSPExtension(infoExtensions);
            }

            if (tslProvider.getTSPServices() != null) {
                List<TLServiceDto> services = new ArrayList<>();
                i = 0;
                for (TSPServiceTypeV5 serviceType : tslProvider.getTSPServices().getTSPService()) {
                    i++;
                    services.add(new TLServiceDto(getTlId(), getId() + "_" + Tag.TSP_SERVICE + "_" + i, serviceType));
                }
                setTSPServices(services);
            }
        }
    }

    public List<TLDifference> asPublishedDiff(TLServiceProvider publishedTlServiceProvider, String parentId) {
        List<TLDifference> diffList = new ArrayList<>();

        /*************************************
         * List<TLService> tspServices;
         *************************************/
        // COPY VALUE OF DRAFT DATA
        List<TLServiceDto> tmp = !CollectionUtils.isEmpty(getTSPServices()) ? new ArrayList<>(getTSPServices()) : new ArrayList<TLServiceDto>();
        List<TLServiceDto> tmpPublished = !CollectionUtils.isEmpty(publishedTlServiceProvider.getTSPServices()) ? new ArrayList<>(publishedTlServiceProvider.getTSPServices())
                : new ArrayList<TLServiceDto>();

        // DELETE EQUALS TLNAME
        if (!CollectionUtils.isEmpty(getTSPServices())) {
            for (int i = 0; i < getTSPServices().size(); i++) {
                if (tmpPublished.contains(getTSPServices().get(i))) {
                    tmp.remove(getTSPServices().get(i));
                    tmpPublished.remove(getTSPServices().get(i));
                }
            }
        }

        // CHECK OTHERS tspServices
        for (TLServiceDto draft : tmp) {
            boolean find = false;
            // LANGUAGE AND VALUE NOT PRESENT
            if (draft.getTypeIdentifier() != null) {
                List<TLDifference> allTmpDifList = new ArrayList<>();
                for (TLServiceDto published : tmpPublished) {
                    if (draft.getServiceName().equals(published.getServiceName())) {// &&
                        // draft.getCurrentStatusStartingDate().equals(published.getCurrentStatusStartingDate()
                        // SAME TYPE AND STARTING DATE
                        List<TLDifference> tmpDifList = draft.asPublishedDiff(published);
                        if ((tmpDifList != null) && !tmpDifList.isEmpty()) {
                            allTmpDifList.addAll(tmpDifList);
                            tmpPublished.remove(published);
                            find = true;
                            break;
                        }
                    }
                }

                if (!allTmpDifList.isEmpty()) {
                    diffList.addAll(allTmpDifList);
                }

                // IF NOT FIND LANGUAGE --> NEW
                if (!find && (draft.getCurrentStatusStartingDate() != null)) {
                    diffList.add(new TLDifference(draft.getId(), "", draft.getTypeIdentifier() + " - " + TLUtils.toStringFormat(draft.getCurrentStatusStartingDate())));
                }

            } else {
                // NO TYPE --> NO CHANGE
                diffList.add(new TLDifference(draft.getId(), "", bundle.getString("changes.noTypeIdentifier")));
            }
        }

        // DELETE ITEM IN DIFF LIST
        for (TLServiceDto published : tmpPublished) {
            diffList.add(new TLDifference(parentId + "_" + Tag.TSP_SERVICE,
                    published.getTypeIdentifier() + " - " + TLUtils.toStringFormat(published.getCurrentStatusStartingDate()), ""));
        }

        /*************************************
         * List<TLName> tspName;
         *************************************/
        if (CollectionUtils.isNotEmpty(getTSPName())) {
            List<TLDifference> changeList = ChangeUtils.diffOfTLNameList(getTSPName(), publishedTlServiceProvider.getTSPName(), getId() + "_" + Tag.TSP_NAME);
            if (changeList.size() > 0) {
                diffList.addAll(changeList);
            }
        } else {
            if ((publishedTlServiceProvider.getTSPName() != null) && CollectionUtils.isNotEmpty(publishedTlServiceProvider.getTSPName())) {
                diffList.addAll(ChangeUtils.initEmptyCurrentListDifference(publishedTlServiceProvider.getTSPName(), getId() + '_' + Tag.TSP_NAME));
            }
        }

        /*************************************
         * List<TLName> tspTradeName;
         *************************************/
        if (CollectionUtils.isNotEmpty(getTSPTradeName())) {
            List<TLDifference> changeList = ChangeUtils.diffOfTLNameList(getTSPTradeName(), publishedTlServiceProvider.getTSPTradeName(), getId() + "_" + Tag.TSP_TRADE_NAME);
            if (changeList.size() > 0) {
                diffList.addAll(changeList);
            }
        } else {
            if ((publishedTlServiceProvider.getTSPTradeName() != null) && CollectionUtils.isNotEmpty(publishedTlServiceProvider.getTSPTradeName())) {
                diffList.addAll(ChangeUtils.initEmptyCurrentListDifference(publishedTlServiceProvider.getTSPTradeName(), getId() + '_' + Tag.TSP_TRADE_NAME));
            }
        }

        /*************************************
         * List<TLPostalAddress> tspPostal;
         *************************************/
        if (CollectionUtils.isNotEmpty(getTSPPostal())) {
            List<TLDifference> changeList = ChangeUtils.diffOfPostalList(getTSPPostal(), publishedTlServiceProvider.getTSPPostal(), getId() + "_" + Tag.POSTAL_ADDRESSES);
            if (changeList.size() > 0) {
                diffList.addAll(changeList);
            }
        } else {
            if ((publishedTlServiceProvider.getTSPPostal() != null) && CollectionUtils.isNotEmpty(publishedTlServiceProvider.getTSPPostal())) {
                diffList.addAll(ChangeUtils.initEmptyCurrentPostalAddressDifference(publishedTlServiceProvider.getTSPPostal(), getId() + '_' + Tag.POSTAL_ADDRESSES));
            }
        }

        /*************************************
         * List<TLElectronicAddress> tspElectronic;
         *************************************/
        if (CollectionUtils.isNotEmpty(getTSPElectronic())) {
            List<TLDifference> changeList = ChangeUtils.diffOfElectronic(getTSPElectronic(), publishedTlServiceProvider.getTSPElectronic(), getId() + "_" + Tag.ELECTRONIC_ADDRESS);
            if (changeList.size() > 0) {
                diffList.addAll(changeList);
            }
        } else {
            if ((publishedTlServiceProvider.getTSPElectronic() != null) && CollectionUtils.isNotEmpty(publishedTlServiceProvider.getTSPElectronic())) {
                diffList.addAll(ChangeUtils.initEmptyCurrentListDifference(publishedTlServiceProvider.getTSPElectronic(), getId() + '_' + Tag.ELECTRONIC_ADDRESS));
            }
        }

        /*************************************
         * List<TLInformationUri> tspInfoUri;
         *************************************/
        if (CollectionUtils.isNotEmpty(getTSPInfoUri())) {
            List<TLDifference> changeList = ChangeUtils.diffOfTLInfoUriList(getTSPInfoUri(), publishedTlServiceProvider.getTSPInfoUri(), getId() + "_" + Tag.TSP_INFORMATION_URI);
            if (changeList.size() > 0) {
                diffList.addAll(changeList);
            }
        } else {
            if ((publishedTlServiceProvider.getTSPInfoUri() != null) && CollectionUtils.isNotEmpty(publishedTlServiceProvider.getTSPInfoUri())) {
                diffList.addAll(ChangeUtils.initEmptyCurrentListDifference(publishedTlServiceProvider.getTSPInfoUri(), getId() + '_' + Tag.TSP_INFORMATION_URI));
            }
        }

        /*************************************
         * List<TLInformationExtension> tspExtension;
         *************************************/

        return diffList;
    }

    public TSPTypeV5 asTSLTypeV5() {
        TSPTypeV5 tspType = new TSPTypeV5();
        TSPInformationTypeV5 info = new TSPInformationTypeV5();
        TSPServicesListTypeV5 tspService = new TSPServicesListTypeV5();

        // TSP NAME
        if ((getTSPName() != null) && CollectionUtils.isNotEmpty(getTSPName())) {
            InternationalNamesTypeV5 intTspName = new InternationalNamesTypeV5();
            for (TLName tlName : getTSPName()) {
                intTspName.getName().add(tlName.asTSLTypeV5());
            }
            info.setTSPName(intTspName);
        }

        // TSP TRADE NAME
        if ((getTSPTradeName() != null) && CollectionUtils.isNotEmpty(getTSPTradeName())) {
            InternationalNamesTypeV5 intTspTradeName = new InternationalNamesTypeV5();
            for (TLName tlName : getTSPTradeName()) {
                intTspTradeName.getName().add(tlName.asTSLTypeV5());
            }
            info.setTSPTradeName(intTspTradeName);
        }

        // SCHEME OPERATOR ADDRESS (POSTAL AND ELECTRONIC)
        AddressTypeV5 adrType = new AddressTypeV5();
        int k = 0;
        if ((getTSPElectronic() != null) && CollectionUtils.isNotEmpty(getTSPElectronic())) {
            ElectronicAddressTypeV5 electronicAdr = new ElectronicAddressTypeV5();
            for (TLElectronicAddress tlElec : getTSPElectronic()) {
                k++;
                electronicAdr.getURI().add(tlElec.asTSLTypeV5());
            }
            adrType.setElectronicAddress(electronicAdr);
        }

        if ((getTSPPostal() != null) && CollectionUtils.isNotEmpty(getTSPPostal())) {
            PostalAddressListTypeV5 postalAdrType = new PostalAddressListTypeV5();
            for (TLPostalAddress tlPostal : getTSPPostal()) {
                postalAdrType.getPostalAddress().add(tlPostal.asTSLTypeV5());
                k++;
            }
            adrType.setPostalAddresses(postalAdrType);
        }
        if (k > 0) {
            info.setTSPAddress(adrType);
        }

        // INFORMATION URI
        if ((getTSPInfoUri() != null) && CollectionUtils.isNotEmpty(getTSPInfoUri())) {
            NonEmptyMultiLangURIListTypeV5 langUriList = new NonEmptyMultiLangURIListTypeV5();
            for (TLInformationUri tlUri : getTSPInfoUri()) {
                langUriList.getURI().add(tlUri.asTSLTypeV5());
            }
            info.setTSPInformationURI(langUriList);
        }

        // TL EXTENSTION
        if ((getTSPExtension() != null) && CollectionUtils.isNotEmpty(getTSPExtension())) {
            ExtensionsListTypeV5 extensionList = new ExtensionsListTypeV5();
            int nbEmpty = 0;
            for (TLInformationExtension tlInfoExt : getTSPExtension()) {
                if (!StringUtils.isEmpty(tlInfoExt.getValue())) {
                    extensionList.getExtension().add(tlInfoExt.asTSLTypeV5());
                } else {
                    nbEmpty = nbEmpty + 1;
                }
            }

            if (nbEmpty != getTSPExtension().size()) {
                info.setTSPInformationExtensions(extensionList);
            }
        }

        // SERVICES
        if ((getTSPServices() != null) && CollectionUtils.isNotEmpty(getTSPServices())) {
            for (TLServiceDto service : getTSPServices()) {
                tspService.getTSPService().add(service.asTSLTypeV5());
            }
            tspType.setTSPServices(tspService);
        }

        tspType.setTSPInformation(info);
        return tspType;
    }

    public void toV5FromV4() {

        if (getTSPServices() != null) {
            for (TLServiceDto tlService : getTSPServices()) {
                tlService.toV5FromV4();
            }
        }

    }

    public String getName() {
        if ((this != null) && !CollectionUtils.isEmpty(getTSPName())) {
            if (getTSPName().get(0).getValue() != null) {
                return getTSPName().get(0).getValue();
            }
        }
        return "";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((tspElectronic == null) ? 0 : tspElectronic.hashCode());
        result = (prime * result) + ((tspExtension == null) ? 0 : tspExtension.hashCode());
        result = (prime * result) + ((tspInfoUri == null) ? 0 : tspInfoUri.hashCode());
        result = (prime * result) + ((tspName == null) ? 0 : tspName.hashCode());
        result = (prime * result) + ((tspPostal == null) ? 0 : tspPostal.hashCode());
        result = (prime * result) + ((tspServices == null) ? 0 : tspServices.hashCode());
        result = (prime * result) + ((tspTradeName == null) ? 0 : tspTradeName.hashCode());
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
        TLServiceProvider other = (TLServiceProvider) obj;
        if (tspElectronic == null) {
            if (other.tspElectronic != null) {
                return false;
            }
        } else if (!tspElectronic.equals(other.tspElectronic)) {
            return false;
        }
        if (tspExtension == null) {
            if (other.tspExtension != null) {
                return false;
            }
        } else if (!tspExtension.equals(other.tspExtension)) {
            return false;
        }
        if (tspInfoUri == null) {
            if (other.tspInfoUri != null) {
                return false;
            }
        } else if (!tspInfoUri.equals(other.tspInfoUri)) {
            return false;
        }
        if (tspName == null) {
            if (other.tspName != null) {
                return false;
            }
        } else if (!tspName.equals(other.tspName)) {
            return false;
        }
        if (tspPostal == null) {
            if (other.tspPostal != null) {
                return false;
            }
        } else if (!tspPostal.equals(other.tspPostal)) {
            return false;
        }
        if (tspServices == null) {
            if (other.tspServices != null) {
                return false;
            }
        } else if (!tspServices.equals(other.tspServices)) {
            return false;
        }
        if (tspTradeName == null) {
            if (other.tspTradeName != null) {
                return false;
            }
        } else if (!tspTradeName.equals(other.tspTradeName)) {
            return false;
        }
        return true;
    }

    /*
     * GETTERS AND SETTERS
     */
    public List<TLName> getTSPName() {
        return tspName;
    }

    public void setTSPName(List<TLName> tSPName) {
        tspName = tSPName;
    }

    public List<TLName> getTSPTradeName() {
        return tspTradeName;
    }

    public void setTSPTradeName(List<TLName> tSPTradeName) {
        tspTradeName = tSPTradeName;
    }

    public List<TLPostalAddress> getTSPPostal() {
        return tspPostal;
    }

    public void setTSPPostal(List<TLPostalAddress> tSPPostal) {
        tspPostal = tSPPostal;
    }

    public List<TLElectronicAddress> getTSPElectronic() {
        return tspElectronic;
    }

    public void setTSPElectronic(List<TLElectronicAddress> tSPElectronic) {
        tspElectronic = tSPElectronic;
    }

    public List<TLInformationUri> getTSPInfoUri() {
        return tspInfoUri;
    }

    public void setTSPInfoUri(List<TLInformationUri> tSPInfoUri) {
        tspInfoUri = tSPInfoUri;
    }

    public List<TLInformationExtension> getTSPExtension() {
        return tspExtension;
    }

    public void setTSPExtension(List<TLInformationExtension> tSPExtension) {
        tspExtension = tSPExtension;
    }

    public List<TLServiceDto> getTSPServices() {
        return tspServices;
    }

    public void setTSPServices(List<TLServiceDto> tSPServices) {
        tspServices = tSPServices;
    }

    public List<String> getqServiceTypes() {
        if (qServiceTypes == null) {
            qServiceTypes = new ArrayList<>();
        }
        return qServiceTypes;
    }

    public void setqServiceTypes(List<String> qServiceTypes) {
        this.qServiceTypes = qServiceTypes;
    }

}
