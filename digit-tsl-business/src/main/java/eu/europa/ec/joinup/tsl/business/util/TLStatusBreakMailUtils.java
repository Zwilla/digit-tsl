package eu.europa.ec.joinup.tsl.business.util;

import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.collections.CollectionUtils;

import eu.europa.ec.joinup.tsl.business.dto.tl.approachbreak.CertificateElement;
import eu.europa.ec.joinup.tsl.business.dto.tl.approachbreak.TLBreakStatus;

public class TLStatusBreakMailUtils {

    private static final String dash = " - ";

    private static final ResourceBundle bundle = ResourceBundle.getBundle("messages");

    public static String getContent(TLBreakStatus tlStatus) {
        StringBuilder builder = new StringBuilder();

        builder.append("<div>").append(bundle.getString("mail.dear.tlso")).append(",</div><br/>");

        builder.append("<div>").append(bundle.getString("mail.approach.break.alert.disclaimer")).append("</div><br/>");

        // Certificates expiration
        List<CertificateElement> certLimitReach = tlStatus.retrieveCertificatesLimitReach();
        if (!CollectionUtils.isEmpty(certLimitReach)) {
            builder.append("<div>").append(bundle.getString("mail.approach.break.cert.list").replace("%COUNTRY%", tlStatus.getTl().getTerritory().getCountryName())).append("</div>");
            for (CertificateElement certificate : certLimitReach) {
                builder.append("<div>");
                builder.append(dash + "<b>C").append(certificate.getIndex()).append("</b>: ").append(certificate.getSubjectShortName()).append(": ").append(DateUtils.getToFormatYMDHMS(certificate.getNotBefore())).append(" - ").append(DateUtils.getToFormatYMDHMS(certificate.getNotAfter())).append(" ");
                if (certificate.getExpirationIn() == 0) {
                    builder.append(bundle.getString("mail.approach.break.cert.today.expire"));
                } else if (certificate.isExpired()) {
                    builder.append(bundle.getString("mail.approach.break.cert.has.expired").replace("%NB_DAYS%", Integer.toString(certificate.getExpirationIn() * -1)));
                } else {
                    builder.append(bundle.getString("mail.approach.break.cert.will.expire").replace("%NB_DAYS%", Integer.toString(certificate.getExpirationIn())));
                }
                builder.append("</div>");
            }
            builder.append("<br/>");
        }

        // Certificate break
        if (certificateAlert(tlStatus)) {

            // Two Certificates
            if (tlStatus.getSigningCertificateElement().isAlert()) {
                builder.append("<div>" + dash);
                if (!tlStatus.getSigningCertificateElement().isVerifiable()) {
                    builder.append(bundle.getString("mail.approach.break.alert.signing.cert.unverifiable"));
                } else {
                    builder.append(listCertificates(tlStatus.getCertificates(), tlStatus.getSigningCertificateElement().getCertificatesAffected()));
                    if (tlStatus.getSigningCertificateElement().getExpireIn() == 0) {
                        builder.append(bundle.getString("mail.approach.break.alert.signing.cert.today.expire"));
                    } else if (tlStatus.getSigningCertificateElement().isExpired()) {
                        builder.append(bundle.getString("mail.approach.break.alert.signing.cert.has.expired").replace("%NB_DAYS%",
                                Integer.toString(tlStatus.getSigningCertificateElement().getExpireIn() * -1)));
                    } else {
                        builder.append(
                                bundle.getString("mail.approach.break.alert.signing.cert.will.expire").replace("%NB_DAYS%", Integer.toString(tlStatus.getSigningCertificateElement().getExpireIn())));
                    }
                }
                builder.append("</div>");
            }

            // Two Certificates
            if (tlStatus.getTwoCertificatesElement().isAlert()) {
                builder.append("<div>" + dash);
                if (!tlStatus.getTwoCertificatesElement().isVerifiable()) {
                    builder.append(bundle.getString("mail.approach.break.alert.two.certs.unverifiable"));
                } else {
                    builder.append(listCertificates(tlStatus.getCertificates(), tlStatus.getTwoCertificatesElement().getCertificatesAffected()));
                    if (tlStatus.getTwoCertificatesElement().getExpireIn() > 0) {
                        builder.append(bundle.getString("mail.approach.break.alert.two.certs.will.expire").replace("%NB_DAYS%", Integer.toString(tlStatus.getTwoCertificatesElement().getExpireIn())));
                    } else if (tlStatus.getTwoCertificatesElement().getExpireIn() == 0) {
                        builder.append(bundle.getString("mail.approach.break.alert.two.certs.today.expire"));
                    }
                }
                builder.append("</div>");
            }

            // ShiftedValidityPeriod
            if (tlStatus.getShiftedPeriodElement().isAlert()) {
                builder.append("<div>" + dash);
                if (!tlStatus.getShiftedPeriodElement().isVerifiable()) {
                    builder.append(bundle.getString("mail.approach.break.alert.shift.period.unverifiable"));
                } else {
                    builder.append(listCertificates(tlStatus.getCertificates(), tlStatus.getShiftedPeriodElement().getCertificatesAffected()));
                    if (tlStatus.getShiftedPeriodElement().getExpireIn() == 0) {
                        builder.append(bundle.getString("mail.approach.break.alert.shift.period.today.expire"));
                    } else if (tlStatus.getShiftedPeriodElement().isExpired()) {
                        builder.append(
                                bundle.getString("mail.approach.break.alert.shift.period.has.expired").replace("%NB_DAYS%", Integer.toString(tlStatus.getShiftedPeriodElement().getExpireIn() * -1)));
                    } else {
                        builder.append(bundle.getString("mail.approach.break.alert.shift.period.will.expire").replace("%NB_DAYS%", Integer.toString(tlStatus.getShiftedPeriodElement().getExpireIn())));
                    }
                }
                builder.append(bundle.getString("mail.approach.break.alert.shift.period.info"));
                builder.append("</div>");
            }

            builder.append("<br/>");
        }

        // Next Update Date
        if (tlStatus.getNextUpdateDateElement().isAlert()) {
            builder.append("<div>").append(bundle.getString("mail.approach.break.consequences.next.update").replace("%COUNTRY%", tlStatus.getTl().getTerritory().getCountryName()));
            if (tlStatus.getNextUpdateDateElement().getExpireIn() == 0) {
                builder.append(bundle.getString("mail.approach.break.alert.next.update.today.expire"));
            } else if (tlStatus.getNextUpdateDateElement().isExpired()) {
                builder.append(bundle.getString("mail.approach.break.alert.next.update.has.expired").replace("%NB_DAYS%", Integer.toString(tlStatus.getNextUpdateDateElement().getExpireIn() * -1)));
            } else {
                builder.append(bundle.getString("mail.approach.break.alert.next.update.will.expire").replace("%NB_DAYS%", Integer.toString(tlStatus.getNextUpdateDateElement().getExpireIn())));
            }
            builder.append("</div><br/>");

            builder.append("<div>").append(bundle.getString("mail.approach.break.consequences.next.update2")).append("</div><br/>");
        }

        builder.append("<div>").append(bundle.getString("mail.approach.break.alert.footer").replace("%COUNTRY%", tlStatus.getTl().getTerritory().getCountryName())).append("</div><br/>");

        builder.append("<div>").append(bundle.getString("mail.best.regards")).append("</div>").append("<div>").append(bundle.getString("mail.ec.team")).append("<div>");

        return builder.toString();
    }

