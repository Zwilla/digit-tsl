package eu.europa.ec.joinup.tsl.business.util;

import java.util.HashMap;
import java.util.Map;

import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.xml.security.Init;

import eu.europa.esig.dss.NamespaceContextMap;
import eu.europa.esig.dss.XAdESNamespaces;

public final class TLXmlUtils {

    private static final XPathFactory factory = XPathFactory.newInstance();

    private static final NamespaceContextMap namespacePrefixMapper;

    private static final Map<String, String> namespaces;

    static {
        Init.init();

        namespacePrefixMapper = new NamespaceContextMap();
        namespaces = new HashMap<>();
        registerDefaultNamespaces();
    }

    private TLXmlUtils() {
    }

    /**
     * This method registers the default namespaces.
     */
    private static void registerDefaultNamespaces() {

        registerNamespace("xml", "http://www.w3.org/XML/1998/namespace"); // xml:lang,...

        registerNamespace("ds", XMLSignature.XMLNS);
        registerNamespace("dsig", XMLSignature.XMLNS);
        registerNamespace("xades", XAdESNamespaces.XAdES); // 1.3.2
        registerNamespace("xades141", XAdESNamespaces.XAdES141);
        registerNamespace("xades122", XAdESNamespaces.XAdES122);
        registerNamespace("xades111", XAdESNamespaces.XAdES111);

        registerNamespace("tsl", TLNamespaces.TSL_NAMESPACE);
        registerNamespace("tslx", TLNamespaces.TSLX_NAMESPACE);
        registerNamespace("ecc", TLNamespaces.ECC_NAMESPACE);
    }

    /**
     * This method allows to register a namespace and associated prefix. If the prefix exists already it is replaced.
     *
     * @param prefix
     *            namespace prefix
     * @param namespace
     *            namespace
     */
    public static void registerNamespace(final String prefix, final String namespace) {
        final String put = namespaces.put(prefix, namespace);
        namespacePrefixMapper.registerNamespace(prefix, namespace);
    }

    /**
     * This method builds a XPathExpression with registred namespaces
     *
     * @param xpathString
     *            XPath query string
     * @return
     */
    public static XPathExpression createXPathExpression(String xpathString) throws XPathExpressionException {
        XPath xpath = factory.newXPath();
        xpath.setNamespaceContext(namespacePrefixMapper);
        return xpath.compile(xpathString);
    }

}
