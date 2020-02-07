package eu.europa.ec.joinup.tsl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import eu.europa.ec.joinup.tsl.model.enums.CheckStatus;

import java.util.Date;

@Entity
@Table(name = "TL_RESULTS")
public class DBCheckResult {

  @Id
  @GeneratedValue
  @Column(name = "RESULT_ID", nullable = false, updatable = false)
  private int id;

  @Column(name = "LOCATION", nullable = false)
  private String location;

  @Column(name = "HUMAN_READABLE_LOCATION", nullable = false, length = 500)
  private String hrLocation;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "CHECK_ID", nullable = false)
  private DBCheck check;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "TL_ID", nullable = false)
  private DBTrustedLists trustedList;

  @Column(name = "STATUS", nullable = false)
  @Enumerated(EnumType.STRING)
  private CheckStatus status;

  @Column(name = "DESCRIPTION", length = 5000)
  private String description;

  public DBCheckResult() {}

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getHrLocation() {
    return hrLocation;
  }

  public void setHrLocation(String hrLocation) {
    this.hrLocation = hrLocation;
  }

  public DBCheck getCheck() {
    return check;
  }

  public void setCheck(DBCheck check) {
    this.check = check;
  }

  public DBTrustedLists getTrustedList() {
    return trustedList;
  }

  public void setTrustedList(DBTrustedLists trustedList) {
    this.trustedList = trustedList;
  }

  public CheckStatus getStatus() {
    return status;
  }

  public void setStatus(CheckStatus status) {
    this.status = status;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    result = prime * result + ((location == null) ? 0 : location.hashCode());
    result = prime * result + ((status == null) ? 0 : status.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    DBCheckResult other = (DBCheckResult) obj;
    if (id != other.id) return false;
    if (location == null) {
      if (other.location != null) return false;
    } else if (!location.equals(other.location)) return false;
      return status == other.status;
  }

  public void setStartDate(Date startDate) {
    StartDate = new Date();
  }

  Date StartDate = new Date();

  public Date getStartDate() {
    return StartDate;
  }

  Date Since;

  public void setSince(long since) {
    Since = new Date();
  }

  Date EndDate;

  public void setEndDate(Date now) {
    EndDate = now;
  }
}
