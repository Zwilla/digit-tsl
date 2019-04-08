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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DLSequence;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.x509.extension.X509ExtensionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import eu.europa.ec.joinup.tsl.business.dto.tl.TLName;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;
import eu.europa.ec.joinup.tsl.model.DBCheckResult;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBFilesAvailability;
import eu.europa.ec.joinup.tsl.model.enums.AvailabilityStatus;
import eu.europa.ec.joinup.tsl.model.enums.CheckStatus;
import eu.europa.ec.joinup.tsl.model.enums.MimeType;
import eu.europa.ec.joinup.tsl.model.enums.SignatureStatus;
import eu.europa.esig.dss.DSSDocument;
import eu.europa.esig.dss.DSSException;
import eu.europa.esig.dss.DSSUtils;
import eu.europa.esig.dss.DigestAlgorithm;
import eu.europa.esig.dss.DomUtils;
import eu.europa.esig.dss.FileDocument;
import eu.europa.esig.dss.validation.AdvancedSignature;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;
import eu.europa.esig.dss.validation.SignedDocumentValidator;
import eu.europa.esig.dss.x509.CertificateToken;
import eu.europa.esig.jaxb.tsl.AnyType;
import eu.europa.esig.jaxb.v5.tsl.AnyTypeV5;

/**
 * TrustedList toolbox
 */
