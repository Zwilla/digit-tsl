package eu.europa.ec.joinup.tsl.business.dto.audit;

import java.util.Arrays;
import java.util.List;

import eu.europa.ec.joinup.tsl.model.enums.AuditAction;
import eu.europa.ec.joinup.tsl.model.enums.AuditTarget;

public class AuditCriteriaDTO {

    private List<String> countries;
    private List<AuditTarget> targets;
    private List<AuditAction> actions;

    protected AuditCriteriaDTO() {

    }

    public AuditCriteriaDTO(List<String> countries) {
        super();
        this.countries = countries;
        this.targets = Arrays.asList(AuditTarget.values());
        this.actions = Arrays.asList(AuditAction.values());
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public List<AuditTarget> getTargets() {
        return targets;
    }

    public void setTargets(List<AuditTarget> targets) {
        this.targets = targets;
    }

    public List<AuditAction> getActions() {
        return actions;
    }

    public void setActions(List<AuditAction> actions) {
        this.actions = actions;
    }

}
