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
package eu.europa.ec.joinup.tsl.business.dto.tl.approachbreak;

import java.util.Date;
import java.util.List;

import eu.europa.ec.joinup.tsl.business.util.DateUtils;

/**
 * Break Element with expiration in days (negative if expired), alert and breakDay flag
 */
public class BreakElement {

    private boolean alert;

    private boolean breakDay;

    private int expireIn;

    public BreakElement() {
        super();
        alert = false;
        breakDay = false;
        expireIn = 0;
    }

    /**
     * Loop through @limitList and detect if @elementDate is expired or require alert;
     *
     * @param elementDate
     * @param checkDate
     * @param limitList
     */
    public void verify(Date elementDate, Date checkDate, List<Integer> limitList) {
        expireIn = DateUtils.getDifferenceBetweenDatesInDays(elementDate, checkDate);
        if (expireIn < 0) {
            alert = true;
        } else if (limitList.get(0) > expireIn) {
            alert = true;
            if (limitList.contains(expireIn)) {
                breakDay = true;
            }
        }
    }

    public Boolean isExpired() {
        return expireIn < 0;
    }

    public boolean isAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public boolean isBreakDay() {
        return breakDay;
    }

    public void setBreakDay(boolean breakDay) {
        this.breakDay = breakDay;
    }

    public int getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(int expireIn) {
        this.expireIn = expireIn;
    }

}
