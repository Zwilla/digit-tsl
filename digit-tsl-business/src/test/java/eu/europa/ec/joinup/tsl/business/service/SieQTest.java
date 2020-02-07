package eu.europa.ec.joinup.tsl.business.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.TrustedListsReport;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLCriteria;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLKeyUsage;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPolicies;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLQualificationExtension;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceDto;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceExtension;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;

public class SieQTest extends AbstractSpringTest {

    private static final String bL = "\n";
    private static final String tab = "     ";
    private StringBuilder atoKu;
    private StringBuilder allKu;
    private StringBuilder noneKu;
    private StringBuilder atoPs;
    private StringBuilder allPs;
    private StringBuilder nonePs;
    private StringBuilder atoOther;
    private StringBuilder allOther;
    private StringBuilder noneOther;
    private StringBuilder atoCriteria;
    private StringBuilder allCriteria;
    private StringBuilder noneCriteria;

    private int indexAtoKu;
    private int indexAllKu;
    private int indexNoneKu;
    private int indexAtoPs;
    private int indexAllPs;
    private int indexNonePs;
    private int indexAtoOther;
    private int indexAllOther;
    private int indexNoneOther;
    private int indexAllCriteria;
    private int indexNoneCriteria;
    private int indexAtoCriteria;

    @Autowired
    private LoadingJobService loadingJobService;

    @Autowired
    private TLService tlService;

    @Before
    public void init() {
        loadingJobService.start();
        atoKu = new StringBuilder();
        allKu = new StringBuilder();
        noneKu = new StringBuilder();
        atoPs = new StringBuilder();
        allPs = new StringBuilder();
        nonePs = new StringBuilder();
        atoOther = new StringBuilder();
        allOther = new StringBuilder();
        noneOther = new StringBuilder();
        atoCriteria = new StringBuilder();
        allCriteria = new StringBuilder();
        noneCriteria = new StringBuilder();
        indexAtoKu = 0;
        indexAllKu = 0;
        indexNoneKu = 0;
        indexAtoPs = 0;
        indexAllPs = 0;
        indexNonePs = 0;
        indexAtoOther = 0;
        indexAllOther = 0;
        indexNoneOther = 0;
        indexAtoCriteria = 0;
        indexAllCriteria = 0;
        indexNoneCriteria = 0;
    }

    @Test
    public void test() {

    }

    // @Test
    @Ignore
    public void countSieQExtensions() throws IOException {
        List<TrustedListsReport> tlReports = tlService.getAllProdTlReports();
        for (TrustedListsReport report : tlReports) {
            TL tl = tlService.getTL(report.getId());
            processTL(tl);
        }

        atoKu.append("Results: ").append(indexAtoKu).append(bL);
        allKu.append("Results: ").append(indexAllKu).append(bL);
        noneKu.append("Results: ").append(indexNoneKu).append(bL);
        atoPs.append("Results: ").append(indexAtoPs).append(bL);
        allPs.append("Results: ").append(indexAllPs).append(bL);
        nonePs.append("Results: ").append(indexNonePs).append(bL);
        atoOther.append("Results: ").append(indexAtoOther).append(bL);
        allOther.append("Results: ").append(indexAllOther).append(bL);
        noneOther.append("Results: ").append(indexNoneOther).append(bL);
        atoCriteria.append("Results: ").append(indexAtoCriteria).append(bL);
        allCriteria.append("Results: ").append(indexAllCriteria).append(bL);
        noneCriteria.append("Results: ").append(indexNoneCriteria).append(bL);

        FileUtils.write(new File("src/test/resources/sieQ-AtLeastOne-KeyUsage.txt"), atoKu.toString(), StandardCharsets.UTF_8);
        FileUtils.write(new File("src/test/resources/sieQ-All-KeyUsage.txt"), allKu.toString(), StandardCharsets.UTF_8);
        FileUtils.write(new File("src/test/resources/sieQ-None-KeyUsage.txt"), noneKu.toString(), StandardCharsets.UTF_8);
        FileUtils.write(new File("src/test/resources/sieQ-AtLeastOne-PolicySet.txt"), atoPs.toString(), StandardCharsets.UTF_8);
        FileUtils.write(new File("src/test/resources/sieQ-All-PolicySet.txt"), allPs.toString(), StandardCharsets.UTF_8);
        FileUtils.write(new File("src/test/resources/sieQ-None-PolicySet.txt"), nonePs.toString(), StandardCharsets.UTF_8);
        FileUtils.write(new File("src/test/resources/sieQ-AtLeastOne-Others.txt"), atoOther.toString(), StandardCharsets.UTF_8);
        FileUtils.write(new File("src/test/resources/sieQ-All-Others.txt"), allOther.toString(), StandardCharsets.UTF_8);
        FileUtils.write(new File("src/test/resources/sieQ-None-Others.txt"), noneOther.toString(), StandardCharsets.UTF_8);
        FileUtils.write(new File("src/test/resources/sieQ-AtLeastOne-Criteria.txt"), atoCriteria.toString(), StandardCharsets.UTF_8);
        FileUtils.write(new File("src/test/resources/sieQ-All-Criteria.txt"), allCriteria.toString(), StandardCharsets.UTF_8);
        FileUtils.write(new File("src/test/resources/sieQ-None-Criteria.txt"), noneCriteria.toString(), StandardCharsets.UTF_8);
    }

