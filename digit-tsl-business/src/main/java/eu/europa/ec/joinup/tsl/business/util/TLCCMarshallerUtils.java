package eu.europa.ec.joinup.tsl.business.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;

import eu.europa.ec.joinup.tsl.business.dto.tlcc.TLCCResults;

/**
 * Unmarshall TLCC XML
 */
public class TLCCMarshallerUtils {

    public static TLCCResults unmarshallTlccXml(String xml) throws Exception {
        InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        JAXBContext jc = JAXBContext.newInstance(TLCCResults.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        XMLInputFactory xif = XMLInputFactory.newInstance();
        xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        XMLStreamReader xsr = xif.createXMLStreamReader(new StreamSource(is));
        TLCCResults tlccResults = (TLCCResults) unmarshaller.unmarshal(xsr);
        return tlccResults;
    }
}
