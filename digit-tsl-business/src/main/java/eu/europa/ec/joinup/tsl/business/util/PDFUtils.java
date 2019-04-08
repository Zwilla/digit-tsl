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

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import eu.europa.ec.joinup.tsl.business.dto.NotificationPointers;
import eu.europa.ec.joinup.tsl.business.dto.TLDifference;
import eu.europa.ec.joinup.tsl.business.dto.contact.ContactList.TLSOContact;
import eu.europa.ec.joinup.tsl.business.dto.notification.PDFMeasure;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

/**
 * PDF generation toolbox
 */
public class PDFUtils {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("messages");

    private static final Logger LOGGER = LoggerFactory.getLogger(PDFUtils.class);

    /**
     * Init PDF location change measure
     *
     * @param schemeTerritory
     * @param location
     * @param isPDF
     * @return
     */
    public static PDFMeasure PDFLocationAdded(String schemeTerritory, String location, boolean isPDF) {
        PDFMeasure change = new PDFMeasure();
        change.addChange(removeHtmlTag(bundle.getString("notificationController.urlPdfChanged").replace("%LOC%", location), isPDF));
        String measure = removeHtmlTag(bundle.getString("notificationController.urlPdfNewMeasure"), isPDF).replace("%ST%", schemeTerritory).replace("%LOC%", location);
        change.addMeasure(measure);
        return change;
    }

    /**
     * Convert TLDifferences object to PDFMeasures
     *
     * @param differences
     * @param pointers
     * @param isPDF
     * @return
     */
    public static List<PDFMeasure> convertDifferences(List<TLDifference> differences, NotificationPointers pointers, boolean isPDF) {
        List<PDFMeasure> notificationChanges = new ArrayList<>();
        Boolean schemeOperatorChange = false;
        for (int i = 0; i < differences.size(); i++) {
            TLDifference difference = differences.get(i);

            PDFMeasure change = new PDFMeasure();
            if (difference.getId() != null) {
                if ((pointers.getHrPointer() != null) && difference.getId().equals(pointers.getHrPointer().getId() + '_' + Tag.POINTER_LOCATION)) {
                    if (pointers.getHrPointer().getTlLocation() == "") {
                        change.addChange(bundle.getString("notificationController.urlPdfRemoved"));
                        String measure = removeHtmlTag(bundle.getString("notificationController.urlPdfRemovedMeasure"), isPDF).replace("%ST%", pointers.getHrPointer().getSchemeTerritory())
                                .replace("%PBL%", difference.getPublishedValue());
                        change.addMeasure(measure);
                        notificationChanges.add(change);
                    } else {
                        change.addChange(removeHtmlTag(bundle.getString("notificationController.urlPdfChanged").replace("%LOC%", pointers.getHrPointer().getTlLocation()), isPDF));
                        String measure = removeHtmlTag(bundle.getString("notificationController.urlPdfChangedMeasure"), isPDF).replace("%ST%", pointers.getHrPointer().getSchemeTerritory())
                                .replace("%PBL%", difference.getPublishedValue()).replace("%LOC%", pointers.getHrPointer().getTlLocation());
                        change.addMeasure(measure);
                        notificationChanges.add(change);
                    }
                } else if ((pointers.getMpPointer() != null) && difference.getId().equals(pointers.getMpPointer().getId() + '_' + Tag.POINTER_LOCATION)) {
                    if (pointers.getMpPointer().getTlLocation() == "") {
                        change.addChange(bundle.getString("notificationController.urlXmlRemoved"));
                        String measure = removeHtmlTag(bundle.getString("notificationController.urlXmlRemovedMeasure"), isPDF).replaceAll("%ST%", pointers.getMpPointer().getSchemeTerritory());
                        change.addMeasure(measure);
                        notificationChanges.add(change);
                    } else {
                        change.addChange(removeHtmlTag(bundle.getString("notificationController.urlXmlChanged").replace("%LOC%", pointers.getMpPointer().getTlLocation()), isPDF));
                        String measure = removeHtmlTag(bundle.getString("notificationController.urlXmlChangedMeasure"), isPDF).replace("%ST%", pointers.getMpPointer().getSchemeTerritory())
                                .replace("%PBL%", difference.getPublishedValue()).replace("%LOC%", pointers.getMpPointer().getTlLocation());
                        change.addMeasure(measure);
                        notificationChanges.add(change);
                    }
                } else if ((pointers.getMpPointer() != null) && difference.getId().equals(Tag.NOTIFICATION.toString() + '_' + Tag.POINTER_LOCATION)) {
                    LOGGER.warn("Notficiation : Undefined changes");
                } else if (difference.getId().contains(Tag.SCHEME_OPERATOR_NAME.toString()) && !schemeOperatorChange) {
                    change.addChange(bundle.getString("notificationController.changeOperatorName"));
                    notificationChanges.add(change);
                    schemeOperatorChange = true;
                }
            }
        }
        PDFMeasure certificatesChanges = detectCertificatesChanges(pointers, isPDF);
        if (certificatesChanges != null) {
            notificationChanges.add(certificatesChanges);
        }
        return notificationChanges;
    }

