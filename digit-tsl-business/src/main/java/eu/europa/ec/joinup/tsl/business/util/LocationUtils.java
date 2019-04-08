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
package eu.europa.ec.joinup.tsl.business.util;

import java.util.ResourceBundle;

import org.apache.commons.collections.CollectionUtils;

import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.ec.joinup.tsl.model.enums.MimeType;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

/**
 * Convert trusted list ID (object & node) into human readable location
 */
public class LocationUtils {

    private static ResourceBundle bundle = ResourceBundle.getBundle("messages");
    private static String multiple = "(s)";

    public static String idUserReadable(TL tl, String checksLocation) {
        StringBuffer hrLocation = new StringBuffer();
        String str1 = checksLocation.substring(checksLocation.indexOf('_') + 1);
        hrLocation.append(startHr(tl));

        if (str1.startsWith(Tag.TSL_TAG.toString())) {
            hrLocation.append(" || " + bundle.getString("check.tag"));
        } else if (str1.startsWith(Tag.POINTERS_TO_OTHER_TSL.toString())) {
            hrLocation.append(" || " + bundle.getString("check.pointers"));

            if (str1.equals(Tag.POINTERS_TO_OTHER_TSL.toString())) {
                return hrLocation.toString();
            } else {
                formatPointersToOtherTSL(tl, hrLocation, str1);
            }
        } else if (str1.startsWith(Tag.SCHEME_INFORMATION.toString())) {
            hrLocation.append(" || " + bundle.getString("check.schemeInformation"));

            if (str1.equals(Tag.SCHEME_INFORMATION.toString())) {
                return hrLocation.toString();
            } else if (str1.equals(Tag.SCHEME_INFORMATION + "_" + Tag.Structure)) {
                hrLocation.append(" || " + bundle.getString("tStrucuture"));
                return hrLocation.toString();
            } else {
                formatSchemeinformation(tl, hrLocation, str1);
            }
        } else if (str1.startsWith(Tag.TSP_SERVICE_PROVIDER.toString())) {
            hrLocation.append(" || " + bundle.getString("check.serviceProvider"));

            if (str1.equals(Tag.TSP_SERVICE_PROVIDER.toString())) {
                return hrLocation.toString();
            } else {
                formatServiceProvider(tl, hrLocation, str1);
            }
        } else if (str1.startsWith(Tag.TSP_SERVICE.toString())) {
            hrLocation.append(" || " + bundle.getString("check.service"));
        } else if (str1.startsWith(Tag.SERVICE_HISTORY.toString())) {
            hrLocation.append(" || " + bundle.getString("check.serviceHistory"));
        } else {
            if (checksLocation.contains(tl.getId() + "_" + Tag.SIGNATURE)) {
                if (checksLocation.equals(tl.getId() + "_" + Tag.SIGNATURE)) {
                    hrLocation.append(" || " + bundle.getString("tSignature"));
                } else if (checksLocation.equals(tl.getId() + "_" + Tag.SIGNATURE + "_" + Tag.DIGEST_ALGORITHM)) {
                    hrLocation.append(" || " + bundle.getString("tSignature") + " || " + bundle.getString("signature.digestAlgo"));
                } else if (checksLocation.equals(tl.getId() + "_" + Tag.SIGNATURE + "_" + Tag.ENCRYPTION_ALGORITHM)) {
                    hrLocation.append(" || " + bundle.getString("tSignature") + " || " + bundle.getString("signature.encryptionAlgo"));
                } else if (checksLocation.equals(tl.getId() + "_" + Tag.SIGNATURE + "_" + Tag.INDICATION)) {
                    hrLocation.append(" || " + bundle.getString("tSignature") + " || " + bundle.getString("signature.indication"));
                } else if (checksLocation.equals(tl.getId() + "_" + Tag.SIGNATURE + "_" + Tag.SIGNATURE_FORMAT)) {
                    hrLocation.append(" || " + bundle.getString("tSignature") + " || " + bundle.getString("signature.format"));
                } else if (checksLocation.equals(tl.getId() + "_" + Tag.SIGNATURE + "_" + Tag.SIGNED_BY)) {
                    hrLocation.append(" || " + bundle.getString("tSignature") + " || " + bundle.getString("signature.signBy"));
                } else if (checksLocation.equals(tl.getId() + "_" + Tag.SIGNATURE + "_" + Tag.SIGNING_DATE)) {
                    hrLocation.append(" || " + bundle.getString("tSignature") + " || " + bundle.getString("signature.signingDate"));
                }
            }
        }
        return hrLocation.toString();
    }

