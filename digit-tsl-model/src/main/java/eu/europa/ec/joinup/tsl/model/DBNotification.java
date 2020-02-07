package eu.europa.ec.joinup.tsl.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import eu.europa.ec.joinup.tsl.model.enums.CheckStatus;
import eu.europa.ec.joinup.tsl.model.enums.NotificationStatus;

@Entity
@Table(name = "TL_NOTIFICATIONS")
public class DBNotification {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "IDENTIFIER")
    private String identifier;

    @ManyToOne
    @JoinColumn(name = "TERRITORY")
    private DBCountries territory;

    @OneToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "NOTIFICATION_FILE_ID")
    private DBFiles notificationFile;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "REPORT_FILE_ID")
    private DBFiles reportFile;

    @Column(name = "NOTIFICATION_STATUS")
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    @Column(name = "INSERT_DATE")
    private Date insertDate;

    @Column(name = "SUBMISSION_DATE")
    private Date submissionDate;

    @Column(name = "EFFECT_DATE")
    private Date effectDate;

    @Column(name = "ARCHIVE")
    private boolean archive = false;

    @Column(name = "CONTACT_CHANGE")
    private boolean contactChange = false;

    @Column(name = "USER_CHANGE")
    private boolean userChange = false;

    @Column(name = "MP_POINTER_DELETED")
    private boolean mpPointerDeleted = false;

    @Column(name = "HR_POINTER_DELETED")
    private boolean hrPointerDeleted = false;

    @Column(name = "CHECK_STATUS")
    @Enumerated(EnumType.STRING)
    private CheckStatus checkStatus;

    @Column(name = "OWNER_ID")
    private String draftStoreId;

    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(name = "TL_TRUSTEDLIST_NOTIFICATION", joinColumns = { @JoinColumn(name = "NOTIFICATION_ID") }, inverseJoinColumns = { @JoinColumn(name = "TL_ID") })
    private List<DBTrustedLists> tls;

    public DBNotification() {
    }

    @Override
    public String toString() {
        return String.format("TrustedListsDB[id=%d; Name = '%s', Status = '%s']", getId(), getIdentifier(), getStatus().name());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public DBCountries getTerritory() {
        return territory;
    }

    public void setTerritory(DBCountries territory) {
        this.territory = territory;
    }

    public DBFiles getNotificationFile() {
        return notificationFile;
    }

    public void setNotificationFile(DBFiles notificationFile) {
        this.notificationFile = notificationFile;
    }

    public DBFiles getReportFile() {
        return reportFile;
    }

    public void setReportFile(DBFiles reportFile) {
        this.reportFile = reportFile;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
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

    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    public CheckStatus getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(CheckStatus checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getDraftStoreId() {
        return draftStoreId;
    }

    public void setDraftStoreId(String draftStoreId) {
        this.draftStoreId = draftStoreId;
    }

    public boolean isContactChange() {
        return contactChange;
    }

    public void setContactChange(boolean contactChange) {
        this.contactChange = contactChange;
    }

    public List<DBTrustedLists> getTls() {
        return tls;
    }

    public void setTls(List<DBTrustedLists> tls) {
        this.tls = tls;
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

    public boolean isUserChange() {
        return userChange;
    }

    public void setUserChange(boolean userChange) {
        this.userChange = userChange;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + id;
        result = (prime * result) + ((identifier == null) ? 0 : identifier.hashCode());
        result = (prime * result) + ((territory == null) ? 0 : territory.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DBNotification other = (DBNotification) obj;
        if (id != other.id) {
            return false;
        }
        if (identifier == null) {
            if (other.identifier != null) {
                return false;
            }
        } else if (!identifier.equals(other.identifier)) {
            return false;
        }
        if (territory == null) {
            if (other.territory != null) {
                return false;
            }
        } else if (!territory.equals(other.territory)) {
            return false;
        }
        return true;
    }

}
