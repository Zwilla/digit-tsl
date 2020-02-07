package eu.europa.ec.joinup.tsl.business.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.LogFileDTO;

public class LogManagerServiceTest extends AbstractSpringTest {

    @Autowired
    private LogManagerService logService;

    @Test
    public void test() {

    }

    @Test
    public void getFiles() {
        List<LogFileDTO> logs = logService.getAllLogs();
        Assert.assertTrue(CollectionUtils.isNotEmpty(logs));
        Assert.assertEquals(3, logs.size());
    }

}
