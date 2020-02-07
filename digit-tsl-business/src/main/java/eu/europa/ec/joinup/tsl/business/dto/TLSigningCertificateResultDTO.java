package eu.europa.ec.joinup.tsl.business.dto;

import java.util.List;

import eu.europa.ec.joinup.tsl.business.dto.tl.TLCertificate;

public class TLSigningCertificateResultDTO {

    private TLCertificate certificate;
    private List<CheckResultDTO> results;

    public TLSigningCertificateResultDTO() {
        super();
    }

    public TLSigningCertificateResultDTO(byte[] certificateEncoded, List<CheckResultDTO> results) {
        super();
        this.certificate = new TLCertificate(certificateEncoded);
        this.results = results;
    }

    public TLCertificate getCertificate() {
        return certificate;
    }

    public void setCertificate(TLCertificate certificate) {
        this.certificate = certificate;
    }

    public List<CheckResultDTO> getResults() {
        return results;
    }

    public void setResults(List<CheckResultDTO> results) {
        this.results = results;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((certificate == null) ? 0 : certificate.hashCode());
        result = prime * result + ((results == null) ? 0 : results.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TLSigningCertificateResultDTO other = (TLSigningCertificateResultDTO) obj;
        if (certificate == null) {
            if (other.certificate != null)
                return false;
        } else if (!certificate.equals(other.certificate))
            return false;
        if (results == null) {
            if (other.results != null)
                return false;
        } else if (!results.equals(other.results))
            return false;
        return true;
    }

}
