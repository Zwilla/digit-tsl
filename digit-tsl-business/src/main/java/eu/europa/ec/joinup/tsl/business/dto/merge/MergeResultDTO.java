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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.europa.ec.joinup.tsl.model.enums.Tag;

public class MergeResultDTO {

    private Map<String, List<MergeEntryDTO>> mergeResult;

    public MergeResultDTO() {
        super();
        this.mergeResult = new HashMap<>();
    }

    public void addEntry(String location, Tag tag, MergeStatus status, Object obj, String draft) {
        if (!mergeResult.containsKey(location)) {
            mergeResult.put(location, new ArrayList<MergeEntryDTO>());
        }
        MergeEntryDTO mergeEntry = new MergeEntryDTO(status, tag, obj, draft);
        mergeResult.get(location).add(mergeEntry);
    }

    public Map<String, List<MergeEntryDTO>> getMergeResult() {
        return mergeResult;
    }

    public void setMergeResult(Map<String, List<MergeEntryDTO>> mergeResult) {
        this.mergeResult = mergeResult;
    }
}
