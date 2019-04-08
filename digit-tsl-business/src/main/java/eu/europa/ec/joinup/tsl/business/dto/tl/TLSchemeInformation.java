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
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.joinup.tsl.business.dto.TLDifference;
import eu.europa.ec.joinup.tsl.business.util.ChangeUtils;
import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.ec.joinup.tsl.model.enums.Tag;
import eu.europa.esig.jaxb.tsl.MultiLangNormStringType;
import eu.europa.esig.jaxb.tsl.MultiLangStringType;
import eu.europa.esig.jaxb.tsl.NonEmptyMultiLangURIType;
import eu.europa.esig.jaxb.tsl.PostalAddressType;
import eu.europa.esig.jaxb.tsl.TSLSchemeInformationType;
import eu.europa.esig.jaxb.v5.tsl.AddressTypeV5;
import eu.europa.esig.jaxb.v5.tsl.ElectronicAddressTypeV5;
import eu.europa.esig.jaxb.v5.tsl.InternationalNamesTypeV5;
import eu.europa.esig.jaxb.v5.tsl.MultiLangNormStringTypeV5;
import eu.europa.esig.jaxb.v5.tsl.MultiLangStringTypeV5;
import eu.europa.esig.jaxb.v5.tsl.NextUpdateTypeV5;
import eu.europa.esig.jaxb.v5.tsl.NonEmptyMultiLangURIListTypeV5;
import eu.europa.esig.jaxb.v5.tsl.NonEmptyMultiLangURITypeV5;
import eu.europa.esig.jaxb.v5.tsl.NonEmptyURIListTypeV5;
import eu.europa.esig.jaxb.v5.tsl.PolicyOrLegalnoticeTypeV5;
import eu.europa.esig.jaxb.v5.tsl.PostalAddressListTypeV5;
import eu.europa.esig.jaxb.v5.tsl.PostalAddressTypeV5;
import eu.europa.esig.jaxb.v5.tsl.TSLSchemeInformationTypeV5;

public class TLSchemeInformation extends AbstractTLDTO {

    private static final Logger LOGGER = LoggerFactory.getLogger(TLSchemeInformation.class);

    private int tlIdentifier;
    private int sequenceNumber;
    private String type;
    private List<TLName> schemeOpeName;
    private List<TLPostalAddress> schemeOpePostal;
    private List<TLElectronicAddress> schemeOpeElectronic;
    private List<TLName> schemeName;
    private List<TLInformationUri> schemeInfoUri;
    private String statusDetermination;
    private List<TLSchemeTypeCommunityRule> schemeTypeCommRule;
    private String territory;
    private List<TLSchemePolicy> schemePolicy;
    private int historicalPeriod;
    private Date issueDate;
    private Date nextUpdateDate;
    private List<TLDistributionPoint> distributionPoint;

    public TLSchemeInformation() {
    }

