package eu.europa.ec.joinup.tsl.business.dto;

import eu.europa.ec.joinup.tsl.model.DBColumnAvailable;

public class Column {

    private String descripion;
    private String name;
    private boolean visible;

    public Column() {
    }

    public Column(DBColumnAvailable col) {
        if (col != null) {
            this.setDescripion(col.getDescription());
            this.setName(col.getCode());
            this.setVisible(col.getVisible());
        }
    }

    public String getDescripion() {
        return descripion;
    }

    public void setDescripion(String descripion) {
        this.descripion = descripion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

}
