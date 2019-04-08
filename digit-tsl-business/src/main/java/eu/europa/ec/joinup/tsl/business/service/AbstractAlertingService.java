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

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Set;

import eu.europa.ec.joinup.tsl.business.dto.TLSignature;
import eu.europa.ec.joinup.tsl.business.dto.data.retention.RetentionAlertDTO;
import eu.europa.ec.joinup.tsl.business.dto.tl.approachbreak.TLBreakStatus;
import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.DBNotification;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.NotificationStatus;
import eu.europa.esig.dss.DSSDocument;

public abstract class AbstractAlertingService {

    public abstract boolean sendNewUserAlert(List<String> ecasIDs);

    public abstract boolean sendAccessListAlert();

    /**
     * Send new notification email to TLSO of the concerned country (+support)
     *
     * @param status
     * @param dbNotif
     * @param pdfDoc
     * @param contactListDeleted
     */
    public abstract boolean sendNotificationToTLSO(NotificationStatus status, DBNotification dbNotif, DSSDocument pdfDoc, List<String> contactListDeleted);

    /**
     * Send new notification email to LOTL SO (+support)
     *
     * @param status
     * @param dbNotif
     * @param pdfDoc
     */
    public abstract boolean sendNotificationToLOTLSO(NotificationStatus status, DBNotification dbNotif, DSSDocument pdfDoc);

    /**
     * Send new trusted list report to contact of concerned country (+support)
     *
     * @param tlId
     */
    public abstract boolean sendNewTLReport(int tlId);

    /**
     * Send notification integration confirmation to concerned country (+support)
     *
     * @param lotl
     * @param country
     * @param notifications
     */
    public abstract boolean sendNotificationPublished(DBTrustedLists lotl, DBCountries country, Set<DBNotification> notifications);

    /**
     * Send email when keystore signing certificates change
     * 
     * @return
     */
    public abstract boolean sendNewSigningCertificate();

    /**
     * Send email to EC-Team when retention policy clean is triggered
     * 
     * @param retentionAlert
     * @return
     */
    public abstract boolean sendRetentionJobReport(RetentionAlertDTO retentionAlert);

    /**
     * Signature Alert when signature status is different from SignatureStatus.VALID
     *
     * @param dbTL
     * @param tmpSignature
     * @return
     */
    public abstract boolean sendSignatureAlert(DBTrustedLists dbTL, TLSignature tmpSignature);

    /**
     * Unavailability alert when trusted list if unavailable/unsupported since more than 2 loading
     *
     * @param dbTL
     * @return
     */
    public abstract boolean sendAvailabilityAlert(DBTrustedLists dbTL);

    /**
     * TL approach of break alert when trusted list next update date/signing certificate come close or are expired
     *
     * @param dbTL
     * @param breakStatus
     */
    public abstract boolean sendBreakAlert(TLBreakStatus breakStatus);

    /**
     * Send report of all the unavailability/unsupported session of production trusted list of the day
     *
     * @param today
     * @param unavailabilityReportOS
     */
    public abstract boolean sendDailyUnavailabilityAlert(Date today, ByteArrayOutputStream unavailabilityReportOS);

    /**
     * Alert EC-TL-Service team of new TL cache issue
     * 
     * @param country
     * @return
     */
    public abstract boolean sendTLCacheIssue(String country, String currentDigest, String otherDigest, int index);

}
