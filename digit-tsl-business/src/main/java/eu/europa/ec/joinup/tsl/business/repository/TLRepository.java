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

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;

public interface TLRepository extends CrudRepository<DBTrustedLists, Integer> {

    DBTrustedLists findByTerritoryAndStatusAndArchiveFalse(DBCountries territory, TLStatus status);

    List<DBTrustedLists> findByStatusAndArchiveFalseOrderByNameAsc(TLStatus status);

    List<DBTrustedLists> findAllByArchiveFalseOrderByNameAsc();

    List<DBTrustedLists> findAllByArchiveFalseOrderByStatusDesc();

    List<DBTrustedLists> findByTerritoryAndStatusAndArchiveTrueOrderByIdDesc(DBCountries territory, TLStatus prod);

    List<DBTrustedLists> findByXmlFileDigestAndStatusOrderByNameAsc(String digest, TLStatus status);

    DBTrustedLists findByXmlFileId(int fileId);

    @Modifying
    @Query("UPDATE DBTrustedLists tl SET tl.lastAccessDate = :lastAccessDate WHERE tl.id = :id")
    void updateLastAccessDate(@Param("lastAccessDate") Date lastAccess, @Param("id") int id);

    Long countByStatus(TLStatus status);

}
