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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLDigitalIdentification;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLElectronicAddress;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLInformationExtension;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLInformationUri;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLName;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPostalAddress;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceDto;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;
import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;

/**
 * Trust service provider edition/deletion management
 */
@Service
@Transactional(value = TxType.REQUIRED)
public class TlEditServiceProviderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TlEditServiceProviderService.class);
    @Autowired
    private TLRepository tlRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private TLService tlService;

    @Autowired
    private TrustedListJaxbService jaxbService;

    public TLServiceProvider edit(int id, TLServiceProvider serviceProvider) {
        DBTrustedLists tldb = tlRepository.findOne(id);
        if (TLStatus.DRAFT.equals(tldb.getStatus())) {
            boolean newServiceProvider = true;
            // CHANGES IN XML FILE
            TL tl = tlService.getTL(id);
            TLServiceProvider serviceProviderUpdated = null;
            for (TLServiceProvider tlServiceProvider : tl.getServiceProviders()) {
                if (tlServiceProvider.getId().equalsIgnoreCase(serviceProvider.getId())) {
                    newServiceProvider = false;
                    if (serviceProvider.getTSPElectronic() != null) {
                        tlServiceProvider.setTSPElectronic(serviceProvider.getTSPElectronic());
                    } else {
                        tlServiceProvider.setTSPElectronic(new ArrayList<TLElectronicAddress>());
                    }

                    if (serviceProvider.getTSPInfoUri() != null) {
                        tlServiceProvider.setTSPInfoUri(serviceProvider.getTSPInfoUri());
                    } else {
                        tlServiceProvider.setTSPInfoUri(new ArrayList<TLInformationUri>());
                    }

                    if (serviceProvider.getTSPName() != null) {
                        tlServiceProvider.setTSPName(serviceProvider.getTSPName());
                    } else {
                        tlServiceProvider.setTSPName(new ArrayList<TLName>());
                    }

                    if (serviceProvider.getTSPPostal() != null) {
                        tlServiceProvider.setTSPPostal(serviceProvider.getTSPPostal());
                    } else {
                        tlServiceProvider.setTSPPostal(new ArrayList<TLPostalAddress>());
                    }

                    if (serviceProvider.getTSPTradeName() != null) {
                        tlServiceProvider.setTSPTradeName(serviceProvider.getTSPTradeName());
                    } else {
                        tlServiceProvider.setTSPTradeName(new ArrayList<TLName>());
                    }

                    // No update
                    if (serviceProvider.getTSPExtension() != null) {
                        tlServiceProvider.setTSPExtension(serviceProvider.getTSPExtension());
                    } else {
                        tlServiceProvider.setTSPExtension(new ArrayList<TLInformationExtension>());
                    }

                    if (serviceProvider.getTSPServices() != null) {
                        List<TLServiceDto> newServiceList = new ArrayList<>();
                        for (TLServiceDto service : serviceProvider.getTSPServices()) {
                            if (service.getDigitalIdentification() != null) {
                                List<TLDigitalIdentification> digtalList = service.getDigitalIdentification();
                                for (TLDigitalIdentification tlDigitalId : digtalList) {

                                    List<Object> newOtherList = new ArrayList<>();
                                    if (tlDigitalId.getOther() != null) {
                                        for (Object obj : tlDigitalId.getOther()) {
                                            LOGGER.debug("****** OBJ BEFORE CASTING : " + obj.toString());
                                            if (obj instanceof String) {
                                                try {
                                                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                                                    dbf.setNamespaceAware(true);
                                                    Element newOtherElement = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(((String) obj).getBytes("UTF-16"))).getDocumentElement();
                                                    LOGGER.warn("other element is a String, casting to Element");
                                                    obj = newOtherElement;
                                                } catch (Exception e) {
                                                    LOGGER.warn("No need to be cast : " + e.getMessage() + " / " + e.toString());
                                                }
                                            }
                                            newOtherList.add(obj);
                                            LOGGER.debug("****** OBJ AFTER CASTING: " + obj.toString());

                                        }
                                        tlDigitalId.setOther(newOtherList);
                                    }
                                }
                                service.setDigitalIdentification(digtalList);
                            }
                            newServiceList.add(service);
                        }
                        tlServiceProvider.setTSPServices(newServiceList);
                    } else {
                        tlServiceProvider.setTSPServices(new ArrayList<eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceDto>());
                    }
                    serviceProviderUpdated = tlServiceProvider;
                }
            }

            if (newServiceProvider) {
                if (tl.getServiceProviders() == null) {
                    tl.setServiceProviders(new ArrayList<TLServiceProvider>());
                }
                tl.getServiceProviders().add(serviceProvider);
                serviceProviderUpdated = serviceProvider;
            }

            byte[] updatedTL = jaxbService.marshallToBytesAsV5(tl);
            if (ArrayUtils.isNotEmpty(updatedTL)) {
                DBFiles xmlFile = tldb.getXmlFile();
                xmlFile.setLocalPath(fileService.storeNewDraftTL(xmlFile.getMimeTypeFile(), updatedTL, tldb.getTerritory().getCodeTerritory(), xmlFile.getLocalPath()));
                // tldb.setXmlFile(xmlFile);
                tldb.setLastEditedDate(new Date());
                tlRepository.save(tldb);
                return serviceProviderUpdated;
            }

        }
        return null;
    }

    public int delete(int id, TLServiceProvider serviceProvider) {
        DBTrustedLists tldb = tlRepository.findOne(id);
        int nbreRemove = 0;
        if (TLStatus.DRAFT.equals(tldb.getStatus())) {

            TL tl = tlService.getTL(id);

            Iterator<TLServiceProvider> it = tl.getServiceProviders().iterator();
            while (it.hasNext()) {
                TLServiceProvider p = it.next();
                if (((p != null) & (p.getId() != null)) && p.getId().equalsIgnoreCase(serviceProvider.getId())) {
                    it.remove();
                    nbreRemove++;
                }
            }

            byte[] updatedTL = jaxbService.marshallToBytesAsV5(tl);
            if (ArrayUtils.isNotEmpty(updatedTL)) {
                DBFiles xmlFile = tldb.getXmlFile();
                xmlFile.setLocalPath(fileService.storeNewDraftTL(xmlFile.getMimeTypeFile(), updatedTL, tldb.getTerritory().getCodeTerritory(), xmlFile.getLocalPath()));
                // tldb.setXmlFile(xmlFile);
                tldb.setLastEditedDate(new Date());
                tlRepository.save(tldb);
            }

        }
        return nbreRemove;
    }
}
