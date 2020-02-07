package eu.europa.ec.joinup.tsl.business.dto;

import java.util.Date;

public class DraftStore {

    private String draftStoreId;
    private Date lastVerification;

    public DraftStore() {
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

}
