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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.constant.ServiceLegalType;
import eu.europa.ec.joinup.tsl.business.dto.data.stats.StatisticCountry;
import eu.europa.ec.joinup.tsl.business.dto.data.stats.StatisticExtractionCriterias;
import eu.europa.ec.joinup.tsl.business.dto.data.stats.StatisticExtractionType;
import eu.europa.ec.joinup.tsl.business.dto.data.stats.StatisticTSP;
import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.model.DBCountries;

public class TLStatisticServiceTest extends AbstractSpringTest {

    @Autowired
    private TLDataService tlDataService;

    @Autowired
    private TLRepository tlRepository;

    @Autowired
    private CountryService countryService;

    @Autowired
    private TLStatisticService statisticService;

    @Test
    public void beTSPStat() {
        loadTL(countryService.getCountryByTerritory("BE"), "2018" + File.separatorChar + "TL - BE");

        List<StatisticTSP> beStat = statisticService.getCountryStatisticByTSP("BE", new Date());
        Assert.assertNotNull(beStat);
        Assert.assertEquals(6, beStat.size());

        StatisticTSP certipost = beStat.get(0);
        Assert.assertEquals("BE", certipost.getCountryCode());
        Assert.assertEquals("Certipost n.v./s.a.", certipost.getName());
        Assert.assertEquals(6, certipost.getNbService());
        Assert.assertEquals(3, certipost.getTypes().get(ServiceLegalType.Q_CERT_ESIG).getNbActive());
        Assert.assertEquals(3, certipost.getTypes().get(ServiceLegalType.Q_CERT_ESIG).getNbInactive());

        StatisticTSP societyWorldwide = beStat.get(4);
        Assert.assertEquals("BE", societyWorldwide.getCountryCode());
        Assert.assertEquals("Society for Worldwide Interbank Financial Telecommunication SCRL", societyWorldwide.getName());
        Assert.assertEquals(1, societyWorldwide.getNbService());
        Assert.assertEquals(0, societyWorldwide.getTypes().get(ServiceLegalType.Q_CERT_ESIG).getNbActive());
        Assert.assertEquals(1, societyWorldwide.getTypes().get(ServiceLegalType.Q_CERT_ESEAL).getNbActive());

        tlRepository.deleteAll();
        tlDataService.deleteDataByCountry("BE");
    }

    @Test
    public void beCountryStat() {
        loadTL(countryService.getCountryByTerritory("BE"), "2018" + File.separatorChar + "TL - BE");

        StatisticCountry beStat = statisticService.getCountryStatisticByCountry("BE", new Date());
        Assert.assertNotNull(beStat);
        Assert.assertEquals("BE", beStat.getCountryCode());
        Assert.assertEquals(100, beStat.getSequenceNumber());

        //Nb TSP
        Assert.assertEquals(6, beStat.getNbTSP());
        Assert.assertEquals(6, beStat.getNbQActive());
        Assert.assertEquals(0, beStat.getNbQTOB());

        Assert.assertEquals(4, beStat.getTypes().get(ServiceLegalType.Q_CERT_ESIG).getNbActive());
        Assert.assertEquals(2, beStat.getTypes().get(ServiceLegalType.Q_CERT_ESEAL).getNbActive());
        Assert.assertEquals(1, beStat.getTypes().get(ServiceLegalType.Q_ERDS).getNbActive());

        tlRepository.deleteAll();
        tlDataService.deleteDataByCountry("BE");
    }

    @Test
    public void beGenerateCSV() throws IOException {
        loadTL(countryService.getCountryByTerritory("BE"), "2018" + File.separatorChar + "TL - BE");

        StatisticExtractionCriterias criteria = new StatisticExtractionCriterias(StatisticExtractionType.COUNTRY, new Date(), "BE");
        ByteArrayOutputStream out = statisticService.generateCSV(criteria);

        FileUtils.writeByteArrayToFile(new File("src/test/resources/BE_COUNTRY_STATS.csv"), out.toByteArray());

        criteria.setType(StatisticExtractionType.TSP);
        out = statisticService.generateCSV(criteria);

        FileUtils.writeByteArrayToFile(new File("src/test/resources/BE_TSP_STATS.csv"), out.toByteArray());

        tlRepository.deleteAll();
        tlDataService.deleteDataByCountry("BE");
    }

