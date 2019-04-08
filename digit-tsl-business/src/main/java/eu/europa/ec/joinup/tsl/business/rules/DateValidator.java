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
package eu.europa.ec.joinup.tsl.business.rules;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.esig.jaxb.v5.utils.JaxbGregorianCalendarZulu;

@Service
public class DateValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateValidator.class);

    public boolean isDateInThePast(Date date) {
        if (date != null) {
            Date now = new Date();
            return now.after(date);
        }
        return false;
    }

    public boolean isDateInTheFuture(Date date) {
        if (date != null) {
            Date now = new Date();
            return now.before(date) || now.equals(date);
        }
        return false;
    }

    public boolean isDateDifferenceOfMax6Months(Date issueDate, Date nextUpdate) {
        if ((issueDate != null) && (nextUpdate != null)) {

            XMLGregorianCalendar xmlIssue = TLUtils.toXMLGregorianDate(issueDate);
            XMLGregorianCalendar xmlNextUpdate = TLUtils.toXMLGregorianDate(nextUpdate);
            JaxbGregorianCalendarZulu adaptater = new JaxbGregorianCalendarZulu();

            SimpleDateFormat formatGMT2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            formatGMT2.setTimeZone(TimeZone.getDefault());
            String zuluIssue = "";
            String zuluNext = "";
            Date dtIssue2 = null;
            Date dtNext2 = null;
            try {
                zuluIssue = adaptater.marshal(xmlIssue);
                zuluNext = adaptater.marshal(xmlNextUpdate);
                dtIssue2 = formatGMT2.parse(zuluIssue);
                dtNext2 = formatGMT2.parse(zuluNext);
            } catch (Exception e) {
                LOGGER.error("IsDateDifferenceOfMax6Months error " + e);
            }

            Calendar endCalendar = new GregorianCalendar();
            endCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
            endCalendar.setTime(dtNext2);

            Calendar exceededCalendar = Calendar.getInstance();
            exceededCalendar.setTimeZone(TimeZone.getDefault());
            exceededCalendar.setTime(dtIssue2);
            // exceededCalendar.setTime(issueDate);
            exceededCalendar.add(Calendar.MONTH, 6);

            return endCalendar.before(exceededCalendar) || endCalendar.equals(exceededCalendar);
        }
        return false;
    }

}
