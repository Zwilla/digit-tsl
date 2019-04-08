package eu.europa.ec.joinup.tsl.business.dto;

import java.util.ArrayList;
import java.util.List;

public class MailDTO {

    private String subject;
    private String to;
    private String cc;
    private String template;
    private List<MailFileDTO> files;

    public MailDTO() {
        super();
        this.subject = "";
        this.files = new ArrayList<>();
    }

    public void addSubject(String subject) {
        this.subject += subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public List<MailFileDTO> getFiles() {
        return files;
    }

    public void setFiles(List<MailFileDTO> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "MailDTO [subject=" + subject + ", to=" + to + ", template=" + template + "]";
    }

}