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

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLDigitalIdentification;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLName;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceDto;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceExtension;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceHistory;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;
import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;

/**
 * TL service history edition/deletion management
 */
@Service
@Transactional(value = TxType.REQUIRED)
public class TlEditHistoryService {

    @Autowired
    private TLRepository tlRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private TLService tlService;

    @Autowired
    private TrustedListJaxbService jaxbService;

    public TLServiceHistory edit(int id, TLServiceHistory history, List<Integer> parentIndex) {
        DBTrustedLists tldb = tlRepository.findOne(id);
        if (TLStatus.DRAFT.equals(tldb.getStatus())) {
            boolean newHistory = true;
            // CHANGES IN XML FILE
            TL tl = tlService.getTL(id);
            TLServiceHistory historyUpdated = null;
            if (history.getId() != null) {
                for (int i = 0; i < tl.getServiceProviders().size(); i++) {
                    TLServiceProvider tlServiceProvider = tl.getServiceProviders().get(i);
                    if (history.getId().startsWith(tlServiceProvider.getId())) {
                        for (int j = 0; j < tlServiceProvider.getTSPServices().size(); j++) {
                            TLServiceDto tlService = tlServiceProvider.getTSPServices().get(j);
                            if (history.getId().startsWith(tlService.getId())) {
                                for (int k = 0; k < tlService.getHistory().size(); k++) {
                                    TLServiceHistory tlHistory = tlService.getHistory().get(k);
                                    if (history.getId().equalsIgnoreCase(tlHistory.getId())) {
                                        newHistory = false;

                                        if (history.getCurrentStatus() != null) {
                                            tlHistory.setCurrentStatus(history.getCurrentStatus());
                                        } else {
                                            tlHistory.setCurrentStatus("");
                                        }
                                        if (history.getCurrentStatusStartingDate() != null) {
                                            tlHistory.setCurrentStatusStartingDate(history.getCurrentStatusStartingDate());
                                        } else {
                                            tlHistory.setCurrentStatusStartingDate(null);
                                        }
                                        if (history.getDigitalIdentification() != null) {
                                            tlHistory.setDigitalIdentification(history.getDigitalIdentification());
                                        } else {
                                            tlHistory.setDigitalIdentification(new ArrayList<TLDigitalIdentification>());
                                        }
                                        if (history.getExtension() != null) {
                                            tlHistory.setExtension(history.getExtension());
                                        } else {
                                            tlHistory.setExtension(new ArrayList<TLServiceExtension>());
                                        }
                                        if (history.getServiceName() != null) {
                                            tlHistory.setServiceName(history.getServiceName());
                                        } else {
                                            tlHistory.setServiceName(new ArrayList<TLName>());
                                        }
                                        if (history.getTypeIdentifier() != null) {
                                            tlHistory.setTypeIdentifier(history.getTypeIdentifier());
                                        } else {
                                            tlHistory.setTypeIdentifier("");
                                        }
                                        historyUpdated = tlHistory;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (newHistory) {
                if (tl.getServiceProviders().get(parentIndex.get(0)).getTSPServices().get(parentIndex.get(1)).getHistory() == null) {
                    tl.getServiceProviders().get(parentIndex.get(0)).getTSPServices().get(parentIndex.get(1)).setHistory(new ArrayList<TLServiceHistory>());
                }
                tl.getServiceProviders().get(parentIndex.get(0)).getTSPServices().get(parentIndex.get(1)).getHistory().add(0, history);
                historyUpdated = history;
            }

            byte[] updatedTL = jaxbService.marshallToBytesAsV5(tl);
            if (ArrayUtils.isNotEmpty(updatedTL)) {
                DBFiles xmlFile = tldb.getXmlFile();
                xmlFile.setLocalPath(fileService.storeNewDraftTL(xmlFile.getMimeTypeFile(), updatedTL, tldb.getTerritory().getCodeTerritory(), xmlFile.getLocalPath()));
                // tldb.setXmlFile(xmlFile);
                tldb.setLastEditedDate(new Date());
                tlRepository.save(tldb);
                return historyUpdated;
            }
        }
        return null;
    }

    public int delete(int id, TLServiceHistory history) {
        DBTrustedLists tldb = tlRepository.findOne(id);
        int nbreRemove = 0;
        if (TLStatus.DRAFT.equals(tldb.getStatus())) {

            TL tl = tlService.getTL(id);
            if (tl.getServiceProviders() != null) {
                Iterator<TLServiceProvider> itProvider = tl.getServiceProviders().iterator();
                while (itProvider.hasNext()) {
                    TLServiceProvider tlProvider = itProvider.next();
                    if (tlProvider.getTSPServices() != null) {
                        Iterator<TLServiceDto> itService = tlProvider.getTSPServices().iterator();
                        while (itService.hasNext()) {
                            TLServiceDto tlService = itService.next();
                            if (tlService.getHistory() != null) {
                                Iterator<TLServiceHistory> itHistory = tlService.getHistory().iterator();
                                while (itHistory.hasNext()) {
                                    TLServiceHistory tlHistory = itHistory.next();
                                    if (tlHistory.getId().equalsIgnoreCase(history.getId())) {
                                        itHistory.remove();
                                        nbreRemove++;
                                    }
                                }
                            }
                        }
                    }
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
