package eu.europa.ec.joinup.tsl.business.dto.tl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.apache.commons.collections.CollectionUtils;

import eu.europa.ec.joinup.tsl.model.enums.Tag;
import eu.europa.esig.jaxb.ecc.CriteriaListType;
import eu.europa.esig.jaxb.ecc.KeyUsageType;
import eu.europa.esig.jaxb.ecc.PoliciesListType;
import eu.europa.esig.jaxb.v5.ecc.CriteriaListTypeV5;
import eu.europa.esig.jaxb.v5.ecc.KeyUsageTypeV5;
import eu.europa.esig.jaxb.v5.ecc.PoliciesListTypeV5;
import eu.europa.esig.jaxb.v5.tslx.CertSubjectDNAttributeTypeV5;
import eu.europa.esig.jaxb.v5.tslx.ExtendedKeyUsageTypeV5;

public class TLCriteria extends AbstractTLDTO {

    private String asserts;
    private String description;

    private List<TLKeyUsage> keyUsage;
    private List<TLPolicies> policyList;

    private List<TLCriteria> criteriaList;

    private TLOtherCriteria otherList;

    public TLCriteria() {
    }

    public TLCriteria(int iddb, String location, CriteriaListType clt) {
        super(iddb, location);
        setAsserts(clt.getAssert());
        setDescription(clt.getDescription());

        int j = 0;
        if ((clt.getKeyUsage() != null) && (clt.getKeyUsage().size() > 0)) {
            List<TLKeyUsage> listKey = new ArrayList<>();
            for (KeyUsageType kut : clt.getKeyUsage()) {
                listKey.add(new TLKeyUsage(iddb, getId() + "_" + Tag.KEY_USAGE + "_" + j, kut));
                j++;
            }
            setKeyUsage(listKey);
        }

        List<TLCriteria> tlCrit = new ArrayList<>();
        if ((clt.getCriteriaList() != null) && (clt.getCriteriaList().size() > 0)) {
            int i = 0;
            for (CriteriaListType clt2 : clt.getCriteriaList()) {
                i++;
                tlCrit.add(new TLCriteria(iddb, getId() + "_" + Tag.CRITERIA + "_" + i, clt2));
            }
            setCriteriaList(tlCrit);
        }

        int i = 0;
        if ((clt.getPolicySet() != null) && (clt.getPolicySet().size() > 0)) {
            List<TLPolicies> listPol = new ArrayList<>();
            for (PoliciesListType pol : clt.getPolicySet()) {
                listPol.add(new TLPolicies(iddb, getId() + "_" + Tag.POLICIES + "_" + i, pol));
                i++;
            }
            setPolicyList(listPol);
        }

        if (clt.getOtherCriteriaList() != null) {
            setOtherList(new TLOtherCriteria(iddb, getId() + Tag.OTHER_CRITERIA, clt.getOtherCriteriaList()));
        }
    }

    public TLCriteria(int iddb, String location, CriteriaListTypeV5 clt) {
        super(iddb, location);
        setAsserts(clt.getAssert());
        setDescription(clt.getDescription());
        int j = 0;
        List<TLKeyUsage> listKey = new ArrayList<>();
        for (KeyUsageTypeV5 kut : clt.getKeyUsage()) {
            listKey.add(new TLKeyUsage(iddb, getId() + "_" + Tag.KEY_USAGE + "_" + j, kut));
            j++;

        }
        setKeyUsage(listKey);

        List<TLCriteria> tlCrit = new ArrayList<>();
        if ((clt.getCriteriaList() != null) && (clt.getCriteriaList().size() > 0)) {
            int i = 0;
            for (CriteriaListTypeV5 clt2 : clt.getCriteriaList()) {
                i++;
                tlCrit.add(new TLCriteria(iddb, getId() + "_" + Tag.CRITERIA + "_" + i, clt2));
            }
        }
        setCriteriaList(tlCrit);

        List<TLPolicies> listPol = new ArrayList<>();
        if ((clt.getPolicySet() != null) && (clt.getPolicySet().size() > 0)) {
            int i = 0;
            for (PoliciesListTypeV5 pol : clt.getPolicySet()) {
                listPol.add(new TLPolicies(iddb, getId() + "_" + Tag.POLICIES + "_" + i, pol));
                i++;
            }
        }
        setPolicyList(listPol);

        if (clt.getOtherCriteriaList() != null) {
            setOtherList(new TLOtherCriteria(iddb, getId() + Tag.OTHER_CRITERIA, clt.getOtherCriteriaList()));
        }
    }

