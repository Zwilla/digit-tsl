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

import eu.europa.ec.joinup.tsl.business.constant.ServiceLegalType;

public class StatisticType {

    private ServiceLegalType type;
    private int nbActive;
    private int nbActiveTOB;
    private int nbInactive;
    private int nbInactiveTOB;

    public StatisticType() {
        super();
    }

    public StatisticType(ServiceLegalType type) {
        super();
        this.type = type;
        nbActive = 0;
        nbActiveTOB = 0;
        nbInactive = 0;
        nbInactiveTOB = 0;
    }

    public void incrementCounter(Boolean isActive, Boolean isTOB) {
        if (isActive && !isTOB) {
            nbActive = nbActive + 1;
        } else if (isActive && isTOB) {
            nbActiveTOB = nbActiveTOB + 1;
        } else if (!isActive && !isTOB) {
            nbInactive = nbInactive + 1;
        } else {
            nbInactiveTOB = nbInactiveTOB + 1;
        }
    }

    /**
     * Return if type has at least one service
     */
    public Boolean hasService() {
        return ((nbActive > 0) || (nbActiveTOB > 0) || (nbInactive > 0) || (nbInactiveTOB > 0));
    }

    public int getAllActive() {
        return nbActive + nbActiveTOB;
    }

    public int getAllInactive() {
        return nbInactive + nbInactiveTOB;
    }

    public ServiceLegalType getType() {
        return type;
    }

    public void setType(ServiceLegalType type) {
        this.type = type;
    }

    public int getNbActive() {
        return nbActive;
    }

    public void setNbActive(int nbActive) {
        this.nbActive = nbActive;
    }

    public int getNbActiveTOB() {
        return nbActiveTOB;
    }

    public void setNbActiveTOB(int nbActiveTOB) {
        this.nbActiveTOB = nbActiveTOB;
    }

    public int getNbInactive() {
        return nbInactive;
    }

    public void setNbInactive(int nbInactive) {
        this.nbInactive = nbInactive;
    }

    public int getNbInactiveTOB() {
        return nbInactiveTOB;
    }

    public void setNbInactiveTOB(int nbInactiveTOB) {
        this.nbInactiveTOB = nbInactiveTOB;
    }

}
