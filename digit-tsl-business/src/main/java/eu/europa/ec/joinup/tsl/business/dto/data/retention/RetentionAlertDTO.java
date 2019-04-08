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
package eu.europa.ec.joinup.tsl.business.dto.data.retention;

public class RetentionAlertDTO {

    private int nbDraftstore;
    private int nbTLs;
    private int nbDraftstoreDeleted;
    private int nbTLsDeleted;

    public RetentionAlertDTO(int nbDraftstore, int nbTLs, int nbDraftstoreDeleted, int nbTLsDeleted) {
        super();
        this.nbDraftstore = nbDraftstore;
        this.nbTLs = nbTLs;
        this.nbDraftstoreDeleted = nbDraftstoreDeleted;
        this.nbTLsDeleted = nbTLsDeleted;
    }

    public int getNbDraftstore() {
        return nbDraftstore;
    }

    public void setNbDraftstore(int nbDraftstore) {
        this.nbDraftstore = nbDraftstore;
    }

    public int getNbTLs() {
        return nbTLs;
    }

    public void setNbTLs(int nbTLs) {
        this.nbTLs = nbTLs;
    }

    public int getNbDraftstoreDeleted() {
        return nbDraftstoreDeleted;
    }

    public void setNbDraftstoreDeleted(int nbDraftstoreDeleted) {
        this.nbDraftstoreDeleted = nbDraftstoreDeleted;
    }

    public int getNbTLsDeleted() {
        return nbTLsDeleted;
    }

    public void setNbTLsDeleted(int nbTLsDeleted) {
        this.nbTLsDeleted = nbTLsDeleted;
    }

}