    public TLSchemeInformation(int iddb, TSLSchemeInformationType tslSchemeInfo) {
        super(iddb, "" + iddb + "_" + Tag.SCHEME_INFORMATION);
        LOGGER.debug("Create TLSchemeInformation V4");
        if (tslSchemeInfo.getHistoricalInformationPeriod() != null) {
            setHistoricalPeriod(tslSchemeInfo.getHistoricalInformationPeriod().intValue());
        }
        if (tslSchemeInfo.getTSLSequenceNumber() != null) {
            setSequenceNumber(tslSchemeInfo.getTSLSequenceNumber().intValue());
        }

        if (tslSchemeInfo.getSchemeTerritory() != null) {
            setTerritory(tslSchemeInfo.getSchemeTerritory());
        }

        if (tslSchemeInfo.getListIssueDateTime() != null) {
            setIssueDate(TLUtils.toDate(tslSchemeInfo.getListIssueDateTime()));
        }

        if (tslSchemeInfo.getNextUpdate() != null) {
            setNextUpdateDate(TLUtils.toDate(tslSchemeInfo.getNextUpdate().getDateTime()));
        }

        if (tslSchemeInfo.getTSLType() != null) {
            setType(tslSchemeInfo.getTSLType());
        }

        if (tslSchemeInfo.getStatusDeterminationApproach() != null) {
            setStatusDetermination(tslSchemeInfo.getStatusDeterminationApproach());
        }

        if (tslSchemeInfo.getTSLVersionIdentifier() != null) {
            setTlIdentifier(tslSchemeInfo.getTSLVersionIdentifier().intValue());
        }

        List<TLName> schemeNames = new ArrayList<>();
        int i = 0;
        if (tslSchemeInfo.getSchemeName() != null) {
            for (MultiLangNormStringType name : tslSchemeInfo.getSchemeName().getName()) {
                i++;
                schemeNames.add(new TLName(iddb, getId() + "_" + Tag.SCHEME_NAME + "_" + i, name));
            }
        }
        setSchemeName(schemeNames);

        i = 0;
        List<TLPostalAddress> postalAddresses = new ArrayList<>();
        List<TLElectronicAddress> electronicAddresses = new ArrayList<>();
        if (tslSchemeInfo.getSchemeOperatorAddress() != null) {
            for (PostalAddressType postAdr : tslSchemeInfo.getSchemeOperatorAddress().getPostalAddresses().getPostalAddress()) {
                i++;
                postalAddresses.add(new TLPostalAddress(iddb, getId() + "_" + Tag.POSTAL_ADDRESSES + "_" + i, postAdr));
            }
            i = 0;
            for (NonEmptyMultiLangURIType elecAdt : tslSchemeInfo.getSchemeOperatorAddress().getElectronicAddress().getURI()) {
                i++;
                electronicAddresses.add(new TLElectronicAddress(iddb, getId() + "_" + Tag.ELECTRONIC_ADDRESS + "_" + i, elecAdt));
            }
        }
        setSchemeOpePostal(postalAddresses);
        setSchemeOpeElectronic(electronicAddresses);

        List<TLName> operatorNames = new ArrayList<>();
        i = 0;
        if (tslSchemeInfo.getSchemeOperatorName() != null) {
            for (MultiLangNormStringType name : tslSchemeInfo.getSchemeOperatorName().getName()) {
                i++;
                operatorNames.add(new TLName(iddb, getId() + "_" + Tag.SCHEME_OPERATOR_NAME + "_" + i, name));
            }
        }
        setSchemeOpeName(operatorNames);

        List<TLInformationUri> infoUris = new ArrayList<>();
        i = 0;
        if (tslSchemeInfo.getSchemeInformationURI() != null) {
            for (NonEmptyMultiLangURIType uri : tslSchemeInfo.getSchemeInformationURI().getURI()) {
                i++;
                infoUris.add(new TLInformationUri(iddb, getId() + "_" + Tag.SCHEME_INFORMATION_URI + "_" + i, uri));
            }
        }
        setSchemeInfoUri(infoUris);

        List<TLSchemeTypeCommunityRule> schemeTypeComm = new ArrayList<>();
        i = 0;
        if (tslSchemeInfo.getSchemeTypeCommunityRules() != null) {
            for (NonEmptyMultiLangURIType uri : tslSchemeInfo.getSchemeTypeCommunityRules().getURI()) {
                i++;
                schemeTypeComm.add(new TLSchemeTypeCommunityRule(iddb, getId() + "_" + Tag.SCHEME_TYPE_COMMUNITY_RULES + "_" + i, uri));
            }
        }
        setSchemeTypeCommRule(schemeTypeComm);

        List<TLSchemePolicy> policyOrLegalNotices = new ArrayList<>();
        i = 0;
        if (tslSchemeInfo.getPolicyOrLegalNotice() != null) {
            for (NonEmptyMultiLangURIType uri : tslSchemeInfo.getPolicyOrLegalNotice().getTSLPolicy()) {
                i++;
                policyOrLegalNotices.add(new TLSchemePolicy(iddb, getId() + "_" + Tag.POLICY_OR_LEGAL_NOTICE + "_" + i, uri));
            }
            for (MultiLangStringType legal : tslSchemeInfo.getPolicyOrLegalNotice().getTSLLegalNotice()) {
                i++;
                policyOrLegalNotices.add(new TLSchemePolicy(iddb, getId() + "_" + Tag.POLICY_OR_LEGAL_NOTICE + "_" + i, legal));
            }
        }
        setSchemePolicy(policyOrLegalNotices);

        i = 0;
        List<TLDistributionPoint> distributionPoints = new ArrayList<>();
        if (tslSchemeInfo.getDistributionPoints() != null) {
            for (String distribution : tslSchemeInfo.getDistributionPoints().getURI()) {
                i++;
                distributionPoints.add(new TLDistributionPoint(iddb, getId() + "_" + Tag.DISTRIBUTION_LIST + "_" + i, distribution));
            }
        }
        setDistributionPoint(distributionPoints);

    }

