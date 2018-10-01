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
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.06 at 08:00:20 AM CET 
//


package eu.europa.esig.jaxb.v5.xmldsig;

import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the eu.europa.esig.jaxb.v5.xmldsig package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Signature_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "Signature");
    private final static QName _SignatureValue_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "SignatureValue");
    private final static QName _SignedInfo_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "SignedInfo");
    private final static QName _CanonicalizationMethod_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "CanonicalizationMethod");
    private final static QName _SignatureMethod_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "SignatureMethod");
    private final static QName _Reference_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "Reference");
    private final static QName _Transforms_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "Transforms");
    private final static QName _Transform_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "Transform");
    private final static QName _DigestMethod_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "DigestMethod");
    private final static QName _DigestValue_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "DigestValue");
    private final static QName _KeyInfo_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "KeyInfo");
    private final static QName _KeyName_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "KeyName");
    private final static QName _MgmtData_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "MgmtData");
    private final static QName _KeyValue_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "KeyValue");
    private final static QName _RetrievalMethod_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "RetrievalMethod");
    private final static QName _X509Data_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "X509Data");
    private final static QName _PGPData_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "PGPData");
    private final static QName _SPKIData_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "SPKIData");
    private final static QName _Object_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "Object");
    private final static QName _Manifest_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "Manifest");
    private final static QName _SignatureProperties_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "SignatureProperties");
    private final static QName _SignatureProperty_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "SignatureProperty");
    private final static QName _DSAKeyValue_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "DSAKeyValue");
    private final static QName _RSAKeyValue_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "RSAKeyValue");
    private final static QName _SPKIDataTypeV5SPKISexp_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "SPKISexp");
    private final static QName _PGPDataTypeV5PGPKeyID_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "PGPKeyID");
    private final static QName _PGPDataTypeV5PGPKeyPacket_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "PGPKeyPacket");
    private final static QName _X509DataTypeV5X509IssuerSerial_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "X509IssuerSerial");
    private final static QName _X509DataTypeV5X509SKI_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "X509SKI");
    private final static QName _X509DataTypeV5X509SubjectName_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "X509SubjectName");
    private final static QName _X509DataTypeV5X509Certificate_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "X509Certificate");
    private final static QName _X509DataTypeV5X509CRL_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "X509CRL");
    private final static QName _TransformTypeV5XPath_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "XPath");
    private final static QName _SignatureMethodTypeV5HMACOutputLength_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "HMACOutputLength");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: eu.europa.esig.jaxb.v5.xmldsig
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SignatureTypeV5 }
     * 
     */
    public SignatureTypeV5 createSignatureTypeV5() {
        return new SignatureTypeV5();
    }

    /**
     * Create an instance of {@link SignatureValueTypeV5 }
     * 
     */
    public SignatureValueTypeV5 createSignatureValueTypeV5() {
        return new SignatureValueTypeV5();
    }

    /**
     * Create an instance of {@link SignedInfoTypeV5 }
     * 
     */
    public SignedInfoTypeV5 createSignedInfoTypeV5() {
        return new SignedInfoTypeV5();
    }

    /**
     * Create an instance of {@link CanonicalizationMethodTypeV5 }
     * 
     */
    public CanonicalizationMethodTypeV5 createCanonicalizationMethodTypeV5() {
        return new CanonicalizationMethodTypeV5();
    }

    /**
     * Create an instance of {@link SignatureMethodTypeV5 }
     * 
     */
    public SignatureMethodTypeV5 createSignatureMethodTypeV5() {
        return new SignatureMethodTypeV5();
    }

    /**
     * Create an instance of {@link ReferenceTypeV5 }
     * 
     */
    public ReferenceTypeV5 createReferenceTypeV5() {
        return new ReferenceTypeV5();
    }

    /**
     * Create an instance of {@link TransformsTypeV5 }
     * 
     */
    public TransformsTypeV5 createTransformsTypeV5() {
        return new TransformsTypeV5();
    }

    /**
     * Create an instance of {@link TransformTypeV5 }
     * 
     */
    public TransformTypeV5 createTransformTypeV5() {
        return new TransformTypeV5();
    }

    /**
     * Create an instance of {@link DigestMethodTypeV5 }
     * 
     */
    public DigestMethodTypeV5 createDigestMethodTypeV5() {
        return new DigestMethodTypeV5();
    }

    /**
     * Create an instance of {@link KeyInfoTypeV5 }
     * 
     */
    public KeyInfoTypeV5 createKeyInfoTypeV5() {
        return new KeyInfoTypeV5();
    }

    /**
     * Create an instance of {@link KeyValueTypeV5 }
     * 
     */
    public KeyValueTypeV5 createKeyValueTypeV5() {
        return new KeyValueTypeV5();
    }

    /**
     * Create an instance of {@link RetrievalMethodTypeV5 }
     * 
     */
    public RetrievalMethodTypeV5 createRetrievalMethodTypeV5() {
        return new RetrievalMethodTypeV5();
    }

    /**
     * Create an instance of {@link X509DataTypeV5 }
     * 
     */
    public X509DataTypeV5 createX509DataTypeV5() {
        return new X509DataTypeV5();
    }

    /**
     * Create an instance of {@link PGPDataTypeV5 }
     * 
     */
    public PGPDataTypeV5 createPGPDataTypeV5() {
        return new PGPDataTypeV5();
    }

    /**
     * Create an instance of {@link SPKIDataTypeV5 }
     * 
     */
    public SPKIDataTypeV5 createSPKIDataTypeV5() {
        return new SPKIDataTypeV5();
    }

    /**
     * Create an instance of {@link ObjectTypeV5 }
     * 
     */
    public ObjectTypeV5 createObjectTypeV5() {
        return new ObjectTypeV5();
    }

    /**
     * Create an instance of {@link ManifestTypeV5 }
     * 
     */
    public ManifestTypeV5 createManifestTypeV5() {
        return new ManifestTypeV5();
    }

    /**
     * Create an instance of {@link SignaturePropertiesTypeV5 }
     * 
     */
    public SignaturePropertiesTypeV5 createSignaturePropertiesTypeV5() {
        return new SignaturePropertiesTypeV5();
    }

    /**
     * Create an instance of {@link SignaturePropertyTypeV5 }
     * 
     */
    public SignaturePropertyTypeV5 createSignaturePropertyTypeV5() {
        return new SignaturePropertyTypeV5();
    }

    /**
     * Create an instance of {@link DSAKeyValueTypeV5 }
     * 
     */
    public DSAKeyValueTypeV5 createDSAKeyValueTypeV5() {
        return new DSAKeyValueTypeV5();
    }

    /**
     * Create an instance of {@link RSAKeyValueTypeV5 }
     * 
     */
    public RSAKeyValueTypeV5 createRSAKeyValueTypeV5() {
        return new RSAKeyValueTypeV5();
    }

    /**
     * Create an instance of {@link X509IssuerSerialTypeV5 }
     * 
     */
    public X509IssuerSerialTypeV5 createX509IssuerSerialTypeV5() {
        return new X509IssuerSerialTypeV5();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SignatureTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "Signature")
    public JAXBElement<SignatureTypeV5> createSignature(SignatureTypeV5 value) {
        return new JAXBElement<SignatureTypeV5>(_Signature_QNAME, SignatureTypeV5 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SignatureValueTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "SignatureValue")
    public JAXBElement<SignatureValueTypeV5> createSignatureValue(SignatureValueTypeV5 value) {
        return new JAXBElement<SignatureValueTypeV5>(_SignatureValue_QNAME, SignatureValueTypeV5 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SignedInfoTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "SignedInfo")
    public JAXBElement<SignedInfoTypeV5> createSignedInfo(SignedInfoTypeV5 value) {
        return new JAXBElement<SignedInfoTypeV5>(_SignedInfo_QNAME, SignedInfoTypeV5 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CanonicalizationMethodTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "CanonicalizationMethod")
    public JAXBElement<CanonicalizationMethodTypeV5> createCanonicalizationMethod(CanonicalizationMethodTypeV5 value) {
        return new JAXBElement<CanonicalizationMethodTypeV5>(_CanonicalizationMethod_QNAME, CanonicalizationMethodTypeV5 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SignatureMethodTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "SignatureMethod")
    public JAXBElement<SignatureMethodTypeV5> createSignatureMethod(SignatureMethodTypeV5 value) {
        return new JAXBElement<SignatureMethodTypeV5>(_SignatureMethod_QNAME, SignatureMethodTypeV5 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReferenceTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "Reference")
    public JAXBElement<ReferenceTypeV5> createReference(ReferenceTypeV5 value) {
        return new JAXBElement<ReferenceTypeV5>(_Reference_QNAME, ReferenceTypeV5 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransformsTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "Transforms")
    public JAXBElement<TransformsTypeV5> createTransforms(TransformsTypeV5 value) {
        return new JAXBElement<TransformsTypeV5>(_Transforms_QNAME, TransformsTypeV5 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransformTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "Transform")
    public JAXBElement<TransformTypeV5> createTransform(TransformTypeV5 value) {
        return new JAXBElement<TransformTypeV5>(_Transform_QNAME, TransformTypeV5 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DigestMethodTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "DigestMethod")
    public JAXBElement<DigestMethodTypeV5> createDigestMethod(DigestMethodTypeV5 value) {
        return new JAXBElement<DigestMethodTypeV5>(_DigestMethod_QNAME, DigestMethodTypeV5 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "DigestValue")
    public JAXBElement<byte[]> createDigestValue(byte[] value) {
        return new JAXBElement<byte[]>(_DigestValue_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link KeyInfoTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "KeyInfo")
    public JAXBElement<KeyInfoTypeV5> createKeyInfo(KeyInfoTypeV5 value) {
        return new JAXBElement<KeyInfoTypeV5>(_KeyInfo_QNAME, KeyInfoTypeV5 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "KeyName")
    public JAXBElement<String> createKeyName(String value) {
        return new JAXBElement<String>(_KeyName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "MgmtData")
    public JAXBElement<String> createMgmtData(String value) {
        return new JAXBElement<String>(_MgmtData_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link KeyValueTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "KeyValue")
    public JAXBElement<KeyValueTypeV5> createKeyValue(KeyValueTypeV5 value) {
        return new JAXBElement<KeyValueTypeV5>(_KeyValue_QNAME, KeyValueTypeV5 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RetrievalMethodTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "RetrievalMethod")
    public JAXBElement<RetrievalMethodTypeV5> createRetrievalMethod(RetrievalMethodTypeV5 value) {
        return new JAXBElement<RetrievalMethodTypeV5>(_RetrievalMethod_QNAME, RetrievalMethodTypeV5 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link X509DataTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "X509Data")
    public JAXBElement<X509DataTypeV5> createX509Data(X509DataTypeV5 value) {
        return new JAXBElement<X509DataTypeV5>(_X509Data_QNAME, X509DataTypeV5 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PGPDataTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "PGPData")
    public JAXBElement<PGPDataTypeV5> createPGPData(PGPDataTypeV5 value) {
        return new JAXBElement<PGPDataTypeV5>(_PGPData_QNAME, PGPDataTypeV5 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SPKIDataTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "SPKIData")
    public JAXBElement<SPKIDataTypeV5> createSPKIData(SPKIDataTypeV5 value) {
        return new JAXBElement<SPKIDataTypeV5>(_SPKIData_QNAME, SPKIDataTypeV5 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObjectTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "Object")
    public JAXBElement<ObjectTypeV5> createObject(ObjectTypeV5 value) {
        return new JAXBElement<ObjectTypeV5>(_Object_QNAME, ObjectTypeV5 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ManifestTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "Manifest")
    public JAXBElement<ManifestTypeV5> createManifest(ManifestTypeV5 value) {
        return new JAXBElement<ManifestTypeV5>(_Manifest_QNAME, ManifestTypeV5 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SignaturePropertiesTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "SignatureProperties")
    public JAXBElement<SignaturePropertiesTypeV5> createSignatureProperties(SignaturePropertiesTypeV5 value) {
        return new JAXBElement<SignaturePropertiesTypeV5>(_SignatureProperties_QNAME, SignaturePropertiesTypeV5 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SignaturePropertyTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "SignatureProperty")
    public JAXBElement<SignaturePropertyTypeV5> createSignatureProperty(SignaturePropertyTypeV5 value) {
        return new JAXBElement<SignaturePropertyTypeV5>(_SignatureProperty_QNAME, SignaturePropertyTypeV5 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DSAKeyValueTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "DSAKeyValue")
    public JAXBElement<DSAKeyValueTypeV5> createDSAKeyValue(DSAKeyValueTypeV5 value) {
        return new JAXBElement<DSAKeyValueTypeV5>(_DSAKeyValue_QNAME, DSAKeyValueTypeV5 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RSAKeyValueTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "RSAKeyValue")
    public JAXBElement<RSAKeyValueTypeV5> createRSAKeyValue(RSAKeyValueTypeV5 value) {
        return new JAXBElement<RSAKeyValueTypeV5>(_RSAKeyValue_QNAME, RSAKeyValueTypeV5 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "SPKISexp", scope = SPKIDataTypeV5 .class)
    public JAXBElement<byte[]> createSPKIDataTypeV5SPKISexp(byte[] value) {
        return new JAXBElement<byte[]>(_SPKIDataTypeV5SPKISexp_QNAME, byte[].class, SPKIDataTypeV5 .class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "PGPKeyID", scope = PGPDataTypeV5 .class)
    public JAXBElement<byte[]> createPGPDataTypeV5PGPKeyID(byte[] value) {
        return new JAXBElement<byte[]>(_PGPDataTypeV5PGPKeyID_QNAME, byte[].class, PGPDataTypeV5 .class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "PGPKeyPacket", scope = PGPDataTypeV5 .class)
    public JAXBElement<byte[]> createPGPDataTypeV5PGPKeyPacket(byte[] value) {
        return new JAXBElement<byte[]>(_PGPDataTypeV5PGPKeyPacket_QNAME, byte[].class, PGPDataTypeV5 .class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link X509IssuerSerialTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "X509IssuerSerial", scope = X509DataTypeV5 .class)
    public JAXBElement<X509IssuerSerialTypeV5> createX509DataTypeV5X509IssuerSerial(X509IssuerSerialTypeV5 value) {
        return new JAXBElement<X509IssuerSerialTypeV5>(_X509DataTypeV5X509IssuerSerial_QNAME, X509IssuerSerialTypeV5 .class, X509DataTypeV5 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "X509SKI", scope = X509DataTypeV5 .class)
    public JAXBElement<byte[]> createX509DataTypeV5X509SKI(byte[] value) {
        return new JAXBElement<byte[]>(_X509DataTypeV5X509SKI_QNAME, byte[].class, X509DataTypeV5 .class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "X509SubjectName", scope = X509DataTypeV5 .class)
    public JAXBElement<String> createX509DataTypeV5X509SubjectName(String value) {
        return new JAXBElement<String>(_X509DataTypeV5X509SubjectName_QNAME, String.class, X509DataTypeV5 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "X509Certificate", scope = X509DataTypeV5 .class)
    public JAXBElement<byte[]> createX509DataTypeV5X509Certificate(byte[] value) {
        return new JAXBElement<byte[]>(_X509DataTypeV5X509Certificate_QNAME, byte[].class, X509DataTypeV5 .class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "X509CRL", scope = X509DataTypeV5 .class)
    public JAXBElement<byte[]> createX509DataTypeV5X509CRL(byte[] value) {
        return new JAXBElement<byte[]>(_X509DataTypeV5X509CRL_QNAME, byte[].class, X509DataTypeV5 .class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "XPath", scope = TransformTypeV5 .class)
    public JAXBElement<String> createTransformTypeV5XPath(String value) {
        return new JAXBElement<String>(_TransformTypeV5XPath_QNAME, String.class, TransformTypeV5 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "HMACOutputLength", scope = SignatureMethodTypeV5 .class)
    public JAXBElement<BigInteger> createSignatureMethodTypeV5HMACOutputLength(BigInteger value) {
        return new JAXBElement<BigInteger>(_SignatureMethodTypeV5HMACOutputLength_QNAME, BigInteger.class, SignatureMethodTypeV5 .class, value);
    }

}
