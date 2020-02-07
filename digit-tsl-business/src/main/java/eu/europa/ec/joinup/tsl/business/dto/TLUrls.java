package eu.europa.ec.joinup.tsl.business.dto;

public class TLUrls {

    private String xmlUrl;
    private String pdfUrl;
    private String sha2Url;

    public TLUrls() {
    }

    public String getXmlUrl() {
        return xmlUrl;
    }

    public void setXmlUrl(String xmlUrl) {
        this.xmlUrl = xmlUrl;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getSha2Url() {
        return sha2Url;
    }

    public void setSha2Url(String sha2Url) {
        this.sha2Url = sha2Url;
    }

}