    private static void formatPointersToOtherTSL(TL tl, StringBuffer hrLocation, String str1) {
        String p_str2 = getLocation(str1, '_', 4);
        if (p_str2.contains("_")) {
            String pointerCheck = p_str2.substring(0, p_str2.indexOf('_'));

            if (isInt(pointerCheck) && pointerIsNotUndefined(tl, Integer.valueOf(pointerCheck))) {
                hrLocation.append(addPointerNameHr(tl, Integer.valueOf(pointerCheck)));
                String p_str3 = p_str2.substring(p_str2.indexOf('_') + 1);

                if (p_str3.startsWith(Tag.POINTER_TYPE.toString())) {
                    hrLocation.append(" || " + bundle.getString("tlType"));

                } else if (p_str3.startsWith(Tag.POINTER_LOCATION.toString())) {
                    hrLocation.append(" || " + bundle.getString("tlLocation"));

                } else if (p_str3.startsWith(Tag.SCHEME_OPERATOR_NAME.toString())) {
                    hrLocation.append(" || " + bundle.getString("schemeOpeName") + multiple);

                } else if (p_str3.startsWith(Tag.POINTER_COMMUNITY_RULE.toString())) {
                    hrLocation.append(" || " + bundle.getString("schemeInfo.communityRule") + multiple);

                } else if (p_str3.startsWith(Tag.POINTER_SCHEME_TERRITORY.toString())) {
                    hrLocation.append(" || " + bundle.getString("schemeTerritory"));

                } else if (p_str3.startsWith(Tag.POINTER_MIME_TYPE.toString())) {
                    hrLocation.append(" || " + bundle.getString("mimeType"));

                } else if (p_str3.startsWith(Tag.SERVICE_DIGITAL_IDENTITY.toString())) {
                    hrLocation.append(" || " + bundle.getString("schemeInfo.digitalIdentities"));
                    formatPointerDigitalIdentities(tl, hrLocation, p_str3, Integer.valueOf(pointerCheck));
                }
            }
        }

    }

    private static void formatPointerDigitalIdentities(TL tl, StringBuffer hrLocation, String strDigitalId, int pointerToCheck) {
        String di_str = getLocation(strDigitalId, '_', 3);
        if (di_str.contains("_")) {
            String digitalIdCheck = di_str.substring(0, di_str.indexOf('_'));
            if (isInt(digitalIdCheck) && pointerDigitalIsNotUndefined(tl, pointerToCheck, Integer.valueOf(digitalIdCheck))) {
                hrLocation.append(addPointerDigitalIdNameHr(tl, pointerToCheck, Integer.valueOf(digitalIdCheck)));
            }
        }
    }

