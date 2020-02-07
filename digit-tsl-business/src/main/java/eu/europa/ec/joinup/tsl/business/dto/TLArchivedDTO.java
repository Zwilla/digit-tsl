package eu.europa.ec.joinup.tsl.business.dto;

import java.util.Date;

import eu.europa.ec.joinup.tsl.model.DBTrustedLists;

public class TLArchivedDTO {

    private int id;

    private String name;

    private int sequenceNumber;

    private Date issueDate;

    private Date nextUpdate;

    private Date firstScanDate;

    public TLArchivedDTO() {
        super();
    }

    public TLArchivedDTO(DBTrustedLists tl) {
        super();
        this.id = tl.getId();
        this.name = tl.getName();
        this.sequenceNumber = tl.getSequenceNumber();
        this.issueDate = tl.getIssueDate();
        this.nextUpdate = tl.getNextUpdateDate();
        this.firstScanDate = tl.getXmlFile().getFirstScanDate();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Date getNextUpdate() {
        return nextUpdate;
    }

    public void setNextUpdate(Date nextUpdate) {
        this.nextUpdate = nextUpdate;
    }

    public Date getFirstScanDate() {
        return firstScanDate;
    }

    public void setFirstScanDate(Date firstScanDate) {
        this.firstScanDate = firstScanDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
