package eu.europa.ec.joinup.tsl.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TL_USERS")
public class DBUser {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ECAS_ID", unique = true, nullable = false)
    private String ecasId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TL_USER_ROLE", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID") })
    private List<DBRole> role;

    @ManyToOne
    @JoinColumn(name = "TERRITORY")
    private DBCountries territory;

    public DBUser() {
    }

    public DBUser(String ecasId, List<DBRole> role, DBCountries territory) {
        super();
        this.ecasId = ecasId;
        this.name = ecasId;
        this.role = role;
        this.territory = territory;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + id;
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
        DBUser other = (DBUser) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEcasId() {
        return ecasId;
    }

    public void setEcasId(String ecasId) {
        this.ecasId = ecasId;
    }

    public List<DBRole> getRole() {
        if (this.role == null) {
            this.role = new ArrayList<>();
        }
        return role;
    }

    public void setRole(List<DBRole> role) {
        this.role = role;
    }

    public DBCountries getTerritory() {
        return territory;
    }

    public void setTerritory(DBCountries territory) {
        this.territory = territory;
    }
}
