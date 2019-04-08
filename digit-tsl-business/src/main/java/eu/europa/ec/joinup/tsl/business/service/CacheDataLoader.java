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

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import eu.europa.esig.dss.client.http.DataLoader;

/**
 * Cache data loader service
 */
@Service
public class CacheDataLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheDataLoader.class);

    @Autowired
    private DataLoader dataLoader;

    @Cacheable(value = "externalResourcesCache", key = "{#root.methodName, #uri}")
    public boolean isAccessibleUri(String uri) {
        boolean result = false;
        if (StringUtils.isNotEmpty(uri)) {
            try {
                byte[] bs = dataLoader.get(uri);
                result = ArrayUtils.isNotEmpty(bs);
            } catch (Exception e) {
                LOGGER.warn("Unable to access to '" + uri + "' : " + e.getMessage());
            }
        }
        return result;
    }

}
