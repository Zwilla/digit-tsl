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
package eu.europa.ec.joinup.tsl.business.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import eu.europa.ec.joinup.tsl.business.repository.SignatureInformationRepository;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBSignatureInformation;
import eu.europa.ec.joinup.tsl.model.enums.SignatureStatus;
import eu.europa.esig.dss.SignatureLevel;
import eu.europa.esig.dss.validation.policy.rules.Indication;
import eu.europa.esig.dss.validation.policy.rules.SubIndication;
import eu.europa.esig.dss.validation.reports.Reports;
import eu.europa.esig.dss.validation.reports.SimpleReport;
import eu.europa.esig.dss.validation.reports.wrapper.CertificateWrapper;
import eu.europa.esig.dss.validation.reports.wrapper.DiagnosticData;
import eu.europa.esig.dss.validation.reports.wrapper.SignatureWrapper;
import eu.europa.esig.dss.x509.CertificateToken;
import eu.europa.esig.dss.x509.CommonTrustedCertificateSource;
import eu.europa.esig.dss.x509.KeyStoreCertificateSource;

@Transactional(value = TxType.REQUIRED)
public abstract class AbstractSignatureValidationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSignatureValidationService.class);

    @Autowired
    @Qualifier("lotlKeyStore")
    private KeyStoreCertificateSource lotlKeyStore;

    @Autowired
    private FileService fileService;

    @Autowired
    private SignatureInformationRepository sigInfoRepository;

    public DBSignatureInformation validateNotificationTmpFile(File file, List<CertificateToken> potentialSigners) {
        DBSignatureInformation signature = new DBSignatureInformation();
        validateFile(potentialSigners, signature, file);
        return signature;
    }

    public DBSignatureInformation validateLOTL(DBFiles file) {
        List<CertificateToken> potentialSigners = new ArrayList<>();
        if ((lotlKeyStore != null) && CollectionUtils.isNotEmpty(lotlKeyStore.getCertificates())) {
            potentialSigners = lotlKeyStore.getCertificates();
        }
        return validateTL(file, potentialSigners);
    }

    public DBSignatureInformation validateNotification(DBFiles file, List<CertificateToken> potentialSigners) {
        return validateDBFiles(file, potentialSigners);
    }

    public DBSignatureInformation validateTL(DBFiles file, List<CertificateToken> potentialSigners) {
        return validateDBFiles(file, potentialSigners);
    }

    private DBSignatureInformation validateDBFiles(DBFiles file, List<CertificateToken> potentialSigners) {
        DBSignatureInformation signatureInfo = file.getSignatureInformation();
        if (signatureInfo == null) {
            signatureInfo = new DBSignatureInformation();
            signatureInfo.setFile(file);
        }
        File tslFile = fileService.getTSLFile(file);
        validateFile(potentialSigners, signatureInfo, tslFile);
        return sigInfoRepository.save(signatureInfo);
    }

    private void validateFile(List<CertificateToken> potentialSigners, DBSignatureInformation signatureInfo, File file) {
        if ((file == null) || !file.exists()) {
            signatureInfo.setIndication(SignatureStatus.FILE_NOT_FOUND);
            cleanSignatureInfo(signatureInfo);
        }
        try {
            Reports reports = getReports(file, potentialSigners);
            SimpleReport simpleReport = reports.getSimpleReport();
            List<String> signatureIdList = simpleReport.getSignatureIdList();
            if (CollectionUtils.isEmpty(signatureIdList)) {
                cleanSignatureInfo(signatureInfo);
                signatureInfo.setIndication(SignatureStatus.NOT_SIGNED);
            } else if (CollectionUtils.size(signatureIdList) > 1) {
                cleanSignatureInfo(signatureInfo);
                signatureInfo.setIndication(SignatureStatus.MORE_THAN_ONE_SIGNATURE);
            } else {
                DiagnosticData diagnosticData = reports.getDiagnosticData();
                SignatureWrapper signatureWrapper = diagnosticData.getSignatureById(diagnosticData.getFirstSignatureId());
                String signatureId = simpleReport.getFirstSignatureId();
                Indication indication = simpleReport.getIndication(signatureId);
                SignatureStatus status = null;
                switch (indication) {
                case TOTAL_PASSED:
                    status = SignatureStatus.VALID;
                    break;
                case INDETERMINATE:
                    status = SignatureStatus.INDETERMINATE;
                    break;
                case TOTAL_FAILED:
                    status = SignatureStatus.INVALID;
                    break;
                default:
                    status = SignatureStatus.CANNOT_BE_VALIDATED;
                    break;
                }

                // VALID, INDETERMINATE or INVALID
                signatureInfo.setIndication(status);
                String signatureFormat = simpleReport.getSignatureFormat(signatureId);
                if (SignatureStatus.VALID.equals(status) && !isBaselineB(signatureFormat)) {
                    signatureInfo.setIndication(SignatureStatus.NOT_BASELINE_B);
                }

                SubIndication subIndication = simpleReport.getSubIndication(signatureId);
                if (subIndication != null) {
                    signatureInfo.setSubIndication(subIndication.name());
                }
                signatureInfo.setSignedBy(simpleReport.getSignedBy(signatureId));

                signatureInfo.setSigningDate(simpleReport.getSigningTime(signatureId));

                signatureInfo.setDigestAlgo(diagnosticData.getFirstSignatureDigestAlgorithm().toString());
                signatureInfo.setEncryptionAlgo(diagnosticData.getFirstSignatureEncryptionAlgorithm().toString());

                String keyLengthUsedToSignThisToken = signatureWrapper.getKeyLengthUsedToSignThisToken();

                if (StringUtils.isNotEmpty(keyLengthUsedToSignThisToken) && NumberUtils.isDigits(keyLengthUsedToSignThisToken)) {
                    signatureInfo.setKeyLength(Integer.parseInt(keyLengthUsedToSignThisToken));
                } else {
                    signatureInfo.setKeyLength(0);
                }

                String signingCertificateId = signatureWrapper.getSigningCertificateId();
                CertificateWrapper certificateWrapper = diagnosticData.getUsedCertificateById(signingCertificateId);

                if (certificateWrapper != null) {
                    signatureInfo.setSignedByNotBefore(certificateWrapper.getNotBefore());
                    signatureInfo.setSignedByNotAfter(certificateWrapper.getNotAfter());
                }

            }
        } catch (Exception e) {
            LOGGER.error("Unable to validate file " + file.getName() + " : " + e.getMessage(), e);
            cleanSignatureInfo(signatureInfo);
            signatureInfo.setIndication(SignatureStatus.CANNOT_BE_VALIDATED);
        }
    }

    private void cleanSignatureInfo(DBSignatureInformation signatureInfo) {
        signatureInfo.setSignatureFormat("");
        signatureInfo.setSubIndication("");
        signatureInfo.setSignedBy("");
        signatureInfo.setSigningDate(null);
        signatureInfo.setDigestAlgo("");
        signatureInfo.setEncryptionAlgo("");
        signatureInfo.setKeyLength(0);
        signatureInfo.setSignedByNotBefore(null);
        signatureInfo.setSignedByNotAfter(null);
    }

    private boolean isBaselineB(String signatureLevel) {
        try {
            SignatureLevel level = SignatureLevel.valueByName(signatureLevel);
            return getBaselineB().equals(level);
        } catch (Exception e) {
            return false;
        }
    }

    protected CommonTrustedCertificateSource buildTrustedCertificateSource(List<CertificateToken> potentialSigners) {
        CommonTrustedCertificateSource commonTrustedCertificateSource = new CommonTrustedCertificateSource();
        if (CollectionUtils.isNotEmpty(potentialSigners)) {
            for (CertificateToken potentialSigner : potentialSigners) {
                commonTrustedCertificateSource.addCertificate(potentialSigner);
            }
        }
        return commonTrustedCertificateSource;
    }

    protected abstract SignatureLevel getBaselineB();

    public abstract Reports getReports(File tslFile, List<CertificateToken> potentialSigners);

}
