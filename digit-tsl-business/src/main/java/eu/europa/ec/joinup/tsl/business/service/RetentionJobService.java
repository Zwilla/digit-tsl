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

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.util.DateUtils;
import eu.europa.ec.joinup.tsl.model.enums.AuditAction;
import eu.europa.ec.joinup.tsl.model.enums.AuditStatus;
import eu.europa.ec.joinup.tsl.model.enums.AuditTarget;

/**
 * Job for retention policy clean
 */
@Service
public class RetentionJobService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetentionJobService.class);

    @Autowired
    private AuditService auditService;

    @Autowired
    private RetentionService retentionService;

    @Autowired
    private ApplicationPropertyService applicationPropertyService;

    /**
     * Run retention job service
     *
     * @param overdo
     *            : true to bypass the application property verification
     */
    public void start(Boolean overdo) {
        if (overdo || applicationPropertyService.runRetentionPolicy()) {
            LOGGER.debug("**** START RETENTION JOB " + DateUtils.getToFormatYMDH(new Date()) + "****");
            auditService.addAuditLog(AuditTarget.JOBS, AuditAction.RETENTION, AuditStatus.SUCCES, "", 0, "SYSTEM", "Start retention job");
            retentionService.retentionClean();
            LOGGER.debug("**** END RETENTION JOB " + DateUtils.getToFormatYMDH(new Date()) + "****");
            auditService.addAuditLog(AuditTarget.JOBS, AuditAction.RETENTION, AuditStatus.SUCCES, "", 0, "SYSTEM", "End retention job");
        }
    }
}