    private static void formatSchemeinformation(TL tl, StringBuffer hrLocation, String str1) {
        String si_str2 = getLocation(str1, '_', 2);

        if (si_str2.startsWith(Tag.SEQUENCE_NUMBER.toString())) {
            hrLocation.append(" || " + bundle.getString("tlBrowser.sequenceNumber"));

        } else if (si_str2.startsWith(Tag.TSL_TYPE.toString())) {
            hrLocation.append(" || " + bundle.getString("schemeInfo.type"));

        } else if (si_str2.startsWith(Tag.SCHEME_OPERATOR_NAME.toString())) {
            hrLocation.append(" || " + bundle.getString("schemeOpeName") + multiple);
            String tmp = deleteStartingGivingFirst(si_str2, Tag.SCHEME_OPERATOR_NAME + "_");
            if (isInt(tmp)) {
                hrLocation.append(" || " + bundle.getString("tLanguage") + " : " + tl.getSchemeInformation().getSchemeOpeName().get(Integer.valueOf(tmp) - 1).getLanguage());
            }

        } else if (si_str2.startsWith(Tag.POSTAL_ADDRESSES.toString())) {
            hrLocation.append(" || " + bundle.getString("tlBrowser.postalAddress") + multiple);
            String tmp = deleteStartingGivingFirst(si_str2, Tag.POSTAL_ADDRESSES + "_");
            if (isInt(tmp)) {
                hrLocation.append(" || " + bundle.getString("tLanguage") + " : " + tl.getSchemeInformation().getSchemeOpePostal().get(Integer.valueOf(tmp) - 1).getLanguage());
            }

        } else if (si_str2.startsWith(Tag.ELECTRONIC_ADDRESS.toString())) {
            hrLocation.append(" || " + bundle.getString("tlBrowser.electronicAddress") + multiple);
            String tmp = deleteStartingGivingFirst(si_str2, Tag.ELECTRONIC_ADDRESS + "_");
            if (isInt(tmp)) {
                hrLocation.append(" || " + bundle.getString("tLanguage") + " : " + tl.getSchemeInformation().getSchemeOpeElectronic().get(Integer.valueOf(tmp) - 1).getLanguage());
            }

        } else if (si_str2.startsWith(Tag.SCHEME_NAME.toString())) {
            hrLocation.append(" || " + bundle.getString("schemeInfo.schemeName") + multiple);
            String tmp = deleteStartingGivingFirst(si_str2, Tag.SCHEME_NAME + "_");
            if (isInt(tmp)) {
                hrLocation.append(" || " + bundle.getString("tLanguage") + " : " + tl.getSchemeInformation().getSchemeName().get(Integer.valueOf(tmp) - 1).getLanguage());
            }

        } else if (si_str2.startsWith(Tag.SCHEME_INFORMATION_URI.toString())) {
            hrLocation.append(" || " + bundle.getString("serviceProvider.informationURI") + multiple);
            String tmp = deleteStartingGivingFirst(si_str2, Tag.SCHEME_INFORMATION_URI + "_");
            if (isInt(tmp)) {
                hrLocation.append(" || " + bundle.getString("tLanguage") + " : " + tl.getSchemeInformation().getSchemeInfoUri().get(Integer.valueOf(tmp) - 1).getLanguage());
            }

        } else if (si_str2.startsWith(Tag.STATUS_DETERMINATION.toString())) {
            hrLocation.append(" || " + bundle.getString("schemeInfo.statusDetermination"));

        } else if (si_str2.startsWith(Tag.SCHEME_TYPE_COMMUNITY_RULES.toString())) {
            hrLocation.append(" || " + bundle.getString("schemeInfo.communityRule") + multiple);
            String tmp = deleteStartingGivingFirst(si_str2, Tag.SCHEME_TYPE_COMMUNITY_RULES + "_");
            if (isInt(tmp)) {
                hrLocation.append(" || " + bundle.getString("tLanguage") + " : " + tl.getSchemeInformation().getSchemeTypeCommRule().get(Integer.valueOf(tmp) - 1).getLanguage());
            }

        } else if (si_str2.startsWith(Tag.POLICY_OR_LEGAL_NOTICE.toString())) {
            hrLocation.append(" || " + bundle.getString("schemeInfo.legalNotice") + multiple);
            String tmp = deleteStartingGivingFirst(si_str2, Tag.POLICY_OR_LEGAL_NOTICE + "_");
            if (isInt(tmp)) {
                hrLocation.append(" || " + bundle.getString("tLanguage") + " : " + tl.getSchemeInformation().getSchemePolicy().get(Integer.valueOf(tmp) - 1).getLanguage());
            }

        } else if (si_str2.startsWith(Tag.ISSUE_DATE.toString())) {
            hrLocation.append(" || " + bundle.getString("tlBrowser.issueDate"));

        } else if (si_str2.startsWith(Tag.NEXT_UPDATE.toString())) {
            hrLocation.append(" || " + bundle.getString("tlBrowser.expiryDate"));

        } else if (si_str2.startsWith(Tag.DISTRIBUTION_LIST.toString())) {
            hrLocation.append(" || " + bundle.getString("schemeInfo.distributionList") + multiple);

        } else if (si_str2.startsWith(Tag.TSL_TAG.toString())) {
            hrLocation.append(" || " + bundle.getString("tlBrowser.tslTag"));

        } else if (si_str2.startsWith(Tag.HISTORICAL_PERIOD.toString())) {
            hrLocation.append(" || " + bundle.getString("tlBrowser.historicalPeriod"));
        } else if (si_str2.startsWith(Tag.TSL_IDENTIFIER.toString())) {
            hrLocation.append(" || " + bundle.getString("tlBrowser.tslId"));
        }
    }

