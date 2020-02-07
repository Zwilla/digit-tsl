package eu.europa.ec.joinup.tsl.web.form;

import java.util.Date;
import java.util.List;

import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceHistory;

public class TLHistoryEdition {

    private int tlId;
    private TLServiceHistory tlHistoryObj;
    private String tlTerritoryCode;
    private List<Integer> parentIndex;
    private Date lastEditedDate;
    private String cookie;
    private boolean checkToRun;

    public int getTlId() {
        return tlId;
    }

    public void setTlId(int tlId) {
        this.tlId = tlId;
    }

    public TLServiceHistory getTlHistoryObj() {
        return tlHistoryObj;
    }

    public void setTlHistoryObj(TLServiceHistory tlHistoryObj) {
        this.tlHistoryObj = tlHistoryObj;
    }

    public String getTlTerritoryCode() {
        return tlTerritoryCode;
    }

    public void setTlTerritoryCode(String tlTerritoryCode) {
        this.tlTerritoryCode = tlTerritoryCode;
    }

    public List<Integer> getParentIndex() {
        return parentIndex;
    }

    public void setParentIndex(List<Integer> parentIndex) {
        this.parentIndex = parentIndex;
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
