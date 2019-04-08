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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import eu.europa.ec.joinup.tsl.business.constant.TLCCTarget;
import eu.europa.ec.joinup.tsl.business.dto.CheckResultDTO;
import eu.europa.ec.joinup.tsl.business.dto.TLCCRequestDTO;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.TLCCResults;
import eu.europa.ec.joinup.tsl.business.util.LocationUtils;
import eu.europa.ec.joinup.tsl.business.util.TLCCMarshallerUtils;
import eu.europa.ec.joinup.tsl.business.util.TLCCParserUtils;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.CheckStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;

/**
 * TLCC call management
 */
@Service
@Transactional
public class TLCCService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TLCCService.class);

    private static ResourceBundle bundle = ResourceBundle.getBundle("messages");

    @Autowired
    private TLService tlService;

    @Autowired
    private ApplicationPropertyService applicationPropertyService;

    @Value("${tlcc.url}")
    private String tlccUrl;

    @Value("${etsi.portal.url}")
    private String etsiPortalUrl;

    @Value("${tsl.folder}")
    private String folderPath;

    @Value("${tlcc.active}")
    private boolean tlccActive;

    /**
     * Trigger TLCC and get errors result
     *
     * @param requestDTO
     * @param tlccTarget
     * @exception tlNull
     *                Trusted list is null
     * @exception targetNull
     *                TLCC target is undefined
     */
    public List<CheckResultDTO> getErrorTlccChecks(TLCCRequestDTO requestDTO, TLCCTarget tlccTarget) {
        List<CheckResultDTO> tlccErrorDto = new ArrayList<>();

        DBTrustedLists dbTL = tlService.getDbTL(requestDTO.getTlId());
        if (dbTL == null) {
            LOGGER.error("Trusted list is null. Id : " + requestDTO.getTlId());
            throw new IllegalStateException(bundle.getString("error.tl.not.found"));
        } else if (dbTL.getType().equals(TLType.LOTL) && !applicationPropertyService.isCheckLOTLEnabled()) {
            LOGGER.debug("Checks on LOTL are disabled");
            return tlccErrorDto;
        } else {
            if (tlccTarget == null) {
                LOGGER.error("TLCC check target is null");
                throw new IllegalStateException(bundle.getString("tlcc.target.null"));
            }
            List<CheckResultDTO> tlccDto = postTLCCRequest(requestDTO, tlccTarget);
            if (tlccDto == null) {
                return null;
            } else {
                for (CheckResultDTO tlcc : tlccDto) {
                    if (!tlcc.getStatus().equals(CheckStatus.SUCCESS)) {
                        tlccErrorDto.add(tlcc);
                    }
                }
            }
            return tlccErrorDto;
        }
    }

    /**
     * Post TLCC request, parse XML result and return all check result
     *
     * @param requestDTO
     * @param tlccTarget
     */
    private List<CheckResultDTO> postTLCCRequest(TLCCRequestDTO requestDTO, TLCCTarget tlccTarget) {
        if (!tlccActive) {
            return Collections.emptyList();
        }
        DBTrustedLists dbTl = tlService.getDbTL(requestDTO.getTlId());
        if (dbTl.getXmlFile() != null) {
            TL tl = tlService.getDtoTL(dbTl);
            DBFiles xmlFile = dbTl.getXmlFile();
            String xmlPath = xmlFile.getLocalPath();
            LOGGER.debug("xmlPath : " + xmlPath);
            if (xmlPath != null) {
                RestTemplate restTemplate = new RestTemplate();

                String requestUrl = tlccUrl + "/" + tlccTarget.toString();

                DBTrustedLists lotl = tlService.getLOTL();
                if ((lotl == null) || (lotl.getXmlFile() == null)) {
                    LOGGER.debug("Get TLCC Results : LOTL or lotl file is null");
                    return null;
                }
                requestDTO.setLotlPath(folderPath + File.separator + lotl.getXmlFile().getLocalPath());
                requestDTO.setTlId(requestDTO.getTlId());
                requestDTO.setTlXmlPath(folderPath + File.separator + xmlPath);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<TLCCRequestDTO> entity = new HttpEntity<>(requestDTO, headers);
                try {
                    restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
                    String answer = restTemplate.postForObject(requestUrl, entity, String.class);
                    try {
                        TLCCResults tlccResults = TLCCMarshallerUtils.unmarshallTlccXml(answer);
                        List<CheckResultDTO> tlccDto = TLCCParserUtils.parseAllTLCC(tlccResults, Integer.toString(requestDTO.getTlId()));
                        for (CheckResultDTO tlcc : tlccDto) {
                            tlcc.setLocation(LocationUtils.idUserReadable(tl, tlcc.getId()));
                        }
                        return tlccDto;
                    } catch (Exception e) {
                        LOGGER.error("Marshall TLCC results error", e);
                        return null;
                    }
                } catch (ResourceAccessException | HttpClientErrorException e) {
                    LOGGER.error("REST TEMPLATE HttpClientErrorException", e);
                    return null;
                }
            }
        }
        LOGGER.error("TL XML file is null.");
        return null;
    }

    public String getEtsiPortalUrl() {
        return etsiPortalUrl;
    }

}
