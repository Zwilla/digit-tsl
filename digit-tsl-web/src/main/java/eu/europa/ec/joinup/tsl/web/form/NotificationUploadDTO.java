package eu.europa.ec.joinup.tsl.web.form;

import java.util.List;

import eu.europa.ec.joinup.tsl.business.dto.NotificationPointers;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLCertificate;

public class NotificationUploadDTO {

    private NotificationPointers notificationPointers;

    private List<TLCertificate> publishedCertificateDeleted;

    public NotificationPointers getNotificationPointers() {
        return notificationPointers;
    }

    public void setNotificationPointers(NotificationPointers notificationPointers) {
        this.notificationPointers = notificationPointers;
    }

    public List<TLCertificate> getPublishedCertificateDeleted() {
        return publishedCertificateDeleted;
    }

    public void setPublishedCertificateDeleted(List<TLCertificate> publishedCertificateDeleted) {
        this.publishedCertificateDeleted = publishedCertificateDeleted;
    }

}
