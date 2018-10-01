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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.Load;
import eu.europa.ec.joinup.tsl.business.dto.TLSignature;
import eu.europa.ec.joinup.tsl.business.dto.TLUrls;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLCertificate;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLDigitalIdentification;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.ec.joinup.tsl.model.enums.AuditAction;
import eu.europa.ec.joinup.tsl.model.enums.AuditStatus;
import eu.europa.ec.joinup.tsl.model.enums.AuditTarget;
import eu.europa.ec.joinup.tsl.model.enums.MimeType;
import eu.europa.ec.joinup.tsl.model.enums.SignatureStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;
import eu.europa.esig.dss.x509.CertificateToken;
import eu.europa.esig.dss.x509.KeyStoreCertificateSource;
import eu.europa.esig.jaxb.tsl.TrustStatusListType;

/**
 * Trusted lists loading job services
 */
@Service
public class LoadingJobService {

    @Value("${lotl.territory}")
    private String lotlTerritory;

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadingJobService.class);

    @Autowired
    private TLLoader tlLoader;

    @Autowired
    private TSLExtractor tslExtractor;

    @Autowired
    private TLService tlService;

    @Autowired
    private AbstractAlertingService alertingService;

    @Autowired
    KeyStoreCertificateSource keyStoreCertificateSource;

    @Autowired
    private AuditService auditService;

    @Autowired
    private ApplicationPropertyService applicationPropertyService;

    public void start() {
        LOGGER.debug("**** START ****");
        // LAUNCH RULES AND SIGNATURE VALIDATION IF NEW TL
        Load loadLotl = new Load();

        TrustStatusListType jaxbTl = tlLoader.loadTL(lotlTerritory, applicationPropertyService.getLOTLUrl(), TLType.LOTL, TLStatus.PROD, loadLotl);
        if (loadLotl.isNew()) {
            tlLoader.checkSigLOTL(loadLotl);
            tlLoader.check(loadLotl);
            tlService.setTlCheckStatus(loadLotl.getTlId());
            tlService.updateDraftLOTL(loadLotl.getTlId());
            alertingService.sendNewTLReport(loadLotl.getTlId());
        }
        TLSignature signature = tlService.getSignatureInfo(loadLotl.getTlId());

        // IF LOTL VALID
        if ((signature != null) && signature.getIndication().equals(SignatureStatus.VALID)) {

            tlLoader.updateLOTLCertificates();
            tlLoader.updateLOTLLocation();

            List<TLPointersToOtherTSL> tlPointers = tslExtractor.getTLPointers(jaxbTl);
            if (loadLotl.isNew()) {
                udpateSigningCertificate(tlPointers);
            }
            Map<String, TLUrls> mapByCountry = buildMap(tlPointers);
            for (Map.Entry<String, TLUrls> entry : mapByCountry.entrySet()) {
                Load loadTl = new Load();
                String countryCode = entry.getKey();
                if (!lotlTerritory.equals(countryCode)) {
                    TLUrls urls = entry.getValue();

                    tlLoader.loadTL(countryCode, urls.getXmlUrl(), TLType.TL, TLStatus.PROD, loadTl);

                    if (loadTl.isNew() || loadLotl.isNew()) {
                        tlLoader.check(loadTl);
                        tlLoader.checkSig(loadTl);
                        tlLoader.loadServiceList(loadTl);
                        tlService.setTlCheckStatus(loadTl.getTlId());
                    }

                    if (loadTl.isNew()) {
                        LOGGER.debug("***** loadTl.isNew should be true --> Send email for " + countryCode + " is : " + loadTl.isNew());
                        alertingService.sendNewTLReport(loadTl.getTlId());
                    }
                }
            }
        } else {
            LOGGER.error("*********************");
            if (signature != null) {
                LOGGER.error("********************* LOTL SIGNATURE NOT VALID : " + signature.getIndication());
                LOGGER.error("*****************************" + signature.getSubIndication());
            } else {
                LOGGER.error("********************* LOTL SIGNATURE IS NULL");
            }
            LOGGER.error("*********************");
        }
    }

    private Map<String, TLUrls> buildMap(List<TLPointersToOtherTSL> tlPointers) {
        Map<String, TLUrls> result = new HashMap<>();
        for (TLPointersToOtherTSL pointer : tlPointers) {
            TLUrls tlUrls = result.get(pointer.getSchemeTerritory());
            if (tlUrls == null) {
                tlUrls = new TLUrls();
                result.put(pointer.getSchemeTerritory(), tlUrls);
            }

            String location = StringUtils.trim(pointer.getTlLocation());
            if (MimeType.XML.equals(pointer.getMimeType())) {
                tlUrls.setXmlUrl(location);
                tlUrls.setSha2Url(TLUtils.getSHA2Url(location));
            } else if (MimeType.PDF.equals(pointer.getMimeType())) {
                tlUrls.setPdfUrl(location);
            }
        }
        return result;
    }

    private void udpateSigningCertificate(List<TLPointersToOtherTSL> tlPointers) {
        boolean change = false;
        for (TLPointersToOtherTSL pointer : tlPointers) {
            if (MimeType.XML.equals(pointer.getMimeType()) && lotlTerritory.equals(pointer.getSchemeTerritory())) {
                LOGGER.debug("UPDATE SIGNING CERTIFICATE FOR " + pointer.getId());

                List<CertificateToken> certificatesFromKeyStore = keyStoreCertificateSource.getCertificates();

                List<CertificateToken> certificatesFromPointer = new ArrayList<>();
                for (TLDigitalIdentification digitalId : pointer.getServiceDigitalId()) {
                    for (TLCertificate cert : digitalId.getCertificateList()) {
                        certificatesFromPointer.add(cert.getToken());
                    }
                }

                sortCertificateList(certificatesFromKeyStore);
                sortCertificateList(certificatesFromPointer);

                if (!Objects.deepEquals(certificatesFromKeyStore, certificatesFromPointer)) {
                    // delete of all certificate
                    for (CertificateToken certificateToken : certificatesFromKeyStore) {
                        if (!certificatesFromPointer.contains(certificateToken)) {
                            keyStoreCertificateSource.deleteCertificateFromKeyStore(certificateToken.getDSSIdAsString());
                            auditService.addAuditLog(AuditTarget.ADMINISTRATION_LIST_SIGNING_CERT, AuditAction.DELETE, AuditStatus.SUCCES, "", 0, "SYSTEM",
                                    "CERTTOKEN:" + certificateToken.getDSSIdAsString());
                            change = true;
                        }

                    }
                    // add all certificate from pointer
                    for (CertificateToken certificateToken : certificatesFromPointer) {
                        if (!certificatesFromKeyStore.contains(certificateToken)) {
                            keyStoreCertificateSource.addCertificateToKeyStore(certificateToken);
                            auditService.addAuditLog(AuditTarget.ADMINISTRATION_LIST_SIGNING_CERT, AuditAction.CREATE, AuditStatus.SUCCES, "", 0, "SYSTEM",
                                    "CERTTOKEN:" + certificateToken.getDSSIdAsString());
                            change = true;
                        }
                    }
                }

            }
        }
        if (change) {
            alertingService.sendNewSigningCertificate();
        }

    }

    private void sortCertificateList(List<CertificateToken> list) {
        Collections.sort(list, new Comparator<CertificateToken>() {
            @Override
            public int compare(CertificateToken o1, CertificateToken o2) {
                return o1.getNotAfter().compareTo(o2.getNotAfter());
            }
        });
    }
}
