package eu.europa.ec.joinup.tsl.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import eu.europa.ec.joinup.tsl.model.enums.CheckStatus;

@MappedSuperclass
public class AbstractCheckResult {

    @Id
    @GeneratedValue
    @Column(name = "RESULT_ID", nullable = false, updatable = false)
    private int id;

    @Column(name = "LOCATION", nullable = false)
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TL_ID", nullable = false)
    private DBTrustedLists trustedList;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private CheckStatus status;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "SINCE")
    private Long since;

    public AbstractCheckResult() {
        super();
    }

    public CheckStatus getStatus() {
        return status;
    }

    public void setStatus(CheckStatus status) {
        this.status = status;
    }

    public DBTrustedLists getTrustedList() {
        return trustedList;
    }

    public void setTrustedList(DBTrustedLists trustedList) {
        this.trustedList = trustedList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getSince() {
        return since;
    }

    public void setSince(Long since) {
        this.since = since;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((endDate == null) ? 0 : endDate.hashCode());
        result = (prime * result) + id;
        result = (prime * result) + ((location == null) ? 0 : location.hashCode());
        result = (prime * result) + ((since == null) ? 0 : since.hashCode());
        result = (prime * result) + ((startDate == null) ? 0 : startDate.hashCode());
        result = (prime * result) + ((status == null) ? 0 : status.hashCode());
        result = (prime * result) + ((trustedList == null) ? 0 : trustedList.hashCode());
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
        AbstractCheckResult other = (AbstractCheckResult) obj;
        if (endDate == null) {
            if (other.endDate != null) {
                return false;
            }
        } else if (!endDate.equals(other.endDate)) {
            return false;
        }
        if (id != other.id) {
            return false;
        }
        if (location == null) {
            if (other.location != null) {
                return false;
            }
        } else if (!location.equals(other.location)) {
            return false;
        }
        if (since == null) {
            if (other.since != null) {
                return false;
            }
        } else if (!since.equals(other.since)) {
            return false;
        }
        if (startDate == null) {
            if (other.startDate != null) {
                return false;
            }
        } else if (!startDate.equals(other.startDate)) {
            return false;
        }
        if (status != other.status) {
            return false;
        }
        if (trustedList == null) {
            if (other.trustedList != null) {
                return false;
            }
        } else if (!trustedList.equals(other.trustedList)) {
            return false;
        }
        return true;
    }

}
