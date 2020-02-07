package eu.europa.ec.joinup.tsl.business.service;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.XmlMappingException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.util.PDFReportMultimapConverter;
import eu.europa.esig.jaxb.tsl.TrustStatusListType;

public class XStreamTLIdTest extends AbstractSpringTest {

    @Autowired
    private TLBuilder tlBuilder;

    @Autowired
    private TrustedListJaxbService jaxbService;

    @Test
    public void test() throws XmlMappingException, IOException {
        TrustStatusListType tsl = jaxbService.unmarshallTSL(new File("src/test/resources/tsl/LU/2016-10-13_12-56-25.xml"));
        TL tl = tlBuilder.buildTLV4(1000, tsl);

        XStream stream = new XStream(new DomDriver("UTF-8"));
        stream.setMode(XStream.NO_REFERENCES);
        stream.processAnnotations(TL.class);
        stream.registerConverter(new PDFReportMultimapConverter(stream.getMapper()));

        String xml = stream.toXML(tl);
        System.out.println("******************************\n\n\n");
        System.out.println(xml);
    }

    @Test
    public void testClean() throws XmlMappingException, IOException {
        TrustStatusListType tsl = jaxbService.unmarshallTSL(new File("src/test/resources/tsl/LU/clean_lu.xml"));
        TL tl = tlBuilder.buildTLV4(2000, tsl);

        XStream stream = new XStream(new DomDriver("UTF-8"));
        stream.setMode(XStream.NO_REFERENCES);
        stream.processAnnotations(TL.class);
        stream.registerConverter(new PDFReportMultimapConverter(stream.getMapper()));

        String xml = stream.toXML(tl);
        System.out.println("******************************\n\n\n");
        System.out.println(xml);
    }
}