    /**
     * Get original contact VS notification contact changes
     *
     * @param pointers
     * @param originalPointers
     * @param contactDiff
     * @param isPDF
     * @return
     */
    public static PDFMeasure detectSchemeContactChange(NotificationPointers pointers, NotificationPointers originalPointers, List<TLDifference> contactDiff, boolean isPDF) {
        PDFMeasure change = new PDFMeasure();
        if ((pointers.getTlsoContact() != null) && (originalPointers.getTlsoContact() != null)) {
            TLSOContact pContact = pointers.getTlsoContact();
            TLSOContact oContact = originalPointers.getTlsoContact();

            if ((pContact.getName() != null) && !pContact.getName().equals(oContact.getName())) {
                if (pContact.getName() == "") {
                    change.addChange(bundle.getString("notificationController.nameRemoved"));
                } else {
                    change.addChange(removeHtmlTag(bundle.getString("notificationController.nameChangedTo").replace("%NAME%", pContact.getName()), isPDF));
                }
            }
            if ((pContact.getPostalAddress() != null) && !pContact.getPostalAddress().equals(oContact.getPostalAddress())) {
                if (pContact.getPostalAddress() == "") {
                    change.addChange(bundle.getString("notificationController.addressRemoved"));
                } else {
                    change.addChange(removeHtmlTag(bundle.getString("notificationController.addressChangedTo").replace("%ADDRESS%", pContact.getPostalAddress()), isPDF));
                }
            }
            if ((pContact.getPhoneNumber() != null) && !pContact.getPhoneNumber().equals(oContact.getPhoneNumber())) {
                if (pContact.getPhoneNumber() == "") {
                    change.addChange(bundle.getString("notificationController.phoneRemoved"));
                } else {
                    change.addChange(removeHtmlTag(bundle.getString("notificationController.phoneChangedTo").replace("%PHONE%", pContact.getPhoneNumber()), isPDF));
                }
            }

            List<String> contactRemoved = new ArrayList<>();
            List<String> contactAdded = new ArrayList<>();
            for (TLDifference diff : contactDiff) {
                if (diff.getId().contains((Tag.ELECTRONIC_ADDRESS.toString()))) {
                    if (diff.getCurrentValue().equals("")) {
                        contactRemoved.add(diff.getPublishedValue());
                    } else if (diff.getPublishedValue().equals("") && (diff.getCurrentValue() != "")) {
                        contactAdded.add(diff.getCurrentValue());
                    }
                }
            }

            if (CollectionUtils.isNotEmpty(contactRemoved)) {
                change.addChange(removeHtmlTag(bundle.getString("notificationController.removed"), isPDF));
                change.getChange().addAll(contactRemoved);
            }
            if (CollectionUtils.isNotEmpty(contactAdded)) {
                change.addChange(removeHtmlTag(bundle.getString("notificationController.added"), isPDF));
                change.getChange().addAll(contactAdded);
            }

        }

        if ((change.getChange() != null) && !change.getChange().isEmpty()) {
            change.getChange().add(0, removeHtmlTag(bundle.getString("notificationController.changeContact"), isPDF));
        }
        return change;
    }

