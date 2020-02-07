package eu.europa.ec.joinup.tsl.checker.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.cert.X509Certificate;
import java.util.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import edu.upc.ac.tsl.conformance.checker.forintegration.IntegrableTSLChecker;
import edu.upc.ac.tsl.conformance.tools.ByteArray;
import eu.europa.ec.joinup.tsl.checker.dto.TLCCFileRequestDTO;
import eu.europa.ec.joinup.tsl.checker.dto.TLCCRequestDTO;
import eu.europa.esig.dss.DSSUtils;
import eu.europa.esig.dss.x509.CertificateToken;
import eu.europa.esig.dss.x509.KeyStoreCertificateSource;

@Path("tlcc")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
public class RunTLCC {

    private static final Logger LOGGER = LoggerFactory.getLogger(RunTLCC.class);

    @Value("${lotl.keystore.file}")
    private String lotlKeyStoreFilename;

    @Value("${lotl.keystore.type}")
    private String lotlKeyStoreType;

    @Value("${lotl.keystore.password}")
    private String lotlKeyStorePassword;

    @POST
    @Path("executeAllChecks/{target}")
    public String executeAllChecks(TLCCRequestDTO requestDTO, @PathParam("target") String target) {
        File rulesFile = new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("AllRules_Integrable.csv")).getFile());
        String rulesPath = "";
        try {
            rulesPath = URLDecoder.decode(rulesFile.getAbsolutePath(), "utf-8");
        } catch (UnsupportedEncodingException e1) {
            LOGGER.error("TLCC read all rules integrable error.");
            throw new IllegalStateException("TLCC rules file can't be find or read.");
        }
        try {
            InputStream is = new FileInputStream(requestDTO.getLotlPath());
            EUMSTLsSigningCertsAsInECLOLReader asInECLOL = new EUMSTLsSigningCertsAsInECLOLReader(is);
            Map<String, TreeSet<ByteArray>> mapSigning = asInECLOL.getEUMSTLsSigingCertsByCountryCode();

            List<X509Certificate> keyStore = getKeyStore();

            IntegrableTSLChecker tslChecker = new IntegrableTSLChecker(mapSigning, keyStore, rulesPath, true);
            Element el = null;
            switch (target) {
            case "TRUSTED_LIST":
                el = tslChecker.verifyTL(requestDTO.getTlIdStr(), requestDTO.getTlXmlPath());
                break;
            case "SCHEME_INFORMATION":
                el = tslChecker.verifySchemeInfo(requestDTO.getTlIdStr(), requestDTO.getTlXmlPath());
                break;
            case "POINTERS_TO_OTHER_TSL":
                el = tslChecker.verifyPointer(requestDTO.getTlIdStr(), requestDTO.getTlXmlPath());
                break;
            case "TRUST_SERVICE_PROVIDER":
                el = tslChecker.verifyTSP(requestDTO.getTlIdStr(), requestDTO.getTlXmlPath(), requestDTO.getTspIndex());
                break;
            case "TSP_SERVICE":
                el = tslChecker.verifyService(requestDTO.getTlIdStr(), requestDTO.getTlXmlPath(), requestDTO.getTspIndex(), requestDTO.getServiceIndex());
                break;
            default:
                throw new IllegalStateException("TLCC can't run rules on target :" + target + ".");
            }

            StringWriter wrt = new StringWriter();
            TransformerFactory.newInstance().newTransformer().transform(new DOMSource(el), new StreamResult(wrt));
            return wrt.toString();

        } catch (UnsupportedEncodingException e) {
            LOGGER.error("TLCC read all rules integrable error.", e);
            throw new IllegalStateException("TLCC rules file can't be find or read.");
        } catch (Exception e) {
            LOGGER.error("Error TLCC call " + requestDTO, e);
            return null;
        }
    }

    @POST
    @Path("/file/executeAllChecks")
    public String executeAllChecksFromFile(TLCCFileRequestDTO tlccFileRequest) {
        File rulesFile = new File(this.getClass().getClassLoader().getResource("AllRules_Integrable.csv").getFile());
        String rulesPath = "";
        try (InputStream lotlIS = new ByteArrayInputStream(tlccFileRequest.getLotlFile()); InputStream tlIS = new ByteArrayInputStream(tlccFileRequest.getTlFile())) {
            rulesPath = URLDecoder.decode(rulesFile.getAbsolutePath(), "utf-8");
            EUMSTLsSigningCertsAsInECLOLReader asInECLOL = new EUMSTLsSigningCertsAsInECLOLReader(lotlIS);
            Map<String, TreeSet<ByteArray>> mapSigning = asInECLOL.getEUMSTLsSigingCertsByCountryCode();

            List<X509Certificate> keystore = new ArrayList<>();
            for (String b64 : tlccFileRequest.getKeyStore()) {
                CertificateToken token = DSSUtils.loadCertificateFromBase64EncodedString(b64);
                keystore.add(token.getCertificate());
            }

            IntegrableTSLChecker tslChecker = new IntegrableTSLChecker(mapSigning, keystore, rulesPath, true);
            Element el = tslChecker.verifyTL("1", tlIS);

            StringWriter wrt = new StringWriter();
            TransformerFactory.newInstance().newTransformer().transform(new DOMSource(el), new StreamResult(wrt));
            return wrt.toString();
        } catch (Exception e) {
            LOGGER.error("Error TLCC call ", e);
            return null;
        }
    }

    private List<X509Certificate> getKeyStore() throws Exception {
        File filesystem = new File(lotlKeyStoreFilename);
        KeyStoreCertificateSource keyStoreCertificateSource = new KeyStoreCertificateSource(filesystem, lotlKeyStoreType, lotlKeyStorePassword);
        List<X509Certificate> certificates = new ArrayList<>();
        for (CertificateToken token : keyStoreCertificateSource.getCertificates()) {
            certificates.add(token.getCertificate());
        }
        return certificates;
    }
}