    @Test
    public void plTSPStat() {
        loadTL(countryService.getCountryByTerritory("PL"), "2018" + File.separatorChar + "TL - PL");

        List<StatisticTSP> plStat = statisticService.getCountryStatisticByTSP("PL", new Date());
        Assert.assertNotNull(plStat);
        Assert.assertEquals(10, plStat.size());

        StatisticTSP asseco = plStat.get(0);
        Assert.assertEquals("PL", asseco.getCountryCode());
        Assert.assertEquals("Asseco Data Systems S.A.", asseco.getName());
        Assert.assertEquals(3, asseco.getTypes().get(ServiceLegalType.Q_CERT_ESIG).getNbActive());
        Assert.assertEquals(1, asseco.getTypes().get(ServiceLegalType.Q_CERT_ESIG).getNbInactive());
        Assert.assertEquals(1, asseco.getTypes().get(ServiceLegalType.Q_CERT_ESEAL).getNbActive());
        Assert.assertEquals(3, asseco.getTypes().get(ServiceLegalType.NON_REGULATORY).getNbActive());

        StatisticTSP tpInternet = plStat.get(8);
        Assert.assertEquals("PL", tpInternet.getCountryCode());
        Assert.assertEquals("TP Internet sp. z o.o.", tpInternet.getName());
        Assert.assertEquals(2, tpInternet.getTypes().get(ServiceLegalType.Q_CERT_ESIG).getNbInactive());

        StatisticTSP unizeto = plStat.get(9);
        Assert.assertEquals("PL", unizeto.getCountryCode());
        Assert.assertEquals("Unizeto Technologies S.A.", unizeto.getName());
        Assert.assertEquals(0, unizeto.getTypes().get(ServiceLegalType.Q_CERT_ESIG).getNbActive());
        Assert.assertEquals(1, unizeto.getTypes().get(ServiceLegalType.Q_CERT_ESIG).getNbActiveTOB());
        Assert.assertEquals(4, unizeto.getTypes().get(ServiceLegalType.Q_CERT_ESIG).getNbInactiveTOB());
        Assert.assertEquals(2, unizeto.getTypes().get(ServiceLegalType.Q_PRES_ESIG).getNbInactiveTOB());
        Assert.assertEquals(4, unizeto.getTypes().get(ServiceLegalType.Q_TIMESTAMP).getNbInactiveTOB());
        Assert.assertEquals(6, unizeto.getTypes().get(ServiceLegalType.NON_REGULATORY).getNbInactiveTOB());

        tlRepository.deleteAll();
        tlDataService.deleteDataByCountry("PL");
    }

    @Test
    public void plCountryStat() {
        loadTL(countryService.getCountryByTerritory("PL"), "2018" + File.separatorChar + "TL - PL");

        StatisticCountry plStat = statisticService.getCountryStatisticByCountry("PL", new Date());
        Assert.assertNotNull(plStat);
        Assert.assertEquals("PL", plStat.getCountryCode());
        Assert.assertEquals(100, plStat.getSequenceNumber());

        //Nb TSP
        Assert.assertEquals(10, plStat.getNbTSP());
        Assert.assertEquals(5, plStat.getNbQActive());
        Assert.assertEquals(1, plStat.getNbQTOB());

        Assert.assertEquals(5, plStat.getTypes().get(ServiceLegalType.Q_CERT_ESIG).getNbActive());
        Assert.assertEquals(1, plStat.getTypes().get(ServiceLegalType.Q_CERT_ESIG).getNbActiveTOB());
        Assert.assertEquals(2, plStat.getTypes().get(ServiceLegalType.Q_CERT_ESIG).getNbInactive());
        Assert.assertEquals(1, plStat.getTypes().get(ServiceLegalType.Q_CERT_ESIG).getNbInactiveTOB());

        Assert.assertEquals(1, plStat.getTypes().get(ServiceLegalType.Q_PRES_ESIG).getNbInactive());
        Assert.assertEquals(1, plStat.getTypes().get(ServiceLegalType.Q_PRES_ESIG).getNbInactiveTOB());

        Assert.assertEquals(5, plStat.getTypes().get(ServiceLegalType.Q_TIMESTAMP).getNbActive());
        Assert.assertEquals(1, plStat.getTypes().get(ServiceLegalType.Q_TIMESTAMP).getNbInactive());
        Assert.assertEquals(2, plStat.getTypes().get(ServiceLegalType.Q_TIMESTAMP).getNbInactiveTOB());

        Assert.assertEquals(2, plStat.getTypes().get(ServiceLegalType.NON_REGULATORY).getNbActive());
        Assert.assertEquals(1, plStat.getTypes().get(ServiceLegalType.NON_REGULATORY).getNbInactiveTOB());

        tlRepository.deleteAll();
        tlDataService.deleteDataByCountry("PL");
    }

