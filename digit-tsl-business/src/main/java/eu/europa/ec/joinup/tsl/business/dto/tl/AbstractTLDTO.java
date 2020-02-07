package eu.europa.ec.joinup.tsl.business.dto.tl;

/**
 * This class is used to identify all DTO in the front-end
 */
public abstract class AbstractTLDTO {

    /**
     * Id of the linked TSL in the DB
     */
    private int tlId;

    /**
     * Location of the object in the TSL (used to compute differences)
     */
    private String id;

    public AbstractTLDTO() {
    }

    protected AbstractTLDTO(int tlId, String location) {
        this.tlId = tlId;
        this.id = location;
    }

    public int getTlId() {
        return tlId;
    }

    public void setTlId(int tlId) {
        this.tlId = tlId;
    }

    public String getId() {
        return id;
    }

    public void setId(String location) {
        this.id = location;
    }

}
