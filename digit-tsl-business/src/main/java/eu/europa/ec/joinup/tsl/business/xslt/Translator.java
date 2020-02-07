package eu.europa.ec.joinup.tsl.business.xslt;

import java.util.ResourceBundle;

/**
 * TL Report PDF method
 */
public class Translator {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("messages");

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
        if (availability.equals("")) {
            return String.format("%s %s", " ", bundle.getString("pdf.draftAvailability"));
        } else {
            return String.format("%s %s", " ", availability);
        }
    }

}
