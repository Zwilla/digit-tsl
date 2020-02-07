package eu.europa.ec.joinup.tsl.business.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.XmlMappingException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import eu.europa.ec.joinup.tsl.business.dto.CheckDTO;
import eu.europa.ec.joinup.tsl.business.dto.CheckResultDTO;
import eu.europa.ec.joinup.tsl.business.dto.NotificationPointers;
import eu.europa.ec.joinup.tsl.business.dto.TLDifference;
import eu.europa.ec.joinup.tsl.business.dto.TLSignature;
import eu.europa.ec.joinup.tsl.business.dto.User;
import eu.europa.ec.joinup.tsl.business.dto.notification.MemberStateNotificationV5;
import eu.europa.ec.joinup.tsl.business.dto.notification.NotificationDto;
import eu.europa.ec.joinup.tsl.business.dto.notification.PDFMeasure;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLCertificate;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLDigitalIdentification;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLName;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLSchemeTypeCommunityRule;
import eu.europa.ec.joinup.tsl.business.repository.NotificationRepository;
import eu.europa.ec.joinup.tsl.business.util.LocationUtils;
import eu.europa.ec.joinup.tsl.business.util.PDFUtils;
import eu.europa.ec.joinup.tsl.business.util.TLChecksComparatorUtils;
import eu.europa.ec.joinup.tsl.business.util.TLNotificationMapper;
import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBNotification;
import eu.europa.ec.joinup.tsl.model.DBSignatureInformation;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.DBUser;
import eu.europa.ec.joinup.tsl.model.enums.CheckStatus;
import eu.europa.ec.joinup.tsl.model.enums.MimeType;
import eu.europa.ec.joinup.tsl.model.enums.NotificationStatus;
import eu.europa.ec.joinup.tsl.model.enums.SignatureStatus;
import eu.europa.ec.joinup.tsl.model.enums.Tag;
import eu.europa.esig.dss.DSSDocument;
import eu.europa.esig.dss.x509.CertificateToken;
import eu.europa.esig.jaxb.v5.tsl.OtherTSLPointerTypeV5;

/**
 * Notification management
 */
@Service
@Transactional(value = TxType.REQUIRED)
public class NotificationService {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    @Autowired
    NotificationJaxbService jaxbService;

    @Autowired
    FileService fileService;

    @Autowired
    CountryService countryService;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    private XmlSignatureValidationService xmlSignatureValidationService;

    @Autowired
    private SignersService signersService;

    @Autowired
    private TLService tlService;

    @Autowired
    private RulesRunnerService rulesRunner;

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    /* ----- ----- Notification form management ----- ----- */

    /**
     * Get notification by @countryCode;
     *
     * <pre>
     * Init:
     * - Pointers
     * - TLSOContact
     * - User in DB
     * - Signature to "NOT_SIGNED"
     * </pre>
     *
     * @param countryCode
     * @return
     */
    public NotificationPointers getNotification(String countryCode) {
        NotificationPointers notificationPointer = new NotificationPointers();
        List<TLPointersToOtherTSL> pointers = tlService.getLOTLPointer(countryCode);
        for (TLPointersToOtherTSL pointer : pointers) {
            if (pointer.getSchemeTerritory().equalsIgnoreCase(countryCode)) {
                if (pointer.getMimeType().equals(MimeType.XML)) {
                    notificationPointer.setMpPointer(pointer);
                } else if (pointer.getMimeType().equals(MimeType.PDF)) {
                    notificationPointer.setHrPointer(pointer);
                }
            }
        }
        notificationPointer.setTlsoContact(contactService.getAllContactByTerritory(countryCode));

        TLSignature signature = new TLSignature();
        signature.setIndication(SignatureStatus.NOT_SIGNED);
        notificationPointer.setSignatureInformation(signature);

        notificationPointer.initUsers(userService.findAuthenticatedUserByTerritory(countryCode));
        return notificationPointer;
    }

