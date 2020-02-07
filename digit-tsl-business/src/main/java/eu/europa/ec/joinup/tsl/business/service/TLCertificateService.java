package eu.europa.ec.joinup.tsl.business.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.data.tl.ServiceDataDTO;
import eu.europa.ec.joinup.tsl.business.dto.tl.approachbreak.CertificateElement;
import eu.europa.ec.joinup.tsl.business.repository.TLCertificateRepository;
import eu.europa.ec.joinup.tsl.business.util.CertificateTokenUtils;
import eu.europa.ec.joinup.tsl.business.util.DateUtils;
import eu.europa.ec.joinup.tsl.model.DBCertificate;
import eu.europa.ec.joinup.tsl.model.DBService;
import eu.europa.ec.joinup.tsl.model.enums.TLType;
import eu.europa.esig.dss.DSSASN1Utils;
import eu.europa.esig.dss.DSSDocument;
import eu.europa.esig.dss.FileDocument;
import eu.europa.esig.dss.client.http.DataLoader;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.AdvancedSignature;
import eu.europa.esig.dss.validation.CertificateVerifier;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;
import eu.europa.esig.dss.validation.SignatureValidationContext;
import eu.europa.esig.dss.validation.SignedDocumentValidator;
import eu.europa.esig.dss.x509.CertificateSourceType;
import eu.europa.esig.dss.x509.CertificateToken;
import eu.europa.esig.dss.x509.CommonTrustedCertificateSource;
import eu.europa.esig.dss.x509.RevocationToken;

