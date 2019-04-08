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