    public TLSchemeInformation(int iddb, TSLSchemeInformationTypeV5 tslSchemeInfo) {
        super(iddb, "" + iddb + "_" + Tag.SCHEME_INFORMATION);
        LOGGER.debug("Create TLSchemeInformation V5");
        if (tslSchemeInfo.getHistoricalInformationPeriod() != null) {
            setHistoricalPeriod(tslSchemeInfo.getHistoricalInformationPeriod().intValue());
        }
        if (tslSchemeInfo.getTSLSequenceNumber() != null) {
            setSequenceNumber(tslSchemeInfo.getTSLSequenceNumber().intValue());
        }

        if (tslSchemeInfo.getSchemeTerritory() != null) {
            setTerritory(tslSchemeInfo.getSchemeTerritory());
        }

        if (tslSchemeInfo.getListIssueDateTime() != null) {
            setIssueDate(TLUtils.toDate(tslSchemeInfo.getListIssueDateTime()));
        }

        if (tslSchemeInfo.getNextUpdate() != null) {
            setNextUpdateDate(TLUtils.toDate(tslSchemeInfo.getNextUpdate().getDateTime()));
        }

        if (tslSchemeInfo.getTSLType() != null) {
            setType(tslSchemeInfo.getTSLType());
        }

        if (tslSchemeInfo.getStatusDeterminationApproach() != null) {
            setStatusDetermination(tslSchemeInfo.getStatusDeterminationApproach());
        }

        if (tslSchemeInfo.getTSLVersionIdentifier() != null) {
            setTlIdentifier(tslSchemeInfo.getTSLVersionIdentifier().intValue());
        }

        List<TLName> schemeNames = new ArrayList<>();
        int i = 0;
        if (tslSchemeInfo.getSchemeName() != null) {
            for (MultiLangNormStringTypeV5 name : tslSchemeInfo.getSchemeName().getName()) {
                i++;
                schemeNames.add(new TLName(iddb, getId() + "_" + Tag.SCHEME_NAME + "_" + i, name));
            }
        }
        setSchemeName(schemeNames);

        i = 0;
        List<TLPostalAddress> postalAddresses = new ArrayList<>();
        List<TLElectronicAddress> electronicAddresses = new ArrayList<>();
        if (tslSchemeInfo.getSchemeOperatorAddress() != null) {
            if ((tslSchemeInfo.getSchemeOperatorAddress().getPostalAddresses() != null) && (tslSchemeInfo.getSchemeOperatorAddress().getPostalAddresses().getPostalAddress() != null)) {
                for (PostalAddressTypeV5 postAdr : tslSchemeInfo.getSchemeOperatorAddress().getPostalAddresses().getPostalAddress()) {
                    i++;
                    postalAddresses.add(new TLPostalAddress(iddb, getId() + "_" + Tag.POSTAL_ADDRESSES + "_" + i, postAdr));
                }
            }
            i = 0;
            if ((tslSchemeInfo.getSchemeOperatorAddress().getElectronicAddress() != null) && (tslSchemeInfo.getSchemeOperatorAddress().getElectronicAddress().getURI() != null)) {
                for (NonEmptyMultiLangURITypeV5 elecAdt : tslSchemeInfo.getSchemeOperatorAddress().getElectronicAddress().getURI()) {
                    i++;
                    electronicAddresses.add(new TLElectronicAddress(iddb, getId() + "_" + Tag.ELECTRONIC_ADDRESS + "_" + i, elecAdt));
                }
            }
        }
        setSchemeOpePostal(postalAddresses);
        setSchemeOpeElectronic(electronicAddresses);

        List<TLName> operatorNames = new ArrayList<>();
        i = 0;
        if (tslSchemeInfo.getSchemeOperatorName() != null) {
            for (MultiLangNormStringTypeV5 name : tslSchemeInfo.getSchemeOperatorName().getName()) {
                i++;
                operatorNames.add(new TLName(iddb, getId() + "_" + Tag.SCHEME_OPERATOR_NAME + "_" + i, name));
            }
        }
        setSchemeOpeName(operatorNames);

        List<TLInformationUri> infoUris = new ArrayList<>();
        i = 0;
        if (tslSchemeInfo.getSchemeInformationURI() != null) {
            for (NonEmptyMultiLangURITypeV5 uri : tslSchemeInfo.getSchemeInformationURI().getURI()) {
                i++;
                infoUris.add(new TLInformationUri(iddb, getId() + "_" + Tag.SCHEME_INFORMATION_URI + "_" + i, uri));
            }
        }
        setSchemeInfoUri(infoUris);

        List<TLSchemeTypeCommunityRule> schemeTypeComm = new ArrayList<>();
        i = 0;
        if (tslSchemeInfo.getSchemeTypeCommunityRules() != null) {
            for (NonEmptyMultiLangURITypeV5 uri : tslSchemeInfo.getSchemeTypeCommunityRules().getURI()) {
                i++;
                schemeTypeComm.add(new TLSchemeTypeCommunityRule(iddb, getId() + "_" + Tag.SCHEME_TYPE_COMMUNITY_RULES + "_" + i, uri));
            }
        }
        setSchemeTypeCommRule(schemeTypeComm);

        List<TLSchemePolicy> policyOrLegalNotices = new ArrayList<>();
        i = 0;
        if (tslSchemeInfo.getPolicyOrLegalNotice() != null) {
            for (NonEmptyMultiLangURITypeV5 uri : tslSchemeInfo.getPolicyOrLegalNotice().getTSLPolicy()) {
                i++;
                policyOrLegalNotices.add(new TLSchemePolicy(iddb, getId() + "_" + Tag.POLICY_OR_LEGAL_NOTICE + "_" + i, uri));
            }
            for (MultiLangStringTypeV5 legal : tslSchemeInfo.getPolicyOrLegalNotice().getTSLLegalNotice()) {
                i++;
                policyOrLegalNotices.add(new TLSchemePolicy(iddb, getId() + "_" + Tag.POLICY_OR_LEGAL_NOTICE + "_" + i, legal));
            }
        }
        setSchemePolicy(policyOrLegalNotices);

        i = 0;
        List<TLDistributionPoint> distributionPoints = new ArrayList<>();
        if (tslSchemeInfo.getDistributionPoints() != null) {
            for (String distribution : tslSchemeInfo.getDistributionPoints().getURI()) {
                i++;
                distributionPoints.add(new TLDistributionPoint(iddb, getId() + "_" + Tag.DISTRIBUTION_LIST + "_" + i, distribution));
            }
        }
        setDistributionPoint(distributionPoints);

    }

