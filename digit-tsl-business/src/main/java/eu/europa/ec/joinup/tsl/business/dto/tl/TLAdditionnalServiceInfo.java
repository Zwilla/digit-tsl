package eu.europa.ec.joinup.tsl.business.dto.tl;

import eu.europa.esig.jaxb.tsl.AdditionalServiceInformationType;
import eu.europa.esig.jaxb.v5.tsl.AdditionalServiceInformationTypeV5;
import eu.europa.esig.jaxb.v5.tsl.NonEmptyMultiLangURITypeV5;

public class TLAdditionnalServiceInfo extends TLGeneric {

    public TLAdditionnalServiceInfo() {
    }

    public TLAdditionnalServiceInfo(int iddb, String location, AdditionalServiceInformationType asit) {
        super(iddb, location, asit.getURI().getLang(), asit.getURI().getValue());
    }

    public TLAdditionnalServiceInfo(int iddb, String location, AdditionalServiceInformationTypeV5 asit) {
        super(iddb, location, asit.getURI().getLang(), asit.getURI().getValue());
    }

    public AdditionalServiceInformationTypeV5 asTSLTypeV5() {
        AdditionalServiceInformationTypeV5 asi = new AdditionalServiceInformationTypeV5();
        NonEmptyMultiLangURITypeV5 uriType = new NonEmptyMultiLangURITypeV5();
        uriType.setLang(getLanguage());
        uriType.setValue(getValue());
        asi.setURI(uriType);

        return asi;
    }

}
