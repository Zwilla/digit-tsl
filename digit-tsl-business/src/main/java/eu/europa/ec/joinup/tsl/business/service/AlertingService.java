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
import eu.europa.ec.joinup.tsl.business.dto.tl.approachbreak.TLBreakStatus;
import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.DBNotification;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.NotificationStatus;
import eu.europa.esig.dss.DSSDocument;

@Transactional
public class AlertingService extends AbstractAlertingService {

    @Override
    public void sendNotificationToTLSO(NotificationStatus status, DBNotification dbNotif, DSSDocument pdfDoc, List<String> contactListDeleted) {
    }

    @Override
    public void sendNotificationToLOTLSO(NotificationStatus status, DBNotification dbNotif, DSSDocument pdfDoc) {
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
    public void sendRetentionJobReport(int nbDraftStore, int nbTrustedList) {
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
    public String getContactListForContent(String cc) {
        return null;
    }

    @Override
    public List<String> getNotificationContact(DBNotification dbNotif, List<String> contactDeleted) {
        return null;
    }

    @Override
    public void sendAccessListAlert() {
    }

    @Override
    public void sendNewUserAlert(List<String> ecasIDs) {
    }

    @Override
    public void sendTLCacheIssue(String country, String currentDigest, String otherDigest, int index) {
    }

}
