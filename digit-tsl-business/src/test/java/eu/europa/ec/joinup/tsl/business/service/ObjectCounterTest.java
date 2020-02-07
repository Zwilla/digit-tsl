package eu.europa.ec.joinup.tsl.business.service;

import java.io.FileInputStream;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLDigitalIdentification;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPolicies;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPoliciesBit;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLQualificationExtension;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceDto;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceExtension;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceHistory;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;
import eu.europa.esig.jaxb.tsl.TrustStatusListType;

public class ObjectCounterTest {
    // extends AbstractSpringTest {

    @Autowired
    private TrustedListJaxbService jaxbService;

    @Autowired
    private TLBuilder tlBuilder;

    int maxAllDigitalId = 0;
    int maxAllCertificate = 0;
    int maxAllServiceExtension = 0;
    int maxAllTakenOverBy = 0;
    int maxAllQualification = 0;
    int maxAllQualifiers = 0;
    int maxAllKeyUsage = 0;
    int maxAllPolicyList = 0;
    String locationQualification = "";
    String locationQualifiers = "";
    String locationKeyUsage = "";
    String locationPolicy = "";
    String maxAllCertificateLocation = "";

    // @Test
    public void countAllDigitalIdentities() throws Exception {
        TL tl = new TL();
        TrustStatusListType tsl = new TrustStatusListType();
        InputStream is = null;

        // AT
        is = new FileInputStream("src/test/resources/tsl/AT/2015-09-21_16-03.xml");
        tsl = jaxbService.unmarshallTSL(is);
        tl = tlBuilder.buildTLV4(1, tsl);
        countHistoryCertificate(tl);

        // BE
        is = new FileInputStream("src/test/resources/tsl/BE/2015-10-28_08-10-53.xml");
        tsl = jaxbService.unmarshallTSL(is);
        tl = tlBuilder.buildTLV4(1, tsl);
        countHistoryCertificate(tl);

        // BG
        is = new FileInputStream("src/test/resources/tsl/BG/2015-09-21_16-02.xml");
        tsl = jaxbService.unmarshallTSL(is);
        tl = tlBuilder.buildTLV4(1, tsl);
        countHistoryCertificate(tl);

        // CY
        is = new FileInputStream("src/test/resources/tsl/CY/2015-09-21_16-03.xml");
        tsl = jaxbService.unmarshallTSL(is);
        tl = tlBuilder.buildTLV4(1, tsl);
        countHistoryCertificate(tl);

        // CZ
        is = new FileInputStream("src/test/resources/tsl/CZ/2015-09-21_16-03.xml");
        tsl = jaxbService.unmarshallTSL(is);
        tl = tlBuilder.buildTLV4(1, tsl);
        countHistoryCertificate(tl);

        // DE
        is = new FileInputStream("src/test/resources/tsl/DE/2015-09-21_16-02.xml");
        tsl = jaxbService.unmarshallTSL(is);
        tl = tlBuilder.buildTLV4(1, tsl);
        countHistoryCertificate(tl);

        // TEST PROVIDER EXTENSIONS
        is = new FileInputStream("src/test/resources/tsl/DE/test-extension.xml");
        tsl = jaxbService.unmarshallTSL(is);
        tl = tlBuilder.buildTLV4(1, tsl);
        countHistoryCertificate(tl);

        // DK
        is = new FileInputStream("src/test/resources/tsl/DK/2015-09-21_16-02.xml");
        tsl = jaxbService.unmarshallTSL(is);
        tl = tlBuilder.buildTLV4(1, tsl);
        countHistoryCertificate(tl);

        // EE
        is = new FileInputStream("src/test/resources/tsl/EE/2015-09-21_16-03.xml");
        tsl = jaxbService.unmarshallTSL(is);
        tl = tlBuilder.buildTLV4(1, tsl);
        countHistoryCertificate(tl);

        // EL
        is = new FileInputStream("src/test/resources/tsl/EL/2015-09-21_16-03.xml");
        tsl = jaxbService.unmarshallTSL(is);
        tl = tlBuilder.buildTLV4(1, tsl);
        countHistoryCertificate(tl);

        // ES
        is = new FileInputStream("src/test/resources/tsl/ES/2015-09-21_16-03.xml");
        tsl = jaxbService.unmarshallTSL(is);
        tl = tlBuilder.buildTLV4(1, tsl);
        countHistoryCertificate(tl);

        // FI
        is = new FileInputStream("src/test/resources/tsl/FI/2015-09-21_16-02.xml");
        tsl = jaxbService.unmarshallTSL(is);
        tl = tlBuilder.buildTLV4(1, tsl);
        countHistoryCertificate(tl);

        // FR
        is = new FileInputStream("src/test/resources/tsl/FR/2015-09-21_16-02.xml");
        tsl = jaxbService.unmarshallTSL(is);
        tl = tlBuilder.buildTLV4(1, tsl);
        countHistoryCertificate(tl);

        // HR
        is = new FileInputStream("src/test/resources/tsl/HR/2015-09-21_16-02.xml");
        tsl = jaxbService.unmarshallTSL(is);
        tl = tlBuilder.buildTLV4(1, tsl);
        countHistoryCertificate(tl);

        // HU
        is = new FileInputStream("src/test/resources/tsl/HU/2015-09-21_16-03.xml");
        tsl = jaxbService.unmarshallTSL(is);
        tl = tlBuilder.buildTLV4(1, tsl);
        countHistoryCertificate(tl);

        // IE
        is = new FileInputStream("src/test/resources/tsl/IE/2015-09-21_16-03.xml");
        tsl = jaxbService.unmarshallTSL(is);
        tl = tlBuilder.buildTLV4(1, tsl);
        countHistoryCertificate(tl);

        // IS
        is = new FileInputStream("src/test/resources/tsl/IS/2015-09-21_16-03.xml");
        tsl = jaxbService.unmarshallTSL(is);
        tl = tlBuilder.buildTLV4(1, tsl);
        countHistoryCertificate(tl);

        // IT
        is = new FileInputStream("src/test/resources/tsl/IT/2015-09-21_16-03.xml");
        tsl = jaxbService.unmarshallTSL(is);
        tl = tlBuilder.buildTLV4(1, tsl);
        countHistoryCertificate(tl);

        // LI
        is = new FileInputStream("src/test/resources/tsl/LI/2015-09-21_16-03.xml");
        tsl = jaxbService.unmarshallTSL(is);
        tl = tlBuilder.buildTLV4(1, tsl);
        countHistoryCertificate(tl);

        // LT
        is = new FileInputStream("src/test/resources/tsl/LT/2015-09-21_16-02.xml");
        tsl = jaxbService.unmarshallTSL(is);
        tl = tlBuilder.buildTLV4(1, tsl);
        countHistoryCertificate(tl);

        // LU
        is = new FileInputStream("src/test/resources/tsl/LU/2015-09-21_16-02.xml");
        tsl = jaxbService.unmarshallTSL(is);
        tl = tlBuilder.buildTLV4(1, tsl);
        countHistoryCertificate(tl);

        // LV
        is = new FileInputStream("src/test/resources/tsl/LV/2015-09-21_16-02.xml");
        tsl = jaxbService.unmarshallTSL(is);
        tl = tlBuilder.buildTLV4(1, tsl);
        countHistoryCertificate(tl);

        // MT
        is = new FileInputStream("src/test/resources/tsl/MT/2015-09-21_16-03.xml");
        tsl = jaxbService.unmarshallTSL(is);
        tl = tlBuilder.buildTLV4(1, tsl);
        countHistoryCertificate(tl);

        // NL
        is = new FileInputStream("src/test/resources/tsl/NL/2015-09-21_16-03.xml");
        tsl = jaxbService.unmarshallTSL(is);
        tl = tlBuilder.buildTLV4(1, tsl);
        countHistoryCertificate(tl);

        // NO
        is = new FileInputStream("src/test/resources/tsl/NO/2015-09-21_16-02.xml");
        tsl = jaxbService.unmarshallTSL(is);
        tl = tlBuilder.buildTLV4(1, tsl);
        countHistoryCertificate(tl);

        // PL
        is = new FileInputStream("src/test/resources/tsl/PL/2015-09-21_16-03.xml");
        tsl = jaxbService.unmarshallTSL(is);
        tl = tlBuilder.buildTLV4(1, tsl);
        countHistoryCertificate(tl);

        // PT
        is = new FileInputStream("src/test/resources/tsl/PT/2015-09-21_16-02.xml");
        tsl = jaxbService.unmarshallTSL(is);
        tl = tlBuilder.buildTLV4(1, tsl);
        countHistoryCertificate(tl);

        // RO
        is = new FileInputStream("src/test/resources/tsl/RO/2015-09-21_16-03.xml");
        tsl = jaxbService.unmarshallTSL(is);
        tl = tlBuilder.buildTLV4(1, tsl);
        countHistoryCertificate(tl);

        // SE
        is = new FileInputStream("src/test/resources/tsl/SE/2015-09-21_16-03.xml");
        tsl = jaxbService.unmarshallTSL(is);
        tl = tlBuilder.buildTLV4(1, tsl);
        countHistoryCertificate(tl);

        // SI
        is = new FileInputStream("src/test/resources/tsl/SI/2015-09-21_16-03.xml");
        tsl = jaxbService.unmarshallTSL(is);
        tl = tlBuilder.buildTLV4(1, tsl);
        countHistoryCertificate(tl);

        // SK
        is = new FileInputStream("src/test/resources/tsl/SK/2015-09-21_16-03.xml");
        tsl = jaxbService.unmarshallTSL(is);
        tl = tlBuilder.buildTLV4(1, tsl);
        countHistoryCertificate(tl);

        // UK
        is = new FileInputStream("src/test/resources/tsl/UK/2015-09-21_16-03.xml");
        tsl = jaxbService.unmarshallTSL(is);
        tl = tlBuilder.buildTLV4(1, tsl);
        countHistoryCertificate(tl);

    }

