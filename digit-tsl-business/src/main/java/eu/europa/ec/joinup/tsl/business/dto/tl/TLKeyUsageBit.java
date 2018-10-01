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

import eu.europa.esig.jaxb.ecc.KeyUsageBitType;
import eu.europa.esig.jaxb.v5.ecc.KeyUsageBitTypeV5;

public class TLKeyUsageBit extends AbstractTLDTO {

	String value;
	Boolean isValue;

	public TLKeyUsageBit() {
	}

	public TLKeyUsageBit(int iddb, String location, KeyUsageBitType obj) {
		super(iddb, location);
		this.setValue(obj.getName());
		this.setIsValue(obj.isValue());
	}

	public TLKeyUsageBit(int iddb, String location, KeyUsageBitTypeV5 obj) {
		super(iddb, location);
		this.setValue(obj.getName());
		this.setIsValue(obj.isValue());
	}

	public KeyUsageBitTypeV5 asTSLTypeV5() {
		KeyUsageBitTypeV5 kubt = new KeyUsageBitTypeV5();
		kubt.setName(this.getValue());
		kubt.setValue(this.isValue);
		return kubt;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Boolean getIsValue() {
		return isValue;
	}

	public void setIsValue(Boolean isValue) {
		this.isValue = isValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((isValue == null) ? 0 : isValue.hashCode());
		result = (prime * result) + ((value == null) ? 0 : value.hashCode());
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
		TLKeyUsageBit other = (TLKeyUsageBit) obj;
		if (isValue == null) {
			if (other.isValue != null) {
				return false;
			}
		} else if (!isValue.equals(other.isValue)) {
			return false;
		}
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}


}
