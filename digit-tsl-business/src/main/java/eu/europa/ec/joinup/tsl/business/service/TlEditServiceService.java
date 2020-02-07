package eu.europa.ec.joinup.tsl.business.service;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLDefinitionUri;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLDigitalIdentification;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLName;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceDto;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceExtension;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceHistory;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLSupplyPoint;
import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;

/**
 * TL service edition/deletion management
 */
@Service
@Transactional(value = TxType.REQUIRED)
public class TlEditServiceService {

    @Autowired
    private TLRepository tlRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private TLService tlService;

    @Autowired
    private TrustedListJaxbService jaxbService;

    private static final Logger LOGGER = LoggerFactory.getLogger(TlEditServiceService.class);

    public TLServiceDto edit(int id, TLServiceDto service, List<Integer> parentIndex) {

        DBTrustedLists tldb = tlRepository.findOne(id);
        if (TLStatus.DRAFT.equals(tldb.getStatus())) {
            boolean newService = true;
            // CHANGES IN XML FILE
            TL tl = tlService.getTL(id);
            TLServiceDto serviceUpdated = null;
            if (service.getId() != null) {
                for (int i = 0; i < tl.getServiceProviders().size(); i++) {
                    TLServiceProvider tlServiceProvider = tl.getServiceProviders().get(i);
                    if (service.getId().startsWith(tlServiceProvider.getId())) {
                        for (int j = 0; j < tlServiceProvider.getTSPServices().size(); j++) {
                            TLServiceDto tlService = tlServiceProvider.getTSPServices().get(j);
                            if (tlService.getId().equalsIgnoreCase(service.getId())) {
                                newService = false;

                                if (service.getTypeIdentifier() != null) {
                                    tlService.setTypeIdentifier(service.getTypeIdentifier());
                                } else {
                                    tlService.setTypeIdentifier("");
                                }
                                if (service.getServiceName() != null) {
                                    tlService.setServiceName(service.getServiceName());
                                } else {
                                    tlService.setServiceName(new ArrayList<TLName>());
                                }
                                if (service.getCurrentStatus() != null) {
                                    tlService.setCurrentStatus(service.getCurrentStatus());
                                } else {
                                    tlService.setCurrentStatus("");
                                }
                                if (service.getCurrentStatusStartingDate() != null) {
                                    tlService.setCurrentStatusStartingDate(service.getCurrentStatusStartingDate());
                                } else {
                                    tlService.setCurrentStatusStartingDate(null);
                                }
                                if (service.getSchemeDefinitionUri() != null) {
                                    tlService.setSchemeDefinitionUri(service.getSchemeDefinitionUri());
                                } else {
                                    tlService.setSchemeDefinitionUri(new ArrayList<TLDefinitionUri>());
                                }
                                if (service.getSupplyPoint() != null) {
                                    tlService.setSupplyPoint(service.getSupplyPoint());
                                } else {
                                    tlService.setSupplyPoint(new ArrayList<TLSupplyPoint>());
                                }
                                if (service.getTSPDefinitionUri() != null) {
                                    tlService.setTSPDefinitionUri(service.getTSPDefinitionUri());
                                } else {
                                    tlService.setTSPDefinitionUri(new ArrayList<TLDefinitionUri>());
                                }
                                if (service.getExtension() != null) {
                                    tlService.setExtension(service.getExtension());
                                } else {
                                    tlService.setExtension(new ArrayList<TLServiceExtension>());
                                }
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
                                                        Element newOtherElement = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(((String) obj).getBytes(StandardCharsets.UTF_16))).getDocumentElement();
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

                                    tlService.setDigitalIdentification(digtalList);
                                } else {
                                    tlService.setDigitalIdentification(new ArrayList<TLDigitalIdentification>());
                                }

                                serviceUpdated = tlService;
                            }
                        }
                    }
                }
            }

            if (newService) {
                if (tl.getServiceProviders().get(parentIndex.get(0)).getTSPServices() == null) {
                    tl.getServiceProviders().get(parentIndex.get(0)).setTSPServices(new ArrayList<TLServiceDto>());
                }
                tl.getServiceProviders().get(parentIndex.get(0)).getTSPServices().add(service);
                serviceUpdated = service;
            }

            byte[] updatedTL = jaxbService.marshallToBytesAsV5(tl);
            if (ArrayUtils.isNotEmpty(updatedTL)) {
                DBFiles xmlFile = tldb.getXmlFile();
                xmlFile.setLocalPath(fileService.storeNewDraftTL(xmlFile.getMimeTypeFile(), updatedTL, tldb.getTerritory().getCodeTerritory(), xmlFile.getLocalPath()));
                // tldb.setXmlFile(xmlFile);
                tlRepository.save(tldb);
                tldb.setLastEditedDate(new Date());
                return serviceUpdated;
            }

        }
        return null;
    }

    public int delete(int id, TLServiceDto service) {
        DBTrustedLists tldb = tlRepository.findOne(id);
        int nbreRemove = 0;
        if (TLStatus.DRAFT.equals(tldb.getStatus())) {

            TL tl = tlService.getTL(id);

            for (TLServiceProvider p : tl.getServiceProviders()) {
                if (p.getTSPServices() != null) {
                    Iterator<TLServiceDto> tlService = p.getTSPServices().iterator();
                    while (tlService.hasNext()) {
                        TLServiceDto q = tlService.next();
                        if (q.getId().equalsIgnoreCase(service.getId())) {
                            tlService.remove();
                            nbreRemove++;
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

    /**
     * Update service status and create new history entry
     * 
     * @param tlId
     * @param tspId
     * @param serviceId
     * @return TL updated
     */
    public TL newStatus(int tlId, String tspId, String serviceId) {
        DBTrustedLists dbTL = tlRepository.findOne(tlId);
        if (TLStatus.DRAFT.equals(dbTL.getStatus())) {
            // CHANGES IN XML FILE
            TL tl = tlService.getDtoTL(dbTL);
            for (TLServiceProvider tsp : tl.getServiceProviders()) {
                if (tsp.getId().equals(tspId)) {
                    for (TLServiceDto service : tsp.getTSPServices()) {
                        if (service.getId().equals(serviceId)) {
                            TLServiceHistory newHistory = new TLServiceHistory();
                            newHistory.setCurrentStatus(service.getCurrentStatus());
                            newHistory.setCurrentStatusStartingDate(service.getCurrentStatusStartingDate());
                            newHistory.setDigitalIdentification(service.getDigitalIdentification());
                            newHistory.setExtension(service.getExtension());
                            newHistory.setServiceName(service.getServiceName());
                            newHistory.setTypeIdentifier(service.getTypeIdentifier());
                            if (CollectionUtils.isEmpty(service.getHistory())) {
                                service.setHistory(new ArrayList<TLServiceHistory>());
                            }
                            service.getHistory().add(0, newHistory);
                            service.setCurrentStatusStartingDate(new Date());
                            break;
                        }
                    }
                }
            }

            byte[] updatedTL = jaxbService.marshallToBytesAsV5(tl);
            if (ArrayUtils.isNotEmpty(updatedTL)) {
                DBFiles xmlFile = dbTL.getXmlFile();
                xmlFile.setLocalPath(fileService.storeNewDraftTL(xmlFile.getMimeTypeFile(), updatedTL, dbTL.getTerritory().getCodeTerritory(), xmlFile.getLocalPath()));
                tlRepository.save(dbTL);
                dbTL.setLastEditedDate(new Date());
                return tlService.getDtoTL(dbTL);
            }

        }
        return null;
    }
}