    private static String deleteStartingGivingFirst(String str, String tag) {
        str = str.replace(tag, "");
        if (str.contains("_")) {
            String[] splitStr = str.split("_");
            return splitStr[0];
        } else {
            return str.substring(0, 1);
        }
    }

    private static void formatServiceProvider(TL tl, StringBuffer hrLocation, String str1) {
        String str2 = getLocation(str1, '_', 3);
        if (str2.contains("_")) {
            String serviceProviderToCheck = str2.substring(0, str2.indexOf('_'));
            if (isInt(serviceProviderToCheck)) {
                hrLocation.append(addServiceProviderNameHr(tl, Integer.valueOf(serviceProviderToCheck)));
                String str3 = str2.substring(str2.indexOf('_') + 1);

                if (str3.startsWith(Tag.TSP_INFORMATION_URI.toString())) {
                    hrLocation.append(" || " + bundle.getString("serviceProvider.informationURI") + multiple);

                } else if (str3.startsWith(Tag.SERVICE_EXTENSION.toString())) {
                    hrLocation.append(" || " + bundle.getString("serviceProvider.extension") + multiple);

                } else if (str3.startsWith(Tag.ELECTRONIC_ADDRESS.toString())) {
                    hrLocation.append(" || " + bundle.getString("tlBrowser.electronicAddress") + multiple);

                } else if (str3.startsWith(Tag.POSTAL_ADDRESSES.toString())) {
                    hrLocation.append(" || " + bundle.getString("tlBrowser.postalAddress") + multiple);

                } else if (str3.startsWith(Tag.TSP_TRADE_NAME.toString())) {
                    hrLocation.append(" || " + bundle.getString("serviceProvider.tradeName") + multiple);

                } else if (str3.startsWith(Tag.TSP_NAME.toString())) {
                    hrLocation.append(" || " + bundle.getString("serviceProvider.name") + multiple);

                } else if (str3.startsWith(Tag.TSP_SERVICE.toString())) {
                    hrLocation.append(" || " + bundle.getString("serviceProvider.trustService"));
                    String str4 = getLocation(str3, '_', 2);
                    if (str4.contains("_")) {
                        String tSPServiceToCheck = str4.substring(0, str4.indexOf('_'));
                        if (isInt(tSPServiceToCheck)) {
                            hrLocation.append(addTSPServiceNameHr(tl, Integer.valueOf(serviceProviderToCheck), Integer.valueOf(tSPServiceToCheck)));
                            String str5 = str4.substring(str4.indexOf('_') + 1);
                            if (str5.startsWith(Tag.SERVICE_TYPE_IDENTIFIER.toString())) {
                                hrLocation.append(" || " + bundle.getString("serviceProvider.serviceInformation.type"));

                            } else if (str5.startsWith(Tag.SERVICE_NAME.toString())) {
                                hrLocation.append(" || " + bundle.getString("serviceProvider.name") + multiple);

                            } else if (str5.startsWith(Tag.SERVICE_STATUS_STARTING_TIME.toString())) {
                                hrLocation.append(" || " + bundle.getString("serviceProvider.serviceInformation.stateDate"));

                            } else if (str5.startsWith(Tag.SERVICE_STATUS.toString())) {
                                hrLocation.append(" || " + bundle.getString("serviceProvider.serviceInformation.currentState"));

                            } else if (str5.startsWith(Tag.SCHEME_SERVICE_DEFINITION_URI.toString())) {
                                hrLocation.append(" || " + bundle.getString("serviceProvider.schemeDef") + multiple);

                            } else if (str5.startsWith(Tag.SERVICE_SUPPLY_POINT.toString())) {
                                hrLocation.append(" || " + bundle.getString("serviceProvider.serviceSupply") + multiple);

                            } else if (str5.startsWith(Tag.TSP_SERVICE_DEFINITION_URI.toString())) {
                                hrLocation.append(" || " + bundle.getString("serviceProvider.tspSchemeDef") + multiple);

                            } else if (str5.startsWith(Tag.SERVICE_EXTENSION.toString())) {
                                hrLocation.append(" || " + bundle.getString("serviceProvider.extension") + multiple);
                                if (str5.contains(Tag.SERVICE_ADDITIONNAL_EXT.toString())) {
                                    hrLocation.append(" || " + bundle.getString("extension.additionalService"));
                                } else if (str5.contains(Tag.SERVICE_QUALIFICATION_EXT.toString())) {
                                    hrLocation.append(" || " + bundle.getString("extension.qualificationExtension"));
                                } else if (str5.contains(Tag.SERVICE_TAKEN_OVER_BY.toString())) {
                                    hrLocation.append(" || " + bundle.getString("extension.takenOverBy"));
                                } else if (str5.contains(Tag.SERVICE_EXPIRED_CERT_REVOCATION.toString())) {
                                    hrLocation.append(" || " + bundle.getString("extension.expiredCertRevocation"));
                                }

                            } else if (str5.startsWith(Tag.SERVICE_DIGITAL_IDENTITY.toString())) {
                                hrLocation.append(" || " + bundle.getString("serviceProvider.digitalIdentities"));
                                formatServiceDigitalIdentities(tl, hrLocation, str5, Integer.valueOf(serviceProviderToCheck), Integer.valueOf(tSPServiceToCheck));

                            } else if (str5.startsWith(Tag.SERVICE_HISTORY.toString())) {
                                hrLocation.append(" || " + bundle.getString("serviceProvider.history"));
                                String str6 = getLocation(str5, '_', 2);
                                if (str6.contains("_")) {
                                    String historyToCheck = str6.substring(0, str6.indexOf('_'));
                                    if (isInt(historyToCheck)) {
                                        hrLocation.append(addHistoryServiceNameHr(tl, Integer.valueOf(serviceProviderToCheck), Integer.valueOf(tSPServiceToCheck), Integer.valueOf(historyToCheck)));
                                        String str7 = str6.substring(str6.indexOf('_') + 1);
                                        if (str7.startsWith(Tag.SERVICE_TYPE_IDENTIFIER.toString())) {
                                            hrLocation.append(" || " + bundle.getString("serviceProvider.serviceInformation.type"));

                                        } else if (str7.startsWith(Tag.SERVICE_NAME.toString())) {
                                            hrLocation.append(" || " + bundle.getString("serviceProvider.name") + multiple);

                                        } else if (str7.startsWith(Tag.SERVICE_STATUS_STARTING_TIME.toString())) {
                                            hrLocation.append(" || " + bundle.getString("serviceProvider.serviceInformation.stateDate"));

                                        } else if (str7.startsWith(Tag.SERVICE_STATUS.toString())) {
                                            hrLocation.append(" || " + bundle.getString("serviceProvider.serviceInformation.currentState"));
                                        } else if (str7.startsWith(Tag.SERVICE_EXTENSION.toString())) {
                                            hrLocation.append(" || " + bundle.getString("serviceProvider.extension") + multiple);
                                            if (str7.contains(Tag.SERVICE_ADDITIONNAL_EXT.toString())) {
                                                hrLocation.append(" || " + bundle.getString("extension.additionalService"));
                                            } else if (str7.contains(Tag.SERVICE_QUALIFICATION_EXT.toString())) {
                                                hrLocation.append(" || " + bundle.getString("extension.qualificationExtension"));
                                            } else if (str7.contains(Tag.SERVICE_TAKEN_OVER_BY.toString())) {
                                                hrLocation.append(" || " + bundle.getString("extension.takenOverBy"));
                                            } else if (str7.contains(Tag.SERVICE_EXPIRED_CERT_REVOCATION.toString())) {
                                                hrLocation.append(" || " + bundle.getString("extension.expiredCertRevocation"));
                                            }
                                        } else if (str7.startsWith(Tag.SERVICE_DIGITAL_IDENTITY.toString())) {
                                            hrLocation.append(" || " + bundle.getString("serviceProvider.digitalIdentities"));
                                            formatServiceDigitalIdentities(tl, hrLocation, str7, Integer.valueOf(serviceProviderToCheck), Integer.valueOf(tSPServiceToCheck));
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        if (isInt(str4)) {
                            hrLocation.append(addTSPServiceNameHr(tl, Integer.valueOf(serviceProviderToCheck), Integer.valueOf(str4)));
                        }
                    }
                }
            }
        }

    }

    private static void formatServiceDigitalIdentities(TL tl, StringBuffer hrLocation, String strDigitalId, int serviceProvider, int tspServiceToCheck) {
        String di_str = getLocation(strDigitalId, '_', 3);
        if (di_str.contains("_")) {
            String digitalIdCheck = di_str.substring(0, di_str.indexOf('_'));
            if (isInt(digitalIdCheck) && pointerIsNotUndefined(tl, Integer.valueOf(digitalIdCheck))) {
                hrLocation.append(addServiceDigitalIdNameHr(tl, serviceProvider, tspServiceToCheck, Integer.valueOf(digitalIdCheck)));
            }
        }
    }

    public static String getDigitalIdList(String location) {
        String ret = null;
        if (location.contains("_")) {
            String p_str2 = getLocation(location, '_', 6);
            if (p_str2.contains("_") && p_str2.startsWith(Tag.SERVICE_DIGITAL_IDENTITY.toString())) {
                String di_str = getLocation(p_str2, '_', 3);
                if (di_str.contains("_")) {
                    String digitalIdCheck = di_str.substring(0, di_str.indexOf('_'));
                    ret = digitalIdCheck;
                }
            }
        }

        return ret;
    }

    public static String formatNotificationPointerId(TLPointersToOtherTSL pointer, String location) {
        StringBuffer hrLocation = new StringBuffer();

        if (location.contains("_")) {
            String p_str2 = getLocation(location, '_', 6);
            if (p_str2.contains("_")) {
                if (p_str2.startsWith(Tag.POINTER_TYPE.toString())) {
                    hrLocation.append(" || " + bundle.getString("tlType"));
                    //
                } else if (p_str2.startsWith(Tag.POINTER_LOCATION.toString())) {
                    if (pointer.getMimeType().equals(MimeType.XML)) {
                        hrLocation.append(" || " + bundle.getString("changes.url.xml"));
                    } else {
                        hrLocation.append(" || " + bundle.getString("changes.url.pdf"));
                    }
                } else if (p_str2.startsWith(Tag.SCHEME_OPERATOR_NAME.toString())) {
                    hrLocation.append(" || " + bundle.getString("schemeOpeName") + multiple);

                } else if (p_str2.startsWith(Tag.SCHEME_TYPE_COMMUNITY_RULES.toString())) {
                    hrLocation.append(" || " + bundle.getString("schemeInfo.communityRule") + multiple);

                } else if (p_str2.startsWith(Tag.POINTER_SCHEME_TERRITORY.toString())) {
                    hrLocation.append(" || " + bundle.getString("schemeTerritory"));

                } else if (p_str2.startsWith(Tag.POINTER_MIME_TYPE.toString())) {
                    hrLocation.append(" || " + bundle.getString("mimeType"));

                } else if (p_str2.startsWith(Tag.SERVICE_DIGITAL_IDENTITY.toString())) {
                    hrLocation.append(" || " + bundle.getString("schemeInfo.digitalIdentities"));
                    String di_str = getLocation(p_str2, '_', 3);
                    if (di_str.contains("_")) {
                        String digitalIdCheckTmp = di_str.substring(0, di_str.indexOf('_'));
                        int digitalIdCheck = Integer.parseInt(digitalIdCheckTmp) - 1;
                        hrLocation.append(" || " + pointer.getServiceDigitalId().get(digitalIdCheck).getCertificateList().get(0).getCertSubjectShortName() + " || "
                                + TLUtils.toStringFormat(pointer.getServiceDigitalId().get(digitalIdCheck).getCertificateList().get(0).getCertNotBefore()) + " - "
                                + TLUtils.toStringFormat(pointer.getServiceDigitalId().get(digitalIdCheck).getCertificateList().get(0).getCertAfter()));
                    }
                } else if (p_str2.startsWith(Tag.POINTERS_TO_OTHER_TSL.toString())) {
                    hrLocation.append(" || " + bundle.getString("pdf.pointer"));
                }
            } else {
                if (location.equals(Tag.NOTIFICATION + "_" + Tag.SERVICE_DIGITAL_IDENTITY)) {
                    hrLocation.append(" || " + bundle.getString("schemeInfo.digitalIdentities"));
                } else if (location.equals(Tag.NOTIFICATION + "_" + Tag.CONTACT + "_" + Tag.ELECTRONIC_ADDRESS)) {
                    hrLocation.append(" || " + bundle.getString("tlBrowser.electronicAddress"));
                } else if (location.equals(Tag.NOTIFICATION + "_" + Tag.CONTACT_NAME)) {
                    hrLocation.append(" || " + bundle.getString("tNotification.schemeContact.name"));
                } else if (location.equals(Tag.NOTIFICATION + "_" + Tag.CONTACT_ADDRESS)) {
                    hrLocation.append(" || " + bundle.getString("tNotification.schemeContact.postalAddress"));
                } else if (location.equals(Tag.NOTIFICATION + "_" + Tag.CONTACT_PHONE)) {
                    hrLocation.append(" || " + bundle.getString("tNotification.schemeContact.electronicAddress"));
                }
            }
        }
        return hrLocation.toString();

    }

    private static String addTSPServiceNameHr(TL tl, int serviceProviderToCheck, int tSPServiceToCheck) {
        if (CollectionUtils.isNotEmpty(tl.getServiceProviders().get(serviceProviderToCheck - 1).getTSPServices())) {
            if (tl.getServiceProviders().get(serviceProviderToCheck - 1).getTSPServices().get(tSPServiceToCheck - 1) != null) {
                if (CollectionUtils.isNotEmpty(tl.getServiceProviders().get(serviceProviderToCheck - 1).getTSPServices().get(tSPServiceToCheck - 1).getServiceName())) {
                    return " : " + tl.getServiceProviders().get(serviceProviderToCheck - 1).getTSPServices().get(tSPServiceToCheck - 1).getServiceName().get(0).getValue();
                }
            }

        }
        return "";
    }

    private static String addHistoryServiceNameHr(TL tl, int serviceProviderToCheck, int tSPServiceToCheck, int historyToCheck) {
        if (CollectionUtils.isNotEmpty(tl.getServiceProviders().get(serviceProviderToCheck - 1).getTSPServices())) {
            if (tl.getServiceProviders().get(serviceProviderToCheck - 1).getTSPServices().get(tSPServiceToCheck - 1) != null) {
                if (tl.getServiceProviders().get(serviceProviderToCheck - 1).getTSPServices().get(tSPServiceToCheck - 1).getHistory().get(historyToCheck - 1) != null) {
                    if (CollectionUtils
                            .isNotEmpty(tl.getServiceProviders().get(serviceProviderToCheck - 1).getTSPServices().get(tSPServiceToCheck - 1).getHistory().get(historyToCheck - 1).getServiceName())) {
                        String date = "";
                        if (tl.getServiceProviders().get(serviceProviderToCheck - 1).getTSPServices().get(tSPServiceToCheck - 1).getHistory().get(historyToCheck - 1)
                                .getCurrentStatusStartingDate() != null) {
                            date = " - " + TLUtils.toDateFormatYMD(tl.getServiceProviders().get(serviceProviderToCheck - 1).getTSPServices().get(tSPServiceToCheck - 1).getHistory()
                                    .get(historyToCheck - 1).getCurrentStatusStartingDate());
                        }
                        return " : " + tl.getServiceProviders().get(serviceProviderToCheck - 1).getTSPServices().get(tSPServiceToCheck - 1).getHistory().get(historyToCheck - 1).getServiceName().get(0)
                                .getValue() + date;
                    }
                }
            }

        }
        return "";
    }

    private static String startHr(TL tl) {
        if (tl.getDbName() != null) {
            return tl.getDbName() + "(" + tl.getSchemeInformation().getSequenceNumber() + ")";
        } else {
            return tl.getSchemeInformation().getTerritory() + "(" + tl.getSchemeInformation().getSequenceNumber() + ")";
        }

    }

    private static boolean isInt(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static String addPointerNameHr(TL myTl, int pointerToCheck) {
        if (myTl.getPointers().get(pointerToCheck - 1).getMimeType() != null) {
            return " : " + myTl.getPointers().get(pointerToCheck - 1).getSchemeTerritory() + "(" + myTl.getPointers().get(pointerToCheck - 1).getMimeType().toString() + ")";
        } else {
            return " : " + myTl.getPointers().get(pointerToCheck - 1).getSchemeTerritory() + "(" + bundle.getString("changes.noMimeType") + ")";
        }

    }

    private static String addPointerDigitalIdNameHr(TL myTl, int pointerToCheck, int pointerDigitalToCheck) {
        if ((myTl.getPointers().get(pointerToCheck - 1).getServiceDigitalId().get(pointerDigitalToCheck - 1).getCertificateList() != null)
                && (myTl.getPointers().get(pointerToCheck - 1).getServiceDigitalId().get(pointerDigitalToCheck - 1).getCertificateList().get(0) != null)) {
            return " : " + myTl.getPointers().get(pointerToCheck - 1).getServiceDigitalId().get(pointerDigitalToCheck - 1).getCertificateList().get(0).getCertSubjectShortName() + " || "
                    + TLUtils.toStringFormat(myTl.getPointers().get(pointerToCheck - 1).getServiceDigitalId().get(pointerDigitalToCheck - 1).getCertificateList().get(0).getCertNotBefore()) + " - "
                    + TLUtils.toStringFormat(myTl.getPointers().get(pointerToCheck - 1).getServiceDigitalId().get(pointerDigitalToCheck - 1).getCertificateList().get(0).getCertAfter());
        } else {
            if (myTl.getPointers().get(pointerToCheck - 1).getServiceDigitalId().get(pointerDigitalToCheck - 1).getSubjectName() != null) {
                return " : " + myTl.getPointers().get(pointerToCheck - 1).getServiceDigitalId().get(pointerDigitalToCheck - 1).getSubjectName();
            } else if (myTl.getPointers().get(pointerToCheck - 1).getServiceDigitalId().get(pointerDigitalToCheck - 1).getX509ski() != null) {
                return " : " + myTl.getPointers().get(pointerToCheck - 1).getServiceDigitalId().get(pointerDigitalToCheck - 1).getX509ski().toString();
            } else {
                return " : undefined";
            }
        }
    }

    private static boolean pointerIsNotUndefined(TL tl, int pointerToCheck) {
        if (tl.getPointers().size() >= pointerToCheck) {
            return tl.getPointers().get(pointerToCheck - 1) != null;
        } else {
            return false;
        }

    }

    private static boolean pointerDigitalIsNotUndefined(TL tl, int pointerToCheck, int pointerDigitalToCheck) {
        if (tl.getPointers().size() >= pointerToCheck) {
            if (tl.getPointers().get(pointerToCheck - 1).getServiceDigitalId().size() >= pointerDigitalToCheck) {
                return tl.getPointers().get(pointerToCheck - 1).getServiceDigitalId().get(pointerDigitalToCheck - 1) != null;
            }
        } else {
            return false;
        }
        return false;

    }

    private static String getLocation(String text, char character, int occurs) {
        String result = text;
        for (int i = 0; i < occurs; i++) {
            result = result.substring(result.indexOf(character) + 1);
        }
        return result;
    }

    private static String addServiceProviderNameHr(TL tl, int serviceProviderToCheck) {
        if (CollectionUtils.isNotEmpty(tl.getServiceProviders().get(serviceProviderToCheck - 1).getTSPName())) {
            return " : " + tl.getServiceProviders().get(serviceProviderToCheck - 1).getTSPName().get(0).getValue();
        }
        return "";
    }

    private static String addServiceDigitalIdNameHr(TL tl, int serviceProvider, int tspServiceToCheck, int digitalToCheck) {
        String ret = "";
        if ((tl.getServiceProviders().get(serviceProvider - 1).getTSPServices().get(tspServiceToCheck - 1).getDigitalIdentification() != null)
                && (tl.getServiceProviders().get(serviceProvider - 1).getTSPServices().get(tspServiceToCheck - 1).getDigitalIdentification().get(digitalToCheck - 1).getCertificateList() != null)) {
            ret = " : "
                    + tl.getServiceProviders().get(serviceProvider - 1).getTSPServices().get(tspServiceToCheck - 1).getDigitalIdentification().get(digitalToCheck - 1).getCertificateList().get(0)
                            .getCertSubjectShortName()
                    + " || "
                    + TLUtils.toStringFormat(tl.getServiceProviders().get(serviceProvider - 1).getTSPServices().get(tspServiceToCheck - 1).getDigitalIdentification().get(digitalToCheck - 1)
                            .getCertificateList().get(0).getCertNotBefore())
                    + " - " + TLUtils.toStringFormat(tl.getServiceProviders().get(serviceProvider - 1).getTSPServices().get(tspServiceToCheck - 1).getDigitalIdentification().get(digitalToCheck - 1)
                            .getCertificateList().get(0).getCertAfter());
        } else {
            ret = " : " + tl.getServiceProviders().get(serviceProvider - 1).getTSPServices().get(tspServiceToCheck - 1).getDigitalIdentification().get(digitalToCheck - 1).getSubjectName();
        }
        return ret;
    }
}
