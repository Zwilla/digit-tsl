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

import org.junit.Assert;
import org.junit.Test;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;

public class CountObject extends AbstractSpringTest {

    //    @Autowired
    //    private TLService tlService;
    //
    //    @Autowired
    //    private LoadingJobService loadingJobService;
    //
    //    @Autowired
    //    private RulesValidationJobService rulesValidationJobService;
    //
    //    @Autowired
    //    private SignatureValidationJobService signatureValidationJobService;
    //
    //    @Autowired
    //    private ErrorAnalysisService errorAnalysisService;

    //Empty test for jenkins
    @Test
    public void emptyTest() {
        Assert.assertTrue(true);
    }

    //    @Test
    //    public void testQuartzMethod() throws Exception {
    //        loadingJobService.start();
    //        signatureValidationJobService.start();
    //        rulesValidationJobService.start();
    //
    //        byte[] b = errorAnalysisService.getErrorAnalysis();
    //
    //        FileUtils.writeByteArrayToFile(new File("src/test/resources/check_analysis.xls"), b);
    //
    //    }

    //        @Test
    //        public void tlBrowserTypesAnalysis() throws Exception {
    //            String jump = " \n\n";
    //            String space = "    ";
    //            loadingJobService.start();
    //            List<TrustedListsReport> allProdTlReports = tlService.getAllProdTlReports();
    //            for (TrustedListsReport tlr : allProdTlReports) {
    //                TL tl = tlService.getTL(tlr.getId());
    //
    //                if ((tl != null) && (tl.getDbCountryName() != null)) {
    //                    StringBuilder builder = new StringBuilder();
    //                    builder.append(tl.getDbCountryName() + jump);
    //                    if (tl.getServiceProviders() != null) {
    //                        for (TLServiceProvider sp : tl.getServiceProviders()) {
    //                            if (sp.getTSPServices() != null) {
    //                                builder.append(space + sp.getName() + jump);
    //                                for (TLServiceDto service : sp.getTSPServices()) {
    //                                    if (service != null) {
    //                                        builder.append(space + space + service.getServiceName().get(0).getValue() + " - " + service.getTypeIdentifier() + jump);
    //                                        if (service.getExtension() != null) {
    //                                            for (TLServiceExtension ext : service.getExtension()) {
    //                                                if (ext.getAdditionnalServiceInfo() != null) {
    //                                                    builder.append(space + space + space + ext.getAdditionnalServiceInfo().getValue() + jump);
    //                                                }
    //                                            }
    //                                        }
    //                                    }
    //                                }
    //                            }
    //                        }
    //                    }
    //                    FileUtils.writeStringToFile(new File("src/test/resources/tlbStatusAnalysis/" + tl.getDbCountryName() + ".txt"), builder.toString());
    //                }
    //            }
    //
    //        }

    //    @Test
    //    public void calculateTrustServiceType() throws Exception {
    //        String jump = " \n\n";
    //        String space = "    ";
    //        loadingJobService.start();
    //        List<TrustedListsReport> allProdTlReports = tlService.getAllProdTlReports();
    //        for (TrustedListsReport tlr : allProdTlReports) {
    //            TL tl = tlService.getTL(tlr.getId());
    //
    //            if ((tl != null) && (tl.getDbCountryName() != null)) {
    //                StringBuilder builder = new StringBuilder();
    //                builder.append(tl.getDbCountryName() + jump);
    //                if (tl.getServiceProviders() != null) {
    //                    for (TLServiceProvider sp : tl.getServiceProviders()) {
    //                        if (sp.getTSPServices() != null) {
    //                            builder.append(space + sp.getName() + jump);
    //                            for (TLServiceDto service : sp.getTSPServices()) {
    //                                if (service != null) {
    //                                    builder.append(space + space + service.getServiceName().get(0).getValue() + " - " + service.getTypeIdentifier() + jump);
    //                                    Set<String> serviceTypes = TLQServiceTypeUtils.getServiceTypes(service);
    //                                    if (CollectionUtils.isEmpty(serviceTypes)) {
    //                                        builder.append(space + space + space + " ! EMPTY LIST ! ");
    //                                    } else {
    //                                        builder.append(space + space + space);
    //                                        for (String type : serviceTypes) {
    //                                            builder.append(type + ";");
    //                                        }
    //                                    }
    //                                    builder.append(jump);
    //                                }
    //                            }
    //                        }
    //                    }
    //                }
    //                FileUtils.writeStringToFile(new File("src/test/resources/tlbStatusAnalysis/statusResult/" + tl.getDbCountryName() + ".txt"), builder.toString());
    //            }
    //        }
    //
    //    }

}
