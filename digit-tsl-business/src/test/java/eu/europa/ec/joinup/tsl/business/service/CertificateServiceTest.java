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
package eu.europa.ec.joinup.tsl.business.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.esig.dss.DSSUtils;
import eu.europa.esig.dss.x509.CertificateToken;

public class CertificateServiceTest extends AbstractSpringTest {

    @Autowired
    private CertificateService certificateService;

    @Value("${lotl.territory}")
    private String lotlTerritory;

    private static final CertificateToken SI_CERTIFICATE;
    private static final CertificateToken DE_CERTIFICATE;

    static {
        SI_CERTIFICATE = DSSUtils.loadCertificateFromBase64EncodedString(
                "MIIFdDCCBFygAwIBAgIEOl6PRjANBgkqhkiG9w0BAQUFADA9MQswCQYDVQQGEwJzaTEbMBkGA1UEChMSc3RhdGUtaW5zdGl0dXRpb25zMREwDwYDVQQLEwhzaWdvdi1jYTAeFw0xMzAzMDUxMDU2MDZaFw0xODAzMDUxMzI4MzVaMIGJMQswCQYDVQQGEwJzaTEbMBkGA1UEChMSc3RhdGUtaW5zdGl0dXRpb25zMRkwFwYDVQQLExB3ZWItY2VydGlmaWNhdGVzMRMwEQYDVQQLEwpHb3Zlcm5tZW50MS0wFAYDVQQFEw0xMjM1MTc0MjE0MDI3MBUGA1UEAxMORGltaXRyaWogU2themEwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCJxPypF9ZVssjvPT/he9cZ/DdBn3htrapLJqHISc1ylop1CpkL0fbPTL9xK3JnaMzzmucbeTkwj2fyEaluP2bl1+ElpMWDYUq59mDOGvpB5KaJvTCXGUdf3rZTFSVOf7WarhI9uR25sjZg1wwVKGJ9VTqVtAlD5WBsIyT2CN1xWj3DXy+faqGtYbd/4mpzMW+qvTJQ9act6rlmTXJXwOVBTYwVEciTqX+IMX1nIn93FEOl6Q9BQeAYFe8pVXbTjoveY7KIE2SyWETogYplMDesK3hWB6cGfnWD05+Vbj78hKFyxSjoKTJegxzk6+8nGy0dzkjup1nlpTrjDBr1Zgt9AgMBAAGjggItMIICKTAOBgNVHQ8BAf8EBAMCBaAwSgYDVR0gBEMwQTA1BgorBgEEAa9ZAQcBMCcwJQYIKwYBBQUHAgEWGWh0dHA6Ly93d3cuY2EuZ292LnNpL2Nwcy8wCAYGBACLMAEBMCIGCCsGAQUFBwEDBBYwFDAIBgYEAI5GAQEwCAYGBACORgEEMCAGA1UdEQQZMBeBFWRpbWl0cmlqLnNrYXphQGdvdi5zaTCB8QYDVR0fBIHpMIHmMFWgU6BRpE8wTTELMAkGA1UEBhMCc2kxGzAZBgNVBAoTEnN0YXRlLWluc3RpdHV0aW9uczERMA8GA1UECxMIc2lnb3YtY2ExDjAMBgNVBAMTBUNSTDQxMIGMoIGJoIGGhldsZGFwOi8veDUwMC5nb3Yuc2kvb3U9c2lnb3YtY2Esbz1zdGF0ZS1pbnN0aXR1dGlvbnMsYz1zaT9jZXJ0aWZpY2F0ZVJldm9jYXRpb25MaXN0P2Jhc2WGK2h0dHA6Ly93d3cuc2lnb3YtY2EuZ292LnNpL2NybC9zaWdvdi1jYS5jcmwwKwYDVR0QBCQwIoAPMjAxMzAzMDUxMDU2MDZagQ8yMDE4MDMwNTEzMjgzNVowHwYDVR0jBBgwFoAUHvjUU2uzgwbpBAZXAvmlv8ZYPHIwHQYDVR0OBBYEFPkW6/pilxObPEzBAucEf/wHqwgdMAkGA1UdEwQCMAAwGQYJKoZIhvZ9B0EABAwwChsEVjcuMQMCA6gwDQYJKoZIhvcNAQEFBQADggEBAI+3jSydwmTfTuFJxIys5PFZGzWNX8pCcyyuYFnbPbsnWwVMA1wE/FazkN51U0E2nTsYlooal4uiZ0u5jgbXW7wBvAIept/mJNyXXLd/il5JiB0Bz76GsGNmw1DoX2lvV06x39NI9X3+ea2rp7L56co3kVJPmFbJImyYc5OK5H9dXjGpIcxzVyWXNoUSbhVZpljIw5Tka+c5/G0gE49o3PiexXH2fziGBAmbICn+eX6+zeSo80OB0DiPRMD0s31IitQfEv1N3H+lz21Pa8gKEKpKw7Ns7b4nMGfw8WQyiVHNNSo95RlCaHPFfeFR5vkDuUayHqGwErB1Zdx8AIjzR+w=");
        DE_CERTIFICATE = DSSUtils.loadCertificateFromBase64EncodedString(
                "MIIECjCCAvKgAwIBAgICBH8wDQYJKoZIhvcNAQENBQAwPzELMAkGA1UEBhMCREUxGjAYBgNVBAoMEUJ1bmRlc25ldHphZ2VudHVyMRQwEgYDVQQDDAsxNFItQ0EgMTpQTjAeFw0xNDA0MTEwODQ0NTJaFw0xOTA0MTEwNjM1MDBaMEAxCzAJBgNVBAYTAkRFMRowGAYDVQQKDBFCdW5kZXNuZXR6YWdlbnR1cjEVMBMGA1UEAwwMMTRSLVRTTCAxOlBOMIIBIzANBgkqhkiG9w0BAQEFAAOCARAAMIIBCwKCAQEAkyyMPdtWEDtPcT+eq+KKYaQ5G+6Hbpl9i6b3nBN6+3DROzqaVqtehrCpuE5CmUdqR2lixvHTbEjYIlk3jsmPTxtImfZ66mwKUoenulI6jE5/lvRNtqKWQbLTd7nrEJAecy/ouHWZ6xiDB/ytftxJhAREUqGPfJiWnCFoyRrDSW6GQ8QQbJnlHMLuxs30KNUIRbVOOX/jb8oeqFI0zXUeSH/AMrshRM3G8W941tee8nn5jK2CZvjOuYEI1hNpcXAzBTuaFRJhLdsvg0SfgW0T6tFhuUbG5eW9wraGOMCNdzfcNnjmFitVrBRtl9yIfyVn2Tgd2DfJ9cHLJGmbTBnUIwIEQAAAgaOCAQwwggEIMA4GA1UdDwEB/wQEAwIGQDAdBgNVHQ4EFgQUYqVd8yHV7CHE+JCq3zLhvyLM43wwEQYDVR0lBAowCAYGBACRNwMAMBgGCCsGAQUFBwEDBAwwCjAIBgYEAI5GAQEwHwYDVR0jBBgwFoAU/fNQhDCO7COa9TOy44EH3eTvgK4wSgYIKwYBBQUHAQEEPjA8MDoGCCsGAQUFBzABhi5odHRwOi8vb2NzcC5ucmNhLWRzLmRlOjgwODAvb2NzcC1vY3NwcmVzcG9uZGVyMBIGA1UdIAQLMAkwBwYFKyQIAQEwGwYJKwYBBAHAbQMFBA4wDAYKKwYBBAHAbQMFATAMBgNVHRMBAf8EAjAAMA0GCSqGSIb3DQEBDQUAA4IBAQAMm2Fj5hoZBOeOOT4LPrky39cTYMPN1+Patx6BB+kuF/pXAI/GmDyOuFIZ+/Sf8bz336sbbIfnbDeV6Y6ZJvCnqzrUT8kBlf3+QTQ+JxOEYfw1bdRffjmYDCbM0S7Rw02eAaSykiHSkSp8kWA6rYWkhVakX/v/PdBUtkPHdq1P5ghLPx7Gk/ax/U3fDLlKGms5iJjz55AIMqlK4HWEc7xZk3QoD8w+lpRqT5QNYwex5ueXO/Mpd9ZtY5qm7bJKhRnKejQaaMO1frAWT+QM2Uve3TaZlgupa0K+FL9i532dMd/D4RjxtDTNfa5o8gcNFS6eDyuo0z8BJDp9LCLtNZYT");

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
    public void isBasicConstraintCaFalse() {
        assertTrue(certificateService.isBasicConstraintCaFalse(SI_CERTIFICATE));

        assertTrue(certificateService.isBasicConstraintCaFalse(DE_CERTIFICATE));
    }

    @Test
    public void hasAllowedKeyUsagesBits() {
        assertFalse(certificateService.hasAllowedKeyUsagesBits(SI_CERTIFICATE));

        assertTrue(certificateService.hasAllowedKeyUsagesBits(DE_CERTIFICATE));
    }

    @Test
    public void getSigningCertificate() throws Exception {

        File f = new File("src/test/resources/tsl-be.xml");
        CertificateToken cert = TLUtils.getSigningCertificate(f);
        Assert.assertNotNull(cert);

    }

}
