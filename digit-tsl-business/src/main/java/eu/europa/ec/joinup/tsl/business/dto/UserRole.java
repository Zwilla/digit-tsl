package eu.europa.ec.joinup.tsl.business.dto;

import eu.europa.ec.joinup.tsl.model.DBRole;

public class UserRole {

    private int id;
    private String code;
    private String name;
    private String description;

    public UserRole() {
    }

    public UserRole(DBRole dbR) {
        this.setId(dbR.getId());
        this.setCode(dbR.getCode());
        this.setName(dbR.getName());
        this.setDescription(dbR.getDescription());
    }

    @Override
    public String toString() {
        return String.format("UserCode[id=%d; Code = '%s']", getId(), getCode());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
