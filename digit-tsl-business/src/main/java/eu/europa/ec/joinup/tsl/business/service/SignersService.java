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

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import eu.europa.esig.dss.x509.CertificateToken;
import eu.europa.esig.dss.x509.KeyStoreCertificateSource;
import eu.europa.esig.jaxb.tsl.TrustStatusListType;

/**
 * Retrieve potentials authorized signers for a trusted list
 */
@Service
public class SignersService {

    @Autowired
    @Qualifier("lotlKeyStore")
    private KeyStoreCertificateSource lotlKeyStore;

    @Autowired
    private TSLExtractor extractor;

    @Autowired
    private TLService tlService;

    @Autowired
    private CountryService countryService;

    /**
     * This method returns all allowed certificates to sign a TL or a LOTL
     */
    public Map<String, List<CertificateToken>> getAllPotentialsSigners() {
        Map<String, List<CertificateToken>> tlPotentialsSigners = getTLPotentialsSigners();

        List<CertificateToken> lotlPotentialSigners = lotlKeyStore.getCertificates();
        tlPotentialsSigners.put(countryService.getLOTLCountry().getCodeTerritory(), lotlPotentialSigners);

        return tlPotentialsSigners;
    }

    /**
     * This method returns all allowed certificates to sign a TL only
     */
    public Map<String, List<CertificateToken>> getTLPotentialsSigners() {
        TrustStatusListType lotlProductionJaxb = tlService.getLOTLProductionJaxb();
        Map<String, List<CertificateToken>> potentialSigners = extractor.getPotentialSigners(lotlProductionJaxb);
        return potentialSigners;
    }

    public List<CertificateToken> getCertificatesFromKeyStore() {
        return lotlKeyStore.getCertificates();
    }

}
