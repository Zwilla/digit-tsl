/*******************************************************************************
 * DIGIT-TSL - Trusted List Manager
 * Copyright (C) 2018 European Commission, provided under the CEF E-Signature programme
 * 
 * This file is part of the "DIGIT-TSL - Trusted List Manager" project.
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
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

    private static ResourceBundle bundle = ResourceBundle.getBundle("messages");

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
        String result = "";
        for (String p : parts) {
            result = result + "	<fo:block>" + p + "</fo:block>";
        }
        return result;
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
