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
package eu.europa.ec.joinup.tsl.business.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBElement;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.esig.jaxb.tsl.OtherTSLPointerType;
import eu.europa.esig.jaxb.tsl.TrustStatusListType;
import eu.europa.esig.jaxb.v5.tsl.ObjectFactory;
import eu.europa.esig.jaxb.v5.tsl.OtherTSLPointerTypeV5;
import eu.europa.esig.jaxb.v5.tsl.TrustStatusListTypeV5;

@Service
public class TrustedListJaxbService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrustedListJaxbService.class);

    @Autowired
    @Qualifier(value = "tslMarshaller")
    private Unmarshaller tslUnmarshaller;

    @Autowired
    @Qualifier(value = "tslMarshaller")
    private Marshaller tslMarshaller;

    @Autowired
    @Qualifier(value = "tslMarshallerV5")
    private Unmarshaller tslUnmarshallerV5;

    @Autowired
    @Qualifier(value = "tslMarshallerV5")
    private Marshaller tslMarshallerV5;

    @SuppressWarnings("unchecked")
    public TrustStatusListType unmarshallTSL(InputStream is) throws XmlMappingException, IOException {
        JAXBElement<TrustStatusListType> jaxbElement = (JAXBElement<TrustStatusListType>) tslUnmarshaller.unmarshal(new StreamSource(is));
        return jaxbElement.getValue();
    }

    @SuppressWarnings("unchecked")
    public TrustStatusListTypeV5 unmarshallTSLV5(InputStream is) throws XmlMappingException, IOException {
        JAXBElement<TrustStatusListTypeV5> jaxbElement = (JAXBElement<TrustStatusListTypeV5>) tslUnmarshallerV5.unmarshal(new StreamSource(is));
        return jaxbElement.getValue();
    }

    public TrustStatusListType unmarshallTSL(File file) throws XmlMappingException, IOException {
        TrustStatusListType tsl = null;
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            tsl = unmarshallTSL(is);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return tsl;
    }

    public TrustStatusListTypeV5 unmarshallTSLV5(File file) throws XmlMappingException, IOException {
        TrustStatusListTypeV5 tsl = null;
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            tsl = unmarshallTSLV5(is);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return tsl;
    }

    public TrustStatusListType unmarshallTSL(byte[] content) throws XmlMappingException, IOException {
        TrustStatusListType tsl = null;
        InputStream is = null;
        try {
            is = new ByteArrayInputStream(content);
            tsl = unmarshallTSL(is);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return tsl;
    }

    public TrustStatusListTypeV5 unmarshallTSLV5(byte[] content) throws XmlMappingException, IOException {
        TrustStatusListTypeV5 tsl = null;
        InputStream is = null;
        try {
            is = new ByteArrayInputStream(content);
            tsl = unmarshallTSLV5(is);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return tsl;
    }

    public void marshallTSL(TrustStatusListTypeV5 tsl, OutputStream os) throws XmlMappingException, IOException {
        ObjectFactory factory = new ObjectFactory();
        JAXBElement<TrustStatusListTypeV5> jaxbElement = factory.createTrustServiceStatusList(tsl);
        tslMarshallerV5.marshal(jaxbElement, new StreamResult(os));
    }

    public byte[] marshallToBytesAsV5(TL tl) {
        byte[] byteArray = null;
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            marshallTSL(tl.asTSLTypeV5(), out);
            byteArray = out.toByteArray();
        } catch (Exception e) {
            LOGGER.error("Unable to marshal TL DTO : " + e.getMessage(), e);
        }
        return byteArray;
    }

    /**
     * POINTER TO OTHER TSL
     * USEFUL FOR NOTIFICATION PROCESS
     */
    @SuppressWarnings("unchecked")
    public OtherTSLPointerType unmarshallPointer(InputStream is) throws XmlMappingException, IOException {
        JAXBElement<OtherTSLPointerType> jaxbElement = (JAXBElement<OtherTSLPointerType>) tslUnmarshaller.unmarshal(new StreamSource(is));
        return jaxbElement.getValue();
    }

    public OtherTSLPointerType unmarshallPointer(String str) throws XmlMappingException, IOException {
        return unmarshallPointer(new ByteArrayInputStream(str.getBytes()));
    }

    public OtherTSLPointerType unmarshallPointer(byte[] content) throws XmlMappingException, IOException {
        OtherTSLPointerType pointer = null;
        InputStream is = null;
        try {
            is = new ByteArrayInputStream(content);
            pointer = unmarshallPointer(is);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return pointer;
    }

    public void marshallPointer(OtherTSLPointerTypeV5 pointer, OutputStream os) throws XmlMappingException, IOException {
        ObjectFactory factory = new ObjectFactory();
        JAXBElement<OtherTSLPointerTypeV5> jaxbElement = factory.createOtherTSLPointer(pointer);
        tslMarshallerV5.marshal(jaxbElement, new StreamResult(os));
    }

    public byte[] marshallPointerToBytesAsV5(TLPointersToOtherTSL pointer) {
        byte[] byteArray = null;
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            marshallPointer(pointer.asTSLTypeV5(), out);
            byteArray = out.toByteArray();
        } catch (Exception e) {
            LOGGER.error("Unable to marshal POINTER DTO : " + e.getMessage(), e);
        }
        return byteArray;
    }
}
