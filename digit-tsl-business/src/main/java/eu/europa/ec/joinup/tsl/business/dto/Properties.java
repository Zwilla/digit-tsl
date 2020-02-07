package eu.europa.ec.joinup.tsl.business.dto;

import java.io.Serializable;

import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.DBProperties;

public class Properties implements Serializable {

    private static final long serialVersionUID = -3775159015988230085L;

    private int id;
    private String codeList;
    private String label;
    private String description;

    public Properties() {
    }

    public Properties(String codeList, DBProperties dbProp) {
        this.codeList = codeList;
        this.id = dbProp.getId();
        this.label = dbProp.getLabel();
        this.description = dbProp.getDescription();
    }

    public Properties(String codeList, DBCountries dbCountry) {
        this.codeList = codeList;
        this.label = dbCountry.getCodeTerritory();
        this.description = dbCountry.getCountryName();
    }

    @Override
    public String toString() {
        return String.format("Properties[code lsit='%s'; propertiesId = %d]", this.codeList, this.getId());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodeList() {
        return codeList;
    }

    public void setCodeList(String codeList) {
        this.codeList = codeList;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
