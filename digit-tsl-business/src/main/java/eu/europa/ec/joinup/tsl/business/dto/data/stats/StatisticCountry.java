package eu.europa.ec.joinup.tsl.business.dto.data.stats;

import java.util.Date;

public class StatisticCountry extends StatisticGeneric {

    private int nbTSP;
    private int nbQActive;
    private int nbQTOB;

    public StatisticCountry() {
        super();
    }

    public StatisticCountry(String countryCode, int sequenceNumber, Date extractDate) {
        super(countryCode, sequenceNumber, extractDate);
        nbTSP = 0;
        nbQActive = 0;
        nbQTOB = 0;
    }

    public void incrementNbTSP() {
        nbTSP = nbTSP + 1;
    }

    public void incrementQActive() {
        nbQActive = nbQActive + 1;
    }

    public void incrementQTOB() {
        nbQTOB = nbQTOB + 1;
    }

    public int getNbTSP() {
        return nbTSP;
    }

    public void setNbTSP(int nbTSP) {
        this.nbTSP = nbTSP;
    }

    public int getNbQActive() {
        return nbQActive;
    }

    public void setNbQActive(int nbQActive) {
        this.nbQActive = nbQActive;
    }

    public int getNbQTOB() {
        return nbQTOB;
    }

    public void setNbQTOB(int nbQTOB) {
        this.nbQTOB = nbQTOB;
    }

}
