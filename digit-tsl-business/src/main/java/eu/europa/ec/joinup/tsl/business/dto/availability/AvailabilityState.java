package eu.europa.ec.joinup.tsl.business.dto.availability;

import java.util.Date;

import eu.europa.ec.joinup.tsl.model.enums.AvailabilityStatus;

public class AvailabilityState {

    private AvailabilityStatus status;
    private Date startDate;
    private Date endDate;

    public AvailabilityState() {

    }

    public AvailabilityStatus getStatus() {
        return status;
    }

    public void setStatus(AvailabilityStatus status) {
        this.status = status;
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

    public long getTiming() {
        if ((endDate != null) && (startDate.getTime() < endDate.getTime())) {
            return endDate.getTime() - startDate.getTime();
        }
        return new Date().getTime() - startDate.getTime();
    }

}
