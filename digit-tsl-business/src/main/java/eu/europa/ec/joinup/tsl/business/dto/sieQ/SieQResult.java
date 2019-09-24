package eu.europa.ec.joinup.tsl.business.dto.sieQ;

import java.security.cert.CertificateEncodingException;
import java.util.ArrayList;
import java.util.List;

import eu.europa.ec.joinup.tsl.business.dto.tl.TLCertificate;
import eu.europa.esig.dss.x509.CertificateToken;

public class SieQResult {

    private TLCertificate certificate;
    private List<SieQServiceEntry> services;

    public SieQResult() {
        super();
        this.services = new ArrayList<>();
    }

    public TLCertificate getCertificate() {
        return certificate;
    }

    public void setCertificate(TLCertificate certificate) {
        this.certificate = certificate;
    }

    public List<SieQServiceEntry> getServices() {
        if (services == null) {
            services = new ArrayList<>();
        }
        return services;
    }

    public void setServices(List<SieQServiceEntry> services) {
        this.services = services;
    }

    public void setCertificate(CertificateToken certificate) {
        try {
            this.certificate = new TLCertificate(certificate.getCertificate().getEncoded());
        } catch (NullPointerException | CertificateEncodingException e) {
            this.certificate = null;
        }
    }

}
