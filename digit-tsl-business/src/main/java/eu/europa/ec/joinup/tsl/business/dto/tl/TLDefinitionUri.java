package eu.europa.ec.joinup.tsl.business.dto.tl;

import eu.europa.esig.jaxb.tsl.NonEmptyMultiLangURIType;
import eu.europa.esig.jaxb.v5.tsl.NonEmptyMultiLangURITypeV5;

public class TLDefinitionUri extends TLGeneric {

    public TLDefinitionUri() {
    }

    public TLDefinitionUri(int iddb, String location, NonEmptyMultiLangURIType definitionUri) {
        super(iddb, location, definitionUri);
    }

    public TLDefinitionUri(int iddb, String location, NonEmptyMultiLangURITypeV5 definitionUri) {
        super(iddb, location, definitionUri);
    }

    public NonEmptyMultiLangURITypeV5 asTSLTypeV5() {
        NonEmptyMultiLangURITypeV5 multiUri = new NonEmptyMultiLangURITypeV5();
        multiUri.setLang(getLanguage());
        multiUri.setValue(getValue());
        return multiUri;
    }

}
