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

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1String;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DLSequence;
import org.bouncycastle.asn1.DLSet;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.CheckResultDTO;
import eu.europa.ec.joinup.tsl.business.dto.TLSigningCertificateResultDTO;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.esig.dss.DSSASN1Utils;
import eu.europa.esig.dss.DSSRevocationUtils;
import eu.europa.esig.dss.tsl.KeyUsageBit;
import eu.europa.esig.dss.validation.SignatureValidationContext;
import eu.europa.esig.dss.x509.CertificateToken;

@Service
public class CertificateService {

    @Autowired
    private CheckService checkService;

    @Autowired
    private TLCertificateService tlCertificateService;

    @Autowired
    private TLService tlService;

    private static final ResourceBundle bundle = ResourceBundle.getBundle("messages");

    private static final Logger LOGGER = LoggerFactory.getLogger(CertificateService.class);

    private static final String SUBJECT_KEY_IDENTIFIER_OID = "2.5.29.14";

    private static final String ID_TSL_KP_TSLSIGNING_OID = "0.4.0.2231.3.0";

    private static final String CHECK_COUNTRY_ATTRIBUTE = "SignatureRules.SIG_SIGCERT_SN_CC_SCHT";
    private static final String CHECK_ORGANIZATION_ATTRIBUTE = "SignatureRules.SIG_SIGCERT_SN_ORG_SON";
    private static final String CHECK_CERT_EXPIRED = "SignatureRules.SIG_SIGCERT_NOT_EXPIRED";
    private static final String CHECK_ISSUER = "SignatureRules.SIG_SIGCERT_ISSUER";
    private static final String CHECK_SKI_VALUE = "SignatureRules.SIG_SIGCERT_SKI_VALUE";
    private static final String CHECK_BASIC_CONSTRAINTS = "SignatureRules.SIG_SIGCERT_BASCONST_VALUE";
    private static final String CHECK_EXTEND_KU = "SignatureRules.SIG_SIGCERT_EXTKEYUS_VALUE";
    private static final String CHECK_KU = "SignatureRules.SIG_SIGCERT_KEYUS_VALUE";

    public String getCountryCode(CertificateToken certificate) {
        return StringUtils.upperCase(getRDNValue(BCStyle.C, certificate));
    }

    public String getOrganization(CertificateToken certificate) {
        return StringEscapeUtils.unescapeJava(getRDNValue(BCStyle.O, certificate));
    }

    /**
     * Perform verification on TL signing certificate following 119 612.2.1.1 - 5.7.1
     * 
     * @param certificateToken
     * @param schemeTerritory
     * @exception IllegalStateException
     *                trusted list not found for given scheme territory
     */
    public TLSigningCertificateResultDTO checkTLSigningCertificate(CertificateToken certificateToken, String schemeTerritory) {
        TL prodTL = tlService.getPublishedTLByCountryCode(schemeTerritory);
        if (prodTL == null) {
            throw new IllegalStateException(bundle.getString("error.tl.country.not.found").replaceFirst("%CC%", schemeTerritory));
        }
        List<CheckResultDTO> checkResults = new ArrayList<>();
        // Check country attribute match
        if (!countryMatchSchemeTerritory(certificateToken, schemeTerritory)) {
            checkResults.add(new CheckResultDTO("", checkService.getCheckById(CHECK_COUNTRY_ATTRIBUTE)));
        }
        // Check organization attribute match
        if (!organizationMatchOperatorName(certificateToken, new ArrayList<String>(TLUtils.extractEnglishValues(prodTL.getSchemeInformation().getSchemeOpeName())))) {
            checkResults.add(new CheckResultDTO("", checkService.getCheckById(CHECK_ORGANIZATION_ATTRIBUTE)));
        }
        // Check validity
        if (certificateIsExpired(certificateToken, new Date())) {
            checkResults.add(new CheckResultDTO("", checkService.getCheckById(CHECK_CERT_EXPIRED)));
        }
        // Check extendedKeyUsages - tslSigning
        if (!hasTslSigningExtendedKeyUsage(certificateToken)) {
            checkResults.add(new CheckResultDTO("", checkService.getCheckById(CHECK_EXTEND_KU)));
        }
        // Check KeyUsages (digitalSignature and/or nonRepudiation)
        if (!hasAllowedKeyUsagesBits(certificateToken)) {
            checkResults.add(new CheckResultDTO("", checkService.getCheckById(CHECK_KU)));
        }
        // Check basic constraints false
        if (!isBasicConstraintCaFalse(certificateToken)) {
            checkResults.add(new CheckResultDTO("", checkService.getCheckById(CHECK_BASIC_CONSTRAINTS)));
        }
        // Check issuer is self-signed or issuyedBy a TSP from TLs
        if (!isIssuerVerified(certificateToken)) {
            checkResults.add(new CheckResultDTO("", checkService.getCheckById(CHECK_ISSUER)));
        }
        // Check SKI value according to RFC 5280
        if (!isSKIComputeRight(certificateToken)) {
            checkResults.add(new CheckResultDTO("", checkService.getCheckById(CHECK_SKI_VALUE)));
        }

        return new TLSigningCertificateResultDTO(certificateToken.getEncoded(), checkResults);
    }

