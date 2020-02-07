package eu.europa.ec.joinup.tsl.business.util;

import org.junit.Test;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;

public class TrustMarkIdentifierTest extends AbstractSpringTest {

    // @Autowired
    // private LoadingJob loadingJob;
    //
    // @Autowired
    // private TLRepository tlRepo;
    //
    // @Autowired
    // private TLService tlService;

    @Test
    public void test() {
        // Empty method for jenkins
    }

    // Not used a integration test
    // @Test
    // @Transactional
    // public void tspListTest() throws Exception {
    // Map<String, String> tmTSPName = new HashMap<String, String>();
    // Map<String, String> tmTSPVat = new HashMap<String, String>();
    // Map<String, String> tmServiceName = new HashMap<String, String>();
    // Map<String, String> tmServiceSKI = new HashMap<String, String>();
    //
    // // int nbTspName = 0;
    // // int nbTspVat = 0;
    // // int nbTspServiceName = 0;
    // // int nbServiceSki = 0;
    //
    // loadingJob.loadLOTL();
    //
    // List<DBTrustedLists> tls = (List<DBTrustedLists>) tlRepo.findAll();
    //
    // for (DBTrustedLists dbtl : tls) {
    // String cc = dbtl.getTerritory().getCodeTerritory();
    // System.out.println("PARSE " + cc);
    // TL tl = tlService.getTL(dbtl.getId());
    // if (tl != null) {
    // if (tl.getServiceProviders() != null) {
    // for (TLServiceProvider tsp : tl.getServiceProviders()) {
    // if (tsp != null) {
    // // TSP Name
    // if (tsp.getTSPName() != null) {
    // for (TLName name : tsp.getTSPName()) {
    // if (name.getLanguage().equalsIgnoreCase("EN")) {
    // tmTSPName.put(cc + ";" + name.getId() + ";" + LocationUtils.idUserReadable(tl, name.getId()),
    // name.getValue());
    // }
    // }
    // }
    // // Trade Name
    // boolean tradeNameFound = false;
    // if (tsp.getTSPTradeName() != null) {
    // for (TLName tname : tsp.getTSPTradeName()) {
    // if (tname.getValue().toUpperCase().startsWith("VAT") || tname.getValue().toUpperCase().startsWith("NTR")) {
    // tmTSPVat.put(cc + ";" + tname.getId() + ";" + LocationUtils.idUserReadable(tl, tname.getId()),
    // tname.getValue());
    // tradeNameFound = true;
    // }
    // }
    // }
    // if (!tradeNameFound) {
    // tmTSPVat.put(cc + ";" + tsp.getId() + "_" + Tag.TSP_TRADE_NAME + ";"
    // + LocationUtils.idUserReadable(tl, tsp.getId() + "_" + Tag.TSP_TRADE_NAME), "NO VALUE");
    // }
    // // Service
    // if (tsp.getTSPServices() != null) {
    // for (TLServiceDto service : tsp.getTSPServices()) {
    // if (service != null) {
    // boolean sNameFound = false;
    // if (service.getServiceName() != null) {
    // // Name
    // for (TLName name : service.getServiceName()) {
    // if (name.getLanguage().equalsIgnoreCase("EN")) {
    // tmServiceName.put(cc + ";" + name.getId() + ";" + LocationUtils.idUserReadable(tl, name.getId()),
    // name.getValue());
    // sNameFound = true;
    // }
    // }
    // }
    // if (!sNameFound) {
    // tmServiceName.put(cc + ";" + service.getId() + "_" + Tag.SERVICE_NAME + ";"
    // + LocationUtils.idUserReadable(tl, service.getId() + "_" + Tag.SERVICE_NAME), "NO VALUE");
    // }
    //
    // // Digital Id
    // boolean skiFound = false;
    // if (service.getDigitalIdentification() != null) {
    // for (TLDigitalIdentification di : service.getDigitalIdentification()) {
    // if ((di != null) && (di.getCertificateList() != null)) {
    // for (TLCertificate cert : di.getCertificateList()) {
    // if (cert.getCertSkiB64() != null) {
    // skiFound = true;
    // tmServiceSKI.put(
    // cc + ";" + cert.getId() + ";" + LocationUtils.idUserReadable(tl, cert.getId()),
    // cert.getCertSkiB64()+";"+service.getCurrentStatus());
    // }
    // }
    // }
    // }
    // }
    //
    // if (!skiFound) {
    // tmServiceSKI.put(
    // cc + ";" + service.getId() + "_" + Tag.SERVICE_DIGITAL_IDENTITY + ";"
    // + LocationUtils.idUserReadable(tl, service.getId() + "_" + Tag.SERVICE_DIGITAL_IDENTITY),
    // "NO VALUE"+";"+service.getCurrentStatus());
    // }
    // }
    // }
    // }
    // }
    // }
    // }
    // }
    // }
    //
    // StringBuilder builder = new StringBuilder();
    // for (Entry<String, String> en : tmTSPName.entrySet()) {
    // builder.append(en.getKey());
    // builder.append(";");
    // builder.append(en.getValue());
    // builder.append("\n");
    // }
    // FileUtils.writeStringToFile(new File("src/test/resources/TrustMarkResult/TrustMark - TSP Name.csv"), builder.toString());
    //
    // builder = new StringBuilder();
    // for (Entry<String, String> en : tmTSPVat.entrySet()) {
    // builder.append(en.getKey());
    // builder.append(";");
    // builder.append(en.getValue());
    // builder.append("\n");
    // }
    // FileUtils.writeStringToFile(new File("src/test/resources/TrustMarkResult/TrustMark - TSP VAT.csv"), builder.toString());
    //
    // builder = new StringBuilder();
    // for (Entry<String, String> en : tmServiceName.entrySet()) {
    // builder.append(en.getKey());
    // builder.append(";");
    // builder.append(en.getValue());
    // builder.append("\n");
    // }
    // FileUtils.writeStringToFile(new File("src/test/resources/TrustMarkResult/TrustMark - Service Name.csv"), builder.toString());
    //
    // builder = new StringBuilder();
    // for (Entry<String, String> en : tmServiceSKI.entrySet()) {
    // builder.append(en.getKey());
    // builder.append(";");
    // builder.append(en.getValue());
    // builder.append("\n");
    // }
    // FileUtils.writeStringToFile(new File("src/test/resources/TrustMarkResult/TrustMark - Service SKI.csv"), builder.toString());
    //
    // }
}
