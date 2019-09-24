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
package eu.europa.ec.joinup.tsl.business.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;
import eu.europa.ec.joinup.tsl.business.util.CertificateTokenUtils;
import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.ec.joinup.tsl.model.enums.Tag;
import eu.europa.esig.dss.x509.CertificateToken;
import eu.europa.esig.jaxb.tsl.AdditionalInformationType;
import eu.europa.esig.jaxb.tsl.DigitalIdentityListType;
import eu.europa.esig.jaxb.tsl.DigitalIdentityType;
import eu.europa.esig.jaxb.tsl.OtherTSLPointerType;
import eu.europa.esig.jaxb.tsl.OtherTSLPointersType;
import eu.europa.esig.jaxb.tsl.TSPType;
import eu.europa.esig.jaxb.tsl.TrustServiceProviderListType;
import eu.europa.esig.jaxb.tsl.TrustStatusListType;
import eu.europa.esig.jaxb.v5.tsl.OtherTSLPointerTypeV5;
import eu.europa.esig.jaxb.v5.tsl.OtherTSLPointersTypeV5;
import eu.europa.esig.jaxb.v5.tsl.TSPTypeV5;
import eu.europa.esig.jaxb.v5.tsl.TrustServiceProviderListTypeV5;

/**
 * Get trusted list DTO from JaxB trusted list object
 */
@Service
public class TSLExtractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TSLExtractor.class);

    public List<TLPointersToOtherTSL> getTLPointers(TrustStatusListType tsl) {
        if ((tsl != null) && (tsl.getSchemeInformation() != null)) {
            return getTLPointers(0, tsl.getSchemeInformation().getPointersToOtherTSL());
        }
        return new ArrayList<>();
    }

    public List<TLPointersToOtherTSL> getTLPointers(int iddb, OtherTSLPointersType pointersToOtherTSL) {
        LOGGER.debug("getTLPointers V4");
        List<TLPointersToOtherTSL> result = new ArrayList<>();
        int i = 0;
        if ((pointersToOtherTSL != null) && CollectionUtils.isNotEmpty(pointersToOtherTSL.getOtherTSLPointer())) {
            for (OtherTSLPointerType otherPointer : pointersToOtherTSL.getOtherTSLPointer()) {
                i++;
                result.add(new TLPointersToOtherTSL(iddb, iddb + "_" + Tag.POINTERS_TO_OTHER_TSL + "_" + i, otherPointer));
            }
        }

        return result;
    }

    public List<TLPointersToOtherTSL> getTLPointers(int iddb, OtherTSLPointersTypeV5 pointersToOtherTSL) {
        LOGGER.debug("getTLPointers V5");
        List<TLPointersToOtherTSL> result = new ArrayList<>();
        int i = 0;
        if ((pointersToOtherTSL != null) && CollectionUtils.isNotEmpty(pointersToOtherTSL.getOtherTSLPointer())) {
            for (OtherTSLPointerTypeV5 otherPointer : pointersToOtherTSL.getOtherTSLPointer()) {
                i++;
                result.add(new TLPointersToOtherTSL(iddb, iddb + "_" + Tag.POINTERS_TO_OTHER_TSL + "_" + i, otherPointer));
            }
        }

        return result;
    }

    public List<TLServiceProvider> getTLProviders(int iddb, TrustServiceProviderListType tslProviders) {
        List<TLServiceProvider> result = new ArrayList<>();
        int i = 0;
        if ((tslProviders != null) && CollectionUtils.isNotEmpty(tslProviders.getTrustServiceProvider())) {
            for (TSPType tslProvider : tslProviders.getTrustServiceProvider()) {
                i++;
                result.add(new TLServiceProvider(iddb, iddb + "_" + Tag.TSP_SERVICE_PROVIDER + "_" + i, tslProvider));
            }
        }

        return result;
    }

    public List<TLServiceProvider> getTLProviders(int iddb, TrustServiceProviderListTypeV5 tslProviders) {
        List<TLServiceProvider> result = new ArrayList<>();
        int i = 0;
        if ((tslProviders != null) && CollectionUtils.isNotEmpty(tslProviders.getTrustServiceProvider())) {
            for (TSPTypeV5 tslProvider : tslProviders.getTrustServiceProvider()) {
                i++;
                result.add(new TLServiceProvider(iddb, iddb + "_" + Tag.TSP_SERVICE_PROVIDER + "_" + i, tslProvider));
            }
        }

        return result;
    }

    public Map<String, List<CertificateToken>> getPotentialSigners(TrustStatusListType tl) {
        Map<String, List<CertificateToken>> result = new HashMap<>();
        if ((tl != null) && (tl.getSchemeInformation() != null) && (tl.getSchemeInformation().getPointersToOtherTSL() != null)) {
            OtherTSLPointersType pointersToOtherTSL = tl.getSchemeInformation().getPointersToOtherTSL();
            for (OtherTSLPointerType tslPointer : pointersToOtherTSL.getOtherTSLPointer()) {
                String country = getSchemeTerritory(tslPointer);
                List<CertificateToken> potentialSigners = getPotentialSigners(tslPointer);
                result.put(country, potentialSigners);
            }
        }
        return result;
    }

    private String getSchemeTerritory(OtherTSLPointerType tslPointer) {
        AdditionalInformationType additionalInformation = tslPointer.getAdditionalInformation();
        Map<String, Object> properties = TLUtils.extractAsMap(additionalInformation.getTextualInformationOrOtherInformation());
        Object object = properties.get("{http://uri.etsi.org/02231/v2#}SchemeTerritory");
        return object.toString();
    }

    private List<CertificateToken> getPotentialSigners(OtherTSLPointerType otherTSLPointerType) {
        List<CertificateToken> list = new ArrayList<>();
        if (otherTSLPointerType.getServiceDigitalIdentities() != null) {
            List<DigitalIdentityListType> serviceDigitalIdentity = otherTSLPointerType.getServiceDigitalIdentities().getServiceDigitalIdentity();
            extractCertificates(serviceDigitalIdentity, list);
        }
        return list;
    }

    private void extractCertificates(List<DigitalIdentityListType> serviceDigitalIdentity, List<CertificateToken> result) {
        for (DigitalIdentityListType digitalIdentityListType : serviceDigitalIdentity) {
            List<CertificateToken> certificates = extractCertificates(digitalIdentityListType);
            if (CollectionUtils.isNotEmpty(certificates)) {
                result.addAll(certificates);
            }
        }
    }

    private List<CertificateToken> extractCertificates(DigitalIdentityListType digitalIdentityListType) {
        List<CertificateToken> certificates = new ArrayList<>();
        List<DigitalIdentityType> digitalIds = digitalIdentityListType.getDigitalId();
        for (DigitalIdentityType digitalId : digitalIds) {
            if (digitalId.getX509Certificate() != null) {
                try {
                    CertificateToken certificate = CertificateTokenUtils.loadCertificate(digitalId.getX509Certificate());
                    certificates.add(certificate);
                } catch (Exception e) {
                    LOGGER.warn("Unable to load certificate : " + e.getMessage(), e);
                }
            }
        }
        return certificates;
    }

}
