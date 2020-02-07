package eu.europa.ec.joinup.tsl.business.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateParsingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.Load;
import eu.europa.ec.joinup.tsl.business.dto.TLSigningCertificateResultDTO;
import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;
import eu.europa.esig.dss.DSSUtils;
import eu.europa.esig.dss.x509.CertificateToken;

public class CertificateServiceTest extends AbstractSpringTest {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private TLLoader tlLoader;

    @Value("${lotl.territory}")
    private String lotlTerritory;

    private static final CertificateToken SI_CERTIFICATE;
    private static final CertificateToken DE_CERTIFICATE;
    private static final CertificateToken CA_ROOT;
    private static final CertificateToken BE_CERTIFICATE;
    private static final CertificateToken BE_SIGNING_CERTIFICATE;
    private static final CertificateToken CY_SIGNING_CERTIFICATE;

    static {
        SI_CERTIFICATE = DSSUtils.loadCertificateFromBase64EncodedString(
                "MIIFdDCCBFygAwIBAgIEOl6PRjANBgkqhkiG9w0BAQUFADA9MQswCQYDVQQGEwJzaTEbMBkGA1UEChMSc3RhdGUtaW5zdGl0dXRpb25zMREwDwYDVQQLEwhzaWdvdi1jYTAeFw0xMzAzMDUxMDU2MDZaFw0xODAzMDUxMzI4MzVaMIGJMQswCQYDVQQGEwJzaTEbMBkGA1UEChMSc3RhdGUtaW5zdGl0dXRpb25zMRkwFwYDVQQLExB3ZWItY2VydGlmaWNhdGVzMRMwEQYDVQQLEwpHb3Zlcm5tZW50MS0wFAYDVQQFEw0xMjM1MTc0MjE0MDI3MBUGA1UEAxMORGltaXRyaWogU2themEwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCJxPypF9ZVssjvPT/he9cZ/DdBn3htrapLJqHISc1ylop1CpkL0fbPTL9xK3JnaMzzmucbeTkwj2fyEaluP2bl1+ElpMWDYUq59mDOGvpB5KaJvTCXGUdf3rZTFSVOf7WarhI9uR25sjZg1wwVKGJ9VTqVtAlD5WBsIyT2CN1xWj3DXy+faqGtYbd/4mpzMW+qvTJQ9act6rlmTXJXwOVBTYwVEciTqX+IMX1nIn93FEOl6Q9BQeAYFe8pVXbTjoveY7KIE2SyWETogYplMDesK3hWB6cGfnWD05+Vbj78hKFyxSjoKTJegxzk6+8nGy0dzkjup1nlpTrjDBr1Zgt9AgMBAAGjggItMIICKTAOBgNVHQ8BAf8EBAMCBaAwSgYDVR0gBEMwQTA1BgorBgEEAa9ZAQcBMCcwJQYIKwYBBQUHAgEWGWh0dHA6Ly93d3cuY2EuZ292LnNpL2Nwcy8wCAYGBACLMAEBMCIGCCsGAQUFBwEDBBYwFDAIBgYEAI5GAQEwCAYGBACORgEEMCAGA1UdEQQZMBeBFWRpbWl0cmlqLnNrYXphQGdvdi5zaTCB8QYDVR0fBIHpMIHmMFWgU6BRpE8wTTELMAkGA1UEBhMCc2kxGzAZBgNVBAoTEnN0YXRlLWluc3RpdHV0aW9uczERMA8GA1UECxMIc2lnb3YtY2ExDjAMBgNVBAMTBUNSTDQxMIGMoIGJoIGGhldsZGFwOi8veDUwMC5nb3Yuc2kvb3U9c2lnb3YtY2Esbz1zdGF0ZS1pbnN0aXR1dGlvbnMsYz1zaT9jZXJ0aWZpY2F0ZVJldm9jYXRpb25MaXN0P2Jhc2WGK2h0dHA6Ly93d3cuc2lnb3YtY2EuZ292LnNpL2NybC9zaWdvdi1jYS5jcmwwKwYDVR0QBCQwIoAPMjAxMzAzMDUxMDU2MDZagQ8yMDE4MDMwNTEzMjgzNVowHwYDVR0jBBgwFoAUHvjUU2uzgwbpBAZXAvmlv8ZYPHIwHQYDVR0OBBYEFPkW6/pilxObPEzBAucEf/wHqwgdMAkGA1UdEwQCMAAwGQYJKoZIhvZ9B0EABAwwChsEVjcuMQMCA6gwDQYJKoZIhvcNAQEFBQADggEBAI+3jSydwmTfTuFJxIys5PFZGzWNX8pCcyyuYFnbPbsnWwVMA1wE/FazkN51U0E2nTsYlooal4uiZ0u5jgbXW7wBvAIept/mJNyXXLd/il5JiB0Bz76GsGNmw1DoX2lvV06x39NI9X3+ea2rp7L56co3kVJPmFbJImyYc5OK5H9dXjGpIcxzVyWXNoUSbhVZpljIw5Tka+c5/G0gE49o3PiexXH2fziGBAmbICn+eX6+zeSo80OB0DiPRMD0s31IitQfEv1N3H+lz21Pa8gKEKpKw7Ns7b4nMGfw8WQyiVHNNSo95RlCaHPFfeFR5vkDuUayHqGwErB1Zdx8AIjzR+w=");
        DE_CERTIFICATE = DSSUtils.loadCertificateFromBase64EncodedString(
                "MIIECjCCAvKgAwIBAgICBH8wDQYJKoZIhvcNAQENBQAwPzELMAkGA1UEBhMCREUxGjAYBgNVBAoMEUJ1bmRlc25ldHphZ2VudHVyMRQwEgYDVQQDDAsxNFItQ0EgMTpQTjAeFw0xNDA0MTEwODQ0NTJaFw0xOTA0MTEwNjM1MDBaMEAxCzAJBgNVBAYTAkRFMRowGAYDVQQKDBFCdW5kZXNuZXR6YWdlbnR1cjEVMBMGA1UEAwwMMTRSLVRTTCAxOlBOMIIBIzANBgkqhkiG9w0BAQEFAAOCARAAMIIBCwKCAQEAkyyMPdtWEDtPcT+eq+KKYaQ5G+6Hbpl9i6b3nBN6+3DROzqaVqtehrCpuE5CmUdqR2lixvHTbEjYIlk3jsmPTxtImfZ66mwKUoenulI6jE5/lvRNtqKWQbLTd7nrEJAecy/ouHWZ6xiDB/ytftxJhAREUqGPfJiWnCFoyRrDSW6GQ8QQbJnlHMLuxs30KNUIRbVOOX/jb8oeqFI0zXUeSH/AMrshRM3G8W941tee8nn5jK2CZvjOuYEI1hNpcXAzBTuaFRJhLdsvg0SfgW0T6tFhuUbG5eW9wraGOMCNdzfcNnjmFitVrBRtl9yIfyVn2Tgd2DfJ9cHLJGmbTBnUIwIEQAAAgaOCAQwwggEIMA4GA1UdDwEB/wQEAwIGQDAdBgNVHQ4EFgQUYqVd8yHV7CHE+JCq3zLhvyLM43wwEQYDVR0lBAowCAYGBACRNwMAMBgGCCsGAQUFBwEDBAwwCjAIBgYEAI5GAQEwHwYDVR0jBBgwFoAU/fNQhDCO7COa9TOy44EH3eTvgK4wSgYIKwYBBQUHAQEEPjA8MDoGCCsGAQUFBzABhi5odHRwOi8vb2NzcC5ucmNhLWRzLmRlOjgwODAvb2NzcC1vY3NwcmVzcG9uZGVyMBIGA1UdIAQLMAkwBwYFKyQIAQEwGwYJKwYBBAHAbQMFBA4wDAYKKwYBBAHAbQMFATAMBgNVHRMBAf8EAjAAMA0GCSqGSIb3DQEBDQUAA4IBAQAMm2Fj5hoZBOeOOT4LPrky39cTYMPN1+Patx6BB+kuF/pXAI/GmDyOuFIZ+/Sf8bz336sbbIfnbDeV6Y6ZJvCnqzrUT8kBlf3+QTQ+JxOEYfw1bdRffjmYDCbM0S7Rw02eAaSykiHSkSp8kWA6rYWkhVakX/v/PdBUtkPHdq1P5ghLPx7Gk/ax/U3fDLlKGms5iJjz55AIMqlK4HWEc7xZk3QoD8w+lpRqT5QNYwex5ueXO/Mpd9ZtY5qm7bJKhRnKejQaaMO1frAWT+QM2Uve3TaZlgupa0K+FL9i532dMd/D4RjxtDTNfa5o8gcNFS6eDyuo0z8BJDp9LCLtNZYT");
        CA_ROOT = DSSUtils.loadCertificateFromBase64EncodedString(
                "MIIDVzCCAj+gAwIBAgIBATANBgkqhkiG9w0BAQ0FADBNMRAwDgYDVQQDDAdyb290LWNhMRkwFwYDVQQKDBBOb3dpbmEgU29sdXRpb25zMREwDwYDVQQLDAhQS0ktVEVTVDELMAkGA1UEBhMCTFUwHhcNMTcwODI5MDg1NzExWhcNMTkwODI5MDg1NzExWjBNMRAwDgYDVQQDDAdyb290LWNhMRkwFwYDVQQKDBBOb3dpbmEgU29sdXRpb25zMREwDwYDVQQLDAhQS0ktVEVTVDELMAkGA1UEBhMCTFUwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCrmYoJRcgkvHFTnkT6P84deTjpnIRp4pjTQ24cH4GiLOlmmG82rdGMKA7DbKolQ2CZSuaQAsYv2ZIeF/5P8gY/Fj0yQiyy5Jk0PrSgnRwLzpVaiL5uu32VZewdcjMWDN4VJezgf9dsC7MWaUUAAEoDMZEwYnMLK2AW9jG7PqwTJk0x5Rf3EzkK3yeob2PE+lu+QF/Vyy9yD9W5PY/wyif8SD23jY9IfU9vXgCo4+/2nDNZlSOsy4CeRz3gvEgHuH2rxGrb29MDuRxUELvla6fHExTBm1YMtJb+JfvNIykGzihAyV2WLjQXDcqgli4jvwf/BGEmswyylkSqMncVtZTtAgMBAAGjQjBAMA4GA1UdDwEB/wQEAwIBBjAdBgNVHQ4EFgQUTi179+uxcXcowtSf1JepPvP35sQwDwYDVR0TAQH/BAUwAwEB/zANBgkqhkiG9w0BAQ0FAAOCAQEAo8zhyiHI7kO0JmdeB/LVdZvMGDvy8238gr/I2Di7PJuNKyA/KCcYwMTVHUl0n5HcBFvwBgrYxdu1UdW5z1l1mk9prjE2I0gDC6dCaSP6FP72pyu1/XxFwXe/2kYRM7t1mnB/wQ597BfFd5HKGWsP+VudO63+wKE2/VqgRAF/EbamexKrd5xjv1PINBjDjTOrNNuA4k5udLCh2Mni4vnud7DmLadR2pztfI/4ym1MFZR5pI8yLyQn9uG3c7nDNLCq5QjBQ49ziYvw8saSM9boyKApnqNVNzHxYEn4fvoQSZkaj+YGzSn4hk5is0vxAeMF7nTFFdar5xLQbdK8hDv7wQ==");
        BE_CERTIFICATE = DSSUtils.loadCertificateFromBase64EncodedString(
                "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");
        BE_SIGNING_CERTIFICATE = DSSUtils.loadCertificateFromBase64EncodedString(
                "MIID3zCCAsegAwIBAgIJAK7RpgGHETKPMA0GCSqGSIb3DQEBBQUAMIGHMS0wKwYDVQQDEyRCZWxnaWFuIFRydXN0ZWQgTGlzdCBTY2hlbWUgT3BlcmF0b3IxSTBHBgNVBAoTQEZQUyBFY29ub215LCBTTUVzLCBTZWxmLWVtcGxveWVkIGFuZCBFbmVyZ3kgLSBRdWFsaXR5IGFuZCBTYWZldHkxCzAJBgNVBAYTAkJFMB4XDTE0MDIxOTEzMzc1MloXDTI1MDIxMTEzMzc1MlowgYcxLTArBgNVBAMTJEJlbGdpYW4gVHJ1c3RlZCBMaXN0IFNjaGVtZSBPcGVyYXRvcjFJMEcGA1UEChNARlBTIEVjb25vbXksIFNNRXMsIFNlbGYtZW1wbG95ZWQgYW5kIEVuZXJneSAtIFF1YWxpdHkgYW5kIFNhZmV0eTELMAkGA1UEBhMCQkUwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDAgEFkoDPTYDvGk+/IPnGSPm58NRE7mpzLHk8lxpYnTAtbMhn7FWru9GlNi+blYYNOEmzN2E5KO9+7AAAMmx2x8zmEMwc3oUQ7E0WN5Gl+Y+7n6NtX50D/4Sbw4IjVvwwRRru8Coj5vq5Hz3JKTgft8teEpwb5vSFZh6+o9irdX342RJU4AtG78sxZvzIqpa3WsddMf5XDyjnGK3dRgkDuOaBxWEexuUiN4LvO+MacwoaxEqLhEZ6TALGWS2WmNEW3OlUdf7nc0Tz/lnyQsuFn01c4pg56hjyxLtpjyHwNwbTDx+cjBpBveOT9Nb6UfKFHknC5AfrIOWnFLXUmyKD/AgMBAAGjTDBKMAkGA1UdEwQCMAAwCwYDVR0PBAQDAgbAMB0GA1UdDgQWBBRf745pXfv0l1rxBwgOUhlQqteQUTARBgNVHSUECjAIBgYEAJE3AwAwDQYJKoZIhvcNAQEFBQADggEBABabI8rU/pW4uiTHk47X838qntx6FJ5iDCs+iaED142+yju/wQVU4J8rjS4UqsdO8wOM4sfyLjMLRRsK60saZ5o2u+tLIj0QrFRytTD1WIuPZxpBjQU8Zj/+aLkr4bQmyqgJ4XzJZ9BMvi3Yv18jQxJSjvGpXaWpUNLNnhENTuzKv8f/0vBn2Inmpg7cwgj2q8qhZ/3r1ZmHETSDmEdjV4ovYruAKX58jwwnRdgacTtgQpB88+zZDtAp3FVJxR9nefCdvjV2nuN++UgA2v8d2u9c8c7ObGd7dL6O9rQCfvBWbw6/h9nkXSJSAjKXS1uvnABtq3fQabHwxNc8rIQPkLk=");
        CY_SIGNING_CERTIFICATE = DSSUtils.loadCertificateFromBase64EncodedString(
                "MIIDkTCCAnmgAwIBAgIFEkAyZ6kwDQYJKoZIhvcNAQELBQAwWDELMAkGA1UEBhMCQ1kxMDAuBgNVBAoMJ0RlcGFydG1lbnQgb2YgRWxlY3Ryb25pYyBDb21tdW5pY2F0aW9uczEXMBUGA1UEAwwOQ1ktVFNMIFNpZ25lcjEwHhcNMTcxMjAxMDAwMDAwWhcNMjcxMjAxMDAwMDAwWjBYMQswCQYDVQQGEwJDWTEwMC4GA1UECgwnRGVwYXJ0bWVudCBvZiBFbGVjdHJvbmljIENvbW11bmljYXRpb25zMRcwFQYDVQQDDA5DWS1UU0wgU2lnbmVyMTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAKAsRMYRj0v937oGqxRQrjvWBFncZ+jEq/qyUxgiQB0DGWnOEN9QbtvdIu/swVSCh3FZc7vrvI2aTt0NDQ2kZ8ohBpcj+qQb4xUFU5PXhUdIiGsqGO/+0hYeteJZyhHZ1VxL97U0LzmqkmfYumNmJUdDOoqfmzkatHrORvooSlK+SYhXHTrCxXEU0e3xIwnlXcyaVXRBbDdFsGY3p5QrhTBQ5Cd+FxZMhHLhxnUetPnqN9GOJXgghZAy+72c4zmc7X8gshaxbvO7MEvrBgdbaZcjHod7XiHowzCTxLFjDR9eHXrLvmukYcY9qeb5ieJ37VXf9JUbO9q+aFQu5wJ61Z8CAwEAAaNiMGAwHQYDVR0OBBYEFDPKoJc8X/T59hCvhxlDqw2JXj1uMAkGA1UdEwQCMAAwDgYDVR0PAQH/BAQDAgbAMBEGA1UdIAQKMAgwBgYEVR0gADARBgNVHSUECjAIBgYEAJE3AwAwDQYJKoZIhvcNAQELBQADggEBAINBgvYZfI1L6iY0i1OnVoQR4Nje/FuCzW9tppUCC2h9T9x9sClph6o8hceaiBUDdobX9/BIxc15o59q7wbmyGtYnNpZG8nctj/90uciXvUdOCA7PQeX5/cMUVU/Yscg+Pv1ydNyu/7tZtdDfKDyc+Lux9CDGpvYMDDRqX048NsYTB4shry0a7EzhOyD8jIYjY2xU7AYIvGQI1w/OZ2wqEAM7KhhEHz915wdhkv9DBb/CB14mONa1gMWTL27e7GnXZ+mWZghF1HyDjbG3Pr6jFzEgvxMTkGGLpvLOFm7ULwqYbP+ORd7l6exZ+XvzoN5ilPRZmnx/6eHdhutre/BqmE=");
    }

