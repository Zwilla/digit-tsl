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

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;

/**
 * PrettyPrint service
 */
@Service
@Transactional
public class TLPrettyPrintService {

    @Autowired
    private TLService tlService;

    @Value("${pretty.print.url}")
    private String prettyPrintUrl;

    /**
     * Post prettyPrint request and return PDF result
     *
     * @param tlId
     * @return
     */
    public String triggerPrettyPrint(int tlId) {
        DBTrustedLists tl = tlService.getDbTL(tlId);
        if (tl.getXmlFile() != null) {
            DBFiles xmlFile = tl.getXmlFile();
            String xmlPath = xmlFile.getLocalPath();
            if (xmlPath != null) {
                RestTemplate restTemplate = new RestTemplate();

                String url = prettyPrintUrl;
                String requestJson = xmlPath;
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
                String answer = restTemplate.postForObject(url, entity, String.class);

                if (answer != null) {
                    return answer;
                }
            }
        }
        return null;
    }

}
