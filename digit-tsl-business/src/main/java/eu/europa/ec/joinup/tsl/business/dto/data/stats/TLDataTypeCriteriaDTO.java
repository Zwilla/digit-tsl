/*******************************************************************************
 * DIGIT-TSL - Trusted List Manager
 * Copyright (C) 2018 European Commission, provided under the CEF E-Signature programme
 *  
 * This file is part of the "DIGIT-TSL - Trusted List Manager" project.
 *  
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 *  
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
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
