package eu.europa.ec.joinup.tsl.business.dto.availability;

import java.util.ArrayList;
import java.util.List;

import eu.europa.ec.joinup.tsl.model.enums.AvailabilityStatus;

public class AvailabilityHistory {

    private List<Availability> availabilityChartList;
    private List<AvailabilityState> unavailableList;
    private AvailabilityPieChart availabilityPieChart;

    public List<Availability> getAvailabilityChartList() {
        return availabilityChartList;
    }

    public void setAvailabilityChartList(List<Availability> availabilityChartList) {
        this.availabilityChartList = availabilityChartList;
    }

    public List<AvailabilityState> getUnavailableList() {
        return unavailableList;
    }

    public void setUnavailableList(List<AvailabilityState> unavailableList) {
        this.unavailableList = new ArrayList<>();
        for (AvailabilityState state : unavailableList) {
            if (!state.getStatus().equals(AvailabilityStatus.AVAILABLE)) {
                this.unavailableList.add(state);
            }
        }
    }

    public AvailabilityPieChart getAvailabilityPieChart() {
        return availabilityPieChart;
    }

    public void setAvailabilityPieChart(AvailabilityPieChart availabilityPieChart) {
        this.availabilityPieChart = availabilityPieChart;
    }

    /* ----- ----- Occurence calculator ----- ----- */

    public int getUnavailableEntryOccurence() {
        return getStateXStatusEntryOccurence(AvailabilityStatus.UNAVAILABLE);
    }

    public int getUnsupportedEntryOccurence() {
        return getStateXStatusEntryOccurence(AvailabilityStatus.UNSUPPORTED);
    }

    private int getStateXStatusEntryOccurence(AvailabilityStatus status) {
        int nb = 0;
        for (AvailabilityState state : unavailableList) {
            if (state.getStatus().equals(status)) {
                nb = nb + 1;
            }
        }
        return nb;
    }

}
