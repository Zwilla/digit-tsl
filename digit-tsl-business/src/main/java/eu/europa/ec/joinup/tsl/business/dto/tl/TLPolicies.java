package eu.europa.ec.joinup.tsl.business.dto.tl;

import java.util.ArrayList;
import java.util.List;

import eu.europa.ec.joinup.tsl.model.enums.Tag;
import eu.europa.esig.jaxb.ecc.PoliciesListType;
import eu.europa.esig.jaxb.v5.ecc.PoliciesListTypeV5;
import eu.europa.esig.jaxb.v5.xades.ObjectIdentifierTypeV5;
import eu.europa.esig.jaxb.xades.ObjectIdentifierType;

public class TLPolicies extends AbstractTLDTO {

    List<TLPoliciesBit> policyBit;

    public TLPolicies() {
    }

    public TLPolicies(int iddb, String location, PoliciesListType obj) {
        super(iddb, location);
        int i = 0;
        List<TLPoliciesBit> listPolBit = new ArrayList<>();
        for (ObjectIdentifierType pol : obj.getPolicyIdentifier()) {
            listPolBit.add(new TLPoliciesBit(iddb, getId() + "_" + Tag.POLICIES_BIT + "_" + i, pol));
            i++;
        }
        this.setPolicyBit(listPolBit);

    }

    public TLPolicies(int iddb, String location, PoliciesListTypeV5 obj) {
        super(iddb, location);
        int i = 0;
        List<TLPoliciesBit> listPolBit = new ArrayList<>();
        for (ObjectIdentifierTypeV5 pol : obj.getPolicyIdentifier()) {
            listPolBit.add(new TLPoliciesBit(iddb, getId() + "_" + Tag.POLICIES_BIT + "_" + i, pol));
            i++;
        }
        this.setPolicyBit(listPolBit);

    }

    public PoliciesListTypeV5 asTSLTypeV5() {
        PoliciesListTypeV5 plt = new PoliciesListTypeV5();

        for (TLPoliciesBit tlPolicy : this.getPolicyBit()) {
            plt.getPolicyIdentifier().add(tlPolicy.asTSLTypeV5());
        }
        return plt;
    }

    public List<TLPoliciesBit> getPolicyBit() {
        return policyBit;
    }

    public void setPolicyBit(List<TLPoliciesBit> policyBit) {
        this.policyBit = policyBit;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((policyBit == null) ? 0 : policyBit.hashCode());
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
        TLPolicies other = (TLPolicies) obj;
        if (policyBit == null) {
            if (other.policyBit != null) {
                return false;
            }
        } else if (!policyBit.equals(other.policyBit)) {
            return false;
        }
        return true;
    }

}
