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
package eu.europa.ec.joinup.tsl.business.dto.data.stats;

import java.util.Date;

public class StatisticCountry extends StatisticGeneric {

    private int nbTSP;
    private int nbQActive;
    private int nbQTOB;

    public StatisticCountry() {
        super();
    }

    public StatisticCountry(String countryCode, int sequenceNumber, Date extractDate) {
        super(countryCode, sequenceNumber, extractDate);
        nbTSP = 0;
        nbQActive = 0;
        nbQTOB = 0;
    }

    public void incrementNbTSP() {
        nbTSP = nbTSP + 1;
    }

    public void incrementQActive() {
        nbQActive = nbQActive + 1;
    }

    public void incrementQTOB() {
        nbQTOB = nbQTOB + 1;
    }

    public int getNbTSP() {
        return nbTSP;
    }

    public void setNbTSP(int nbTSP) {
        this.nbTSP = nbTSP;
    }

    public int getNbQActive() {
        return nbQActive;
    }

    public void setNbQActive(int nbQActive) {
        this.nbQActive = nbQActive;
    }

    public int getNbQTOB() {
        return nbQTOB;
    }

    public void setNbQTOB(int nbQTOB) {
        this.nbQTOB = nbQTOB;
    }

}
