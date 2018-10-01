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
package eu.europa.ec.joinup.tsl.business.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.Properties;
import eu.europa.ec.joinup.tsl.model.DBCountries;

public class CountryServiceTest extends AbstractSpringTest {

    @Autowired
    private CountryService countryService;

    @Test
    public void testIsExists() {
        assertTrue(countryService.isExist("BE"));
        assertFalse(countryService.isExist("be"));
        assertTrue(countryService.isExist("BE")); //Cacheable 2 queries for 3 calls
    }

    @Test
    public void getPropertiesCountry(){
        List<Properties> list = countryService.getPropertiesCountry();
        Assert.assertNotNull(list);
        Assert.assertEquals(32, list.size());
    }

    @Test
    public void add(){
        DBCountries c = new DBCountries();
        c.setCodeTerritory("TE");
        c.setCountryName("TEST");
        Properties p = countryService.add(c);
        Assert.assertNotNull(p);
        Assert.assertEquals("COUNTRYCODENAME", p.getCodeList());
    }

}
