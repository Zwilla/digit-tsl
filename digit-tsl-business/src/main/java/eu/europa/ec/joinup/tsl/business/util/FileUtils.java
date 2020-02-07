package eu.europa.ec.joinup.tsl.business.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Get trusted list file version from the XML file
 */
public final class FileUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    private FileUtils() {

    }

    public static String getTlVersion(File xmlFile) {
        String version = "";
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document xmlDocument = builder.parse(xmlFile);

            XPath xPath = XPathFactory.newInstance().newXPath();
            String expression = "//*[starts-with(local-name(),\"TSLVersionIdentifier\")]";
            version = xPath.compile(expression).evaluate(xmlDocument);
            LOGGER.debug("TSL VERSION --> " + version + " <-- processing");
        }
        catch (XPathExpressionException | ParserConfigurationException | IOException | SAXException e) {
            LOGGER.error(e.getMessage());
        }

        return version;
    }

}
