package eu.europa.ec.joinup.tsl.web.form;

import java.util.Date;

public class TLNewStatus {

    private int tlId;
    private String tspId;
    private String serviceId;
    private String cookie;
    private Date lastEditedDate;
    private boolean checkToRun;

    public TLNewStatus() {
        super();
    }

    public int getTlId() {
        return tlId;
    }

    public void setTlId(int tlId) {
        this.tlId = tlId;
    }

    public String getTspId() {
        return tspId;
    }

    public void setTspId(String tspId) {
        this.tspId = tspId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public Date getLastEditedDate() {
        return lastEditedDate;
    }

    public void setLastEditedDate(Date lastEditedDate) {
        this.lastEditedDate = lastEditedDate;
    }

    public boolean isCheckToRun() {
        return checkToRun;
    }

    public void setCheckToRun(boolean checkToRun) {
        this.checkToRun = checkToRun;
    }

}