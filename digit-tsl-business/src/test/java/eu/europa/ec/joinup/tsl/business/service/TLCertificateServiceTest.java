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

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.XmlMappingException;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.data.tl.ServiceDataDTO;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLCertificate;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLDigitalIdentification;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.ec.joinup.tsl.business.dto.tl.approachbreak.CertificateElement;
import eu.europa.ec.joinup.tsl.business.repository.TLCertificateRepository;
import eu.europa.ec.joinup.tsl.business.util.CertificateTokenUtils;
import eu.europa.ec.joinup.tsl.model.DBCertificate;
import eu.europa.ec.joinup.tsl.model.enums.MimeType;
import eu.europa.ec.joinup.tsl.model.enums.TLType;
import eu.europa.esig.dss.validation.SignatureValidationContext;
import eu.europa.esig.dss.x509.CertificateToken;
import eu.europa.esig.jaxb.v5.tsl.TrustStatusListTypeV5;

public class TLCertificateServiceTest extends AbstractSpringTest {

    @Autowired
    private TLCertificateService certificateService;

    @Autowired
    private TLCertificateRepository certificateRepository;

    @Autowired
    private TrustedListJaxbService jaxbService;

    @Autowired
    private TLBuilder tlBuilder;

    @Before
    public void init() throws XmlMappingException, IOException {
        certificateRepository.deleteAll();

        TrustStatusListTypeV5 tsl = jaxbService.unmarshallTSLV5(new File("src/test/resources/TLCC/LOTL.xml"));
        TL tl = tlBuilder.buildTLV5(0, tsl);
        if ((tl != null) && !CollectionUtils.isEmpty(tl.getPointers())) {
            for (TLPointersToOtherTSL pointer : tl.getPointers()) {
                if (pointer.getMimeType().equals(MimeType.XML)) {
                    if (!CollectionUtils.isEmpty(pointer.getServiceDigitalId())) {
                        for (TLDigitalIdentification digit : pointer.getServiceDigitalId()) {
                            if (!CollectionUtils.isEmpty(digit.getCertificateList())) {
                                for (TLCertificate certificate : digit.getCertificateList()) {
                                    certificateService.addCertificateEntry(certificate.getToken(), pointer.getSchemeTerritory(), TLType.LOTL, null);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    @SuppressWarnings("deprecation")
    public void getByCountryCode() {
        List<CertificateElement> certElements = certificateService.getByCountryCode("EU", TLType.LOTL, new Date(117, 6, 6, 12, 00));
        Assert.assertNotNull(certElements);
        Assert.assertEquals(4, certElements.size());

        certElements = certificateService.getByCountryCodeBeforeDate("EU", new Date(117, 6, 6, 12, 00), TLType.LOTL);
        Assert.assertNotNull(certElements);
        Assert.assertEquals(4, certElements.size());
        CertificateElement cert = certElements.get(0);
        Assert.assertEquals("CN=EC_DIGIT, OU=Informatics, O=European Commission, L=Etterbeek, ST=Brussel, C=BE", cert.getSubjectName());
        Assert.assertTrue(cert.isExpired());
        Assert.assertEquals(-248, cert.getExpirationIn());
        // Not before 01/01/2015
        certElements = certificateService.getByCountryCodeBeforeDate("EU", new Date(115, 0, 0, 12, 00), TLType.LOTL);
        Assert.assertEquals(3, certElements.size());

        // Not before 01/01/2011
        certElements = certificateService.getByCountryCodeBeforeDate("EU", new Date(111, 0, 0, 12, 00), TLType.LOTL);
        Assert.assertEquals(0, certElements.size());
    }

    @Test
    @SuppressWarnings("deprecation")
    public void getByCountryCodeAndExpirationDate() {
        Date expirationDate = new Date(118, 6, 19, 12, 00);
        List<CertificateElement> certElements = certificateService.getExpiredCertificateByCountryCode("EU", expirationDate, TLType.LOTL, new Date());
        Assert.assertNotNull(certElements);
        Assert.assertEquals(1, certElements.size());
    }

    @Test
    @SuppressWarnings("deprecation")
    public void deleteAll() {
        List<DBCertificate> dbCerts = certificateRepository.findAll();
        Assert.assertEquals(84, dbCerts.size());

        List<CertificateElement> certElements = certificateService.getByCountryCodeBeforeDate("EU", new Date(117, 6, 6, 12, 00), TLType.LOTL);
        Assert.assertEquals(4, certElements.size());
        certificateService.deleteByCountryCode("EU", TLType.LOTL);
        certElements = certificateService.getByCountryCodeBeforeDate("EU", new Date(117, 6, 6, 12, 00), TLType.LOTL);
        Assert.assertEquals(0, certElements.size());

        dbCerts = certificateRepository.findAll();
        Assert.assertEquals(80, dbCerts.size());
        certificateService.deleteLOTL();
        dbCerts = certificateRepository.findAll();
        Assert.assertEquals(0, dbCerts.size());
    }

    @Test
    public void getRootCA() {
        loadAllTLs("");

        String[] goodCerts = { "4B03182BF35832FCAB11FA59E9BA4E8F604C2C779A57D01F72B3F28D7F9F17BE.cer", "4B10C09FFE7405D7EB230D24165B5F4AF598EE32B149C7404CF42F7E64FF28F7.cer",
                "4BA917B3C7CD7AABC7842681E215E199CC5F49A36FE6A74D4930E61A647FCC48.cer", "4D93083C3477AE9E972640779CCCD6BF7F35DA47681FAC267ACA8E13BC5448BB.cer",
                "5AA887CC7082CE5107FD737A00D1D3E0D99A30A3F9CF4F2AF22BB1A1D1DDBECC.cer", "5E2FDC8BDFC7F3B1DF8B34EA0B10E7550F90F894219AB43C35F47F24A645D7AD.cer",
                "5F7DB6AA051CA3384FCE329513E5474706A2B2C5FA2EDCAB1FF3984CEEC292D7.cer" };

        String[] badCerts = { "4A8B421D423E8436041B6A3B9678A27EBFA084CE06494828CA55416B490BF4CC.cer", "4E3133D82C7E4B669C1E40D3F56784BB1BDB124AE15F0956E8AB9B3149DDF1B3.cer",
                "5D05516D8D31D0866A679D9B5ACF9FCA566F581A0098317D45E404BEFF159703.cer", "7C37121A7E63DCE1F808523CC3A70AF9428A5C973FFAF773E615A526C9E363A7.cer" };

        File dir = new File("src/test/resources/certs");
        File[] directoryListing = dir.listFiles();

        CertificateToken ca;
        CertificateToken rootCA;

        if (directoryListing != null) {
            for (File file : directoryListing) {
                ca = CertificateTokenUtils.loadCertificate(file);
                SignatureValidationContext svc = certificateService.initSVC(ca);
                final Set<CertificateToken> rootCAs = certificateService.getRootCertificate(ca, svc);
                if (CollectionUtils.isEmpty(rootCAs)) {
                    rootCA = null;
                } else {
                    rootCA = rootCAs.iterator().next();
                }

                if (rootCA == null) {
                    // Bad cert
                    ArrayUtils.contains(badCerts, file.getName());

                } else {
                    // Good cert
                    ArrayUtils.contains(goodCerts, file.getName());
                    Set<ServiceDataDTO> rootServices = certificateService.getServicesByCertificate(ca, svc);
                    Assert.assertTrue(!rootServices.isEmpty());
                    if (file.getName().equals("5E2FDC8BDFC7F3B1DF8B34EA0B10E7550F90F894219AB43C35F47F24A645D7AD.cer")) {
                        // 3 Services use the same root CA
                        Assert.assertEquals(3, rootServices.size());
                    } else if (file.getName().equals("5AA887CC7082CE5107FD737A00D1D3E0D99A30A3F9CF4F2AF22BB1A1D1DDBECC.cer")
                            || file.getName().equals("5F7DB6AA051CA3384FCE329513E5474706A2B2C5FA2EDCAB1FF3984CEEC292D7.cer")) {
                        Assert.assertEquals(2, rootServices.size());
                    } else {
                        Assert.assertEquals(1, rootServices.size());
                    }
                }
            }
        }

    }

    @Test
    public void validatePKCS7() {
        File f = new File("src/test/resources/signed-files/invalid-cert.pdf");
        Set<CertificateToken> signingCertificates = certificateService.retrieveSigningCertificatesFromFile(f);
        Assert.assertNotNull(signingCertificates);
        Assert.assertEquals(1, signingCertificates.size());
        // CommonCertificateVerifier null during retrieve certificate
        CertificateToken token = signingCertificates.iterator().next();
        SignatureValidationContext svc = certificateService.initSVC(token);
        Assert.assertTrue(certificateService.getServicesByCertificate(token, svc).isEmpty());

        loadAllTLs("");
        signingCertificates = certificateService.retrieveSigningCertificatesFromFile(f);
        token = signingCertificates.iterator().next();
        svc = certificateService.initSVC(token);
        Set<ServiceDataDTO> servicesByRootCA = certificateService.getServicesByCertificate(token, svc);
        Assert.assertFalse(servicesByRootCA.isEmpty());
        ServiceDataDTO sd = servicesByRootCA.iterator().next();
        Assert.assertNotNull(sd);
        Assert.assertEquals("29_TSP_SERVICE_PROVIDER_4_TSP_SERVICE_1", sd.getServiceId());
    }

    @Test
    public void getHRCert() {
        loadAllTLs("");

        Set<CertificateToken> retrieveSigningCertificatesFromFile = certificateService.retrieveSigningCertificatesFromFile(new File("src/test/resources/signed-files/HR.xml"));
        for (CertificateToken certificateToken : retrieveSigningCertificatesFromFile) {
            SignatureValidationContext svc = certificateService.initSVC(certificateToken);
            CertificateToken rootCertificate = certificateService.getRootCertificate(certificateToken, svc).iterator().next();
            Assert.assertNotNull(rootCertificate);
            svc = certificateService.initSVC(rootCertificate);
            Set<ServiceDataDTO> servicesByRootCA = certificateService.getServicesByCertificate(rootCertificate, svc);
            Assert.assertFalse(servicesByRootCA.isEmpty());
            Assert.assertEquals(2, servicesByRootCA.size());
        }
    }

    @Test
    public void retrieveCertFromSignedFile() {
        File f;

        // BE.xml TL
        f = new File("src/test/resources/signed-files/BE.xml");
        Set<CertificateToken> signingCertificates = certificateService.retrieveSigningCertificatesFromFile(f);
        Assert.assertNotNull(signingCertificates);
        Assert.assertEquals(1, signingCertificates.size());

        // Multi-signature file
        f = new File("src/test/resources/signed-files/multiple-signature.pdf");
        signingCertificates = certificateService.retrieveSigningCertificatesFromFile(f);
        Assert.assertNotNull(signingCertificates);
        Assert.assertEquals(5, signingCertificates.size());

    }

}