    public TSLSchemeInformationTypeV5 asTSLTypeV5() {
        LOGGER.debug("asTSLTypeV5");
        TSLSchemeInformationTypeV5 tslScheme = new TSLSchemeInformationTypeV5();

        // TL INFO
        tslScheme.setHistoricalInformationPeriod(TLUtils.toBigInteger(getHistoricalPeriod()));
        tslScheme.setTSLSequenceNumber(TLUtils.toBigInteger(getSequenceNumber()));
        tslScheme.setSchemeTerritory(getTerritory());
        tslScheme.setListIssueDateTime(TLUtils.toXMLGregorianDate(getIssueDate()));
        NextUpdateTypeV5 nut = new NextUpdateTypeV5();
        nut.setDateTime(TLUtils.toXMLGregorianDate(getNextUpdateDate()));
        tslScheme.setNextUpdate(nut);
        tslScheme.setTSLType(getType());
        tslScheme.setStatusDeterminationApproach(getStatusDetermination());
        tslScheme.setTSLVersionIdentifier(TLUtils.toBigInteger(getTlIdentifier()));

        // SCHEME NAME
        if (getSchemeName() != null) {
            InternationalNamesTypeV5 intSchemeName = new InternationalNamesTypeV5();
            for (TLName tlName : getSchemeName()) {
                intSchemeName.getName().add(tlName.asTSLTypeV5());
            }
            tslScheme.setSchemeName(intSchemeName);
        }

        // SCHEME OPERATOR ADDRESS (POSTAL AND ELECTRONIC)
        AddressTypeV5 adrType = new AddressTypeV5();
        if (getSchemeOpeElectronic() != null) {
            ElectronicAddressTypeV5 electronicAdr = new ElectronicAddressTypeV5();
            for (TLElectronicAddress tlElec : getSchemeOpeElectronic()) {
                electronicAdr.getURI().add(tlElec.asTSLTypeV5());
            }
            adrType.setElectronicAddress(electronicAdr);
        }

        if (getSchemeOpePostal() != null) {
            PostalAddressListTypeV5 postalAdrType = new PostalAddressListTypeV5();
            for (TLPostalAddress tlPostal : getSchemeOpePostal()) {
                postalAdrType.getPostalAddress().add(tlPostal.asTSLTypeV5());
            }
            adrType.setPostalAddresses(postalAdrType);
        }
        tslScheme.setSchemeOperatorAddress(adrType);

        // SCHEME OPERATOR NAME
        if (getSchemeOpeName() != null) {
            InternationalNamesTypeV5 intSchemeOperatorName = new InternationalNamesTypeV5();
            for (TLName tlName : getSchemeOpeName()) {
                intSchemeOperatorName.getName().add(tlName.asTSLTypeV5());
            }
            tslScheme.setSchemeOperatorName(intSchemeOperatorName);
        }

        // SCHEME INFORMATION URI
        if (getSchemeInfoUri() != null) {
            NonEmptyMultiLangURIListTypeV5 langUriList = new NonEmptyMultiLangURIListTypeV5();
            for (TLInformationUri tlUri : getSchemeInfoUri()) {
                langUriList.getURI().add(tlUri.asTSLTypeV5());
            }
            tslScheme.setSchemeInformationURI(langUriList);
        }

        // SCHEME TYPE COMMUNITY RULE
        if (getSchemeTypeCommRule() != null) {
            NonEmptyMultiLangURIListTypeV5 typeCommunity = new NonEmptyMultiLangURIListTypeV5();
            for (TLSchemeTypeCommunityRule tlTypeComm : getSchemeTypeCommRule()) {
                typeCommunity.getURI().add(tlTypeComm.asTSLTypeV5());
            }
            tslScheme.setSchemeTypeCommunityRules(typeCommunity);
        }

        // POLICY OR LEGAL NOTICE
        if (getSchemePolicy() != null) {
            PolicyOrLegalnoticeTypeV5 policyOrLegal = new PolicyOrLegalnoticeTypeV5();
            for (TLSchemePolicy tlPolicy : getSchemePolicy()) {
                policyOrLegal.getTSLLegalNotice().add(tlPolicy.asTSLTypeV5());
            }
            tslScheme.setPolicyOrLegalNotice(policyOrLegal);
        }

        // DISTRIBUTION POINT
        if (CollectionUtils.isNotEmpty(getDistributionPoint())) {
            NonEmptyURIListTypeV5 distributionList = new NonEmptyURIListTypeV5();
            for (TLDistributionPoint tlDistri : getDistributionPoint()) {
                distributionList.getURI().add(tlDistri.getValue());
            }
            tslScheme.setDistributionPoints(distributionList);
        }

        return tslScheme;
    }

