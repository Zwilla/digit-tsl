package eu.europa.ec.joinup.tsl.business.dto.tl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import eu.europa.ec.joinup.tsl.model.enums.Tag;
import eu.europa.esig.jaxb.ecc.CriteriaListType;
import eu.europa.esig.jaxb.ecc.QualificationElementType;
import eu.europa.esig.jaxb.ecc.QualifierType;
import eu.europa.esig.jaxb.v5.ecc.CriteriaListTypeV5;
import eu.europa.esig.jaxb.v5.ecc.QualificationElementTypeV5;
import eu.europa.esig.jaxb.v5.ecc.QualifierTypeV5;
import eu.europa.esig.jaxb.v5.ecc.QualifiersTypeV5;

public class TLQualificationExtension extends AbstractTLDTO {

    private List<String> qualifTypeList;
    private TLCriteria criteria;

    public TLQualificationExtension() {
    }

    public TLQualificationExtension(int iddb, String location, QualificationElementType qet) {
        super(iddb, location);
        List<String> qualifTypeList = new ArrayList<>();

        for (QualifierType qualifType : qet.getQualifiers().getQualifier()) {
            qualifTypeList.add(qualifType.getUri());
        }
        setQualifTypeList(qualifTypeList);

        CriteriaListType clt = qet.getCriteriaList();
        setCriteria(new TLCriteria(iddb, getId() + "_" + Tag.CRITERIA + "_", clt));

    }

    public TLQualificationExtension(int iddb, String location, QualificationElementTypeV5 qet) {
        super(iddb, location);
        List<String> qualifTypeList = new ArrayList<>();

        for (QualifierTypeV5 qualifType : qet.getQualifiers().getQualifier()) {
            qualifTypeList.add(qualifType.getUri());
        }
        setQualifTypeList(qualifTypeList);

        CriteriaListTypeV5 clt = qet.getCriteriaList();
        setCriteria(new TLCriteria(iddb, getId() + "_" + Tag.CRITERIA + "_", clt));

    }

    public QualificationElementTypeV5 asTSLTypeV5() {
        QualificationElementTypeV5 qet = new QualificationElementTypeV5();
        CriteriaListTypeV5 extClt = new CriteriaListTypeV5();

        if (getCriteria() != null) {
            extClt = getCriteria().asTSLTypeV5();
        }

        if (getQualifTypeList() != null) {
            if (qet.getQualifiers() == null) {
                qet.setQualifiers(new QualifiersTypeV5());
            }
            for (String str : getQualifTypeList()) {
                QualifierTypeV5 qt = new QualifierTypeV5();
                qt.setUri(str);
                qet.getQualifiers().getQualifier().add(qt);
            }
        }

        qet.setCriteriaList(extClt);
        return qet;
    }

    public void toV5FromV4() {
        if (getQualifTypeList() != null) {
            List<String> migratedQualifType = new ArrayList<>();
            for (int i = 0; i < getQualifTypeList().size(); i++) {
                String tmpType = StringUtils.trimToEmpty(getQualifTypeList().get(i));
                switch (tmpType) {
                case "https://uri.etsi.org/TrstSvc/TrustedList/SvcInfoExt/QCWithSSCD":
                    migratedQualifType.add("https://uri.etsi.org/TrstSvc/TrustedList/SvcInfoExt/QCWithQSCD");
                    break;
                case "https://uri.etsi.org/TrstSvc/TrustedList/SvcInfoExt/QCNoSSCD":
                    migratedQualifType.add("https://uri.etsi.org/TrstSvc/TrustedList/SvcInfoExt/QCNoQSCD");
                    break;
                case "https://uri.etsi.org/TrstSvc/TrustedList/SvcInfoExt/QCSSCDStatusAsInCert":
                    migratedQualifType.add("https://uri.etsi.org/TrstSvc/TrustedList/SvcInfoExt/QCQSCDStatusAsInCert");
                    break;
                case "https://uri.etsi.org/TrstSvc/TrustedList/SvcInfoExt/QCStatement":
                    migratedQualifType.add("https://uri.etsi.org/TrstSvc/TrustedList/SvcInfoExt/QCStatement");
                    migratedQualifType.add("https://uri.etsi.org/TrstSvc/TrustedList/SvcInfoExt/QCForESig");
                    break;
                case "https://uri.etsi.org/TrstSvc/TrustedList/SvcInfoExt/QCForLegalPerson":
                    migratedQualifType.add("https://uri.etsi.org/TrstSvc/TrustedList/SvcInfoExt/NotQualified");
                    break;

                default:
                    migratedQualifType.add(tmpType);
                    break;
                }
            }
            setQualifTypeList(migratedQualifType);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((criteria == null) ? 0 : criteria.hashCode());
        result = (prime * result) + ((qualifTypeList == null) ? 0 : qualifTypeList.hashCode());
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
        TLQualificationExtension other = (TLQualificationExtension) obj;
        if (criteria == null) {
            if (other.criteria != null) {
                return false;
            }
        } else if (!criteria.equals(other.criteria)) {
            return false;
        }
        if (qualifTypeList == null) {
            if (other.qualifTypeList != null) {
                return false;
            }
        } else if (!qualifTypeList.equals(other.qualifTypeList)) {
            return false;
        }
        return true;
    }

    public List<String> getQualifTypeList() {
        return qualifTypeList;
    }

    public void setQualifTypeList(List<String> qualifTypeList) {
        this.qualifTypeList = qualifTypeList;
    }

    public TLCriteria getCriteria() {
        return criteria;
    }

    public void setCriteria(TLCriteria criteria) {
        this.criteria = criteria;
    }

}
