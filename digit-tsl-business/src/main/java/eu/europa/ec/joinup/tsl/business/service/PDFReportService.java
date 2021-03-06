package eu.europa.ec.joinup.tsl.business.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.xml.transform.Result;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.fop.apps.EnvironmentalProfileFactory;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopConfParser;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FopFactoryBuilder;
import org.apache.fop.apps.MimeConstants;
import org.apache.fop.apps.io.ResourceResolverFactory;
import org.apache.pdfbox.io.IOUtils;
import org.apache.xmlgraphics.io.Resource;
import org.apache.xmlgraphics.io.ResourceResolver;
import org.apache.xmlgraphics.io.TempResourceResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import eu.europa.ec.joinup.tsl.business.dto.CheckDTO;
import eu.europa.ec.joinup.tsl.business.dto.CheckResultDTO;
import eu.europa.ec.joinup.tsl.business.dto.NotificationPointers;
import eu.europa.ec.joinup.tsl.business.dto.TLDifference;
import eu.europa.ec.joinup.tsl.business.dto.TLSignature;
import eu.europa.ec.joinup.tsl.business.dto.User;
import eu.europa.ec.joinup.tsl.business.dto.pdf.PDFCheck;
import eu.europa.ec.joinup.tsl.business.dto.pdf.PDFChecksChanges;
import eu.europa.ec.joinup.tsl.business.dto.pdf.PDFInfoTL;
import eu.europa.ec.joinup.tsl.business.dto.pdf.PDFNotificationReport;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.util.PDFReportMultimapConverter;
import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

/**
 * PDF report generator (Notification/TL)
 */
@Service
public abstract class PDFReportService {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("messages");

    @Autowired
    private CheckService checkService;

    @Autowired
    private TLService tlService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private SignatureChangeService signatureChangeService;

    @Autowired
    private PDFNotificationReportBuilderService notificationReportBuilderService;

    @Value("${tlcc.active}")
    private boolean tlccActive;

    private FopFactory fopFactory;
    private FOUserAgent foUserAgent;
    private Templates templateOrderedCheckResult;
    private Templates templateNotificationChange;

    public void init() throws Exception {

        ResourceResolver rr = ResourceResolverFactory.createTempAwareResourceResolver(getTempResourceResolver(), getResourceResolver());
        FopFactoryBuilder builder = new FopConfParser(new ClassPathResource("/fop/fop.conf.xml").getInputStream(),
                EnvironmentalProfileFactory.createRestrictedIO(new ClassPathResource("/fop/").getURI(), rr)).getFopFactoryBuilder();
        builder.setAccessibility(true);

        fopFactory = builder.build();

        foUserAgent = fopFactory.newFOUserAgent();
        foUserAgent.setCreator("TL-Manager");
        foUserAgent.setAccessibility(true);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        InputStream simpleIS = new ClassPathResource("/xslt/TLStatusReport.xslt").getInputStream();
        templateOrderedCheckResult = transformerFactory.newTemplates(new StreamSource(simpleIS));
        IOUtils.closeQuietly(simpleIS);

        simpleIS = new ClassPathResource("/xslt/notificationChange.xslt").getInputStream();
        templateNotificationChange = transformerFactory.newTemplates(new StreamSource(simpleIS));
        IOUtils.closeQuietly(simpleIS);
    }

