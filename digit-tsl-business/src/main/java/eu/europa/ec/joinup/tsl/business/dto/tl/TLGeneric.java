package eu.europa.ec.joinup.tsl.business.dto.tl;

import org.apache.commons.lang3.StringUtils;

import eu.europa.ec.joinup.tsl.business.dto.TLDifference;
import eu.europa.esig.jaxb.tsl.MultiLangNormStringType;
import eu.europa.esig.jaxb.tsl.MultiLangStringType;
import eu.europa.esig.jaxb.tsl.NonEmptyMultiLangURIType;
import eu.europa.esig.jaxb.v5.tsl.MultiLangNormStringTypeV5;
import eu.europa.esig.jaxb.v5.tsl.MultiLangStringTypeV5;
import eu.europa.esig.jaxb.v5.tsl.NonEmptyMultiLangURITypeV5;

public abstract class TLGeneric extends AbstractTLDTO {

    private String language;
    private String value;

    public TLGeneric() {
    }

    protected TLGeneric(int ltId, String location, String language, String value) {
        super(ltId, location);
        this.language = language;
        this.value = value;
    }

    protected TLGeneric(int iddb, String location, NonEmptyMultiLangURIType name) {
        this(iddb, location + "_" + name.getLang(), name.getLang(), name.getValue());
    }

    protected TLGeneric(int iddb, String location, MultiLangStringType name) {
        this(iddb, location + "_" + name.getLang(), name.getLang(), name.getValue());
    }

    protected TLGeneric(int iddb, String location, MultiLangNormStringType name) {
        this(iddb, location + "_" + name.getLang(), name.getLang(), name.getValue());
    }

    protected TLGeneric(int iddb, String location, NonEmptyMultiLangURITypeV5 name) {
        this(iddb, location + "_" + name.getLang(), name.getLang(), name.getValue());
    }

    protected TLGeneric(int iddb, String location, MultiLangStringTypeV5 name) {
        this(iddb, location + "_" + name.getLang(), name.getLang(), name.getValue());
    }

    protected TLGeneric(int iddb, String location, MultiLangNormStringTypeV5 name) {
        this(iddb, location + "_" + name.getLang(), name.getLang(), name.getValue());
    }

    /*
     * GETTERS AND SETTERS
     */
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TLDifference asPublishedDiff(TLGeneric publishedTl) {
        TLDifference myDiff = null;
        if (StringUtils.isNotEmpty(publishedTl.getValue()) && !StringUtils.equals(getValue(), publishedTl.getValue())) {
            myDiff = new TLDifference(getId(), publishedTl.getLanguage() + " - " + publishedTl.getValue(), publishedTl.getLanguage() + " - " + getValue());
        }

        return myDiff;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((language == null) ? 0 : language.hashCode());
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
        TLGeneric other = (TLGeneric) obj;
        if (language == null) {
            if (other.language != null) {
                return false;
            }
        } else if (!language.equals(other.language)) {
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