    private String getRDNValue(ASN1ObjectIdentifier oid, CertificateToken certificate) {
        String value = null;
        if (certificate != null) {
            try {

                X509Certificate x509Certificate = certificate.getCertificate();
                X500Name x500name = new JcaX509CertificateHolder(x509Certificate).getSubject();
                if (x500name != null) {
                    Map<String, Object> pairs = new HashMap<>();

                    DLSequence seq = (DLSequence) DERSequence.fromByteArray(x500name.getEncoded());
                    for (int i = 0; i < seq.size(); i++) {
                        DLSet set = (DLSet) seq.getObjectAt(i);
                        for (int j = 0; j < set.size(); j++) {
                            DLSequence pair = (DLSequence) set.getObjectAt(j);
                            ASN1Encodable objectAt = pair.getObjectAt(1);
                            pairs.put(((ASN1ObjectIdentifier) pair.getObjectAt(0)).getId(), objectAt);
                        }
                    }

                    if (pairs.get(oid.toString()) instanceof ASN1String) {
                        ASN1String o = (ASN1String) pairs.get(oid.toString());
                        value = o.getString();
                    } else if (pairs.get(oid.toString()) != null) {
                        LOGGER.error("Type unknown " + pairs.get(oid.toString()).getClass());
                    }
                }
            } catch (Exception e) {
                LOGGER.debug("Unable to retrieve X500Name from certificate : " + e.getMessage(), e);
            }
        }
        return value;
    }

    public byte[] getSubjectKeyIdentifier(CertificateToken certificate) {
        byte[] subjectKeyIdentifier = null;
        if (certificate != null) {
            X509Certificate x509Certificate = certificate.getCertificate();
            subjectKeyIdentifier = x509Certificate.getExtensionValue(SUBJECT_KEY_IDENTIFIER_OID);
        }
        return subjectKeyIdentifier;
    }

    /**
     * Compare certificate country code to scheme territory
     * 
     * @param certificate
     * @param schemeTerritory
     */
    public boolean countryMatchSchemeTerritory(CertificateToken certificate, String schemeTerritory) {
        if (certificate == null || schemeTerritory == null) {
            return false;
        }
        return DSSASN1Utils.extractAttributeFromX500Principal(BCStyle.C, certificate.getSubjectX500Principal()).equals(schemeTerritory);
    }

    /**
     * Compare certificate organization to scheme operator names
     * 
     * @param certificate
     * @param schemeTerritory
     */
    public boolean organizationMatchOperatorName(CertificateToken certificate, List<String> operatorNames) {
        if (certificate == null || CollectionUtils.isEmpty(operatorNames)) {
            return false;
        }
        String organization = DSSASN1Utils.extractAttributeFromX500Principal(BCStyle.O, certificate.getSubjectX500Principal());
        return operatorNames.contains(organization);
    }