    /**
     * Get signature from the XML model attribute
     */
    public TLSignature getSignatureModelXML(String territory, DSSDocument xmlDoc) throws IOException {
        File tmp = File.createTempFile("Notif", "xml");
        FileOutputStream fos = null;
        fos = new FileOutputStream(tmp);
        xmlDoc.writeTo(fos);
        IOUtils.closeQuietly(fos);

        Map<String, List<CertificateToken>> potentialsSigners = signersService.getTLPotentialsSigners();
        DBSignatureInformation status = xmlSignatureValidationService.validateNotificationTmpFile(tmp, potentialsSigners.get(territory));
        TLSignature signature = new TLSignature(status);
        return signature;
    }

    /**
     * Store XML notification in byteArrayOutputStream
     *
     * @param notification
     */
    public ByteArrayOutputStream getNotificationOs(NotificationPointers notification) {
        if (notification.getHrPointer() != null && StringUtils.isNotEmpty(notification.getHrPointer().getTlLocation())) {
            String hrLocation = notification.getHrPointer().getTlLocation();
            notification.getHrPointer().setTlLocation(hrLocation);
            notification.getHrPointer().setMimeType(MimeType.PDF);
            if (notification.getMpPointer() != null) {
                notification.getHrPointer().setSchemeOpeName(notification.getMpPointer().getSchemeOpeName());
                notification.getHrPointer().setSchemeTerritory(notification.getMpPointer().getSchemeTerritory());
                notification.getHrPointer().setSchemeTypeCommunity(notification.getMpPointer().getSchemeTypeCommunity());
                notification.getHrPointer().setServiceDigitalId(notification.getMpPointer().getServiceDigitalId());
            } else {
                notification.getHrPointer().setSchemeTerritory(notification.getTlsoContact().getTerritory());
            }
        } else if (notification.getHrPointer() != null && StringUtils.isEmpty(notification.getHrPointer().getTlLocation()) && StringUtils.isEmpty(notification.getHrPointer().getSchemeTerritory())) {
            notification.setHrPointer(null);
        }
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        String docID = notification.getMpPointer().getSchemeTerritory() + "-" + df.format(notification.getCreationDate());

        MemberStateNotificationV5 memberStateNotificationV5 = TLNotificationMapper.mapDTONotificationToXMLNotificationObj(notification, docID,
                contactService.getDeletedContact(notification.getTlsoContact()));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            jaxbService.marshallNotification(memberStateNotificationV5, out);
        } catch (IOException e) {
            LOGGER.info("createDraftFromBinaries error : " + e.getMessage(), e);
        }

