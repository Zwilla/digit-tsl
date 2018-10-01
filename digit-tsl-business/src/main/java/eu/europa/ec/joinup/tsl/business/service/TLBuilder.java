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
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLSchemeInformation;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;
import eu.europa.esig.jaxb.tsl.TSLSchemeInformationType;
import eu.europa.esig.jaxb.tsl.TrustServiceProviderListType;
import eu.europa.esig.jaxb.tsl.TrustStatusListType;
import eu.europa.esig.jaxb.v5.tsl.TSLSchemeInformationTypeV5;
import eu.europa.esig.jaxb.v5.tsl.TrustServiceProviderListTypeV5;
import eu.europa.esig.jaxb.v5.tsl.TrustStatusListTypeV5;

/**
 * TrustStatusListType JaxB object to trusted list DTO builer
 */
@Service
public class TLBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(TLBuilder.class);
    @Autowired
    private TSLExtractor tslExtractor;

    /**
     * Build TL v4
     *
     * @param iddb
     * @param tsl
     */
    public TL buildTLV4(int iddb, TrustStatusListType tsl) {
        LOGGER.debug("build TL V4");
        TLSchemeInformation schemeInformationDTO = null;
        List<TLPointersToOtherTSL> tlPointersDTO = new ArrayList<>();
        List<TLServiceProvider> tlProvidersDTO = new ArrayList<>();

        TSLSchemeInformationType schemeInformation = tsl.getSchemeInformation();
        if (schemeInformation != null) {
            schemeInformationDTO = new TLSchemeInformation(iddb, schemeInformation);
            if (schemeInformation.getPointersToOtherTSL() != null) {
                tlPointersDTO = tslExtractor.getTLPointers(iddb, schemeInformation.getPointersToOtherTSL());
            }
        }

        TrustServiceProviderListType trustServiceProviderList = tsl.getTrustServiceProviderList();
        if (trustServiceProviderList != null) {
            tlProvidersDTO = tslExtractor.getTLProviders(iddb, trustServiceProviderList);
        }

        return new TL(iddb, tsl.getId(), tsl.getTSLTag(), schemeInformationDTO, tlPointersDTO, tlProvidersDTO);
    }

    /**
     * Build TL v5
     *
     * @param iddb
     * @param tsl
     */
    public TL buildTLV5(int iddb, TrustStatusListTypeV5 tsl) {
        LOGGER.debug("build TL V5");
        TLSchemeInformation schemeInformationDTO = null;
        List<TLPointersToOtherTSL> tlPointersDTO = new ArrayList<>();
        List<TLServiceProvider> tlProvidersDTO = new ArrayList<>();

        TSLSchemeInformationTypeV5 schemeInformation = tsl.getSchemeInformation();
        if (schemeInformation != null) {
            schemeInformationDTO = new TLSchemeInformation(iddb, schemeInformation);
            if (schemeInformation.getPointersToOtherTSL() != null) {
                tlPointersDTO = tslExtractor.getTLPointers(iddb, schemeInformation.getPointersToOtherTSL());
            }
        }

        TrustServiceProviderListTypeV5 trustServiceProviderList = tsl.getTrustServiceProviderList();
        if (trustServiceProviderList != null) {
            tlProvidersDTO = tslExtractor.getTLProviders(iddb, trustServiceProviderList);
        }

        return new TL(iddb, tsl.getId(), tsl.getTSLTag(), schemeInformationDTO, tlPointersDTO, tlProvidersDTO);
    }

}
