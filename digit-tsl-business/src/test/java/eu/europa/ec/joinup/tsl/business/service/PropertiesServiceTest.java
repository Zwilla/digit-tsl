package eu.europa.ec.joinup.tsl.business.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.Properties;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PropertiesServiceTest extends AbstractSpringTest {

    @Autowired
    private PropertiesService propertiesService;

    @Test
    public void AgetProperties() {
        List<Properties> list = propertiesService.getProperties();
        Assert.assertNotNull(list);
        Assert.assertEquals(148, list.size());

    }

    @Test
    public void BgetPrefixUris() {
        List<String> prefixUris = propertiesService.getPrefixUris();
        assertTrue(CollectionUtils.isNotEmpty(prefixUris));
        assertTrue(prefixUris.contains("http://"));
        assertFalse(prefixUris.contains("ftp://"));
    }

    @Test
    public void CgetTSLTagValue() {
        assertEquals("https://uri.etsi.org/19612/TSLTag", propertiesService.getTSLTagValue());
    }

    @Test
    public void DgetTSLVersionIdentifier() {
        assertEquals("5", propertiesService.getTSLVersionIdentifier());
    }

    @Test
    public void EgetLOTLStatusDeterminationApproachValue() {
        assertEquals("https://uri.etsi.org/TrstSvc/TrustedList/StatusDetn/EUlistofthelists", propertiesService.getLOTLStatusDeterminationApproachValue());
    }

    @Test
    public void FgetTLStatusDeterminationApproachValue() {
        assertEquals("https://uri.etsi.org/TrstSvc/TrustedList/StatusDetn/EUappropriate", propertiesService.getTLStatusDeterminationApproachValue());
    }

    @Test
    public void GgetLOTLSchemeCommunityRulesValue() {
        assertEquals("https://uri.etsi.org/TrstSvc/TrustedList/schemerules/EUlistofthelists", propertiesService.getLOTLSchemeCommunityRulesValue());
    }

    @Test
    public void HgetServiceTypeIdentifiers() {
        List<String> serviceTypeIdentifiers = propertiesService.getServiceTypeIdentifiers();
        assertTrue(CollectionUtils.isNotEmpty(serviceTypeIdentifiers));
        assertTrue(serviceTypeIdentifiers.contains("https://uri.etsi.org/TrstSvc/Svctype/IdV"));
    }

    @Test
    public void IgetServiceStatus() {
        List<String> serviceStatus = propertiesService.getServiceStatus();
        assertTrue(CollectionUtils.isNotEmpty(serviceStatus));
        assertTrue(serviceStatus.contains("https://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/setbynationallaw"));
    }

    @Test
    public void JgetQualifiers() {
        List<String> qualifiers = propertiesService.getQualifiers();
        assertTrue(CollectionUtils.isNotEmpty(qualifiers));
        assertTrue(qualifiers.contains("https://uri.etsi.org/TrstSvc/TrustedList/SvcInfoExt/QCWithSSCD"));
    }

    @Test
    public void KgetTLSchemeCommunityRulesValues() {
        List<String> schemeCommunityRulesValues = propertiesService.getTLSchemeCommunityRulesValues();
        assertTrue(CollectionUtils.isNotEmpty(schemeCommunityRulesValues));
        assertTrue(schemeCommunityRulesValues.contains("https://uri.etsi.org/TrstSvc/TrustedList/schemerules/AT"));
        assertTrue(schemeCommunityRulesValues.contains("https://uri.etsi.org/TrstSvc/TrustedList/schemerules/EUcommon"));
        assertFalse(schemeCommunityRulesValues.contains("https://uri.etsi.org/TrstSvc/TrustedList/schemerules/EUlistofthelists"));
    }

    @Test
    public void LgetHistoryInformationPeriod() {
        Integer historyInformationPeriod = propertiesService.getHistoryInformationPeriod();
        assertEquals(65535, historyInformationPeriod.intValue());
    }

    @Test
    public void MgetServiceTypeNationalRootCAQC() {
        assertTrue(StringUtils.isNotEmpty(propertiesService.getServiceTypeNationalRootCAQC()));
    }

    @Test
    public void NgetStatusForNationalRootCAQC() {
        List<String> statusForNationalRootCAQC = propertiesService.getStatusForNationalRootCAQC();
        assertEquals(2, CollectionUtils.size(statusForNationalRootCAQC));
        assertTrue(statusForNationalRootCAQC.contains("https://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/setbynationallaw"));
        assertTrue(statusForNationalRootCAQC.contains("https://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/deprecatedbynationallaw"));
    }

    @Test
    public void OgetEnglishSchemeNameText() {
        String englishSchemeNameText = propertiesService.getEnglishSchemeNameText();
        assertEquals(
                "Trusted list including information related to the qualified trust service providers which are supervised by the issuing Member State, together with information related to the qualified trust services provided by them, in accordance with the relevant provisions laid down in Regulation (EU) No 910/2014 of the European Parliament and of the Council of 23 July 2014 on electronic identification and trust services for electronic transactions in the internal market and repealing Directive 1999/93/EC.",
                englishSchemeNameText);
    }

    @Test
    public void PgetServiceTypeForAsiForeSignatureSealChecks() {
        List<String> l = propertiesService.getServiceTypeForAsiForeSignatureSealChecks();
        Assert.assertNotNull(l);
        Assert.assertEquals(2, l.size());
    }

    @Test
    public void QgetServiceTypeForExpiredCertRevocationInfoChecks() {
        List<String> l = propertiesService.getServiceTypeForExpiredCertRevocationInfoChecks();
        Assert.assertNotNull(l);
        Assert.assertEquals(7, l.size());
    }

    @Test
    public void RgetServiceTypeForAsiForeChecks() {
        List<String> l = propertiesService.getServiceTypeForAsiForeChecks();
        Assert.assertNotNull(l);
        Assert.assertEquals(6, l.size());
    }

    @Test
    public void Sadd() {
        List<Properties> list = propertiesService.getProperties();
        Assert.assertNotNull(list);

        Properties prop = new Properties();
        prop.setCodeList("ADRTYPE");
        prop.setDescription("description");
        prop.setLabel("label");
        propertiesService.add(prop);
        List<Properties> listTmp = propertiesService.getProperties();
        Assert.assertNotNull(listTmp);
        Assert.assertEquals(list.size() + 1, listTmp.size());

    }

    @Test
    public void Tdelete() {
        List<Properties> list = propertiesService.getProperties();
        Assert.assertNotNull(list);
        propertiesService.delete(list.get(0));
        List<Properties> listBis = propertiesService.getProperties();
        Assert.assertNotNull(list);
        Assert.assertEquals(list.size() - 1, listBis.size());

    }

}
