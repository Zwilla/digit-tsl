package eu.europa.ec.joinup.tsl.web.form.nexu;

public class GetCertificateResponse {

    private TokenId tokenId;

    private String keyId;

    private String certificate;

    public TokenId getTokenId() {
        return tokenId;
    }

    public void setTokenId(TokenId tokenId) {
        this.tokenId = tokenId;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

}