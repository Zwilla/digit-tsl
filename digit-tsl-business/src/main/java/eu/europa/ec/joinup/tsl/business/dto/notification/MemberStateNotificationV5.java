package eu.europa.ec.joinup.tsl.business.dto.notification;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import eu.europa.ec.joinup.tsl.business.dto.User;
import eu.europa.ec.joinup.tsl.business.dto.contact.ContactList;
import eu.europa.esig.jaxb.v5.tsl.OtherTSLPointerTypeV5;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "versionIdentifier", "toolIdentifier", "notificationIdentifier", "schemeContact", "pointersToOtherTSL", "submissionDate", "effectDate", "contactDeleted", "users" })
@XmlRootElement(name = "MemberStateNotification")
public class MemberStateNotificationV5 {

    @XmlElement(name = "VersionIdentifier")
    protected int versionIdentifier;
    @XmlElement(name = "ToolIdentifier", required = true)
    protected String toolIdentifier;
    @XmlElement(name = "NotificationIdentifier", required = true)
    protected String notificationIdentifier;
    @XmlElement(name = "SchemeContact", required = true)
    protected ContactList.TLSOContact schemeContact;
    @XmlElement(name = "PointersToOtherTSL", required = true)
    protected List<OtherTSLPointerTypeV5> pointersToOtherTSL;
    @XmlElement(name = "SubmissionDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar submissionDate;
    @XmlElement(name = "EffectDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar effectDate;
    @XmlElement(name = "ContactDeleted", required = true)
    protected List<String> contactDeleted;
    @XmlElement(name = "users", required = true)
    protected List<User> users;

    /**
     * Obtient la valeur de la propri�t� versionIdentifier.
     *
     */
    public int getVersionIdentifier() {
        return versionIdentifier;
    }

    /**
     * D�finit la valeur de la propri�t� versionIdentifier.
     *
     */
    public void setVersionIdentifier(int value) {
        this.versionIdentifier = value;
    }

    /**
     * Obtient la valeur de la propri�t� toolIdentifier.
     *
     * @return possible object is {@link String }
     *
     */
    public String getToolIdentifier() {
        return toolIdentifier;
    }

    /**
     * D�finit la valeur de la propri�t� toolIdentifier.
     *
     * @param value
     *            allowed object is {@link String }
     *
     */
    public void setToolIdentifier(String value) {
        this.toolIdentifier = value;
    }

    /**
     * Obtient la valeur de la propri�t� notificationIdentifier.
     *
     * @return possible object is {@link String }
     *
     */
    public String getNotificationIdentifier() {
        return notificationIdentifier;
    }

    /**
     * D�finit la valeur de la propri�t� notificationIdentifier.
     *
     * @param value
     *            allowed object is {@link String }
     *
     */
    public void setNotificationIdentifier(String value) {
        this.notificationIdentifier = value;
    }

    /**
     * Obtient la valeur de la propri�t� submissionDate.
     *
     * @return possible object is {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getSubmissionDate() {
        return submissionDate;
    }

    /**
     * D�finit la valeur de la propri�t� submissionDate.
     *
     * @param value
     *            allowed object is {@link XMLGregorianCalendar }
     *
     */
    public void setSubmissionDate(XMLGregorianCalendar value) {
        this.submissionDate = value;
    }

    /**
     * Obtient la valeur de la propri�t� effectDate.
     *
     * @return possible object is {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getEffectDate() {
        return effectDate;
    }

    /**
     * D�finit la valeur de la propri�t� effectDate.
     *
     * @param value
     *            allowed object is {@link XMLGregorianCalendar }
     *
     */
    public void setEffectDate(XMLGregorianCalendar value) {
        this.effectDate = value;
    }

    public ContactList.TLSOContact getSchemeContact() {
        return schemeContact;
    }

    public void setSchemeContact(ContactList.TLSOContact schemeContact) {
        this.schemeContact = schemeContact;
    }

    public List<OtherTSLPointerTypeV5> getPointersToOtherTSL() {
        return pointersToOtherTSL;
    }

    public void setPointersToOtherTSL(List<OtherTSLPointerTypeV5> pointersToOtherTSL) {
        this.pointersToOtherTSL = pointersToOtherTSL;
    }

    public List<String> getContactDeleted() {
        return contactDeleted;
    }

    public void setContactDeleted(List<String> contactDeleted) {
        this.contactDeleted = contactDeleted;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
