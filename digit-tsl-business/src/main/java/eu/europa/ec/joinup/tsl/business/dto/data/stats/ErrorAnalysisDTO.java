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

import java.util.Map;
import java.util.TreeMap;

import eu.europa.ec.joinup.tsl.model.DBCheck;

public class ErrorAnalysisDTO {

	private DBCheck check = null;
	private Map<String, Integer> resultMap = new TreeMap<String, Integer>();
	private int totalCheck;
	private String tlImpactedCc;
	private int tlImpacted;

	public DBCheck getCheck() {
		return check;
	}

	public void setCheck(DBCheck check) {
		this.check = check;
	}

	public Map<String, Integer> getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map<String, Integer> resultMap) {
		this.resultMap = resultMap;
	}

	public int getTotalCheck() {
		return totalCheck;
	}

	public void setTotalCheck(int totalCheck) {
		this.totalCheck = totalCheck;
	}

	public int getTlImpacted() {
		return tlImpacted;
	}

	public void setTlImpacted(int tlImpacted) {
		this.tlImpacted = tlImpacted;
	}

	public String getTlImpactedCc() {
		return tlImpactedCc;
	}

	public void setTlImpactedCc(String tlImpactedCc) {
		this.tlImpactedCc = tlImpactedCc;
	}
}
