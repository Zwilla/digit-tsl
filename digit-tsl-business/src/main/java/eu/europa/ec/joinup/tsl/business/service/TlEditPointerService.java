package eu.europa.ec.joinup.tsl.business.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLDigitalIdentification;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLName;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLSchemeTypeCommunityRule;
import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;

/**
 * TL pointer to other TSL edition/deletion management
 */
@Service
@Transactional(value = TxType.REQUIRED)
public class TlEditPointerService {

    @Autowired
    private TLRepository tlRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private TLService tlService;

    @Autowired
    private TrustedListJaxbService jaxbService;

    public TLPointersToOtherTSL edit(int id, TLPointersToOtherTSL pointer, String tag) {
        DBTrustedLists tldb = tlRepository.findOne(id);
        if (TLStatus.DRAFT.equals(tldb.getStatus())) {
            boolean newPointer = true;
            // CHANGE IN XML FILE
            TL tl = tlService.getTL(id);
            TLPointersToOtherTSL pointerUpdated = null;
            for (TLPointersToOtherTSL tlPointer : tl.getPointers()) {
                if (tlPointer.getId().equalsIgnoreCase(pointer.getId())) {
                    newPointer = false;
                    if (pointer.getMimeType() != null) {
                        tlPointer.setMimeType(pointer.getMimeType());
                    }

                    if (pointer.getSchemeOpeName() != null) {
                        tlPointer.setSchemeOpeName(pointer.getSchemeOpeName());
                    } else {
                        tlPointer.setSchemeOpeName(new ArrayList<TLName>());
                    }

                    if (pointer.getSchemeTerritory() != null) {
                        tlPointer.setSchemeTerritory(pointer.getSchemeTerritory());
                    } else {
                        tlPointer.setSchemeTerritory("");
                    }

                    if (pointer.getSchemeTypeCommunity() != null) {
                        tlPointer.setSchemeTypeCommunity(pointer.getSchemeTypeCommunity());
                    } else {
                        tlPointer.setSchemeTypeCommunity(new ArrayList<TLSchemeTypeCommunityRule>());
                    }

                    if (pointer.getServiceDigitalId() != null) {
                        tlPointer.setServiceDigitalId(pointer.getServiceDigitalId());
                    } else {
                        tlPointer.setServiceDigitalId(new ArrayList<TLDigitalIdentification>());
                    }

                    if (pointer.getTlLocation() != null) {
                        tlPointer.setTlLocation(pointer.getTlLocation());
                    } else {
                        tlPointer.setTlLocation("");
                    }

                    if (pointer.getTlType() != null) {
                        tlPointer.setTlType(pointer.getTlType());
                    } else {
                        tlPointer.setTlType("");
                    }

                    pointerUpdated = tlPointer;
                }
            }

            if (newPointer) {
                tl.getPointers().add(pointer);
                pointerUpdated = pointer;
            }

            byte[] updatedTL = jaxbService.marshallToBytesAsV5(tl);
            if (ArrayUtils.isNotEmpty(updatedTL)) {
                DBFiles xmlFile = tldb.getXmlFile();
                xmlFile.setLocalPath(fileService.storeNewDraftTL(xmlFile.getMimeTypeFile(), updatedTL, tldb.getTerritory().getCodeTerritory(), xmlFile.getLocalPath()));
                // tldb.setXmlFile(xmlFile);
                tldb.setLastEditedDate(new Date());
                tlRepository.save(tldb);
                return pointerUpdated;
            }

        }
        return null;
    }

    public int delete(int id, TLPointersToOtherTSL pointer, String tag) {
        DBTrustedLists tldb = tlRepository.findOne(id);
        int nbreRemove = 0;
        if (TLStatus.DRAFT.equals(tldb.getStatus())) {

            TL tl = tlService.getTL(id);

            Iterator<TLPointersToOtherTSL> it = tl.getPointers().iterator();
            while (it.hasNext()) {
                TLPointersToOtherTSL p = it.next();
                if (p.getId().equalsIgnoreCase(pointer.getId())) {
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
