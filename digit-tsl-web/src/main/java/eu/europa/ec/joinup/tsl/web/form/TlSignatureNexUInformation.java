package eu.europa.ec.joinup.tsl.web.form;

import eu.europa.ec.joinup.tsl.web.form.nexu.GetCertificateResponse;

public class TlSignatureNexUInformation {

    private int tlId;
    private byte[] signatureValue;
    private GetCertificateResponse certificateResponse;

    public TlSignatureNexUInformation() {
    }

    public int getTlId() {
        return tlId;
    }

    public void setTlId(int tlId) {
        this.tlId = tlId;
    }

    public GetCertificateResponse getCertificateResponse() {
        return certificateResponse;
    }

    public void setCertificateResponse(GetCertificateResponse certificateResponse) {
        this.certificateResponse = certificateResponse;
    }

    public byte[] getSignatureValue() {
        return signatureValue;
    }

    public void setSignatureValue(byte[] signatureValue) {
        this.signatureValue = signatureValue;
    }

}
