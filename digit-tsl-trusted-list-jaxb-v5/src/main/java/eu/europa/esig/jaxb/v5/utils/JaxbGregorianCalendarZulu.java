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
package eu.europa.esig.jaxb.v5.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class JaxbGregorianCalendarZulu extends XmlAdapter<String, XMLGregorianCalendar> {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String DATE_FORMAT2 = "yyyy-MM-dd'T'HH:mm:ss.S'Z'";
    private static final String DATE_FORMAT3 = "yyyy-MM-dd'T'HH:mm:ss";

    @Override
    public XMLGregorianCalendar unmarshal(String v) {
        SimpleDateFormat formatUTC = new SimpleDateFormat(DATE_FORMAT);
        formatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat formatUTC2 = new SimpleDateFormat(DATE_FORMAT2);
        formatUTC2.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat formatUTC3 = new SimpleDateFormat(DATE_FORMAT3);
        formatUTC3.setTimeZone(TimeZone.getTimeZone("UTC"));
        GregorianCalendar gregorianCalendar = new GregorianCalendar();

        Date dt = null;
        try {
            dt = formatUTC.parse(v);
        } catch (ParseException e) {
        }

        if (dt == null) {
            try {
                dt = formatUTC2.parse(v);
            } catch (ParseException e) {
            }
        }

        if (dt == null) {
            try {
                dt = formatUTC3.parse(v);
            } catch (ParseException e) {
            }
        }

        if (dt == null) {
            System.out.println("ERROR WHEN PARSING DATE : " + v + "[Unknow date format]");
        }

        gregorianCalendar.setTime(dt);
        XMLGregorianCalendar calendar = null;

        try {
            calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
        return calendar;

    }

    @Override
    public String marshal(XMLGregorianCalendar v) throws Exception {
        SimpleDateFormat formatUTC = new SimpleDateFormat(DATE_FORMAT);
        formatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatUTC.format(v.toGregorianCalendar().getTime());
    }

}
