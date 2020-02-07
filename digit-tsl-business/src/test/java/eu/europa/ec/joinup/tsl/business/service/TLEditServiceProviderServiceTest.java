package eu.europa.ec.joinup.tsl.business.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.Load;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;

@Transactional
public class TLEditServiceProviderServiceTest extends AbstractSpringTest {

    private static final String LU_COUNTRY_CODE = "LU";
    private static final String BE_COUNTRY_CODE = "BE";

    @Autowired
    private TLLoader tlLoader;

    @Autowired
    private TLService tlService;

    @Autowired
    private TlEditServiceProviderService tlEditServiceProviderService;

    @Before
    public void initialize() {
        Load load = new Load();
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

        int nbreSvc = detl.getServiceProviders().size();
        TLServiceProvider svc = detl.getServiceProviders().get(0);
        svc.setId("");
        TLServiceProvider svcUpdated = tlEditServiceProviderService.edit(detl.getTlId(), svc);
        // CHECK RETURN
        assertTrue(svcUpdated.getId().equalsIgnoreCase(""));

        TL tlUpdated = tlService.getTL(1);
        assertEquals((tlUpdated.getServiceProviders().size() - 1), nbreSvc);
    }

    @Test
    public void editSvc() {
        TL detl = tlService.getTL(1);
        assertTrue(detl.getSchemeInformation().getTerritory().equalsIgnoreCase(LU_COUNTRY_CODE));

        TL betl = tlService.getTL(2);
        assertTrue(betl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        int nbreSvc = detl.getServiceProviders().size();

        TLServiceProvider svcDe = detl.getServiceProviders().get(0);
        TLServiceProvider svcBe = betl.getServiceProviders().get(0);

        svcBe.setId(svcDe.getId());

        TLServiceProvider svcUpdated = tlEditServiceProviderService.edit(detl.getTlId(), svcBe);

        assertTrue(svcUpdated.getId().equalsIgnoreCase(svcDe.getId()));

        TL tlUpdated = tlService.getTL(1);
        assertEquals(tlUpdated.getServiceProviders().size(), nbreSvc);
        assertTrue(CollectionUtils.isEqualCollection(tlUpdated.getServiceProviders().get(0).getTSPName(), svcBe.getTSPName()));
    }

    @Test
    public void deleteSvc() {

        TL detl = tlService.getTL(1);
        assertTrue(detl.getSchemeInformation().getTerritory().equalsIgnoreCase(LU_COUNTRY_CODE));

        int nbreSvc = detl.getServiceProviders().size();

        int nbre = tlEditServiceProviderService.delete(detl.getTlId(), detl.getServiceProviders().get(0));
        // CHECK RETURN
        assertEquals(1, nbre);

        TL tlUpdated = tlService.getTL(1);
        assertEquals((tlUpdated.getServiceProviders().size() + 1), nbreSvc);

    }

}
