package eu.europa.ec.joinup.tsl.business.service;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.CheckDTO;
import eu.europa.ec.joinup.tsl.business.dto.TLDifference;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLDigitalIdentification;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceDto;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;
import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;

public class RulesRunnerServiceTest extends AbstractSpringTest {

    @Autowired
    private TLRepository tlRepository;

    @Autowired
    private TrustedListJaxbService jaxbService;

    @Autowired
    private TLBuilder tlBuilder;

    @Autowired
    private CheckService checkService;

    @SuppressWarnings("unused")
    @Ignore
    // TODO : FIX previous TL not persisted
    public void analyzeTLWithPrevious() throws Exception {

        int previousTLId = createTLinDB();
        TL previous = tlBuilder.buildTLV4(previousTLId, jaxbService.unmarshallTSL(new File("src/test/resources/tsl/BE-TEST/2016-10-13_12-55-38.xml")));

        int currentTLId = createTLinDB();
        TL current = tlBuilder.buildTLV4(currentTLId, jaxbService.unmarshallTSL(new File("src/test/resources/tsl/BE-TEST/2016-10-13_12-55-39-bis.xml")));
        // rulesRunnerService.runAllRules(current, previous);

        List<CheckDTO> checkResults = checkService.getTLChecks(currentTLId);
        assertTrue(CollectionUtils.isNotEmpty(checkResults));

    }

    @Test
    public void compareSK() throws Exception {
        int previousTLId = createTLinDB();
        TL previous = tlBuilder.buildTLV4(previousTLId, jaxbService.unmarshallTSL(new File("src/test/resources/tsl/SK/2016-10-13_13-05-40.xml")));
        List<TLDifference> diffs = new ArrayList<>();
        for (TLServiceProvider tsp : previous.getServiceProviders()) {
            tsp.asPublishedDiff(tsp, tsp.getId());
            for (TLServiceDto service : tsp.getTSPServices()) {
                for (TLDigitalIdentification dit : service.getDigitalIdentification()) {
                    diffs.addAll(dit.asPublishedDiff(dit));
                }
            }
        }
    }

    private int createTLinDB() {
        DBTrustedLists trustedList = new DBTrustedLists();
        trustedList.setType(TLType.TL);
        trustedList.setXmlFile(new DBFiles());
        trustedList.setStatus(TLStatus.PROD);
        tlRepository.save(trustedList);
        return trustedList.getId();
    }

}
