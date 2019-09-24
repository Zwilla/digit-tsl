package eu.europa.ec.joinup.tsl.business.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.data.tl.ServiceDataDTO;
import eu.europa.ec.joinup.tsl.business.dto.sieQ.SieQResult;
import eu.europa.ec.joinup.tsl.business.dto.sieQ.SieQSearchType;
import eu.europa.ec.joinup.tsl.business.dto.sieQ.SieQServiceEntry;
import eu.europa.ec.joinup.tsl.business.dto.sieQ.SieQValidationForm;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceDto;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;
import eu.europa.ec.joinup.tsl.business.util.CertificateTokenUtils;
import eu.europa.esig.dss.client.http.commons.CommonsDataLoader;
import eu.europa.esig.dss.tsl.Condition;
import eu.europa.esig.dss.tsl.ServiceInfo;
import eu.europa.esig.dss.tsl.ServiceInfoStatus;
import eu.europa.esig.dss.tsl.TrustedListsCertificateSource;
import eu.europa.esig.dss.tsl.service.TSLRepository;
import eu.europa.esig.dss.tsl.service.TSLValidationJob;
import eu.europa.esig.dss.validation.SignatureValidationContext;
import eu.europa.esig.dss.x509.CertificateToken;
import eu.europa.esig.dss.x509.KeyStoreCertificateSource;

