package eu.europa.ec.joinup.tsl.web.form;

import javax.xml.bind.annotation.XmlSeeAlso;

import eu.europa.ec.joinup.tsl.business.dto.CheckDTO;
import eu.europa.ec.joinup.tsl.business.dto.TLDifference;

@XmlSeeAlso(value = { TLDifference.class, CheckDTO.class })
public class ServiceResponse<T> {

    private String responseStatus;
    private T content;
    private String errorMessage;

    public ServiceResponse() {
        super();
    }

    public ServiceResponse(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessages) {
        this.errorMessage = errorMessages;
    }

}
