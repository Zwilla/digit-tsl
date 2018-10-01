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

import java.util.ResourceBundle;

/**
 * TL Report PDF method
 */
public class Translator {

    private static ResourceBundle bundle = ResourceBundle.getBundle("messages");

    static public String translate(String key) {
        return bundle.getString(key);
    }

    static public String translatePdfTitle(String countryName) {
        return bundle.getString("pdf.title") + " " + countryName;
    }

    static public String translateChecksResume(String lastTimeChecked) {
        String text = bundle.getString("pdf.checks.resume.info");
        text = text.replace("%TIME%", lastTimeChecked);
        return text;
    }

    static public String translateProduction(String production) {
        if (production.equals("true")) {
            return bundle.getString("pdf.published.tl");
        } else {
            return bundle.getString("pdf.draft.tl");
        }
    }

    static public String translateComparedProduction(String production) {
        if (production.equals("true")) {
            return bundle.getString("pdf.previous.tl");
        } else {
            return bundle.getString("pdf.published.tl");
        }
    }

    static public String translatePageNumber(int current, int total) {
        String text = bundle.getString("pdf.page");
        char firstLetter = Character.toUpperCase(text.charAt(0));
        text = text.substring(1);
        text = firstLetter + text;
        return String.format("%s %d %s %d", text, current, bundle.getString("pdf.of"), total);
    }

    static public String translateStringObj(String str) {
        return String.format("%s", str);
    }

    static public String translateAvailability(String availability) {
        if (availability == "") {
            return String.format("%s %s", " ", bundle.getString("pdf.draftAvailability"));
        } else {
            return String.format("%s %s", " ", availability);
        }
    }

}
