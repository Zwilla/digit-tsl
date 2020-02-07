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
