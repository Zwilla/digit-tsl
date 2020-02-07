package eu.europa.ec.joinup.tsl.business.util;

import java.io.IOException;

import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DLSequence;
import org.junit.Test;

import eu.europa.esig.dss.DSSASN1Utils;
import eu.europa.esig.dss.DSSUtils;
import eu.europa.esig.dss.DigestAlgorithm;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.x509.CertificateToken;

public class CertSKIComputTest {

    @SuppressWarnings("unused")
    @Test
    public void test() throws IOException {
        CertificateToken cyToken = DSSUtils.loadCertificateFromBase64EncodedString(
                "MIIDkTCCAnmgAwIBAgIFEkAyZ6kwDQYJKoZIhvcNAQELBQAwWDELMAkGA1UEBhMCQ1kxMDAuBgNVBAoMJ0RlcGFydG1lbnQgb2YgRWxlY3Ryb25pYyBDb21tdW5pY2F0aW9uczEXMBUGA1UEAwwOQ1ktVFNMIFNpZ25lcjEwHhcNMTcxMjAxMDAwMDAwWhcNMjcxMjAxMDAwMDAwWjBYMQswCQYDVQQGEwJDWTEwMC4GA1UECgwnRGVwYXJ0bWVudCBvZiBFbGVjdHJvbmljIENvbW11bmljYXRpb25zMRcwFQYDVQQDDA5DWS1UU0wgU2lnbmVyMTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAKAsRMYRj0v937oGqxRQrjvWBFncZ+jEq/qyUxgiQB0DGWnOEN9QbtvdIu/swVSCh3FZc7vrvI2aTt0NDQ2kZ8ohBpcj+qQb4xUFU5PXhUdIiGsqGO/+0hYeteJZyhHZ1VxL97U0LzmqkmfYumNmJUdDOoqfmzkatHrORvooSlK+SYhXHTrCxXEU0e3xIwnlXcyaVXRBbDdFsGY3p5QrhTBQ5Cd+FxZMhHLhxnUetPnqN9GOJXgghZAy+72c4zmc7X8gshaxbvO7MEvrBgdbaZcjHod7XiHowzCTxLFjDR9eHXrLvmukYcY9qeb5ieJ37VXf9JUbO9q+aFQu5wJ61Z8CAwEAAaNiMGAwHQYDVR0OBBYEFDPKoJc8X/T59hCvhxlDqw2JXj1uMAkGA1UdEwQCMAAwDgYDVR0PAQH/BAQDAgbAMBEGA1UdIAQKMAgwBgYEVR0gADARBgNVHSUECjAIBgYEAJE3AwAwDQYJKoZIhvcNAQELBQADggEBAINBgvYZfI1L6iY0i1OnVoQR4Nje/FuCzW9tppUCC2h9T9x9sClph6o8hceaiBUDdobX9/BIxc15o59q7wbmyGtYnNpZG8nctj/90uciXvUdOCA7PQeX5/cMUVU/Yscg+Pv1ydNyu/7tZtdDfKDyc+Lux9CDGpvYMDDRqX048NsYTB4shry0a7EzhOyD8jIYjY2xU7AYIvGQI1w/OZ2wqEAM7KhhEHz915wdhkv9DBb/CB14mONa1gMWTL27e7GnXZ+mWZghF1HyDjbG3Pr6jFzEgvxMTkGGLpvLOFm7ULwqYbP+ORd7l6exZ+XvzoN5ilPRZmnx/6eHdhutre/BqmE=");

        String cyski = Utils.toHex(DSSASN1Utils.getSki(cyToken, false));
        String cycomputeSKI = Utils.toHex(computeSKIFromOctets(cyToken));
        String cycomputeSKIBytes = Utils.toHex(computeSKIFromBytes(cyToken));

        System.out.println("CY SKI " + cyski);
        System.out.println("CY METHOD 1 " + cycomputeSKI);
        System.out.println("CY METHOD 2 " + cycomputeSKIBytes);

        String certF = "041433caa0973c5ff4f9f610af871943ab0d895e3d6e";
        String m1 = "04140dd51350647ed4f20237dbca5a9abf112ee9ac37";
        String m2 = "04084a9abf112ee9ac37";

    }

    private byte[] computeSKIFromOctets(CertificateToken certificate) throws IOException {
        DLSequence seq = (DLSequence) DERSequence.fromByteArray(certificate.getPublicKey().getEncoded());
        DERBitString item = (DERBitString) seq.getObjectAt(1);
        return DSSUtils.digest(DigestAlgorithm.SHA1, item.getOctets());
    }

    private byte[] computeSKIFromBytes(CertificateToken certificate) {
        // DLSequence seq = (DLSequence) DERSequence.fromByteArray(certificate.getPublicKey().getEncoded());
        // DERBitString item = (DERBitString) seq.getObjectAt(1);
        return DSSUtils.digest(DigestAlgorithm.SHA1, certificate.getPublicKey().getEncoded());
    }

}
