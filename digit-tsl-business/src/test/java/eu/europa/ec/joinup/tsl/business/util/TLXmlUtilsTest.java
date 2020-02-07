package eu.europa.ec.joinup.tsl.business.util;

import static org.junit.Assert.assertNotNull;

import javax.xml.xpath.XPathExpressionException;

import org.junit.Test;

public class TLXmlUtilsTest {

    @Test
    public void createXPathExpression() throws XPathExpressionException {
        assertNotNull(TLXmlUtils.createXPathExpression("/TrustServiceStatusList"));
        assertNotNull(TLXmlUtils.createXPathExpression("/tsl:TrustServiceStatusList"));
        assertNotNull(TLXmlUtils.createXPathExpression("/tsl:TrustServiceStatusList/tsl:SchemeInformation"));
        assertNotNull(TLXmlUtils.createXPathExpression("/tsl:TrustServiceStatusList/tsl:SchemeInformation/tsl:TSLSequenceNumber"));

        assertNotNull(TLXmlUtils.createXPathExpression("/tslx:MimeType"));
        assertNotNull(TLXmlUtils.createXPathExpression("/MimeType"));

        assertNotNull(TLXmlUtils.createXPathExpression("/ecc:Qualifications/ecc:QualificationElement"));
        assertNotNull(TLXmlUtils.createXPathExpression("/Qualifications/QualificationElement"));
    }

    /**
     * Unknown namespace toto
     */
    @Test(expected = XPathExpressionException.class)
    public void createXPathExpressionUnknowNamespace() throws XPathExpressionException {
        TLXmlUtils.createXPathExpression("/toto:TrustServiceStatusList");
    }

}
