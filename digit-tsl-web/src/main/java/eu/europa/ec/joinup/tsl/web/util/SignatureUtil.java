package eu.europa.ec.joinup.tsl.web.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.crypto.dsig.CanonicalizationMethod;

import org.apache.commons.codec.binary.Base64;

import eu.europa.ec.joinup.tsl.business.util.CertificateTokenUtils;
import eu.europa.ec.joinup.tsl.web.form.nexu.GetCertificateResponse;
import eu.europa.ec.joinup.tsl.web.form.nexu.SignatureRequest;
import eu.europa.ec.joinup.tsl.web.form.nexu.ToBeSigned;
import eu.europa.ec.joinup.tsl.web.form.nexu.TokenId;
import eu.europa.esig.dss.DSSDocument;
import eu.europa.esig.dss.DigestAlgorithm;
import eu.europa.esig.dss.SignatureLevel;
import eu.europa.esig.dss.SignaturePackaging;
import eu.europa.esig.dss.x509.CertificateToken;
import eu.europa.esig.dss.xades.DSSReference;
import eu.europa.esig.dss.xades.DSSTransform;
import eu.europa.esig.dss.xades.XAdESSignatureParameters;
import eu.europa.esig.dss.xades.signature.XAdESService;

public class SignatureUtil {

    public static XAdESSignatureParameters getSignatureParams(GetCertificateResponse certificateResponse, DSSDocument doc) {
        XAdESSignatureParameters params = new XAdESSignatureParameters();

        CertificateToken signingCertificate = CertificateTokenUtils.loadCertificate(certificateResponse.getCertificate());

        params.setSignatureLevel(SignatureLevel.XAdES_BASELINE_B);
        params.setSignaturePackaging(SignaturePackaging.ENVELOPED);
        assert signingCertificate != null;
        params.setSigningCertificate(signingCertificate);
        params.bLevel().setSigningDate(new Date());
        params.setDigestAlgorithm(DigestAlgorithm.SHA256);
        // SigningCertificateV2 (false => old standard 119612 / true => new standard 319132)
        params.setEn319132(false);

        final List<DSSReference> references = new ArrayList<>();

        DSSReference dssReference = new DSSReference();
        dssReference.setId("xml_ref_id");
        dssReference.setUri("");
        dssReference.setContents(doc);
        dssReference.setDigestMethodAlgorithm(params.getDigestAlgorithm());

        final List<DSSTransform> transforms = new ArrayList<>();

        DSSTransform dssTransform = new DSSTransform();
        dssTransform.setAlgorithm(CanonicalizationMethod.ENVELOPED);
        transforms.add(dssTransform);

        dssTransform = new DSSTransform();
        dssTransform.setAlgorithm(CanonicalizationMethod.EXCLUSIVE);
        transforms.add(dssTransform);

        dssReference.setTransforms(transforms);
        references.add(dssReference);

        params.setReferences(references);
        return params;
    }

    public static SignatureRequest getToBeSigned(GetCertificateResponse certificateResponse, XAdESService signatureService, DSSDocument doc, XAdESSignatureParameters params) {
        SignatureRequest tbs = new SignatureRequest();
        eu.europa.esig.dss.ToBeSigned dssTbs = signatureService.getDataToSign(doc, params);

        ToBeSigned nexuTbs = new ToBeSigned();
        nexuTbs.setBytes(Base64.encodeBase64String(dssTbs.getBytes()));
        tbs.setToBeSigned(nexuTbs);
        tbs.setKeyId(certificateResponse.getKeyId());
        TokenId tokenId = new TokenId();
        tokenId.setId(certificateResponse.getTokenId().getId());
        tbs.setTokenId(tokenId);
        tbs.setDigestAlgorithm(DigestAlgorithm.SHA256.toString());
        return tbs;
    }

}
