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

    private static NamespaceContextMap namespacePrefixMapper;

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
     * @return true if this map did not already contain the specified element
     */
    public static boolean registerNamespace(final String prefix, final String namespace) {
        final String put = namespaces.put(prefix, namespace);
        namespacePrefixMapper.registerNamespace(prefix, namespace);
        return put == null;
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