    @Test
    public void getCountryCode() {
        assertEquals("SI", certificateService.getCountryCode(SI_CERTIFICATE));
        assertEquals("DE", certificateService.getCountryCode(DE_CERTIFICATE));
    }

    @Test
    public void getOrganization() {
        assertEquals("state-institutions", certificateService.getOrganization(SI_CERTIFICATE));
        assertEquals("Bundesnetzagentur", certificateService.getOrganization(DE_CERTIFICATE));
    }

    @Test
    public void getSubjectKeyIdentifier() {
        assertTrue(ArrayUtils.isNotEmpty(certificateService.getSubjectKeyIdentifier(SI_CERTIFICATE)));
        assertTrue(ArrayUtils.isNotEmpty(certificateService.getSubjectKeyIdentifier(DE_CERTIFICATE)));
    }

    @Test
    public void hasTslSigningExtendedKeyUsage() {
        assertFalse(certificateService.hasTslSigningExtendedKeyUsage(SI_CERTIFICATE));
        assertTrue(certificateService.hasTslSigningExtendedKeyUsage(DE_CERTIFICATE));
    }

    @Test
    public void hasAllowedKeyUsagesBits() {
        assertFalse(certificateService.hasAllowedKeyUsagesBits(SI_CERTIFICATE));
        assertTrue(certificateService.hasAllowedKeyUsagesBits(DE_CERTIFICATE));
    }

