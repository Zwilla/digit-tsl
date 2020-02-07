package eu.europa.ec.joinup.tsl.business.util;

import java.util.Comparator;

import eu.europa.ec.joinup.tsl.model.DBFilesAvailability;

/**
 * Order DBFilesAvailability by check date DESC
 */
public class AvailabilityComparatorUtils implements Comparator<DBFilesAvailability> {

    @Override
    public int compare(DBFilesAvailability o1, DBFilesAvailability o2) {
        return (o2.getCheckDate().compareTo(o1.getCheckDate()));
    }

}
