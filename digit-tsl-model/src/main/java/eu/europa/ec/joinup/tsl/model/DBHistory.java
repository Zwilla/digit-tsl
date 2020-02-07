package eu.europa.ec.joinup.tsl.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TL_HISTORY")
public class DBHistory {

    @Id
    @Column(name = "ID", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "HISTORY_ID", unique = true, nullable = false, length = 65)
    private String historyId;

    @Column(name = "COUNTRY_CODE", nullable = false, length = 2)
    private String countryCode;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "TL_HISTORY_NAME", joinColumns = @JoinColumn(name = "HISTORY_ID"))
    @Column(name = "HISTORY_NAME")
    private Set<String> historyNames;

    @Column(name = "TYPE", nullable = false, length = 100)
    private String type;

    @Column(name = "STATUS", nullable = false, length = 100)
    private String status;

    @Column(name = "STARTING_DATE", nullable = false)
    private Date startingDate;

    @Column(name = "TAKEN_OVER_BY")
    private String takenOverBy;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "TL_HISTORY_TYPE", joinColumns = @JoinColumn(name = "HISTORY_ID"))
    @Column(name = "HISTORY_TYPE")
    private Set<String> qHistoryTypes;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "SERVICE_ID", nullable = false, updatable = false)
    private DBService trustService;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Set<String> getHistoryNames() {
        return historyNames;
    }

    public void setHistoryNames(Set<String> historyNames) {
        this.historyNames = historyNames;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(Date startingDate) {
        this.startingDate = startingDate;
    }

    public String getTakenOverBy() {
        return takenOverBy;
    }

    public void setTakenOverBy(String takenOverBy) {
        this.takenOverBy = takenOverBy;
    }

    public Set<String> getQHistoryTypes() {
        return qHistoryTypes;
    }

    public void setQHistoryTypes(Set<String> qHistoryTypes) {
        this.qHistoryTypes = qHistoryTypes;
    }

    public DBService getTrustService() {
        return trustService;
    }

    public void setTrustService(DBService trustService) {
        this.trustService = trustService;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((historyId == null) ? 0 : historyId.hashCode());
        result = (prime * result) + id;
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
        DBHistory other = (DBHistory) obj;
        if (historyId == null) {
            if (other.historyId != null) {
                return false;
            }
        } else if (!historyId.equals(other.historyId)) {
            return false;
        }
        if (id != other.id) {
            return false;
        }
        return true;
    }

}
