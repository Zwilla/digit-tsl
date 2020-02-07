package eu.europa.ec.joinup.tsl.web.form;

import eu.europa.ec.joinup.tsl.business.dto.notification.NotificationDto;

public class TLNotificationEdition {

    private NotificationDto notification;
    private int tlId;
    private String cookie;
    private Boolean newDraft;

    public NotificationDto getNotification() {
        return notification;
    }

    public void setNotification(NotificationDto notification) {
        this.notification = notification;
    }

    public int getTlId() {
        return tlId;
    }

    public void setTlId(int tlId) {
        this.tlId = tlId;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public Boolean getNewDraft() {
        return newDraft;
    }

    public void setNewDraft(Boolean newDraft) {
        this.newDraft = newDraft;
    }

}
