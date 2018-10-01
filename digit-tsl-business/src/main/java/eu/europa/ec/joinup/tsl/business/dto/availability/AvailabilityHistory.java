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

import java.util.ArrayList;
import java.util.List;

import eu.europa.ec.joinup.tsl.model.enums.AvailabilityStatus;

public class AvailabilityHistory {

    private List<Availability> availabilityChartList;
    private List<AvailabilityState> unavailableList;
    private AvailabilityPieChart availabilityPieChart;

    public List<Availability> getAvailabilityChartList() {
        return availabilityChartList;
    }

    public void setAvailabilityChartList(List<Availability> availabilityChartList) {
        this.availabilityChartList = availabilityChartList;
    }

    public List<AvailabilityState> getUnavailableList() {
        return unavailableList;
    }

    public void setUnavailableList(List<AvailabilityState> unavailableList) {
        this.unavailableList = new ArrayList<>();
        for (AvailabilityState state : unavailableList) {
            if (!state.getStatus().equals(AvailabilityStatus.AVAILABLE)) {
                this.unavailableList.add(state);
            }
        }
    }

    public AvailabilityPieChart getAvailabilityPieChart() {
        return availabilityPieChart;
    }

    public void setAvailabilityPieChart(AvailabilityPieChart availabilityPieChart) {
        this.availabilityPieChart = availabilityPieChart;
    }

    /* ----- ----- Occurence calculator ----- ----- */

    public int getUnavailableEntryOccurence() {
        return getStateXStatusEntryOccurence(AvailabilityStatus.UNAVAILABLE);
    }

    public int getUnsupportedEntryOccurence() {
        return getStateXStatusEntryOccurence(AvailabilityStatus.UNSUPPORTED);
    }

    private int getStateXStatusEntryOccurence(AvailabilityStatus status) {
        int nb = 0;
        for (AvailabilityState state : unavailableList) {
            if (state.getStatus().equals(status)) {
                nb = nb + 1;
            }
        }
        return nb;
    }

}