    @Test
    public void plGenerateCSV() throws IOException {
        loadTL(countryService.getCountryByTerritory("PL"), "2018" + File.separatorChar + "TL - PL");

        StatisticExtractionCriterias criteria = new StatisticExtractionCriterias(StatisticExtractionType.COUNTRY, new Date(), "PL");
        ByteArrayOutputStream out = statisticService.generateCSV(criteria);

        FileUtils.writeByteArrayToFile(new File("src/test/resources/PL_COUNTRY_STATS.csv"), out.toByteArray());

        criteria.setType(StatisticExtractionType.TSP);
        out = statisticService.generateCSV(criteria);

        FileUtils.writeByteArrayToFile(new File("src/test/resources/PL_TSP_STATS.csv"), out.toByteArray());

        tlRepository.deleteAll();
        tlDataService.deleteDataByCountry("PL");
    }

    @Test
    public void getAllCountries() throws IOException {
        loadAllTLs("2018" + File.separatorChar);

        StatisticExtractionCriterias criteria = new StatisticExtractionCriterias(StatisticExtractionType.COUNTRY, new Date());
        ByteArrayOutputStream out = statisticService.generateCSV(criteria);

        FileUtils.writeByteArrayToFile(new File("src/test/resources/TLS_COUNTRY_STATS.csv"), out.toByteArray());

        criteria.setType(StatisticExtractionType.TSP);
        out = statisticService.generateCSV(criteria);

        FileUtils.writeByteArrayToFile(new File("src/test/resources/TLS_TSP_STATS.csv"), out.toByteArray());

        tlRepository.deleteAll();
        for (DBCountries country : countryService.getAll()) {
            tlDataService.deleteDataByCountry(country.getCodeTerritory());
        }
    }

    @SuppressWarnings("deprecation")
    @Test
    public void luHistory() throws IOException {
        loadTL(countryService.getCountryByTerritory("LU"), "LU_HISTORY");

        List<StatisticTSP> luStat = statisticService.getCountryStatisticByTSP("LU", new Date());
        Assert.assertNotNull(luStat);

        StatisticTSP luxTrust = luStat.get(0);
        Assert.assertEquals("LuxTrust S.A.", luxTrust.getName());
        Assert.assertEquals(4, luxTrust.getNbService());
        Assert.assertEquals(1, luxTrust.getTypes().get(ServiceLegalType.Q_CERT_ESIG).getNbInactive());
        Assert.assertEquals(1, luxTrust.getTypes().get(ServiceLegalType.Q_WAC).getNbActive());
        Assert.assertEquals(1, luxTrust.getTypes().get(ServiceLegalType.Q_TIMESTAMP).getNbActive());
        Assert.assertEquals(1, luxTrust.getTypes().get(ServiceLegalType.Q_TIMESTAMP).getNbInactive());


        //2017-01-01
        luStat = statisticService.getCountryStatisticByTSP("LU", new Date(117,0,1));
        luxTrust = luStat.get(0);
        Assert.assertEquals(2, luxTrust.getNbService());
        Assert.assertEquals(1, luxTrust.getTypes().get(ServiceLegalType.Q_CERT_ESIG).getNbActive());
        Assert.assertEquals(0, luxTrust.getTypes().get(ServiceLegalType.Q_CERT_ESIG).getNbInactive());
        Assert.assertEquals(0, luxTrust.getTypes().get(ServiceLegalType.Q_WAC).getNbActive());
        Assert.assertEquals(1, luxTrust.getTypes().get(ServiceLegalType.Q_TIMESTAMP).getNbActive());
        Assert.assertEquals(0, luxTrust.getTypes().get(ServiceLegalType.Q_TIMESTAMP).getNbInactive());

        //2013-01-01
        luStat = statisticService.getCountryStatisticByTSP("LU", new Date(113,0,1));
        luxTrust = luStat.get(0);
        Assert.assertEquals(1, luxTrust.getNbService());
        Assert.assertEquals(0, luxTrust.getTypes().get(ServiceLegalType.Q_CERT_ESIG).getNbActive());
        Assert.assertEquals(0, luxTrust.getTypes().get(ServiceLegalType.Q_CERT_ESIG).getNbInactive());
        Assert.assertEquals(0, luxTrust.getTypes().get(ServiceLegalType.Q_WAC).getNbActive());
        Assert.assertEquals(0, luxTrust.getTypes().get(ServiceLegalType.Q_TIMESTAMP).getNbActive());
        Assert.assertEquals(0, luxTrust.getTypes().get(ServiceLegalType.Q_TIMESTAMP).getNbInactive());
        Assert.assertEquals(1, luxTrust.getTypes().get(ServiceLegalType.UNDEFINED).getNbInactive());

        tlRepository.deleteAll();
        tlDataService.deleteDataByCountry("LU");
    }

}
