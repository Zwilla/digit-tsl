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

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.tl.TLGeneric;
import eu.europa.ec.joinup.tsl.business.service.LanguageService;

@Service
public class LanguageValidator {

    @Autowired
    private GenericValidator genericValidator;

    @Autowired
    private LanguageService languageService;

    public boolean isLanguageLowerCase(TLGeneric tlGeneric) {
        if (tlGeneric != null) {
            return genericValidator.isLowerCase(tlGeneric.getLanguage());
        }
        return false;
    }

    public boolean isLanguagePresent(TLGeneric tlGeneric) {
        if (tlGeneric != null) {
            return genericValidator.isNotEmpty(tlGeneric.getLanguage());
        }
        return false;
    }

    public boolean isLanguagesContainsEnglish(List<? extends TLGeneric> tlGenerics) {
        if (CollectionUtils.isNotEmpty(tlGenerics)) {
            for (TLGeneric tlGeneric : tlGenerics) {
                if (StringUtils.equals(LanguageService.ENGLISH_CODE, tlGeneric.getLanguage())) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public boolean isAllowedLanguage(TLGeneric tlGeneric) {
        if (tlGeneric != null) {
            return isAllowedLanguage(tlGeneric.getLanguage());
        }
        return false;
    }

    public boolean isAllowedLanguage(String language) {
        if (StringUtils.isNotEmpty(language)) {
            List<String> allowedLanguages = languageService.getAllowedLanguages();
            return allowedLanguages.contains(language);
        }
        return false;
    }

}
