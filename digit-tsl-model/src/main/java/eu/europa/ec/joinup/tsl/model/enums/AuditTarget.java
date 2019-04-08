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
package eu.europa.ec.joinup.tsl.model.enums;

public enum AuditTarget {

    NOTIFICATION, DRAFT_TL, PROD_TL, ADMINISTRATION_USER, ADMINISTRATION_PROPERTIES, ADMINISTRATION_LIST_SIGNING_CERT, ADMINISTRATION_CHECKS, CONTACT_LIST, JOBS, CACHE, NEW_TL, NEW_NOTIFICATION, NOTIFICATION_INTEGRATED, NEW_SIGNING_CERTIFICATE, UNAVAILABILITY, TL_STATUS_BREAK, SIGNATURE_INVALID, RETENTION_POLICY, DRAFTSTORE, USER;

}
