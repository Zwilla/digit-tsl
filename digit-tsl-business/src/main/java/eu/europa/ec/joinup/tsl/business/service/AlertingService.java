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
    public boolean sendNewTLReport(int tlId) {
        return false;
    }

    @Override
    public boolean sendNotificationPublished(DBTrustedLists lotl, DBCountries country, Set<DBNotification> notifications) {
        return false;
    }

    @Override
    public boolean sendNewSigningCertificate() {
        return false;
    }

    @Override
    public boolean sendRetentionJobReport(RetentionAlertDTO retentionAlert) {
        return false;
    }

    @Override
    public boolean sendSignatureAlert(DBTrustedLists dbTL, TLSignature tmpSignature) {
        return false;
    }

    @Override
    public boolean sendAvailabilityAlert(DBTrustedLists dbTL) {
        return false;
    }

    @Override
    public boolean sendBreakAlert(TLBreakStatus breakStatus) {
        return false;
    }

    @Override
    public boolean sendDailyUnavailabilityAlert(Date today, ByteArrayOutputStream unavailabilityReportOS) {
        return false;
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
    public boolean sendTLCacheIssue(String country, String currentDigest, String otherDigest, int index) {
        return false;
    }

}
