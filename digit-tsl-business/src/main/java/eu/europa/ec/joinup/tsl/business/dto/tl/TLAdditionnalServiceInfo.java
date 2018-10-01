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

import eu.europa.esig.jaxb.tsl.AdditionalServiceInformationType;
import eu.europa.esig.jaxb.v5.tsl.AdditionalServiceInformationTypeV5;
import eu.europa.esig.jaxb.v5.tsl.NonEmptyMultiLangURITypeV5;

public class TLAdditionnalServiceInfo extends TLGeneric {

    public TLAdditionnalServiceInfo() {
    }

    public TLAdditionnalServiceInfo(int iddb, String location, AdditionalServiceInformationType asit) {
        super(iddb, location, asit.getURI().getLang(), asit.getURI().getValue());
    }

    public TLAdditionnalServiceInfo(int iddb, String location, AdditionalServiceInformationTypeV5 asit) {
        super(iddb, location, asit.getURI().getLang(), asit.getURI().getValue());
    }

    public AdditionalServiceInformationTypeV5 asTSLTypeV5() {
        AdditionalServiceInformationTypeV5 asi = new AdditionalServiceInformationTypeV5();
        NonEmptyMultiLangURITypeV5 uriType = new NonEmptyMultiLangURITypeV5();
        uriType.setLang(getLanguage());
        uriType.setValue(getValue());
        asi.setURI(uriType);

        return asi;
    }

}
