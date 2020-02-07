package eu.europa.ec.joinup.tsl.business.dto.tl;

import eu.europa.esig.jaxb.ecc.KeyUsageBitType;
import eu.europa.esig.jaxb.v5.ecc.KeyUsageBitTypeV5;

public class TLKeyUsageBit extends AbstractTLDTO {

    String value;
    boolean isValue;

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

    public boolean getIsValue() {
        return isValue;
    }

    public void setIsValue(boolean isValue) {
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
