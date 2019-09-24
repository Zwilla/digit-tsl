package eu.europa.ec.joinup.tsl.business.dto.sieQ;

import org.springframework.web.multipart.MultipartFile;

public class SieQValidationForm {

    private SieQSearchType certificateType;
    private String certificateB64;
    private MultipartFile certificateFile;

    public SieQValidationForm() {
        super();
    }

    public SieQValidationForm(MultipartFile certificateFile) {
        this.certificateType = SieQSearchType.FILE;
        this.certificateFile = certificateFile;
    }

    public SieQValidationForm(String certificateB64) {
        this.certificateType = SieQSearchType.B64;
        this.certificateB64 = certificateB64;
    }

    public SieQSearchType getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(SieQSearchType certificateType) {
        this.certificateType = certificateType;
    }

    public String getCertificateB64() {
        return certificateB64;
    }

    public void setCertificateB64(String certificateB64) {
        this.certificateB64 = certificateB64;
    }

    public MultipartFile getCertificateFile() {
        return certificateFile;
    }

    public void setCertificateFile(MultipartFile certificateFile) {
        this.certificateFile = certificateFile;
    }

}
