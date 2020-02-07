package eu.europa.ec.joinup.tsl.business.dto;

public class Load {

    private int tlId;
    private boolean isNew;

    public Load() {
        setNew(false);
    }

    public int getTlId() {
        return tlId;
    }

    public void setTlId(int tlId) {
        this.tlId = tlId;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

}
