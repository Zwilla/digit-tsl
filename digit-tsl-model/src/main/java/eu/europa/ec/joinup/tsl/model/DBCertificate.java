package eu.europa.ec.joinup.tsl.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import eu.europa.ec.joinup.tsl.model.enums.TLType;

@Entity
@Table(name = "TL_CERTIFICATE")
public class DBCertificate {

    @Id
    @GeneratedValue
    @Column(name = "CERTIFICATE_ID", nullable = false, updatable = false)
    private int id;

    @Column(name = "COUNTRY_CODE", nullable = false)
    private String countryCode;

    @Column(name = "SUBJECT_NAME_B64", nullable = false, length = 1000)
    private String subjectName;

    @Column(name = "NOT_BEFORE", nullable = false)
    private Date notBefore;

    @Column(name = "NOT_AFTER", nullable = false)
    private Date notAfter;

    @Column(name = "BASE_64", nullable = false, length = 6000)
    private String base64;

    @Column(name = "TL_TYPE", nullable = false)
    private TLType tlType;

    @Column(name = "SKI", nullable = false)
    private byte[] ski;

    @ManyToOne(fetch = FetchType.LAZY)
    private DBService service;

    public DBCertificate() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Date getNotBefore() {
        return notBefore;
    }

    public void setNotBefore(Date notBefore) {
        this.notBefore = notBefore;
    }

    public Date getNotAfter() {
        return notAfter;
    }

    public void setNotAfter(Date notAfter) {
        this.notAfter = notAfter;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public TLType getTlType() {
        return tlType;
    }

    public void setTlType(TLType tlType) {
        this.tlType = tlType;
    }

    public DBService getService() {
        return service;
    }

    public void setService(DBService service) {
        this.service = service;
    }

    public byte[] getSki() {
        return ski;
    }

    public void setSki(byte[] ski) {
        this.ski = ski;
    }

}
