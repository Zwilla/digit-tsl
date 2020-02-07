package eu.europa.ec.joinup.tsl.business.dto.data.stats;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import eu.europa.ec.joinup.tsl.business.constant.ServiceLegalType;

public abstract class StatisticGeneric {

    private String countryCode;
    private int sequenceNumber;
    private Date extractDate;
    private Map<ServiceLegalType, StatisticType> types;

    public StatisticGeneric() {
        super();
    }

    public StatisticGeneric(String countryCode, int sequenceNumber, Date extractDate) {
        super();
        this.countryCode = countryCode;
        this.sequenceNumber = sequenceNumber;
        this.extractDate = extractDate;
        types = new TreeMap<>();
        for (ServiceLegalType type : ServiceLegalType.values()) {
            types.put(type, new StatisticType(type));
        }
    }

    public void incrementType(ServiceLegalType legalType, boolean isActive, boolean isTOB) {
        types.get(legalType).incrementCounter(isActive, isTOB);
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Date getExtractDate() {
        return extractDate;
    }

    public void setExtractDate(Date extractDate) {
        this.extractDate = extractDate;
    }

    public Map<ServiceLegalType, StatisticType> getTypes() {
        return types;
    }

    public void setTypes(Map<ServiceLegalType, StatisticType> types) {
        this.types = types;
    }

}
