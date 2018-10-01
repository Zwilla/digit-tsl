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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import eu.europa.ec.joinup.tsl.business.dto.TLDifference;
import eu.europa.ec.joinup.tsl.business.util.ChangeUtils;
import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.ec.joinup.tsl.model.enums.MimeType;
import eu.europa.ec.joinup.tsl.model.enums.Tag;
import eu.europa.esig.jaxb.tsl.DigitalIdentityListType;
import eu.europa.esig.jaxb.tsl.InternationalNamesType;
import eu.europa.esig.jaxb.tsl.MultiLangNormStringType;
import eu.europa.esig.jaxb.tsl.NonEmptyMultiLangURIListType;
import eu.europa.esig.jaxb.tsl.NonEmptyMultiLangURIType;
import eu.europa.esig.jaxb.tsl.OtherTSLPointerType;
import eu.europa.esig.jaxb.v5.tsl.AdditionalInformationTypeV5;
import eu.europa.esig.jaxb.v5.tsl.AnyTypeV5;
import eu.europa.esig.jaxb.v5.tsl.DigitalIdentityListTypeV5;
import eu.europa.esig.jaxb.v5.tsl.InternationalNamesTypeV5;
import eu.europa.esig.jaxb.v5.tsl.MultiLangNormStringTypeV5;
import eu.europa.esig.jaxb.v5.tsl.NonEmptyMultiLangURIListTypeV5;
import eu.europa.esig.jaxb.v5.tsl.NonEmptyMultiLangURITypeV5;
import eu.europa.esig.jaxb.v5.tsl.OtherTSLPointerTypeV5;
import eu.europa.esig.jaxb.v5.tsl.ServiceDigitalIdentityListTypeV5;

public class TLPointersToOtherTSL extends AbstractTLDTO {

    private List<TLDigitalIdentification> serviceDigitalIdentification;
    private String tlLocation;
    private MimeType mimeType;
    private String tlType;
    private String schemeTerritory;
    private List<TLName> schemeOpeName;
    private List<TLSchemeTypeCommunityRule> schemeTypeCommunity;

    public TLPointersToOtherTSL() {
    }

    public TLPointersToOtherTSL(int iddb, String location, OtherTSLPointerType tslPointerType) {
        super(iddb, location);

        List<Serializable> textualInformationOrOtherInformation = tslPointerType.getAdditionalInformation().getTextualInformationOrOtherInformation();
        Map<String, Object> properties = TLUtils.extractAsMap(textualInformationOrOtherInformation);

        Object mm = properties.get("{http://uri.etsi.org/02231/v2/additionaltypes#}MimeType");
        if (mm instanceof String) {
            setMimeType(TLUtils.convert((String) mm));
        }

        setTlLocation(tslPointerType.getTSLLocation());
        Object st = properties.get("{http://uri.etsi.org/02231/v2#}SchemeTerritory");

        setSchemeTerritory(st.toString());
        Object tlt = properties.get("{http://uri.etsi.org/02231/v2#}TSLType");
        if(tlt==null){
            setTlType("");
        }else{
            setTlType(tlt.toString());
        }

        List<TLSchemeTypeCommunityRule> schemeTypeCommunity = new ArrayList<>();
        Object cr = properties.get("{http://uri.etsi.org/02231/v2#}SchemeTypeCommunityRules");
        NonEmptyMultiLangURIListType communityTsl = (NonEmptyMultiLangURIListType) cr;
        int i = 0;
        for (NonEmptyMultiLangURIType uriType : communityTsl.getURI()) {
            i++;
            schemeTypeCommunity.add(new TLSchemeTypeCommunityRule(getTlId(), getId() + "_" + Tag.POINTER_COMMUNITY_RULE + "_" + i, uriType));
        }
        setSchemeTypeCommunity(schemeTypeCommunity);

        List<TLName> schemeOpeName = new ArrayList<>();
        Object son = properties.get("{http://uri.etsi.org/02231/v2#}SchemeOperatorName");
        InternationalNamesType nameTsl = (InternationalNamesType) son;
        i = 0;
        for (MultiLangNormStringType name : nameTsl.getName()) {
            i++;
            schemeOpeName.add(new TLName(getTlId(), getId() + "_" + Tag.SCHEME_OPERATOR_NAME + "_" + i, name));
        }
        setSchemeOpeName(schemeOpeName);

        // DIGITAL IDENTIFICATION
        List<TLDigitalIdentification> svcDigitalIdentification = new ArrayList<>();
        i = 0;
        for (DigitalIdentityListType jxbPointerDigitalList : tslPointerType.getServiceDigitalIdentities().getServiceDigitalIdentity()) {
            i++;
            TLDigitalIdentification digital = new TLDigitalIdentification(getTlId(), getId() + "_" + Tag.SERVICE_DIGITAL_IDENTITY + "_" + i, jxbPointerDigitalList);
            digital.setSubjectName(null);
            digital.setX509ski(null);
            svcDigitalIdentification.add(digital);
        }
        setServiceDigitalId(svcDigitalIdentification);
    }

