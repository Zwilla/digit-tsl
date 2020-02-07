package eu.europa.ec.joinup.tsl.business.util;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class AnyTypeUtils {

    /**
     * Convert Other tag object to String
     *
     * @param tmpObject
     * @return
     */
    public static String convertOtherTag(Object tmpObject) {
        if (tmpObject instanceof Element) {
            Element elementNS = (Element) tmpObject;
            Document doc = elementNS.getOwnerDocument();
            try {
                StringWriter sw = new StringWriter();
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                transformer.setOutputProperty(OutputKeys.METHOD, "xml");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

                transformer.transform(new DOMSource(doc), new StreamResult(sw));
                String result = sw.toString();
                if (StringUtils.isEmpty(result)) {
                    return null;
                }
                return result;
            } catch (Exception ex) {
                return null;
            }
        } else {
            return tmpObject.toString().replaceAll("(\\r|\\n)", "").trim();
        }
    }

    /**
     * Clean Other tag and remove chariot return, blank space
     *
     * @param str
     */
    public static String cleanOtherTag(String str) {
        return str.replaceAll("(\\r|\\n)", "").trim();

    }
}
