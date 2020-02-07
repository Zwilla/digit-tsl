package eu.europa.ec.joinup.tsl.business.dto.data.tl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import eu.europa.ec.joinup.tsl.model.DBService;
import eu.europa.ec.joinup.tsl.model.DBTrustServiceProvider;

public class TSPDataDTO {

    private int id;
    private String tspId;
    private List<String> tspTradeNames;
    private List<String> tspNames;
    private String countryCode;
    private int sequenceNumber;
    private List<ServiceDataDTO> services;

    public TSPDataDTO() {
        super();
    }

    /**
     * Init TSP
     *
     * @param dbTSP
     * @param initServices
     */
    public TSPDataDTO(DBTrustServiceProvider dbTSP) {
        super();
        id = dbTSP.getId();
        countryCode = dbTSP.getCountryCode();
        sequenceNumber = dbTSP.getSequenceNumber();
        tspId = dbTSP.getTspId();
        tspNames = new ArrayList<>(dbTSP.getTspNames());
        tspTradeNames = new ArrayList<>(dbTSP.getTspTradeNames());
        services = new ArrayList<>();
    }

    /**
     * Init TSP and services (if initServices is true)
     *
     * @param dbTrustService
     * @param initServices
     *            Initialize service list
     */
    public TSPDataDTO(DBTrustServiceProvider dbTrustService, boolean initServices) {
        this(dbTrustService);
        if (initServices && !CollectionUtils.isEmpty(dbTrustService.getServices())) {
            for (DBService dbService : dbTrustService.getServices()) {
                services.add(new ServiceDataDTO(dbService, false));
            }
        }
    }

    /**
     * Init TSP, services and history (if params true)
     *
     * @param dbTrustService
     * @param initServices
     *            Initialize service list
     * @param initHistory
     *            Initialize history list (require initServices true)
     */
    public TSPDataDTO(DBTrustServiceProvider dbTrustService, boolean initServices, boolean initHistory) {
        this(dbTrustService);
        if (initServices && !CollectionUtils.isEmpty(dbTrustService.getServices())) {
            for (DBService dbService : dbTrustService.getServices()) {
                services.add(new ServiceDataDTO(dbService, false, initHistory));
            }
        }
    }

    /**
     * Get first name
     */
    public String getMName() {
        if (CollectionUtils.isEmpty(tspNames)) {
            return "";
        }
        return tspNames.get(0);
    }

    /**
     * Get first trade name
     */
    public String getMTradeName() {
        if (CollectionUtils.isEmpty(tspTradeNames)) {
            return "";
        }
        return tspTradeNames.get(0);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTspId() {
        return tspId;
    }

    public void setTspId(String tspId) {
        this.tspId = tspId;
    }

    public List<String> getTspTradeNames() {
        return tspTradeNames;
    }

    public void setTspTradeNames(List<String> tspTradeNames) {
        this.tspTradeNames = tspTradeNames;
    }

    public List<String> getTspNames() {
        return tspNames;
    }

    public void setTspNames(List<String> tspNames) {
        this.tspNames = tspNames;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public List<ServiceDataDTO> getServices() {
        return services;
    }

    public void setServices(List<ServiceDataDTO> services) {
        this.services = services;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + id;
        result = (prime * result) + ((tspId == null) ? 0 : tspId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TSPDataDTO other = (TSPDataDTO) obj;
        if (id != other.id) {
            return false;
        }
        if (tspId == null) {
            if (other.tspId != null) {
                return false;
            }
        } else if (!tspId.equals(other.tspId)) {
            return false;
        }
        return true;
    }

}
