package eu.europa.ec.joinup.tsl.business.dto.data.tl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import eu.europa.ec.joinup.tsl.model.DBHistory;
import eu.europa.ec.joinup.tsl.model.DBService;

public class ServiceDataDTO extends ServiceHistoryAbstractDataDTO {

    private int id;
    private String serviceId;
    private TSPDataDTO trustServiceProvider;
    private List<HistoryDataDTO> history;

    public ServiceDataDTO() {
        super();
    }

    /**
     * Init Service and his TSP
     *
     * @param dbService
     * @param initTSP
     *            Initialize trust service provider
     */
    public ServiceDataDTO(DBService dbService, boolean initTSP) {
        super(dbService);
        id = dbService.getId();
        serviceId = dbService.getServiceId();
        if (initTSP) {
            trustServiceProvider = new TSPDataDTO(dbService.getTrustServiceProvider(), false);
        }
        history = new ArrayList<>();
    }

    /**
     * Init Service, his TSP and history
     *
     * @param dbService
     * @param initTSP
     *            Initialize trust service provider
     * @param initHistory
     *            Initialize history list
     */
    public ServiceDataDTO(DBService dbService, boolean initTSP, boolean initHistory) {
        this(dbService, initTSP);
        if (initHistory && !CollectionUtils.isEmpty(dbService.getHistory())) {
            for (DBHistory dbHistory : dbService.getHistory()) {
                history.add(new HistoryDataDTO(dbHistory));
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public TSPDataDTO getTrustServiceProvider() {
        return trustServiceProvider;
    }

    public void setTrustServiceProvider(TSPDataDTO trustServiceProvider) {
        this.trustServiceProvider = trustServiceProvider;
    }

    public List<HistoryDataDTO> getHistory() {
        return history;
    }

    public void setHistory(List<HistoryDataDTO> history) {
        this.history = history;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + id;
        result = (prime * result) + ((serviceId == null) ? 0 : serviceId.hashCode());
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
        ServiceDataDTO other = (ServiceDataDTO) obj;
        if (id != other.id) {
            return false;
        }
        if (serviceId == null) {
            if (other.serviceId != null) {
                return false;
            }
        } else if (!serviceId.equals(other.serviceId)) {
            return false;
        }
        return true;
    }

}
