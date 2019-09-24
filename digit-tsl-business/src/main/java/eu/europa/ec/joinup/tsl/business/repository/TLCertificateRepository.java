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

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import eu.europa.ec.joinup.tsl.model.DBCertificate;
import eu.europa.ec.joinup.tsl.model.enums.TLType;

public interface TLCertificateRepository extends CrudRepository<DBCertificate, Integer> {

    @Override
    public List<DBCertificate> findAll();

    public List<DBCertificate> findByTlType(TLType type);

    public List<DBCertificate> getAllByCountryCodeAndTlType(String countryCode, TLType type);

    public List<DBCertificate> getAllByCountryCodeAndNotAfterBetweenAndTlType(String countryCode, Date expirationDateStart, Date expirationDateEnd, TLType type);

    public List<DBCertificate> getAllByCountryCodeAndNotBeforeLessThanAndTlType(String countryCode, Date today, TLType type);

    public void deleteByCountryCodeAndTlType(String countryCode, TLType type);

    public void deleteByTlType(TLType type);

    public List<DBCertificate> findBySki(byte[] ski);
}
