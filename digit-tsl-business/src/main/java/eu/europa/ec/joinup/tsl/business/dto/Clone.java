package eu.europa.ec.joinup.tsl.business.dto;

import java.io.Serializable;

public class Clone implements Serializable {

    private static final long serialVersionUID = -3775159015988230085L;

    private String cookieId;
    private String territory;

    public Clone() {
    }

    public String getCookieId() {
        return cookieId;
    }

    public void setCookieId(String cookieId) {
        this.cookieId = cookieId;
    }

    public String getTerritory() {
        return territory;
    }

    public void setTerritory(String territory) {
        this.territory = territory;
    }

}
