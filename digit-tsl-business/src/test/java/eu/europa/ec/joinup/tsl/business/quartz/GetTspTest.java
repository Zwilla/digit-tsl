package eu.europa.ec.joinup.tsl.business.quartz;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLElectronicAddress;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLKeyUsage;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLKeyUsageBit;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLName;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPolicies;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPoliciesBit;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPostalAddress;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLQualificationExtension;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceDto;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceExtension;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceHistory;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLSupplyPoint;
import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.business.service.LoadingJobService;
import eu.europa.ec.joinup.tsl.business.service.TLService;
import eu.europa.ec.joinup.tsl.business.util.DateUtils;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;

public class GetTspTest extends AbstractSpringTest {

  @Autowired private LoadingJobService loadingJobService;

  @Autowired private TLRepository tlRepo;

  @Autowired private TLService tlService;

  @Test
  public void test() {
    // Empty method for jenkins
  }

  public void getSupplyPoint() throws Exception {
    loadingJobService.start();

    StringBuilder stringBuilder = new StringBuilder();
    List<DBTrustedLists> tls = (List<DBTrustedLists>) tlRepo.findAll();
    for (DBTrustedLists dbtl : tls) {
      String cc = dbtl.getTerritory().getCodeTerritory();
      TL tl = tlService.getTL(dbtl.getId());
      if (tl != null) {
        if (tl.getServiceProviders() != null) {
          for (TLServiceProvider tsp : tl.getServiceProviders()) {
            if (!CollectionUtils.isEmpty(tsp.getTSPServices())) {
              for (TLServiceDto service : tsp.getTSPServices()) {
                if (!CollectionUtils.isEmpty(service.getSupplyPoint())) {
                  for (TLSupplyPoint tlSupplyPoint : service.getSupplyPoint()) {
                    stringBuilder.append(cc).append("|").append(tsp.getName()).append("|").append(getName(service.getServiceName())).append("|").append(tlSupplyPoint.getValue()).append("\n");
                  }
                }
              }
            }
          }
        }
      }
    }

    System.out.println(stringBuilder.toString());
    FileUtils.writeByteArrayToFile(
        new File("src/test/resources/service_supply_points.csv"),
        stringBuilder.toString().getBytes());
  }

  public void getTradeName() throws Exception {
    loadingJobService.start();

    StringBuilder stringBuilder = new StringBuilder();
    List<DBTrustedLists> tls = (List<DBTrustedLists>) tlRepo.findAll();
    for (DBTrustedLists dbtl : tls) {
      String cc = dbtl.getTerritory().getCodeTerritory();
      TL tl = tlService.getTL(dbtl.getId());
      if (tl != null) {
        if (tl.getServiceProviders() != null) {
          for (TLServiceProvider tsp : tl.getServiceProviders()) {

            boolean tspVat = false;
            boolean tspNtr = false;

            if (!CollectionUtils.isEmpty(tsp.getTSPTradeName())) {

              for (TLName tlName : tsp.getTSPTradeName()) {
                if (tlName.getLanguage().equals("en")) {
                  if (tlName.getValue().startsWith("VAT")) {
                    tspVat = true;
                  } else if (tlName.getValue().startsWith("NTR")) {
                    tspNtr = true;
                  }
                }
              }
            }
            stringBuilder
                .append(cc)
                .append("|")
                .append(tsp.getName())
                .append("|")
                .append(tspVat)
                .append("|")
                .append(tspNtr)
                .append("\n");
          }
        }
      }
    }

    System.out.println(stringBuilder.toString());
    FileUtils.writeByteArrayToFile(
        new File("src/test/resources/tsp_trade_names.csv"), stringBuilder.toString().getBytes());
  }

