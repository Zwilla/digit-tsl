package eu.europa.ec.joinup.tsl.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "TL_DRAFTSTORE")
public class DBDraftStore {

    @Id
    @Column(name = "DRAFTSTORE_ID", unique = true, nullable = false)
    private String draftStoreId;

    @Column(name = "LAST_VERIFICATION")
    private Date lastVerification;

    @OneToMany
    @JoinColumn(name = "OWNER_ID", referencedColumnName = "DRAFTSTORE_ID")
    private List<DBTrustedLists> draftList;

    public DBDraftStore() {
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((draftStoreId == null) ? 0 : draftStoreId.hashCode());
        result = (prime * result) + ((lastVerification == null) ? 0 : lastVerification.hashCode());
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
        DBDraftStore other = (DBDraftStore) obj;
        if (draftStoreId == null) {
            if (other.draftStoreId != null) {
                return false;
            }
        } else if (!draftStoreId.equals(other.draftStoreId)) {
            return false;
        }
        if (lastVerification == null) {
            if (other.lastVerification != null) {
                return false;
            }
        } else if (!lastVerification.equals(other.lastVerification)) {
            return false;
        }
        return true;
    }

    public String getDraftStoreId() {
        return draftStoreId;
    }

    public void setDraftStoreId(String draftStoreId) {
        this.draftStoreId = draftStoreId;
    }

    public Date getLastVerification() {
        return lastVerification;
    }

    public void setLastVerification(Date lastVerification) {
        this.lastVerification = lastVerification;
    }

    public List<DBTrustedLists> getDraftList() {
        if (draftList == null) {
            draftList = new ArrayList<>();
        }
        return draftList;
    }

    public void setDraftList(List<DBTrustedLists> draftList) {
        this.draftList = draftList;
    }

}