        return out;
    }

    /**
     * Verify notification signature with certificate(s) of the concerned territory from the pointer to other tsl of the LOTL
     *
     * @param notifId
     */
    public SignatureStatus verifyNotificationSignature(int notifId) {
        DBNotification dbNotif = notificationRepository.findOne(notifId);
        List<CertificateToken> potentialsSigners = signersService.getTLPotentialsSigners().get((dbNotif.getTerritory().getCodeTerritory()));
        DBFiles xmlFile = dbNotif.getNotificationFile();
        if ((xmlFile != null) && StringUtils.isNotEmpty(xmlFile.getLocalPath())) {
            DBSignatureInformation signatureInfo = xmlSignatureValidationService.validateNotification(xmlFile, potentialsSigners);
            xmlFile.setSignatureInformation(signatureInfo);
            return signatureInfo.getIndication();
        }
        return null;
    }

    /**
     * Init TLPointersToOtherTSL from notification JaxB format
     *
     * @param notif
     * @param mimeType
     */
    public TLPointersToOtherTSL getPointer(MemberStateNotificationV5 notif, MimeType mimeType) {
        if (notif != null) {
            for (OtherTSLPointerTypeV5 tPtot : notif.getPointersToOtherTSL()) {
                TLPointersToOtherTSL tPtotMarshalled = new TLPointersToOtherTSL(0, "", tPtot);
                if (tPtotMarshalled.getMimeType().equals(mimeType)) {
                    return tPtotMarshalled;
                }
            }
        }
        return new TLPointersToOtherTSL();
    }

    /* ----- ----- Notification checks/changes ----- ----- */

    /**
     * Calcul notification changes and init legal measures
     *
     * @param notification
     * @param isPDF
     */
    public List<PDFMeasure> getNotificationChanges(NotificationPointers notification, boolean isPDF) {
        List<PDFMeasure> notificationChanges = new ArrayList<>();

        NotificationPointers originalNotification = getNotification(notification.getMpPointer().getSchemeTerritory());
        List<TLDifference> diffList = new ArrayList<>();

        if ((notification.getMpPointer() != null) && (originalNotification.getMpPointer() != null)) {
            diffList.addAll(notification.getMpPointer().asPublishedDiff(originalNotification.getMpPointer()));
        }

        if ((notification.getHrPointer() != null) && (originalNotification.getHrPointer() != null)) {
            diffList.addAll(notification.getHrPointer().asPublishedDiff(originalNotification.getHrPointer()));
        }

        if ((notification.getHrPointer() != null) && !StringUtils.isEmpty(notification.getHrPointer().getTlLocation()) && (originalNotification.getHrPointer() == null)) {
            notificationChanges.add(PDFUtils.PDFLocationAdded(notification.getMpPointer().getSchemeTerritory(), notification.getHrPointer().getTlLocation(), isPDF));
        }
        notificationChanges.addAll(PDFUtils.convertDifferences(diffList, notification, isPDF));
        // Contact
        PDFMeasure conctactChange = PDFUtils.detectSchemeContactChange(notification, originalNotification,
                contactService.getContactChanges(notification.getTlsoContact(), notification.getMpPointer().getSchemeTerritory(), ""), isPDF);
        if (!CollectionUtils.isEmpty(conctactChange.getChange())) {
            notificationChanges.add(conctactChange);
        }
        // User authentified
        List<TLDifference> userChanges = userService.getUserDifference("", notification.getUsers(), notification.getTlsoContact().getTerritory());
        if (!CollectionUtils.isEmpty(userChanges)) {
            notificationChanges.add(PDFUtils.detectUserChanges(userChanges, isPDF));
        }

        return notificationChanges;
    }

    /**
     * Calcul certificates deleted between notification and related pointer in the LOTL
     *
     * @param notifPointer
     * @param prodPointer
     * @return
     */
    public List<TLCertificate> getPublishedCertificateDeleted(NotificationPointers notifPointer, NotificationPointers prodPointer) {
        List<TLDigitalIdentification> notifDigitalId = new ArrayList<>();
        List<TLDigitalIdentification> prodDigitalId = new ArrayList<>();
        // Init notif digital id
        if (notifPointer.getMpPointer() != null) {
            notifDigitalId = notifPointer.getMpPointer().getServiceDigitalId();
        } else if (notifPointer.getHrPointer() != null) {
            notifDigitalId = notifPointer.getHrPointer().getServiceDigitalId();
        }
        // Init prod digital id
        if (prodPointer.getMpPointer() != null) {
            prodDigitalId = prodPointer.getMpPointer().getServiceDigitalId();
        } else if (prodPointer.getHrPointer() != null) {
            prodDigitalId = prodPointer.getHrPointer().getServiceDigitalId();
        }

        prodDigitalId.removeAll(notifDigitalId);
        List<TLCertificate> publishedCertificateDeleted = new ArrayList<>();
        for (TLDigitalIdentification digitalId : notifDigitalId) {
            publishedCertificateDeleted.addAll(digitalId.getCertificateList());
        }
        return publishedCertificateDeleted;
    }

    /**
     * Retrieve contact deleted from notification XML file
     *
     * @param id
     */
    public List<String> getDeleteContact(int id) {
        DBNotification dbNotif = notificationRepository.findOne(id);
        File file = fileService.getTSLFile(dbNotif.getNotificationFile());
        try {
            MemberStateNotificationV5 msNotification = jaxbService.unmarshallNotification(new FileInputStream(file));
            return msNotification.getContactDeleted();
        } catch (Exception e) {
            LOGGER.error("Get Delete Contact notification" + e);
            return new ArrayList<>();
        }
    }

    /**
     * Perform conformance checks on notification
     *
     * @param mpPointer
     */
    public List<CheckDTO> getNotificationCheck(TLPointersToOtherTSL mpPointer) {
        List<CheckResultDTO> checkResults = rulesRunner.validateNotification(mpPointer);
        List<CheckDTO> checks = new ArrayList<>();

        for (CheckResultDTO result : checkResults) {
            if (!result.getStatus().equals(CheckStatus.SUCCESS)) {
                result.setLocation(LocationUtils.formatNotificationPointerId(mpPointer, result.getId()));
                checks.add(new CheckDTO(result));
            }
        }

        Collections.sort(checks, new TLChecksComparatorUtils());
        return checks;
    }

    /* ----- ----- Notification Identifier ----- ----- */

    /**
     * Init notification draft identifier as an trusted list identifier
     *
     * @param notification
     */
    public NotificationDto initNotificationDraftId(NotificationDto notification) {
        if (notification.getPointer().getMpPointer() != null) {
            initNotificationPointerId(notification.getPointer().getMpPointer());
        }
        if (notification.getPointer().getHrPointer() != null) {
            notification.getPointer().getHrPointer().setId("1");
            initNotificationPointerId(notification.getPointer().getHrPointer());
        }
        return notification;
    }

    /**
     * Init notification pointer id as an trusted list identifier (required for integration)
     *
     * @param pointer
     */
    private void initNotificationPointerId(TLPointersToOtherTSL pointer) {
        final String prefix = pointer.getId();
        if (CollectionUtils.isEmpty(pointer.getSchemeOpeName())) {
            for (TLName name : pointer.getSchemeOpeName()) {
                name.setId(prefix + name.getId());
            }
        }
        if (CollectionUtils.isEmpty(pointer.getSchemeTypeCommunity())) {
            for (TLSchemeTypeCommunityRule community : pointer.getSchemeTypeCommunity()) {
                community.setId(prefix + community.getId());
            }
        }
        if (CollectionUtils.isEmpty(pointer.getServiceDigitalId())) {
            for (TLDigitalIdentification digital : pointer.getServiceDigitalId()) {
                digital.setId(prefix + digital.getId());
                if (CollectionUtils.isEmpty(digital.getCertificateList())) {
                    for (TLCertificate certificate : digital.getCertificateList()) {
                        certificate.setId(prefix + certificate.getId());
                    }
                }
            }
        }
    }

    /**
     * Get pointer ID of the given @territory LOTL pointer
     *
     * @param mimeType
     * @param territory
     */
    public String getNotificationId(MimeType mimeType, String territory) {
        TL tl = tlService.getPublishedTLByCountry(countryService.getLOTLCountry());
        if (tl == null) {
            return null;
        }
        String id = tl.getId() + "_" + Tag.POINTERS_TO_OTHER_TSL + "_" + (tl.getPointers().size() + 1);
        for (TLPointersToOtherTSL pointer : tl.getPointers()) {
            if (pointer.getMimeType().equals(mimeType) && pointer.getSchemeTerritory().equalsIgnoreCase(territory)) {
                id = pointer.getId();
            }
        }
        return id;
    }

    /**
     * Parse trusted list find by @tlId and return pointer ID equal to the territory of given @pointer
     *
     * @param pointer
     * @param tlId
     */
    public String getPointerIdInDraft(TLPointersToOtherTSL pointer, int tlId) {
        TL tl = tlService.getTL(tlId);
        for (TLPointersToOtherTSL tlPointer : tl.getPointers()) {
            if (tlPointer.getSchemeTerritory().equals(pointer.getSchemeTerritory()) && tlPointer.getMimeType().equals(pointer.getMimeType())) {
                return tlPointer.getId();
            }
        }
        return "";
    }

    /* ----- ----- Get Notification draft ----- ----- */

    /**
     * Get notification draft list
     */
    public List<NotificationDto> getDraftList() {
        List<NotificationDto> notifiList = new ArrayList<>();
        List<DBNotification> dbNotifList = notificationRepository.findByArchiveFalseOrderByInsertDateDesc();

        for (DBNotification dbNotif : dbNotifList) {
            NotificationDto dtoNotif = null;
            if (dbNotif.getStatus().equals(NotificationStatus.VALIDATED)) {
                dtoNotif = getDraft(dbNotif);
            } else {
                dtoNotif = new NotificationDto(dbNotif);
            }

            // Add if initialized
            if (dtoNotif != null) {
                dtoNotif.setPDFFilled(dbNotif.getReportFile() != null);
                dtoNotif.setUsersChange(dbNotif.isUserChange());
                notifiList.add(dtoNotif);
            }
        }
        return notifiList;
    }

    /**
     * Get notification draft by @notificationId as notification dto format
     */
    public NotificationDto getNotificationDraft(int notificationId) {
        DBNotification dbNotif = notificationRepository.findOne(notificationId);
        NotificationDto dtoNotif = getDraft(dbNotif);
        dtoNotif.setPDFFilled(dbNotif.getReportFile() != null);
        return dtoNotif;
    }

    /**
     * Get notification with authenticated user draft by @notificationId as notification dto format
     */
    public NotificationDto getFullNotificationDraft(int id) {
        NotificationDto notification = getNotificationDraft(id);
        notification.setUsers(new ArrayList<User>());
        List<DBUser> dbAthUsers = userService.findAuthenticatedUserByTerritory(notification.getCountryCode());
        for (DBUser athUser : dbAthUsers) {
            notification.getUsers().add(new User(athUser));
        }
        return notification;
    }

    /**
     * Get draft notification PDF file by @notificationId
     *
     * @param notificationId
     */
    public File getDraftPdf(int notificationId) {
        DBNotification tldb = notificationRepository.findOne(notificationId);
        DBFiles dbf = tldb.getReportFile();
        File xml = fileService.getTSLFile(dbf);
        if (xml != null) {
            return xml;
        }
        return null;
    }

    /**
     * Get draft notification XML file by @notificationId
     *
     * @param notificationId
     */
    public File getNotificationXmlFile(int notificationId) {
        DBNotification notifDb = notificationRepository.findOne(notificationId);
        DBFiles dbf = notifDb.getNotificationFile();
        File xml = fileService.getTSLFile(dbf);
        if (xml != null) {
            return xml;
        }
        return null;
    }

    /**
     * Init Notification DTO from @DBNotification
     *
     * @param dbNotif
     */
    private NotificationDto getDraft(DBNotification dbNotif) {
        NotificationDto rt = new NotificationDto();

        File file = fileService.getTSLFile(dbNotif.getNotificationFile());
        try {
            MemberStateNotificationV5 msNotification = jaxbService.unmarshallNotification(new FileInputStream(file));
            rt = new NotificationDto(dbNotif, msNotification);
            if (rt.getPointer() != null) {
                if (rt.getPointer().getMpPointer() != null) {
                    rt.getPointer().getMpPointer().setId(getNotificationId(rt.getPointer().getMpPointer().getMimeType(), rt.getPointer().getMpPointer().getSchemeTerritory()));
                }
                if (rt.getPointer().getHrPointer() != null) {
                    rt.getPointer().getHrPointer().setId(getNotificationId(rt.getPointer().getHrPointer().getMimeType(), rt.getPointer().getHrPointer().getSchemeTerritory()));
                }
            }
        } catch (Exception e) {
            LOGGER.error("Get Draft Notification" + e);
        }
        return rt;
    }

    /* ----- ----- Notification draft management ----- ----- */

    /**
     * Create notification database entry based on xml/pdf byteArray
     *
     * @param xmlByteArray
     * @param pdfByteArray
     * @param cookieId
     * @throws XmlMappingException
     */
    public DBNotification persistNotification(byte[] xmlByteArray, byte[] pdfByteArray, String cookieId) throws XmlMappingException {

        DBNotification dbNotif = new DBNotification();
        try {
            MemberStateNotificationV5 msNotification = jaxbService.unmarshallNotification(xmlByteArray);
            msNotification.getPointersToOtherTSL().get(0);
            TLPointersToOtherTSL pointer = new TLPointersToOtherTSL(0, "", msNotification.getPointersToOtherTSL().get(0));

            if (xmlByteArray != null) {
                DBFiles xmlFile = new DBFiles();
                xmlFile.setMimeTypeFile(MimeType.XML);
                xmlFile.setDigest(TLUtils.getSHA2(xmlByteArray));
                String draftXmlPath = fileService.storeNewDraftNotification(MimeType.XML, xmlByteArray, pointer.getSchemeTerritory(), null);
                xmlFile.setLocalPath(draftXmlPath);
                dbNotif.setNotificationFile(xmlFile);
            }

            if (pdfByteArray != null) {
                DBFiles pdfFile = new DBFiles();
                pdfFile.setMimeTypeFile(MimeType.PDF);
                pdfFile.setDigest(TLUtils.getSHA2(pdfByteArray));
                String reportXmlPath = fileService.storeNewDraftNotification(MimeType.PDF, pdfByteArray, pointer.getSchemeTerritory(), null);
                pdfFile.setLocalPath(reportXmlPath);
                dbNotif.setReportFile(pdfFile);
            }

            DBCountries country = countryService.getCountryByTerritory(pointer.getSchemeTerritory());

            dbNotif.setDraftStoreId(cookieId);

            dbNotif.setTerritory(country);
            dbNotif.setEffectDate(TLUtils.toDate(msNotification.getEffectDate()));
            dbNotif.setArchive(false);
            dbNotif.setInsertDate(new Date());
            dbNotif.setSubmissionDate(TLUtils.toDate(msNotification.getSubmissionDate()));
            dbNotif.setStatus(NotificationStatus.RECEIVED);
            dbNotif.setIdentifier(msNotification.getNotificationIdentifier());

            notificationRepository.save(dbNotif);

        } catch (Exception e) {
            LOGGER.info("createDraftFromBinaries error : " + e.getMessage(), e);

        }

        return dbNotif;
    }

    /**
     * Update notification status to "IN_DRAFT" by @notificationId and trusted list @draftId
     *
     * @param notificationId
     * @param draftId
     */
    public void putInDraft(int notificationId, int draftId) {
        DBNotification notif = notificationRepository.findOne(notificationId);
        DBTrustedLists tl = tlService.getDbTL(draftId);
        notif.getTls().add(tl); // KO
        notif.setStatus(NotificationStatus.INDRAFT);
    }

    /**
     * Delete draft notification by @notificationId
     *
     * @param notificationId
     */
    public void deleteNotification(int notificationId) {
        DBNotification notification = notificationRepository.findOne(notificationId);
        if (notification.getNotificationFile() != null) {
            fileService.delete(notification.getNotificationFile());
        }
        if (notification.getReportFile() != null) {
            fileService.delete(notification.getReportFile());
        }
        notificationRepository.delete(notificationId);
    }

    /**
     * Update notification status by @DBNotification
     *  @param notif
     * @param status
     */
    public void updateStatus(DBNotification notif, NotificationStatus status) {
        notif.setStatus(status);
        notificationRepository.save(notif);
    }

    /**
     * Persist DBNotification
     *
     * @param notification
     */
    public void saveNotification(DBNotification notification) {
        notificationRepository.save(notification);
    }

}
