package eu.europa.ec.joinup.tsl.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "TL_PROPERTIES_LIST")
public class DBPropertiesList {

    @Id
    @Column(name = "CODE")
    private String code;

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany(mappedBy = "propertiesList", fetch = FetchType.LAZY)
    private List<DBProperties> propertiesInfo;

    public DBPropertiesList() {
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((code == null) ? 0 : code.hashCode());
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
        DBPropertiesList other = (DBPropertiesList) obj;
        if (code == null) {
            if (other.code != null) {
                return false;
            }
        } else if (!code.equals(other.code)) {
            return false;
        }
        return true;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<DBProperties> getPropertiesInfo() {
        return propertiesInfo;
    }

    public void setPropertiesInfo(List<DBProperties> propertiesInfo) {
        this.propertiesInfo = propertiesInfo;
    }

}