    public TLPointersToOtherTSL(int iddb, String location, OtherTSLPointerTypeV5 tslPointerType) {
        super(iddb, location);
        List<Serializable> textualInformationOrOtherInformation = tslPointerType.getAdditionalInformation().getTextualInformationOrOtherInformation();
        Map<String, Object> properties = TLUtils.extractAsMapV5(textualInformationOrOtherInformation);

        Object mm = properties.get("{http://uri.etsi.org/02231/v2/additionaltypes#}MimeType");
        if ((mm instanceof String) && (((String) mm).length() > 1)) {
            setMimeType(TLUtils.convert((String) mm));
        }

        if (tslPointerType.getTSLLocation() != null) {
            setTlLocation(tslPointerType.getTSLLocation());
        }

        Object st = properties.get("{http://uri.etsi.org/02231/v2#}SchemeTerritory");
        if (st != null) {
            setSchemeTerritory(st.toString());
        }

        Object tlt = properties.get("{http://uri.etsi.org/02231/v2#}TSLType");
        if (tlt != null) {
            setTlType(tlt.toString());
        }

        List<TLSchemeTypeCommunityRule> schemeTypeCommunity = new ArrayList<>();
        Object cr = properties.get("{http://uri.etsi.org/02231/v2#}SchemeTypeCommunityRules");
        NonEmptyMultiLangURIListTypeV5 communityTsl = (NonEmptyMultiLangURIListTypeV5) cr;
        int i = 0;
        if (communityTsl != null) {
            for (NonEmptyMultiLangURITypeV5 uriType : communityTsl.getURI()) {
                i++;
                schemeTypeCommunity.add(new TLSchemeTypeCommunityRule(getTlId(), getId() + "_" + Tag.POINTER_COMMUNITY_RULE + "_" + i, uriType));
            }
        }
        setSchemeTypeCommunity(schemeTypeCommunity);

        List<TLName> schemeOpeName = new ArrayList<>();
        Object son = properties.get("{http://uri.etsi.org/02231/v2#}SchemeOperatorName");
        InternationalNamesTypeV5 nameTsl = (InternationalNamesTypeV5) son;
        i = 0;
        if (nameTsl != null) {
            for (MultiLangNormStringTypeV5 name : nameTsl.getName()) {
                i++;
                schemeOpeName.add(new TLName(getTlId(), getId() + "_" + Tag.SCHEME_OPERATOR_NAME + "_" + i, name));
            }
        }
        setSchemeOpeName(schemeOpeName);

        // DIGITAL IDENTIFICATION
        List<TLDigitalIdentification> svcDigitalIdentification = new ArrayList<>();
        i = 0;
        if (tslPointerType != null) {
            for (DigitalIdentityListTypeV5 jxbPointerDigitalList : tslPointerType.getServiceDigitalIdentities().getServiceDigitalIdentity()) {
                i++;
                svcDigitalIdentification.add(new TLDigitalIdentification(getTlId(), getId() + "_" + Tag.SERVICE_DIGITAL_IDENTITY + "_" + i, jxbPointerDigitalList));
            }
        }
        setServiceDigitalId(svcDigitalIdentification);
    }

