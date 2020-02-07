package eu.europa.ec.joinup.tsl.business.util;

import java.util.Comparator;

import eu.europa.ec.joinup.tsl.business.dto.CheckDTO;

/**
 * Order CheckDTO by hrLocation > status > description
 */
public class TLChecksComparatorUtils implements Comparator<CheckDTO> {

    @Override
    public int compare(CheckDTO o1, CheckDTO o2) {
        String location1 = o1.getHrLocation();
        String location2 = o2.getHrLocation();
        int locComp = location1.compareTo(location2);
        if (locComp != 0) {
            return locComp;
        } else {
            String target1 = o1.getStatus().toString();
            String target2 = o2.getStatus().toString();
            int sComp = target1.compareTo(target2);

            if (sComp != 0) {
                return sComp;
            } else {
                String desc1 = o1.getDescription();
                String desc2 = o2.getDescription();
                return desc1.compareTo(desc2);
            }
        }
    }

}
