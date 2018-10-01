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
package eu.europa.ec.joinup.tsl.business.dto.notification;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("changes")
public class PDFMeasure {

    private List<String> change;
    private List<String> measure;

    public PDFMeasure() {
        super();
        this.change = new ArrayList<String>();
        this.measure = new ArrayList<String>();
    }

    public void addChange(String str) {
        change.add(str);
    }

    public void addMeasure(String str) {
        measure.add(str);
    }

    public List<String> getChange() {
        return change;
    }

    public void setChange(List<String> change) {
        this.change = change;
    }

    public List<String> getMeasure() {
        return measure;
    }

    public void setMeasure(List<String> measure) {
        this.measure = measure;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((change == null) ? 0 : change.hashCode());
        result = (prime * result) + ((measure == null) ? 0 : measure.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PDFMeasure other = (PDFMeasure) obj;
        if (change == null) {
            if (other.change != null) {
                return false;
            }
        } else if (!change.equals(other.change)) {
            return false;
        }
        if (measure == null) {
            if (other.measure != null) {
                return false;
            }
        } else if (!measure.equals(other.measure)) {
            return false;
        }
        return true;
    }

}
