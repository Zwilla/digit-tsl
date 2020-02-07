package eu.europa.ec.joinup.tsl.checker.dto;

import java.security.cert.X509Certificate;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class TLCCRequestDTO {

    private int tlId;
    private String tlXmlPath;
    private String lotlPath;
    private int tspIndex;
    private int serviceIndex;

    @JsonSerialize(using = X509JacksonSerializer.class)
    private List<X509Certificate> certificates;

    public int getTlId() {
        return tlId;
    }

    public String getTlIdStr() {
        return Integer.toString(tlId);
    }

    public void setTlId(int tlId) {
        this.tlId = tlId;
    }

    public String getTlXmlPath() {
        return tlXmlPath;
    }

    public void setTlXmlPath(String tlXmlPath) {
        this.tlXmlPath = tlXmlPath;
    }

    public String getLotlPath() {
        return lotlPath;
    }

    public void setLotlPath(String lotlPath) {
        this.lotlPath = lotlPath;
    }

    public int getTspIndex() {
        return tspIndex;
    }

    public void setTspIndex(int tspIndex) {
        this.tspIndex = tspIndex;
    }

    public int getServiceIndex() {
        return serviceIndex;
    }

    public void setServiceIndex(int serviceIndex) {
        this.serviceIndex = serviceIndex;
    }

    public List<X509Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<X509Certificate> certificates) {
        this.certificates = certificates;
    }

    @Override
    public String toString() {
        return "TLCCRequestDTO [tlId=" + tlId + ", tlXmlPath=" + tlXmlPath + ", lotlPath=" + lotlPath + ", tspIndex=" + tspIndex + ", serviceIndex=" + serviceIndex + "]";
    }

}
