package eu.europa.ec.joinup.tsl.business.util;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.service.TLService;

public class ConcatenateResultTest extends AbstractSpringTest {

    @Autowired
    TLService tlService;

    // @Autowired
    // private RulesRunnerService rulesRunnerService;
    //
    // @Autowired
    // private TLRepository tlRepository;
    //
    // @Autowired
    // private JaxbService jaxbService;
    //
    //
    // @Autowired
    // private TLBuilder tlBuilder;

    @Test
    public void jenkinsEmpty() {

    }

    // public void parseBE() throws Exception {
    // parseTLCC("BE");
    // }
    //
    // public void parseIT() throws Exception {
    // parseTLCC("IT");
    // }
    //
    // public void parseES() throws Exception {
    // parseTLCC("ES");
    // }
    //
    // @Test
    // public void parseNL() throws Exception {
    // parseTLCC("NL");
    // }
    //
    // public void parseAT() throws Exception {
    // parseTLCC("AT");
    // }
    //
    // @Test
    // public void test() throws Exception {
    // assertTrue(true);
    // }
    //
    // private int parseTLCC(String cc) throws Exception {
    // String str = FileUtils.readFileToString(new File("src/test/resources/TLCC/result" + cc + ".xml"));
    //
    // TLCCResults tlccObj = TLCCMarshallerUtils.unmarshallTlccXml(str);
    // Assert.assertNotNull(tlccObj);
    // List<TlccDTO> checks = TLCCParserUtils.parseAllTLCC(tlccObj, "1");
    //
    // int lotlId = createTLinDB(TLType.TL);
    // TrustStatusListTypeV5 tsl = jaxbService.unmarshallTSLV5(new File("src/test/resources/TLCC/" + cc +".xml"));
    // TL tl = tlBuilder.buildTLV5(lotlId, tsl);
    // System.out.println("************** runAllRules for TL : " + tl.getServiceProviders().size());
    // rulesRunnerService.runAllRules(tl, null);
    // List<TLChecks> checkResults = tlService.getTLChecksResult(lotlId, true, true);
    //
    //
    //
    // Set<String> tlccSet = new LinkedHashSet<String>();
    // List<String> tlccList = new ArrayList<String>();
    // for (TlccDTO tlccCheck : checks) {
    // tlccCheck.setHrLocation(LocationUtils.idUserReadable(tl, tlccCheck.getLocation()));
    //
    // }
    //
    // System.out.println("***********************************************");
    // System.out.println("* RESULT FOR " + cc + " *");
    // System.out.println("***********************************************");
    // for (TlccDTO tlccCheck : checks) {
    //
    // if (tlccCheck.getCheckId()!=null){
    // if ((tlccCheck.getStatus()!= CheckStatus.SUCCESS) &&
    // !tlccCheck.getCheckId().equalsIgnoreCase("DigitalIdentityRules.X509SUBJECTNAME_DIFFERENT_FROM_OTHER_X509CERTS")){
    // System.out.println(StringUtils.remove("TLCC;" + tlccCheck.getLocation()+";"+tlccCheck.getStatus()+";"+tlccCheck.getDescription(),","));
    // }
    // }else{
    // System.out.println("************** ERROR -> CheckId is null ***************");
    // System.out.println("************** Location : " +tlccCheck.getLocation() + " ***************");
    // System.out.println("************** Status : " +tlccCheck.getStatus() + " ***************");
    // }
    //
    //
    // }
    // for (TLChecks tlccCheck : checkResults) {
    // if (tlccCheck.getStatus()!= CheckStatus.SUCCESS){
    // System.out.println(StringUtils.remove("TLM;" + tlccCheck.getId()+";"+tlccCheck.getStatus()+";"+tlccCheck.getDescription(),","));
    // }
    // }
    //
    //
    // return checks.size();
    // }
    //
    // private int createTLinDB(TLType type) {
    // DBTrustedLists trustedList = new DBTrustedLists();
    // trustedList.setType(type);
    // trustedList.setXmlFile(new DBFiles());
    // tlRepository.save(trustedList);
    // return trustedList.getId();
    // }
}
