package eu.europa.ec.joinup.tsl.business.dto.data.retention;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class RetentionCriteriaDTO {

    @JsonDeserialize
    private RetentionTarget target;

    private String territoryCode;

    private Date date;

    public RetentionCriteriaDTO() {
        super();
    }

    public RetentionCriteriaDTO(RetentionTarget target, Date date) {
        super();
        this.target = target;
        this.date = date;
    }

    public RetentionTarget getTarget() {
        return target;
    }

    public void setTarget(RetentionTarget target) {
        this.target = target;
    }

    public String getTerritoryCode() {
        return territoryCode;
    }

    public void setTerritoryCode(String territoryCode) {
        this.territoryCode = territoryCode;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
