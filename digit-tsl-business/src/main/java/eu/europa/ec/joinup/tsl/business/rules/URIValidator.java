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
package eu.europa.ec.joinup.tsl.business.rules;

import java.util.concurrent.Future;

import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.service.CacheDataLoader;

@Service
public class URIValidator extends GenericValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(URIValidator.class);

    @Autowired
    private CacheDataLoader cacheDataLoader;

    @Cacheable(value = "externalResourcesCache", key = "#url")
    public boolean isCorrectUrl(String url) {
        String[] schemes = {
                "http", "https"
        }; // DEFAULT schemes = "http", "https", "ftp"
        UrlValidator urlValidator = new UrlValidator(schemes);
        boolean valid = urlValidator.isValid(url);
        if (!valid) {
            LOGGER.debug("Not valid URL detected '" + url + "'");
        }
        return valid;
    }

    @Async
    public Future<Boolean> isAccessibleUri(String uri) {
        return new AsyncResult<>(cacheDataLoader.isAccessibleUri(uri));
    }

    public boolean isSecureURI(String uri) {
        if (uri.toLowerCase().startsWith("https")) {
            return true;
        } else {
            return false;
        }
    }

}
