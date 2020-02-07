package eu.europa.ec.joinup.tsl.business.dto.tl.approachbreak;

public enum BreakType {

    // No problem
    NONE,
    // Expiration approach or expired but not day of break
    WARNING,
    // Day of break reached
    DAY_OF_BREAK

}
