package eu.europa.ec.joinup.tsl.business.dto.data.retention;

import java.util.Date;

import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;

public class TrustedListRetentionDTO {

    private int id;
    private String territoryCode;
    private int sequenceNumber;
    private boolean archive;
    private Date lastAccessDate;
    private String draftStoreId;

    public TrustedListRetentionDTO() {
        super();
    }

    public TrustedListRetentionDTO(DBTrustedLists dbTL) {
        super();
        id = dbTL.getId();
        territoryCode = dbTL.getTerritory().getCodeTerritory();
        sequenceNumber = dbTL.getSequenceNumber();
        archive = dbTL.isArchive();
        if (dbTL.getStatus().equals(TLStatus.DRAFT)) {
            lastAccessDate = dbTL.getLastAccessDate();
        } else if (dbTL.getStatus().equals(TLStatus.PROD)) {
            lastAccessDate = dbTL.getXmlFile().getFirstScanDate();
        }

        draftStoreId = dbTL.getDraftStoreId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTerritoryCode() {
        return territoryCode;
    }

    public void setTerritoryCode(String territoryCode) {
        this.territoryCode = territoryCode;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    public Date getLastAccessDate() {
        return lastAccessDate;
    }

    public void setLastAccessDate(Date lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    public String getDraftStoreId() {
        return draftStoreId;
    }

    public void setDraftStoreId(String draftStoreId) {
        this.draftStoreId = draftStoreId;
    }

}