    public void toV5FromV4() {
        LOGGER.debug("toV5FromV4");

        setTlIdentifier(5);

        // New ‘EN_name_value’ as per CID 2015/1505/EU Annex I
        if (getSchemeName() != null) {
            for (TLName tlName : getSchemeName()) {
                if (tlName.getLanguage().equalsIgnoreCase("en")) {
                    tlName.setValue(getTerritory()
                            + ":Trusted list including information related to the qualified trust service providers which are supervised by the issuing Member State, together with information related to the qualified trust services provided by them, in accordance with the relevant provisions laid down in Regulation (EU) No 910/2014 of the European Parliament and of the Council of 23 July 2014 on electronic identification and trust services for electronic transactions in the internal market and repealing Directive 1999/93/EC.");
                }
            }
        }

        if (!getTerritory().equalsIgnoreCase("EU")) {

            setStatusDetermination("http://uri.etsi.org/TrstSvc/TrustedList/StatusDetn/EUappropriate");

            // New ‘EN_name_value’ as per CID 2015/1505/EU
            if (getSchemePolicy() != null) {
                // InternationalNamesType intSchemeName = new InternationalNamesType();
                for (TLSchemePolicy tlScheme : getSchemePolicy()) {
                    if (tlScheme.getLanguage().equalsIgnoreCase("en")) {
                        tlScheme.setValue(
                                "The applicable legal framework for the present trusted list is Regulation (EU) No 910/2014 of the European Parliament and of the Council of 23 July 2014 on electronic identification and trust services for electronic transactions in the internal market and repealing Directive 1999/93/EC.");
                    }
                }
            }
        } else {
            if (getSchemePolicy() != null) {
                // InternationalNamesType intSchemeName = new InternationalNamesType();
                for (TLSchemePolicy tlScheme : getSchemePolicy()) {
                    if (tlScheme.getLanguage().equalsIgnoreCase("en")) {
                        tlScheme.setValue(
                                "In accordance with Art. 2 (4) of Commission Decision 2009/767/EC of 16 October 2009 setting out measures facilitating the use of procedures by electronic means through the ‘points of single contact’ under Directive 2006/123/EC of the European Parliament and of the Council on services in the internal market, as amended by Commission Decision 2010/425/EU, the present list contains the information notified by Member States pursuant to Art. 2 (3) of Commission Decision 2009/767/EC. The European Commission maintains the present list as an interoperability tool designed to facilitate the practical use of national Trusted Lists. Our goal is to keep this information timely and accurate. If errors are brought to our attention, we will try to correct them. However, the Commission accepts no responsibility or liability whatsoever with regard to the content of national Trusted Lists which lies exclusively with the Member States. It is our goal to minimize disruption caused by technical errors. However, some data or information on our site may have been created or structured in files or formats that are not error-free and we cannot guarantee that our service will not be interrupted or otherwise affected by such problems. The European Commission accepts no responsibility with regard to such problems incurred as a result of using this site or any linked external sites. This disclaimer is not intended to limit the liability of the European Commission in contravention of any requirements laid down in applicable national law nor to exclude its liability for matters which may not be excluded under that law.");
                    }
                }
            }
        }
    }

