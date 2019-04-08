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
package eu.europa.ec.joinup.tsl.business.dto.data.stats;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class StatisticExtractionCriterias {

    // Extraction
    private StatisticExtractionType type;
    private String specificCountry;
    private Date extractDate;

    // Countries
    private List<String> countries;

    // Show/hide fields
    private Boolean showTOB;
    private Boolean showSequenceNumber;
    private Boolean showTradeName;
    private Boolean showQualified;
    private Boolean showUnqualified;

    public StatisticExtractionCriterias() {
        super();
    }

    public StatisticExtractionCriterias(List<String> countries) {
        this(StatisticExtractionType.COUNTRY, new Date());
        this.countries = countries;
    }

    public StatisticExtractionCriterias(StatisticExtractionType type, Date extractDate) {
        super();
        this.type = type;
        specificCountry = null;
        this.extractDate = extractDate;
        showTOB = true;
        showSequenceNumber = true;
        showTradeName = true;
        showQualified = true;
        showUnqualified = true;
    }

    public StatisticExtractionCriterias(StatisticExtractionType type, Date extractDate, String countryCode) {
        super();
        this.type = type;
        specificCountry = countryCode;
        this.extractDate = extractDate;
        showTOB = true;
        showSequenceNumber = true;
        showTradeName = true;
        showQualified = true;
        showUnqualified = true;
    }

    public String getDynamicName() {
        StringBuilder fileName = new StringBuilder();
        // Country
        if (StringUtils.isEmpty(specificCountry)) {
            fileName.append("ALL - ");
        } else {
            fileName.append(specificCountry + " - ");
        }
        // Type
        fileName.append(type.name() + " - ");
        // Date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.YYYY");
        fileName.append(dateFormat.format(extractDate));
        // TOB
        if (showTOB) {
            fileName.append(" - TOB");
        }
        // Options (qualified/non qualified)
        if (showQualified && showUnqualified) {
            fileName.append(" - Q & NQ");
        } else if (showQualified && !showUnqualified) {
            fileName.append(" - Q");
        } else if (!showQualified && showUnqualified) {
            fileName.append(" - NQ");
        }
        return fileName.toString();
    }

    public StatisticExtractionType getType() {
        return type;
    }

    public void setType(StatisticExtractionType type) {
        this.type = type;
    }

    public String getSpecificCountry() {
        return specificCountry;
    }

    public void setSpecificCountry(String specificCountry) {
        this.specificCountry = specificCountry;
    }

    public Date getExtractDate() {
        return extractDate;
    }

    public void setExtractDate(Date extractDate) {
        this.extractDate = extractDate;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public Boolean getShowTOB() {
        return showTOB;
    }

    public void setShowTOB(Boolean showTOB) {
        this.showTOB = showTOB;
    }

    public Boolean getShowSequenceNumber() {
        return showSequenceNumber;
    }

    public void setShowSequenceNumber(Boolean showSequenceNumber) {
        this.showSequenceNumber = showSequenceNumber;
    }

    public Boolean getShowTradeName() {
        return showTradeName;
    }

    public void setShowTradeName(Boolean showTradeName) {
        this.showTradeName = showTradeName;
    }

    public Boolean getShowQualified() {
        return showQualified;
    }

    public void setShowQualified(Boolean showQualified) {
        this.showQualified = showQualified;
    }

    public Boolean getShowUnqualified() {
        return showUnqualified;
    }

    public void setShowUnqualified(Boolean showUnqualified) {
        this.showUnqualified = showUnqualified;
    }

}
