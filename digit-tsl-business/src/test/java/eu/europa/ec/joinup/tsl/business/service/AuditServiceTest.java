package eu.europa.ec.joinup.tsl.business.service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.NewDTO;
import eu.europa.ec.joinup.tsl.business.dto.audit.Audit;
import eu.europa.ec.joinup.tsl.business.dto.audit.AuditSearchDTO;
import eu.europa.ec.joinup.tsl.business.repository.AuditRepository;
import eu.europa.ec.joinup.tsl.business.repository.FileRepository;
import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.AuditAction;
import eu.europa.ec.joinup.tsl.model.enums.AuditStatus;
import eu.europa.ec.joinup.tsl.model.enums.AuditTarget;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuditServiceTest extends AbstractSpringTest {

    @Autowired
    private AuditService auditService;

    @Autowired
    private AuditRepository auditRepository;

    @Autowired
    private TLRepository tlRepository;

    @Autowired
    private FileRepository fileRepository;

    @Test
    public void AtestAddAudit() {
        auditRepository.deleteAll();
        Audit audit = auditService.addAuditLog(AuditTarget.DRAFTSTORE, AuditAction.CREATE, AuditStatus.SUCCES, "BE", 0, "TLM", "TEST INFOS SUPP");
        Assert.assertNotNull(audit);
        assertSame(audit.getTarget(), AuditTarget.DRAFTSTORE);

        List<Audit> auditList = auditService.getAllAuditOrderByDateDesc();
        Assert.assertNotNull(auditList);
        assertTrue(auditList.size() > 0);
    }

    @Test
    public void BtestAuditBy() {
        auditRepository.deleteAll();
        Audit audit = auditService.addAuditLog(AuditTarget.PROD_TL, AuditAction.CREATE, AuditStatus.SUCCES, "BE", 0, "TLM", "TEST INFOS SUPP");
        Assert.assertNotNull(audit);
        assertSame(audit.getTarget(), AuditTarget.PROD_TL);

        audit = auditService.addAuditLog(AuditTarget.PROD_TL, AuditAction.CREATE, AuditStatus.SUCCES, "LU", 0, "TLM", "TEST INFOS SUPP");
        Assert.assertNotNull(audit);
        assertSame(audit.getTarget(), AuditTarget.PROD_TL);

        Calendar c = new GregorianCalendar();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 1);
        Date d1 = c.getTime();

        AuditSearchDTO dto = new AuditSearchDTO();
        dto.setCountryCode("BE");
        dto.setTarget(AuditTarget.PROD_TL);
        dto.setAction(AuditAction.CREATE);
        dto.setStartDate(d1);

        List<Audit> auditList = auditService.searchAuditByCriteria(dto);
        Assert.assertNotNull(auditList);
        assertEquals(1, auditList.size());

        dto.setCountryCode("BE");
        dto.setTarget(AuditTarget.DRAFT_TL);
        dto.setAction(AuditAction.CREATE);
        dto.setStartDate(d1);

        List<Audit> auditList2 = auditService.searchAuditByCriteria(dto);
        Assert.assertNotNull(auditList2);
        assertEquals(0, auditList2.size());

        dto = new AuditSearchDTO();
        dto.setAction(AuditAction.CREATE);
        List<Audit> auditList3 = auditService.searchAuditByCriteria(dto);
        Assert.assertNotNull(auditList3);
        assertEquals(2, auditList3.size());

    }

    @Test
    public void CisFloodLimitReach() {
        auditRepository.deleteAll();
        Assert.assertFalse(auditService.isFloodLimitReach("BE"));
        auditService.addAuditLog(AuditTarget.PROD_TL, AuditAction.CREATE, AuditStatus.SUCCES, "BE", 1, "test-man", "infos");
        auditService.addAuditLog(AuditTarget.PROD_TL, AuditAction.CREATE, AuditStatus.SUCCES, "BE", 1, "test-man", "infos2");
        auditService.addAuditLog(AuditTarget.PROD_TL, AuditAction.CREATE, AuditStatus.SUCCES, "BE", 1, "test-man", "infos3");
        Assert.assertFalse(auditService.isFloodLimitReach("BE"));
        auditService.addAuditLog(AuditTarget.PROD_TL, AuditAction.CREATE, AuditStatus.SUCCES, "BE", 1, "test-man", "infos4");
        Assert.assertTrue(auditService.isFloodLimitReach("BE"));
    }

    @Test
    @Transactional
    public void DgetNews() {
        List<NewDTO> news = auditService.getLatestNews();
        Assert.assertTrue(CollectionUtils.isEmpty(news));

        DBTrustedLists dbTl = new DBTrustedLists();
        DBFiles xmlFile = new DBFiles();
        xmlFile.setId(1);
        xmlFile = fileRepository.save(xmlFile);
        dbTl.setXmlFile(xmlFile);
        dbTl.setSequenceNumber(1000);
        tlRepository.save(dbTl);

        auditRepository.deleteAll();

        news = auditService.getLatestNews();
        Assert.assertNotNull(news);
        Assert.assertEquals(0, news.size());
        auditService.addAuditLog(AuditTarget.PROD_TL, AuditAction.CREATE, AuditStatus.SUCCES, "BE", 1, "test-man", "infos");
        news = auditService.getLatestNews();
        Assert.assertEquals(1, news.size());
        auditService.addAuditLog(AuditTarget.PROD_TL, AuditAction.CREATE, AuditStatus.SUCCES, "EU", 1, "test-man", "infos2");
        auditService.addAuditLog(AuditTarget.PROD_TL, AuditAction.CREATE, AuditStatus.SUCCES, "LU", 1, "test-man", "infos3");
        auditService.addAuditLog(AuditTarget.PROD_TL, AuditAction.CREATE, AuditStatus.SUCCES, "IT", 1, "test-man", "infos4");
        news = auditService.getLatestNews();
        Assert.assertEquals(3, news.size());

    }

}
