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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.apache.commons.collections.CollectionUtils;

import eu.europa.ec.joinup.tsl.model.enums.Tag;
import eu.europa.esig.jaxb.tslx.CertSubjectDNAttributeType;
import eu.europa.esig.jaxb.tslx.ExtendedKeyUsageType;
import eu.europa.esig.jaxb.v5.tsl.AnyTypeV5;
import eu.europa.esig.jaxb.v5.tslx.CertSubjectDNAttributeTypeV5;
import eu.europa.esig.jaxb.v5.tslx.ExtendedKeyUsageTypeV5;
import eu.europa.esig.jaxb.v5.xades.ObjectIdentifierTypeV5;
import eu.europa.esig.jaxb.xades.ObjectIdentifierType;

public class TLOtherCriteria extends AbstractTLDTO {

	private List<TLPoliciesBit> extendedKeyUsageList;
	private List<TLPoliciesBit> certDnaList;

	public TLOtherCriteria() {
	}

	@SuppressWarnings("rawtypes")
	public TLOtherCriteria(int iddb, String location, eu.europa.esig.jaxb.xades.AnyType anyType) {
		super(iddb, location);
		int i = 0;
		int j = 0;
		if (anyType.getContent() != null) {
			List<TLPoliciesBit> extended = new ArrayList<TLPoliciesBit>();
			List<TLPoliciesBit> certDna = new ArrayList<TLPoliciesBit>();
			for (Object obj : anyType.getContent()) {
				if (obj instanceof JAXBElement) {
					JAXBElement obj2 = (JAXBElement) obj;
					if (obj2.getValue() instanceof CertSubjectDNAttributeType) {
						i++;
						CertSubjectDNAttributeType csda = (CertSubjectDNAttributeType) obj2.getValue();
						for (ObjectIdentifierType oit : csda.getAttributeOID()) {
							certDna.add(new TLPoliciesBit(iddb, getId() + "_" + Tag.POLICIES_BIT + "_" + i, oit));
						}

					} else if (obj2.getValue() instanceof ExtendedKeyUsageType) {
						j++;
						ExtendedKeyUsageType ekyt = (ExtendedKeyUsageType) obj2.getValue();
						for (ObjectIdentifierType oit : ekyt.getKeyPurposeId()) {
							extended.add(new TLPoliciesBit(iddb, getId() + "_" + Tag.POLICIES_BIT + "_" + j, oit));
						}

					}
				}
			}

			if (CollectionUtils.isNotEmpty(extended)) {
				this.setExtendedKeyUsageList(extended);
			}

			if (CollectionUtils.isNotEmpty(certDna)) {
				this.setCertDnaList(certDna);
			}

		}

	}

	@SuppressWarnings("rawtypes")
	public TLOtherCriteria(int iddb, String location, eu.europa.esig.jaxb.v5.xades.AnyTypeV5 at) {
		super(iddb, location);
		int i = 0;
		int j = 0;
		if (at.getContent() != null) {
			List<TLPoliciesBit> extended = new ArrayList<TLPoliciesBit>();
			List<TLPoliciesBit> certDna = new ArrayList<TLPoliciesBit>();
			for (Object obj : at.getContent()) {
				if (obj instanceof JAXBElement) {
					JAXBElement obj2 = (JAXBElement) obj;
					if (obj2.getValue() instanceof CertSubjectDNAttributeTypeV5) {
						i++;
						CertSubjectDNAttributeTypeV5 csda = (CertSubjectDNAttributeTypeV5) obj2.getValue();
						for (ObjectIdentifierTypeV5 oit : csda.getAttributeOID()) {
							certDna.add(new TLPoliciesBit(iddb, getId() + "_" + Tag.POLICIES_BIT + "_" + i, oit));
						}

					} else if (obj2.getValue() instanceof ExtendedKeyUsageTypeV5) {
						j++;
						ExtendedKeyUsageTypeV5 ekyt = (ExtendedKeyUsageTypeV5) obj2.getValue();
						for (ObjectIdentifierTypeV5 oit : ekyt.getKeyPurposeId()) {
							extended.add(new TLPoliciesBit(iddb, getId() + "_" + Tag.POLICIES_BIT + "_" + j, oit));
						}

					}
				}
			}

			if (CollectionUtils.isNotEmpty(extended)) {
				this.setExtendedKeyUsageList(extended);
			}

			if (CollectionUtils.isNotEmpty(certDna)) {
				this.setCertDnaList(certDna);
			}

		}

	}

	public AnyTypeV5 asTSLTypeV5() {
		AnyTypeV5 at = new AnyTypeV5();

		if (CollectionUtils.isNotEmpty(this.getCertDnaList())) {
			CertSubjectDNAttributeTypeV5 csDNAt = new CertSubjectDNAttributeTypeV5();
			for (TLPoliciesBit pb : this.getCertDnaList()) {
				csDNAt.getAttributeOID().add(pb.asTSLTypeV5());
			}
			at.getContent().add(new JAXBElement<CertSubjectDNAttributeTypeV5>(new QName("http://uri.etsi.org/02231/v2/additionaltypes#", "CertSubjectDNAttribute"),
					CertSubjectDNAttributeTypeV5.class, csDNAt));
		}

		if (CollectionUtils.isNotEmpty(this.getExtendedKeyUsageList())) {
			ExtendedKeyUsageTypeV5 ekut = new ExtendedKeyUsageTypeV5();
			for (TLPoliciesBit pb : this.getExtendedKeyUsageList()) {
				ekut.getKeyPurposeId().add(pb.asTSLTypeV5());
			}
			at.getContent().add(
					new JAXBElement<ExtendedKeyUsageTypeV5>(new QName("http://uri.etsi.org/02231/v2/additionaltypes#", "ExtendedKeyUsage"), ExtendedKeyUsageTypeV5.class, ekut));
		}

		return at;
	}

	public List<TLPoliciesBit> getExtendedKeyUsageList() {
		return extendedKeyUsageList;
	}

	public void setExtendedKeyUsageList(List<TLPoliciesBit> extendedKeyUsageList) {
		this.extendedKeyUsageList = extendedKeyUsageList;
	}

	public List<TLPoliciesBit> getCertDnaList() {
		return certDnaList;
	}

	public void setCertDnaList(List<TLPoliciesBit> certDnaList) {
		this.certDnaList = certDnaList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((certDnaList == null) ? 0 : certDnaList.hashCode());
		result = (prime * result) + ((extendedKeyUsageList == null) ? 0 : extendedKeyUsageList.hashCode());
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
		TLOtherCriteria other = (TLOtherCriteria) obj;
		if (certDnaList == null) {
			if (other.certDnaList != null) {
				return false;
			}
		} else if (!certDnaList.equals(other.certDnaList)) {
			return false;
		}
		if (extendedKeyUsageList == null) {
			if (other.extendedKeyUsageList != null) {
				return false;
			}
		} else if (!extendedKeyUsageList.equals(other.extendedKeyUsageList)) {
			return false;
		}
		return true;
	}

}
