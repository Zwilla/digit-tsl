package eu.europa.ec.joinup.tsl.business.dto.pdf;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import eu.europa.ec.joinup.tsl.business.dto.CheckResultDTO;
import eu.europa.ec.joinup.tsl.business.dto.TLDifference;
import eu.europa.ec.joinup.tsl.model.enums.CheckStatus;

@XStreamAlias("root")
public class PDFChecksChanges {

    @XStreamAsAttribute
    private String countryName;
    @XStreamAsAttribute
    private String sequence;
    @XStreamAsAttribute
    private int numberOfChanges = 0;
    @XStreamAsAttribute
    private int schemeChanges;
    @XStreamAsAttribute
    private int pointerChanges;
    @XStreamAsAttribute
    private int serviceChanges;
    @XStreamAsAttribute
    private boolean tlccActive;
    @XStreamAsAttribute
    private int signatureChanges;

    @XStreamOmitField
    private List<PDFCheck> checks;

    private List<PDFCheck> signatureChecks;
    private List<PDFCheck> tlChecks;
    private List<PDFCheck> schemeInformationChecks;
    private List<PDFCheck> pointersToOtherTslChecks;
    private List<PDFCheck> serviceProviderChecks;

    @XStreamAsAttribute
    private boolean production;
    @XStreamAsAttribute
    private PDFInfoTL currentTL;
    @XStreamAsAttribute
    private PDFInfoTL comparedTL;

    private List<TLDifference> schemeInformationChanges;
    @XStreamAsAttribute
    private List<TLDifference> pointersToOtherTslChanges;
    @XStreamAsAttribute
    private List<TLDifference> serviceProviderChanges;
    @XStreamAsAttribute
    private List<TLDifference> signatureListChanges;

