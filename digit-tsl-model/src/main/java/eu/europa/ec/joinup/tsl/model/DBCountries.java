package eu.europa.ec.joinup.tsl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TL_COUNTRIES")
public class DBCountries {

    @Id
    @Column(name = "CODE_TERRITORY")
    private String codeTerritory;

    @Column(name = "COUNTRY_NAME")
    private String countryName;

    public DBCountries() {
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((codeTerritory == null) ? 0 : codeTerritory.hashCode());
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
        DBCountries other = (DBCountries) obj;
        if (codeTerritory == null) {
            if (other.codeTerritory != null) {
                return false;
            }
        } else if (!codeTerritory.equals(other.codeTerritory)) {
            return false;
        }
        return true;
    }

    public String getCodeTerritory() {
        return codeTerritory;
    }

    public void setCodeTerritory(String codeTerritory) {
        this.codeTerritory = codeTerritory;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

}
