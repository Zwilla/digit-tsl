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
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.TLDifference;
import eu.europa.ec.joinup.tsl.business.dto.TLSignature;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.util.LocationUtils;
import eu.europa.ec.joinup.tsl.model.enums.SignatureStatus;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

/**
 * Calcul TLDifferences of different trusted list signature
 */
@Service
public class SignatureChangeService {

    @Autowired
    private TLService tlService;

    /**
     * Get signature changes between current trusted list & compared one
     *
     * @param currentTl
     * @param comparedTl
     * @return
     */
    public List<TLDifference> getSignatureChanges(TL currentTl, TL comparedTl) {
        List<TLDifference> differences = new ArrayList<>();

        TLSignature signature = tlService.getSignatureInfo(currentTl.getTlId());
        TLSignature comparedSignature = tlService.getSignatureInfo(comparedTl.getTlId());

        differences.addAll(getChanges(currentTl.getId(), signature, comparedSignature));
        for (TLDifference diff : differences) {
            diff.setHrLocation(LocationUtils.idUserReadable(currentTl, diff.getId()));
        }
        return differences;
    }

    public List<TLDifference> getChanges(String id, TLSignature signature, TLSignature comparedSignature) {
        List<TLDifference> differences = new ArrayList<>();

        if ((signature == null) || signature.getIndication().equals(SignatureStatus.NOT_SIGNED)) {
            if ((comparedSignature != null) && !comparedSignature.getIndication().equals(SignatureStatus.NOT_SIGNED)) {
                differences.add(new TLDifference(id + "_" + Tag.SIGNATURE, comparedSignature.getSignedBy() + " - " + comparedSignature.getSigningDate().toString(), ""));
            }
        } else {
            differences.addAll(signature.asPublishedDiff(comparedSignature, id + "_" + Tag.SIGNATURE));
        }
        return differences;
    }

}
