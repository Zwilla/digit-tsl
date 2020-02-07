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