    public OtherTSLPointerTypeV5 asTSLTypeV5() {
        OtherTSLPointerTypeV5 tslPointers = new OtherTSLPointerTypeV5();
        tslPointers.setTSLLocation(getTlLocation());

        // ADDITIONAL INFORMATION
        AdditionalInformationTypeV5 additional = new AdditionalInformationTypeV5();
        tslPointers.setAdditionalInformation(additional);

        JAXBElement<String> el = new JAXBElement<>(new QName("http://uri.etsi.org/02231/v2#", "TSLType"), String.class, getTlType());
        AnyTypeV5 anyTSLType = new AnyTypeV5();
        anyTSLType.getContent().add(el);
        additional.getTextualInformationOrOtherInformation().add(anyTSLType);

        el = new JAXBElement<>(new QName("http://uri.etsi.org/02231/v2#", "SchemeTerritory"), String.class, getSchemeTerritory());
        AnyTypeV5 anySchemeTerritory = new AnyTypeV5();
        anySchemeTerritory.getContent().add(el);
        additional.getTextualInformationOrOtherInformation().add(anySchemeTerritory);

        el = new JAXBElement<>(new QName("http://uri.etsi.org/02231/v2/additionaltypes#", "MimeType"), String.class, TLUtils.convert(getMimeType()));
        AnyTypeV5 anyMime = new AnyTypeV5();
        anyMime.getContent().add(el);
        additional.getTextualInformationOrOtherInformation().add(anyMime);

        InternationalNamesTypeV5 intSchemeOperatorName = new InternationalNamesTypeV5();
        if (getSchemeOpeName() != null) {
            for (TLName tlName : getSchemeOpeName()) {
                intSchemeOperatorName.getName().add(tlName.asTSLTypeV5());
            }
        }
        JAXBElement<InternationalNamesTypeV5> el2 = new JAXBElement<>(new QName("http://uri.etsi.org/02231/v2#", "SchemeOperatorName"), InternationalNamesTypeV5.class,
                intSchemeOperatorName);
        AnyTypeV5 anySchemeOpe = new AnyTypeV5();
        anySchemeOpe.getContent().add(el2);
        additional.getTextualInformationOrOtherInformation().add(anySchemeOpe);

        NonEmptyMultiLangURIListTypeV5 typeCommunity = new NonEmptyMultiLangURIListTypeV5();
        if (getSchemeTypeCommunity() != null) {
            for (TLSchemeTypeCommunityRule tlTypeComm : getSchemeTypeCommunity()) {
                typeCommunity.getURI().add(tlTypeComm.asTSLTypeV5());
            }
        }
        JAXBElement<NonEmptyMultiLangURIListTypeV5> el3 = new JAXBElement<>(new QName("http://uri.etsi.org/02231/v2#", "SchemeTypeCommunityRules"),
                NonEmptyMultiLangURIListTypeV5.class, typeCommunity);
        AnyTypeV5 anyComm = new AnyTypeV5();
        anyComm.getContent().add(el3);
        additional.getTextualInformationOrOtherInformation().add(anyComm);

        // DIGITAL ID
        ServiceDigitalIdentityListTypeV5 digitalId = new ServiceDigitalIdentityListTypeV5();
        if (getServiceDigitalId() != null) {
            for (TLDigitalIdentification tlDigitalId : getServiceDigitalId()) {
                digitalId.getServiceDigitalIdentity().add(tlDigitalId.asTSLTypeV5());
            }
        }
        tslPointers.setServiceDigitalIdentities(digitalId);

        return tslPointers;
    }

