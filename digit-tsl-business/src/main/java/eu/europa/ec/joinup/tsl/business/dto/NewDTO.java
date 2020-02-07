package eu.europa.ec.joinup.tsl.business.dto;

import java.util.Date;

public class NewDTO {

    private Date date;
    private String countryCode;
    private String countryName;
    private String infos;
    private String tlInfo;
    private int tlId;

    public NewDTO() {
        super();
    }

    public NewDTO(Date date, String countryCode, String countryName, String infos, String tlInfo, int tlId) {
        super();
        this.date = date;
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.infos = infos;
        this.tlInfo = tlInfo;
        this.tlId = tlId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getInfos() {
        return infos;
    }

    public void setInfos(String infos) {
        this.infos = infos;
    }

    public String getTlInfo() {
        return tlInfo;
    }

    public void setTlInfo(String tlInfo) {
        this.tlInfo = tlInfo;
    }

    public int getTlId() {
        return tlId;
    }

    public void setTlId(int tlId) {
        this.tlId = tlId;
    }
}