    @Test
    public void isBasicConstraintCaFalse() {
        assertTrue(certificateService.isBasicConstraintCaFalse(SI_CERTIFICATE));
        assertTrue(certificateService.isBasicConstraintCaFalse(DE_CERTIFICATE));
        assertFalse(certificateService.isBasicConstraintCaFalse(CA_ROOT));
    }

    @Test
    public void getSigningCertificate() {

        File f = new File("src/test/resources/tsl-be.xml");
        CertificateToken cert = TLUtils.getSigningCertificate(f);
        Assert.assertNotNull(cert);

    }

    @Test
    public void countryMatchSchemeTerritory() {
        Assert.assertFalse(certificateService.countryMatchSchemeTerritory(DE_CERTIFICATE, "BE"));
        Assert.assertTrue(certificateService.countryMatchSchemeTerritory(DE_CERTIFICATE, "DE"));
    }

    @Test
    public void organizationMatchOperatorName() {
        List<String> names = new ArrayList<>();
        names.add("Test");
        names.add("Name");
        Assert.assertFalse(certificateService.organizationMatchOperatorName(DE_CERTIFICATE, names));
        names.add("Bundesnetzagentur");
        Assert.assertTrue(certificateService.organizationMatchOperatorName(DE_CERTIFICATE, names));
    }

