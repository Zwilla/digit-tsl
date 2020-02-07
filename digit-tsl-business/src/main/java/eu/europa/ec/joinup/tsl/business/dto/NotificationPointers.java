package eu.europa.ec.joinup.tsl.business.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eu.europa.ec.joinup.tsl.business.dto.contact.ContactList.TLSOContact;
import eu.europa.ec.joinup.tsl.business.dto.contact.ContactList.TLSOContact.ElectronicAddress;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.ec.joinup.tsl.model.DBUser;

public class NotificationPointers implements Serializable {

    private static final long serialVersionUID = -3775159015988230085L;
    private TLPointersToOtherTSL hrPointer;
    private TLPointersToOtherTSL mpPointer;
    private Date dateOfSubmission;
    private Date dateOfEffect;
    private List<String> deletedCertificates;
    private List<String> addedCertificates;
    private List<String> currentCertificates;
    private TLSOContact tlsoContact;
    private Date creationDate;
    private List<User> users;
    private TLSignature signatureInformation;

    public NotificationPointers() {
        this.deletedCertificates = new ArrayList<>();
        this.addedCertificates = new ArrayList<>();
        this.currentCertificates = new ArrayList<>();
        this.tlsoContact = new TLSOContact();
    }

    public TLPointersToOtherTSL getHrPointer() {
        return hrPointer;
    }

    public void setHrPointer(TLPointersToOtherTSL hrPointer) {
        this.hrPointer = hrPointer;
    }

    public TLPointersToOtherTSL getMpPointer() {
        return mpPointer;
    }

    public void setMpPointer(TLPointersToOtherTSL mpPointer) {
        this.mpPointer = mpPointer;
    }

    public Date getDateOfSubmission() {
        return dateOfSubmission;
    }

    public void setDateOfSubmission(Date dateOfSubmission) {
        this.dateOfSubmission = dateOfSubmission;
    }

    public Date getDateOfEffect() {
        return dateOfEffect;
    }

    public void setDateOfEffect(Date dateOfEffect) {
        this.dateOfEffect = dateOfEffect;
    }

    public List<String> getDeletedCertificates() {
        return deletedCertificates;
    }

    public void setDeletedCertificates(List<String> deletedCertificates) {
        this.deletedCertificates = deletedCertificates;
    }

    public List<String> getAddedCertificates() {
        return addedCertificates;
    }

    public void setAddedCertificates(List<String> addedCertificates) {
        this.addedCertificates = addedCertificates;
    }

    public List<String> getCurrentCertificates() {
        return currentCertificates;
    }

    public void setCurrentCertificates(List<String> currentCertificates) {
        this.currentCertificates = currentCertificates;
    }

    public TLSOContact getTlsoContact() {
        return tlsoContact;
    }

    public void setTlsoContact(TLSOContact tlsoContact) {
        if (tlsoContact.getName() == null) {
            tlsoContact.setName("");
        }
        if (tlsoContact.getElectronicAddress() == null) {
            tlsoContact.setElectronicAddress(new ElectronicAddress());
        }
        if (tlsoContact.getPhoneNumber() == null) {
            tlsoContact.setPhoneNumber("");
        }
        if (tlsoContact.getPostalAddress() == null) {
            tlsoContact.setPostalAddress("");
        }
        this.tlsoContact = tlsoContact;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public TLSignature getSignatureInformation() {
        return signatureInformation;
    }

    public void setSignatureInformation(TLSignature signatureInformation) {
        this.signatureInformation = signatureInformation;
    }

    public List<User> getUsers() {
        if (users == null) {
            this.users = new ArrayList<>();
        }
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void initUsers(List<DBUser> dbUsers) {
        this.users = new ArrayList<>();
        for (DBUser dbUser : dbUsers) {
            this.users.add(new User(dbUser));
        }
    }

}
