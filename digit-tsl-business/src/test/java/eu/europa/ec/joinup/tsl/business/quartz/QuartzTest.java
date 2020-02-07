package eu.europa.ec.joinup.tsl.business.quartz;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.repository.AvailabilityRepository;
import eu.europa.ec.joinup.tsl.business.repository.FileRepository;
import eu.europa.ec.joinup.tsl.business.repository.ResultRepository;
import eu.europa.ec.joinup.tsl.business.repository.SignatureInformationRepository;
import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.business.service.LoadingJobService;

public class QuartzTest extends AbstractSpringTest {

    @Autowired
    private LoadingJobService loadingJobService;

    // @Autowired
    // private SignatureValidationJobService signatureValidationJobService;

    @Autowired
    private TLRepository tlRepo;

    @Autowired
    private FileRepository fileRepo;

    @Autowired
    private AvailabilityRepository availabilityRepo;

    @Autowired
    private SignatureInformationRepository signInfoRepo;

    @Autowired
    private ResultRepository resultRepo;

    // @Autowired
    // private TLService tlService;

    @Test
    public void testQuartzMethod() {

        assertEquals(0, tlRepo.count());
        assertEquals(0, fileRepo.count());
        assertEquals(0, availabilityRepo.count());
        assertEquals(0, signInfoRepo.count());
        assertEquals(0, resultRepo.count());

        try {
            loadingJobService.start();
            assertTrue(tlRepo.count() > 0);
            assertTrue(fileRepo.count() > 0);
            assertTrue(availabilityRepo.count() > 0);
            assertTrue(signInfoRepo.count() > 0);
        } catch (Exception e) {
        }
        // TODO(5.4.RC1) : Implement integration test
        // assertTrue(resultRepo.count() > 0);
        // long signCountLoad = signInfoRepo.count();

        // try {
        // List<TrustedListsReport> allProdTlReports = tlService.getAllProdTlReports();
        // assertTrue(allProdTlReports.size() > 10);
        // for (TrustedListsReport tl : allProdTlReports) {
        // assertEquals(TLStatus.PROD, tl.getTlStatus());
        // assertNotNull("Territory is null", tl.getTerritoryCode());
        // assertNotNull("Availability is null for " + tl.getTerritoryCode(), tl.getAvailability());
        // }
        //
        // List<DBTrustedLists> tls = (List<DBTrustedLists>) tlRepo.findAll();
        // assertTrue(tls.size() > 0);
        // for (DBTrustedLists tl : tls) {
        // assertEquals(TLStatus.PROD, tl.getStatus());
        // assertNotNull("Territory is null", tl.getTerritory());
        // assertNotNull("Type is null for " + tl.getTerritory(), tl.getType());
        //
        // DBFiles xmlFile = tl.getXmlFile();
        // assertNotNull("XML is null", xmlFile);
        // assertNotNull("No availability found for XML", availabilityRepo.findTopByFileOrderByCheckDateDesc(xmlFile));
        // }
        //
        // signatureValidationJobService.start();
        // assertTrue(signInfoRepo.count() == signCountLoad);
        //
        // allProdTlReports = tlService.getAllProdTlReports();
        // for (TrustedListsReport tl : allProdTlReports) {
        // if (tl.getAvailability().equals(AvailabilityStatus.AVAILABLE)) {
        // assertNotNull("Sig Status is null for " + tl.getTerritoryCode(), tl.getSigStatus());
        // }
        // }

        // rulesValidationJobService.start();
        // assertTrue(resultRepo.count() > 0);

    }

}
