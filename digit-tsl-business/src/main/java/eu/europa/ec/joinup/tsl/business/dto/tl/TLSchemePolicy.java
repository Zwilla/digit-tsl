package eu.europa.ec.joinup.tsl.business.dto.tl;

import eu.europa.esig.jaxb.tsl.MultiLangStringType;
import eu.europa.esig.jaxb.tsl.NonEmptyMultiLangURIType;
import eu.europa.esig.jaxb.v5.tsl.MultiLangStringTypeV5;
import eu.europa.esig.jaxb.v5.tsl.NonEmptyMultiLangURITypeV5;

public class TLSchemePolicy extends TLGeneric {

    public TLSchemePolicy() {
    }

    public TLSchemePolicy(int iddb, String location, NonEmptyMultiLangURIType uri) {
        super(iddb, location, uri);
    }

    public TLSchemePolicy(int iddb, String location, MultiLangStringType name) {
        super(iddb, location, name);
    }

    public TLSchemePolicy(int iddb, String location, NonEmptyMultiLangURITypeV5 uri) {
        super(iddb, location, uri);
    }

    public TLSchemePolicy(int iddb, String location, MultiLangStringTypeV5 name) {
        super(iddb, location, name);
    }

    public MultiLangStringTypeV5 asTSLTypeV5() {
        MultiLangStringTypeV5 multiString = new MultiLangStringTypeV5();
        multiString.setLang(this.getLanguage());
        multiString.setValue(this.getValue());
        return multiString;
    }

}
