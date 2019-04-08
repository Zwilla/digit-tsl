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
package eu.europa.ec.joinup.tsl.business.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import eu.europa.ec.joinup.tsl.model.DBTrustServiceProvider;

public interface TrustServiceProviderRepository extends CrudRepository<DBTrustServiceProvider, Integer> {

    @Override
    List<DBTrustServiceProvider> findAll();

    List<DBTrustServiceProvider> findDistinctByCountryCodeInAndServicesQServiceTypesInOrderByCountryCode(List<String> countries, Set<String> types);

    List<DBTrustServiceProvider> findAllByCountryCode(String countryCode);

    void deleteAllByCountryCode(String countryCode);

}
