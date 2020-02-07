package eu.europa.ec.joinup.tsl.business.dto;

import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.CheckStatus;

public class TrustBackbonePref {

    private int id;
    private String name;
    private String territoryCode;
    private int sequenceNumber;
    private CheckStatus tbStatus;

    public TrustBackbonePref() {
    }

    public TrustBackbonePref(DBTrustedLists tl) {
        this.setId(tl.getId());
        this.setName(tl.getName());
        this.setTerritoryCode(tl.getTerritory().getCodeTerritory());
        this.setSequenceNumber(tl.getSequenceNumber());
    }

    @Override
    public String toString() {
        return String.format("TrustBackbonePref[id=%d; Name = '%s', Territory = '%s']", getId(), getName(), getTerritoryCode());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public CheckStatus getTbStatus() {
        return tbStatus;
    }

    public void setTbStatus(CheckStatus tbStatus) {
        this.tbStatus = tbStatus;
    }

}
