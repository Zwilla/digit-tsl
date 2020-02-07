package eu.europa.ec.joinup.tsl.business.xslt;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;

import eu.europa.ec.joinup.tsl.business.util.TLUtils;

/**
 * Notification report PDF method
 */
public class NotificationReportTranslator {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("messages");

    public static String translateTableSecondColumn(String country) {
        return bundle.getString("pdf.notification.table.measure") + " " + country + " TLSO";
    }

    public static String translateTableIntro(String country) {
        return bundle.getString("tNotification.change.info").replace("%MS%", country);
    }

    public static String translatePointersIntro(String country) {
        return bundle.getString("pdf.notification.pointers.intro").replace("%MS%", country);
    }

    static public String translate(String key) {
        return bundle.getString(key);
    }

    public static String skipLineNotification(String str) {
        String[] parts = str.split("&#xA;");
        StringBuilder result = new StringBuilder();
        for (String p : parts) {
            result.append("	<fo:block>").append(p).append("</fo:block>");
        }
        return result.toString();
    }

    public static String replaceLineNotification(String str) {
        return str.replace("&#xA;", "\n\n");
    }

    static public String toDateFormat(String sDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date = df.parse(sDate);
            return TLUtils.toStringFormatZ(date);
        } catch (ParseException e) {
            return "";
        }
    }

}
