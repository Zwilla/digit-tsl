package eu.europa.ec.joinup.tsl.business.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.Load;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceDto;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;

@Transactional
public class TLEditServiceServiceTest extends AbstractSpringTest {

    // private static final String LU_COUNTRY_CODE = "DE";
    private static final String LU_COUNTRY_CODE = "LU";
    private static final String BE_COUNTRY_CODE = "BE";

    @Autowired
    private TLLoader tlLoader;

    @Autowired
    private TLService tlService;

    @Autowired
    private TlEditServiceService tlEditServiceService;

    @Before
    public void initialize() {
        Load load = new Load();
        // tlLoader.loadTL(LU_COUNTRY_CODE, "https://www.nrca-ds.de/st/TSL-XML.xml", "pdfUrl", "sha2Url", TLType.TL, TLStatus.DRAFT, load);
        tlLoader.loadTL(LU_COUNTRY_CODE, "https://portail-qualite.public.lu/content/dam/qualite/fr/publications/confiance-numerique/liste-confiance-nationale/tsl-xml/tsl.xml", TLType.TL, TLStatus.DRAFT, load);
        tlLoader.loadTL(BE_COUNTRY_CODE, "http://tsl.belgium.be/tsl-be.xml", TLType.TL, TLStatus.PROD, load);

        TL detl = tlService.getTL(1);
        assertTrue(detl.getSchemeInformation().getTerritory().equalsIgnoreCase(LU_COUNTRY_CODE));

        TL betl = tlService.getTL(2);
        assertTrue(betl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));
    }

    @Test
    public void addSvc() {

        TL detl = tlService.getTL(1);
        assertTrue(detl.getSchemeInformation().getTerritory().equalsIgnoreCase(LU_COUNTRY_CODE));

        int nbreSvc = detl.getServiceProviders().get(0).getTSPServices().size();
        TLServiceDto svc = detl.getServiceProviders().get(0).getTSPServices().get(0);
        svc.setId("");
        List<Integer> intList = new ArrayList<>();
        intList.add(0);
        TLServiceDto svcUpdated = tlEditServiceService.edit(detl.getTlId(), svc, intList);
        // CHECK RETURN
        assertTrue(svcUpdated.getId().equalsIgnoreCase(""));

        TL tlUpdated = tlService.getTL(1);
        assertEquals((tlUpdated.getServiceProviders().get(0).getTSPServices().size() - 1), nbreSvc);
    }

    @Test
    public void editSvc() {
        TL detl = tlService.getTL(1);
        assertTrue(detl.getSchemeInformation().getTerritory().equalsIgnoreCase(LU_COUNTRY_CODE));

        TL betl = tlService.getTL(2);
        assertTrue(betl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        int nbreSvc = detl.getServiceProviders().get(0).getTSPServices().size();

        TLServiceDto svcDe = detl.getServiceProviders().get(0).getTSPServices().get(0);
        TLServiceDto svcBe = betl.getServiceProviders().get(0).getTSPServices().get(0);

        svcBe.setId(svcDe.getId());

        List<Integer> intList = new ArrayList<>();
        intList.add(0);
        TLServiceDto svcUpdated = tlEditServiceService.edit(detl.getTlId(), svcBe, intList);

        assertTrue(svcUpdated.getId().equalsIgnoreCase(svcDe.getId()));

        TL tlUpdated = tlService.getTL(1);
        assertEquals(tlUpdated.getServiceProviders().get(0).getTSPServices().size(), nbreSvc);
        assertTrue(tlUpdated.getServiceProviders().get(0).getTSPServices().get(0).getCurrentStatus().equalsIgnoreCase(svcBe.getCurrentStatus()));
    }

    @Test
    public void deleteSvc() {

        TL detl = tlService.getTL(1);
        assertTrue(detl.getSchemeInformation().getTerritory().equalsIgnoreCase(LU_COUNTRY_CODE));

        int nbreSvc = detl.getServiceProviders().get(0).getTSPServices().size();

        int nbre = tlEditServiceService.delete(detl.getTlId(), detl.getServiceProviders().get(0).getTSPServices().get(0));
        // CHECK RETURN
        assertEquals(1, nbre);

        TL tlUpdated = tlService.getTL(1);
        assertEquals((tlUpdated.getServiceProviders().get(0).getTSPServices().size() + 1), nbreSvc);

    }

}
