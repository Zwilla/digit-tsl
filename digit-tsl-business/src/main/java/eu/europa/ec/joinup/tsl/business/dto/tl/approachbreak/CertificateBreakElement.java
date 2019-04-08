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
package eu.europa.ec.joinup.tsl.business.dto.tl.approachbreak;

import java.util.ArrayList;
import java.util.List;

/**
 * Extends BreakElement with a list of certificates affected and verifiable flag
 */
public class CertificateBreakElement extends BreakElement {

    private Boolean verifiable;

    private List<CertificateElement> certificatesAffected;

    public CertificateBreakElement() {
        super();
        verifiable = true;
        certificatesAffected = new ArrayList<>();
    }

    public Boolean isVerifiable() {
        return verifiable;
    }

    public void setVerifiable(Boolean verifiable) {
        this.verifiable = verifiable;
    }

    public List<CertificateElement> getCertificatesAffected() {
        return certificatesAffected;
    }

    public void setCertificatesAffected(List<CertificateElement> certificatesAffected) {
        this.certificatesAffected = certificatesAffected;
    }

}