    private void processTL(TL tl) {
        if (tl == null) {
            System.out.println("NULL TL");
        } else {
            final String TLInfo = "TL : " + tl.getSchemeInformation().getTerritory() + " Sn " + tl.getSchemeInformation().getSequenceNumber() + bL;
            if (!CollectionUtils.isEmpty(tl.getServiceProviders())) {
                for (TLServiceProvider tsp : tl.getServiceProviders()) {
                    if (!CollectionUtils.isEmpty(tsp.getTSPServices())) {
                        for (TLServiceDto service : tsp.getTSPServices()) {
                            boolean found = false;
                            String serviceInfo = "";
                            if (!CollectionUtils.isEmpty(service.getExtension())) {
                                for (TLServiceExtension extension : service.getExtension()) {
                                    if (extension.getQualificationsExtension() != null && !CollectionUtils.isEmpty(extension.getQualificationsExtension())) {
                                        if (!found) {
                                            serviceInfo = TLInfo + tab + "Service: " + tsp.getName() + " - " + service.getServiceName().get(0).getValue() + bL;
                                        }
                                        found = true;
                                        for (TLQualificationExtension qualificationExtension : extension.getQualificationsExtension()) {
                                            processQualificationExtension(serviceInfo, qualificationExtension);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void processQualificationExtension(String serviceInfo, TLQualificationExtension qE) {
        StringBuilder sb = new StringBuilder();
        int indexKeyUsage = 0;
        int indexPolicyList = 0;
        boolean otherItems = false;
        boolean criteriaList = false;
        sb.append(serviceInfo).append(tab).append(tab).append("Qualifiers: ").append(getQualifiers(qE.getQualifTypeList())).append(bL);
        final TLCriteria criteria = qE.getCriteria();
        sb.append(tab + tab + "Assert: ").append(criteria.getAsserts()).append(bL);
        // Key Usage
        if (!CollectionUtils.isEmpty(criteria.getKeyUsage())) {
            int i = 1;
            for (TLKeyUsage keyUsage : criteria.getKeyUsage()) {
                indexKeyUsage = indexKeyUsage + keyUsage.getKeyUsageBit().size();
                sb.append(tab + tab + "KeyUsage n° ").append(i).append(" - ").append(keyUsage.getKeyUsageBit().size()).append(" bit").append(bL);
                i++;
            }
            sb.append(bL);
        }
        // Policy List
        if (!CollectionUtils.isEmpty(criteria.getPolicyList())) {
            int i = 1;
            for (TLPolicies policy : criteria.getPolicyList()) {
                indexPolicyList = indexPolicyList + policy.getPolicyBit().size();
                sb.append(tab + tab + "Policy set ").append(i).append(" - ").append(policy.getPolicyBit().size()).append(" OID").append(bL);
                i++;
            }
            sb.append(bL);
        }
        // Other criteria
        if (criteria.getOtherList() != null) {
            if (criteria.getOtherList().getCertDnaList() != null) {
                sb.append(tab + tab + "Cert DNA ").append(criteria.getOtherList().getCertDnaList().size());
                otherItems = true;
            }
            if (criteria.getOtherList().getExtendedKeyUsageList() != null) {
                sb.append(tab + tab + "Ext KeyUsage ").append(criteria.getOtherList().getExtendedKeyUsageList().size());
                otherItems = true;
            }
        }
        // Criteria List
        if (!CollectionUtils.isEmpty(criteria.getCriteriaList())) {
            sb.append(tab + tab + "Criteria list ").append(criteria.getCriteriaList().size());
            for (TLCriteria tlCriteria : criteria.getCriteriaList()) {
                processCriteria(sb, tlCriteria);
            }
            criteriaList = true;
        }
        sb.append(bL + bL);

        if (indexKeyUsage == 1 && indexPolicyList == 0 && !otherItems && !criteriaList) {
            switch (criteria.getAsserts()) {
            case "atLeastOne":
                atoKu.append(sb.toString());
                indexAtoKu++;
                break;
            case "all":
                allKu.append(sb.toString());
                indexAllKu++;
                break;
            case "none":
                noneKu.append(sb.toString());
                indexNoneKu++;
                break;
            }
        } else if (indexKeyUsage == 0 && indexPolicyList == 1 && !otherItems && !criteriaList) {
            switch (criteria.getAsserts()) {
            case "atLeastOne":
                atoPs.append(sb.toString());
                indexAtoPs++;
                break;
            case "all":
                allPs.append(sb.toString());
                indexAllPs++;
                break;
            case "none":
                nonePs.append(sb.toString());
                indexNonePs++;
                break;
            }
        } else if (criteriaList) {
            switch (criteria.getAsserts()) {
            case "atLeastOne":
                atoCriteria.append(sb.toString());
                indexAtoCriteria++;
                break;
            case "all":
                allCriteria.append(sb.toString());
                indexAllCriteria++;
                break;
            case "none":
                noneCriteria.append(sb.toString());
                indexNoneCriteria++;
                break;
            }
        } else {
            switch (criteria.getAsserts()) {
            case "atLeastOne":
                atoOther.append(sb.toString());
                indexAtoOther++;
                break;
            case "all":
                allOther.append(sb.toString());
                indexAllOther++;
                break;
            case "none":
                noneOther.append(sb.toString());
                indexNoneOther++;
                break;
            }
        }
    }

    private void processCriteria(StringBuilder sb, TLCriteria criteria) {
        sb.append(tab + tab + "Assert: ").append(criteria.getAsserts()).append(bL);
        // Key Usage
        if (!CollectionUtils.isEmpty(criteria.getKeyUsage())) {
            int i = 1;
            for (TLKeyUsage keyUsage : criteria.getKeyUsage()) {
                sb.append(tab + tab + "KeyUsage n° ").append(i).append(" - ").append(keyUsage.getKeyUsageBit().size()).append(" bit").append(bL);
                i++;
            }
            sb.append(bL);
        }
        // Policy List
        if (!CollectionUtils.isEmpty(criteria.getPolicyList())) {
            int i = 1;
            for (TLPolicies policy : criteria.getPolicyList()) {
                sb.append(tab + tab + "Policy set ").append(i).append(" - ").append(policy.getPolicyBit().size()).append(" OID").append(bL);
                i++;
            }
            sb.append(bL);
        }
        // Other criteria
        if (criteria.getOtherList() != null) {
            if (criteria.getOtherList().getCertDnaList() != null) {
                sb.append(tab + tab + "Cert DNA ").append(criteria.getOtherList().getCertDnaList().size());
            }
            if (criteria.getOtherList().getExtendedKeyUsageList() != null) {
                sb.append(tab + tab + "Ext KeyUsage ").append(criteria.getOtherList().getExtendedKeyUsageList().size());
            }
        }
        // Criteria List
        if (!CollectionUtils.isEmpty(criteria.getCriteriaList())) {
            sb.append(tab + tab + "Criteria list ").append(criteria.getCriteriaList().size());
            for (TLCriteria tlCriteria : criteria.getCriteriaList()) {
                processCriteria(sb, tlCriteria);
            }
        }
        sb.append(bL + bL);
    }

    private String getQualifiers(List<String> qualifTypeList) {
        StringBuilder qualifiers = new StringBuilder();
        for (String qualif : qualifTypeList) {
            qualifiers.append(qualif.replace("https://uri.etsi.org/TrstSvc/TrustedList/SvcInfoExt/", "")).append("; ");
        }
        return qualifiers.toString();
    }
}
