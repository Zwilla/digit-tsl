package eu.europa.ec.joinup.tsl.web.form;

import java.util.List;

import eu.europa.ec.joinup.tsl.business.dto.TrustedListsReport;

public class PrepareMergeDTO {

    private String countryCode;
    private List<TrustedListsReport> drafts;

    public PrepareMergeDTO() {
        super();
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public List<TrustedListsReport> getDrafts() {
        return drafts;
    }

    public void setDrafts(List<TrustedListsReport> drafts) {
        this.drafts = drafts;
    }

}
