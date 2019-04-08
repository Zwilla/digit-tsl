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
package eu.europa.ec.joinup.tsl.business.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import eu.europa.ec.joinup.tsl.business.constant.ASIConstant;
import eu.europa.ec.joinup.tsl.business.constant.STIConstant;
import eu.europa.ec.joinup.tsl.business.constant.ServiceLegalType;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceDto;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceExtension;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceHistory;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;

/**
 * QTrust/NonQTrust service types calculator
 */
public class TLQServiceTypeUtils {

    /**
     * Loop through 'Service' of 'Trust Service Provider' and get set of service legal type
     *
     * @param trustServiceProvider
     * @return
     */
    public static Set<String> getTSPTypes(TLServiceProvider trustServiceProvider) {
        Set<String> trustServiceTypes = new HashSet<>();
        if ((trustServiceProvider != null) && (trustServiceProvider.getTSPServices() != null)) {
            for (TLServiceDto service : trustServiceProvider.getTSPServices()) {
                trustServiceTypes.addAll(getServiceTypes(service));
            }
        }
        return trustServiceTypes;
    }

    /**
     * Get service legal type(s)
     *
     * @param service
     */
    public static Set<String> getServiceTypes(TLServiceDto service) {
        if (service == null) {
            return java.util.Collections.emptySet();
        } else {
            return extractTypes(service.getTypeIdentifier(), service.getExtension());
        }
    }

    /**
     * Get history legal type(s)
     *
     * @param service
     */
    public static Set<String> getHistoryTypes(TLServiceHistory history) {
        if (history == null) {
            return java.util.Collections.emptySet();
        } else {
            return extractTypes(history.getTypeIdentifier(), history.getExtension());
        }
    }

    /**
     * Extract legal type based on type identfier and extensions
     *
     * @param typeIdentifier
     * @param extensions
     */
    private static Set<String> extractTypes(String typeIdentifier, List<TLServiceExtension> extensions) {
        Set<String> serviceTypes = new HashSet<>();
        if (!StringUtils.isEmpty(typeIdentifier)) {
            if (STIConstant.qTrustServices.contains(typeIdentifier)) {
                serviceTypes.addAll(getQTrustTypes(typeIdentifier, extensions));
            } else if (STIConstant.qNonTrustServices.contains(typeIdentifier)) {
                serviceTypes.addAll(getNonQTrustTypes(typeIdentifier, extensions));
            } else {
                serviceTypes.add(ServiceLegalType.NON_REGULATORY.getCode());
            }
        }
        if (CollectionUtils.isEmpty(serviceTypes)) {
            serviceTypes.add(ServiceLegalType.UNDEFINED.getCode());
        }
        return serviceTypes;
    }

    /**
     * Get 'Q Trust' service type identified by aSi
     *
     * @param service
     * @return
     */
    private static Set<String> getQTrustTypes(String typeIdentifier, List<TLServiceExtension> extensions) {
        Set<String> qTrustTypes = new HashSet<>();
        switch (typeIdentifier) {
        case STIConstant.caQC:
        case STIConstant.ocspQC:
        case STIConstant.crlQC:
            qTrustTypes.addAll(loopExtension(extensions, ServiceLegalType.UNDEFINED, ServiceLegalType.Q_CERT_ESIG, ServiceLegalType.Q_CERT_ESEAL, ServiceLegalType.Q_WAC));
            break;
        case STIConstant.qesValidationQ:
            qTrustTypes.addAll(loopExtension(extensions, ServiceLegalType.UNDEFINED, ServiceLegalType.Q_VAL_ESIG, ServiceLegalType.Q_VAL_ESEAL, ServiceLegalType.UNDEFINED));
            break;
        case STIConstant.psesQ:
            qTrustTypes.addAll(loopExtension(extensions, ServiceLegalType.UNDEFINED, ServiceLegalType.Q_PRES_ESIG, ServiceLegalType.Q_PRES_ESEAL, ServiceLegalType.UNDEFINED));
            break;
        case STIConstant.tsaQTST:
            qTrustTypes.add(ServiceLegalType.Q_TIMESTAMP.getCode());
            break;
        case STIConstant.edsQ:
        case STIConstant.edsRemQ:
            qTrustTypes.add(ServiceLegalType.Q_ERDS.getCode());
            break;
        default:
            break;
        }
        return qTrustTypes;
    }

    /**
     * Get 'nonQ Trust' service type identified by aSi
     *
     * @param service
     * @return
     */
    private static Set<String> getNonQTrustTypes(String typeIdentifier, List<TLServiceExtension> extensions) {
        Set<String> nonQTrustTypes = new HashSet<>();
        switch (typeIdentifier) {
        case STIConstant.caPKC:
        case STIConstant.ocsp:
        case STIConstant.crl:
            nonQTrustTypes.addAll(loopExtension(extensions, ServiceLegalType.UNDEFINED, ServiceLegalType.CERT_ESIG, ServiceLegalType.CERT_ESEAL, ServiceLegalType.WAC));
            break;
        case STIConstant.adesValidation:
            nonQTrustTypes.addAll(loopExtension(extensions, ServiceLegalType.UNDEFINED, ServiceLegalType.VAL_ESIG, ServiceLegalType.VAL_ESEAL, ServiceLegalType.UNDEFINED));
            break;
        case STIConstant.adesGeneration:
            nonQTrustTypes.addAll(loopExtension(extensions, ServiceLegalType.UNDEFINED, ServiceLegalType.GEN_ESIG, ServiceLegalType.GEN_ESEAL, ServiceLegalType.UNDEFINED));
            break;
        case STIConstant.pses:
            nonQTrustTypes.addAll(loopExtension(extensions, ServiceLegalType.UNDEFINED, ServiceLegalType.PRES_ESIG, ServiceLegalType.PRES_ESEAL, ServiceLegalType.UNDEFINED));
            break;
        case STIConstant.tsa:
        case STIConstant.tsaTssQC:
        case STIConstant.tsaTssAdesQaQES:
            nonQTrustTypes.add(ServiceLegalType.TIMESTAMP.getCode());
            break;
        case STIConstant.eds:
        case STIConstant.edsRem:
            nonQTrustTypes.add(ServiceLegalType.ERDS.getCode());
            break;
        default:
            break;
        }
        return nonQTrustTypes;

    }

    /**
     * Loop through service additionnal service extension and set service type depends on aSi Set undefined value when no aSi detected
     *
     * @param service
     * @param undefinedValue
     * @param eSigValue
     * @param eSealValue
     * @param wsaValue
     * @return
     */
    private static Set<String> loopExtension(List<TLServiceExtension> extensions, ServiceLegalType undefinedValue, ServiceLegalType eSigValue, ServiceLegalType eSealValue, ServiceLegalType wsaValue) {
        Set<String> results = new HashSet<>();
        if (CollectionUtils.isEmpty(extensions)) {
            // Default value
            results.add(undefinedValue.getCode());
        } else {
            // Check through Additionnal Service Extension
            for (TLServiceExtension ext : extensions) {
                if ((ext.getAdditionnalServiceInfo() != null) && (ext.getAdditionnalServiceInfo().getValue() != null)) {
                    switch (ext.getAdditionnalServiceInfo().getValue()) {
                    case ASIConstant.foreSignatures:
                        results.add(eSigValue.getCode());
                        break;
                    case ASIConstant.foreSeals:
                        results.add(eSealValue.getCode());
                        break;
                    case ASIConstant.forWebSiteAuthentication:
                        results.add(wsaValue.getCode());
                        break;
                    default:
                        break;
                    }
                }
            }
        }
        return results;
    }

}
