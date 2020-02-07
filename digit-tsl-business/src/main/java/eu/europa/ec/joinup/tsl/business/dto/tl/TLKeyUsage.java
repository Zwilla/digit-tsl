package eu.europa.ec.joinup.tsl.business.dto.tl;

import java.util.ArrayList;
import java.util.List;

import eu.europa.ec.joinup.tsl.model.enums.Tag;
import eu.europa.esig.jaxb.ecc.KeyUsageBitType;
import eu.europa.esig.jaxb.ecc.KeyUsageType;
import eu.europa.esig.jaxb.v5.ecc.KeyUsageBitTypeV5;
import eu.europa.esig.jaxb.v5.ecc.KeyUsageTypeV5;

public class TLKeyUsage extends AbstractTLDTO {

    private List<TLKeyUsageBit> keyUsageBit;

    public TLKeyUsage() {
    }

    public TLKeyUsage(int iddb, String location, KeyUsageType obj) {
        super(iddb, location);
        List<TLKeyUsageBit> listKeyBit = new ArrayList<>();
        int j = 0;
        for (KeyUsageBitType kubt : obj.getKeyUsageBit()) {

            listKeyBit.add(new TLKeyUsageBit(iddb, getId() + "_" + Tag.KEY_USAGE_BIT + "_" + j, kubt));
        }
        this.setKeyUsageBit(listKeyBit);
    }

    public TLKeyUsage(int iddb, String location, KeyUsageTypeV5 obj) {
        super(iddb, location);
        List<TLKeyUsageBit> listKeyBit = new ArrayList<>();
        int j = 0;
        for (KeyUsageBitTypeV5 kubt : obj.getKeyUsageBit()) {

            listKeyBit.add(new TLKeyUsageBit(iddb, getId() + "_" + Tag.KEY_USAGE_BIT + "_" + j, kubt));
        }
        this.setKeyUsageBit(listKeyBit);
    }

    public KeyUsageTypeV5 asTSLTypeV5() {
        KeyUsageTypeV5 kut = new KeyUsageTypeV5();
        for (TLKeyUsageBit keyUsage : this.getKeyUsageBit()) {
            if (keyUsage.getIsValue() != null) {
                kut.getKeyUsageBit().add(keyUsage.asTSLTypeV5());
            }
        }

        return kut;
    }

    public List<TLKeyUsageBit> getKeyUsageBit() {
        return keyUsageBit;
    }

    public void setKeyUsageBit(List<TLKeyUsageBit> keyUsageBit) {
        this.keyUsageBit = keyUsageBit;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((keyUsageBit == null) ? 0 : keyUsageBit.hashCode());
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
        TLKeyUsage other = (TLKeyUsage) obj;
        if (keyUsageBit == null) {
            if (other.keyUsageBit != null) {
                return false;
            }
        } else if (!keyUsageBit.equals(other.keyUsageBit)) {
            return false;
        }
        return true;
    }

}
