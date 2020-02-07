package eu.europa.ec.joinup.tsl.business.dto.pdf;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import eu.europa.ec.joinup.tsl.business.dto.CheckDTO;
import eu.europa.ec.joinup.tsl.business.dto.TLSignature;
import eu.europa.ec.joinup.tsl.business.dto.User;
import eu.europa.ec.joinup.tsl.business.dto.notification.PDFMeasure;
import eu.europa.ec.joinup.tsl.business.util.TLChecksComparatorUtils;
import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.ec.joinup.tsl.model.enums.CheckStatus;

@XStreamAlias("root")
public class PDFNotificationReport {

    @XStreamAsAttribute
    private String memberState;
    @XStreamAsAttribute
    private String contactName;
    @XStreamAsAttribute
    private String contactAdress;
    @XStreamAsAttribute
    private String contactPhone;
    @XStreamAsAttribute
    private List<String> contactMail;
    @XStreamAsAttribute
    private String mpPointer;
    @XStreamAsAttribute
    private String hrPointer;
    @XStreamAsAttribute
    private String dateOfSubmission;
    @XStreamAsAttribute
    private String dateOfEffect;
    @XStreamAsAttribute
    private TLSignature signatureInformation;
    @XStreamAsAttribute
    private List<User> users;

    private final List<Map<String, String>> schemeOperatorNames = new ArrayList<>();

    @XStreamImplicit(itemFieldName = "certificate")
    private final List<String> signingCertificates = new ArrayList<>();
    @XStreamImplicit(itemFieldName = "certificateRemoved")
    private final List<String> signingCertificatesRemoved = new ArrayList<>();
    @XStreamAlias("changeList")
    private final List<PDFMeasure> changes = new ArrayList<>();

    private List<PDFCheck> pointersToOtherTslChecks = new ArrayList<>();

    @XStreamAsAttribute
    private int numberOfCheck;
    @XStreamAsAttribute
    private int warningChecks;
    @XStreamAsAttribute
    private int errorChecks;
    @XStreamAsAttribute
    private int infoChecks;

    public PDFNotificationReport(String countryName, Date date) {
        String docID = "(Document ID : " + countryName + "-" + TLUtils.toStringFormatZ(date) + ")";
    }

    public String getMemberState() {
        return memberState;
    }

    public void setMemberState(String memberState) {
        this.memberState = memberState;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactAddress() {
        return contactAdress;
    }

    public void setContactAddress(String contactAddress) {
        contactAdress = contactAddress;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public List<String> getContactMail() {
        return contactMail;
    }

    public void setContactMail(List<String> contactMail) {
        this.contactMail = contactMail;
    }

    public String getMpPointer() {
        return mpPointer;
    }

    public void setMpPointer(String mpPointer) {
        this.mpPointer = mpPointer;
    }

    public String getHrPointer() {
        return hrPointer;
    }

    public void setHrPointer(String hrPointer) {
        this.hrPointer = hrPointer;
    }

    public String getDateOfSubmission() {
        return dateOfSubmission;
    }

    public void setDateOfSubmission(Date dateOfSubmission) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.dateOfSubmission = dateFormat.format(dateOfSubmission);
    }

    public String getDateOfEffect() {
        return dateOfEffect;
    }

    public void setDateOfEffect(Date dateOfEffect) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.dateOfEffect = dateFormat.format(dateOfEffect);
    }

    public void addSchemeOperatorName(String language, String value) {
        boolean canAdd = true;
        Map<String, String> map = new HashMap<>();
        map.put(language, value);
        schemeOperatorNames.add(map);
    }

    public void addSigningCertificate(String value) {
        signingCertificates.add(value);
    }

    public List<String> getSigningCertificate() {
        return signingCertificates;
    }

    public void addRemovedSigningCertificate(String value) {
        signingCertificatesRemoved.add(value);
    }

    public List<String> getRemovedSigningCertificate() {
        return signingCertificatesRemoved;
    }

    public void addChange(PDFMeasure change) {
        if (canAddChange(change)) {
            changes.add(change);
        }
    }

    private boolean canAddChange(PDFMeasure change) {
        if ((change.getChange() == null) || (change.getMeasure() == null)) {
            return false;
        }
        if (change.getChange().isEmpty()) {
            return false;
        }
        for (PDFMeasure c : changes) {
            if (c.equals(change)) {
                return false;
            }
        }
        return true;
    }

    public void setChecks(List<CheckDTO> checks) {
        Collections.sort(checks, new Comparator<CheckDTO>() {
            @Override
            public int compare(CheckDTO check1, CheckDTO check2) {
                return check1.getHrLocation().compareTo(check2.getHrLocation());
            }
        });

        pointersToOtherTslChecks = createCheckList(checks);
        countChecks(checks);
    }

    private void countChecks(List<CheckDTO> checks) {
        errorChecks = 0;
        warningChecks = 0;
        infoChecks = 0;

        for (CheckDTO check : checks) {
            if (check.getStatus() == CheckStatus.ERROR) {
                errorChecks++;
            } else if (check.getStatus() == CheckStatus.WARNING) {
                warningChecks++;
            } else if (check.getStatus() == CheckStatus.INFO) {
                infoChecks++;
            }
        }

        numberOfCheck = errorChecks + warningChecks + infoChecks;
    }

    private List<PDFCheck> createCheckList(List<CheckDTO> list) {
        List<PDFCheck> result = new ArrayList<>();
        CheckDTO previous = null;
        for (CheckDTO check : list) {
            check.setId(check.getHrLocation());
            if (previous == null) {
                PDFCheck toSerialize = new PDFCheck();
                toSerialize.add(check);
                Collections.sort(toSerialize.getChecks(), new TLChecksComparatorUtils());
                result.add(toSerialize);
                previous = check;
            } else {
                if (previous.getId().equals(check.getId())) {
                    result.get(result.size() - 1).add(check);
                } else {
                    PDFCheck toSerialize = new PDFCheck();
                    toSerialize.add(check);
                    Collections.sort(toSerialize.getChecks(), new TLChecksComparatorUtils());
                    result.add(toSerialize);
                    previous = check;
                }
            }
        }
        return result;
    }

    public int getNumberOfCheck() {
        return numberOfCheck;
    }

    public void setNumberOfCheck(int numberOfCheck) {
        this.numberOfCheck = numberOfCheck;
    }

    public int getWarningChecks() {
        return warningChecks;
    }

    public void setWarningChecks(int warningChecks) {
        this.warningChecks = warningChecks;
    }

    public int getErrorChecks() {
        return errorChecks;
    }

    public void setErrorChecks(int errorChecks) {
        this.errorChecks = errorChecks;
    }

    public int getInfoChecks() {
        return infoChecks;
    }

    public void setInfoChecks(int infoChecks) {
        this.infoChecks = infoChecks;
    }

    public List<PDFCheck> getPointersToOtherTslChecks() {
        return pointersToOtherTslChecks;
    }

    public void setPointersToOtherTslChecks(List<PDFCheck> pointersToOtherTslChecks) {
        this.pointersToOtherTslChecks = pointersToOtherTslChecks;
    }

    public TLSignature getSignatureInformation() {
        return signatureInformation;
    }

    public void setSignatureInformation(TLSignature signatureInformation) {
        this.signatureInformation = signatureInformation;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
