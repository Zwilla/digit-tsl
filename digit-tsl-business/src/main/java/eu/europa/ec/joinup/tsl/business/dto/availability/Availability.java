package eu.europa.ec.joinup.tsl.business.dto.availability;

import java.util.Date;

import eu.europa.ec.joinup.tsl.model.enums.AvailabilityStatus;

public class Availability {

    private AvailabilityStatus status;
    private int statusNumber;
    private Date checkDate;

    public Availability() {

    }

    public Availability(AvailabilityStatus status) {
        this.status = status;
        if (status.equals(AvailabilityStatus.AVAILABLE)) {
            statusNumber = 1;
        } else {
            statusNumber = 0;
        }
    }

    public AvailabilityStatus getStatus() {
        return status;
    }

    public void setStatus(AvailabilityStatus status) {
        this.status = status;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public int getStatusNumber() {
        return statusNumber;
    }

    public void setStatusNumber(int statusNumber) {
        this.statusNumber = statusNumber;
    }

}
