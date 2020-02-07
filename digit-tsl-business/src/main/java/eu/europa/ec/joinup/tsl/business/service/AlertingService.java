package eu.europa.ec.joinup.tsl.business.service;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import eu.europa.ec.joinup.tsl.business.dto.TLSignature;
import eu.europa.ec.joinup.tsl.business.dto.data.retention.RetentionAlertDTO;
import eu.europa.ec.joinup.tsl.business.dto.tl.approachbreak.TLBreakStatus;
import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.DBNotification;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.NotificationStatus;
import eu.europa.esig.dss.DSSDocument;

@Transactional
public class AlertingService extends AbstractAlertingService {

    @Override
    public boolean sendNotificationToTLSO(NotificationStatus status, DBNotification dbNotif, DSSDocument pdfDoc, List<String> contactListDeleted) {
        return false;
    }

    @Override
    public boolean sendNotificationToLOTLSO(NotificationStatus status, DBNotification dbNotif, DSSDocument pdfDoc) {
        return false;
    }

    @Override
    public void sendNewTLReport(int tlId) {
    }

    @Override
    public void sendNotificationPublished(DBTrustedLists lotl, DBCountries country, Set<DBNotification> notifications) {
    }

    @Override
    public void sendNewSigningCertificate() {
    }

    @Override
    public void sendRetentionJobReport(RetentionAlertDTO retentionAlert) {
    }

    @Override
    public void sendSignatureAlert(DBTrustedLists dbTL, TLSignature tmpSignature) {
    }

    @Override
    public void sendAvailabilityAlert(DBTrustedLists dbTL) {
    }

    @Override
    public void sendBreakAlert(TLBreakStatus breakStatus) {
    }

    @Override
    public void sendDailyUnavailabilityAlert(Date today, ByteArrayOutputStream unavailabilityReportOS) {
    }

    @Override
    public boolean sendAccessListAlert() {
        return false;
    }

    @Override
    public boolean sendNewUserAlert(List<String> ecasIDs) {
        return false;
    }

    @Override
    public void sendTLCacheIssue(String country, String currentDigest, String otherDigest, int index) {
    }

}
