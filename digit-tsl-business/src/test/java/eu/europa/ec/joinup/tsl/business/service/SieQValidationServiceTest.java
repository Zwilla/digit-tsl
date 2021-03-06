package eu.europa.ec.joinup.tsl.business.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.sieQ.SieQResult;
import eu.europa.ec.joinup.tsl.business.dto.sieQ.SieQSearchType;
import eu.europa.ec.joinup.tsl.business.dto.sieQ.SieQServiceEntry;
import eu.europa.ec.joinup.tsl.business.dto.sieQ.SieQValidationForm;

public class SieQValidationServiceTest extends AbstractSpringTest {

    @Autowired
    private SieQValidationService sieQValidationService;

    @Test
    public void getMatchingQualifiers() {
        loadAllTLs("");
        SieQValidationForm form = new SieQValidationForm();
        form.setCertificateType(SieQSearchType.B64);
        form.setCertificateB64(
                "MIIGdTCCBF2gAwIBAgIQEAAAAAAAkos8yR6Ewzq6GTANBgkqhkiG9w0BAQsFADAzMQswCQYDVQQGEwJCRTETMBEGA1UEAxMKQ2l0aXplbiBDQTEPMA0GA1UEBRMGMjAxNjMxMB4XDTE3MDEyNTIyMTIxMloXDTI3MDEyMTIzNTk1OVowgYAxCzAJBgNVBAYTAkJFMSswKQYDVQQDEyJQaWVycmljayBWYW5kZW5icm91Y2tlIChTaWduYXR1cmUpMRYwFAYDVQQEEw1WYW5kZW5icm91Y2tlMRYwFAYDVQQqEw1QaWVycmljayBQYWNvMRQwEgYDVQQFEws4NzAxMjczMDczODCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAOI4HvZASLyOwTIFPvb6gHqJyvAcUbTG2EmaMguJemLdMyidK0hOuZ90F2BfvY8ArHhTwTKHK/hm5HFsKwlGfOlGsItzuRegAFFYiBs+039oHqijrxSrxU/zgoThYCr8zKb6uKdvKdVHgN/VB1XQgiUr/9efKCRXPBZLUhJ4DwDFwCEzo87iLmmw/93YL7kC9x+4+PY0kIghVMCehfSY6pDufMujcW7k16E/sun4GV+Wq6YdH+y6aMtkfYo4RZ7h16YRue4vRz5mxXSmpnbnpFEFmriHPGvL2atZU2ohCQqVuwX6TTafGDXxTg8w/P0liwcXoDVkrFu/9Pvty9GFf4sCAwEAAaOCAjUwggIxMB8GA1UdIwQYMBaAFM6Al2fQrdlOxJlqgCcikM0RNRCHMHAGCCsGAQUFBwEBBGQwYjA2BggrBgEFBQcwAoYqaHR0cDovL2NlcnRzLmVpZC5iZWxnaXVtLmJlL2JlbGdpdW1yczQuY3J0MCgGCCsGAQUFBzABhhxodHRwOi8vb2NzcC5laWQuYmVsZ2l1bS5iZS8yMIIBGAYDVR0gBIIBDzCCAQswggEHBgdgOAwBAQIBMIH7MCwGCCsGAQUFBwIBFiBodHRwOi8vcmVwb3NpdG9yeS5laWQuYmVsZ2l1bS5iZTCBygYIKwYBBQUHAgIwgb0agbpHZWJydWlrIG9uZGVyd29ycGVuIGFhbiBhYW5zcHJha2VsaWpraGVpZHNiZXBlcmtpbmdlbiwgemllIENQUyAtIFVzYWdlIHNvdW1pcyDDoCBkZXMgbGltaXRhdGlvbnMgZGUgcmVzcG9uc2FiaWxpdMOpLCB2b2lyIENQUyAtIFZlcndlbmR1bmcgdW50ZXJsaWVndCBIYWZ0dW5nc2Jlc2NocsOkbmt1bmdlbiwgZ2Vtw6RzcyBDUFMwOQYDVR0fBDIwMDAuoCygKoYoaHR0cDovL2NybC5laWQuYmVsZ2l1bS5iZS9laWRjMjAxNjMxLmNybDAOBgNVHQ8BAf8EBAMCBkAwEQYJYIZIAYb4QgEBBAQDAgUgMCIGCCsGAQUFBwEDBBYwFDAIBgYEAI5GAQEwCAYGBACORgEEMA0GCSqGSIb3DQEBCwUAA4ICAQA5pxf0iw5i66hb1x9F/9e1/XXsS0fsVGPxT0njjqnCr2qLvkwtjjgcrilECkaGrJzyI2YRuxenjMB4AzCbIrDiV+95xQkAFDcDov5K1DDojmXr6x+0KtKt8mfVTWNYrE7X0vR62teK16q4EP43gfjKfvYXJrid/DfOacNAErlRSjdUZbNU+TDTMiijBM6Hfyxck0LuvYgAy26/infQts9ADWxoYew80rLTxefzf4wj2S1OOHkg26yT6+qVynJanj3ObSkHJXSfijVwke6PSeKymMKRaiOZdIirYRoXuSi0WEhhQQub4curoMwtKXthVhCGjll1Rj5sG6a+vGaYodwHTAFWdrIitNNE+5AGN+wZo1J2pHUM3se4XpZc3Xh+2nwXWxd9qu8RZmfKhdGyn+XEDNl74XJUjCphCgjiJ9hG7yWiDlyyICSk7JudoTFZm2avba19rrygoANlcVBUInBk2fQmEzjA5lynfja2G+7VKCJpCOTSHG8oz54US4lhDYS7BVpnyHAavcFCsgweiO0uRCrMkOS4zYXCeZaYPmiIMctEgsEfqN9kaMRJlCExS0zjvok6vCuudgDoM+mIAjGJyo/bFXBIUC0SXEp2bgWCMyMOjt8hxD2eMP22nRTEs0zt88X/bCm7IZsdwiyRGcUzZKAwVJhWJ6URPV22O1IaWQ==");

        SieQResult result = sieQValidationService.getMatchingQualifiers(form);
        Assert.assertNotNull(result.getCertificate());
        Assert.assertEquals(
                "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",
                result.getCertificate().getCertB64());
        Assert.assertEquals(1, result.getServices().size());
        SieQServiceEntry sieQServiceEntry = result.getServices().get(0);
        Assert.assertEquals("CN=Belgium Root CA4, C=BE", sieQServiceEntry.getService().getServiceName().get(0).getValue());
        Assert.assertEquals("QCQSCDStatusAsInCert", sieQServiceEntry.getQualifiers().get(0));
    }

}
