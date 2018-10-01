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

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import eu.europa.ec.joinup.tsl.model.DBFilesAvailability;

public interface AvailabilityRepository extends CrudRepository<DBFilesAvailability, Integer> {

    /**
     * This method returns the latest record
     */
    DBFilesAvailability findTopByFileIdOrderByCheckDateDesc(int fileId);

    @Query("SELECT f FROM DBFilesAvailability f WHERE f.file.id =:fileId AND f.checkDate BETWEEN :dMin AND :dMax")
    List<DBFilesAvailability> findBetweenTwoDate(@Param("fileId") int fileId, @Param("dMin") Date dMin, @Param("dMax") Date dMax);

}
