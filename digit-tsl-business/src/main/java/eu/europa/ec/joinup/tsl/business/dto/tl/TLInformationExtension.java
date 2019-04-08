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

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import eu.europa.esig.jaxb.tsl.ExtensionType;
import eu.europa.esig.jaxb.v5.tsl.ExtensionTypeV5;
import eu.europa.esig.jaxb.v5.xades.IdentifierTypeV5;
import eu.europa.esig.jaxb.v5.xades.ObjectIdentifierTypeV5;
import eu.europa.esig.jaxb.xades.ObjectIdentifierType;

public class TLInformationExtension extends AbstractTLDTO {

    private boolean critical;
    private String value;

    public boolean isCritical() {
        return critical;
    }

    public TLInformationExtension() {
        super();

    }

    @SuppressWarnings("rawtypes")
    public TLInformationExtension(int iddb, String location, ExtensionType ext) {
        super(iddb, location);
        this.setCritical(ext.isCritical());
        for (Object obj : ext.getContent()) {
            if (obj instanceof JAXBElement) {
                JAXBElement obj2 = (JAXBElement) obj;
                ObjectIdentifierType oit = (ObjectIdentifierType) obj2.getValue();
                this.setValue(oit.getIdentifier().getValue());
            }
        }

    }

    @SuppressWarnings("rawtypes")
    public TLInformationExtension(int iddb, String location, ExtensionTypeV5 ext) {
        super(iddb, location);
        this.setCritical(ext.isCritical());
        for (Object obj : ext.getContent()) {
            if (obj instanceof JAXBElement) {
                JAXBElement obj2 = (JAXBElement) obj;
                ObjectIdentifierTypeV5 oit = (ObjectIdentifierTypeV5) obj2.getValue();
                this.setValue(oit.getIdentifier().getValue());
            }
        }

    }

    public ExtensionTypeV5 asTSLTypeV5() {
        ExtensionTypeV5 e = new ExtensionTypeV5();
        e.setCritical(this.isCritical());
        if (this.getValue() != null) {
            ObjectIdentifierTypeV5 oitV5 = new ObjectIdentifierTypeV5();
            IdentifierTypeV5 itV5 = new IdentifierTypeV5();
            itV5.setValue(this.getValue());
            oitV5.setIdentifier(itV5);
            e.getContent().add(new JAXBElement<ObjectIdentifierTypeV5>(new QName("http://uri.etsi.org/01903/v1.3.2#", "ObjectIdentifier"), ObjectIdentifierTypeV5.class, oitV5));
        }
        return e;
    }

    /*
     * GETTER ANS SETTER
     */
    public void setCritical(boolean critical) {
        this.critical = critical;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