    /**
     * Check certificate expiration validity
     * 
     * @param certificate
     */
    public boolean certificateIsExpired(CertificateToken certificate, Date date) {
        if (certificate == null || date == null) {
            return false;
        }
        return certificate.isExpiredOn(date);
    }

    /**
     * Check if certificate contains tslSigning extended key usage
     * 
     * @param certificate
     */
    public boolean hasTslSigningExtendedKeyUsage(CertificateToken certificate) {
        if (certificate != null) {
            try {
                X509Certificate x509Certificate = certificate.getCertificate();
                List<String> extendedKeyUsages = x509Certificate.getExtendedKeyUsage();
                return CollectionUtils.isNotEmpty(extendedKeyUsages) && extendedKeyUsages.contains(ID_TSL_KP_TSLSIGNING_OID);
            } catch (Exception e) {
                LOGGER.debug("Unable to retrieve extended key usages : " + e.getMessage(), e);
            }
        }
        return false;
    }

    /**
     * Check if certificate contains digitalSignature and/or nonRepudiation key usage and no others
     * 
     * @param certificate
     */
    public boolean hasAllowedKeyUsagesBits(CertificateToken certificate) {
        if (certificate != null) {
            List<KeyUsageBit> keyUsageBits = certificate.getKeyUsageBits();
            if (CollectionUtils.size(keyUsageBits) == 1) {
                return (keyUsageBits.contains(KeyUsageBit.digitalSignature) || keyUsageBits.contains(KeyUsageBit.nonRepudiation));
            } else if (CollectionUtils.size(keyUsageBits) == 2) {
                return (keyUsageBits.contains(KeyUsageBit.digitalSignature) && keyUsageBits.contains(KeyUsageBit.nonRepudiation));
            }
        }
        return false;
    }

    /**
     * Check if the certificate BasicConstraints extension is CA=false
     * 
     * @param certificate
     */
    public boolean isBasicConstraintCaFalse(CertificateToken certificate) {
        if (certificate != null) {
            X509Certificate x509Certificate = certificate.getCertificate();
            int basicConstraints = x509Certificate.getBasicConstraints();
            return basicConstraints == -1;
        }
        return false;
    }

    /**
     * Check if the certificate is self-signed or issued by a TSP listed in a TL
     * 
     * @param certificate
     */
    public boolean isIssuerVerified(CertificateToken certificate) {
        if (certificate.isSelfSigned()) {
            return true;
        } else {
            SignatureValidationContext svc = tlCertificateService.initSVC(certificate);
            Set<CertificateToken> rootCertificate = tlCertificateService.getRootCertificate(certificate, svc);
            return (!CollectionUtils.isEmpty(rootCertificate));
        }
    }

    /**
     * Check if SKI in the certificate is computed according to method M1 or M2 defined in RFC 5280
     * 
     * @param certificate
     */
    public boolean isSKIComputeRight(CertificateToken certificate) {
        try {
            X509ExtensionUtils extensionUtils = new X509ExtensionUtils(DSSRevocationUtils.getSHA1DigestCalculator());
            ASN1OctetString asn1;

            X509CertificateHolder certificiateHolder = new X509CertificateHolder(certificate.getEncoded());
            SubjectPublicKeyInfo publickey = certificiateHolder.getSubjectPublicKeyInfo();
            SubjectKeyIdentifier certSki = extensionUtils.createSubjectKeyIdentifier(publickey);
            asn1 = (ASN1OctetString) certSki.toASN1Primitive();
            byte[] skiSHAMethod1 = asn1.getOctets();

            SubjectKeyIdentifier certSkiTruncated = extensionUtils.createTruncatedSubjectKeyIdentifier(publickey);
            asn1 = (ASN1OctetString) certSkiTruncated.toASN1Primitive();
            byte[] skiSHA1Method2 = asn1.getOctets();

            byte[] ski = DSSASN1Utils.getSki(certificate);
            return (Arrays.equals(ski, skiSHAMethod1) || Arrays.equals(ski, skiSHA1Method2));
        } catch (IOException e) {
            LOGGER.error("Error while getting SKI", e);
            return false;
        }
    }

}