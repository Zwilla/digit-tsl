package eu.europa.ec.joinup.tsl.business.quartz;

import org.junit.Assert;
import org.junit.Test;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;

public class CountObject extends AbstractSpringTest {

    // @Autowired
    // private TLService tlService;
    //
    // @Autowired
    // private LoadingJobService loadingJobService;
    //
    // @Autowired
    // private RulesValidationJobService rulesValidationJobService;
    //
    // @Autowired
    // private SignatureValidationJobService signatureValidationJobService;
    //
    // @Autowired
    // private ErrorAnalysisService errorAnalysisService;

    // Empty test for jenkins
    @Test
    public void emptyTest() {
        Assert.assertTrue(true);
    }

    // @Test
    // public void testQuartzMethod() throws Exception {
    // loadingJobService.start();
    // signatureValidationJobService.start();
    // rulesValidationJobService.start();
    //
    // byte[] b = errorAnalysisService.getErrorAnalysis();
    //
    // FileUtils.writeByteArrayToFile(new File("src/test/resources/check_analysis.xls"), b);
    //
    // }

    // @Test
    // public void tlBrowserTypesAnalysis() throws Exception {
    // String jump = " \n\n";
    // String space = " ";
    // loadingJobService.start();
    // List<TrustedListsReport> allProdTlReports = tlService.getAllProdTlReports();
    // for (TrustedListsReport tlr : allProdTlReports) {
    // TL tl = tlService.getTL(tlr.getId());
    //
    // if ((tl != null) && (tl.getDbCountryName() != null)) {
    // StringBuilder builder = new StringBuilder();
    // builder.append(tl.getDbCountryName() + jump);
    // if (tl.getServiceProviders() != null) {
    // for (TLServiceProvider sp : tl.getServiceProviders()) {
    // if (sp.getTSPServices() != null) {
    // builder.append(space + sp.getName() + jump);
    // for (TLServiceDto service : sp.getTSPServices()) {
    // if (service != null) {
    // builder.append(space + space + service.getServiceName().get(0).getValue() + " - " + service.getTypeIdentifier() + jump);
    // if (service.getExtension() != null) {
    // for (TLServiceExtension ext : service.getExtension()) {
    // if (ext.getAdditionnalServiceInfo() != null) {
    // builder.append(space + space + space + ext.getAdditionnalServiceInfo().getValue() + jump);
    // }
    // }
    // }
    // }
    // }
    // }
    // }
    // }
    // FileUtils.writeStringToFile(new File("src/test/resources/tlbStatusAnalysis/" + tl.getDbCountryName() + ".txt"), builder.toString());
    // }
    // }
    //
    // }

    // @Test
    // public void calculateTrustServiceType() throws Exception {
    // String jump = " \n\n";
    // String space = " ";
    // loadingJobService.start();
    // List<TrustedListsReport> allProdTlReports = tlService.getAllProdTlReports();
    // for (TrustedListsReport tlr : allProdTlReports) {
    // TL tl = tlService.getTL(tlr.getId());
    //
    // if ((tl != null) && (tl.getDbCountryName() != null)) {
    // StringBuilder builder = new StringBuilder();
    // builder.append(tl.getDbCountryName() + jump);
    // if (tl.getServiceProviders() != null) {
    // for (TLServiceProvider sp : tl.getServiceProviders()) {
    // if (sp.getTSPServices() != null) {
    // builder.append(space + sp.getName() + jump);
    // for (TLServiceDto service : sp.getTSPServices()) {
    // if (service != null) {
    // builder.append(space + space + service.getServiceName().get(0).getValue() + " - " + service.getTypeIdentifier() + jump);
    // Set<String> serviceTypes = TLQServiceTypeUtils.getServiceTypes(service);
    // if (CollectionUtils.isEmpty(serviceTypes)) {
    // builder.append(space + space + space + " ! EMPTY LIST ! ");
    // } else {
    // builder.append(space + space + space);
    // for (String type : serviceTypes) {
    // builder.append(type + ";");
    // }
    // }
    // builder.append(jump);
    // }
    // }
    // }
    // }
    // }
    // FileUtils.writeStringToFile(new File("src/test/resources/tlbStatusAnalysis/statusResult/" + tl.getDbCountryName() + ".txt"),
    // builder.toString());
    // }
    // }
    //
    // }

}
