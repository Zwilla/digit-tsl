package eu.europa.ec.joinup.tsl.business.dto.tl.approachbreak;

import java.util.ArrayList;
import java.util.List;

/**
 * Extends BreakElement with a list of certificates affected and verifiable flag
 */
public class CertificateBreakElement extends BreakElement {

    private boolean verifiable;

    private List<CertificateElement> certificatesAffected;

    public CertificateBreakElement() {
        super();
        verifiable = true;
        certificatesAffected = new ArrayList<>();
    }

    public boolean isVerifiable() {
        return verifiable;
    }

    public void setVerifiable(boolean verifiable) {
        this.verifiable = verifiable;
    }

    public List<CertificateElement> getCertificatesAffected() {
        return certificatesAffected;
    }

    public void setCertificatesAffected(List<CertificateElement> certificatesAffected) {
        this.certificatesAffected = certificatesAffected;
    }

}
