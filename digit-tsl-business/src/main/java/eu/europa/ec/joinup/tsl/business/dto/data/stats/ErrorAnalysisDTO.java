package eu.europa.ec.joinup.tsl.business.dto.data.stats;

import java.util.Map;
import java.util.TreeMap;

import eu.europa.ec.joinup.tsl.model.DBCheck;

public class ErrorAnalysisDTO {

    private DBCheck check = null;
    private Map<String, Integer> resultMap = new TreeMap<>();
    private int totalCheck;
    private String tlImpactedCc;
    private int tlImpacted;

    public DBCheck getCheck() {
        return check;
    }

    public void setCheck(DBCheck check) {
        this.check = check;
    }

    public Map<String, Integer> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<String, Integer> resultMap) {
        this.resultMap = resultMap;
    }

    public int getTotalCheck() {
        return totalCheck;
    }

    public void setTotalCheck(int totalCheck) {
        this.totalCheck = totalCheck;
    }

    public int getTlImpacted() {
        return tlImpacted;
    }

    public void setTlImpacted(int tlImpacted) {
        this.tlImpacted = tlImpacted;
    }

    public String getTlImpactedCc() {
        return tlImpactedCc;
    }

    public void setTlImpactedCc(String tlImpactedCc) {
        this.tlImpactedCc = tlImpactedCc;
    }
}
