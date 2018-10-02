package eu.europa.ec.joinup.tsl.web.form.nexu;

public class SignatureRequest {

	private TokenId tokenId;
	private ToBeSigned toBeSigned;
	private String digestAlgorithm;
	private String keyId;

	public SignatureRequest() {
		super();
	}

	public TokenId getTokenId() {
		return tokenId;
	}

	public void setTokenId(TokenId tokenId) {
		this.tokenId = tokenId;
	}

	public ToBeSigned getToBeSigned() {
		return toBeSigned;
	}

	public void setToBeSigned(ToBeSigned toBeSigned) {
		this.toBeSigned = toBeSigned;
	}

	public String getDigestAlgorithm() {
		return digestAlgorithm;
	}

	public void setDigestAlgorithm(String digestAlgorithm) {
		this.digestAlgorithm = digestAlgorithm;
	}

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

}