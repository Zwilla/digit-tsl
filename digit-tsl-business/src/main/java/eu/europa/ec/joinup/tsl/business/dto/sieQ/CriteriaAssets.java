package eu.europa.ec.joinup.tsl.business.dto.sieQ;

public enum CriteriaAssets {

    ALO("atLeastOne"), NONE("none"), ALL("all");

    private String label;

    private CriteriaAssets(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