    public PDFChecksChanges() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String generationDate = dateFormat.format(currentDate);
    }

    public void setChecks(List<CheckResultDTO> checks) {
        if (checks == null) {
            checks = new ArrayList<>();
        }
        Collections.sort(checks, new Comparator<CheckResultDTO>() {
            @Override
            public int compare(CheckResultDTO o1, CheckResultDTO o2) {
                if (o1.getStatus().equals(o2.getStatus())) {
                    return o1.getLocation().compareTo(o2.getLocation());
                } else {
                    return o1.getStatus().toString().compareTo(o2.getStatus().toString());
                }
            }
        });

        this.checks = createCheckList(checks);
        extractChecks();
        countChecks(checks);
    }

    private void extractChecks() {
        tlChecks = new ArrayList<>();
        signatureChecks = new ArrayList<>();
        serviceProviderChecks = new ArrayList<>();
        schemeInformationChecks = new ArrayList<>();
        pointersToOtherTslChecks = new ArrayList<>();

        for (PDFCheck check : checks) {
            if (check.getLocationId().contains("TSP_SERVICE_PROVIDER")) {
                serviceProviderChecks.add(check);
            } else if (check.getLocationId().contains("SCHEME_INFORMATION")) {
                schemeInformationChecks.add(check);
            } else if (check.getLocationId().contains("POINTERS_TO_OTHER_TSL")) {
                pointersToOtherTslChecks.add(check);
            } else if (check.getLocationId().contains("SIGNATURE")) {
                signatureChecks.add(check);
            } else {
                tlChecks.add(check);
            }
        }
    }

    private void countChecks(List<CheckResultDTO> checks) {
        int errorChecks = 0;
        int warningChecks = 0;
        int infoChecks = 0;

        for (CheckResultDTO check : checks) {
            if (check.getStatus() == CheckStatus.ERROR) {
                errorChecks++;
            } else if (check.getStatus() == CheckStatus.WARNING) {
                warningChecks++;
            } else if (check.getStatus() == CheckStatus.INFO) {
                infoChecks++;
            }
        }

        int numberOfCheck = errorChecks + warningChecks + infoChecks;
    }

    private List<PDFCheck> createCheckList(List<CheckResultDTO> list) {
        List<PDFCheck> result = new ArrayList<>();
        CheckResultDTO previous = null;
        for (CheckResultDTO check : list) {
            if (previous == null) {
                PDFCheck toSerialize = new PDFCheck();
                toSerialize.add(check);
                result.add(toSerialize);
                previous = check;
            } else {
                if (previous.getLocation().equals(check.getLocation())) {
                    result.get(result.size() - 1).add(check);
                } else {
                    PDFCheck toSerialize = new PDFCheck();
                    toSerialize.add(check);
                    result.add(toSerialize);
                    previous = check;
                }
            }
        }
        return result;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public List<TLDifference> getSchemeInformationChanges() {
        return schemeInformationChanges;
    }

    public void setSchemeInformationChanges(List<TLDifference> schemeInformationChanges) {
        Collections.sort(schemeInformationChanges, new Comparator<TLDifference>() {
            @Override
            public int compare(TLDifference change1, TLDifference change2) {
                return change1.getId().compareTo(change2.getId());
            }
        });
        this.schemeInformationChanges = schemeInformationChanges;
    }

    public List<TLDifference> getPointersToOtherTslChanges() {
        return pointersToOtherTslChanges;
    }

    public void setPointersToOtherTslChanges(List<TLDifference> pointersToOtherTslChanges) {
        Collections.sort(pointersToOtherTslChanges, new Comparator<TLDifference>() {
            @Override
            public int compare(TLDifference change1, TLDifference change2) {
                return change1.getId().compareTo(change2.getId());
            }
        });
        this.pointersToOtherTslChanges = pointersToOtherTslChanges;
    }

    public List<TLDifference> getServiceProviderChanges() {
        return serviceProviderChanges;
    }

    public void setServiceProviderChanges(List<TLDifference> serviceProviderChanges) {
        Collections.sort(serviceProviderChanges, new Comparator<TLDifference>() {
            @Override
            public int compare(TLDifference change1, TLDifference change2) {
                return change1.getId().compareTo(change2.getId());
            }
        });
        this.serviceProviderChanges = serviceProviderChanges;
    }

    public PDFInfoTL getCurrentTL() {
        return currentTL;
    }

    public void setCurrentTL(PDFInfoTL currentTL) {
        this.currentTL = currentTL;
    }

    public PDFInfoTL getComparedTL() {
        return comparedTL;
    }

    public void setComparedTL(PDFInfoTL comparedtTL) {
        comparedTL = comparedtTL;
    }

    public boolean isProduction() {
        return production;
    }

    public void setProduction(boolean production) {
        this.production = production;
    }

    public int getNumberOfChanges() {
        return numberOfChanges;
    }

    public void setNumberOfChanges(int numberOfChanges) {
        this.numberOfChanges = numberOfChanges;
    }

    public List<PDFCheck> getSchemeInformationChecks() {
        return schemeInformationChecks;
    }

    public void setSchemeInformationChecks(List<PDFCheck> schemeInformationChecks) {
        this.schemeInformationChecks = schemeInformationChecks;
    }

    public List<PDFCheck> getPointersToOtherTslChecks() {
        return pointersToOtherTslChecks;
    }

    public void setPointersToOtherTslChecks(List<PDFCheck> pointersToOtherTslChecks) {
        this.pointersToOtherTslChecks = pointersToOtherTslChecks;
    }

    public List<PDFCheck> getServiceProviderChecks() {
        return serviceProviderChecks;
    }

    public void setServiceProviderChecks(List<PDFCheck> serviceProviderChecks) {
        this.serviceProviderChecks = serviceProviderChecks;
    }

    public int getSchemeChanges() {
        return schemeChanges;
    }

    public void setSchemeChanges(int schemeChanges) {
        this.schemeChanges = schemeChanges;
    }

    public int getPointerChanges() {
        return pointerChanges;
    }

    public void setPointerChanges(int pointerChanges) {
        this.pointerChanges = pointerChanges;
    }

    public int getServiceChanges() {
        return serviceChanges;
    }

    public void setServiceChanges(int serviceChanges) {
        this.serviceChanges = serviceChanges;
    }

    public List<PDFCheck> getSignatureChecks() {
        return signatureChecks;
    }

    public void setSignatureChecks(List<PDFCheck> signatureChecks) {
        this.signatureChecks = signatureChecks;
    }

    public boolean getTlccActive() {
        return tlccActive;
    }

    public void setTlccActive(boolean tlccActive) {
        this.tlccActive = tlccActive;
    }

    public int getSignatureChanges() {
        return signatureChanges;
    }

    public void setSignatureChanges(int signatureChanges) {
        this.signatureChanges = signatureChanges;
    }

    public List<TLDifference> getSignatureListChanges() {
        return signatureListChanges;
    }

    public void setSignatureListChanges(List<TLDifference> signatureListChanges) {
        this.signatureListChanges = signatureListChanges;
    }

    public List<PDFCheck> getTlChecks() {
        return tlChecks;
    }

    public void setTlChecks(List<PDFCheck> tlChecks) {
        this.tlChecks = tlChecks;
    }

}
