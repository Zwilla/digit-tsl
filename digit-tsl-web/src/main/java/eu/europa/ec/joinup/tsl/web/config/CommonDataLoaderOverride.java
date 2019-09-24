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
package eu.europa.ec.joinup.tsl.web.config;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.esig.dss.DSSException;
import eu.europa.esig.dss.client.http.commons.CommonsDataLoader;
import eu.europa.esig.dss.utils.Utils;

public class CommonDataLoaderOverride extends CommonsDataLoader {

    private static final Logger LOG = LoggerFactory.getLogger(CommonDataLoaderOverride.class);

    private static final long serialVersionUID = 1L;

    /**
     * This method retrieves data using HTTP or HTTPS protocol and 'get' method.
     *
     * @param url
     *            to access
     * @return {@code byte} array of obtained data or null
     */
    @Override
    protected byte[] httpGet(final String url) {
        HttpGet httpRequest = null;
        CloseableHttpResponse httpResponse = null;
        CloseableHttpClient client = null;
        try {

            final URI uri = new URI(url.trim());
            httpRequest = new HttpGet(uri);
            if (contentType != null) {
                httpRequest.setHeader(CONTENT_TYPE, contentType);
                httpRequest.addHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
            }

            client = getHttpClient(url);
            httpResponse = getHttpResponse(client, httpRequest);

            return readHttpResponse(httpResponse);

        } catch (URISyntaxException | IOException e) {
            throw new DSSException("Unable to process GET call for url '" + url + "'", e);
        } finally {
            try {
                if (httpRequest != null) {
                    httpRequest.releaseConnection();
                }
                if (httpResponse != null) {
                    EntityUtils.consumeQuietly(httpResponse.getEntity());
                    Utils.closeQuietly(httpResponse);
                }
            } finally {
                Utils.closeQuietly(client);
            }
        }
    }

    void closeClient(CloseableHttpClient httpClient) {
        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (Exception ex) {
                LOG.warn("Cound not close client", ex);
            } finally {
                httpClient = null;
            }
        }
    }
}
