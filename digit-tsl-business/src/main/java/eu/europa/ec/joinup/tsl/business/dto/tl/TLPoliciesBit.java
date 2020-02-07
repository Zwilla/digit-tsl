package eu.europa.ec.joinup.tsl.business.dto.tl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import eu.europa.esig.jaxb.v5.xades.DocumentationReferencesTypeV5;
import eu.europa.esig.jaxb.v5.xades.IdentifierTypeV5;
import eu.europa.esig.jaxb.v5.xades.ObjectIdentifierTypeV5;
import eu.europa.esig.jaxb.v5.xades.QualifierTypeV5;
import eu.europa.esig.jaxb.xades.ObjectIdentifierType;

public class TLPoliciesBit extends AbstractTLDTO {

    String description;
    String identifierValue;
    String identifierType;
    List<String> documentationReferences;

    public TLPoliciesBit() {
    }

    public TLPoliciesBit(int iddb, String location, ObjectIdentifierType obj) {
        super(iddb, location);
        setDescription(obj.getDescription());
        setIdentifierValue(obj.getIdentifier().getValue());
        if (obj.getIdentifier().getQualifier() != null) {
            setIdentifierType(obj.getIdentifier().getQualifier().value());
        }

        if ((obj.getDocumentationReferences() != null) && (obj.getDocumentationReferences().getDocumentationReference() != null)
                && (obj.getDocumentationReferences().getDocumentationReference().size() > 0)) {
            setDocumentationReferences(obj.getDocumentationReferences().getDocumentationReference());
        }
    }

    public TLPoliciesBit(int iddb, String location, ObjectIdentifierTypeV5 obj) {
        super(iddb, location);
        setDescription(obj.getDescription());
        setIdentifierValue(obj.getIdentifier().getValue());
        if (obj.getIdentifier().getQualifier() != null) {
            setIdentifierType(obj.getIdentifier().getQualifier().value());
        }

        if ((obj.getDocumentationReferences() != null) && (obj.getDocumentationReferences().getDocumentationReference() != null)
                && (obj.getDocumentationReferences().getDocumentationReference().size() > 0)) {
            setDocumentationReferences(obj.getDocumentationReferences().getDocumentationReference());
        }
    }

    public ObjectIdentifierTypeV5 asTSLTypeV5() {
        ObjectIdentifierTypeV5 oit = new ObjectIdentifierTypeV5();
        if (StringUtils.isNotEmpty(description)) {
            oit.setDescription(description);
        }
        IdentifierTypeV5 it = new IdentifierTypeV5();
        it.setValue(getIdentifierValue());
        if (QualifierTypeV5.OID_AS_URI.value().equalsIgnoreCase(getIdentifierType())) {
            it.setQualifier(QualifierTypeV5.OID_AS_URI);
        } else if (QualifierTypeV5.OID_AS_URN.value().equalsIgnoreCase(getIdentifierType())) {
            it.setQualifier(QualifierTypeV5.OID_AS_URN);
        }
        oit.setIdentifier(it);
        if (!isDocumentationReferencesEmpty()) {
            DocumentationReferencesTypeV5 doc = new DocumentationReferencesTypeV5();
            for (String str : documentationReferences) {
                doc.getDocumentationReference().add(str);
            }
            oit.setDocumentationReferences(doc);
        }

        return oit;
    }

    private boolean isDocumentationReferencesEmpty() {
        if (CollectionUtils.isEmpty(documentationReferences)) {
            return true;
        } else {
            int nbEmpty = 0;
            for (String string : documentationReferences) {
                if (StringUtils.isEmpty(string)) {
                    nbEmpty = nbEmpty + 1;
                }
            }
            return nbEmpty == documentationReferences.size();
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdentifierValue() {
        return identifierValue;
    }

    public void setIdentifierValue(String identifierValue) {
        this.identifierValue = identifierValue;
    }

    public List<String> getDocumentationReferences() {
        return documentationReferences;
    }

    public void setDocumentationReferences(List<String> documentationReferences) {
        this.documentationReferences = documentationReferences;
    }

    public String getIdentifierType() {
        return identifierType;
    }

    public void setIdentifierType(String identifierType) {
        this.identifierType = identifierType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((description == null) ? 0 : description.hashCode());
        result = (prime * result) + ((documentationReferences == null) ? 0 : documentationReferences.hashCode());
        result = (prime * result) + ((identifierType == null) ? 0 : identifierType.hashCode());
        result = (prime * result) + ((identifierValue == null) ? 0 : identifierValue.hashCode());
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
        TLPoliciesBit other = (TLPoliciesBit) obj;
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (documentationReferences == null) {
            if (other.documentationReferences != null) {
                return false;
            }
        } else if (!documentationReferences.equals(other.documentationReferences)) {
            return false;
        }
        if (identifierType == null) {
            if (other.identifierType != null) {
                return false;
            }
        } else if (!identifierType.equals(other.identifierType)) {
            return false;
        }
        if (identifierValue == null) {
            if (other.identifierValue != null) {
                return false;
            }
        } else if (!identifierValue.equals(other.identifierValue)) {
            return false;
        }
        return true;
    }

}
