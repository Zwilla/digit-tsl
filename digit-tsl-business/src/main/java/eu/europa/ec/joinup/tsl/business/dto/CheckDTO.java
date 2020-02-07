package eu.europa.ec.joinup.tsl.business.dto;

import java.io.Serializable;
import java.util.Date;

import eu.europa.ec.joinup.tsl.model.DBCheck;
import eu.europa.ec.joinup.tsl.model.DBCheckResult;
import eu.europa.ec.joinup.tsl.model.enums.CheckImpact;
import eu.europa.ec.joinup.tsl.model.enums.CheckName;
import eu.europa.ec.joinup.tsl.model.enums.CheckStatus;
import eu.europa.ec.joinup.tsl.model.enums.CheckType;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

public class CheckDTO implements Serializable {

    private static final long serialVersionUID = 3842997713655023600L;

    private String id;
    private String hrLocation;
    private Tag target;
    private CheckName name;
    private CheckStatus status;
    private CheckImpact impact;
    private String description;
    private String translation;
    private String standardReference;
    private CheckType type;

    private Date startDate;
    private Long since;

    public CheckDTO() {
    }

    public CheckDTO(DBCheck db) {
        super();
        this.id = db.getId();
        this.hrLocation = "";
        this.target = db.getTarget();
        this.name = db.getName();
        this.status = db.getPriority();
        this.impact = db.getImpact();
        this.description = db.getDescription();
        this.type = db.getType();
        this.translation = db.getTranslation();
        this.standardReference = db.getStandardReference();
    }

    public CheckDTO(CheckResultDTO checkResult) {
        this.id = checkResult.getId();
        this.hrLocation = checkResult.getLocation();
        this.description = checkResult.getDescription();
        this.status = checkResult.getStatus();
        this.translation = checkResult.getTranslation();
        this.standardReference = checkResult.getStandardReference();
    }

    public CheckDTO(DBCheckResult dbResult) {
        this.id = dbResult.getLocation();
        this.hrLocation = dbResult.getHrLocation();
        this.description = dbResult.getDescription();
        this.status = dbResult.getStatus();
        this.translation = dbResult.getCheck().getTranslation();
        this.standardReference = dbResult.getCheck().getStandardReference();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHrLocation() {
        return hrLocation;
    }

    public void setHrLocation(String hrLocation) {
        this.hrLocation = hrLocation;
    }

    public Tag getTarget() {
        return target;
    }

    public void setTarget(Tag target) {
        this.target = target;
    }

    public CheckName getName() {
        return name;
    }

    public void setName(CheckName name) {
        this.name = name;
    }

    public CheckStatus getStatus() {
        return status;
    }

    public void setStatus(CheckStatus status) {
        this.status = status;
    }

    public CheckImpact getImpact() {
        return impact;
    }

    public void setImpact(CheckImpact impact) {
        this.impact = impact;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CheckType getType() {
        return type;
    }

    public void setType(CheckType type) {
        this.type = type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Long getSince() {
        return since;
    }

    public void setSince(Long since) {
        this.since = since;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getStandardReference() {
        return standardReference;
    }

    public void setStandardReference(String standardReference) {
        this.standardReference = standardReference;
    }

}
