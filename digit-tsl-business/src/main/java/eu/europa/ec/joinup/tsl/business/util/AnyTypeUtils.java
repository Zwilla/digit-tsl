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

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xerces.dom.ElementNSImpl;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;

public class AnyTypeUtils {

    /**
     * Convert Other tag object to String
     *
     * @param tmpObject
     * @return
     */
    public static String convertOtherTag(Object tmpObject) {
        if (tmpObject instanceof ElementNSImpl) {
            ElementNSImpl elementNS = (ElementNSImpl) tmpObject;
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
