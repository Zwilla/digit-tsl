package eu.europa.ec.joinup.tsl.web.form;

import java.util.Date;

import eu.europa.ec.joinup.tsl.business.dto.tl.TLSchemeInformation;

public class TLInformationEdition {

    private int tlId;
    private TLSchemeInformation tlSchemeInfoObj;
    private String editAttribute;
    private Date lastEditedDate;
    private String cookie;
    private String tslIdentifier;
    private boolean checkToRun;

    public int getTlId() {
        return tlId;
    }

    public void setTlId(int tlId) {
        this.tlId = tlId;
    }

    public TLSchemeInformation getTlSchemeInfoObj() {
        return tlSchemeInfoObj;
    }

    public void setTlSchemeInfoObj(TLSchemeInformation tlSchemeInfoObj) {
        this.tlSchemeInfoObj = tlSchemeInfoObj;
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

    public String getTslIdentifier() {
        return tslIdentifier;
    }

    public void setTslIdentifier(String tslIdentifier) {
        this.tslIdentifier = tslIdentifier;
    }

    public boolean isCheckToRun() {
        return checkToRun;
    }

    public void setCheckToRun(boolean checkToRun) {
        this.checkToRun = checkToRun;
    }
}
