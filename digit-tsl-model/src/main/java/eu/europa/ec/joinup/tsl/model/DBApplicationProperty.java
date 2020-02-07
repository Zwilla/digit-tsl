package eu.europa.ec.joinup.tsl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TL_APPLICATION_PROPERTIES")
public class DBApplicationProperty {

    @Id
    @GeneratedValue
    @Column(name = "APPLICATION_PROPERTIES_ID", nullable = false, updatable = false)
    private int id;

    @Column(name = "PROPERTY_TYPE", length = 50, nullable = false, unique = true)
    private String type;

    @Column(name = "ACTIVE", nullable = false)
    private boolean active;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + (active ? 1231 : 1237);
        result = (prime * result) + ((description == null) ? 0 : description.hashCode());
        result = (prime * result) + id;
        result = (prime * result) + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DBApplicationProperty other = (DBApplicationProperty) obj;
        if (active != other.active) {
            return false;
        }
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (id != other.id) {
            return false;
        }
        if (type == null) {
            return other.type == null;
        } else return type.equals(other.type);
    }

}
