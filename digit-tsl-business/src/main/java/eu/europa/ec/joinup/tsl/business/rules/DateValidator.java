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
            assert dtNext2 != null;
            endCalendar.setTime(dtNext2);

            Calendar exceededCalendar = Calendar.getInstance();
            exceededCalendar.setTimeZone(TimeZone.getDefault());
            assert dtIssue2 != null;
            exceededCalendar.setTime(dtIssue2);
            // exceededCalendar.setTime(issueDate);
            exceededCalendar.add(Calendar.MONTH, 6);

            return endCalendar.before(exceededCalendar) || endCalendar.equals(exceededCalendar);
        }
        return false;
    }

}
