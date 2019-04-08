/*******************************************************************************
 * DIGIT-TSL - Trusted List Manager
 * Copyright (C) 2018 European Commission, provided under the CEF E-Signature programme
 *  
 * This file is part of the "DIGIT-TSL - Trusted List Manager" project.
 *  
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 *  
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
package eu.europa.ec.joinup.tsl.business.dto.tl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.joinup.tsl.business.util.AnyTypeUtils;
import eu.europa.ec.joinup.tsl.model.enums.Tag;
import eu.europa.esig.jaxb.tsl.AnyType;
import eu.europa.esig.jaxb.tsl.MultiLangNormStringType;
import eu.europa.esig.jaxb.tslx.TakenOverByType;
import eu.europa.esig.jaxb.v5.tsl.AnyTypeV5;
import eu.europa.esig.jaxb.v5.tsl.InternationalNamesTypeV5;
import eu.europa.esig.jaxb.v5.tsl.MultiLangNormStringTypeV5;
import eu.europa.esig.jaxb.v5.tslx.TakenOverByTypeV5;

public class TLTakenOverBy extends AbstractTLDTO {

    private static final Logger LOGGER = LoggerFactory.getLogger(TLTakenOverBy.class);

    private TLName url;
    private List<TLName> tspName;
    private List<TLName> operatorName;
    private String territory;
    private List<String> OtherQualifier;

    public TLTakenOverBy() {
    }

    public String mainOperatorName() {
        for (TLName tlName : operatorName) {
            if (tlName.getLanguage().equalsIgnoreCase("en")) {
                return tlName.getValue();
            }
        }
        return "Undefined";
    }

    public TLTakenOverBy(int iddb, String location, TakenOverByType ttob) {
        super(iddb, location + ttob.getSchemeTerritory());
        setTerritory(ttob.getSchemeTerritory());
        setUrl(new TLName(getTlId(), getId() + "_" + Tag.INFORMATION_URI, ttob.getURI()));

        int i = 0;
        List<TLName> tspName = new ArrayList<>();
        for (MultiLangNormStringType tName : ttob.getTSPName().getName()) {
            i++;
            tspName.add(new TLName(getTlId(), getId() + "_" + Tag.TSP_NAME + i, tName));
        }
        setTspName(tspName);

        i = 0;
        List<TLName> opeName = new ArrayList<>();
        for (MultiLangNormStringType oName : ttob.getSchemeOperatorName().getName()) {
            i++;
            opeName.add(new TLName(getTlId(), getId() + "_" + Tag.SCHEME_OPERATOR_NAME + "_" + i, oName));
        }
        setOperatorName(opeName);

        if ((ttob.getOtherQualifier() != null) && !CollectionUtils.isEmpty(ttob.getOtherQualifier())) {
            for (AnyType anyType : ttob.getOtherQualifier()) {
                if (!CollectionUtils.isEmpty(anyType.getContent())) {
                    for (Object object : anyType.getContent()) {
                        String tmpQualifier = AnyTypeUtils.convertOtherTag(object);
                        if (tmpQualifier == null) {
                            LOGGER.error("TLTakenOverBy - can't parse otherQualifier : " + ttob.getOtherQualifier().get(0).toString());
                        } else {
                            addOtherQualifier(tmpQualifier);
                        }
                    }
                }
            }
        }
    }

    public TLTakenOverBy(int iddb, String location, TakenOverByTypeV5 ttob) {
        super(iddb, location + ttob.getSchemeTerritory());
        setTerritory(ttob.getSchemeTerritory());
        if (ttob.getURI() != null) {
            setUrl(new TLName(getTlId(), getId() + "_" + Tag.INFORMATION_URI, ttob.getURI()));
        }

        int i = 0;
        List<TLName> tspName = new ArrayList<>();
        if (ttob.getTSPName() != null) {
            for (MultiLangNormStringTypeV5 tName : ttob.getTSPName().getName()) {
                i++;
                tspName.add(new TLName(getTlId(), getId() + "_" + Tag.TSP_NAME + i, tName));
            }
        }
        setTspName(tspName);

        i = 0;
        List<TLName> opeName = new ArrayList<>();
        if (ttob.getSchemeOperatorName() != null) {
            for (MultiLangNormStringTypeV5 oName : ttob.getSchemeOperatorName().getName()) {
                i++;
                opeName.add(new TLName(getTlId(), getId() + "_" + Tag.SCHEME_OPERATOR_NAME + "_" + i, oName));
            }
        }
        setOperatorName(opeName);

        if ((ttob.getOtherQualifier() != null) && !CollectionUtils.isEmpty(ttob.getOtherQualifier())) {
            for (AnyTypeV5 anyType : ttob.getOtherQualifier()) {
                if (!CollectionUtils.isEmpty(anyType.getContent())) {
                    StringBuilder tmpQualifier = new StringBuilder();
                    for (Object object : anyType.getContent()) {
                        String tmpObject = AnyTypeUtils.convertOtherTag(object);
                        if (!StringUtils.isEmpty(tmpObject)) {
                            tmpQualifier.append(tmpObject);
                        }
                    }

                    if (StringUtils.isEmpty(tmpQualifier.toString())) {
                        LOGGER.error("TLTakenOverByV5 - can't parse otherQualifier : " + ttob.getOtherQualifier().get(0).toString());
                    } else {
                        addOtherQualifier(tmpQualifier.toString());
                    }
                }
            }
        }
    }

    public TakenOverByTypeV5 asTSLTypeV5() {
        TakenOverByTypeV5 tobt = new TakenOverByTypeV5();

        if (!StringUtils.isEmpty(getTerritory())) {
            tobt.setSchemeTerritory(getTerritory());
        }

        if ((getUrl() != null) && (getUrl().getValue() != null)) {
            tobt.setURI(getUrl().asTSLTypeV5Non());
        }

        if (!CollectionUtils.isEmpty(getOperatorName())) {
            InternationalNamesTypeV5 intNames = new InternationalNamesTypeV5();
            if (getOperatorName() != null) {
                for (TLName name : getOperatorName()) {
                    intNames.getName().add(name.asTSLTypeV5());
                }
            }
            tobt.setSchemeOperatorName(intNames);
        }

        if (!CollectionUtils.isEmpty(getTspName())) {
            InternationalNamesTypeV5 tspNames = new InternationalNamesTypeV5();
            if (getTspName() != null) {
                for (TLName name : getTspName()) {
                    tspNames.getName().add(name.asTSLTypeV5());
                }
            }
            tobt.setTSPName(tspNames);
        }

        if (!CollectionUtils.isEmpty(getOtherQualifier())) {
            for (String anyType : getOtherQualifier()) {
                AnyTypeV5 anyTypeV5 = new AnyTypeV5();
                anyTypeV5.getContent().add(anyType);
                tobt.getOtherQualifier().add(anyTypeV5);
            }
        }

        return tobt;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((OtherQualifier == null) ? 0 : OtherQualifier.hashCode());
        result = (prime * result) + ((operatorName == null) ? 0 : operatorName.hashCode());
        result = (prime * result) + ((territory == null) ? 0 : territory.hashCode());
        result = (prime * result) + ((tspName == null) ? 0 : tspName.hashCode());
        result = (prime * result) + ((url == null) ? 0 : url.hashCode());
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
        TLTakenOverBy other = (TLTakenOverBy) obj;
        if (OtherQualifier == null) {
            if (other.OtherQualifier != null) {
                return false;
            }
        } else if (!OtherQualifier.equals(other.OtherQualifier)) {
            return false;
        }
        if (operatorName == null) {
            if (other.operatorName != null) {
                return false;
            }
        } else if (!operatorName.equals(other.operatorName)) {
            return false;
        }
        if (territory == null) {
            if (other.territory != null) {
                return false;
            }
        } else if (!territory.equals(other.territory)) {
            return false;
        }
        if (tspName == null) {
            if (other.tspName != null) {
                return false;
            }
        } else if (!tspName.equals(other.tspName)) {
            return false;
        }
        if (url == null) {
            if (other.url != null) {
                return false;
            }
        } else if (!url.equals(other.url)) {
            return false;
        }
        return true;
    }

    public TLName getUrl() {
        return url;
    }

    public void setUrl(TLName url) {
        this.url = url;
    }

    public List<TLName> getTspName() {
        return tspName;
    }

    public void setTspName(List<TLName> tspName) {
        this.tspName = tspName;
    }

    public List<TLName> getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(List<TLName> operatorName) {
        this.operatorName = operatorName;
    }

    public String getTerritory() {
        return territory;
    }

    public void setTerritory(String territory) {
        this.territory = territory;
    }

    public List<String> getOtherQualifier() {
        return OtherQualifier;
    }

    public void setOtherQualifier(List<String> otherQualifier) {
        OtherQualifier = otherQualifier;
    }

    public void addOtherQualifier(String qualifier) {
        String cleanQualifier = AnyTypeUtils.cleanOtherTag(qualifier);
        if (!StringUtils.isEmpty(cleanQualifier)) {
            if (OtherQualifier == null) {
                OtherQualifier = new ArrayList<>();
            }
            OtherQualifier.add(cleanQualifier);
        }
    }

}
