package eu.europa.ec.joinup.tsl.business.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.repository.ApplicationPropertyRepository;
import eu.europa.ec.joinup.tsl.model.DBApplicationProperty;

/**
 * Get properties stored in database and return if active
 */
@Service
public class ApplicationPropertyService {

    private static final String MAIL_SEND_NEW_TL = "MAIL_SEND_NEW_TL";
    private static final String MAIL_SEND_NOTIFICATION = "MAIL_SEND_NOTIFICATION ";
    private static final String MAIL_ALERT = "MAIL_ALERT";
    private static final String MAIL_USE_TL_CONTACT = "MAIL_USE_TL_CONTACT";
    private static final String MAIL_SEND_NOTIFICATION_PUBLISHED = "MAIL_SEND_NOTIFICATION_PUBLISHED";
    private static final String MAIL_SEND_NEW_SIGNING_CERTIFICATE = "MAIL_SEND_NEW_SIGNING_CERTIFICATE";
    private static final String MAIL_SIGNATURE_ALERT = "MAIL_SIGNATURE_ALERT";
    private static final String MAIL_UNAVAILABILITY_ALERT = "MAIL_UNAVAILABILITY_ALERT";
    private static final String MAIL_TL_STATUS_BREAK_ALERT = "MAIL_TL_STATUS_BREAK_ALERT";
    private static final String MAIL_DAILY_UNAVAILABILITY_REPORT_MAIL = "MAIL_DAILY_UNAVAILABILITY_REPORT_MAIL";
    private static final String MAIL_NEW_USER_MAIL = "MAIL_NEW_USER_MAIL";
    private static final String MAIL_ACCESS_LIST_MAIL = "MAIL_ACCESS_LIST_MAIL";
    private static final String RUN_RETENTION_POLICY = "RUN_RETENTION_POLICY";
    private static final String CHECK_LOTL = "CHECK_LOTL";

    private static final String APPROACH_BREAK_CRON = "APPROACH_BREAK_CRON";
    private static final String LOTL_URL = "LOTL_URL";

    private static final List<String> booleanProperties = new ArrayList<>(Arrays.asList(APPROACH_BREAK_CRON, LOTL_URL));

    @Autowired
    private ApplicationPropertyRepository propertyRepository;

    public List<DBApplicationProperty> getbooleanProperties() {
        return propertyRepository.findByTypeNotIn(booleanProperties);
    }

    @Transactional
    public DBApplicationProperty switchProperty(DBApplicationProperty dbProperty) {
        try {
            dbProperty.setActive(!dbProperty.isActive());
            return propertyRepository.save(dbProperty);
        } catch (Exception e) {
            return null;
        }
    }

    /*-------- LOTL Url --------*/

    public String getLOTLUrl() {
        DBApplicationProperty property = propertyRepository.findByType(LOTL_URL);
        if (property == null) {
            return "";
        }
        return property.getDescription();
    }

    @Transactional
    public void updateLOTLUrl(String newLocation) {
        DBApplicationProperty property = propertyRepository.findByType(LOTL_URL);
        property.setDescription(newLocation);
    }

    /*-------- Get properties --------*/

    public boolean sendNewTLByCountry(String codeTerritory) {
        return getPropertyValue(MAIL_SEND_NEW_TL + "_" + codeTerritory);
    }

    public String getApproachOfBreakCron() {
        DBApplicationProperty property = propertyRepository.findByType(APPROACH_BREAK_CRON);
        if (property == null) {
            return null;
        }
        return property.getDescription();
    }

    public boolean isCheckLOTLEnabled() {
        return getPropertyValue(CHECK_LOTL);
    }

    public boolean sendNewTL() {
        return getPropertyValue(MAIL_SEND_NEW_TL);
    }

    public boolean sendNewNotification() {
        return getPropertyValue(MAIL_SEND_NOTIFICATION);
    }

    public boolean sendNotificationPublished() {
        return getPropertyValue(MAIL_SEND_NOTIFICATION_PUBLISHED);
    }

    public boolean mailAlert() {
        return getPropertyValue(MAIL_ALERT);
    }

    public boolean useTlContact() {
        return getPropertyValue(MAIL_USE_TL_CONTACT);
    }

    public boolean sendNewSigningCertificate() {
        return getPropertyValue(MAIL_SEND_NEW_SIGNING_CERTIFICATE);
    }

    public boolean runRetentionPolicy() {
        return getPropertyValue(RUN_RETENTION_POLICY);
    }

    public boolean sendSignatureAlert() {
        return getPropertyValue(MAIL_SIGNATURE_ALERT);
    }

    public boolean sendUnavailabilityAlert() {
        return getPropertyValue(MAIL_UNAVAILABILITY_ALERT);
    }

    public boolean sendTLStatusBreakAlert() {
        return getPropertyValue(MAIL_TL_STATUS_BREAK_ALERT);
    }

    public boolean sendDailyUnavailabilityAlert() {
        return getPropertyValue(MAIL_DAILY_UNAVAILABILITY_REPORT_MAIL);
    }

    public boolean sendNewUserAlert() {
        return getPropertyValue(MAIL_NEW_USER_MAIL);
    }

    public boolean sendAccessListAlert() {
        return getPropertyValue(MAIL_ACCESS_LIST_MAIL);
    }

    private boolean getPropertyValue(String type) {
        DBApplicationProperty property = propertyRepository.findByType(type);
        if (property == null) {
            return false;
        }
        return property.isActive();
    }

}
