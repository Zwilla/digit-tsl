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
package eu.europa.ec.joinup.tsl.business.dto.availability;

public class AvailabilityPieChart {

    private long availableTiming;
    private long unavailableTiming;
    private long unsupportedTiming;

    public AvailabilityPieChart() {
        availableTiming = 0;
        unavailableTiming = 0;
        unsupportedTiming = 0;
    }

    public long getAvailableTiming() {
        return availableTiming;
    }

    public void setAvailableTiming(long availableTiming) {
        this.availableTiming = availableTiming;
    }

    public long getUnavailableTiming() {
        return unavailableTiming;
    }

    public void setUnavailableTiming(long unavailableTiming) {
        this.unavailableTiming = unavailableTiming;
    }

    public long getUnsupportedTiming() {
        return unsupportedTiming;
    }

    public void setUnsupportedTiming(long unsupportedTiming) {
        this.unsupportedTiming = unsupportedTiming;
    }

    public void addTime(AvailabilityState state) {
        switch (state.getStatus()) {
        case AVAILABLE:
            availableTiming = availableTiming + state.getTiming();
            break;
        case UNAVAILABLE:
            unavailableTiming = unavailableTiming + state.getTiming();
            break;
        case UNSUPPORTED:
            unsupportedTiming = unsupportedTiming + state.getTiming();
            break;
        }

    }

}
