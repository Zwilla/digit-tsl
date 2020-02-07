package eu.europa.ec.joinup.tsl.business.dto;

import java.util.Date;

import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.AvailabilityStatus;
import eu.europa.ec.joinup.tsl.model.enums.SignatureStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;

public class TLLightReport {

    private int id;
    private String name;
    private String territoryCode;
    private String countryName;
    private int sequenceNumber;
    private Date issueDate;
    private Date nextUpdateDate;
    private TLType tlType;
    private SignatureStatus signature;
    private AvailabilityStatus availability;
    private Date firstScanDate;

    public TLLightReport(DBTrustedLists dbTL, AvailabilityStatus availability, SignatureStatus signature, Date firstScanDate) {
        super();
        id = dbTL.getId();
        name = dbTL.getName();
        territoryCode = dbTL.getTerritory().getCodeTerritory();
        countryName = dbTL.getTerritory().getCountryName();
        sequenceNumber = dbTL.getSequenceNumber();
        issueDate = dbTL.getIssueDate();
        nextUpdateDate = dbTL.getNextUpdateDate();
        tlType = dbTL.getType();
        this.availability = availability;
        this.signature = signature;
        this.firstScanDate = firstScanDate;
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

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getNextUpdateDate() {
        return nextUpdateDate;
    }

    public void setNextUpdateDate(Date nextUpdateDate) {
        this.nextUpdateDate = nextUpdateDate;
    }

    public TLType getTlType() {
        return tlType;
    }

    public void setTlType(TLType tlType) {
        this.tlType = tlType;
    }

    public AvailabilityStatus getAvailability() {
        return availability;
    }

    public void setAvailability(AvailabilityStatus availability) {
        this.availability = availability;
    }

    public SignatureStatus getSignature() {
        return signature;
    }

    public void setSignature(SignatureStatus signature) {
        this.signature = signature;
    }

    public Date getFirstScanDate() {
        return firstScanDate;
    }

    public void setFirstScanDate(Date firstScanDate) {
        this.firstScanDate = firstScanDate;
    }

}
