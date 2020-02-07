package eu.europa.ec.joinup.tsl.business.dto.data.tl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import eu.europa.ec.joinup.tsl.model.DBHistory;
import eu.europa.ec.joinup.tsl.model.DBService;

public class ServiceHistoryAbstractDataDTO {

    private String countryCode;
    private List<String> names;
    private String type;
    private String status;
    private Date startingDate;
    private String takenOverBy;
    private List<String> qTypes;

    public ServiceHistoryAbstractDataDTO() {
        super();
    }

    public ServiceHistoryAbstractDataDTO(DBService dbService) {
        super();
        countryCode = dbService.getCountryCode();
        names = new ArrayList<>(dbService.getServiceNames());
        type = dbService.getType();
        status = dbService.getStatus();
        startingDate = dbService.getStartingDate();
        takenOverBy = dbService.getTakenOverBy();
        qTypes = new ArrayList<>(dbService.getQServiceTypes());
    }

    public ServiceHistoryAbstractDataDTO(DBHistory dbHistory) {
        super();
        countryCode = dbHistory.getCountryCode();
        names = new ArrayList<>(dbHistory.getHistoryNames());
        type = dbHistory.getType();
        status = dbHistory.getStatus();
        startingDate = dbHistory.getStartingDate();
        takenOverBy = dbHistory.getTakenOverBy();
        qTypes = new ArrayList<>(dbHistory.getQHistoryTypes());
    }

    public String getMName() {
        if (CollectionUtils.isEmpty(names)) {
            return "";
        }
        return names.get(0);
    }

    /**
     * Return true when service is taken over by
     */
    public boolean isTakenOverBy() {
        return !StringUtils.isEmpty(takenOverBy);
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(Date startingDate) {
        this.startingDate = startingDate;
    }

    public String getTakenOverBy() {
        return takenOverBy;
    }

    public void setTakenOverBy(String takenOverBy) {
        this.takenOverBy = takenOverBy;
    }

    public List<String> getQTypes() {
        return qTypes;
    }

    public void setQTypes(List<String> qTypes) {
        this.qTypes = qTypes;
    }

}
