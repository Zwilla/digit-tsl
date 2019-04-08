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

import eu.europa.esig.jaxb.v5.ecc.CriteriaListTypeV5;
import eu.europa.esig.jaxb.xades.IdentifierType;

public class TLIdentifier extends AbstractTLDTO {

    public TLIdentifier() {
    }

    public TLIdentifier(int iddb, String location, IdentifierType idt) {
        super(iddb, location);

    }

    public CriteriaListTypeV5 asTSLTypeV5() {
        CriteriaListTypeV5 clt = new CriteriaListTypeV5();
        return clt;
    }

}
