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

import java.util.Date;
import java.util.List;

public class CronRetention {

    private List<DraftStoreRetentionDTO> draftstores;
    private DraftStoreRetentionDTO draftTL;
    private Date nextCron;
    private Date lastAccessDate;

    public CronRetention() {
        super();
    }

    public List<DraftStoreRetentionDTO> getDraftstores() {
        return draftstores;
    }

    public void setDraftstores(List<DraftStoreRetentionDTO> draftstores) {
        this.draftstores = draftstores;
    }

    public DraftStoreRetentionDTO getDraftTL() {
        return draftTL;
    }

    public void setDraftTL(DraftStoreRetentionDTO draftTL) {
        this.draftTL = draftTL;
    }

    public Date getNextCron() {
        return nextCron;
    }

    public void setNextCron(Date nextCron) {
        this.nextCron = nextCron;
    }

    public Date getLastAccessDate() {
        return lastAccessDate;
    }

    public void setLastAccessDate(Date lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((draftTL == null) ? 0 : draftTL.hashCode());
        result = prime * result + ((draftstores == null) ? 0 : draftstores.hashCode());
        result = prime * result + ((lastAccessDate == null) ? 0 : lastAccessDate.hashCode());
        result = prime * result + ((nextCron == null) ? 0 : nextCron.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CronRetention other = (CronRetention) obj;
        if (draftTL == null) {
            if (other.draftTL != null)
                return false;
        } else if (!draftTL.equals(other.draftTL))
            return false;
        if (draftstores == null) {
            if (other.draftstores != null)
                return false;
        } else if (!draftstores.equals(other.draftstores))
            return false;
        if (lastAccessDate == null) {
            if (other.lastAccessDate != null)
                return false;
        } else if (!lastAccessDate.equals(other.lastAccessDate))
            return false;
        if (nextCron == null) {
            if (other.nextCron != null)
                return false;
        } else if (!nextCron.equals(other.nextCron))
            return false;
        return true;
    }

}
