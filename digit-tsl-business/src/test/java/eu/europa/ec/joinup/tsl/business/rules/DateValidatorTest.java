package eu.europa.ec.joinup.tsl.business.rules;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;

public class DateValidatorTest extends AbstractSpringTest {

    @Autowired
    private DateValidator dateValidator;

    @Test
    public void isDateInThePast() {
        assertFalse(dateValidator.isDateInThePast(null));

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 1);
        assertFalse(dateValidator.isDateInThePast(calendar.getTime()));

        calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -1);
        assertTrue(dateValidator.isDateInThePast(calendar.getTime()));
    }

    @Test
    public void isDateInTheFuture() {
        assertFalse(dateValidator.isDateInTheFuture(null));

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -1);
        assertFalse(dateValidator.isDateInTheFuture(calendar.getTime()));

        calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 1);
        assertTrue(dateValidator.isDateInTheFuture(calendar.getTime()));
    }

    @Test
    public void isDateDifferenceOfMax6Months() {
        assertFalse(dateValidator.isDateDifferenceOfMax6Months(null, null));

        Calendar calendar = Calendar.getInstance();
        assertFalse(dateValidator.isDateDifferenceOfMax6Months(calendar.getTime(), null));
        assertFalse(dateValidator.isDateDifferenceOfMax6Months(null, calendar.getTime()));

        Calendar diff = Calendar.getInstance();
        assertTrue(dateValidator.isDateDifferenceOfMax6Months(calendar.getTime(), diff.getTime()));

        diff.add(Calendar.MONTH, 5);
        assertTrue(dateValidator.isDateDifferenceOfMax6Months(calendar.getTime(), diff.getTime()));

        diff.add(Calendar.DAY_OF_YEAR, 27);
        assertTrue(dateValidator.isDateDifferenceOfMax6Months(calendar.getTime(), diff.getTime()));

        diff.add(Calendar.DAY_OF_YEAR, 5);
        assertFalse(dateValidator.isDateDifferenceOfMax6Months(calendar.getTime(), diff.getTime()));
    }

}
