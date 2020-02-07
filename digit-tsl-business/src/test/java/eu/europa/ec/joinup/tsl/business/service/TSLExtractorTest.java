package eu.europa.ec.joinup.tsl.business.service;

import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.ec.joinup.tsl.model.enums.MimeType;
import eu.europa.esig.jaxb.tsl.TrustStatusListType;

public class TSLExtractorTest extends AbstractSpringTest {

    @Autowired
    private TSLExtractor extractor;

    @Autowired
    private TrustedListJaxbService jaxbService;

    @Test
    public void getTLPointers() throws Exception {
        FileInputStream is = new FileInputStream("src/test/resources/lotl.xml");
        TrustStatusListType tsl = jaxbService.unmarshallTSL(is);
        List<TLPointersToOtherTSL> tlPointers = extractor.getTLPointers(tsl);
        assertTrue(CollectionUtils.isNotEmpty(tlPointers));

        boolean detectPdf = false;
        boolean detectXml = false;
        boolean detectBelgium = false;
        for (TLPointersToOtherTSL pointer : tlPointers) {
            if ("BE".equals(pointer.getSchemeTerritory())) {
                detectBelgium = true;
            }
            if (MimeType.XML.equals(pointer.getMimeType())) {
                detectXml = true;
            }
            if (MimeType.PDF.equals(pointer.getMimeType())) {
                detectPdf = true;
            }
        }
        assertTrue(detectPdf);
        assertTrue(detectXml);
        assertTrue(detectBelgium);

        IOUtils.closeQuietly(is);
    }

}
