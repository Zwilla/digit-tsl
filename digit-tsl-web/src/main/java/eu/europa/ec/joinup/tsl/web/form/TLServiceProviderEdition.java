package eu.europa.ec.joinup.tsl.web.form;

import java.util.Date;

import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;

public class TLServiceProviderEdition {

    private int tlId;
    private TLServiceProvider tlServiceProviderObj;
    private String editAttribute;
    private String tlTerritoryCode;
    private Date lastEditedDate;
    private String cookie;
    private boolean checkToRun;

    public int getTlId() {
        return tlId;
    }

    public void setTlId(int tlId) {
        this.tlId = tlId;
    }

    public TLServiceProvider getTlServiceProviderObj() {
        return tlServiceProviderObj;
    }

    public void setTlServiceProviderObj(TLServiceProvider tlServiceProviderObj) {
        this.tlServiceProviderObj = tlServiceProviderObj;
    }

    public String getTlTerritoryCode() {
        return tlTerritoryCode;
    }

    public void setTlTerritoryCode(String tlTerritoryCode) {
        this.tlTerritoryCode = tlTerritoryCode;
    }

    public String getEditAttribute() {
        return editAttribute;
    }

    public void setEditAttribute(String editAttribute) {
        this.editAttribute = editAttribute;
    }

    public Date getLastEditedDate() {
        return lastEditedDate;
    }

    public void setLastEditedDate(Date lastEditedDate) {
        this.lastEditedDate = lastEditedDate;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public boolean isCheckToRun() {
        return checkToRun;
    }

    public void setCheckToRun(boolean checkToRun) {
        this.checkToRun = checkToRun;
    }

}