    /**
     * Generate Notification PDF report
     *
     * @param pointers
     * @param os
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void generateNotificationReport(NotificationPointers pointers, OutputStream os) throws Exception {
        foUserAgent.getRendererOptions().put("pdf-a-mode", "PDF");

        XStream stream = new XStream(new DomDriver("UTF-8"));
        stream.setMode(XStream.NO_REFERENCES);
        stream.processAnnotations(PDFNotificationReport.class);
        stream.registerConverter(new PDFReportMultimapConverter(stream.getMapper()));

        stream.processAnnotations(PDFChecksChanges.class);
        stream.processAnnotations(PDFCheck.class);
        stream.alias("check", CheckDTO.class);
        stream.alias("tag", Tag.class);
        stream.alias("result", CheckResultDTO.class);
        stream.alias("user", User.class);

        String xml = stream.toXML(notificationReportBuilderService.buildNotificationReport(pointers));
        Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, os);
        Result res = new SAXResult(fop.getDefaultHandler());
        Transformer transformer = templateNotificationChange.newTransformer();
        InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        transformer.transform(new StreamSource(is), res);
    }

    /**
     * Generate TL PDF report from TL
     *
     * @param tl
     * @param os
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void generateTLReport(TL tl, OutputStream os) throws Exception {
        foUserAgent.getRendererOptions().put("pdf-a-mode", "PDF");
        PDFChecksChanges toSerial = initPDFToSerial(tl);

        XStream stream = new XStream();
        stream.setMode(XStream.NO_REFERENCES);
        stream.processAnnotations(PDFChecksChanges.class);
        stream.processAnnotations(PDFCheck.class);
        stream.alias("check", CheckDTO.class);
        stream.alias("tag", Tag.class);
        stream.alias("change", TLDifference.class);
        stream.alias("result", CheckResultDTO.class);
        String xml = stream.toXML(toSerial);

        Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, os);
        Result res = new SAXResult(fop.getDefaultHandler());
        Transformer transformer = templateOrderedCheckResult.newTransformer();
        transformer.transform(new StreamSource(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))), res);

    }

    /**
     * Generate TL PDF report from PDFChecksChanges
     * 
     * @param toSerial
     * @param os
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void generateTLReport(PDFChecksChanges toSerial, OutputStream os) throws Exception {
        foUserAgent.getRendererOptions().put("pdf-a-mode", "PDF");

        XStream stream = new XStream();
        stream.setMode(XStream.NO_REFERENCES);
        stream.processAnnotations(PDFChecksChanges.class);
        stream.processAnnotations(PDFCheck.class);
        stream.alias("check", CheckDTO.class);
        stream.alias("tag", Tag.class);
        stream.alias("change", TLDifference.class);
        stream.alias("result", CheckResultDTO.class);
        String xml = stream.toXML(toSerial);

        Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, os);
        Result res = new SAXResult(fop.getDefaultHandler());
        Transformer transformer = templateOrderedCheckResult.newTransformer();
        transformer.transform(new StreamSource(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))), res);

    }

    public PDFChecksChanges initPDFToSerial(TL tl) {
        PDFChecksChanges toSerial = new PDFChecksChanges();
        String countryName = "";
        if (tl.getDbStatus().equals(TLStatus.PROD)) {
            countryName += bundle.getString("pdf.published");
        } else {
            countryName += bundle.getString("pdf.draft");
        }
        countryName += " " + tl.getSchemeInformation().getTerritory() + " (Sn" + tl.getSchemeInformation().getSequenceNumber() + ")";
        toSerial.setCountryName(countryName);

        TLSignature currentSignature = tlService.getSignatureInfo(tl.getTlId());
        PDFInfoTL infoTl = new PDFInfoTL(tl, currentSignature);
        toSerial.setCurrentTL(infoTl);
        // Compared TL
        TL comparedTl = null;
        if (tl.getDbStatus().equals(TLStatus.DRAFT)) {
            toSerial.setProduction(false);
            comparedTl = tlService.getPublishedTLByCountryCode(tl.getSchemeInformation().getTerritory());
            if (comparedTl != null) {
                TLSignature comparedSignature = tlService.getSignatureInfo(comparedTl.getTlId());
                toSerial.setComparedTL(new PDFInfoTL(comparedTl, comparedSignature));
            }
        } else {
            toSerial.setProduction(true);
            DBCountries country = countryService.getCountryByTerritory(tl.getSchemeInformation().getTerritory());
            comparedTl = tlService.getPreviousProductionByCountry(country);
            if (comparedTl != null) {
                TLSignature comparedSignature = tlService.getSignatureInfo(comparedTl.getTlId());
                toSerial.setComparedTL(new PDFInfoTL(comparedTl, comparedSignature));
            }
        }

        if (comparedTl != null) {
            List<TLDifference> diffProviders = new ArrayList<>();
            List<TLDifference> diffSignature = new ArrayList<>();

            List<TLDifference> diffSchemeInfo = new ArrayList<>(tl.asPublishedDiff(comparedTl));
            List<TLDifference> diffPointers = new ArrayList<>(tl.getPointersDiff(comparedTl.getPointers(), tl.getId() + "_" + Tag.POINTERS_TO_OTHER_TSL));

            if ((tl.getServiceProviders() != null) && (comparedTl.getServiceProviders() != null)) {
                diffProviders.addAll(tl.getTrustServiceProvidersDiff(comparedTl.getServiceProviders(), tl.getId() + "_" + Tag.TSP_SERVICE_PROVIDER));
            }
            diffSignature = signatureChangeService.getSignatureChanges(tl, comparedTl);
            // schemeChanges
            toSerial.setSchemeChanges(diffSchemeInfo.size());
            toSerial.setPointerChanges(diffPointers.size());
            toSerial.setServiceChanges(diffProviders.size());
            toSerial.setSignatureChanges(diffSignature.size());
            toSerial.setNumberOfChanges(diffSchemeInfo.size() + diffPointers.size() + diffProviders.size() + diffSignature.size());

            toSerial.setSchemeInformationChanges(diffSchemeInfo);
            toSerial.setPointersToOtherTslChanges(diffPointers);
            toSerial.setServiceProviderChanges(diffProviders);
            toSerial.setSignatureListChanges(diffSignature);
        }

        toSerial.setTlccActive(tlccActive);
        if (tlccActive) {
            final List<CheckResultDTO> errorTlccChecks = checkService.getTLChecksResuls(tl.getTlId());
            if (CollectionUtils.isNotEmpty(errorTlccChecks)) {
                toSerial.setChecks(errorTlccChecks);
            }
        }
        return toSerial;
    }

    private ResourceResolver getResourceResolver() {
        return new ResourceResolver() {

            @Override
            public Resource getResource(URI uri) throws IOException {
                return new Resource(new ClassPathResource("/fop/" + uri.toString()).getInputStream());
            }

            @Override
            public OutputStream getOutputStream(URI uri) throws IOException {
                return new FileOutputStream(new File(uri));
            }
        };
    }

    private TempResourceResolver getTempResourceResolver() {
        return new TempResourceResolver() {

            private final Map<String, ByteArrayOutputStream> tempBaos = Collections.synchronizedMap(new HashMap<String, ByteArrayOutputStream>());

            @Override
            public Resource getResource(String id) {
                if (!tempBaos.containsKey(id)) {
                    throw new IllegalArgumentException("Resource with id = " + id + " does not exist");
                }
                return new Resource(new ByteArrayInputStream(tempBaos.remove(id).toByteArray()));
            }

            @Override
            public OutputStream getOutputStream(String id) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                tempBaos.put(id, out);
                return out;
            }
        };
    }
}