    public CriteriaListTypeV5 asTSLTypeV5() {
        CriteriaListTypeV5 clt = new CriteriaListTypeV5();
        clt.setAssert(getAsserts());
        clt.setDescription(getDescription());

        if ((getKeyUsage() != null) && CollectionUtils.isNotEmpty(getKeyUsage())) {
            for (TLKeyUsage tlKey : getKeyUsage()) {
                clt.getKeyUsage().add(tlKey.asTSLTypeV5());
            }
        }

        if ((getPolicyList() != null) & CollectionUtils.isNotEmpty(getPolicyList())) {
            for (TLPolicies tlPolicy : getPolicyList()) {
                clt.getPolicySet().add(tlPolicy.asTSLTypeV5());
            }
        }

        if ((getCriteriaList() != null) && CollectionUtils.isNotEmpty(getCriteriaList())) {
            for (TLCriteria tlCriteria : getCriteriaList()) {
                clt.getCriteriaList().add(tlCriteria.asTSLTypeV5());
            }
        }

        if (getOtherList() != null) {

            if (CollectionUtils.isNotEmpty(getOtherList().getCertDnaList())) {
                CertSubjectDNAttributeTypeV5 csDNAt = new CertSubjectDNAttributeTypeV5();
                for (TLPoliciesBit pb : getOtherList().getCertDnaList()) {
                    csDNAt.getAttributeOID().add(pb.asTSLTypeV5());
                }
                if (clt.getOtherCriteriaList() == null) {
                    clt.setOtherCriteriaList(new eu.europa.esig.jaxb.v5.xades.AnyTypeV5());
                }
                clt.getOtherCriteriaList().getContent()
                        .add(new JAXBElement<>(new QName("https://uri.etsi.org/02231/v2/additionaltypes#", "CertSubjectDNAttribute"), CertSubjectDNAttributeTypeV5.class, csDNAt));
            }

            if (CollectionUtils.isNotEmpty(getOtherList().getExtendedKeyUsageList())) {
                ExtendedKeyUsageTypeV5 ekut = new ExtendedKeyUsageTypeV5();
                for (TLPoliciesBit pb : getOtherList().getExtendedKeyUsageList()) {
                    ekut.getKeyPurposeId().add(pb.asTSLTypeV5());
                }
                if (clt.getOtherCriteriaList() == null) {
                    clt.setOtherCriteriaList(new eu.europa.esig.jaxb.v5.xades.AnyTypeV5());
                }
                clt.getOtherCriteriaList().getContent().add(new JAXBElement<>(new QName("https://uri.etsi.org/02231/v2/additionaltypes#", "ExtendedKeyUsage"), ExtendedKeyUsageTypeV5.class, ekut));
            }
        }
        return clt;
    }

    public String getAsserts() {
        return asserts;
    }

    public void setAsserts(String asserts) {
        this.asserts = asserts;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TLKeyUsage> getKeyUsage() {
        return keyUsage;
    }

    public void setKeyUsage(List<TLKeyUsage> keyUsage) {
        this.keyUsage = keyUsage;
    }

    public List<TLCriteria> getCriteriaList() {
        if (CollectionUtils.isEmpty(criteriaList)) {
            return null;
        }
        return criteriaList;
    }

    public void setCriteriaList(List<TLCriteria> criteriaList) {
        this.criteriaList = criteriaList;
    }

    public List<TLPolicies> getPolicyList() {
        if (CollectionUtils.isEmpty(policyList)) {
            return null;
        }
        return policyList;
    }

    public void setPolicyList(List<TLPolicies> policyList) {
        this.policyList = policyList;
    }

    public TLOtherCriteria getOtherList() {
        return otherList;
    }

    public void setOtherList(TLOtherCriteria otherList) {
        this.otherList = otherList;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((asserts == null) ? 0 : asserts.hashCode());
        result = (prime * result) + ((criteriaList == null) ? 0 : criteriaList.hashCode());
        result = (prime * result) + ((description == null) ? 0 : description.hashCode());
        result = (prime * result) + ((keyUsage == null) ? 0 : keyUsage.hashCode());
        result = (prime * result) + ((otherList == null) ? 0 : otherList.hashCode());
        result = (prime * result) + ((policyList == null) ? 0 : policyList.hashCode());
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
        TLCriteria other = (TLCriteria) obj;
        if (asserts == null) {
            if (other.asserts != null) {
                return false;
            }
        } else if (!asserts.equals(other.asserts)) {
            return false;
        }
        if (criteriaList == null) {
            if (other.criteriaList != null) {
                return false;
            }
        } else if (!criteriaList.equals(other.criteriaList)) {
            return false;
        }
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (keyUsage == null) {
            if (other.keyUsage != null) {
                return false;
            }
        } else if (!keyUsage.equals(other.keyUsage)) {
            return false;
        }
        if (otherList == null) {
            if (other.otherList != null) {
                return false;
            }
        } else if (!otherList.equals(other.otherList)) {
            return false;
        }
        if (policyList == null) {
            if (other.policyList != null) {
                return false;
            }
        } else if (!policyList.equals(other.policyList)) {
            return false;
        }
        return true;
    }

}
