package eu.europa.ec.joinup.tsl.business.dto.data.stats;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Find Trust service or service by country and type
 */
public class TLDataTypeCriteriaDTO {

    private List<String> countries;
    private List<String> serviceTypes;

    public TLDataTypeCriteriaDTO() {
        super();
    }

    public TLDataTypeCriteriaDTO(List<String> countries, List<String> serviceTypes) {
        super();
        this.countries = countries;
        this.serviceTypes = serviceTypes;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public Set<String> getSetServiceTypes() {
        if (serviceTypes == null) {
            return new HashSet<>();
        }
        return new HashSet<>(serviceTypes);
    }

    public List<String> getServiceTypes() {
        return serviceTypes;
    }

    public void setServiceTypes(List<String> serviceTypes) {
        this.serviceTypes = serviceTypes;
    }

}