    public void countDigitalIdentitiesMax(TL tl) {
        int nbMaxDigital = 0;
        int nbMaxCertificate = 0;
        int nbMaxServiceExtension = 0;
        int nbMaxQualification = 0;
        int nbMaxQualifiers = 0;
        int nbMaxkeyUsage = 0;
        int nbMaxPolicyList = 0;
        for (TLServiceProvider serviceProvider : tl.getServiceProviders()) {
            int nbServiceExtension = 0;
            if (serviceProvider.getTSPExtension() != null) {
                nbServiceExtension = serviceProvider.getTSPExtension().size();
            }
            if (nbServiceExtension > nbMaxServiceExtension) {
                nbMaxServiceExtension = nbServiceExtension;
            }
            if (nbMaxServiceExtension > maxAllServiceExtension) {
                maxAllServiceExtension = nbMaxServiceExtension;
            }
            // Service
            for (TLServiceDto service : serviceProvider.getTSPServices()) {
                int nbDigitalId = service.getDigitalIdentification().size();
                if (nbDigitalId > nbMaxDigital) {
                    nbMaxDigital = nbDigitalId;
                }
                if (nbMaxDigital > maxAllDigitalId) {
                    maxAllDigitalId = nbMaxDigital;
                }
                for (TLDigitalIdentification digitalId : service.getDigitalIdentification()) {
                    int nbCertificate = digitalId.getCertificateList().size();
                    if (nbCertificate > nbMaxCertificate) {
                        nbMaxCertificate = nbCertificate;
                    }
                }
                if (nbMaxCertificate > maxAllCertificate) {
                    maxAllCertificate = nbMaxCertificate;
                    maxAllCertificateLocation = tl.getSchemeInformation().getTerritory() + " - " + service.getServiceName().get(0).getValue();
                }

                for (TLServiceExtension extension : service.getExtension()) {
                    if (extension.getTakenOverBy() != null) {
                        if (extension.getTakenOverBy().getOtherQualifier() != null) {
                        }
                    }
                    if (extension.getExpiredCertsRevocationDate() != null) {
                    }
                    if (extension.getQualificationsExtension() != null) {
                        if (!service.getTypeIdentifier().contains("CA/QC")) {
                        }

                        int nbQualification = extension.getQualificationsExtension().size();
                        if (nbQualification > nbMaxQualification) {
                            nbMaxQualification = nbQualification;
                        }
                        for (TLQualificationExtension qualif : extension.getQualificationsExtension()) {
                            if (qualif.getQualifTypeList() != null) {
                                int nbQualifiers = qualif.getQualifTypeList().size();
                                if (nbQualifiers > nbMaxQualifiers) {
                                    nbMaxQualifiers = nbQualifiers;
                                }
                                // if(nbQualifiers>=2){
                                // }
                            }

                            if (qualif.getCriteria() != null) {
                            }
                            assert qualif.getCriteria() != null;
                            if (qualif.getCriteria().getKeyUsage() != null) {
                                int nbKeyUsage = qualif.getCriteria().getKeyUsage().size();
                                if (nbKeyUsage > nbMaxkeyUsage) {
                                    nbMaxkeyUsage = nbKeyUsage;
                                }
                            }
                            if (qualif.getCriteria().getPolicyList() != null) {
                                int nbPolicyList = qualif.getCriteria().getPolicyList().size();
                                if (nbPolicyList > nbMaxPolicyList) {
                                    nbMaxPolicyList = nbPolicyList;
                                }
                                // if(nbPolicyList>1){
                                // }
                                for (TLPolicies policy : qualif.getCriteria().getPolicyList()) {
                                    int nbPolicy = policy.getPolicyBit().size();
                                    if (nbPolicy > 2) {
                                    }

                                    for (TLPoliciesBit bit : policy.getPolicyBit()) {
                                        if (bit.getDescription() != null) {
                                        }
                                        if (bit.getDocumentationReferences() != null) {
                                        }
                                    }
                                }
                            }
                        }

                    }
                    if (nbMaxQualification > maxAllQualification) {
                        maxAllQualification = nbMaxQualification;
                        locationQualification = tl.getSchemeInformation().getTerritory() + " - " + service.getServiceName().get(0).getValue();
                    }
                    if (nbMaxQualifiers > maxAllQualifiers) {
                        maxAllQualifiers = nbMaxQualifiers;
                        locationQualifiers = tl.getSchemeInformation().getTerritory() + " - " + service.getServiceName().get(0).getValue();
                    }
                    if (nbMaxPolicyList > maxAllPolicyList) {
                        maxAllPolicyList = nbMaxPolicyList;
                        locationPolicy = tl.getSchemeInformation().getTerritory() + " - " + service.getServiceName().get(0).getValue();
                    }
                    if (nbMaxkeyUsage > maxAllKeyUsage) {
                        maxAllKeyUsage = nbMaxkeyUsage;
                        locationKeyUsage = tl.getSchemeInformation().getTerritory() + " - " + service.getServiceName().get(0).getValue();
                    }
                }

            }
        }
    }

    public void countHistoryCertificate(TL tl) throws Exception {
        for (TLServiceProvider tsp : tl.getServiceProviders()) {
            for (TLServiceDto service : tsp.getTSPServices()) {
                for (TLServiceHistory history : service.getHistory()) {
                    for (TLDigitalIdentification digit : history.getDigitalIdentification()) {
                        if ((digit.getCertificateList() != null) && (digit.getCertificateList().size() > 1)) {
                            throw new Exception();
                        }
                    }
                }
            }
        }
    }

}
