package eu.europa.ec.joinup.tsl.business.dto.data.retention;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eu.europa.ec.joinup.tsl.model.DBDraftStore;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;

public class DraftStoreRetentionDTO implements Serializable {

    private static final long serialVersionUID = 3963763939421753224L;

    private String draftStoreId;
    private Date lastVerification;
    private List<TrustedListRetentionDTO> tls;

    public DraftStoreRetentionDTO() {
        super();
    }

    public DraftStoreRetentionDTO(DBDraftStore dbDS) {
        super();
        draftStoreId = dbDS.getDraftStoreId();
        lastVerification = dbDS.getLastVerification();
        tls = new ArrayList<>();
        for (DBTrustedLists dbTL : dbDS.getDraftList()) {
            tls.add(new TrustedListRetentionDTO(dbTL));
        }
    }

    public DraftStoreRetentionDTO(RetentionCriteriaDTO criteria, List<DBTrustedLists> dbTLs) {
        draftStoreId = criteria.getTarget().toString();
        lastVerification = criteria.getDate();
        tls = new ArrayList<>();
        for (DBTrustedLists dbTL : dbTLs) {
            tls.add(new TrustedListRetentionDTO(dbTL));
        }
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

    /**
     * Init if list null
     */
    public List<TrustedListRetentionDTO> getTls() {
        if (tls == null) {
            tls = new ArrayList<>();
        }
        return tls;
    }

    public void setTls(List<TrustedListRetentionDTO> tls) {
        this.tls = tls;
    }

}
