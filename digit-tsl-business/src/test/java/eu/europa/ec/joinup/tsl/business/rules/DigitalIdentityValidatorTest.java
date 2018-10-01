/*******************************************************************************
 * DIGIT-TSL - Trusted List Manager
 * Copyright (C) 2018 European Commission, provided under the CEF E-Signature programme
 * 
 * This file is part of the "DIGIT-TSL - Trusted List Manager" project.
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
package eu.europa.ec.joinup.tsl.business.rules;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.esig.dss.DSSUtils;
import eu.europa.esig.dss.x509.CertificateToken;

public class DigitalIdentityValidatorTest extends AbstractSpringTest {

    @Value("${notification.shift.period}")
    private int notificationShiftPeriod;

    @Autowired
    private DigitalIdentityValidator validator;

    @Test
    public void isBase64Certificate() {
        String base64Cert = "MIIDlDCCAnygAwIBAgIQWAsFbFMk27JQVxhf+eWmUDANBgkqhkiG9w0BAQUFADAnMQswCQYDVQQGEwJCRTEYMBYGA1UEAxMPQmVsZ2l1bSBSb290IENBMB4XDTAzMDEyNjIzMDAwMFoXDTE0MDEyNjIzMDAwMFowJzELMAkGA1UEBhMCQkUxGDAWBgNVBAMTD0JlbGdpdW0gUm9vdCBDQTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAMihcekcRkJ5eHFvna6pqKsot03HIOswkVp19eLSz8hMFJhCWK3HEcVAQGpa+XQSJ4fpnOVxTiIs0RIYqjBeoiG52bv/9nTrMQHnO35YD5EWTXaJqAFPrSJmcPpLHZXBMFjqvNll2Jq0iOtJRlLf0lMVdssUXRlJsW9q09P9vMIt7EU/CT9YvvzU7wCMgTVyv/cY6pZifSsofxVsY9LKyn0FrMhtB20yvmi4BUCuVJhWPmbxMOjvxKuTXgfeMo8SdKpbNCNUwOpszv42kqgJF+qhLc9s44Qd3ocuMws8dOIhUDiVLlzg5cYx+dtA+mqhpIqTm6chBocdJ9PEoclMsG8CAwEAAaOBuzCBuDAOBgNVHQ8BAf8EBAMCAQYwDwYDVR0TAQH/BAUwAwEB/zBCBgNVHSAEOzA5MDcGBWA4AQEBMC4wLAYIKwYBBQUHAgEWIGh0dHA6Ly9yZXBvc2l0b3J5LmVpZC5iZWxnaXVtLmJlMB0GA1UdDgQWBBQQ8AxWm2HqVzq2NZdtn925FI7b5jARBglghkgBhvhCAQEEBAMCAAcwHwYDVR0jBBgwFoAUEPAMVpth6lc6tjWXbZ/duRSO2+YwDQYJKoZIhvcNAQEFBQADggEBAMhtIlGKYfgPlm7VILKB+MbcoxYA2s1q52sq+llIp0xJN9dzoWoBZV4yveeX09AuPHPTjHuD79ZCwT+oqV0PN7p20kC9zC0/00RBSZz9Wyn0AiMiW3Ebv1jZKE4tRfTa57VjRUQRDSp/M382SbTObqkCMa5c/ciJv0J71/Fg8teH9lcuen5qE4Ad3OPQYx49cTGxYNSeCMqr8JTHSHVUgfMbrXec6LKP24OsjzRr6L/D2fVDw2RV6xq9NoY2uiGMlxoh1OotO6y67Kcdq765Sps1LxxcHVGnH1TtEpf/8m6HfUbJdNbv6z195lluBpQE5KJVhzgoaiJe4r50ErAEQyo=";
        byte[] certificateBinaries = Base64.decodeBase64(base64Cert);
        assertTrue(validator.isBase64Certificate(certificateBinaries));

        String wrongBase64Cert = "AAAIDlDCCAnygAwIBAgIQWAsFbFMk27JQVxhf+eWmUDANBgkqhkiG9w0BAQUFADAnMQswCQYDVQQGEwJCRTEYMBYGA1UEAxMPQmVsZ2l1bSBSb290IENBMB4XDTAzMDEyNjIzMDAwMFoXDTE0MDEyNjIzMDAwMFowJzELMAkGA1UEBhMCQkUxGDAWBgNVBAMTD0JlbGdpdW0gUm9vdCBDQTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAMihcekcRkJ5eHFvna6pqKsot03HIOswkVp19eLSz8hMFJhCWK3HEcVAQGpa+XQSJ4fpnOVxTiIs0RIYqjBeoiG52bv/9nTrMQHnO35YD5EWTXaJqAFPrSJmcPpLHZXBMFjqvNll2Jq0iOtJRlLf0lMVdssUXRlJsW9q09P9vMIt7EU/CT9YvvzU7wCMgTVyv/cY6pZifSsofxVsY9LKyn0FrMhtB20yvmi4BUCuVJhWPmbxMOjvxKuTXgfeMo8SdKpbNCNUwOpszv42kqgJF+qhLc9s44Qd3ocuMws8dOIhUDiVLlzg5cYx+dtA+mqhpIqTm6chBocdJ9PEoclMsG8CAwEAAaOBuzCBuDAOBgNVHQ8BAf8EBAMCAQYwDwYDVR0TAQH/BAUwAwEB/zBCBgNVHSAEOzA5MDcGBWA4AQEBMC4wLAYIKwYBBQUHAgEWIGh0dHA6Ly9yZXBvc2l0b3J5LmVpZC5iZWxnaXVtLmJlMB0GA1UdDgQWBBQQ8AxWm2HqVzq2NZdtn925FI7b5jARBglghkgBhvhCAQEEBAMCAAcwHwYDVR0jBBgwFoAUEPAMVpth6lc6tjWXbZ/duRSO2+YwDQYJKoZIhvcNAQEFBQADggEBAMhtIlGKYfgPlm7VILKB+MbcoxYA2s1q52sq+llIp0xJN9dzoWoBZV4yveeX09AuPHPTjHuD79ZCwT+oqV0PN7p20kC9zC0/00RBSZz9Wyn0AiMiW3Ebv1jZKE4tRfTa57VjRUQRDSp/M382SbTObqkCMa5c/ciJv0J71/Fg8teH9lcuen5qE4Ad3OPQYx49cTGxYNSeCMqr8JTHSHVUgfMbrXec6LKP24OsjzRr6L/D2fVDw2RV6xq9NoY2uiGMlxoh1OotO6y67Kcdq765Sps1LxxcHVGnH1TtEpf/8m6HfUbJdNbv6z195lluBpQE5KJVhzgoaiJe4r50ErAEQyo=";
        byte[] wrongCertificateBinaries = Base64.decodeBase64(wrongBase64Cert);
        assertFalse(validator.isBase64Certificate(wrongCertificateBinaries));
    }

    @Test
    public void isCorrectX509SKI() {
        String base64Cert = "MIIDlDCCAnygAwIBAgIQWAsFbFMk27JQVxhf+eWmUDANBgkqhkiG9w0BAQUFADAnMQswCQYDVQQGEwJCRTEYMBYGA1UEAxMPQmVsZ2l1bSBSb290IENBMB4XDTAzMDEyNjIzMDAwMFoXDTE0MDEyNjIzMDAwMFowJzELMAkGA1UEBhMCQkUxGDAWBgNVBAMTD0JlbGdpdW0gUm9vdCBDQTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAMihcekcRkJ5eHFvna6pqKsot03HIOswkVp19eLSz8hMFJhCWK3HEcVAQGpa+XQSJ4fpnOVxTiIs0RIYqjBeoiG52bv/9nTrMQHnO35YD5EWTXaJqAFPrSJmcPpLHZXBMFjqvNll2Jq0iOtJRlLf0lMVdssUXRlJsW9q09P9vMIt7EU/CT9YvvzU7wCMgTVyv/cY6pZifSsofxVsY9LKyn0FrMhtB20yvmi4BUCuVJhWPmbxMOjvxKuTXgfeMo8SdKpbNCNUwOpszv42kqgJF+qhLc9s44Qd3ocuMws8dOIhUDiVLlzg5cYx+dtA+mqhpIqTm6chBocdJ9PEoclMsG8CAwEAAaOBuzCBuDAOBgNVHQ8BAf8EBAMCAQYwDwYDVR0TAQH/BAUwAwEB/zBCBgNVHSAEOzA5MDcGBWA4AQEBMC4wLAYIKwYBBQUHAgEWIGh0dHA6Ly9yZXBvc2l0b3J5LmVpZC5iZWxnaXVtLmJlMB0GA1UdDgQWBBQQ8AxWm2HqVzq2NZdtn925FI7b5jARBglghkgBhvhCAQEEBAMCAAcwHwYDVR0jBBgwFoAUEPAMVpth6lc6tjWXbZ/duRSO2+YwDQYJKoZIhvcNAQEFBQADggEBAMhtIlGKYfgPlm7VILKB+MbcoxYA2s1q52sq+llIp0xJN9dzoWoBZV4yveeX09AuPHPTjHuD79ZCwT+oqV0PN7p20kC9zC0/00RBSZz9Wyn0AiMiW3Ebv1jZKE4tRfTa57VjRUQRDSp/M382SbTObqkCMa5c/ciJv0J71/Fg8teH9lcuen5qE4Ad3OPQYx49cTGxYNSeCMqr8JTHSHVUgfMbrXec6LKP24OsjzRr6L/D2fVDw2RV6xq9NoY2uiGMlxoh1OotO6y67Kcdq765Sps1LxxcHVGnH1TtEpf/8m6HfUbJdNbv6z195lluBpQE5KJVhzgoaiJe4r50ErAEQyo=";
        CertificateToken certificate = DSSUtils.loadCertificateFromBase64EncodedString(base64Cert);
        assertNotNull(certificate);

        String base64SKI = "EPAMVpth6lc6tjWXbZ/duRSO2+Y=";
        byte[] ski = Base64.decodeBase64(base64SKI);
        assertTrue(validator.isCorrectX509SKI(ski, certificate));

        String wrongBase64SKI = "AAAMVpth6lc6tjWXbZ/duRSO2+Y=";
        byte[] wrongSki = Base64.decodeBase64(wrongBase64SKI);
        assertFalse(validator.isCorrectX509SKI(wrongSki, certificate));

        String cert1 = "MIIELTCCAxWgAwIBAgICA4gwDQYJKoZIhvcNAQELBQAwXDELMAkGA1UEBhMCVVMxGTAXBgNVBAoMEFZlcml6b24gQnVzaW5lc3MxETAPBgNVBAsMCE9tbmlSb290MR8wHQYDVQQDDBZWZXJpem9uIEdsb2JhbCBSb290IENBMB4XDTEyMDExMTE5NDUwNloXDTIyMDExMTE5NDQzNFowYjELMAkGA1UEBhMCQkUxHDAaBgNVBAoTE0NlcnRpcG9zdCBuLnYuL3MuYS4xNTAzBgNVBAMTLENlcnRpcG9zdCBQdWJsaWMgQ0EgZm9yIFF1YWxpZmllZCBTaWduYXR1cmVzMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAow3rmuZKZMnGhQRGeEzZK4THeq59CIqK6BseSxLmZ3sh8znY0FBNK4OXmFEj0Y99QnIyAJxnU5bcvSSBFQKPwtD5cFcmpcyP7BROi6/MyJCE6BMD8wcS61CJfLlm8/p/VRF9KsDfa6fMd/W1ghbq78Owa22+UgXpFr27eqBCsUzEiZya5cILWXMOhmP+ZE3Oi7pLZ/Dh+50tn/R+P0IVBBIypiycnx/u4Q/1oEqMyy+DF1iuMfCbCpE2Pbwz0R+SCqlFNeRO9dlfmJ5XlpSr5K7dKXJP8DgoMw8Cu5fGLU8z2qwqx+3ZvOXdxDNFe2g8HHX4WdMymhSzbmnLjGVYrQIDAQABo4HyMIHvMBIGA1UdEwEB/wQIMAYBAf8CAQAwRQYDVR0gBD4wPDA6BgRVHSAAMDIwMAYIKwYBBQUHAgEWJGh0dHBzOi8vd3d3LmNlcnRpcG9zdC5jb20vc2hvd3BvbGljeTAOBgNVHQ8BAf8EBAMCAQYwHwYDVR0jBBgwFoAUTDgRuJgAW1orcD6qeOTVZ2dnp34wQgYDVR0fBDswOTA3oDWgM4YxaHR0cDovL2NkcDEucHVibGljLXRydXN0LmNvbS9DUkwvT21uaXJvb3QyMDM0LmNybDAdBgNVHQ4EFgQUDjczxyhuv85f5irmmJCLrMHmKEQwDQYJKoZIhvcNAQELBQADggEBAHPwVwcH8zTeSFMePgqIMwdsVUnSdYVUkvKAGRyGXdf0EDUYMaw1+I1LD21mSxVMKJESeDvEs0JlqERGohCM9jigquuNQhgQ4SGsWywNyXw1atIMfp2D7FsiNrTcry3yh2v5fxZ3CyV7o2ZSS+pEvFhvufqdZUlgZ64/RhPcq1ZV74asJuNBRZ7S6IF3PxzAKDN9Ytp8vJw1cs1RoS/0CJ/6aJS8HjBc863Rj39SscL/zZW+KanvLvvDafCCJ/FNuaA80VYjHWHsnk1ZjFWBWlpibJNzIVr2UoSKr5cBln60eYCRpeIrexknt5opr6MnciipCXObk6c/4EiPnrWYivg=";
        String cert2 = "MIIELTCCAxWgAwIBAgIEBydrxjANBgkqhkiG9w0BAQsFADBaMQswCQYDVQQGEwJJRTESMBAGA1UEChMJQmFsdGltb3JlMRMwEQYDVQQLEwpDeWJlclRydXN0MSIwIAYDVQQDExlCYWx0aW1vcmUgQ3liZXJUcnVzdCBSb290MB4XDTEyMDExMTE5MTUyNVoXDTIyMDExMTE5MTUwNFowYjELMAkGA1UEBhMCQkUxHDAaBgNVBAoTE0NlcnRpcG9zdCBuLnYuL3MuYS4xNTAzBgNVBAMTLENlcnRpcG9zdCBQdWJsaWMgQ0EgZm9yIFF1YWxpZmllZCBTaWduYXR1cmVzMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAow3rmuZKZMnGhQRGeEzZK4THeq59CIqK6BseSxLmZ3sh8znY0FBNK4OXmFEj0Y99QnIyAJxnU5bcvSSBFQKPwtD5cFcmpcyP7BROi6/MyJCE6BMD8wcS61CJfLlm8/p/VRF9KsDfa6fMd/W1ghbq78Owa22+UgXpFr27eqBCsUzEiZya5cILWXMOhmP+ZE3Oi7pLZ/Dh+50tn/R+P0IVBBIypiycnx/u4Q/1oEqMyy+DF1iuMfCbCpE2Pbwz0R+SCqlFNeRO9dlfmJ5XlpSr5K7dKXJP8DgoMw8Cu5fGLU8z2qwqx+3ZvOXdxDNFe2g8HHX4WdMymhSzbmnLjGVYrQIDAQABo4HyMIHvMBIGA1UdEwEB/wQIMAYBAf8CAQAwRQYDVR0gBD4wPDA6BgRVHSAAMDIwMAYIKwYBBQUHAgEWJGh0dHBzOi8vd3d3LmNlcnRpcG9zdC5jb20vc2hvd3BvbGljeTAOBgNVHQ8BAf8EBAMCAQYwHwYDVR0jBBgwFoAU5Z1ZMIJHWMys+ghUNoZ7OrUETfAwQgYDVR0fBDswOTA3oDWgM4YxaHR0cDovL2NkcDEucHVibGljLXRydXN0LmNvbS9DUkwvT21uaXJvb3QyMDI1LmNybDAdBgNVHQ4EFgQUDjczxyhuv85f5irmmJCLrMHmKEQwDQYJKoZIhvcNAQELBQADggEBAEE9O1Qfp04ERgAi1+hVwD91rxbeMrbpdaEo4XrJd+qLNl0KmLhho1oUZsEEOIFtBc2zYDqRjpfGBEzc2La7ZrCjja6nvQmGabFsbGB3Ionsket0HnhVjV//yquB4jHzZnLuWTv0+IefdKzwrrLGzJKjdI26CIQ49nwwAK3ihwpXy/VVNKhayIqETtBvq3MiBEH7vmKJI7/InxFbbDE2tWZxje5wi2l3xg9KjREy+yV0UH3C2BaziU9oI2JfA+0selKvuz2BaUJC2Z3VsIHkvcRK87tD4vX23tTQwSYYJjwdBFlp3GLQzsXgT67PEtjiCsw3tEOb9hkWnBkuSwIWpJw=";
        String commonSki = "Djczxyhuv85f5irmmJCLrMHmKEQ=";

        assertTrue(validator.isCorrectX509SKI(Base64.decodeBase64(commonSki), DSSUtils.loadCertificateFromBase64EncodedString(cert1)));
        assertTrue(validator.isCorrectX509SKI(Base64.decodeBase64(commonSki), DSSUtils.loadCertificateFromBase64EncodedString(cert2)));

    }

    @Test
    public void isCorrectX509SubjectName() {
        String base64Cert = "MIIDlDCCAnygAwIBAgIQWAsFbFMk27JQVxhf+eWmUDANBgkqhkiG9w0BAQUFADAnMQswCQYDVQQGEwJCRTEYMBYGA1UEAxMPQmVsZ2l1bSBSb290IENBMB4XDTAzMDEyNjIzMDAwMFoXDTE0MDEyNjIzMDAwMFowJzELMAkGA1UEBhMCQkUxGDAWBgNVBAMTD0JlbGdpdW0gUm9vdCBDQTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAMihcekcRkJ5eHFvna6pqKsot03HIOswkVp19eLSz8hMFJhCWK3HEcVAQGpa+XQSJ4fpnOVxTiIs0RIYqjBeoiG52bv/9nTrMQHnO35YD5EWTXaJqAFPrSJmcPpLHZXBMFjqvNll2Jq0iOtJRlLf0lMVdssUXRlJsW9q09P9vMIt7EU/CT9YvvzU7wCMgTVyv/cY6pZifSsofxVsY9LKyn0FrMhtB20yvmi4BUCuVJhWPmbxMOjvxKuTXgfeMo8SdKpbNCNUwOpszv42kqgJF+qhLc9s44Qd3ocuMws8dOIhUDiVLlzg5cYx+dtA+mqhpIqTm6chBocdJ9PEoclMsG8CAwEAAaOBuzCBuDAOBgNVHQ8BAf8EBAMCAQYwDwYDVR0TAQH/BAUwAwEB/zBCBgNVHSAEOzA5MDcGBWA4AQEBMC4wLAYIKwYBBQUHAgEWIGh0dHA6Ly9yZXBvc2l0b3J5LmVpZC5iZWxnaXVtLmJlMB0GA1UdDgQWBBQQ8AxWm2HqVzq2NZdtn925FI7b5jARBglghkgBhvhCAQEEBAMCAAcwHwYDVR0jBBgwFoAUEPAMVpth6lc6tjWXbZ/duRSO2+YwDQYJKoZIhvcNAQEFBQADggEBAMhtIlGKYfgPlm7VILKB+MbcoxYA2s1q52sq+llIp0xJN9dzoWoBZV4yveeX09AuPHPTjHuD79ZCwT+oqV0PN7p20kC9zC0/00RBSZz9Wyn0AiMiW3Ebv1jZKE4tRfTa57VjRUQRDSp/M382SbTObqkCMa5c/ciJv0J71/Fg8teH9lcuen5qE4Ad3OPQYx49cTGxYNSeCMqr8JTHSHVUgfMbrXec6LKP24OsjzRr6L/D2fVDw2RV6xq9NoY2uiGMlxoh1OotO6y67Kcdq765Sps1LxxcHVGnH1TtEpf/8m6HfUbJdNbv6z195lluBpQE5KJVhzgoaiJe4r50ErAEQyo=";
        CertificateToken certificate = DSSUtils.loadCertificateFromBase64EncodedString(base64Cert);

        String subjectName = "CN=Belgium Root CA,C=BE";
        assertTrue(validator.isCorrectX509SubjectName(subjectName, certificate));

        subjectName = "C=BE,CN=Belgium Root CA";
        assertTrue(validator.isCorrectX509SubjectName(subjectName, certificate));

        String wrongSubjectName = "CN=Belgium Root CA,C=FR";
        assertFalse(validator.isCorrectX509SubjectName(wrongSubjectName, certificate));

        String cert1 = "MIIELTCCAxWgAwIBAgICA4gwDQYJKoZIhvcNAQELBQAwXDELMAkGA1UEBhMCVVMxGTAXBgNVBAoMEFZlcml6b24gQnVzaW5lc3MxETAPBgNVBAsMCE9tbmlSb290MR8wHQYDVQQDDBZWZXJpem9uIEdsb2JhbCBSb290IENBMB4XDTEyMDExMTE5NDUwNloXDTIyMDExMTE5NDQzNFowYjELMAkGA1UEBhMCQkUxHDAaBgNVBAoTE0NlcnRpcG9zdCBuLnYuL3MuYS4xNTAzBgNVBAMTLENlcnRpcG9zdCBQdWJsaWMgQ0EgZm9yIFF1YWxpZmllZCBTaWduYXR1cmVzMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAow3rmuZKZMnGhQRGeEzZK4THeq59CIqK6BseSxLmZ3sh8znY0FBNK4OXmFEj0Y99QnIyAJxnU5bcvSSBFQKPwtD5cFcmpcyP7BROi6/MyJCE6BMD8wcS61CJfLlm8/p/VRF9KsDfa6fMd/W1ghbq78Owa22+UgXpFr27eqBCsUzEiZya5cILWXMOhmP+ZE3Oi7pLZ/Dh+50tn/R+P0IVBBIypiycnx/u4Q/1oEqMyy+DF1iuMfCbCpE2Pbwz0R+SCqlFNeRO9dlfmJ5XlpSr5K7dKXJP8DgoMw8Cu5fGLU8z2qwqx+3ZvOXdxDNFe2g8HHX4WdMymhSzbmnLjGVYrQIDAQABo4HyMIHvMBIGA1UdEwEB/wQIMAYBAf8CAQAwRQYDVR0gBD4wPDA6BgRVHSAAMDIwMAYIKwYBBQUHAgEWJGh0dHBzOi8vd3d3LmNlcnRpcG9zdC5jb20vc2hvd3BvbGljeTAOBgNVHQ8BAf8EBAMCAQYwHwYDVR0jBBgwFoAUTDgRuJgAW1orcD6qeOTVZ2dnp34wQgYDVR0fBDswOTA3oDWgM4YxaHR0cDovL2NkcDEucHVibGljLXRydXN0LmNvbS9DUkwvT21uaXJvb3QyMDM0LmNybDAdBgNVHQ4EFgQUDjczxyhuv85f5irmmJCLrMHmKEQwDQYJKoZIhvcNAQELBQADggEBAHPwVwcH8zTeSFMePgqIMwdsVUnSdYVUkvKAGRyGXdf0EDUYMaw1+I1LD21mSxVMKJESeDvEs0JlqERGohCM9jigquuNQhgQ4SGsWywNyXw1atIMfp2D7FsiNrTcry3yh2v5fxZ3CyV7o2ZSS+pEvFhvufqdZUlgZ64/RhPcq1ZV74asJuNBRZ7S6IF3PxzAKDN9Ytp8vJw1cs1RoS/0CJ/6aJS8HjBc863Rj39SscL/zZW+KanvLvvDafCCJ/FNuaA80VYjHWHsnk1ZjFWBWlpibJNzIVr2UoSKr5cBln60eYCRpeIrexknt5opr6MnciipCXObk6c/4EiPnrWYivg=";
        String cert2 = "MIIELTCCAxWgAwIBAgIEBydrxjANBgkqhkiG9w0BAQsFADBaMQswCQYDVQQGEwJJRTESMBAGA1UEChMJQmFsdGltb3JlMRMwEQYDVQQLEwpDeWJlclRydXN0MSIwIAYDVQQDExlCYWx0aW1vcmUgQ3liZXJUcnVzdCBSb290MB4XDTEyMDExMTE5MTUyNVoXDTIyMDExMTE5MTUwNFowYjELMAkGA1UEBhMCQkUxHDAaBgNVBAoTE0NlcnRpcG9zdCBuLnYuL3MuYS4xNTAzBgNVBAMTLENlcnRpcG9zdCBQdWJsaWMgQ0EgZm9yIFF1YWxpZmllZCBTaWduYXR1cmVzMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAow3rmuZKZMnGhQRGeEzZK4THeq59CIqK6BseSxLmZ3sh8znY0FBNK4OXmFEj0Y99QnIyAJxnU5bcvSSBFQKPwtD5cFcmpcyP7BROi6/MyJCE6BMD8wcS61CJfLlm8/p/VRF9KsDfa6fMd/W1ghbq78Owa22+UgXpFr27eqBCsUzEiZya5cILWXMOhmP+ZE3Oi7pLZ/Dh+50tn/R+P0IVBBIypiycnx/u4Q/1oEqMyy+DF1iuMfCbCpE2Pbwz0R+SCqlFNeRO9dlfmJ5XlpSr5K7dKXJP8DgoMw8Cu5fGLU8z2qwqx+3ZvOXdxDNFe2g8HHX4WdMymhSzbmnLjGVYrQIDAQABo4HyMIHvMBIGA1UdEwEB/wQIMAYBAf8CAQAwRQYDVR0gBD4wPDA6BgRVHSAAMDIwMAYIKwYBBQUHAgEWJGh0dHBzOi8vd3d3LmNlcnRpcG9zdC5jb20vc2hvd3BvbGljeTAOBgNVHQ8BAf8EBAMCAQYwHwYDVR0jBBgwFoAU5Z1ZMIJHWMys+ghUNoZ7OrUETfAwQgYDVR0fBDswOTA3oDWgM4YxaHR0cDovL2NkcDEucHVibGljLXRydXN0LmNvbS9DUkwvT21uaXJvb3QyMDI1LmNybDAdBgNVHQ4EFgQUDjczxyhuv85f5irmmJCLrMHmKEQwDQYJKoZIhvcNAQELBQADggEBAEE9O1Qfp04ERgAi1+hVwD91rxbeMrbpdaEo4XrJd+qLNl0KmLhho1oUZsEEOIFtBc2zYDqRjpfGBEzc2La7ZrCjja6nvQmGabFsbGB3Ionsket0HnhVjV//yquB4jHzZnLuWTv0+IefdKzwrrLGzJKjdI26CIQ49nwwAK3ihwpXy/VVNKhayIqETtBvq3MiBEH7vmKJI7/InxFbbDE2tWZxje5wi2l3xg9KjREy+yV0UH3C2BaziU9oI2JfA+0selKvuz2BaUJC2Z3VsIHkvcRK87tD4vX23tTQwSYYJjwdBFlp3GLQzsXgT67PEtjiCsw3tEOb9hkWnBkuSwIWpJw=";
        String commonSubjectName = "CN=Certipost Public CA for Qualified Signatures,O=Certipost n.v./s.a.,C=BE";

        assertTrue(validator.isCorrectX509SubjectName(commonSubjectName, DSSUtils.loadCertificateFromBase64EncodedString(cert1)));
        assertTrue(validator.isCorrectX509SubjectName(commonSubjectName, DSSUtils.loadCertificateFromBase64EncodedString(cert2)));
    }

}
