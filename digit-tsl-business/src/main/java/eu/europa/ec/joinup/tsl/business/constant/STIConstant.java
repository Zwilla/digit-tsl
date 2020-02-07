package eu.europa.ec.joinup.tsl.business.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** ETSI STI **/
public class STIConstant {

    /* 5.5.1.1 QTS(reg) */
    public static final String caQC = "https://uri.etsi.org/TrstSvc/Svctype/CA/QC";
    public static final String ocspQC = "https://uri.etsi.org/TrstSvc/Svctype/Certstatus/OCSP/QC";
    public static final String crlQC = "https://uri.etsi.org/TrstSvc/Svctype/Certstatus/CRL/QC";
    public static final String tsaQTST = "https://uri.etsi.org/TrstSvc/Svctype/TSA/QTST";
    public static final String edsQ = "https://uri.etsi.org/TrstSvc/Svctype/EDS/Q";
    public static final String edsRemQ = "https://uri.etsi.org/TrstSvc/Svctype/EDS/REM/Q";
    public static final String psesQ = "https://uri.etsi.org/TrstSvc/Svctype/PSES/Q";
    public static final String qesValidationQ = "https://uri.etsi.org/TrstSvc/Svctype/QESValidation/Q";

    /* 5.5.1.2 nonQTS(reg) */
    public static final String caPKC = "https://uri.etsi.org/TrstSvc/Svctype/CA/PKC";
    public static final String ocsp = "https://uri.etsi.org/TrstSvc/Svctype/Certstatus/OCSP";
    public static final String crl = "https://uri.etsi.org/TrstSvc/Svctype/Certstatus/CRL";
    public static final String tsa = "https://uri.etsi.org/TrstSvc/Svctype/TSA";
    public static final String tsaTssQC = "https://uri.etsi.org/TrstSvc/Svctype/TSA/TSS-QC";
    public static final String tsaTssAdesQaQES = "https://uri.etsi.org/TrstSvc/Svctype/TSA/TSS-AdESQCandQES";
    public static final String eds = "https://uri.etsi.org/TrstSvc/Svctype/EDS";
    public static final String edsRem = "https://uri.etsi.org/TrstSvc/Svctype/EDS/REM";
    public static final String pses = "https://uri.etsi.org/TrstSvc/Svctype/PSES";
    public static final String adesValidation = "https://uri.etsi.org/TrstSvc/Svctype/AdESValidation";
    public static final String adesGeneration = "https://uri.etsi.org/TrstSvc/Svctype/AdESGeneration";

    public static final List<String> qTrustServices = new ArrayList<>(Arrays.asList(caQC, ocspQC, crlQC, tsaQTST, edsQ, edsRemQ, psesQ, qesValidationQ));

    public static final List<String> qNonTrustServices = new ArrayList<>(Arrays.asList(caPKC, ocsp, crl, tsa, tsaTssQC, tsaTssAdesQaQES, eds, edsRem, pses, adesValidation, adesGeneration));

}
