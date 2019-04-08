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

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class TLMergeResultDTO {

    private MergeResultDTO siResult;
    private MergeResultDTO pointerResult;
    private MergeResultDTO tspResult;
    private Set<Integer> draftIds;

    public TLMergeResultDTO() {
        super();
        this.siResult = new MergeResultDTO();
        this.pointerResult = new MergeResultDTO();
        this.tspResult = new MergeResultDTO();
        this.draftIds = new TreeSet<Integer>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
    }

    public String getDraftIdsConcat() {
        String concat = "";
        Iterator<Integer> iterator = draftIds.iterator();
        while (iterator.hasNext()) {
            concat = concat + iterator.next();
            if (iterator.hasNext()) {
                concat = concat + "+";
            }
        }
        return concat;
    }

    public MergeResultDTO getSiResult() {
        return siResult;
    }

    public void setSiResult(MergeResultDTO siResult) {
        this.siResult = siResult;
    }

    public MergeResultDTO getPointerResult() {
        return pointerResult;
    }

    public void setPointerResult(MergeResultDTO pointerResult) {
        this.pointerResult = pointerResult;
    }

    public MergeResultDTO getTspResult() {
        return tspResult;
    }

    public void setTspResult(MergeResultDTO tspResult) {
        this.tspResult = tspResult;
    }

    public Set<Integer> getDraftIds() {
        return draftIds;
    }

    public void setDraftIds(Set<Integer> draftIds) {
        this.draftIds = draftIds;
    }
}