    public List<TLDifference> asPublishedDiff(TLSchemeInformation publishedTl) {
        List<TLDifference> diffList = new ArrayList<>();
        if ((getSequenceNumber() == null) && (publishedTl.getSequenceNumber() != null)) {
            diffList.add(new TLDifference(getId() + "_" + Tag.SEQUENCE_NUMBER, Integer.toString(publishedTl.getSequenceNumber()), ""));
        } else if (getSequenceNumber().compareTo(publishedTl.getSequenceNumber()) != 0) {
            diffList.add(new TLDifference(getId() + "_" + Tag.SEQUENCE_NUMBER, Integer.toString(publishedTl.getSequenceNumber()), Integer.toString(getSequenceNumber())));
        }

        if ((getIssueDate() == null) && (publishedTl.getIssueDate() != null)) {
            diffList.add(new TLDifference(getId() + "_" + Tag.ISSUE_DATE, TLUtils.toStringFormat(publishedTl.getIssueDate()), ""));
        } else if (!getIssueDate().equals(publishedTl.getIssueDate())) {
            diffList.add(new TLDifference(getId() + "_" + Tag.ISSUE_DATE, TLUtils.toStringFormat(publishedTl.getIssueDate()), TLUtils.toStringFormat(getIssueDate())));
        }

        if ((getNextUpdateDate() == null) && (publishedTl.getNextUpdateDate() != null)) {
            diffList.add(new TLDifference(getId() + "_" + Tag.NEXT_UPDATE, TLUtils.toStringFormat(publishedTl.getNextUpdateDate()), ""));
        } else if (getNextUpdateDate() != null && !getNextUpdateDate().equals(publishedTl.getNextUpdateDate())) {
            diffList.add(new TLDifference(getId() + "_" + Tag.NEXT_UPDATE, TLUtils.toStringFormat(publishedTl.getNextUpdateDate()), TLUtils.toStringFormat(getNextUpdateDate())));
        }

        if ((getHistoricalPeriod() == null) && (publishedTl.getHistoricalPeriod() != null)) {
            diffList.add(new TLDifference(getId() + "_" + Tag.HISTORICAL_PERIOD, Integer.toString(publishedTl.getHistoricalPeriod()), ""));
        } else if (getHistoricalPeriod().compareTo(publishedTl.getHistoricalPeriod()) != 0) {
            diffList.add(new TLDifference(getId() + "_" + Tag.HISTORICAL_PERIOD, Integer.toString(publishedTl.getHistoricalPeriod()), Integer.toString(getHistoricalPeriod())));
        }

        if ((getStatusDetermination() == null) && (publishedTl.getStatusDetermination() != null)) {
            diffList.add(new TLDifference(getId() + "_" + Tag.STATUS_DETERMINATION, publishedTl.getStatusDetermination(), ""));
        } else if (!getStatusDetermination().equalsIgnoreCase(publishedTl.getStatusDetermination())) {
            diffList.add(new TLDifference(getId() + "_" + Tag.STATUS_DETERMINATION, publishedTl.getStatusDetermination(), getStatusDetermination()));
        }

        if ((getTerritory() == null) && (publishedTl.getTerritory() != null)) {
            diffList.add(new TLDifference(getId() + "_" + Tag.TERRITORY, publishedTl.getTerritory(), ""));
        } else if (!getTerritory().equalsIgnoreCase(publishedTl.getTerritory())) {
            diffList.add(new TLDifference(getId() + "_" + Tag.TERRITORY, publishedTl.getTerritory(), getTerritory()));
        }

        if ((getType() == null) && (publishedTl.getType() != null)) {
            diffList.add(new TLDifference(getId() + "_" + Tag.TSL_TYPE, publishedTl.getType(), ""));
        } else if (!getType().equalsIgnoreCase(publishedTl.getType())) {
            diffList.add(new TLDifference(getId() + "_" + Tag.TSL_TYPE, publishedTl.getType(), getType()));
        }

        if ((getTlIdentifier() == null) && (publishedTl.getTlIdentifier() != null)) {
            diffList.add(new TLDifference(getId() + "_" + Tag.VERSION_IDENTIFER, Integer.toString(publishedTl.getTlIdentifier()), ""));
        } else if (getTlIdentifier().compareTo(publishedTl.getTlIdentifier()) != 0) {
            diffList.add(new TLDifference(getId() + "_" + Tag.VERSION_IDENTIFER, Integer.toString(publishedTl.getTlIdentifier()), Integer.toString(getTlIdentifier())));
        }

        // LIST
        // DISTRIBUTION POINT
        if (CollectionUtils.isNotEmpty(getDistributionPoint())) {
            List<TLDifference> changeList = ChangeUtils.diffOfDistributionPoint(getDistributionPoint(), publishedTl.getDistributionPoint(), getId() + "_" + Tag.DISTRIBUTION_LIST);
            if (changeList.size() > 0) {
                diffList.addAll(changeList);
            }
        } else {
            if ((publishedTl.getDistributionPoint() != null) && CollectionUtils.isNotEmpty(publishedTl.getDistributionPoint())) {
                diffList.addAll(ChangeUtils.initEmptyCurrentListDifference(publishedTl.getDistributionPoint(), getId() + '_' + Tag.DISTRIBUTION_LIST));
            }
        }

        // SCHEME INFORMATION URI
        if (CollectionUtils.isNotEmpty(getSchemeInfoUri())) {
            List<TLDifference> changeList = ChangeUtils.diffOfTLInfoUriList(getSchemeInfoUri(), publishedTl.getSchemeInfoUri(), getId() + "_" + Tag.SCHEME_INFORMATION_URI);
            if (changeList.size() > 0) {
                diffList.addAll(changeList);
            }
        } else {
            if ((publishedTl.getSchemeInfoUri() != null) && CollectionUtils.isNotEmpty(publishedTl.getSchemeInfoUri())) {
                diffList.addAll(ChangeUtils.initEmptyCurrentListDifference(publishedTl.getSchemeInfoUri(), getId() + '_' + Tag.SCHEME_INFORMATION_URI));
            }
        }

        // SCHEME NAME
        if (CollectionUtils.isNotEmpty(getSchemeName())) {
            List<TLDifference> changeList = ChangeUtils.diffOfTLNameList(getSchemeName(), publishedTl.getSchemeName(), getId() + "_" + Tag.SCHEME_NAME);
            if (changeList.size() > 0) {
                diffList.addAll(changeList);
            }
        } else {
            if ((publishedTl.getSchemeName() != null) && CollectionUtils.isNotEmpty(publishedTl.getSchemeName())) {
                diffList.addAll(ChangeUtils.initEmptyCurrentListDifference(publishedTl.getSchemeName(), getId() + '_' + Tag.SCHEME_NAME));
            }
        }

        // SCHEME OPE NAME
        if (CollectionUtils.isNotEmpty(getSchemeOpeName())) {
            List<TLDifference> changeList = ChangeUtils.diffOfTLNameList(getSchemeOpeName(), publishedTl.getSchemeOpeName(), getId() + "_" + Tag.SCHEME_OPERATOR_NAME);
            if (changeList.size() > 0) {
                diffList.addAll(changeList);
            }
        } else {
            if ((publishedTl.getSchemeOpeName() != null) && CollectionUtils.isNotEmpty(publishedTl.getSchemeOpeName())) {
                diffList.addAll(ChangeUtils.initEmptyCurrentListDifference(publishedTl.getSchemeOpeName(), getId() + '_' + Tag.SCHEME_OPERATOR_NAME));
            }
        }

        // SCHEME OPE ELECTRONIC
        if (CollectionUtils.isNotEmpty(getSchemeOpeElectronic())) {
            List<TLDifference> changeList = ChangeUtils.diffOfElectronic(getSchemeOpeElectronic(), publishedTl.getSchemeOpeElectronic(), getId() + "_" + Tag.ELECTRONIC_ADDRESS);
            if (changeList.size() > 0) {
                diffList.addAll(changeList);
            }
        } else {
            if ((publishedTl.getSchemeOpeElectronic() != null) && CollectionUtils.isNotEmpty(publishedTl.getSchemeOpeElectronic())) {
                diffList.addAll(ChangeUtils.initEmptyCurrentListDifference(publishedTl.getSchemeOpeElectronic(), getId() + '_' + Tag.ELECTRONIC_ADDRESS));
            }
        }

        // SCHEME OPE POSTAL
        if (CollectionUtils.isNotEmpty(getSchemeOpePostal())) {
            List<TLDifference> changeList = ChangeUtils.diffOfPostalList(getSchemeOpePostal(), publishedTl.getSchemeOpePostal(), getId() + "_" + Tag.POSTAL_ADDRESSES);
            if (changeList.size() > 0) {
                diffList.addAll(changeList);
            }
        } else {
            if ((publishedTl.getSchemeOpePostal() != null) && CollectionUtils.isNotEmpty(publishedTl.getSchemeOpePostal())) {
                diffList.addAll(ChangeUtils.initEmptyCurrentPostalAddressDifference(publishedTl.getSchemeOpePostal(), getId() + '_' + Tag.POSTAL_ADDRESSES));
            }
        }

        // SCHEME POLICY
        if (CollectionUtils.isNotEmpty(getSchemePolicy())) {
            List<TLDifference> changeList = ChangeUtils.diffOfTLPolicyList(getSchemePolicy(), publishedTl.getSchemePolicy(), getId() + "_" + Tag.POLICY_OR_LEGAL_NOTICE);
            if (changeList.size() > 0) {
                diffList.addAll(changeList);
            }
        } else {
            if ((publishedTl.getSchemePolicy() != null) && CollectionUtils.isNotEmpty(publishedTl.getSchemePolicy())) {
                diffList.addAll(ChangeUtils.initEmptyCurrentListDifference(publishedTl.getSchemePolicy(), getId() + '_' + Tag.POLICY_OR_LEGAL_NOTICE));
            }
        }

        // SCHEME TYPE COMMUNITY RULE
        if (CollectionUtils.isNotEmpty(getSchemeTypeCommRule())) {
            List<TLDifference> changeList = ChangeUtils.diffOfTLTypeCommunityList(getSchemeTypeCommRule(), publishedTl.getSchemeTypeCommRule(), getId() + "_" + Tag.SCHEME_TYPE_COMMUNITY_RULES);
            if (changeList.size() > 0) {
                diffList.addAll(changeList);
            }
        } else {
            if ((publishedTl.getSchemeTypeCommRule() != null) && CollectionUtils.isNotEmpty(publishedTl.getSchemeTypeCommRule())) {
                diffList.addAll(ChangeUtils.initEmptyCurrentListDifference(publishedTl.getSchemeTypeCommRule(), getId() + '_' + Tag.SCHEME_TYPE_COMMUNITY_RULES));
            }
        }

        LOGGER.debug("Number of Scheme Information [published] diff : " + diffList.size());
        return diffList;
    }

