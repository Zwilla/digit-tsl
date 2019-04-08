package eu.europa.ec.joinup.tsl.business.dto;

public class MailFileDTO {

    private String fileName;
    private byte[] file;
    private String fileType;

    public MailFileDTO(String fileName, byte[] file, String fileType) {
        super();
        this.fileName = fileName;
        this.file = file;
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

}
