package eu.europa.ec.joinup.tsl.web.form;

import java.util.Date;

import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;

public class TLPointerEdition {

    private int tlId;
    private TLPointersToOtherTSL tlPointerObj;
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

    public TLPointersToOtherTSL getTlPointerObj() {
        return tlPointerObj;
    }

    public void setTlPointerObj(TLPointersToOtherTSL tlPointerObj) {
        this.tlPointerObj = tlPointerObj;
    }

    public String getEditAttribute() {
        return editAttribute;
    }

    public void setEditAttribute(String editAttribute) {
        this.editAttribute = editAttribute;
    }

    public String getTlTerritoryCode() {
        return tlTerritoryCode;
    }

    public void setTlTerritoryCode(String tlTerritoryCode) {
        this.tlTerritoryCode = tlTerritoryCode;
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

    public boolean objectCorrect() {
        if ((this.getTlPointerObj() == null) || (this.getTlPointerObj().getMimeType() == null) || (this.getTlPointerObj().getSchemeTerritory() == null)
                || (this.getTlPointerObj().getSchemeTerritory() == null)) {
            return false;
        }
        return true;
    }

}
