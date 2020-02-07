package eu.europa.ec.joinup.tsl.business.service;

import java.io.File;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.Load;
import eu.europa.ec.joinup.tsl.business.dto.TrustedListsReport;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;

@Transactional
public class TLServiceTest extends AbstractSpringTest {

    private static final String EU_COUNTRY_CODE = "EU";

    @Autowired
    private TLLoader tlLoader;

    @Autowired
    private TLService tlService;

    @Before
    public void initialize() {
        Load load = new Load();
        tlLoader.loadTL(EU_COUNTRY_CODE, getLOTLUrl(), TLType.LOTL, TLStatus.PROD, load);
    }

    @Test
    public void getReport() {
        List<TrustedListsReport> tls = tlService.getAllProdTlReports();
        Assert.assertNotNull(tls);
        Assert.assertEquals(1, tls.size());
    }

    @Test
    public void getLOTLId() {
        int id = tlService.getLOTLId();
        Assert.assertEquals(1, id);
    }

    @Test
    public void getXmlFile() {
        File f = tlService.getXmlFile(1);
        Assert.assertNotNull(f.getPath());
    }

}
