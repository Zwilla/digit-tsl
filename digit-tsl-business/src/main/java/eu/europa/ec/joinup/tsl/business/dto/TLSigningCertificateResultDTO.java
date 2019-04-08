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

import java.util.List;

import eu.europa.ec.joinup.tsl.business.dto.tl.TLCertificate;

public class TLSigningCertificateResultDTO {

    private TLCertificate certificate;
    private List<CheckResultDTO> results;

    public TLSigningCertificateResultDTO() {
        super();
    }

    public TLSigningCertificateResultDTO(byte[] certificateEncoded, List<CheckResultDTO> results) {
        super();
        this.certificate = new TLCertificate(certificateEncoded);
        this.results = results;
    }

    public TLCertificate getCertificate() {
        return certificate;
    }

    public void setCertificate(TLCertificate certificate) {
        this.certificate = certificate;
    }

    public List<CheckResultDTO> getResults() {
        return results;
    }

    public void setResults(List<CheckResultDTO> results) {
        this.results = results;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((certificate == null) ? 0 : certificate.hashCode());
        result = prime * result + ((results == null) ? 0 : results.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TLSigningCertificateResultDTO other = (TLSigningCertificateResultDTO) obj;
        if (certificate == null) {
            if (other.certificate != null)
                return false;
        } else if (!certificate.equals(other.certificate))
            return false;
        if (results == null) {
            if (other.results != null)
                return false;
        } else if (!results.equals(other.results))
            return false;
        return true;
    }

}