    private static boolean certificateAlert(TLBreakStatus tlStatus) {
        return tlStatus.getSigningCertificateElement().isAlert() || tlStatus.getTwoCertificatesElement().isAlert() || tlStatus.getShiftedPeriodElement().isAlert();
    }

    private static String listCertificates(List<CertificateElement> certificates, List<CertificateElement> certificatesAffected) {
        StringBuilder builder = new StringBuilder();
        builder.append(bundle.getString("mail.approach.break.due.to"));
        for (int i = 0; i < certificatesAffected.size(); i++) {
            builder.append("<b>C").append(getCertificateIndex(certificates, certificatesAffected.get(i))).append("</b>");
            if (i < (certificatesAffected.size() - 1)) {
                builder.append(" & ");
            }
        }
        builder.append(" : ");
        return builder.toString();
    }

    private static String getCertificateIndex(List<CertificateElement> certificates, CertificateElement certificateElement) {
        for (CertificateElement certificate : certificates) {
            if (certificate.getBase64().equals(certificateElement.getBase64())) {
                return Integer.toString(certificate.getIndex());
            }
        }
        return "undefined";
    }

    public static String getSubject(TLBreakStatus breakStatus) {
        if (breakStatus.getNextUpdateDateElement().isAlert() && certificateAlert(breakStatus)) {
            return bundle.getString("mail.approach.break.alert.subject_both").replaceAll("%COUNTRY%", breakStatus.getTl().getTerritory().getCountryName());
        } else if (breakStatus.getNextUpdateDateElement().isAlert()) {
            return bundle.getString("mail.approach.break.alert.subject_next_update").replaceAll("%COUNTRY%", breakStatus.getTl().getTerritory().getCountryName());
        } else if (certificateAlert(breakStatus)) {
            return bundle.getString("mail.approach.break.alert.subject_signing_certificates").replaceAll("%COUNTRY%", breakStatus.getTl().getTerritory().getCountryName());
        }
        return bundle.getString("mail.approach.break.alert.subject_undefined").replaceAll("%COUNTRY%", breakStatus.getTl().getTerritory().getCountryName());
    }

}
