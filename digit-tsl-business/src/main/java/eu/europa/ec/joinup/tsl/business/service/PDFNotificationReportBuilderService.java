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

import eu.europa.ec.joinup.tsl.business.dto.CheckDTO;
import eu.europa.ec.joinup.tsl.business.dto.NotificationPointers;
import eu.europa.ec.joinup.tsl.business.dto.notification.PDFMeasure;
import eu.europa.ec.joinup.tsl.business.dto.pdf.PDFNotificationReport;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLName;

/**
 * Init PDFNotificationReport that contains all the needed information to be serialize in XML to generate a PDF with FOP
 */
@Service
public class PDFNotificationReportBuilderService {

    @Autowired
    private NotificationService notificationService;

    public PDFNotificationReportBuilderService() {
    }

    public PDFNotificationReport buildNotificationReport(NotificationPointers pointers) {
        PDFNotificationReport result = new PDFNotificationReport(pointers.getMpPointer().getSchemeTerritory(), pointers.getCreationDate());

        List<String> contact = new ArrayList<>();
        String contactName = "";
        String contactPhoneNumber = "";
        String contactPostalAddress = "";
        if (pointers.getTlsoContact() != null) {
            if ((pointers.getTlsoContact().getElectronicAddress() != null) && (pointers.getTlsoContact().getElectronicAddress().getURI() != null)) {
                contact = pointers.getTlsoContact().getElectronicAddress().getURI();
            }
            if (pointers.getTlsoContact().getName() != null) {
                contactName = pointers.getTlsoContact().getName();
            }
            if (pointers.getTlsoContact().getPhoneNumber() != null) {
                contactPhoneNumber = pointers.getTlsoContact().getPhoneNumber();
            }
            if (pointers.getTlsoContact().getPostalAddress() != null) {
                contactPostalAddress = pointers.getTlsoContact().getPostalAddress();
            }
        }
        result.setContactMail(contact);
        result.setContactName(contactName);
        result.setContactPhone(contactPhoneNumber);
        result.setContactAddress(contactPostalAddress);

        result.setMemberState(pointers.getMpPointer().getSchemeTerritory());
        result.setMpPointer(pointers.getMpPointer().getTlLocation());
        if ((pointers.getHrPointer() != null) && (pointers.getHrPointer().getTlLocation() != null)) {
            result.setHrPointer(pointers.getHrPointer().getTlLocation());
        }

        if (pointers.getDateOfEffect() != null) {
            result.setDateOfEffect(pointers.getDateOfEffect());
        }
        if (pointers.getDateOfSubmission() != null) {
            result.setDateOfSubmission(pointers.getDateOfSubmission());
        }
        for (TLName name : pointers.getMpPointer().getSchemeOpeName()) {
            result.addSchemeOperatorName(name.getLanguage(), name.getValue());
        }

        for (String certificate : pointers.getCurrentCertificates()) {
            result.addSigningCertificate(certificate);
        }
        for (String certificate : pointers.getAddedCertificates()) {
            result.addSigningCertificate(certificate);
        }

        List<CheckDTO> checkList = notificationService.getNotificationCheck(pointers.getMpPointer());
        result.setChecks(checkList);

        List<PDFMeasure> notificationChanges = notificationService.getNotificationChanges(pointers, true);
        for (PDFMeasure change : notificationChanges) {
            result.addChange(change);
        }

        result.setUsers(pointers.getUsers());

        // Signature
        result.setSignatureInformation(pointers.getSignatureInformation());

        return result;
    }

}