public final class TLUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TLUtils.class);

    private static final Pattern hexadecimalPattern = Pattern.compile("^[0-9a-fA-F]+$");
    private static final String PDF_START_CONTENT = "%PDF-";
    private static final DateFormat dateFormatYMD = new SimpleDateFormat("yyyy-MM-dd");

    private TLUtils() {
    }

    public static boolean isHex(byte[] content) {
        if (content == null) {
            return false;
        }
        String hexContent = new String(content);
        Matcher matcher = hexadecimalPattern.matcher(hexContent);
        return matcher.find();
    }

    public static boolean isPdf(byte[] content) {
        if (content == null) {
            return false;
        }
        String stringContent = new String(content, 0, content.length > 20 ? 20 : content.length);
        return StringUtils.startsWithIgnoreCase(stringContent, PDF_START_CONTENT);
    }

    public static boolean isXml(String url, byte[] content) {
        if (ArrayUtils.isEmpty(content)) {
            return false;
        }
        try {
            Document dom = DomUtils.buildDOM(content);
            if (dom != null) {
                return true;
            }
        } catch (Exception e) {
            LOGGER.debug("Unable to parse xml (url:" + url + ") : " + e.getMessage());
        }
        return false;
    }

    public static String getSHA2Url(String xmlUrl) {
        String sha2Url = xmlUrl.replaceAll("(?i)\\.xml$", ".sha2").replaceAll("(?i)\\.xtsl$", ".sha2");
        if (!sha2Url.matches(".+sha2$")) {
            sha2Url = sha2Url + ".sha2";
        }
        return sha2Url;
    }

    public static String getSHA2(byte[] content) {
        if (content == null) {
            return StringUtils.EMPTY;
        }
        BOMInputStream bomContent = null;
        byte[] digest = null;
        try {
            bomContent = new BOMInputStream(new ByteArrayInputStream(content));
            digest = DSSUtils.digest(DigestAlgorithm.SHA256, bomContent);
        } catch (Exception e) {
            LOGGER.error("Conversion of byte array to SHA2 digest error.");
        } finally {
            IOUtils.closeQuietly(bomContent);
        }

        if (digest == null) {
            return StringUtils.EMPTY;
        }
        return Hex.encodeHexString(digest);
    }

    public static Map<String, Object> extractAsMap(List<Serializable> serializables) {
        Map<String, Object> properties = new HashMap<>();
        for (Serializable serializable : serializables) {
            if (serializable instanceof AnyType) {
                AnyType anyInfo = (AnyType) serializable;
                for (Object content : anyInfo.getContent()) {
                    if (content instanceof JAXBElement) {
                        @SuppressWarnings("rawtypes")
                        JAXBElement jaxbElement = (JAXBElement) content;
                        properties.put(jaxbElement.getName().toString(), jaxbElement.getValue());
                    } else if (content instanceof Element) {
                        Element element = (Element) content;
                        properties.put("{" + element.getNamespaceURI() + "}" + element.getLocalName(), element);
                    }
                }
            }
        }
        return properties;
    }

    public static Map<String, Object> extractAsMapV5(List<Serializable> serializables) {
        Map<String, Object> properties = new HashMap<>();
        for (Serializable serializable : serializables) {
            if (serializable instanceof AnyTypeV5) {
                AnyTypeV5 anyInfo = (AnyTypeV5) serializable;
                for (Object content : anyInfo.getContent()) {
                    if (content instanceof JAXBElement) {
                        @SuppressWarnings("rawtypes")
                        JAXBElement jaxbElement = (JAXBElement) content;
                        properties.put(jaxbElement.getName().toString(), jaxbElement.getValue());
                    } else if (content instanceof Element) {
                        Element element = (Element) content;
                        properties.put("{" + element.getNamespaceURI() + "}" + element.getLocalName(), element);
                    }
                }
            }
        }
        return properties;
    }

    public static MimeType convert(String mimeTypeProp) {
        if (StringUtils.endsWithIgnoreCase("application/vnd.etsi.tsl+xml", mimeTypeProp)) {
            return MimeType.XML;
        } else if (StringUtils.endsWithIgnoreCase("application/pdf", mimeTypeProp)) {
            return MimeType.PDF;
        } else {
            return null;
        }
    }

    public static String convert(MimeType mimeType) {
        if (MimeType.XML == mimeType) {
            return "application/vnd.etsi.tsl+xml";
        } else if (MimeType.PDF == mimeType) {
            return "application/pdf";
        } else {
            return "";
        }
    }

    public static Date toDate(XMLGregorianCalendar calendar) {
        if (calendar == null) {
            return null;
        }
        return calendar.toGregorianCalendar().getTime();
    }

    public static Date toDateFormat(String date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            return df.parse(date);
        } catch (ParseException e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    public static String toStringFormatZ(Date date) {
        DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            return df.format(date);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    // Use for "Changes" - Use LOCAL DATETIME
    public static String toStringFormat(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(date==null) {
            return "";
        }
        return df.format(date);
    }

    public static String toDateFormatYMD(Date date) {
        return dateFormatYMD.format(date);
    }

    /**
     * Converts XMLGregorianCalendar to java.util.Date in Java
     **/
    public static XMLGregorianCalendar toXMLGregorianDate(Date date) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);

        XMLGregorianCalendar calendar = null;
        try {
            calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
        } catch (DatatypeConfigurationException e) {
            LOGGER.debug("Unable to parse Date to XMLGregorianCalendar : " + e.getMessage());
        }
        return calendar;
    }

    public static boolean checkDate(Date date1, Date date2) {
        return toXMLGregorianDate(date1).equals(toXMLGregorianDate(date2));
    }

    /**
     * Converts XMLGregorianCalendar to java.util.Date in Java
     **/
    public static BigInteger toBigInteger(int value) {
        return BigInteger.valueOf(value);
    }

    public static CheckStatus getCheckStatus(List<DBCheckResult> checkResList) {
        CheckStatus tmp = CheckStatus.SUCCESS;
        if (CollectionUtils.isNotEmpty(checkResList)) {
            for (DBCheckResult checkRes : checkResList) {
                if (checkRes.getEndDate() == null) {
                    if (CheckStatus.ERROR.equals(checkRes.getStatus())) {
                        return CheckStatus.ERROR;
                    } else if (CheckStatus.WARNING.equals(checkRes.getStatus())) {
                        tmp = CheckStatus.WARNING;
                    }
                }
            }
        }
        return tmp;
    }

    public static CheckStatus getTLStatus(CheckStatus checkStatus, DBFiles xmlFile) {
        CheckStatus tmp = checkStatus;

        if (xmlFile != null) {
            List<DBFilesAvailability> aList = xmlFile.getAvailabilityInfos();
            /**
             * AVAILABLE UNSUPPORTED UNAVAILABLE
             **/
            if (!CollectionUtils.isEmpty(aList) && !aList.get(0).getStatus().equals(AvailabilityStatus.AVAILABLE)) {
                return CheckStatus.ERROR;
            }

            if ((xmlFile.getSignatureInformation() != null) && !xmlFile.getSignatureInformation().getIndication().equals(SignatureStatus.VALID)) {
                return CheckStatus.ERROR;
            }

        }

        return tmp;
    }

    public static CertificateToken getSigningCertificate(File xmlFile) {
        CertificateToken cert = null;
        DSSDocument dssDocument = new FileDocument(xmlFile);
        SignedDocumentValidator v = SignedDocumentValidator.fromDocument(dssDocument);
        v.setCertificateVerifier(new CommonCertificateVerifier());
        if ((v.getSignatures() != null) && !v.getSignatures().isEmpty()) {
            AdvancedSignature ades = v.getSignatures().get(0);
            if (ades.getSigningCertificateToken() != null) {
                cert = ades.getSigningCertificateToken();
            } else {
                LOGGER.warn("No signing certificate");
            }
        }
        return cert;
    }

    public static byte[] getSki(final CertificateToken certificateToken) throws DSSException {
        try {
            byte[] sKI = certificateToken.getCertificate().getExtensionValue(Extension.subjectKeyIdentifier.getId());
            if (ArrayUtils.isNotEmpty(sKI)) {
                ASN1Primitive extension = X509ExtensionUtil.fromExtensionValue(sKI);
                SubjectKeyIdentifier skiBC = SubjectKeyIdentifier.getInstance(extension);
                return skiBC.getKeyIdentifier();
            } else {
                DLSequence seq1 = (DLSequence) DERSequence.fromByteArray(certificateToken.getPublicKey().getEncoded());
                DERBitString item2 = (DERBitString) seq1.getObjectAt(1);

                return DSSUtils.digest(DigestAlgorithm.SHA1, item2.getOctets());
            }
        } catch (Exception e) {
            throw new DSSException(e);
        }
    }

    public static String getSkiString(final CertificateToken certificateToken) throws DSSException {
        try {
            byte[] sKI = certificateToken.getCertificate().getExtensionValue(Extension.subjectKeyIdentifier.getId());
            if (ArrayUtils.isNotEmpty(sKI)) {
                return getStringSKIfromBytes(sKI);
            } else {
                DLSequence seq1 = (DLSequence) DERSequence.fromByteArray(certificateToken.getPublicKey().getEncoded());
                DERBitString item2 = (DERBitString) seq1.getObjectAt(1);

                return Base64.encodeBase64String(DSSUtils.digest(DigestAlgorithm.SHA1, item2.getOctets()));
            }
        } catch (Exception e) {
            throw new DSSException(e);
        }
    }

    public static String getStringSKIfromBytes(byte[] sKI) {
        try {
            ASN1Primitive extension = X509ExtensionUtil.fromExtensionValue(sKI);
            SubjectKeyIdentifier skiBC = SubjectKeyIdentifier.getInstance(extension);
            return Base64.encodeBase64String(skiBC.getKeyIdentifier());

        } catch (IOException e) {
            return DatatypeConverter.printBase64Binary(sKI);
        }
    }

    public static String getStringDigest(String str) {
        return Base64.encodeBase64String(DSSUtils.digest(DigestAlgorithm.SHA1, str.getBytes()));
    }

    /**
     * Extract english name of list
     *
     * @param names
     */
    public static Set<String> extractEnglishValues(List<TLName> names) {
        Set<String> namesList = new HashSet<>();
        if (!CollectionUtils.isEmpty(names)) {
            for (TLName name : names) {
                if (name.getLanguage().equalsIgnoreCase("en")) {
                    namesList.add(name.getValue());
                }
            }
        }
        return namesList;
    }

    /**
     * Compare TSP Trade names and names 'en' value and return true if an entry match
     * 
     * @param currentTSP
     * @param previousTSP
     */
    public static boolean isTSPMatch(TLServiceProvider currentTSP, TLServiceProvider previousTSP) {
        if (isTradeNameMatch(currentTSP.getTSPTradeName(), previousTSP.getTSPTradeName()) || isNameMatch(currentTSP.getTSPName(), previousTSP.getTSPName())) {
            return true;
        }
        return false;
    }

    /**
     * Compare 'en' tsp names values and return true if an entry match
     * 
     * @param currentNames
     * @param comparedNames
     */
    public static boolean isNameMatch(List<TLName> currentNames, List<TLName> comparedNames) {
        for (String currentName : extractEnglishValues(currentNames)) {
            for (String comparedName : extractEnglishValues(comparedNames)) {
                if (currentName.equalsIgnoreCase(comparedName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Compare 'en' tsp trade names values and return true if a entry match
     * 
     * @param currentTradeNames
     * @param comparedTradeNames
     */
    public static boolean isTradeNameMatch(List<TLName> currentTradeNames, List<TLName> comparedTradeNames) {
        for (String currentName : extractVATorNTR(currentTradeNames)) {
            for (String comparedName : extractVATorNTR(comparedTradeNames)) {
                if (currentName.equalsIgnoreCase(comparedName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Extract english VAT/NTR values from TSP trade names. Return "undefined" in the list if no VAR/NTR found
     * 
     * @param tradeNames
     */
    public static Set<String> extractVATorNTR(List<TLName> tradeNames) {
        Set<String> setVATorNTR = new HashSet<>();
        if (CollectionUtils.isNotEmpty(tradeNames)) {
            for (String tradeName : extractEnglishValues(tradeNames)) {
                if (TLUtils.isVATorNTR(tradeName)) {
                    setVATorNTR.add(tradeName);
                }
            }
        }
        return setVATorNTR;
    }

    /**
     * Return if TradeName match VAT/NTR pattern
     * 
     * @param tradeName
     */
    public static boolean isVATorNTR(String tradeName) {
        return (tradeName.startsWith("VAT") || tradeName.startsWith("NTR"));
    }
}