package eu.europa.ec.joinup.tsl.business.dto.tl.approachbreak;

import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.x500.style.BCStyle;

import eu.europa.ec.joinup.tsl.business.util.CertificateTokenUtils;
import eu.europa.ec.joinup.tsl.business.util.DateUtils;
import eu.europa.ec.joinup.tsl.model.DBCertificate;
import eu.europa.ec.joinup.tsl.model.enums.TLType;
import eu.europa.esig.dss.DSSASN1Utils;
import eu.europa.esig.dss.DSSException;
import eu.europa.esig.dss.x509.CertificateToken;

public class CertificateElement {

    private int index;

    private String countryCode;

    private String subjectName;

    private Date notBefore;

    private Date notAfter;

    private String base64;

    private boolean breakDay;

    private int expireIn;

    private TLType tlType;

    private CertificateToken token;

    public CertificateElement(DBCertificate dbCertificate, Date checkDate) {
        super();
        countryCode = dbCertificate.getCountryCode();
        subjectName = new String(Base64.decodeBase64(dbCertificate.getSubjectName()));
        notBefore = dbCertificate.getNotBefore();
        notAfter = dbCertificate.getNotAfter();
        base64 = dbCertificate.getBase64();
        tlType = dbCertificate.getTlType();
        breakDay = false;
        expireIn = DateUtils.getDifferenceBetweenDatesInDays(notAfter, checkDate);
        try {
            token = CertificateTokenUtils.loadCertificate(dbCertificate.getBase64());
        } catch (DSSException e) {
            // Could not parse certificate
            token = null;
        }
    }

    public CertificateElement(CertificateElement certificate) {
        super();
        index = certificate.getIndex();
        countryCode = certificate.getCountryCode();
        subjectName = certificate.getSubjectName();
        notBefore = certificate.getNotBefore();
        notAfter = certificate.getNotAfter();
        base64 = certificate.getBase64();
        tlType = certificate.getTlType();
        breakDay = false;
        expireIn = certificate.getExpirationIn();
        token = certificate.getToken();
    }

    public String getSubjectShortName() {
        if (token == null) {
            return subjectName;
        } else {
            String certSubjectShortName = DSSASN1Utils.extractAttributeFromX500Principal(BCStyle.CN, token.getSubjectX500Principal());
            if (certSubjectShortName == null) {
                certSubjectShortName = subjectName;
            }
            return certSubjectShortName;
        }
    }

    public boolean isExpired() {
        return expireIn <= 0;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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

    public boolean isBreakDay() {
        return breakDay;
    }

    public void setBreakDay(boolean breakDay) {
        this.breakDay = breakDay;
    }

    public int getExpirationIn() {
        return expireIn;
    }

    public void setExpirationIn(int expirationIn) {
        expireIn = expirationIn;
    }

    public TLType getTlType() {
        return tlType;
    }

    public void setTlType(TLType tlType) {
        this.tlType = tlType;
    }

    public CertificateToken getToken() {
        return token;
    }

    public void setToken(CertificateToken token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "CertificateElement [countryCode=" + countryCode + ", isExpired=" + isExpired() + ", expirationDay=" + expireIn + ", breakDay=" + breakDay + ", notBefore=" + notBefore + ", notAfter="
                + notAfter + ", tlType=" + tlType + ", subjectName=" + subjectName + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((base64 == null) ? 0 : base64.hashCode());
        result = (prime * result) + (breakDay ? 1231 : 1237);
        result = (prime * result) + ((countryCode == null) ? 0 : countryCode.hashCode());
        result = (prime * result) + expireIn;
        result = (prime * result) + index;
        result = (prime * result) + ((notAfter == null) ? 0 : notAfter.hashCode());
        result = (prime * result) + ((notBefore == null) ? 0 : notBefore.hashCode());
        result = (prime * result) + ((subjectName == null) ? 0 : subjectName.hashCode());
        result = (prime * result) + ((tlType == null) ? 0 : tlType.hashCode());
        result = (prime * result) + ((token == null) ? 0 : token.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CertificateElement other = (CertificateElement) obj;
        if (base64 == null) {
            if (other.base64 != null) {
                return false;
            }
        } else if (!base64.equals(other.base64)) {
            return false;
        }
        if (breakDay != other.breakDay) {
            return false;
        }
        if (countryCode == null) {
            if (other.countryCode != null) {
                return false;
            }
        } else if (!countryCode.equals(other.countryCode)) {
            return false;
        }
        if (expireIn != other.expireIn) {
            return false;
        }
        if (index != other.index) {
            return false;
        }
        if (notAfter == null) {
            if (other.notAfter != null) {
                return false;
            }
        } else if (!notAfter.equals(other.notAfter)) {
            return false;
        }
        if (notBefore == null) {
            if (other.notBefore != null) {
                return false;
            }
        } else if (!notBefore.equals(other.notBefore)) {
            return false;
        }
        if (subjectName == null) {
            if (other.subjectName != null) {
                return false;
            }
        } else if (!subjectName.equals(other.subjectName)) {
            return false;
        }
        if (tlType != other.tlType) {
            return false;
        }
        if (token == null) {
            if (other.token != null) {
                return false;
            }
        } else if (!token.equals(other.token)) {
            return false;
        }
        return true;
    }

}
