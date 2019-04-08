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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.europa.ec.joinup.tsl.checker.controller;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import org.etsi.uri.x02231.v2.AdditionalInformationType;
import org.etsi.uri.x02231.v2.AnyType;
import org.etsi.uri.x02231.v2.DigitalIdentityListType;
import org.etsi.uri.x02231.v2.DigitalIdentityType;
import org.etsi.uri.x02231.v2.OtherTSLPointerType;
import org.etsi.uri.x02231.v2.OtherTSLPointersType;
import org.etsi.uri.x02231.v2.ServiceDigitalIdentityListType;
import org.etsi.uri.x02231.v2.TSLSchemeInformationType;
import org.etsi.uri.x02231.v2.TrustServiceStatusListDocument;
import org.etsi.uri.x02231.v2.TrustStatusListType;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ext.DefaultHandler2;

import edu.upc.ac.tsl.conformance.tools.ByteArray;
import edu.upc.ac.xml.UPCXMLUtils;

/**
 * Convenience class for generating a Map whose keys are values of SchemeTerritory components and whose values are treeSets of ByteArray instances
 * that encapsulate DER-encoded X509 certificates that, according to the ECLOL may sign the Trusted List of the country indicated by the key value
 *
 */
public class EUMSTLsSigningCertsAsInECLOLReader {

    private InputStream isECLOL;

    /**
     * Constructor
     *
     * @param arg
     *            inputstream for the file where the ECLOL may be accessed
     */
    public EUMSTLsSigningCertsAsInECLOLReader(InputStream arg) {
        this.isECLOL = arg;
    }

    /**
     * Method that generates the Map whose keys are values of SchemeTerritory components and whose values are treeSets of ByteArray instances that
     * encapsulate DER-encoded X509 certificates that, according to the ECLOL may sign the Trusted List of the country indicated by the key value
     *
     * @return the aforementioned map
     * @throws Exception
     *             if any problem occurs while processing the ECLOL
     */
    public Map<String, TreeSet<ByteArray>> getEUMSTLsSigingCertsByCountryCode() throws Exception {
        Map<String, TreeSet<ByteArray>> result = new HashMap<String, TreeSet<ByteArray>>();
        TrustServiceStatusListDocument tslDoc = TrustServiceStatusListDocument.Factory.parse(this.isECLOL);
        if (tslDoc == null) {
            return result;
        }
        TrustStatusListType tsl = tslDoc.getTrustServiceStatusList();
        if (tsl == null) {
            return result;
        }
        TSLSchemeInformationType schi = tsl.getSchemeInformation();
        if (schi == null) {
            return result;
        }
        OtherTSLPointersType allPtrs = schi.getPointersToOtherTSL();
        if (allPtrs == null) {
            return result;
        }
        OtherTSLPointerType[] ptrs = allPtrs.getOtherTSLPointerArray();
        if ((ptrs == null) || (ptrs.length == 0)) {
            return result;
        }
        for (int i = 0; i < ptrs.length; i++) {
            this.addCertsFromPointer(result, ptrs[i], i + 1);
        }
        return result;
    }

    private void addCertsFromPointer(Map<String, TreeSet<ByteArray>> result, OtherTSLPointerType ptr, int numPointer) {
        AdditionalInformationType addInfo = ptr.getAdditionalInformation();
        if (addInfo == null) {
            System.out.println("No AdditionalInfo in pointer number: " + numPointer);
            return;
        }
        String schemeTerritory = this.getSchemeTerritory(addInfo);
        if (schemeTerritory == null) {
            System.out.println("No SchemeTerritory in pointer number: " + numPointer);
            return;
        }
        if (this.isPointingToXMLEUMSList(addInfo)) {
            ServiceDigitalIdentityListType sdisList = ptr.getServiceDigitalIdentities();
            if (sdisList == null) {
                System.out.println("No digital identities in pointer number: " + numPointer);
                return;
            }
            this.getCertificatesForTerritory(result, schemeTerritory, sdisList, numPointer);
        }
    }

    private boolean isPointingToXMLEUMSList(AdditionalInformationType addInfo) {
        AnyType[] othersArr = addInfo.getOtherInformationArray();
        if ((othersArr == null) || (othersArr.length == 0)) {
            return false;
        }
        for (AnyType other : othersArr) {
            Element elOther = (Element) other.getDomNode();
            NodeList mimeTypes = elOther.getElementsByTagNameNS("http://uri.etsi.org/02231/v2/additionaltypes#", "MimeType");
            if ((mimeTypes != null) && (mimeTypes.getLength() > 0)) {
                Element mimeTypeEl = (Element) mimeTypes.item(0);
                String mimeType = UPCXMLUtils.getTextContent(mimeTypeEl);
                if (mimeType.equals("application/vnd.etsi.tsl+xml")) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    private void getCertificatesForTerritory(Map<String, TreeSet<ByteArray>> result, String schemeTerritory, ServiceDigitalIdentityListType sdisList, int numPointer) {
        DigitalIdentityListType[] digIds = sdisList.getServiceDigitalIdentityArray();
        TreeSet<ByteArray> set = new TreeSet<ByteArray>();
        result.put(schemeTerritory, set);
        if ((digIds == null) || (digIds.length == 0)) {
            return;
        }
        boolean found = false;
        for (DigitalIdentityListType digId : digIds) {
            DigitalIdentityType[] idArr = digId.getDigitalIdArray();
            if (idArr != null) {
                for (DigitalIdentityType id : idArr) {
                    byte[] certBytes = id.getX509Certificate();
                    if (certBytes != null) {
                        found = true;
                        set.add(new ByteArray(certBytes));
                    }
                }
            }
        }
        if (!found) {
            System.out.println("No X509 certificates found for pointer to " + "TL from " + schemeTerritory + " in pointer number " + numPointer);
        }
    }

    private String getSchemeTerritory(AdditionalInformationType addInfo) {
        AnyType[] othersArr = addInfo.getOtherInformationArray();
        if ((othersArr == null) || (othersArr.length == 0)) {
            return null;
        }
        for (AnyType other : othersArr) {
            Element elOther = (Element) other.getDomNode();
            NodeList schTerrs = elOther.getElementsByTagNameNS("http://uri.etsi.org/02231/v2#", "SchemeTerritory");
            if ((schTerrs != null) && (schTerrs.getLength() > 0)) {
                Element schTerr = (Element) schTerrs.item(0);
                return UPCXMLUtils.getTextContent(schTerr);
            }
        }
        return null;
    }

    protected Element getRootElementFromInputStream(InputStream input) throws Exception {

        Element rootElement = null;
        javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setAttribute("http://xml.org/sax/features/namespaces", Boolean.TRUE);
        javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
        db.setErrorHandler(new DefaultHandler2());
        org.w3c.dom.Document doc = db.parse(input);
        rootElement = doc.getDocumentElement();
        return rootElement;
    }

}
