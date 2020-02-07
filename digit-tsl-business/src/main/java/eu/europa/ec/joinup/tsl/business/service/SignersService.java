package eu.europa.ec.joinup.tsl.business.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import eu.europa.esig.dss.x509.CertificateToken;
import eu.europa.esig.dss.x509.KeyStoreCertificateSource;
import eu.europa.esig.jaxb.tsl.TrustStatusListType;

/**
 * Retrieve potentials authorized signers for a trusted list
 */
@Service
public class SignersService {

    @Autowired
    @Qualifier("lotlKeyStore")
    private KeyStoreCertificateSource lotlKeyStore;

    @Autowired
    private TSLExtractor extractor;

    @Autowired
    private TLService tlService;

    @Autowired
    private CountryService countryService;

    /**
     * This method returns all allowed certificates to sign a TL or a LOTL
     */
    public Map<String, List<CertificateToken>> getAllPotentialsSigners() {
        Map<String, List<CertificateToken>> tlPotentialsSigners = getTLPotentialsSigners();

        List<CertificateToken> lotlPotentialSigners = lotlKeyStore.getCertificates();
        tlPotentialsSigners.put(countryService.getLOTLCountry().getCodeTerritory(), lotlPotentialSigners);

        return tlPotentialsSigners;
    }

    /**
     * This method returns all allowed certificates to sign a TL only
     */
    public Map<String, List<CertificateToken>> getTLPotentialsSigners() {
        TrustStatusListType lotlProductionJaxb = tlService.getLOTLProductionJaxb();
        Map<String, List<CertificateToken>> potentialSigners = extractor.getPotentialSigners(lotlProductionJaxb);
        return potentialSigners;
    }

    public List<CertificateToken> getCertificatesFromKeyStore() {
        return lotlKeyStore.getCertificates();
    }

}