    /**
     * Get original db_users VS notification users changes
     *
     * @param userDifferences
     * @param isPDF
     * @return
     */
    public static PDFMeasure detectUserChanges(List<TLDifference> userDifferences, boolean isPDF) {
        PDFMeasure notificationChange = new PDFMeasure();
        notificationChange.getChange().add(removeHtmlTag(bundle.getString("notificationController.changeUser"), isPDF));
        List<String> userRemoved = new ArrayList<>();
        List<String> userAdded = new ArrayList<>();
        for (TLDifference userDifference : userDifferences) {
            if (StringUtils.isEmpty(userDifference.getCurrentValue())) {
                userRemoved.add(userDifference.getPublishedValue());
            } else {
                userAdded.add(userDifference.getCurrentValue());
            }
        }

        if (!CollectionUtils.isEmpty(userRemoved)) {
            notificationChange.addChange(removeHtmlTag(bundle.getString("notificationController.removed"), isPDF));
            notificationChange.getChange().addAll(userRemoved);
        }
        if (!CollectionUtils.isEmpty(userAdded)) {
            notificationChange.addChange(removeHtmlTag(bundle.getString("notificationController.added"), isPDF));

            notificationChange.getChange().addAll(userAdded);
        }

        return notificationChange;
    }

    /**
     * Get original country certificate VS notification certificates changes
     *
     * @param pointers
     * @param isPDF
     * @return
     */
    private static PDFMeasure detectCertificatesChanges(NotificationPointers pointers, boolean isPDF) {
        PDFMeasure result = new PDFMeasure();
        if (!pointers.getAddedCertificates().isEmpty() || !pointers.getDeletedCertificates().isEmpty()) {
            // Deleted Cert
            if (!pointers.getDeletedCertificates().isEmpty()) {
                result.addChange(removeHtmlTag(bundle.getString("notificationController.removed"), isPDF));
                for (String id : pointers.getDeletedCertificates()) {
                    result.addChange(formatCertificateId(id, isPDF));
                }
            }
            // Added Cert
            if (!pointers.getAddedCertificates().isEmpty()) {
                result.addChange(removeHtmlTag(bundle.getString("notificationController.added"), isPDF));
                for (String id : pointers.getAddedCertificates()) {
                    result.addChange(formatCertificateId(id, isPDF));
                }
            }
            // Good Cert
            if (!pointers.getCurrentCertificates().isEmpty()) {
                String measure = bundle.getString("notificationController.followingCertificates").replace("%ST%", pointers.getMpPointer().getSchemeTerritory());
                result.addMeasure(measure);
                for (String id : pointers.getCurrentCertificates()) {
                    result.addMeasure(formatCertificateId(id, isPDF));
                    // report.addSigningCertificate(id);
                }
            } else if (pointers.getCurrentCertificates().isEmpty() && !pointers.getAddedCertificates().isEmpty()) {
                String measure = bundle.getString("notificationController.allCertRemoved").replaceAll("%ST%", pointers.getMpPointer().getSchemeTerritory());
                result.addMeasure(measure);
            } else {
                String measure = bundle.getString("notificationController.allCertRemovedNoNew").replaceAll("%ST%", pointers.getMpPointer().getSchemeTerritory());
                result.addMeasure(measure);
            }

        } else {
            return null;
        }
        return result;
    }

    private static String formatCertificateId(String id, boolean isPDF) {
        String result = "";
        String[] splitStr = id.split("\\(");
        result = "<label>" + splitStr[0] + "</label> " + id.substring(splitStr[0].length());
        return removeHtmlTag(result, isPDF);
    }

    /**
     * Remove HTML used to measures format
     *
     * @param str
     * @param isPDF
     * @return
     */
    private static String removeHtmlTag(String str, boolean isPDF) {
        if (isPDF) {
            return str.replaceAll("<u>", "").replaceAll("</u>", "").replaceAll("<label>", "").replaceAll("</label>", "").replaceAll("<span>", "").replaceAll("</span>", "");
        } else {
            return str;
        }
    }
}
