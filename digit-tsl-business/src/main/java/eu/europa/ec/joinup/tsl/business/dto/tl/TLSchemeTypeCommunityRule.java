package eu.europa.ec.joinup.tsl.business.dto.tl;

import eu.europa.esig.jaxb.tsl.NonEmptyMultiLangURIType;
import eu.europa.esig.jaxb.v5.tsl.NonEmptyMultiLangURITypeV5;

public class TLSchemeTypeCommunityRule extends TLGeneric {

    public TLSchemeTypeCommunityRule() {
    }

    public TLSchemeTypeCommunityRule(int iddb, String location, NonEmptyMultiLangURIType uriType) {
        super(iddb, location, uriType);
    }

    public TLSchemeTypeCommunityRule(int iddb, String location, NonEmptyMultiLangURITypeV5 uriType) {
        super(iddb, location, uriType);
    }

    public NonEmptyMultiLangURITypeV5 asTSLTypeV5() {
        NonEmptyMultiLangURITypeV5 multiUri = new NonEmptyMultiLangURITypeV5();
        multiUri.setLang(this.getLanguage());
        multiUri.setValue(this.getValue());
        return multiUri;
    }

}
