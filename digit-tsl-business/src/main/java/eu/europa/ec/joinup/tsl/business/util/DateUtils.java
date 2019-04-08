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
package eu.europa.ec.joinup.tsl.business.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils {

    /**
     * Format date to YYYY-MM-DD HH:mm:ss
     *
     * @param date
     */
    public static String getToFormatYMDHMS(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }

    /**
     * Format date to YYYY-MM-DD HH:mm:ss
     *
     * @param date
     */
    public static String getToFormatYMDH(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    /**
     * Set date to start of the day (0:0:0:0)
     *
     * @param date
     * @return
     */
    public static Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * Set date to end of the day (23:59:59:999)
     *
     * @param date
     * @return
     */
    public static Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static int getDifferenceBetweenDatesInDays(Date d1, Date d2) {
        d1 = getEndOfDay(d1);
        d2 = getEndOfDay(d2);

        int differenceDays = 0;
        if (d1.before(d2)) {
            // D1 is expired
            long differenceMS = d2.getTime() - d1.getTime();
            differenceDays = (int) TimeUnit.DAYS.convert(differenceMS, TimeUnit.MILLISECONDS) * (-1);
        } else {
            // D1 is not expired yet
            long differenceMS = d1.getTime() - d2.getTime();
            differenceDays = (int) TimeUnit.DAYS.convert(differenceMS, TimeUnit.MILLISECONDS);
        }
        return differenceDays;
    }

    /**
     * Return true if d1 is after d2 (compare date without time)
     */
    public static boolean compareDateByDay(Date d1, Date d2) {
        if (d1 == null) {
            return false;
        } else {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date dayOfSigning = formatter.parse(formatter.format(d1));
                Date dayOfPublication = formatter.parse(formatter.format(d2));
                return dayOfSigning.after(dayOfPublication);
            } catch (ParseException e) {
                return false;
            }
        }
    }

    /**
     * Return true if date equals (false if not or null)
     * 
     * @param d1
     * @param d2
     */
    public static boolean compareDateNotNull(Date d1, Date d2) {
        if (d1 != null && d2 != null && d1.equals(d2)) {
            return true;
        }
        return false;
    }
}
