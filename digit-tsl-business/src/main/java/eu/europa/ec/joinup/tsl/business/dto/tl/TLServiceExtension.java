package eu.europa.ec.joinup.tsl.business.dto.tl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import eu.europa.ec.joinup.tsl.business.util.AnyTypeUtils;
import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.ec.joinup.tsl.model.enums.Tag;
import eu.europa.esig.jaxb.ecc.QualificationElementType;
import eu.europa.esig.jaxb.ecc.QualificationsType;
import eu.europa.esig.jaxb.tsl.AdditionalServiceInformationType;
import eu.europa.esig.jaxb.tsl.AnyType;
import eu.europa.esig.jaxb.tsl.ExtensionType;
import eu.europa.esig.jaxb.tslx.TakenOverByType;
import eu.europa.esig.jaxb.v5.ecc.QualificationElementTypeV5;
import eu.europa.esig.jaxb.v5.ecc.QualificationsTypeV5;
import eu.europa.esig.jaxb.v5.tsl.AdditionalServiceInformationTypeV5;
import eu.europa.esig.jaxb.v5.tsl.ExtensionTypeV5;
import eu.europa.esig.jaxb.v5.tslx.TakenOverByTypeV5;
import eu.europa.esig.jaxb.v5.utils.JaxbGregorianCalendarZulu;

public class TLServiceExtension extends AbstractTLDTO {

    private static final Logger LOGGER = LoggerFactory.getLogger(TLServiceExtension.class);

    private boolean critical;
    private TLTakenOverBy takenOverBy;
    private TLAdditionnalServiceInfo additionnalServiceInfo;
    private List<TLQualificationExtension> qualificationsExtension;
    private Date expiredCertsRevocationDate;
    private String anyType;

    public TLServiceExtension() {
    }

    public TLServiceExtension(int iddb, String location) {
        super(iddb, location);
    }

    @SuppressWarnings("rawtypes")
    public TLServiceExtension(int iddb, String location, ExtensionType ext) {
        super(iddb, location);
        setCritical(ext.isCritical());
        for (Object obj : ext.getContent()) {
            if (obj instanceof JAXBElement) {
                JAXBElement obj2 = (JAXBElement) obj;
                filterType(obj2);
            } else if (obj instanceof Element) {
                Element myObj = (Element) obj;
                try {
                    JAXBContext ctx = JAXBContext.newInstance(TakenOverByType.class, AdditionalServiceInformationType.class, QualificationElementType.class, AnyType.class);
                    Object obj2 = ctx.createUnmarshaller().unmarshal(myObj);
                    filterType(obj2);
                } catch (Exception e) {
                    String tmpTag = AnyTypeUtils.convertOtherTag(myObj);
                    if (tmpTag == null) {
                        LOGGER.error("TLServiceExtension - can't parse ExtensionType", e);
                    } else {
                        processAnyType(tmpTag);
                    }
                }
            } else if (obj instanceof String) {
                processAnyType((String) obj);
            }
        }
    }

    @SuppressWarnings("rawtypes")
    public TLServiceExtension(int iddb, String location, ExtensionTypeV5 ext) {
        super(iddb, location);
        setCritical(ext.isCritical());
        for (Object obj : ext.getContent()) {
            if (obj instanceof JAXBElement) {
                JAXBElement obj2 = (JAXBElement) obj;
                filterTypeV5(obj2);
            } else if (obj instanceof Element) {
                Element myObj = (Element) obj;
                try {
                    JAXBContext ctx = JAXBContext.newInstance(TakenOverByTypeV5.class, AdditionalServiceInformationTypeV5.class, QualificationElementTypeV5.class);
                    Object obj2 = ctx.createUnmarshaller().unmarshal(myObj);
                    filterTypeV5(obj2);
                } catch (Exception e) {
                    String tmpTag = AnyTypeUtils.convertOtherTag(myObj);
                    if (tmpTag == null) {
                        LOGGER.error("TLServiceExtensionV5 - can't parse ExtensionType", e);
                    } else {
                        processAnyType(tmpTag);
                    }
                }
            } else if (obj instanceof String) {
                processAnyType((String) obj);
            }
        }
    }

    public void toV5FromV4() {
        if (getQualificationsExtension() != null) {
            for (TLQualificationExtension qe : getQualificationsExtension()) {
                qe.toV5FromV4();
            }
        }
    }

