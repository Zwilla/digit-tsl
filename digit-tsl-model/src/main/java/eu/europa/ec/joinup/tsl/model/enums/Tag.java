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

public enum Tag {

    // TLINFORMATION
    TRUST_SERVICE_STATUS_LIST, SCHEME_INFORMATION, SCHEME_NAME, SCHEME_OPERATOR_NAME, SERVICE_NAME, TSP_NAME, TSP_TRADE_NAME, POSTAL_ADDRESSES, POSTAL_ADDRESS_STREET, POSTAL_ADDRESS_COUNTRY, POSTAL_ADDRESS_LOCALITY, POSTAL_ADDRESS_CODE, POSTAL_ADDRESS_PROVINCE, TSL_IDENTIFIER, VERSION_IDENTIFER, SEQUENCE_NUMBER, ISSUE_DATE, NEXT_UPDATE, TERRITORY, INFORMATION_URI, TSL_TYPE, STATUS_DETERMINATION, DISTRIBUTION_LIST, HISTORICAL_PERIOD, ELECTRONIC_ADDRESS, SCHEME_INFORMATION_URI, SCHEME_TYPE_COMMUNITY_RULES, TSP_INFORMATION_URI, TSP_INFORMATION_EXT, POLICY_OR_LEGAL_NOTICE,

    // POINTERS
    POINTERS_TO_OTHER_TSL, POINTER_MIME_TYPE, POINTER_SCHEME_TERRITORY, POINTER_LOCATION, POINTER_TYPE, POINTER_COMMUNITY_RULE,

    // SERVICE
    TSP_SERVICE, TSP_SERVICE_PROVIDER, SERVICE_TYPE_IDENTIFIER, SERVICE_STATUS, SERVICE_STATUS_STARTING_TIME, SCHEME_SERVICE_DEFINITION_URI, TSP_SERVICE_DEFINITION_URI, SERVICE_SUPPLY_POINT, SERVICE_EXTENSION, SERVICE_EXTENSION_REVOC_DATE, SERVICE_HISTORY, CRITERIA, POLICIES, OTHER_CRITERIA, POLICIES_BIT, KEY_USAGE, KEY_USAGE_BIT, SERVICE_TAKEN_OVER_BY, SERVICE_EXPIRED_CERT_REVOCATION,

    // HISTORY

    // OTHERS
    TSL_TAG, SERVICE_DIGITAL_IDENTITY, X509_CERTIFICATE, X509_SUBJECT_NAME, X509_SKI, OTHER, SERVICE_QUALIFICATION_EXT, SERVICE_QUALIFICATION_QUALIFIERS, SERVICE_ADDITIONNAL_EXT, SERVICE_TOB_EXT,

    // SIGNATURE
    SIGNATURE, SIGNING_CERTIFICATE, DIGEST_ALGORITHM, ENCRYPTION_ALGORITHM, INDICATION, SIGNATURE_FORMAT, SIGNED_BY, SIGNING_DATE,

    // NOTIFICATION
    NOTIFICATION,

    // CONTACT
    CONTACT, CONTACT_NAME, CONTACT_PHONE, CONTACT_ADDRESS,

    // USER
    USER,

    // IGNORE
    IGNORE, AMBIGUOUS,

    // COMPARISON
    COMPARISON_CHECK,
    // TRANSITION
    TRANSITION_CHECK,

    // TLCC
    Structure, AddressRules, CommonRules, DigitalIdentityRules, IntegrableTLCCRules, LangAttributedRules, SchemeInformationRules, ServiceInfoExtensionsRules, SignatureRules, StringRules, StringTypesRules, TSLRules, TSPRules, TSPServiceRules, URITypesRules;

}