@Service
public class TLCertificateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TLCertificateService.class);

    @Autowired
    private TLCertificateRepository certificateRepository;

    @Autowired
    private DataLoader dataLoader;

    /**
     * Get all certificates by @countryCode, @type. @checkDate used to initialize CertificateElement expiration
     *
     * @param countryCode
     * @param type
     * @return
     */
    public List<CertificateElement> getByCountryCode(String countryCode, TLType type, Date checkDate) {
        List<CertificateElement> certificateList = new ArrayList<>();
        for (DBCertificate dbCertificate : certificateRepository.getAllByCountryCodeAndTlType(countryCode, type)) {
            certificateList.add(new CertificateElement(dbCertificate, checkDate));
        }
        return certificateList;
    }

    /**
     * Get all certificates by @countryCode, @notBefore (set to end of day), @type.
     *
     * @param countryCode
     * @param notBefore
     * @param type
     */
    public List<CertificateElement> getByCountryCodeBeforeDate(String countryCode, Date notBefore, TLType type) {
        List<CertificateElement> certificateList = new ArrayList<>();
        for (DBCertificate dbCertificate : certificateRepository.getAllByCountryCodeAndNotBeforeLessThanAndTlType(countryCode, DateUtils.getEndOfDay(notBefore), type)) {
            certificateList.add(new CertificateElement(dbCertificate, notBefore));
        }
        return certificateList;
    }

    /**
     * Get certificate(s) that expire on given @expirationDate by @countryCode
     *
     * @param countryCode
     * @param expirationDate
     */
    public List<CertificateElement> getExpiredCertificateByCountryCode(String countryCode, Date expirationDate, TLType type, Date checkDate) {
        List<CertificateElement> certificateList = new ArrayList<>();

        for (DBCertificate dbCertificate : certificateRepository.getAllByCountryCodeAndNotAfterBetweenAndTlType(countryCode, DateUtils.getStartOfDay(expirationDate),
                DateUtils.getEndOfDay(expirationDate), type)) {
            certificateList.add(new CertificateElement(dbCertificate, checkDate));
        }
        return certificateList;
    }

    /**
     * Delete all the certificates from @countryCode
     *
     * @param countryCode
     */
    @Transactional(value = TxType.REQUIRES_NEW)
    public void deleteByCountryCode(String countryCode, TLType type) {
        certificateRepository.deleteByCountryCodeAndTlType(countryCode, type);
    }

    /**
     * Delete all the certificates from the LOTL
     */
    @Transactional(value = TxType.REQUIRES_NEW)
    public void deleteLOTL() {
        certificateRepository.deleteByTlType(TLType.LOTL);
    }

    /**
     * Store trusted list certificate in database
     *
     * @param certificate
     * @param countryCode
     * @param tlType
     * @param string
     */
    public void addCertificateEntry(CertificateToken certificate, String countryCode, TLType tlType, DBService service) {
        DBCertificate dbCertificate = new DBCertificate();
        dbCertificate.setCountryCode(countryCode);
        dbCertificate.setNotAfter(certificate.getNotAfter());
        dbCertificate.setNotBefore(certificate.getNotBefore());
        dbCertificate.setSubjectName(Base64.encodeBase64String(certificate.getSubjectX500Principal().toString().getBytes()));
        dbCertificate.setBase64(Utils.toBase64(certificate.getEncoded()));
        dbCertificate.setTlType(tlType);
        dbCertificate.setSki(DSSASN1Utils.computeSkiFromCert(certificate));
        dbCertificate.setService(service);
        try {
            certificateRepository.save(dbCertificate);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("AddCertificateEntry - Insert error ", e);
        }
    }

    /* ----- ----- Certificate chain ----- ----- */

    /**
     * Get current certificate chain of certificates not expired
     *
     * @param certificates
     */
    public List<CertificateElement> getCertificateChain(CertificateElement certificateReference, List<CertificateElement> certificates) {
        sortCertificateByExpiration(certificates);

        Set<CertificateElement> certificateChainSet = new HashSet<>();
        for (CertificateElement certificate : certificates) {
            if (!certificateReference.equals(certificate) && certificateReference.getNotAfter().after(certificate.getNotBefore())
                    && certificateReference.getNotAfter().before(certificate.getNotAfter())) {
                certificateChainSet.add(certificate);
                certificateReference = certificate;
            }
        }
        return new ArrayList<>(certificateChainSet);
    }

    /**
     * Get a list of TLs certificate distinct by Base64
     */
    public List<DBCertificate> getTLCertificateDistinctByB64() {
        List<DBCertificate> dbCertificates = certificateRepository.findByTlType(TLType.TL);

        Map<String, DBCertificate> distinctDbCertificates = new HashMap<>();
        for (DBCertificate certificate : dbCertificates) {
            if (!distinctDbCertificates.containsKey(certificate.getBase64())) {
                distinctDbCertificates.put(certificate.getBase64(), certificate);
            }
        }
        return new ArrayList<>(distinctDbCertificates.values());
    }

    /**
     * Get a list of certificate by SKI
     */
    public List<DBCertificate> findBySKI(CertificateToken certificate) {
        return certificateRepository.findBySki(DSSASN1Utils.computeSkiFromCert(certificate));
    }

    /* ----- ----- Retrieve Signing Certificates ----- ----- */

    /**
     * Get set of signing certificates from a signed file with validation (init TrustAnchors if possible)
     *
     * @param file
     */
    public Set<CertificateToken> retrieveSigningCertificatesFromFile(File file) {
        DSSDocument signedDocument = new FileDocument(file);
        Set<CertificateToken> signingCertificates = new HashSet<>();
        SignedDocumentValidator sdv = SignedDocumentValidator.fromDocument(signedDocument);
        // Init certificateVerifier with LOTL/TLs certificates
        sdv.setCertificateVerifier(initCommonCertificateVerifier());
        sdv.validateDocument();

        // Retrieve signature(s)
        for (AdvancedSignature signature : sdv.getSignatures()) {
            signingCertificates.add(signature.getSigningCertificateToken());
        }
        return signingCertificates;
    }

    /* ----- ----- Retrieve ROOT-CA ----- ----- */

    /**
     * Get list of trustAnchor services based on certificate trust anchor
     *
     * @param certificate
     */
    @Transactional(value = TxType.REQUIRED)
    public Set<ServiceDataDTO> getServicesByCertificate(CertificateToken certificate, SignatureValidationContext svc) {
        Set<CertificateToken> rootCAs = getRootCertificate(certificate, svc);
        Set<ServiceDataDTO> rootServices = new HashSet<>();
        if (!CollectionUtils.isEmpty(rootCAs)) {
            for (CertificateToken rootCA : rootCAs) {
                rootServices.addAll(getServicesFromRootCA(rootCA));
            }
        }
        return rootServices;
    }

    /**
     * Get list of trustAnchor services based on certificate trust anchor
     *
     * @param certificate
     */
    public Set<ServiceDataDTO> getServicesFromRootCA(CertificateToken rootCA) {
        Set<ServiceDataDTO> rootServices = new HashSet<>();
        if (rootCA != null) {
            List<DBCertificate> dbRootCertificates = findBySKI(rootCA);
            if (!CollectionUtils.isEmpty(dbRootCertificates)) {
                for (DBCertificate dbCertificate : dbRootCertificates) {
                    if (dbCertificate.getTlType().equals(TLType.TL)) {
                        rootServices.add(new ServiceDataDTO(dbCertificate.getService(), true));
                    }
                }

            }
        }
        return rootServices;
    }

    /**
     * Retrieve Trusted Root-CA of a given certificate.
     *
     * @param certificateB64
     */
    public Set<CertificateToken> getRootCertificate(CertificateToken certificateToken, SignatureValidationContext svc) {
        Set<CertificateToken> rootCertificates = new HashSet<>();
        Map<CertificateToken, Set<CertificateSourceType>> certificateSourceTypes = svc.getCertificateSourceTypes();
        // Loop through certificates and retrieve the trust one
        for (CertificateToken token : svc.getProcessedCertificates()) {
            Set<CertificateSourceType> sources = certificateSourceTypes.get(token);
            if (sources.contains(CertificateSourceType.TRUSTED_STORE)) {
                rootCertificates.add(token);
            }
        }

        return rootCertificates;
    }

    /**
     * Return if certificate is revoked
     * 
     * @param certificateToken
     */
    public boolean isCertificateRevoked(CertificateToken certificateToken, SignatureValidationContext svc) {
        Set<RevocationToken> processedRevocations = svc.getProcessedRevocations();
        for (RevocationToken revocationToken : processedRevocations) {
            if (certificateToken.getDSSIdAsString().equals(revocationToken.getRelatedCertificateID())) {
                return Utils.isTrue(revocationToken.getStatus());
            }
        }
        return false;
    }

    /**
     * Init CommonCertificateVerifier with all the certificate in database (LOTL/TLs)
     */
    private CertificateVerifier initCommonCertificateVerifier() {
        CertificateVerifier certificateVerifier = new CommonCertificateVerifier();
        CommonTrustedCertificateSource certSource = new CommonTrustedCertificateSource();

        certificateVerifier.setDataLoader(dataLoader);

        // Load TLs services and LOTL pointers certificates
        for (DBCertificate certificate : getTLCertificateDistinctByB64()) {
            CertificateToken certificateToken = CertificateTokenUtils.loadCertificate(certificate.getBase64());
            if (certificateToken != null) {
                certSource.addCertificate(certificateToken);
            }
        }
        certificateVerifier.setTrustedCertSource(certSource);
        return certificateVerifier;
    }

    /**
     * Init signature validation context with certificate token
     * 
     * @param certificateToken
     */
    public SignatureValidationContext initSVC(CertificateToken certificateToken) {
        SignatureValidationContext svc = new SignatureValidationContext();
        svc.initialize(initCommonCertificateVerifier());
        // Set certificate to track
        svc.addCertificateTokenForVerification(certificateToken);
        svc.validate();
        return svc;
    }

    /* ----- ----- Sort ----- ----- */

    /**
     * Sort Certificate element by expiration day (furthest to nearest)
     *
     * @param elements
     */
    public void sortCertificateByExpiration(List<CertificateElement> elements) {
        elements.sort(Comparator.comparingInt(CertificateElement::getExpirationIn));
    }

}