    @SuppressWarnings("rawtypes")
    private void filterType(Object obj) {
        int i = 0;
        if (obj instanceof QualificationsType) {
            QualificationsType qt = (QualificationsType) obj;
            List<TLQualificationExtension> qualificationsExtension = new ArrayList<>();
            for (QualificationElementType qet : qt.getQualificationElement()) {
                i++;
                qualificationsExtension.add(new TLQualificationExtension(getTlId(), getId() + "_" + Tag.SERVICE_QUALIFICATION_EXT + "_" + i, qet));
            }
            setQualificationsExtension(qualificationsExtension);

        } else if (obj instanceof AdditionalServiceInformationType) {
            AdditionalServiceInformationType asit = (AdditionalServiceInformationType) obj;
            setAdditionnalServiceInfo(new TLAdditionnalServiceInfo(getTlId(), getId() + "_" + Tag.SERVICE_ADDITIONNAL_EXT, asit));

        } else if (obj instanceof TakenOverByType) {
            TakenOverByType ttob = (TakenOverByType) obj;
            setTakenOverBy(new TLTakenOverBy(getTlId(), getId() + "_" + Tag.SERVICE_TOB_EXT, ttob));

        } else if (obj instanceof JAXBElement) {
            JAXBElement obj2 = (JAXBElement) obj;
            filterType(obj2.getValue());

        } else if (obj instanceof XMLGregorianCalendar) {
            XMLGregorianCalendar calendar = (XMLGregorianCalendar) obj;
            setExpiredCertsRevocationDate(TLUtils.toDate(calendar));
        } else {
            LOGGER.warn("Cannot read object " + obj.getClass());
        }
    }

    @SuppressWarnings("rawtypes")
    private void filterTypeV5(Object obj) {
        int i = 0;
        if (obj instanceof QualificationsTypeV5) {
            QualificationsTypeV5 qt = (QualificationsTypeV5) obj;
            List<TLQualificationExtension> qualificationsExtension = new ArrayList<>();
            for (QualificationElementTypeV5 qet : qt.getQualificationElement()) {
                i++;
                qualificationsExtension.add(new TLQualificationExtension(getTlId(), getId() + "_" + Tag.SERVICE_QUALIFICATION_EXT + "_" + i, qet));
            }
            setQualificationsExtension(qualificationsExtension);

        } else if (obj instanceof AdditionalServiceInformationTypeV5) {
            AdditionalServiceInformationTypeV5 asit = (AdditionalServiceInformationTypeV5) obj;
            setAdditionnalServiceInfo(new TLAdditionnalServiceInfo(getTlId(), getId() + "_" + Tag.SERVICE_ADDITIONNAL_EXT, asit));

        } else if (obj instanceof TakenOverByTypeV5) {
            TakenOverByTypeV5 ttob = (TakenOverByTypeV5) obj;
            setTakenOverBy(new TLTakenOverBy(getTlId(), getId() + "_" + Tag.SERVICE_TOB_EXT, ttob));

        } else if (obj instanceof JAXBElement) {
            JAXBElement obj2 = (JAXBElement) obj;
            filterTypeV5(obj2.getValue());

        } else if (obj instanceof String) {
            JaxbGregorianCalendarZulu adaptater = new JaxbGregorianCalendarZulu();
            String date = (String) obj;
            setExpiredCertsRevocationDate(TLUtils.toDate(adaptater.unmarshal(date)));

        } else {
            LOGGER.warn("Cannot read object " + obj.getClass());
        }
    }

