package eu.europa.ec.joinup.tsl.business.dto.tl;

public class TLDistributionPoint extends TLGeneric {

    public TLDistributionPoint() {
    }

    public TLDistributionPoint(int iddb, String location, String uri) {
        super(iddb, location, "", uri);
        this.setValue(uri);
    }

}
