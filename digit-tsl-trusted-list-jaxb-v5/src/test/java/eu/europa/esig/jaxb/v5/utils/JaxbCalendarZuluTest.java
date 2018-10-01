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
package eu.europa.esig.jaxb.v5.utils;

import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.Assert;
import org.junit.Test;

public class JaxbCalendarZuluTest {

	@Test
	public void test1() throws Exception {
		
		
		JaxbGregorianCalendarZulu z = new JaxbGregorianCalendarZulu();
		String v = "2009-05-05T14:00:00Z";
		XMLGregorianCalendar cal = z.unmarshal(v);
		
		System.out.println(cal);
		
		System.out.println(z.marshal(cal));
		
		Assert.assertEquals(v, z.marshal(z.unmarshal(v)));
		
	}
	
}