    public ExtensionTypeV5 asTSLTypeV5() {
        ExtensionTypeV5 e = new ExtensionTypeV5();
        e.setCritical(isCritical());

        // QualificationsType
        if (getQualificationsExtension() != null) {
            QualificationsTypeV5 qt = new QualificationsTypeV5();
            for (TLQualificationExtension tlqe : getQualificationsExtension()) {
                qt.getQualificationElement().add(tlqe.asTSLTypeV5());
            }
            e.getContent().add(new JAXBElement<>(new QName("https://uri.etsi.org/TrstSvc/SvcInfoExt/eSigDir-1999-93-EC-TrustedList/#", "Qualifications"), QualificationsTypeV5.class, qt));
        }

        // AdditionalServiceInformationType
        if (getAdditionnalServiceInfo() != null) {
            e.getContent().add(
                    new JAXBElement<>(new QName("https://uri.etsi.org/02231/v2#", "AdditionalServiceInformation"), AdditionalServiceInformationTypeV5.class, getAdditionnalServiceInfo().asTSLTypeV5()));

        }

        if (getTakenOverBy() != null) {
            e.getContent().add(new JAXBElement<>(new QName("https://uri.etsi.org/02231/v2/additionaltypes#", "TakenOverBy"), TakenOverByTypeV5.class, getTakenOverBy().asTSLTypeV5()));
        }

        if (getExpiredCertsRevocationDate() != null) {
            JaxbGregorianCalendarZulu adaptater = new JaxbGregorianCalendarZulu();
            try {
                e.getContent().add(new JAXBElement<>(new QName("https://uri.etsi.org/02231/v2#", "ExpiredCertsRevocationInfo"), String.class,
                        adaptater.marshal(TLUtils.toXMLGregorianDate(getExpiredCertsRevocationDate()))));
            } catch (Exception e1) {
                LOGGER.error(e1.getMessage());
            }
        }

        if (!StringUtils.isEmpty(getAnyType())) {
            e.getContent().add(getAnyType());
        }

        return e;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((additionnalServiceInfo == null) ? 0 : additionnalServiceInfo.hashCode());
        result = (prime * result) + (critical ? 1231 : 1237);
        result = (prime * result) + ((expiredCertsRevocationDate == null) ? 0 : expiredCertsRevocationDate.hashCode());
        result = (prime * result) + ((qualificationsExtension == null) ? 0 : qualificationsExtension.hashCode());
        result = (prime * result) + ((takenOverBy == null) ? 0 : takenOverBy.hashCode());
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
        TLServiceExtension other = (TLServiceExtension) obj;
        if (additionnalServiceInfo == null) {
            if (other.additionnalServiceInfo != null) {
                return false;
            }
        } else if (!additionnalServiceInfo.equals(other.additionnalServiceInfo)) {
            return false;
        }
        if (critical != other.critical) {
            return false;
        }
        if (expiredCertsRevocationDate == null) {
            if (other.expiredCertsRevocationDate != null) {
                return false;
            }
        } else if (!expiredCertsRevocationDate.equals(other.expiredCertsRevocationDate)) {
            return false;
        }
        if (qualificationsExtension == null) {
            if (other.qualificationsExtension != null) {
                return false;
            }
        } else if (!qualificationsExtension.equals(other.qualificationsExtension)) {
            return false;
        }
        if (takenOverBy == null) {
            if (other.takenOverBy != null) {
                return false;
            }
        } else if (!takenOverBy.equals(other.takenOverBy)) {
            return false;
        }
        return true;
    }

    //
    /*
     * GETTERS AND SETTERS
     */
    public boolean isCritical() {
        return critical;
    }

    public void setCritical(boolean critical) {
        this.critical = critical;
    }

    public TLAdditionnalServiceInfo getAdditionnalServiceInfo() {
        return additionnalServiceInfo;
    }

    public void setAdditionnalServiceInfo(TLAdditionnalServiceInfo additionnalServiceInfo) {
        this.additionnalServiceInfo = additionnalServiceInfo;
    }

    public List<TLQualificationExtension> getQualificationsExtension() {
        return qualificationsExtension;
    }

    public void setQualificationsExtension(List<TLQualificationExtension> qualificationsExtension) {
        this.qualificationsExtension = qualificationsExtension;
    }

    public TLTakenOverBy getTakenOverBy() {
        return takenOverBy;
    }

    public void setTakenOverBy(TLTakenOverBy takenOverBy) {
        this.takenOverBy = takenOverBy;
    }

    public Date getExpiredCertsRevocationDate() {
        return expiredCertsRevocationDate;
    }

    public void setExpiredCertsRevocationDate(Date expiredCertsRevocationDate) {
        this.expiredCertsRevocationDate = expiredCertsRevocationDate;
    }

    public String getAnyType() {
        return anyType;
    }

    /**
     * Concat current anyType with tmpAnyType if not empty string
     *
     * @param tmpAnyType
     */
    public void processAnyType(String tmpAnyType) {
        String cleanAnyType = AnyTypeUtils.cleanOtherTag(tmpAnyType);
        if (!StringUtils.isEmpty(cleanAnyType)) {
            if (StringUtils.isEmpty(anyType)) {
                anyType = tmpAnyType;
            } else {
                anyType.concat(tmpAnyType);
            }
        }
    }

}
