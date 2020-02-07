package eu.europa.ec.joinup.tsl.business.dto.availability;

import eu.europa.ec.joinup.tsl.model.DBTrustedLists;

public class UnavailabilityReportEntry {

    private DBTrustedLists trustedList;

    private AvailabilityHistory unavailabilityStories;

    public UnavailabilityReportEntry() {
        super();
    }

    public UnavailabilityReportEntry(DBTrustedLists trustedList, AvailabilityHistory unavailabilityStories) {
        super();
        this.trustedList = trustedList;
        this.unavailabilityStories = unavailabilityStories;
    }

    public DBTrustedLists getTrustedList() {
        return trustedList;
    }

    public void setTrustedList(DBTrustedLists trustedList) {
        this.trustedList = trustedList;
    }

    public AvailabilityHistory getUnavailabilityStories() {
        return unavailabilityStories;
    }

    public void setUnavailabilityStories(AvailabilityHistory unavailabilityStories) {
        this.unavailabilityStories = unavailabilityStories;
    }

}
