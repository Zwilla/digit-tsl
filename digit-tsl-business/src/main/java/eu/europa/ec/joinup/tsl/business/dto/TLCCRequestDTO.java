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
package eu.europa.ec.joinup.tsl.business.dto;

/**
 * DTO to Request TLCC service
 */
public class TLCCRequestDTO {

    private int tlId;
    private String tlXmlPath;
    private String lotlPath;
    private int tspIndex;
    private int serviceIndex;

    public TLCCRequestDTO(int tlId) {
        this.tlId = tlId;
    }

    public TLCCRequestDTO(int tlId, int tspIndex) {
        this.tlId = tlId;
        this.tspIndex = tspIndex;
    }

    public TLCCRequestDTO(int tlId, int tspIndex, int serviceIndex) {
        this.tlId = tlId;
        this.tspIndex = tspIndex;
        this.serviceIndex = serviceIndex;
    }

    public int getTlId() {
        return tlId;
    }

    public void setTlId(int tlId) {
        this.tlId = tlId;
    }

    public String getTlXmlPath() {
        return tlXmlPath;
    }

    public void setTlXmlPath(String tlXmlPath) {
        this.tlXmlPath = tlXmlPath;
    }

    public String getLotlPath() {
        return lotlPath;
    }

    public void setLotlPath(String lotlPath) {
        this.lotlPath = lotlPath;
    }

    public int getTspIndex() {
        return tspIndex;
    }

    public void setTspIndex(int tspIndex) {
        this.tspIndex = tspIndex;
    }

    public int getServiceIndex() {
        return serviceIndex;
    }

    public void setServiceIndex(int serviceIndex) {
        this.serviceIndex = serviceIndex;
    }

}
