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
package eu.europa.ec.joinup.tsl.business.dto.merge;

import eu.europa.ec.joinup.tsl.model.enums.Tag;

public class MergeEntryDTO {

    private MergeStatus status;
    private Tag tag;
    private Object objectChanged;
    private String draft;

    public MergeEntryDTO(MergeStatus status, Tag tag, Object objectChanged, String draft) {
        super();
        this.status = status;
        this.tag = tag;
        this.objectChanged = objectChanged;
        this.draft = draft;
    }

    public MergeStatus getStatus() {
        return status;
    }

    public void setStatus(MergeStatus status) {
        this.status = status;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Object getObjectChanged() {
        return objectChanged;
    }

    public void setObjectChanged(Object objectChanged) {
        this.objectChanged = objectChanged;
    }

    public String getDraft() {
        return draft;
    }

    public void setDraft(String draft) {
        this.draft = draft;
    }

    @Override
    public String toString() {
        return "MergeEntryDTO [status=" + status + ", tag=" + tag + ", draft=" + draft + "]";
    }

}
