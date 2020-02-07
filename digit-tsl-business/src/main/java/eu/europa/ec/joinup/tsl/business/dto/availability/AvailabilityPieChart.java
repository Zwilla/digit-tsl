package eu.europa.ec.joinup.tsl.business.dto.availability;

public class AvailabilityPieChart {

    private long availableTiming;
    private long unavailableTiming;
    private long unsupportedTiming;

    public AvailabilityPieChart() {
        availableTiming = 0;
        unavailableTiming = 0;
        unsupportedTiming = 0;
    }

    public long getAvailableTiming() {
        return availableTiming;
    }

    public void setAvailableTiming(long availableTiming) {
        this.availableTiming = availableTiming;
    }

    public long getUnavailableTiming() {
        return unavailableTiming;
    }

    public void setUnavailableTiming(long unavailableTiming) {
        this.unavailableTiming = unavailableTiming;
    }

    public long getUnsupportedTiming() {
        return unsupportedTiming;
    }

    public void setUnsupportedTiming(long unsupportedTiming) {
        this.unsupportedTiming = unsupportedTiming;
    }

    public void addTime(AvailabilityState state) {
        switch (state.getStatus()) {
        case AVAILABLE:
            availableTiming = availableTiming + state.getTiming();
            break;
        case UNAVAILABLE:
            unavailableTiming = unavailableTiming + state.getTiming();
            break;
        case UNSUPPORTED:
            unsupportedTiming = unsupportedTiming + state.getTiming();
            break;
        }

    }

}