@Service
public class SieQValidationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheDataLoader.class);

    private static final ResourceBundle bundle = ResourceBundle.getBundle("messages");

    private final String OJ_URL = "http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=uriserv:OJ.C_.2016.233.01.0001.01.ENG";
    private final String LOTL_ROOT_SCHEME_INFO_URI = "https://ec.europa.eu/information_society/policy/esignature/trusted-list/tl.html";
    private TrustedListsCertificateSource trustedListsCertificateSource;

    @Autowired
    @Qualifier("lotlKeyStore")
    private KeyStoreCertificateSource lotlKeyStore;

    @Autowired
    private ApplicationPropertyService applicationPropertyService;

    @Autowired
    private TLCertificateService tlCertificateService;

    @Autowired
    private TLService tlService;

    @Transactional
    public SieQResult getMatchingQualifiers(SieQValidationForm form) {
        CertificateToken certificate = null;
        if (SieQSearchType.FILE.equals(form.getCertificateType())) {
            try {
                if (form.getCertificateFile() != null && form.getCertificateFile().getBytes() != null) {
                    certificate = CertificateTokenUtils.loadCertificate(form.getCertificateFile().getBytes());
                }
            } catch (IOException e) {
                certificate = null;
                LOGGER.error("SieQ validation certificate parsing error", e);
            }
        } else {
            if (!StringUtils.isEmpty(form.getCertificateB64())) {
                certificate = CertificateTokenUtils.loadCertificate(form.getCertificateB64());
            }
        }

        if (certificate == null) {
            throw new IllegalArgumentException(bundle.getString("error.sieQValidation.formIncorrect"));
        } else {
            return getMatchingQualifiers(certificate);
        }
    }

    /**
     * Get matching qualifiers for given certificate
     * 
     * @param certificate
     */
    private SieQResult getMatchingQualifiers(CertificateToken certificate) {
        SieQResult result = new SieQResult();
        if (trustedListsCertificateSource == null) {
            refreshTLCertificateSource();
        }

        SignatureValidationContext svc = tlCertificateService.initSVC(certificate);
        Set<CertificateToken> rootCAs = tlCertificateService.getRootCertificate(certificate, svc);
        result.setCertificate(certificate);

        Map<String, TL> tls = new HashMap<>();
        // Loop through rootCA found for given certificate
        if (!CollectionUtils.isEmpty(rootCAs)) {
            for (CertificateToken rootCA : rootCAs) {
                if (rootCA != null) {
                    boolean serviceFound = false;
                    Set<ServiceDataDTO> services = tlCertificateService.getServicesFromRootCA(rootCA);
                    for (ServiceDataDTO serviceData : services) {
                        TL tl = retrieveTL(tls, serviceData);

                        Set<ServiceInfo> sis = trustedListsCertificateSource.getTrustServices(rootCA);
                        for (ServiceInfo serviceInfo : sis) {
                            ServiceInfoStatus currentStatus = serviceInfo.getStatus().getCurrent(new Date());
                            // Match services find from TLM database with ServiceInfo entry from
                            if (currentStatus != null && currentStatus.getServiceName().equals(serviceData.getMName())) {
                                serviceFound = true;
                                final TLServiceDto tmpService = getService(tl, serviceData);
                                if (tmpService != null) {
                                    result.getServices().add(new SieQServiceEntry(tl.getSchemeInformation().getTerritory(), tmpService, getQualifiers(currentStatus, certificate)));
                                } else {
                                    LOGGER.error("Service (from TL) not found based on service ID :" + serviceData.getMName() + " / " + serviceData.getServiceId());
                                }
                            }
                        }
                        if (!serviceFound) {
                            LOGGER.error("Service (from TLM database) does not match any DSS ServiceInfo :" + serviceData.getMName());
                        }
                    }

                }
            }
        }
        return result;
    }

    private TLServiceDto getService(TL tl, ServiceDataDTO serviceData) {
        if (!CollectionUtils.isEmpty(tl.getServiceProviders())) {
            for (TLServiceProvider tsp : tl.getServiceProviders()) {
                if (!CollectionUtils.isEmpty(tsp.getTSPServices())) {
                    for (TLServiceDto service : tsp.getTSPServices()) {
                        if (service.getId().equals(serviceData.getServiceId())) {
                            service.setHistory(null);
                            return service;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Retrieve TL from map or get it from DB
     * 
     * @param tls
     * @param serviceData
     */
    private TL retrieveTL(Map<String, TL> tls, ServiceDataDTO serviceData) {
        final String countryCode = serviceData.getCountryCode();
        TL tl = null;
        if (tls.containsKey(countryCode)) {
            tl = tls.get(countryCode);
        } else {
            tl = tlService.getPublishedTLByCountryCode(countryCode);
            tls.put(countryCode, tl);
        }
        return tl;
    }

    /**
     * Refresh TrustedListsCertificateSource (trigger on new TL load)
     */
    public void refreshTLCertificateSource() {
        final String lotlUrl = applicationPropertyService.getLOTLUrl();
        TSLRepository repository = new TSLRepository();
        trustedListsCertificateSource = new TrustedListsCertificateSource();
        repository.setTrustedListsCertificateSource(trustedListsCertificateSource);

        TSLValidationJob job = new TSLValidationJob();
        job.setCheckLOTLSignature(true);
        job.setCheckTSLSignatures(true);
        job.setDataLoader(new CommonsDataLoader());
        job.setLotlUrl(lotlUrl);
        job.setLotlRootSchemeInfoUri(LOTL_ROOT_SCHEME_INFO_URI);
        job.setOjUrl(OJ_URL);
        job.setLotlCode("EU");
        job.setOjContentKeyStore(lotlKeyStore);
        job.setRepository(repository);

        job.refresh();
        trustedListsCertificateSource.getCertificates();
    }

    /**
     * Get matching qualifiers
     * 
     * @param serviceInfoStatus
     * @param certificateToken
     */
    private List<String> getQualifiers(ServiceInfoStatus serviceInfoStatus, CertificateToken certificateToken) {
        List<String> list = new ArrayList<String>();
        final Map<String, List<Condition>> qualifiersAndConditions = serviceInfoStatus.getQualifiersAndConditions();
        for (Entry<String, List<Condition>> conditionEntry : qualifiersAndConditions.entrySet()) {
            List<Condition> conditions = conditionEntry.getValue();
            for (final Condition condition : conditions) {
                if (condition.check(certificateToken)) {
                    list.add(conditionEntry.getKey());
                    break;
                }
            }
        }
        return list;

    }

}
