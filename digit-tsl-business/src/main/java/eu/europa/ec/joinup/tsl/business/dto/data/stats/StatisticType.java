package eu.europa.ec.joinup.tsl.business.dto.data.stats;

import eu.europa.ec.joinup.tsl.business.constant.ServiceLegalType;

public class StatisticType {

    private ServiceLegalType type;
    private int nbActive;
    private int nbActiveTOB;
    private int nbInactive;
    private int nbInactiveTOB;

    public StatisticType() {
        super();
    }

    public StatisticType(ServiceLegalType type) {
        super();
        this.type = type;
        nbActive = 0;
        nbActiveTOB = 0;
        nbInactive = 0;
        nbInactiveTOB = 0;
    }

    public void incrementCounter(boolean isActive, boolean isTOB) {
        if (isActive && !isTOB) {
            nbActive = nbActive + 1;
        } else if (isActive) {
            nbActiveTOB = nbActiveTOB + 1;
        } else if (!isTOB) {
            nbInactive = nbInactive + 1;
        } else {
            nbInactiveTOB = nbInactiveTOB + 1;
        }
    }

    /**
     * Return if type has at least one service
     */
    public boolean hasService() {
        return ((nbActive > 0) || (nbActiveTOB > 0) || (nbInactive > 0) || (nbInactiveTOB > 0));
    }

    public int getAllActive() {
        return nbActive + nbActiveTOB;
    }

    public int getAllInactive() {
        return nbInactive + nbInactiveTOB;
    }

    public ServiceLegalType getType() {
        return type;
    }

    public void setType(ServiceLegalType type) {
        this.type = type;
    }

    public int getNbActive() {
        return nbActive;
    }

    public void setNbActive(int nbActive) {
        this.nbActive = nbActive;
    }

    public int getNbActiveTOB() {
        return nbActiveTOB;
    }

    public void setNbActiveTOB(int nbActiveTOB) {
        this.nbActiveTOB = nbActiveTOB;
    }

    public int getNbInactive() {
        return nbInactive;
    }

    public void setNbInactive(int nbInactive) {
        this.nbInactive = nbInactive;
    }

    public int getNbInactiveTOB() {
        return nbInactiveTOB;
    }

    public void setNbInactiveTOB(int nbInactiveTOB) {
        this.nbInactiveTOB = nbInactiveTOB;
    }

}
