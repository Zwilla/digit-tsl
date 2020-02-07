package eu.europa.ec.joinup.tsl.business.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.IterableUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.repository.DraftStoreRepository;
import eu.europa.ec.joinup.tsl.model.DBDraftStore;

public class DraftStoreServiceTest extends AbstractSpringTest {

    @Autowired
    private DraftStoreService dsService;

    @Autowired
    private DraftStoreRepository dsRepository;

    @Before
    public void init() {
        dsRepository.deleteAll();
        Assert.assertTrue(IterableUtils.isEmpty(dsRepository.findAll()));
    }

    @Test
    public void getNewDraftStoreId() {
        String ds = dsService.getNewDraftStore();
        Assert.assertNotNull(ds);
        Iterable<DBDraftStore> dbDSs = dsRepository.findAll();
        Assert.assertNotNull(dbDSs);
        DBDraftStore dbDS = dbDSs.iterator().next();
        Assert.assertNotNull(dbDS);
        Assert.assertEquals(1, IterableUtils.size(dsRepository.findAll()));
        Assert.assertEquals(ds, dbDS.getDraftStoreId());
    }

    @Test
    public void checkDraftStoreId() throws InterruptedException {
        String dsID = dsService.getNewDraftStore();
        DBDraftStore dbDS = dsService.findOne(dsID);
        Thread.sleep(1000);
        Assert.assertTrue(dsService.checkDraftStoreId(dsID));
        DBDraftStore updateddbDS = dsService.findOne(dsID);
        Assert.assertTrue(dbDS.getLastVerification().before(updateddbDS.getLastVerification()));
    }

    @Test
    public void getDraftStoreNotVerifiedSince() throws InterruptedException {
        Date date = new Date();
        Thread.sleep(1000);
        for (int i = 0; i < 10; i++) {
            dsService.getNewDraftStore();
        }
        Iterable<DBDraftStore> dbDSs = dsRepository.findAll();
        Assert.assertEquals(10, IterableUtils.size(dbDSs));
        List<DBDraftStore> notVerified = dsService.getDraftStoreNotVerifiedSince(date);
        Assert.assertEquals(0, notVerified.size());
        Thread.sleep(1000);
        Date date2 = new Date();
        List<DBDraftStore> notVerified2 = dsService.getDraftStoreNotVerifiedSince(date2);
        Assert.assertEquals(10, notVerified2.size());

    }

}
