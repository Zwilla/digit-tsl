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
package eu.europa.ec.joinup.tsl.business.dto.tl;

/**
 * This class is used to identify all DTO in the front-end
 */
public abstract class AbstractTLDTO {

    /**
     * Id of the linked TSL in the DB
     */
    private int tlId;

    /**
     * Location of the object in the TSL (used to compute differences)
     */
    private String id;

    public AbstractTLDTO() {
    }

    protected AbstractTLDTO(int tlId, String location) {
        this.tlId = tlId;
        this.id = location;
    }

    public int getTlId() {
        return tlId;
    }

    public void setTlId(int tlId) {
        this.tlId = tlId;
    }

    public String getId() {
        return id;
    }

    public void setId(String location) {
        this.id = location;
    }

}