    @Test
    public void isIssuerVerified() {
        assertFalse(certificateService.isIssuerVerified(BE_CERTIFICATE));
        assertTrue(certificateService.isIssuerVerified(CA_ROOT));

        Load load = new Load();
        tlLoader.loadTL("BE", "https://tsl.belgium.be/tsl-be.xml", TLType.TL, TLStatus.PROD, load);
        tlLoader.loadServiceList(load);
        assertTrue(certificateService.isIssuerVerified(BE_CERTIFICATE));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void certificateIsExpired() {
        Date date2018 = new Date(118, Calendar.FEBRUARY, 1);
        Date date2050 = new Date(150, Calendar.FEBRUARY, 1);
        Assert.assertFalse(certificateService.certificateIsExpired(DE_CERTIFICATE, date2018));
        Assert.assertTrue(certificateService.certificateIsExpired(DE_CERTIFICATE, date2050));
    }

    @Test
    public void isSKIComputeRight() {
        Assert.assertTrue(certificateService.isSKIComputeRight(BE_SIGNING_CERTIFICATE));
        Assert.assertFalse(certificateService.isSKIComputeRight(CY_SIGNING_CERTIFICATE));
    }

    @Test
    public void checkTLSigningCertificate() {
        Load load = new Load();
        tlLoader.loadTL("BE", "https://tsl.belgium.be/tsl-be.xml", TLType.TL, TLStatus.PROD, load);
        tlLoader.loadServiceList(load);
        TLSigningCertificateResultDTO beCheck = certificateService.checkTLSigningCertificate(BE_SIGNING_CERTIFICATE, "BE");
        Assert.assertEquals(0, beCheck.getResults().size());

        beCheck = certificateService.checkTLSigningCertificate(BE_CERTIFICATE, "BE");
        Assert.assertEquals(3, beCheck.getResults().size());
        Assert.assertEquals("SignatureRules.SIG_SIGCERT_SN_ORG_SON", beCheck.getResults().get(0).getCheckId());
        Assert.assertEquals("SignatureRules.SIG_SIGCERT_EXTKEYUS_VALUE", beCheck.getResults().get(1).getCheckId());
        Assert.assertEquals("SignatureRules.SIG_SIGCERT_SKI_VALUE", beCheck.getResults().get(2).getCheckId());

        tlLoader.loadTL("CY", "http://www.mcw.gov.cy/mcw/dec/dec.nsf/all/B28C11BBFDBAC045C2257E0D002937E9/$file/TSL-CY-sign.xml", TLType.TL, TLStatus.PROD, load);
        TLSigningCertificateResultDTO cyCheck = certificateService.checkTLSigningCertificate(CY_SIGNING_CERTIFICATE, "CY");
        Assert.assertEquals(1, cyCheck.getResults().size());
        Assert.assertEquals("SignatureRules.SIG_SIGCERT_SKI_VALUE", cyCheck.getResults().get(0).getCheckId());
    }

}
