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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.service.LoadingJobService;
import eu.europa.ec.joinup.tsl.business.service.RetentionJobService;
import eu.europa.ec.joinup.tsl.business.service.RulesValidationJobService;
import eu.europa.ec.joinup.tsl.business.service.SignatureAlertingJobService;
import eu.europa.ec.joinup.tsl.business.service.SignatureValidationJobService;
import eu.europa.ec.joinup.tsl.business.service.TLBreakAlertingJobService;

/**
 * Run different jobs in asynchronous way
 */
@Service
public class ASyncJobs {

    @Autowired
    private LoadingJobService loadingJobService;

    @Autowired
    private RulesValidationJobService rulesValidationJobService;

    @Autowired
    private SignatureValidationJobService signatureValidationJobService;

    @Autowired
    private RetentionJobService retentionJobService;

    @Autowired
    private SignatureAlertingJobService signatureAlertJobService;

    @Autowired
    private TLBreakAlertingJobService tlBreakAlertJobService;

    @Async
    public void launchLoading() {
        loadingJobService.start();
    }

    @Async
    public void launchRulesValidation() {
        rulesValidationJobService.start();
    }

    @Async
    public void launchSignatureValidation() {
        signatureValidationJobService.start();
    }

    @Async
    public void launchRetentionPolicy() {
        retentionJobService.start(true);
    }

    @Async
    public void launchSignatureAlert() {
        signatureAlertJobService.start();
    }

    @Async
    public void launchApproachBreakAlert() {
        tlBreakAlertJobService.start();
    }
}
