package eu.europa.ec.joinup.tsl.business.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;

public class LanguageServiceTest extends AbstractSpringTest {

    @Autowired
    private LanguageService languageService;

    @Test
    public void getAllowedLanguages() {
        List<String> allowedLanguages = languageService.getAllowedLanguages();
        assertTrue(CollectionUtils.isNotEmpty(allowedLanguages));
        assertTrue(allowedLanguages.contains(LanguageService.ENGLISH_CODE));
        assertFalse(allowedLanguages.contains("xx"));
    }

}
