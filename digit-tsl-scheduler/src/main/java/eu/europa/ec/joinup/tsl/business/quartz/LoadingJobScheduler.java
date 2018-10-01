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

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.business.service.LoadingJobService;

@Service
public class LoadingJobScheduler {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoadingJobScheduler.class);

	@Value("${load.lotl.on.startup:false}")
	private boolean loadLotlOnStartup = false;

	@Autowired
	private TLRepository tlRepository;

	@Autowired
	private LoadingJobService loadingJobService;

	@PostConstruct
	public void launchIfEmpty() {
		if (loadLotlOnStartup && (tlRepository.count() == 0)) {
			loadLOTL();
		}
	}

	@Scheduled(cron = "${cron.loading.job}")
	public void loadLOTL() {
		LOGGER.debug("LOAD LOTL CRON JOBS --> loadingJobService.start()");
		loadingJobService.start();
	}



}
