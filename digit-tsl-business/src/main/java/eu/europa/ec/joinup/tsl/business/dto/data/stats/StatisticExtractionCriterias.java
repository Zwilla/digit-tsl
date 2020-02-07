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
    private boolean showTOB;
    private boolean showSequenceNumber;
    private boolean showTradeName;
    private boolean showQualified;
    private boolean showUnqualified;

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
            fileName.append(specificCountry).append(" - ");
        }
        // Type
        fileName.append(type.name()).append(" - ");
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
        } else if (showQualified) {
            fileName.append(" - Q");
        } else if (showUnqualified) {
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

    public boolean getShowTOB() {
        return showTOB;
    }

    public void setShowTOB(boolean showTOB) {
        this.showTOB = showTOB;
    }

    public boolean getShowSequenceNumber() {
        return showSequenceNumber;
    }

    public void setShowSequenceNumber(boolean showSequenceNumber) {
        this.showSequenceNumber = showSequenceNumber;
    }

    public boolean getShowTradeName() {
        return showTradeName;
    }

    public void setShowTradeName(boolean showTradeName) {
        this.showTradeName = showTradeName;
    }

    public boolean getShowQualified() {
        return showQualified;
    }

    public void setShowQualified(boolean showQualified) {
        this.showQualified = showQualified;
    }

    public boolean getShowUnqualified() {
        return showUnqualified;
    }

    public void setShowUnqualified(boolean showUnqualified) {
        this.showUnqualified = showUnqualified;
    }

}
