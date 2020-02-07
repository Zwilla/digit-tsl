package eu.europa.ec.joinup.tsl.business.dto;

/**
 * DTO to Request TLCC service
 */
public class TLCCRequestDTO {

    private int tlId;
    private String tlXmlPath;
    private String lotlPath;
    private int tspIndex;
    private int serviceIndex;

    public TLCCRequestDTO(int tlId) {
        this.tlId = tlId;
    }

    public TLCCRequestDTO(int tlId, int tspIndex) {
        this.tlId = tlId;
        this.tspIndex = tspIndex;
    }

    public TLCCRequestDTO(int tlId, int tspIndex, int serviceIndex) {
        this.tlId = tlId;
        this.tspIndex = tspIndex;
        this.serviceIndex = serviceIndex;
    }

    public int getTlId() {
        return tlId;
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

}
