/*******************************************************************************
 * DIGIT-TSL - Trusted List Manager
 * Copyright (C) 2018 European Commission, provided under the CEF E-Signature programme
 * 
 * This file is part of the "DIGIT-TSL - Trusted List Manager" project.
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
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
import eu.europa.ec.joinup.tsl.business.dto.tl.TLName;
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
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;

public class GetTspTest extends AbstractSpringTest {

    @Autowired
    private LoadingJobService loadingJobService;

    @Autowired
    private TLRepository tlRepo;

    @Autowired
    private TLService tlService;

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
                                        stringBuilder.append(cc + "|" + tsp.getName() + "|" + getName(service.getServiceName()) + "|" + tlSupplyPoint.getValue() + "\n");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        System.out.println(stringBuilder.toString());
        FileUtils.writeByteArrayToFile(new File("src/test/resources/service_supply_points.csv"), stringBuilder.toString().getBytes());
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
                        if (tl != null) {

                            Boolean tspVat = false;
                            Boolean tspNtr = false;

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
                            stringBuilder.append(cc + "|" + tsp.getName() + "|" + tspVat + "|" + tspNtr + "\n");
                        }
                    }
                }
            }
        }

        System.out.println(stringBuilder.toString());
        FileUtils.writeByteArrayToFile(new File("src/test/resources/tsp_trade_names.csv"), stringBuilder.toString().getBytes());
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
                                                stringBuilder.append(cc + "|" + tsp.getName() + "|" + serviceName + "|" + historyName + "\n");
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
        FileUtils.writeByteArrayToFile(new File("src/test/resources/service_history_names.csv"), stringBuilder.toString().getBytes());
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
                                        stringBuilder.append(cc + "|" + tsp.getName() + "|" + service.getServiceName().get(0).getValue() + "|" + service.getCurrentStatus() + "|"
                                                + service.getCurrentStatusStartingDate() + "\n");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        System.out.println(stringBuilder.toString());
        FileUtils.writeByteArrayToFile(new File("src/test/resources/eidas_starting_date_false.csv"), stringBuilder.toString().getBytes());
    }

    // Not used a integration test
    public void tspListTest() throws Exception {
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
                                        adresse = adr.getStreet() + " - " + adr.getLocality() + " - " + adr.getPostalCode() + " - " + adr.getStateOrProvince() + " - "
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
                                        stringBuilder.append(cc + " | " + tspName + " | " + adresse + " | " + mail + " | " + serviceType + ";\n");
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
        qualifiedTypes.add("http://uri.etsi.org/TrstSvc/Svctype/TSA/QTST");
        qualifiedTypes.add("http://uri.etsi.org/TrstSvc/Svctype/CA/QC");
        qualifiedTypes.add("http://uri.etsi.org/TrstSvc/Svctype/EDS/REM/Q");
        qualifiedTypes.add("http://uri.etsi.org/TrstSvc/Svctype/Certstatus/OCSP/QC");
        qualifiedTypes.add("http://uri.etsi.org/TrstSvc/Svctype/PSES/Q");
        qualifiedTypes.add("http://uri.etsi.org/TrstSvc/Svctype/QESValidation/Q");
        qualifiedTypes.add("http://uri.etsi.org/TrstSvc/Svctype/EDS/Q");

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
            sb.append(string + ";");
        }
        FileUtils.writeByteArrayToFile(new File("src/test/resources/tsp_emails.txt"), sb.toString().getBytes());
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
                            Boolean tobUpdate = false;
                            Boolean tobGranted = false;
                            for (TLServiceDto service : tsp.getTSPServices()) {
                                Boolean countUpdate = false;
                                if (service != null) {
                                    String sName = service.getServiceName().get(0).getValue();
                                    if (!service.getCurrentStatus().equals("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/granted")
                                            && !service.getCurrentStatus().equals("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/recognisedatnationallevel")) {
                                        countUpdate = true;
                                    }

                                    if (!CollectionUtils.isEmpty(service.getExtension())) {
                                        for (TLServiceExtension extension : service.getExtension()) {
                                            if (extension.getTakenOverBy() != null) {
                                                if (service.getCurrentStatus().equals("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/granted")
                                                        || service.getCurrentStatus().equals("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/recognisedatnationallevel")) {
                                                    tobGranted = true;
                                                }
                                                countUpdate = true;
                                                tobUpdate = true;
                                                for (TLName extName : extension.getTakenOverBy().getTspName()) {
                                                    if (extName.getLanguage().equalsIgnoreCase("en")) {
                                                        sb.append(cc + "|" + name + "|" + sName + "|" + service.getCurrentStatus() + "|" + extName.getValue() + "|"
                                                                + tspNames.contains(extName.getValue()) + "\n");
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
                                    sb2.append(cc + "|" + name + "|" + "OK" + "|" + tobGranted + "\n");
                                } else {
                                    sb2.append(cc + "|" + name + "|" + "NOK" + "|" + tobGranted + "\n");
                                }
                            }
                        }
                    }
                }
            }
        }

        FileUtils.writeByteArrayToFile(new File("src/test/resources/taken_over_by.csv"), sb.toString().getBytes());
        FileUtils.writeByteArrayToFile(new File("src/test/resources/taken_over_by_tsp.csv"), sb2.toString().getBytes());
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
                                Boolean found = false;
                                if (service != null) {
                                    String sName = service.getServiceName().get(0).getValue();

                                    if (!CollectionUtils.isEmpty(service.getExtension())) {
                                        for (TLServiceExtension extension : service.getExtension()) {
                                            if (extension.getQualificationsExtension() != null) {
                                                for (TLQualificationExtension tlQualificationExtension : extension.getQualificationsExtension()) {
                                                    for (String string : tlQualificationExtension.getQualifTypeList()) {
                                                        if (string.equalsIgnoreCase("http://uri.etsi.org/TrstSvc/TrustedList/SvcInfoExt/QCQSCDManagedOnBehalf")) {
                                                            found = true;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (found) {
                                        sb.append(cc + "|" + name + "|" + sName + "\n");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        FileUtils.writeByteArrayToFile(new File("src/test/resources/managed_on_behalf.csv"), sb.toString().getBytes());
    }
}
