package eu.europa.ec.joinup.tsl.business.service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.esig.jaxb.tsl.OtherTSLPointerType;
import eu.europa.esig.jaxb.tsl.TrustStatusListType;
import eu.europa.esig.jaxb.v5.tsl.TrustStatusListTypeV5;

public class JaxbServiceTest extends AbstractSpringTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JaxbServiceTest.class);

    @Autowired
    private TrustedListJaxbService jaxbService;

    @Autowired
    private TLBuilder tlBuilder;

    @Test
    public void testUnmashallMarshallTSL() throws Exception {
        InputStream is = new FileInputStream("src/test/resources/lotl.xml");
        TrustStatusListType tsl = jaxbService.unmarshallTSL(is);
        Assert.assertNotNull(tsl);

        // COULD NOT MARSHALL A V4

        // jaxbService.marshallTSL(tsl, os);
        // String result = os.toString();
        // IOUtils.closeQuietly(os);
        // IOUtils.closeQuietly(is);
        // assertTrue(StringUtils.isNotEmpty(result));
    }

    @Test
    public void testUnmashallMarshallBE() throws Exception {
        InputStream is = new FileInputStream("src/test/resources/tsl/BE-TEST/2016-10-13_12-55-38.xml");
        OutputStream os = new ByteArrayOutputStream();
        TrustStatusListType tsl = jaxbService.unmarshallTSL(is);

        TL tl = tlBuilder.buildTLV4(1, tsl);

        TrustStatusListTypeV5 asTSLTypeV5 = tl.asTSLTypeV5();
        jaxbService.marshallTSL(asTSLTypeV5, os);
        IOUtils.closeQuietly(os);
        IOUtils.closeQuietly(is);

        LOGGER.info(os.toString());
    }

    @Test
    public void testUnmashallMarshallSK() throws Exception {
        InputStream is = new FileInputStream("src/test/resources/tsl/SK/2016-10-13_13-05-40.xml");
        OutputStream os = new ByteArrayOutputStream();
        TrustStatusListType tsl = jaxbService.unmarshallTSL(is);

        TL tl = tlBuilder.buildTLV4(1, tsl);

        TrustStatusListTypeV5 asTSLTypeV5 = tl.asTSLTypeV5();
        jaxbService.marshallTSL(asTSLTypeV5, os);
        IOUtils.closeQuietly(os);
        IOUtils.closeQuietly(is);

        LOGGER.info(os.toString());
    }

    @Test
    public void testUnmashallMarshallATWithServiceHistory() throws Exception {
        InputStream is = new FileInputStream("src/test/resources/tsl/AT/2016-10-13_13-09-04.xml");
        OutputStream os = new ByteArrayOutputStream();
        TrustStatusListType tsl = jaxbService.unmarshallTSL(is);

        TL tl = tlBuilder.buildTLV4(1, tsl);

        TrustStatusListTypeV5 asTSLTypeV5 = tl.asTSLTypeV5();
        jaxbService.marshallTSL(asTSLTypeV5, os);
        IOUtils.closeQuietly(os);
        IOUtils.closeQuietly(is);

        LOGGER.info(os.toString());
    }

    @Test
    public void testUnmarshallHrPointerToOtherTsl() throws Exception {
        InputStream is = new FileInputStream("src/test/resources/HrPointerToOtherTsl.xml");
        OtherTSLPointerType result = jaxbService.unmarshallPointer(is);
        IOUtils.closeQuietly(is);
        Assert.assertNotNull(result);
        Assert.assertEquals("http://tsl.belgium.be/tsl-be.pdf", result.getTSLLocation());
        Assert.assertEquals(5, result.getServiceDigitalIdentities().getServiceDigitalIdentity().size());
    }

    @Test
    public void testUnmarshallMpPointerToOtherTsl() throws Exception {
        InputStream is = new FileInputStream("src/test/resources/MpPointerToOtherTsl.xml");
        OtherTSLPointerType result = jaxbService.unmarshallPointer(is);
        IOUtils.closeQuietly(is);
        Assert.assertNotNull(result);
        Assert.assertEquals("http://tsl.belgium.be/tsl-be.xml", result.getTSLLocation());
        Assert.assertEquals(5, result.getServiceDigitalIdentities().getServiceDigitalIdentity().size());
    }

}