  public void getHistoricNameMismatch() throws Exception {
    loadingJobService.start();

    StringBuilder stringBuilder = new StringBuilder();
    List<DBTrustedLists> tls = (List<DBTrustedLists>) tlRepo.findAll();
    for (DBTrustedLists dbtl : tls) {
      String cc = dbtl.getTerritory().getCodeTerritory();
      TL tl = tlService.getTL(dbtl.getId());
      if (tl != null) {
        if (tl.getServiceProviders() != null) {
          for (TLServiceProvider tsp : tl.getServiceProviders()) {
            if (tsp != null) {
              if (!CollectionUtils.isEmpty(tsp.getTSPServices())) {
                for (TLServiceDto service : tsp.getTSPServices()) {
                  String serviceName = getName(service.getServiceName());
                  if (!CollectionUtils.isEmpty(service.getHistory())) {
                    for (TLServiceHistory history : service.getHistory()) {
                      String historyName = getName(history.getServiceName());
                      if (!serviceName.equals(historyName)) {
                        stringBuilder.append(cc).append("|").append(tsp.getName()).append("|").append(serviceName).append("|").append(historyName).append("\n");
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

    System.out.println(stringBuilder.toString());
    FileUtils.writeByteArrayToFile(
        new File("src/test/resources/service_history_names.csv"),
        stringBuilder.toString().getBytes());
  }

  private String getName(List<TLName> serviceName) {
    for (TLName tlName : serviceName) {
      if (tlName.getLanguage().equals("en")) {
        return tlName.getValue();
      }
    }
    return "No english name found";
  }

  public void getCurrentStatusTest() throws Exception {
    loadingJobService.start();

    StringBuilder stringBuilder = new StringBuilder();
    List<DBTrustedLists> tls = (List<DBTrustedLists>) tlRepo.findAll();
    for (DBTrustedLists dbtl : tls) {
      String cc = dbtl.getTerritory().getCodeTerritory();
      TL tl = tlService.getTL(dbtl.getId());
      if (tl != null) {
        if (tl.getServiceProviders() != null) {
          for (TLServiceProvider tsp : tl.getServiceProviders()) {
            if (tsp != null) {
              if (!CollectionUtils.isEmpty(tsp.getTSPServices())) {
                for (TLServiceDto service : tsp.getTSPServices()) {
                  Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                  cal.setTimeInMillis(1467324000000L);

                  if (service.getCurrentStatusStartingDate().before(cal.getTime())) {
                    stringBuilder.append(cc)
                        .append("|")
                        .append(tsp.getName())
                        .append("|")
                        .append(service.getServiceName().get(0).getValue())
                        .append("|")
                        .append(service.getCurrentStatus())
                        .append("|")
                        .append(service.getCurrentStatusStartingDate())
                        .append("\n");
                  }
                }
              }
            }
          }
        }
      }
    }

    System.out.println(stringBuilder.toString());
    FileUtils.writeByteArrayToFile(
        new File("src/test/resources/eidas_starting_date_false.csv"),
        stringBuilder.toString().getBytes());
  }

  // Not used a integration test
  public void tspListTest() {
    loadingJobService.start();

    StringBuilder stringBuilder = new StringBuilder();
    List<DBTrustedLists> tls = (List<DBTrustedLists>) tlRepo.findAll();
    for (DBTrustedLists dbtl : tls) {
      String cc = dbtl.getTerritory().getCodeTerritory();
      String tspName = "";
      String serviceType = "";
      String adresse = "";
      String mail = "";
      TL tl = tlService.getTL(dbtl.getId());
      if (tl != null) {
        if (tl.getServiceProviders() != null) {
          for (TLServiceProvider tsp : tl.getServiceProviders()) {
            if (tsp != null) {
              if (tsp.getTSPName() != null) {
                for (TLName name : tsp.getTSPName()) {
                  if (name.getLanguage().equalsIgnoreCase("EN")) {
                    tspName = name.getValue();
                  }
                }
              }
              if (tsp.getTSPPostal() != null) {
                for (TLPostalAddress adr : tsp.getTSPPostal()) {
                  if (adr.getLanguage().equalsIgnoreCase("EN")) {
                    adresse =
                        adr.getStreet()
                            + " - "
                            + adr.getLocality()
                            + " - "
                            + adr.getPostalCode()
                            + " - "
                            + adr.getStateOrProvince()
                            + " - "
                            + adr.getCountryCode();
                  }
                }
              }
              if (tsp.getTSPElectronic() != null) {
                for (TLElectronicAddress adr : tsp.getTSPElectronic()) {
                  if (adr.getLanguage().equalsIgnoreCase("EN")) {
                    mail = adr.getValue();
                  }
                }
              }
              if (tsp.getTSPServices() != null) {
                for (TLServiceDto service : tsp.getTSPServices()) {
                  if (service != null) {
                    serviceType = service.getTypeIdentifier();
                    stringBuilder.append(cc).append(" | ").append(tspName).append(" | ").append(adresse).append(" | ").append(mail).append(" | ").append(serviceType).append(";\n");
                  }
                }
              }
            }
          }
        }
      }
    }

    String finalString = stringBuilder.toString();
    System.out.println(finalString);
  }

  public void tspMailsList() throws Exception {
    loadingJobService.start();

    List<String> qualifiedTypes = new ArrayList<>();
    qualifiedTypes.add("https://uri.etsi.org/TrstSvc/Svctype/TSA/QTST");
    qualifiedTypes.add("https://uri.etsi.org/TrstSvc/Svctype/CA/QC");
    qualifiedTypes.add("https://uri.etsi.org/TrstSvc/Svctype/EDS/REM/Q");
    qualifiedTypes.add("https://uri.etsi.org/TrstSvc/Svctype/Certstatus/OCSP/QC");
    qualifiedTypes.add("https://uri.etsi.org/TrstSvc/Svctype/PSES/Q");
    qualifiedTypes.add("https://uri.etsi.org/TrstSvc/Svctype/QESValidation/Q");
    qualifiedTypes.add("https://uri.etsi.org/TrstSvc/Svctype/EDS/Q");

    Set<String> emails = new HashSet<>();
    List<DBTrustedLists> tls = (List<DBTrustedLists>) tlRepo.findAll();
    for (DBTrustedLists dbtl : tls) {
      TL tl = tlService.getTL(dbtl.getId());
      if (tl != null) {
        if (tl.getServiceProviders() != null) {
          for (TLServiceProvider tsp : tl.getServiceProviders()) {
            if (tsp != null) {
              if (tsp.getTSPServices() != null) {
                for (TLServiceDto service : tsp.getTSPServices()) {
                  if (service != null) {
                    if (qualifiedTypes.contains(service.getTypeIdentifier())) {
                      if (tsp.getTSPElectronic() != null) {
                        for (TLElectronicAddress adr : tsp.getTSPElectronic()) {
                          if (adr.getValue().contains("mailto:")) {
                            emails.add(adr.getValue().replace("mailto:", ""));
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
      }
    }

    StringBuilder sb = new StringBuilder();
    for (String string : emails) {
      sb.append(string).append(";");
    }
    FileUtils.writeByteArrayToFile(
        new File("src/test/resources/tsp_emails.txt"), sb.toString().getBytes());
  }

  public void getTakenOverBy() throws Exception {
    loadingJobService.start();

    List<DBTrustedLists> tls = (List<DBTrustedLists>) tlRepo.findAll();
    StringBuilder sb = new StringBuilder();
    StringBuilder sb2 = new StringBuilder();
    for (DBTrustedLists dbtl : tls) {
      TL tl = tlService.getTL(dbtl.getId());
      if (tl != null) {
        String cc = tl.getDbCountryName();
        Set<String> tspNames = new HashSet<>();
        if (tl.getServiceProviders() != null) {
          for (TLServiceProvider tsp : tl.getServiceProviders()) {
            for (TLName tlName : tsp.getTSPName()) {
              if (tlName.getLanguage().equalsIgnoreCase("en")) {
                tspNames.add(tlName.getValue());
              }
            }
          }

          for (TLServiceProvider tsp : tl.getServiceProviders()) {
            if (tsp != null) {
              int nbServiceTOB = 0;
              String name = tsp.getName();
              boolean tobUpdate = false;
              boolean tobGranted = false;
              for (TLServiceDto service : tsp.getTSPServices()) {
                boolean countUpdate = false;
                if (service != null) {
                  String sName = service.getServiceName().get(0).getValue();
                  if (!service
                          .getCurrentStatus()
                          .equals("https://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/granted")
                      && !service
                          .getCurrentStatus()
                          .equals(
                              "https://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/recognisedatnationallevel")) {
                    countUpdate = true;
                  }

                  if (!CollectionUtils.isEmpty(service.getExtension())) {
                    for (TLServiceExtension extension : service.getExtension()) {
                      if (extension.getTakenOverBy() != null) {
                        if (service
                                .getCurrentStatus()
                                .equals("https://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/granted")
                            || service
                                .getCurrentStatus()
                                .equals(
                                    "https://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/recognisedatnationallevel")) {
                          tobGranted = true;
                        }
                        countUpdate = true;
                        tobUpdate = true;
                        for (TLName extName : extension.getTakenOverBy().getTspName()) {
                          if (extName.getLanguage().equalsIgnoreCase("en")) {
                            sb.append(cc)
                                .append("|")
                                .append(name)
                                .append("|")
                                .append(sName)
                                .append("|")
                                .append(service.getCurrentStatus())
                                .append("|")
                                .append(extName.getValue())
                                .append("|")
                                .append(tspNames.contains(extName.getValue()))
                                .append("\n");
                          }
                        }
                      }
                    }
                  }
                }

                if (countUpdate) {
                  nbServiceTOB = nbServiceTOB + 1;
                }
              }

              if ((nbServiceTOB > 0) && tobUpdate) {
                if (nbServiceTOB >= tsp.getTSPServices().size()) {
                  sb2.append(cc)
                      .append("|")
                      .append(name)
                      .append("|")
                      .append("OK")
                      .append("|")
                      .append(tobGranted)
                      .append("\n");
                } else {
                  sb2.append(cc)
                      .append("|")
                      .append(name)
                      .append("|")
                      .append("NOK")
                      .append("|")
                      .append(tobGranted)
                      .append("\n");
                }
              }
            }
          }
        }
      }
    }

    FileUtils.writeByteArrayToFile(
        new File("src/test/resources/taken_over_by.csv"), sb.toString().getBytes());
    FileUtils.writeByteArrayToFile(
        new File("src/test/resources/taken_over_by_tsp.csv"), sb2.toString().getBytes());
  }

  public void getManagedOnBehalf() throws Exception {
    loadingJobService.start();
    List<DBTrustedLists> tls = (List<DBTrustedLists>) tlRepo.findAll();
    StringBuilder sb = new StringBuilder();
    for (DBTrustedLists dbtl : tls) {
      TL tl = tlService.getTL(dbtl.getId());
      if (tl != null) {
        String cc = tl.getDbCountryName();
        if (tl.getServiceProviders() != null) {
          for (TLServiceProvider tsp : tl.getServiceProviders()) {
            if (tsp != null) {
              String name = tsp.getName();
              for (TLServiceDto service : tsp.getTSPServices()) {
                boolean found = false;
                if (service != null) {
                  String sName = service.getServiceName().get(0).getValue();

                  if (!CollectionUtils.isEmpty(service.getExtension())) {
                    for (TLServiceExtension extension : service.getExtension()) {
                      if (extension.getQualificationsExtension() != null) {
                        for (TLQualificationExtension tlQualificationExtension :
                            extension.getQualificationsExtension()) {
                          for (String string : tlQualificationExtension.getQualifTypeList()) {
                              if (string.equalsIgnoreCase(
                                      "https://uri.etsi.org/TrstSvc/TrustedList/SvcInfoExt/QCQSCDManagedOnBehalf")) {
                                  found = true;
                                  break;
                              }
                          }
                        }
                      }
                    }
                  }
                  if (found) {
                    sb.append(cc).append("|").append(name).append("|").append(sName).append("\n");
                  }
                }
              }
            }
          }
        }
      }
    }

    FileUtils.writeByteArrayToFile(
        new File("src/test/resources/managed_on_behalf.csv"), sb.toString().getBytes());
  }

  public void getSieQ() throws Exception {
    loadingJobService.start();
    List<DBTrustedLists> tls = (List<DBTrustedLists>) tlRepo.findAll();
    StringBuilder sb = new StringBuilder();
    for (DBTrustedLists dbtl : tls) {
      TL tl = tlService.getTL(dbtl.getId());
      if (tl != null) {
        String cc = tl.getDbCountryName();
        if (tl.getServiceProviders() != null) {
          for (TLServiceProvider tsp : tl.getServiceProviders()) {
            if (tsp != null) {
              String name = tsp.getName();
              for (TLServiceDto service : tsp.getTSPServices()) {
                if (service != null) {
                  String sName =
                      service.getServiceName().get(0).getValue()
                          + " - "
                          + service
                              .getCurrentStatus()
                              .replace("https://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/", "")
                          + " : "
                          + DateUtils.getToFormatYMDHMS(service.getCurrentStatusStartingDate());
                  if (!CollectionUtils.isEmpty(service.getExtension())) {
                    for (TLServiceExtension extension : service.getExtension()) {
                      if (extension.getQualificationsExtension() != null) {
                        sb.append(cc)
                            .append("\n")
                            .append(name)
                            .append("\n")
                            .append(sName)
                            .append("\n");

                        StringBuilder qualif = new StringBuilder();
                        StringBuilder criteria = new StringBuilder();
                        String critAssert = "";
                        for (TLQualificationExtension tlQualificationExtension :
                            extension.getQualificationsExtension()) {
                          qualif.append(tlQualificationExtension.getQualifTypeList().toString());
                          if (tlQualificationExtension.getCriteria() != null) {
                            critAssert = tlQualificationExtension.getCriteria().getAsserts();
                            if (!CollectionUtils.isEmpty(
                                tlQualificationExtension.getCriteria().getKeyUsage())) {
                              int i = 0;
                              for (TLKeyUsage tlKeyUsage :
                                  tlQualificationExtension.getCriteria().getKeyUsage()) {
                                criteria.append("\tKey usages ").append(i).append(":\n");
                                for (TLKeyUsageBit tlKeyUsageBit : tlKeyUsage.getKeyUsageBit()) {
                                  criteria
                                      .append("\t\t")
                                      .append(tlKeyUsageBit.getValue())
                                      .append("(")
                                      .append(tlKeyUsageBit.getIsValue())
                                      .append(")\n");
                                }
                                i = i + 1;
                              }
                            }

                            if (!CollectionUtils.isEmpty(
                                tlQualificationExtension.getCriteria().getPolicyList())) {
                              int i = 0;
                              for (TLPolicies tlPolicy :
                                  tlQualificationExtension.getCriteria().getPolicyList()) {
                                criteria.append("\tPolicy list ").append(i).append(":\n");
                                for (TLPoliciesBit tlPoliciesBit : tlPolicy.getPolicyBit()) {
                                  criteria
                                      .append("\t\t")
                                      .append(tlPoliciesBit.getIdentifierValue())
                                      .append("\n");
                                }
                                i = i + 1;
                              }
                            }

                            if (!CollectionUtils.isEmpty(
                                tlQualificationExtension.getCriteria().getCriteriaList())) {
                              criteria
                                  .append(
                                      tlQualificationExtension
                                          .getCriteria()
                                          .getCriteriaList()
                                          .size())
                                  .append(" criteria list\n");
                            }
                          } else {
                            critAssert = "No Criteria";
                            criteria = new StringBuilder("No criteria");
                          }

                          sb.append(qualif)
                              .append("\nAssert:")
                              .append(critAssert)
                              .append("\nCriterias:\n")
                              .append(criteria)
                              .append("\n\n\n");
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
    }

    FileUtils.writeByteArrayToFile(
        new File("src/test/resources/trust_services_sieQ.txt"), sb.toString().getBytes());
  }
}
