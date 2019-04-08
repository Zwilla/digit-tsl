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
package eu.europa.ec.joinup.tsl.business.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** ETSI STI **/
public class STIConstant {

    /* 5.5.1.1 QTS(reg) */
    public static final String caQC = "http://uri.etsi.org/TrstSvc/Svctype/CA/QC";
    public static final String ocspQC = "http://uri.etsi.org/TrstSvc/Svctype/Certstatus/OCSP/QC";
    public static final String crlQC = "http://uri.etsi.org/TrstSvc/Svctype/Certstatus/CRL/QC";
    public static final String tsaQTST = "http://uri.etsi.org/TrstSvc/Svctype/TSA/QTST";
    public static final String edsQ = "http://uri.etsi.org/TrstSvc/Svctype/EDS/Q";
    public static final String edsRemQ = "http://uri.etsi.org/TrstSvc/Svctype/EDS/REM/Q";
    public static final String psesQ = "http://uri.etsi.org/TrstSvc/Svctype/PSES/Q";
    public static final String qesValidationQ = "http://uri.etsi.org/TrstSvc/Svctype/QESValidation/Q";

    /* 5.5.1.2 nonQTS(reg) */
    public static final String caPKC = "http://uri.etsi.org/TrstSvc/Svctype/CA/PKC";
    public static final String ocsp = "http://uri.etsi.org/TrstSvc/Svctype/Certstatus/OCSP";
    public static final String crl = "http://uri.etsi.org/TrstSvc/Svctype/Certstatus/CRL";
    public static final String tsa = "http://uri.etsi.org/TrstSvc/Svctype/TSA";
    public static final String tsaTssQC = "http://uri.etsi.org/TrstSvc/Svctype/TSA/TSS-QC";
    public static final String tsaTssAdesQaQES = "http://uri.etsi.org/TrstSvc/Svctype/TSA/TSS-AdESQCandQES";
    public static final String eds = "http://uri.etsi.org/TrstSvc/Svctype/EDS";
    public static final String edsRem = "http://uri.etsi.org/TrstSvc/Svctype/EDS/REM";
    public static final String pses = "http://uri.etsi.org/TrstSvc/Svctype/PSES";
    public static final String adesValidation = "http://uri.etsi.org/TrstSvc/Svctype/AdESValidation";
    public static final String adesGeneration = "http://uri.etsi.org/TrstSvc/Svctype/AdESGeneration";

    public static final List<String> qTrustServices = new ArrayList<>(Arrays.asList(caQC, ocspQC, crlQC, tsaQTST, edsQ, edsRemQ, psesQ, qesValidationQ));

    public static final List<String> qNonTrustServices = new ArrayList<>(Arrays.asList(caPKC, ocsp, crl, tsa, tsaTssQC, tsaTssAdesQaQES, eds, edsRem, pses, adesValidation, adesGeneration));

}
