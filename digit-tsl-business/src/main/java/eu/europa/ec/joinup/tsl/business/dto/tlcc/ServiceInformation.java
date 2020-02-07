package eu.europa.ec.joinup.tsl.business.dto.tlcc;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java pour anonymous complex type.
 *
 * <p>
 * Le fragment de schema suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}ServiceTypeIdentifier" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}ServiceName" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}ServiceDigitalIdentity" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}ServiceStatus" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}StatusStartingTime" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}SchemeServiceDefinitionURI" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}ServiceSupplyPoints" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}TSPServiceDefinitionURI" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}ServiceInformationExtensions" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}Check" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "serviceTypeIdentifier", "serviceName", "serviceDigitalIdentity", "serviceStatus", "statusStartingTime", "schemeServiceDefinitionURI", "serviceSupplyPoints",
        "tspServiceDefinitionURI", "serviceInformationExtensions", "check" })
@XmlRootElement(name = "ServiceInformation", namespace = "http://www.etsi.org/19162/conformanceChecker")
public class ServiceInformation {

    @XmlElement(name = "ServiceTypeIdentifier", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected ServiceTypeIdentifier serviceTypeIdentifier;
    @XmlElement(name = "ServiceName", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected ServiceName serviceName;
    @XmlElement(name = "ServiceDigitalIdentity", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected ServiceDigitalIdentity serviceDigitalIdentity;
    @XmlElement(name = "ServiceStatus", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected ServiceStatus serviceStatus;
    @XmlElement(name = "StatusStartingTime", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected StatusStartingTime statusStartingTime;
    @XmlElement(name = "SchemeServiceDefinitionURI", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected SchemeServiceDefinitionURI schemeServiceDefinitionURI;
    @XmlElement(name = "ServiceSupplyPoints", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected ServiceSupplyPoints serviceSupplyPoints;
    @XmlElement(name = "TSPServiceDefinitionURI", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected TSPServiceDefinitionURI tspServiceDefinitionURI;
    @XmlElement(name = "ServiceInformationExtensions", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected ServiceInformationExtensions serviceInformationExtensions;
    @XmlElement(name = "Check", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected List<Check> check;

    public ServiceTypeIdentifier getServiceTypeIdentifier() {
        return serviceTypeIdentifier;
    }

    public void setServiceTypeIdentifier(ServiceTypeIdentifier value) {
        this.serviceTypeIdentifier = value;
    }

    public ServiceName getServiceName() {
        return serviceName;
    }

    public void setServiceName(ServiceName value) {
        this.serviceName = value;
    }

    public ServiceDigitalIdentity getServiceDigitalIdentity() {
        return serviceDigitalIdentity;
    }

    public void setServiceDigitalIdentity(ServiceDigitalIdentity value) {
        this.serviceDigitalIdentity = value;
    }

    public ServiceStatus getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(ServiceStatus value) {
        this.serviceStatus = value;
    }

    public StatusStartingTime getStatusStartingTime() {
        return statusStartingTime;
    }

    public void setStatusStartingTime(StatusStartingTime value) {
        this.statusStartingTime = value;
    }

    public SchemeServiceDefinitionURI getSchemeServiceDefinitionURI() {
        return schemeServiceDefinitionURI;
    }

    public void setSchemeServiceDefinitionURI(SchemeServiceDefinitionURI value) {
        this.schemeServiceDefinitionURI = value;
    }

    public ServiceSupplyPoints getServiceSupplyPoints() {
        return serviceSupplyPoints;
    }

    public void setServiceSupplyPoints(ServiceSupplyPoints value) {
        this.serviceSupplyPoints = value;
    }

    public TSPServiceDefinitionURI getTSPServiceDefinitionURI() {
        return tspServiceDefinitionURI;
    }

    public void setTSPServiceDefinitionURI(TSPServiceDefinitionURI value) {
        this.tspServiceDefinitionURI = value;
    }

    public ServiceInformationExtensions getServiceInformationExtensions() {
        return serviceInformationExtensions;
    }

    public void setServiceInformationExtensions(ServiceInformationExtensions value) {
        this.serviceInformationExtensions = value;
    }

    public List<Check> getCheck() {
        if (check == null) {
            check = new ArrayList<>();
        }
        return this.check;
    }

}
