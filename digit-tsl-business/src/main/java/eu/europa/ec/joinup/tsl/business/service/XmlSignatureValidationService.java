package eu.europa.ec.joinup.tsl.business.service;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.europa.esig.dss.DSSDocument;
import eu.europa.esig.dss.FileDocument;
import eu.europa.esig.dss.SignatureLevel;
import eu.europa.esig.dss.validation.CertificateVerifier;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;
import eu.europa.esig.dss.validation.executor.ValidationLevel;
import eu.europa.esig.dss.validation.reports.Reports;
import eu.europa.esig.dss.x509.CertificateToken;
import eu.europa.esig.dss.xades.XPathQueryHolder;
import eu.europa.esig.dss.xades.validation.XMLDocumentValidator;

/**
 * XML Signature Validation
 */
@Service
public class XmlSignatureValidationService extends AbstractSignatureValidationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlSignatureValidationService.class);

    @Value("${dss.constraint}")
    private String constraintUrl;

    @Override
    public Reports getReports(File tslFile, List<CertificateToken> potentialSigners) {
        CertificateVerifier certificateVerifier = new CommonCertificateVerifier(true);
        certificateVerifier.setTrustedCertSource(buildTrustedCertificateSource(potentialSigners));

        DSSDocument dssDocument = new FileDocument(tslFile);
        XMLDocumentValidator xmlDocumentValidator = new XMLDocumentValidator(dssDocument);

        File file = new File(constraintUrl);
        LOGGER.debug("CONSTRAINT FILE EXIST ? : " + file.exists());
        xmlDocumentValidator.setValidationLevel(ValidationLevel.BASIC_SIGNATURES);

        xmlDocumentValidator.setCertificateVerifier(certificateVerifier);
        // To increase the security: the default {@code XPathQueryHolder} is used.
        List<XPathQueryHolder> xPathQueryHolders = xmlDocumentValidator.getXPathQueryHolder();
        xPathQueryHolders.clear();
        xPathQueryHolders.add(new XPathQueryHolder());

        return xmlDocumentValidator.validateDocument(file);
    }

    @Override
    protected SignatureLevel getBaselineB() {
        return SignatureLevel.XAdES_BASELINE_B;
    }

}
