//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.06 at 08:00:20 AM CET 
//

package eu.europa.esig.jaxb.v5.tsl;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java element interface generated in the eu.europa.esig.jaxb.v5.tsl
 * package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the Java representation for XML content. The Java representation of XML
 * content can consist of schema derived interfaces and classes representing the binding of schema type definitions, element declarations and model
 * groups. Factory methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _PostalAddresses_QNAME = new QName("https://uri.etsi.org/02231/v2#", "PostalAddresses");
    private final static QName _PostalAddress_QNAME = new QName("https://uri.etsi.org/02231/v2#", "PostalAddress");
    private final static QName _ElectronicAddress_QNAME = new QName("https://uri.etsi.org/02231/v2#", "ElectronicAddress");
    private final static QName _Extension_QNAME = new QName("https://uri.etsi.org/02231/v2#", "Extension");
    private final static QName _TrustServiceStatusList_QNAME = new QName("https://uri.etsi.org/02231/v2#", "TrustServiceStatusList");
    private final static QName _TrustServiceProviderList_QNAME = new QName("https://uri.etsi.org/02231/v2#", "TrustServiceProviderList");
    private final static QName _SchemeInformation_QNAME = new QName("https://uri.etsi.org/02231/v2#", "SchemeInformation");
    private final static QName _TSLType_QNAME = new QName("https://uri.etsi.org/02231/v2#", "TSLType");
    private final static QName _SchemeOperatorName_QNAME = new QName("https://uri.etsi.org/02231/v2#", "SchemeOperatorName");
    private final static QName _SchemeName_QNAME = new QName("https://uri.etsi.org/02231/v2#", "SchemeName");
    private final static QName _SchemeInformationURI_QNAME = new QName("https://uri.etsi.org/02231/v2#", "SchemeInformationURI");
    private final static QName _SchemeTypeCommunityRules_QNAME = new QName("https://uri.etsi.org/02231/v2#", "SchemeTypeCommunityRules");
    private final static QName _SchemeTerritory_QNAME = new QName("https://uri.etsi.org/02231/v2#", "SchemeTerritory");
    private final static QName _PolicyOrLegalNotice_QNAME = new QName("https://uri.etsi.org/02231/v2#", "PolicyOrLegalNotice");
    private final static QName _NextUpdate_QNAME = new QName("https://uri.etsi.org/02231/v2#", "NextUpdate");
    private final static QName _PointersToOtherTSL_QNAME = new QName("https://uri.etsi.org/02231/v2#", "PointersToOtherTSL");
    private final static QName _OtherTSLPointer_QNAME = new QName("https://uri.etsi.org/02231/v2#", "OtherTSLPointer");
    private final static QName _ServiceDigitalIdentities_QNAME = new QName("https://uri.etsi.org/02231/v2#", "ServiceDigitalIdentities");
    private final static QName _AdditionalInformation_QNAME = new QName("https://uri.etsi.org/02231/v2#", "AdditionalInformation");
    private final static QName _DistributionPoints_QNAME = new QName("https://uri.etsi.org/02231/v2#", "DistributionPoints");
    private final static QName _TrustServiceProvider_QNAME = new QName("https://uri.etsi.org/02231/v2#", "TrustServiceProvider");
    private final static QName _TSPInformation_QNAME = new QName("https://uri.etsi.org/02231/v2#", "TSPInformation");
    private final static QName _TSPServices_QNAME = new QName("https://uri.etsi.org/02231/v2#", "TSPServices");
    private final static QName _TSPService_QNAME = new QName("https://uri.etsi.org/02231/v2#", "TSPService");
    private final static QName _ServiceInformation_QNAME = new QName("https://uri.etsi.org/02231/v2#", "ServiceInformation");
    private final static QName _ServiceStatus_QNAME = new QName("https://uri.etsi.org/02231/v2#", "ServiceStatus");
    private final static QName _ServiceSupplyPoints_QNAME = new QName("https://uri.etsi.org/02231/v2#", "ServiceSupplyPoints");
    private final static QName _ServiceTypeIdentifier_QNAME = new QName("https://uri.etsi.org/02231/v2#", "ServiceTypeIdentifier");
    private final static QName _ServiceDigitalIdentity_QNAME = new QName("https://uri.etsi.org/02231/v2#", "ServiceDigitalIdentity");
    private final static QName _ServiceHistory_QNAME = new QName("https://uri.etsi.org/02231/v2#", "ServiceHistory");
    private final static QName _ServiceHistoryInstance_QNAME = new QName("https://uri.etsi.org/02231/v2#", "ServiceHistoryInstance");
    private final static QName _ExpiredCertsRevocationInfo_QNAME = new QName("https://uri.etsi.org/02231/v2#", "ExpiredCertsRevocationInfo");
    private final static QName _AdditionalServiceInformation_QNAME = new QName("https://uri.etsi.org/02231/v2#", "AdditionalServiceInformation");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: eu.europa.esig.jaxb.v5.tsl
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PostalAddressListTypeV5 }
     * 
     */
    public PostalAddressListTypeV5 createPostalAddressListTypeV5() {
        return new PostalAddressListTypeV5();
    }

    /**
     * Create an instance of {@link PostalAddressTypeV5 }
     * 
     */
    public PostalAddressTypeV5 createPostalAddressTypeV5() {
        return new PostalAddressTypeV5();
    }

    /**
     * Create an instance of {@link ElectronicAddressTypeV5 }
     * 
     */
    public ElectronicAddressTypeV5 createElectronicAddressTypeV5() {
        return new ElectronicAddressTypeV5();
    }

    /**
     * Create an instance of {@link ExtensionTypeV5 }
     * 
     */
    public ExtensionTypeV5 createExtensionTypeV5() {
        return new ExtensionTypeV5();
    }

    /**
     * Create an instance of {@link TrustStatusListTypeV5 }
     * 
     */
    public TrustStatusListTypeV5 createTrustStatusListTypeV5() {
        return new TrustStatusListTypeV5();
    }

    /**
     * Create an instance of {@link TrustServiceProviderListTypeV5 }
     * 
     */
    public TrustServiceProviderListTypeV5 createTrustServiceProviderListTypeV5() {
        return new TrustServiceProviderListTypeV5();
    }

    /**
     * Create an instance of {@link TSLSchemeInformationTypeV5 }
     * 
     */
    public TSLSchemeInformationTypeV5 createTSLSchemeInformationTypeV5() {
        return new TSLSchemeInformationTypeV5();
    }

    /**
     * Create an instance of {@link InternationalNamesTypeV5 }
     * 
     */
    public InternationalNamesTypeV5 createInternationalNamesTypeV5() {
        return new InternationalNamesTypeV5();
    }

    /**
     * Create an instance of {@link NonEmptyMultiLangURIListTypeV5 }
     * 
     */
    public NonEmptyMultiLangURIListTypeV5 createNonEmptyMultiLangURIListTypeV5() {
        return new NonEmptyMultiLangURIListTypeV5();
    }

    /**
     * Create an instance of {@link PolicyOrLegalnoticeTypeV5 }
     * 
     */
    public PolicyOrLegalnoticeTypeV5 createPolicyOrLegalnoticeTypeV5() {
        return new PolicyOrLegalnoticeTypeV5();
    }

    /**
     * Create an instance of {@link NextUpdateTypeV5 }
     * 
     */
    public NextUpdateTypeV5 createNextUpdateTypeV5() {
        return new NextUpdateTypeV5();
    }

    /**
     * Create an instance of {@link OtherTSLPointersTypeV5 }
     * 
     */
    public OtherTSLPointersTypeV5 createOtherTSLPointersTypeV5() {
        return new OtherTSLPointersTypeV5();
    }

    /**
     * Create an instance of {@link OtherTSLPointerTypeV5 }
     * 
     */
    public OtherTSLPointerTypeV5 createOtherTSLPointerTypeV5() {
        return new OtherTSLPointerTypeV5();
    }

    /**
     * Create an instance of {@link ServiceDigitalIdentityListTypeV5 }
     * 
     */
    public ServiceDigitalIdentityListTypeV5 createServiceDigitalIdentityListTypeV5() {
        return new ServiceDigitalIdentityListTypeV5();
    }

    /**
     * Create an instance of {@link AdditionalInformationTypeV5 }
     * 
     */
    public AdditionalInformationTypeV5 createAdditionalInformationTypeV5() {
        return new AdditionalInformationTypeV5();
    }

    /**
     * Create an instance of {@link NonEmptyURIListTypeV5 }
     * 
     */
    public NonEmptyURIListTypeV5 createNonEmptyURIListTypeV5() {
        return new NonEmptyURIListTypeV5();
    }

    /**
     * Create an instance of {@link TSPTypeV5 }
     * 
     */
    public TSPTypeV5 createTSPTypeV5() {
        return new TSPTypeV5();
    }

    /**
     * Create an instance of {@link TSPInformationTypeV5 }
     * 
     */
    public TSPInformationTypeV5 createTSPInformationTypeV5() {
        return new TSPInformationTypeV5();
    }

    /**
     * Create an instance of {@link TSPServicesListTypeV5 }
     * 
     */
    public TSPServicesListTypeV5 createTSPServicesListTypeV5() {
        return new TSPServicesListTypeV5();
    }

    /**
     * Create an instance of {@link TSPServiceTypeV5 }
     * 
     */
    public TSPServiceTypeV5 createTSPServiceTypeV5() {
        return new TSPServiceTypeV5();
    }

    /**
     * Create an instance of {@link TSPServiceInformationTypeV5 }
     * 
     */
    public TSPServiceInformationTypeV5 createTSPServiceInformationTypeV5() {
        return new TSPServiceInformationTypeV5();
    }

    /**
     * Create an instance of {@link ServiceSupplyPointsTypeV5 }
     * 
     */
    public ServiceSupplyPointsTypeV5 createServiceSupplyPointsTypeV5() {
        return new ServiceSupplyPointsTypeV5();
    }

    /**
     * Create an instance of {@link DigitalIdentityListTypeV5 }
     * 
     */
    public DigitalIdentityListTypeV5 createDigitalIdentityListTypeV5() {
        return new DigitalIdentityListTypeV5();
    }

    /**
     * Create an instance of {@link ServiceHistoryTypeV5 }
     * 
     */
    public ServiceHistoryTypeV5 createServiceHistoryTypeV5() {
        return new ServiceHistoryTypeV5();
    }

    /**
     * Create an instance of {@link ServiceHistoryInstanceTypeV5 }
     * 
     */
    public ServiceHistoryInstanceTypeV5 createServiceHistoryInstanceTypeV5() {
        return new ServiceHistoryInstanceTypeV5();
    }

    /**
     * Create an instance of {@link AdditionalServiceInformationTypeV5 }
     * 
     */
    public AdditionalServiceInformationTypeV5 createAdditionalServiceInformationTypeV5() {
        return new AdditionalServiceInformationTypeV5();
    }

    /**
     * Create an instance of {@link MultiLangNormStringTypeV5 }
     * 
     */
    public MultiLangNormStringTypeV5 createMultiLangNormStringTypeV5() {
        return new MultiLangNormStringTypeV5();
    }

    /**
     * Create an instance of {@link MultiLangStringTypeV5 }
     * 
     */
    public MultiLangStringTypeV5 createMultiLangStringTypeV5() {
        return new MultiLangStringTypeV5();
    }

    /**
     * Create an instance of {@link AddressTypeV5 }
     * 
     */
    public AddressTypeV5 createAddressTypeV5() {
        return new AddressTypeV5();
    }

    /**
     * Create an instance of {@link AnyTypeV5 }
     * 
     */
    public AnyTypeV5 createAnyTypeV5() {
        return new AnyTypeV5();
    }

    /**
     * Create an instance of {@link ExtensionsListTypeV5 }
     * 
     */
    public ExtensionsListTypeV5 createExtensionsListTypeV5() {
        return new ExtensionsListTypeV5();
    }

    /**
     * Create an instance of {@link NonEmptyMultiLangURITypeV5 }
     * 
     */
    public NonEmptyMultiLangURITypeV5 createNonEmptyMultiLangURITypeV5() {
        return new NonEmptyMultiLangURITypeV5();
    }

    /**
     * Create an instance of {@link DigitalIdentityTypeV5 }
     * 
     */
    public DigitalIdentityTypeV5 createDigitalIdentityTypeV5() {
        return new DigitalIdentityTypeV5();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PostalAddressListTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://uri.etsi.org/02231/v2#", name = "PostalAddresses")
    public JAXBElement<PostalAddressListTypeV5> createPostalAddresses(PostalAddressListTypeV5 value) {
        return new JAXBElement<>(_PostalAddresses_QNAME, PostalAddressListTypeV5.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PostalAddressTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://uri.etsi.org/02231/v2#", name = "PostalAddress")
    public JAXBElement<PostalAddressTypeV5> createPostalAddress(PostalAddressTypeV5 value) {
        return new JAXBElement<>(_PostalAddress_QNAME, PostalAddressTypeV5.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ElectronicAddressTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://uri.etsi.org/02231/v2#", name = "ElectronicAddress")
    public JAXBElement<ElectronicAddressTypeV5> createElectronicAddress(ElectronicAddressTypeV5 value) {
        return new JAXBElement<>(_ElectronicAddress_QNAME, ElectronicAddressTypeV5.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExtensionTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://uri.etsi.org/02231/v2#", name = "Extension")
    public JAXBElement<ExtensionTypeV5> createExtension(ExtensionTypeV5 value) {
        return new JAXBElement<>(_Extension_QNAME, ExtensionTypeV5.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TrustStatusListTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://uri.etsi.org/02231/v2#", name = "TrustServiceStatusList")
    public JAXBElement<TrustStatusListTypeV5> createTrustServiceStatusList(TrustStatusListTypeV5 value) {
        return new JAXBElement<>(_TrustServiceStatusList_QNAME, TrustStatusListTypeV5.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TrustServiceProviderListTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://uri.etsi.org/02231/v2#", name = "TrustServiceProviderList")
    public JAXBElement<TrustServiceProviderListTypeV5> createTrustServiceProviderList(TrustServiceProviderListTypeV5 value) {
        return new JAXBElement<>(_TrustServiceProviderList_QNAME, TrustServiceProviderListTypeV5.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TSLSchemeInformationTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://uri.etsi.org/02231/v2#", name = "SchemeInformation")
    public JAXBElement<TSLSchemeInformationTypeV5> createSchemeInformation(TSLSchemeInformationTypeV5 value) {
        return new JAXBElement<>(_SchemeInformation_QNAME, TSLSchemeInformationTypeV5.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://uri.etsi.org/02231/v2#", name = "TSLType")
    public JAXBElement<String> createTSLType(String value) {
        return new JAXBElement<>(_TSLType_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InternationalNamesTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://uri.etsi.org/02231/v2#", name = "SchemeOperatorName")
    public JAXBElement<InternationalNamesTypeV5> createSchemeOperatorName(InternationalNamesTypeV5 value) {
        return new JAXBElement<>(_SchemeOperatorName_QNAME, InternationalNamesTypeV5.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InternationalNamesTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://uri.etsi.org/02231/v2#", name = "SchemeName")
    public JAXBElement<InternationalNamesTypeV5> createSchemeName(InternationalNamesTypeV5 value) {
        return new JAXBElement<>(_SchemeName_QNAME, InternationalNamesTypeV5.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NonEmptyMultiLangURIListTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://uri.etsi.org/02231/v2#", name = "SchemeInformationURI")
    public JAXBElement<NonEmptyMultiLangURIListTypeV5> createSchemeInformationURI(NonEmptyMultiLangURIListTypeV5 value) {
        return new JAXBElement<>(_SchemeInformationURI_QNAME, NonEmptyMultiLangURIListTypeV5.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NonEmptyMultiLangURIListTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://uri.etsi.org/02231/v2#", name = "SchemeTypeCommunityRules")
    public JAXBElement<NonEmptyMultiLangURIListTypeV5> createSchemeTypeCommunityRules(NonEmptyMultiLangURIListTypeV5 value) {
        return new JAXBElement<>(_SchemeTypeCommunityRules_QNAME, NonEmptyMultiLangURIListTypeV5.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://uri.etsi.org/02231/v2#", name = "SchemeTerritory")
    public JAXBElement<String> createSchemeTerritory(String value) {
        return new JAXBElement<>(_SchemeTerritory_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PolicyOrLegalnoticeTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://uri.etsi.org/02231/v2#", name = "PolicyOrLegalNotice")
    public JAXBElement<PolicyOrLegalnoticeTypeV5> createPolicyOrLegalNotice(PolicyOrLegalnoticeTypeV5 value) {
        return new JAXBElement<>(_PolicyOrLegalNotice_QNAME, PolicyOrLegalnoticeTypeV5.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NextUpdateTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://uri.etsi.org/02231/v2#", name = "NextUpdate")
    public JAXBElement<NextUpdateTypeV5> createNextUpdate(NextUpdateTypeV5 value) {
        return new JAXBElement<>(_NextUpdate_QNAME, NextUpdateTypeV5.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OtherTSLPointersTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://uri.etsi.org/02231/v2#", name = "PointersToOtherTSL")
    public JAXBElement<OtherTSLPointersTypeV5> createPointersToOtherTSL(OtherTSLPointersTypeV5 value) {
        return new JAXBElement<>(_PointersToOtherTSL_QNAME, OtherTSLPointersTypeV5.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OtherTSLPointerTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://uri.etsi.org/02231/v2#", name = "OtherTSLPointer")
    public JAXBElement<OtherTSLPointerTypeV5> createOtherTSLPointer(OtherTSLPointerTypeV5 value) {
        return new JAXBElement<>(_OtherTSLPointer_QNAME, OtherTSLPointerTypeV5.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceDigitalIdentityListTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://uri.etsi.org/02231/v2#", name = "ServiceDigitalIdentities")
    public JAXBElement<ServiceDigitalIdentityListTypeV5> createServiceDigitalIdentities(ServiceDigitalIdentityListTypeV5 value) {
        return new JAXBElement<>(_ServiceDigitalIdentities_QNAME, ServiceDigitalIdentityListTypeV5.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdditionalInformationTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://uri.etsi.org/02231/v2#", name = "AdditionalInformation")
    public JAXBElement<AdditionalInformationTypeV5> createAdditionalInformation(AdditionalInformationTypeV5 value) {
        return new JAXBElement<>(_AdditionalInformation_QNAME, AdditionalInformationTypeV5.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NonEmptyURIListTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://uri.etsi.org/02231/v2#", name = "DistributionPoints")
    public JAXBElement<NonEmptyURIListTypeV5> createDistributionPoints(NonEmptyURIListTypeV5 value) {
        return new JAXBElement<>(_DistributionPoints_QNAME, NonEmptyURIListTypeV5.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TSPTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://uri.etsi.org/02231/v2#", name = "TrustServiceProvider")
    public JAXBElement<TSPTypeV5> createTrustServiceProvider(TSPTypeV5 value) {
        return new JAXBElement<>(_TrustServiceProvider_QNAME, TSPTypeV5.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TSPInformationTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://uri.etsi.org/02231/v2#", name = "TSPInformation")
    public JAXBElement<TSPInformationTypeV5> createTSPInformation(TSPInformationTypeV5 value) {
        return new JAXBElement<>(_TSPInformation_QNAME, TSPInformationTypeV5.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TSPServicesListTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://uri.etsi.org/02231/v2#", name = "TSPServices")
    public JAXBElement<TSPServicesListTypeV5> createTSPServices(TSPServicesListTypeV5 value) {
        return new JAXBElement<>(_TSPServices_QNAME, TSPServicesListTypeV5.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TSPServiceTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://uri.etsi.org/02231/v2#", name = "TSPService")
    public JAXBElement<TSPServiceTypeV5> createTSPService(TSPServiceTypeV5 value) {
        return new JAXBElement<>(_TSPService_QNAME, TSPServiceTypeV5.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TSPServiceInformationTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://uri.etsi.org/02231/v2#", name = "ServiceInformation")
    public JAXBElement<TSPServiceInformationTypeV5> createServiceInformation(TSPServiceInformationTypeV5 value) {
        return new JAXBElement<>(_ServiceInformation_QNAME, TSPServiceInformationTypeV5.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://uri.etsi.org/02231/v2#", name = "ServiceStatus")
    public JAXBElement<String> createServiceStatus(String value) {
        return new JAXBElement<>(_ServiceStatus_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceSupplyPointsTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://uri.etsi.org/02231/v2#", name = "ServiceSupplyPoints")
    public JAXBElement<ServiceSupplyPointsTypeV5> createServiceSupplyPoints(ServiceSupplyPointsTypeV5 value) {
        return new JAXBElement<>(_ServiceSupplyPoints_QNAME, ServiceSupplyPointsTypeV5.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://uri.etsi.org/02231/v2#", name = "ServiceTypeIdentifier")
    public JAXBElement<String> createServiceTypeIdentifier(String value) {
        return new JAXBElement<>(_ServiceTypeIdentifier_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DigitalIdentityListTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://uri.etsi.org/02231/v2#", name = "ServiceDigitalIdentity")
    public JAXBElement<DigitalIdentityListTypeV5> createServiceDigitalIdentity(DigitalIdentityListTypeV5 value) {
        return new JAXBElement<>(_ServiceDigitalIdentity_QNAME, DigitalIdentityListTypeV5.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceHistoryTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://uri.etsi.org/02231/v2#", name = "ServiceHistory")
    public JAXBElement<ServiceHistoryTypeV5> createServiceHistory(ServiceHistoryTypeV5 value) {
        return new JAXBElement<>(_ServiceHistory_QNAME, ServiceHistoryTypeV5.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceHistoryInstanceTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://uri.etsi.org/02231/v2#", name = "ServiceHistoryInstance")
    public JAXBElement<ServiceHistoryInstanceTypeV5> createServiceHistoryInstance(ServiceHistoryInstanceTypeV5 value) {
        return new JAXBElement<>(_ServiceHistoryInstance_QNAME, ServiceHistoryInstanceTypeV5.class, null, value);
    }

    // /**
    // * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
    // *
    // */
    // @XmlElementDecl(namespace = "https://uri.etsi.org/02231/v2#", name = "ExpiredCertsRevocationInfo")
    // public JAXBElement<XMLGregorianCalendar> createExpiredCertsRevocationInfo(XMLGregorianCalendar value) {
    // return new JAXBElement<XMLGregorianCalendar>(_ExpiredCertsRevocationInfo_QNAME, XMLGregorianCalendar.class, null, value);
    // }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://uri.etsi.org/02231/v2#", name = "ExpiredCertsRevocationInfo")
    public JAXBElement<String> createExpiredCertsRevocationInfo(String value) {
        return new JAXBElement<>(_ExpiredCertsRevocationInfo_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdditionalServiceInformationTypeV5 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://uri.etsi.org/02231/v2#", name = "AdditionalServiceInformation")
    public JAXBElement<AdditionalServiceInformationTypeV5> createAdditionalServiceInformation(AdditionalServiceInformationTypeV5 value) {
        return new JAXBElement<>(_AdditionalServiceInformation_QNAME, AdditionalServiceInformationTypeV5.class, null, value);
    }

}
