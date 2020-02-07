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
        assertTrue(countryService.isExist("BE")); // Cacheable 2 queries for 3 calls
    }

    @Test
    public void getPropertiesCountry() {
        List<Properties> list = countryService.getPropertiesCountry();
        Assert.assertNotNull(list);
        Assert.assertEquals(32, list.size());
    }

    @Test
    public void add() {
        DBCountries c = new DBCountries();
        c.setCodeTerritory("TE");
        c.setCountryName("TEST");
        Properties p = countryService.add(c);
        Assert.assertNotNull(p);
        Assert.assertEquals("COUNTRYCODENAME", p.getCodeList());
    }

}
