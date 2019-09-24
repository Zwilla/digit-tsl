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
package eu.europa.ec.joinup.tsl.checker.dto;

import java.util.List;

public class TLCCFileRequestDTO {

    private byte[] lotlFile;
    private byte[] tlFile;
    private List<String> keyStore;

    public TLCCFileRequestDTO() {
        super();
    }

    public TLCCFileRequestDTO(byte[] lotlFile, byte[] file) {
        this.lotlFile = lotlFile;
        this.tlFile = file;
    }

    public byte[] getLotlFile() {
        return lotlFile;
    }

    public void setLotlFile(byte[] lotlFile) {
        this.lotlFile = lotlFile;
    }

    public byte[] getTlFile() {
        return tlFile;
    }

    public void setTlFile(byte[] tlFile) {
        this.tlFile = tlFile;
    }

    public List<String> getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(List<String> keyStore) {
        this.keyStore = keyStore;
    }
}