package eu.europa.ec.joinup.tsl.business.dto.tl;

import eu.europa.esig.jaxb.tsl.NonEmptyMultiLangURIType;
import eu.europa.esig.jaxb.v5.tsl.NonEmptyMultiLangURITypeV5;

public class TLInformationUri extends TLGeneric {

    public TLInformationUri() {
    }

    public TLInformationUri(int iddb, String location, NonEmptyMultiLangURIType uri) {
        super(iddb, location, uri);
    }

    public TLInformationUri(int iddb, String location, NonEmptyMultiLangURITypeV5 uri) {
        super(iddb, location, uri);
    }

    public NonEmptyMultiLangURITypeV5 asTSLTypeV5() {
        NonEmptyMultiLangURITypeV5 multiUri = new NonEmptyMultiLangURITypeV5();
        multiUri.setLang(getLanguage());
        multiUri.setValue(getValue());
        return multiUri;
    }

}
