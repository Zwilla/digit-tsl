package eu.europa.ec.joinup.tsl.business.dto.tl;

import eu.europa.esig.jaxb.tsl.MultiLangNormStringType;
import eu.europa.esig.jaxb.tsl.NonEmptyMultiLangURIType;
import eu.europa.esig.jaxb.v5.tsl.MultiLangNormStringTypeV5;
import eu.europa.esig.jaxb.v5.tsl.NonEmptyMultiLangURITypeV5;

public class TLName extends TLGeneric {

    public TLName() {
    }

    public TLName(int iddb, String location, MultiLangNormStringType name) {
        super(iddb, location, name);
    }

    public TLName(int iddb, String location, NonEmptyMultiLangURIType name) {
        super(iddb, location, name);
    }

    public TLName(int iddb, String location, MultiLangNormStringTypeV5 name) {
        super(iddb, location, name);
    }

    public TLName(int iddb, String location, NonEmptyMultiLangURITypeV5 name) {
        super(iddb, location, name);
    }

    public MultiLangNormStringTypeV5 asTSLTypeV5() {
        MultiLangNormStringTypeV5 multiLang = new MultiLangNormStringTypeV5();
        multiLang.setLang(getLanguage());
        multiLang.setValue(getValue());
        return multiLang;
    }

    public NonEmptyMultiLangURITypeV5 asTSLTypeV5Non() {
        NonEmptyMultiLangURITypeV5 multiLang = new NonEmptyMultiLangURITypeV5();
        multiLang.setLang(getLanguage());
        multiLang.setValue(getValue());
        return multiLang;
    }

}