    public List<TLDifference> asPublishedDiff(TLPointersToOtherTSL publishedTl) {
        List<TLDifference> diffList = new ArrayList<>();

        if (mimeType != publishedTl.getMimeType()) {
            diffList.add(new TLDifference(getId() + "_" + Tag.POINTER_MIME_TYPE, publishedTl.getMimeType().toString(), getMimeType().toString()));
        }

        if (!StringUtils.equals(schemeTerritory, publishedTl.getSchemeTerritory())) {
            diffList.add(new TLDifference(getId() + "_" + Tag.POINTER_SCHEME_TERRITORY, publishedTl.getSchemeTerritory(), getSchemeTerritory()));
        }

        if (!StringUtils.equals(tlLocation, publishedTl.getTlLocation())) {
            diffList.add(new TLDifference(getId() + "_" + Tag.POINTER_LOCATION, publishedTl.getTlLocation(), getTlLocation()));
        }

        if (!StringUtils.equals(tlType, publishedTl.getTlType())) {
            diffList.add(new TLDifference(getId() + "_" + Tag.POINTER_TYPE, publishedTl.getTlType(), getTlType()));
        }

        // LIST
        // SCHEME OPE NAME
        if (CollectionUtils.isNotEmpty(schemeOpeName)) {
            List<TLDifference> changeList = ChangeUtils.diffOfTLNameList(schemeOpeName, publishedTl.getSchemeOpeName(), getId() + "_" + Tag.SCHEME_OPERATOR_NAME);
            if (changeList.size() > 0) {
                diffList.addAll(changeList);
            }
        } else {
            if ((publishedTl.getSchemeOpeName() != null) && CollectionUtils.isNotEmpty(publishedTl.getSchemeOpeName())) {
                diffList.addAll(ChangeUtils.initEmptyCurrentListDifference(publishedTl.getSchemeOpeName(), getId() + '_' + Tag.SCHEME_OPERATOR_NAME));
            }
        }

        // SCHEME TYPE COMMUNITY RULE
        if (CollectionUtils.isNotEmpty(schemeTypeCommunity)) {
            List<TLDifference> changeList = ChangeUtils.diffOfTLTypeCommunityList(schemeTypeCommunity, publishedTl.getSchemeTypeCommunity(),
                    getId() + "_" + Tag.POINTER_COMMUNITY_RULE);
            if (changeList.size() > 0) {
                diffList.addAll(changeList);
            }
        } else {
            if ((publishedTl.getSchemeTypeCommunity() != null) && CollectionUtils.isNotEmpty(publishedTl.getSchemeTypeCommunity())) {
                diffList.addAll(ChangeUtils.initEmptyCurrentListDifference(publishedTl.getSchemeTypeCommunity(), getId() + '_' + Tag.POINTER_COMMUNITY_RULE));
            }
        }

        // SERVICE DIGITAL ID
        diffList.addAll(ChangeUtils.diffOfDigitalList(getServiceDigitalId(), publishedTl.getServiceDigitalId(), getId()));

        return diffList;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((mimeType == null) ? 0 : mimeType.hashCode());
        result = (prime * result) + ((schemeOpeName == null) ? 0 : schemeOpeName.hashCode());
        result = (prime * result) + ((schemeTerritory == null) ? 0 : schemeTerritory.hashCode());
        result = (prime * result) + ((schemeTypeCommunity == null) ? 0 : schemeTypeCommunity.hashCode());
        result = (prime * result) + ((serviceDigitalIdentification == null) ? 0 : serviceDigitalIdentification.hashCode());
        result = (prime * result) + ((tlLocation == null) ? 0 : tlLocation.hashCode());
        result = (prime * result) + ((tlType == null) ? 0 : tlType.hashCode());
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
        TLPointersToOtherTSL other = (TLPointersToOtherTSL) obj;
        if (mimeType != other.mimeType) {
            return false;
        }
        if (schemeOpeName == null) {
            if (other.schemeOpeName != null) {
                return false;
            }
        } else if (!schemeOpeName.equals(other.schemeOpeName)) {
            return false;
        }
        if (schemeTerritory == null) {
            if (other.schemeTerritory != null) {
                return false;
            }
        } else if (!schemeTerritory.equals(other.schemeTerritory)) {
            return false;
        }
        if (schemeTypeCommunity == null) {
            if (other.schemeTypeCommunity != null) {
                return false;
            }
        } else if (!schemeTypeCommunity.equals(other.schemeTypeCommunity)) {
            return false;
        }
        if (serviceDigitalIdentification == null) {
            if (other.serviceDigitalIdentification != null) {
                return false;
            }
        } else if (!serviceDigitalIdentification.equals(other.serviceDigitalIdentification)) {
            return false;
        }
        if (tlLocation == null) {
            if (other.tlLocation != null) {
                return false;
            }
        } else if (!tlLocation.equals(other.tlLocation)) {
            return false;
        }
        if (tlType == null) {
            if (other.tlType != null) {
                return false;
            }
        } else if (!tlType.equals(other.tlType)) {
            return false;
        }
        return true;
    }

    /*
     * GETTERS AND SETTERS
     */
    public String getTlType() {
        return tlType;
    }

    public void setTlType(String tlType) {
        this.tlType = tlType;
    }

    public String getTlLocation() {
        return tlLocation;
    }

    public void setTlLocation(String tlLocation) {
        this.tlLocation = tlLocation;
    }

    public List<TLSchemeTypeCommunityRule> getSchemeTypeCommunity() {
        return schemeTypeCommunity;
    }

    public void setSchemeTypeCommunity(List<TLSchemeTypeCommunityRule> schemeTypeCommunity) {
        this.schemeTypeCommunity = schemeTypeCommunity;
    }

    public List<TLDigitalIdentification> getServiceDigitalId() {
        return serviceDigitalIdentification;
    }

    public void setServiceDigitalId(List<TLDigitalIdentification> serviceDigitalId) {
        serviceDigitalIdentification = serviceDigitalId;
    }

    public List<TLName> getSchemeOpeName() {
        return schemeOpeName;
    }

    public void setSchemeOpeName(List<TLName> schemeOpeName) {
        this.schemeOpeName = schemeOpeName;
    }

    public String getSchemeTerritory() {
        return schemeTerritory;
    }

    public void setSchemeTerritory(String schemeTerritory) {
        this.schemeTerritory = schemeTerritory;
    }

    public MimeType getMimeType() {
        return mimeType;
    }

    public void setMimeType(MimeType mimeType) {
        this.mimeType = mimeType;
    }

}
