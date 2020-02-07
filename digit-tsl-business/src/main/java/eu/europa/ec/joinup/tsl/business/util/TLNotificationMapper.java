package eu.europa.ec.joinup.tsl.business.util;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import eu.europa.ec.joinup.tsl.business.dto.NotificationPointers;
import eu.europa.ec.joinup.tsl.business.dto.TLDifference;
import eu.europa.ec.joinup.tsl.business.dto.notification.MemberStateNotificationV5;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLCertificate;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLDigitalIdentification;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.ec.joinup.tsl.model.enums.MimeType;
import eu.europa.ec.joinup.tsl.model.enums.Tag;
import eu.europa.esig.jaxb.v5.tsl.OtherTSLPointerTypeV5;

/**
 * Map JaxB XML Notification <-> DTO Notification
 */
public class TLNotificationMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(TLNotificationMapper.class);

    private static final String toolIdentifier = "ecTools";
    private static final int versionIdentifier = 5;

    private static final ResourceBundle bundle = ResourceBundle.getBundle("messages");

    /**
     * Initialize MemberStateNotificationV5 XML object
     *
     * @param notification
     * @param notificationIdentifier
     * @param deletedContact
     * @return
     */
    public static MemberStateNotificationV5 mapDTONotificationToXMLNotificationObj(NotificationPointers notification, String notificationIdentifier, List<String> deletedContact) {
        MemberStateNotificationV5 msNotification = new MemberStateNotificationV5();

        msNotification.setToolIdentifier(toolIdentifier);
        msNotification.setVersionIdentifier(versionIdentifier);
        if (notificationIdentifier != null) {
            msNotification.setNotificationIdentifier(notificationIdentifier);
        }

        // Date (Submission & Effect)
        XMLGregorianCalendar gEffectDate = null;
        XMLGregorianCalendar gSubmissionDate = null;
        try {
            GregorianCalendar gCalendar = new GregorianCalendar();
            if (notification.getDateOfEffect() != null) {
                gCalendar.setTime(notification.getDateOfEffect());
                gEffectDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
            }
            if (notification.getDateOfSubmission() != null) {
                gCalendar.setTime(notification.getDateOfSubmission());
                gSubmissionDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
            }
        } catch (DatatypeConfigurationException e) {
            LOGGER.error("Error mapMemberStateNotification" + e);
        }
        msNotification.setEffectDate(gEffectDate);
        msNotification.setSubmissionDate(gSubmissionDate);

        List<OtherTSLPointerTypeV5> pointerListV5 = new ArrayList<>();
        // Human redeable
        if (notification.getHrPointer() != null) {
            pointerListV5.add(notification.getHrPointer().asTSLTypeV5());
        }
        // Machine processable
        if (notification.getMpPointer() != null) {
            pointerListV5.add(notification.getMpPointer().asTSLTypeV5());
        }
        msNotification.setPointersToOtherTSL(pointerListV5);

        // Scheme Contact
        msNotification.setSchemeContact(notification.getTlsoContact());
        if (deletedContact == null) {
            msNotification.setContactDeleted(new ArrayList<String>());
        } else {
            msNotification.setContactDeleted(deletedContact);
        }

        // Users
        msNotification.setUsers(notification.getUsers());
        return msNotification;
    }

    /**
     * Initialize NotificationPointers
     *
     * @param notificationXMLObj
     * @param prodPointer
     * @return
     */
    public static NotificationPointers mapXMLNotificationObjToDTONotification(MemberStateNotificationV5 notificationXMLObj, NotificationPointers prodPointer) {
        NotificationPointers notif = new NotificationPointers();
        if (notificationXMLObj != null) {
            if (notificationXMLObj.getEffectDate() != null) {
                notif.setDateOfEffect(TLUtils.toDate(notificationXMLObj.getEffectDate()));
            }
            if (notificationXMLObj.getSchemeContact() != null) {
                notif.setTlsoContact(notificationXMLObj.getSchemeContact());
            }
            if (notificationXMLObj.getSubmissionDate() != null) {
                notif.setDateOfSubmission(TLUtils.toDate(notificationXMLObj.getSubmissionDate()));
            }
            notif.setAddedCertificates(new ArrayList<String>());
            notif.setDeletedCertificates(new ArrayList<String>());
            int i = 1;
            TLPointersToOtherTSL pointer = null;
            for (OtherTSLPointerTypeV5 otherPointer : notificationXMLObj.getPointersToOtherTSL()) {
                pointer = new TLPointersToOtherTSL(0, "1_" + Tag.POINTERS_TO_OTHER_TSL + "_" + i, otherPointer);
                if ((pointer.getMimeType() != null) && pointer.getMimeType().equals(MimeType.XML)) {
                    notif.setMpPointer(pointer);
                } else {
                    notif.setHrPointer(pointer);
                }
                i = i + 1;
            }
            if ((pointer != null) && (prodPointer.getMpPointer() != null)) {
                Set<String> currentCerts = new HashSet<>();
                Set<String> deletedCerts = new HashSet<>();
                Set<String> addedCerts = new HashSet<>();
                for (TLDigitalIdentification digitalId : pointer.getServiceDigitalId()) {
                    for (TLCertificate tlCert : digitalId.getCertificateList()) {
                        currentCerts.add(tlCert.certToString());
                    }
                }

                List<TLDifference> certDiffs = ChangeUtils.diffOfDigitalList(pointer.getServiceDigitalId(), prodPointer.getMpPointer().getServiceDigitalId(), pointer.getId());
                for (TLDifference diff : certDiffs) {
                    if (StringUtils.isEmpty(diff.getCurrentValue())) {
                        deletedCerts.add(diff.getPublishedValue());
                        currentCerts.remove(diff.getPublishedValue());
                    } else if (StringUtils.isEmpty(diff.getPublishedValue())) {
                        addedCerts.add(diff.getCurrentValue());
                        currentCerts.remove(diff.getCurrentValue());
                    }
                }

                i = 1;
                for (String certStr : currentCerts) {
                    notif.getCurrentCertificates().add(formatCertMeasure(certStr, i));
                    i = i + 1;
                }
                for (String certStr : deletedCerts) {
                    notif.getDeletedCertificates().add(formatCertMeasure(certStr, i));
                    i = i + 1;
                }
                for (String certStr : addedCerts) {
                    notif.getAddedCertificates().add(formatCertMeasure(certStr, i));
                    i = i + 1;
                }
            }

            notif.setUsers(notificationXMLObj.getUsers());

        }
        return notif;
    }

    private static String formatCertMeasure(String certStr, int i) {
        return bundle.getString("tCertificate") + " " + i + " (" + certStr + ")";
    }

}
