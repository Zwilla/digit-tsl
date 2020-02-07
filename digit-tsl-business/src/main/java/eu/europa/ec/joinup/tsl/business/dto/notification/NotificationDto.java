package eu.europa.ec.joinup.tsl.business.dto.notification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eu.europa.ec.joinup.tsl.business.dto.NotificationPointers;
import eu.europa.ec.joinup.tsl.business.dto.User;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.ec.joinup.tsl.model.DBNotification;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.MimeType;
import eu.europa.ec.joinup.tsl.model.enums.NotificationStatus;
import eu.europa.ec.joinup.tsl.model.enums.Tag;
import eu.europa.esig.jaxb.v5.tsl.OtherTSLPointerTypeV5;

public class NotificationDto {

    private int id;
    private String identifier;
    private String countryCode;
    private String countryName;
    private NotificationStatus status;
    private boolean contactChange;
    private boolean mpPointerDeleted;
    private boolean hrPointerDeleted;
    private NotificationPointers pointer;
    private List<String> tlName;
    private List<String> storeId;
    private Date submissionDate;
    private Date effectDate;
    // Show/Hide PDF button in view
    private boolean isPDFFilled;
    private List<User> users;
    private boolean usersChange;

    public NotificationDto() {
        users = new ArrayList<>();
    }

    public NotificationDto(DBNotification dbNotif, MemberStateNotificationV5 msNotifV5) {
        setId(dbNotif.getId());
        setIdentifier(dbNotif.getIdentifier());
        setCountryCode(dbNotif.getTerritory().getCodeTerritory());
        setCountryName(dbNotif.getTerritory().getCountryName());
        setStatus(dbNotif.getStatus());
        setContactChange(dbNotif.isContactChange());
        setUsersChange(dbNotif.isUserChange());
        setMpPointerDeleted(dbNotif.isMpPointerDeleted());
        setHrPointerDeleted(dbNotif.isHrPointerDeleted());
        setSubmissionDate(dbNotif.getSubmissionDate());
        setEffectDate(dbNotif.getEffectDate());

        List<String> tmpList = new ArrayList<>();
        List<String> tmpStoreList = new ArrayList<>();
        for (DBTrustedLists dbTl : dbNotif.getTls()) {
            tmpList.add(dbTl.getName());
            tmpStoreList.add(dbTl.getDraftStoreId());
        }
        setTlName(tmpList);
        setStoreId(tmpStoreList);

        NotificationPointers notifpointers = new NotificationPointers();
        if (msNotifV5.getSchemeContact() != null) {
            notifpointers.setTlsoContact(msNotifV5.getSchemeContact());
        }

        for (OtherTSLPointerTypeV5 otherPointer : msNotifV5.getPointersToOtherTSL()) {
            TLPointersToOtherTSL pointer = new TLPointersToOtherTSL(1, "1_" + Tag.POINTERS_TO_OTHER_TSL + "_0", otherPointer);
            if ((pointer.getMimeType() != null) && pointer.getMimeType().equals(MimeType.XML)) {
                pointer.setId("0");
                notifpointers.setMpPointer(pointer);
            } else {
                pointer.setId("0");
                notifpointers.setHrPointer(pointer);
            }
        }

        notifpointers.setDateOfEffect(TLUtils.toDate(msNotifV5.getEffectDate()));
        notifpointers.setDateOfSubmission(TLUtils.toDate(msNotifV5.getSubmissionDate()));

        setPointer(notifpointers);

    }

    public NotificationDto(DBNotification dbNotif) {
        setId(dbNotif.getId());
        setIdentifier(dbNotif.getIdentifier());
        setCountryCode(dbNotif.getTerritory().getCodeTerritory());
        setCountryName(dbNotif.getTerritory().getCountryName());
        setStatus(dbNotif.getStatus());
        setContactChange(dbNotif.isContactChange());
        setMpPointerDeleted(dbNotif.isMpPointerDeleted());
        setHrPointerDeleted(dbNotif.isHrPointerDeleted());
        setSubmissionDate(dbNotif.getSubmissionDate());
        setEffectDate(dbNotif.getEffectDate());

        List<String> tmpList = new ArrayList<>();
        List<String> tmpStoreList = new ArrayList<>();
        for (DBTrustedLists dbTl : dbNotif.getTls()) {
            tmpList.add(dbTl.getName());
            tmpStoreList.add(dbTl.getDraftStoreId());
        }
        setTlName(tmpList);
        setStoreId(tmpStoreList);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public boolean isContactChange() {
        return contactChange;
    }

    public void setContactChange(boolean contactChange) {
        this.contactChange = contactChange;
    }

    public NotificationPointers getPointer() {
        return pointer;
    }

    public void setPointer(NotificationPointers pointer) {
        this.pointer = pointer;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public List<String> getTlName() {
        return tlName;
    }

    public void setTlName(List<String> tlName) {
        this.tlName = tlName;
    }

    public boolean isMpPointerDeleted() {
        return mpPointerDeleted;
    }

    public void setMpPointerDeleted(boolean mpPointerDeleted) {
        this.mpPointerDeleted = mpPointerDeleted;
    }

    public boolean isHrPointerDeleted() {
        return hrPointerDeleted;
    }

    public void setHrPointerDeleted(boolean hrPointerDeleted) {
        this.hrPointerDeleted = hrPointerDeleted;
    }

    public List<String> getStoreId() {
        return storeId;
    }

    public void setStoreId(List<String> storeId) {
        this.storeId = storeId;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public Date getEffectDate() {
        return effectDate;
    }

    public void setEffectDate(Date effectDate) {
        this.effectDate = effectDate;
    }

    public boolean isPDFFilled() {
        return isPDFFilled;
    }

    public void setPDFFilled(boolean isPDFFilled) {
        this.isPDFFilled = isPDFFilled;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public boolean isUsersChange() {
        return usersChange;
    }

    public void setUsersChange(boolean usersChange) {
        this.usersChange = usersChange;
    }

}