    /*
     * GETTERS AND SETTERS
     */
    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getTerritory() {
        return territory;
    }

    public void setTerritory(String territory) {
        this.territory = territory;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getNextUpdateDate() {
        return nextUpdateDate;
    }

    public void setNextUpdateDate(Date nextUpdateDate) {
        this.nextUpdateDate = nextUpdateDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatusDetermination() {
        return statusDetermination;
    }

    public void setStatusDetermination(String statusDetermination) {
        this.statusDetermination = statusDetermination;
    }

    public Integer getTlIdentifier() {
        return tlIdentifier;
    }

    public void setTlIdentifier(Integer tlIdentifier) {
        this.tlIdentifier = tlIdentifier;
    }

    public List<TLName> getSchemeOpeName() {
        return schemeOpeName;
    }

    public void setSchemeOpeName(List<TLName> schemeOpeName) {
        this.schemeOpeName = schemeOpeName;
    }

    public List<TLPostalAddress> getSchemeOpePostal() {
        return schemeOpePostal;
    }

    public void setSchemeOpePostal(List<TLPostalAddress> schemeOpePostal) {
        this.schemeOpePostal = schemeOpePostal;
    }

    public List<TLElectronicAddress> getSchemeOpeElectronic() {
        return schemeOpeElectronic;
    }

    public void setSchemeOpeElectronic(List<TLElectronicAddress> schemeOpeElectronic) {
        this.schemeOpeElectronic = schemeOpeElectronic;
    }

    public List<TLName> getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(List<TLName> schemeName) {
        this.schemeName = schemeName;
    }

    public List<TLInformationUri> getSchemeInfoUri() {
        return schemeInfoUri;
    }

    public void setSchemeInfoUri(List<TLInformationUri> schemeInfoUri) {
        this.schemeInfoUri = schemeInfoUri;
    }

    public List<TLSchemeTypeCommunityRule> getSchemeTypeCommRule() {
        return schemeTypeCommRule;
    }

    public void setSchemeTypeCommRule(List<TLSchemeTypeCommunityRule> schemeTypeCommRule) {
        this.schemeTypeCommRule = schemeTypeCommRule;
    }

    public List<TLSchemePolicy> getSchemePolicy() {
        return schemePolicy;
    }

    public void setSchemePolicy(List<TLSchemePolicy> schemePolicy) {
        this.schemePolicy = schemePolicy;
    }

    public List<TLDistributionPoint> getDistributionPoint() {
        return distributionPoint;
    }

    public void setDistributionPoint(List<TLDistributionPoint> distributionPoint) {
        this.distributionPoint = distributionPoint;
    }

    public Integer getHistoricalPeriod() {
        return historicalPeriod;
    }

    public void setHistoricalPeriod(Integer historicalPeriod) {
        this.historicalPeriod = historicalPeriod;
    }

}
