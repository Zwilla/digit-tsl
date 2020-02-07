package eu.europa.ec.joinup.tsl.business.dto;

import java.io.File;
import java.util.Date;

public class LogFileDTO {

    private String fileName;
    private Date lastModificationDate;
    private double size;

    public LogFileDTO() {
        super();
    }

    public LogFileDTO(File file) {
        fileName = file.getName();
        lastModificationDate = new Date(file.lastModified());
        size = file.length();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(Date date) {
        lastModificationDate = date;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "LogFileDTO [fileName=" + fileName + ", lastModificationDate=" + lastModificationDate + ", size=" + size + "]";
    }

}
