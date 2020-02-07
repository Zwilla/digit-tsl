package eu.europa.ec.joinup.tsl.business.dto.data.stats;

import java.util.Date;

public class StatisticTSP extends StatisticGeneric {

    private String name;
    private String tradeName;
    private int nbService;

    public StatisticTSP() {
        super();
    }

    public StatisticTSP(String countryCode, int sequenceNumber, String name, String tradeName, Date extractDate) {
        super(countryCode, sequenceNumber, extractDate);
        this.name = name;
        this.tradeName = tradeName;
        nbService = 0;
    }

    public void incrementNbService() {
        nbService = nbService + 1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public int getNbService() {
        return nbService;
    }

    public void setNbService(int nbService) {
        this.nbService = nbService;
    }

}
