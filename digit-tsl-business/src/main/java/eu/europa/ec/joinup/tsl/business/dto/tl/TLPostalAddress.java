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

import org.apache.commons.lang3.StringUtils;

import eu.europa.ec.joinup.tsl.business.dto.TLDifference;
import eu.europa.ec.joinup.tsl.model.enums.Tag;
import eu.europa.esig.jaxb.tsl.PostalAddressType;
import eu.europa.esig.jaxb.v5.tsl.PostalAddressTypeV5;

public class TLPostalAddress extends AbstractTLDTO {

    private String street;
    private String locality;
    private String StateOrProvince;
    private String postalCode;
    private String countryCode;
    private String language;

    public TLPostalAddress() {
    }

    public TLPostalAddress(int iddb, String location, PostalAddressType tslPostalAdr) {
        super(iddb, location);
        setCountryCode(tslPostalAdr.getCountryName());
        setLanguage(tslPostalAdr.getLang());
        setLocality(tslPostalAdr.getLocality());
        setPostalCode(tslPostalAdr.getPostalCode());
        setStateOrProvince(tslPostalAdr.getStateOrProvince());
        setStreet(tslPostalAdr.getStreetAddress());
    }

    public TLPostalAddress(int iddb, String location, PostalAddressTypeV5 tslPostalAdr) {
        super(iddb, location);
        setCountryCode(tslPostalAdr.getCountryName());
        setLanguage(tslPostalAdr.getLang());
        setLocality(tslPostalAdr.getLocality());
        setPostalCode(tslPostalAdr.getPostalCode());
        setStateOrProvince(tslPostalAdr.getStateOrProvince());
        setStreet(tslPostalAdr.getStreetAddress());
    }

    public PostalAddressTypeV5 asTSLTypeV5() {
        PostalAddressTypeV5 tslPostal = new PostalAddressTypeV5();
        if (!StringUtils.isEmpty(getCountryCode())) {
            tslPostal.setCountryName(getCountryCode());
        }
        if (!StringUtils.isEmpty(getLanguage())) {
            tslPostal.setLang(getLanguage());
        }
        if (!StringUtils.isEmpty(getLocality())) {
            tslPostal.setLocality(getLocality());
        }
        if (!StringUtils.isEmpty(getPostalCode())) {
            tslPostal.setPostalCode(getPostalCode());
        }
        if (!StringUtils.isEmpty(getStateOrProvince())) {
            tslPostal.setStateOrProvince(getStateOrProvince());
        }
        if (!StringUtils.isEmpty(getStreet())) {
            tslPostal.setStreetAddress(getStreet());
        }
        return tslPostal;
    }

    public TLDifference asPublishedDiff(TLPostalAddress publishedTl) {
        TLDifference myDiff = null;

        if (publishedTl.getLanguage() != null) {
            if (!(getLanguage().equalsIgnoreCase(publishedTl.getLanguage()))) {
                myDiff = new TLDifference(getId(), publishedTl.getLanguage(), getLanguage());
            } else {
                if ((getStreet() != null) && !(getStreet().equalsIgnoreCase(publishedTl.getStreet()))) {
                    myDiff = new TLDifference(getId(), Tag.POSTAL_ADDRESS_STREET + " : " + publishedTl.getStreet(), Tag.POSTAL_ADDRESS_STREET + " : " + getStreet());
                }

                if ((getCountryCode() != null) && !(getCountryCode().equalsIgnoreCase(publishedTl.getCountryCode()))) {
                    myDiff = new TLDifference(getId(), Tag.POSTAL_ADDRESS_COUNTRY + " : " + publishedTl.getCountryCode(), Tag.POSTAL_ADDRESS_COUNTRY + " : " + getCountryCode());
                }

                if ((getLocality() != null) && !(getLocality().equalsIgnoreCase(publishedTl.getLocality()))) {
                    myDiff = new TLDifference(getId(), Tag.POSTAL_ADDRESS_LOCALITY + " : " + publishedTl.getLocality(), Tag.POSTAL_ADDRESS_LOCALITY + " : " + getLocality());
                }

                if ((getPostalCode() != null) && !(getPostalCode().equalsIgnoreCase(publishedTl.getPostalCode()))) {
                    myDiff = new TLDifference(getId(), Tag.POSTAL_ADDRESS_CODE + " : " + publishedTl.getPostalCode(), Tag.POSTAL_ADDRESS_CODE + " : " + getPostalCode());
                }

                if ((getStateOrProvince() != null) && !(getStateOrProvince().equalsIgnoreCase(publishedTl.getStateOrProvince()))) {
                    myDiff = new TLDifference(getId(), Tag.POSTAL_ADDRESS_PROVINCE + " : " + publishedTl.getStateOrProvince(), Tag.POSTAL_ADDRESS_PROVINCE + " : " + getStateOrProvince());
                }
            }
        }

        return myDiff;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((StateOrProvince == null) ? 0 : StateOrProvince.hashCode());
        result = (prime * result) + ((countryCode == null) ? 0 : countryCode.hashCode());
        result = (prime * result) + ((language == null) ? 0 : language.hashCode());
        result = (prime * result) + ((locality == null) ? 0 : locality.hashCode());
        result = (prime * result) + ((postalCode == null) ? 0 : postalCode.hashCode());
        result = (prime * result) + ((street == null) ? 0 : street.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TLPostalAddress other = (TLPostalAddress) obj;
        if (StateOrProvince == null) {
            if (other.StateOrProvince != null) {
                return false;
            }
        } else if (!StateOrProvince.equals(other.StateOrProvince)) {
            return false;
        }
        if (countryCode == null) {
            if (other.countryCode != null) {
                return false;
            }
        } else if (!countryCode.equals(other.countryCode)) {
            return false;
        }
        if (language == null) {
            if (other.language != null) {
                return false;
            }
        } else if (!language.equals(other.language)) {
            return false;
        }
        if (locality == null) {
            if (other.locality != null) {
                return false;
            }
        } else if (!locality.equals(other.locality)) {
            return false;
        }
        if (postalCode == null) {
            if (other.postalCode != null) {
                return false;
            }
        } else if (!postalCode.equals(other.postalCode)) {
            return false;
        }
        if (street == null) {
            if (other.street != null) {
                return false;
            }
        } else if (!street.equals(other.street)) {
            return false;
        }
        return true;
    }

    /*
     * GETTERS AND SETTERS
     */
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getStateOrProvince() {
        return StateOrProvince;
    }

    public void setStateOrProvince(String stateOrProvince) {
        StateOrProvince = stateOrProvince;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
