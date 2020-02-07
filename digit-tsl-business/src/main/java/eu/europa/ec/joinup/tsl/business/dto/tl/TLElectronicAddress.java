package eu.europa.ec.joinup.tsl.business.dto.tl;

import org.apache.commons.lang3.StringUtils;

import eu.europa.esig.jaxb.tsl.NonEmptyMultiLangURIType;
import eu.europa.esig.jaxb.v5.tsl.NonEmptyMultiLangURITypeV5;

public class TLElectronicAddress extends TLGeneric {

    public TLElectronicAddress() {
    }

    public TLElectronicAddress(int iddb, String location, NonEmptyMultiLangURIType tslElecAdr) {
        super(iddb, location, tslElecAdr);
    }

    public TLElectronicAddress(int iddb, String location, NonEmptyMultiLangURITypeV5 tslElecAdr) {
        super(iddb, location, tslElecAdr);
    }

    public NonEmptyMultiLangURITypeV5 asTSLTypeV5() {
        NonEmptyMultiLangURITypeV5 nonEmptyUri = new NonEmptyMultiLangURITypeV5();
        if (!StringUtils.isEmpty(getLanguage())) {
            nonEmptyUri.setLang(getLanguage());
        }
        if (!StringUtils.isEmpty(getValue())) {
            nonEmptyUri.setValue(getValue());
        }
        return nonEmptyUri;
    }

}
