package eu.europa.ec.joinup.tsl.business.rules;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLGeneric;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLName;

public class LanguageValidatorTest extends AbstractSpringTest {

    @Autowired
    private LanguageValidator languageValidator;

    @Test
    public void isLanguagePresent() {
        TLGeneric tlGeneric = null;
        assertFalse(languageValidator.isLanguagePresent(null));

        tlGeneric = new TLName();
        assertFalse(languageValidator.isLanguagePresent(tlGeneric));

        tlGeneric.setLanguage("");
        assertFalse(languageValidator.isLanguagePresent(tlGeneric));

        tlGeneric.setLanguage("bla");
        assertTrue(languageValidator.isLanguagePresent(tlGeneric));
    }

    @Test
    public void isLanguageLowerCase() {
        TLGeneric tlGeneric = null;
        assertFalse(languageValidator.isLanguageLowerCase(null));

        tlGeneric = new TLName();
        assertFalse(languageValidator.isLanguageLowerCase(tlGeneric));

        tlGeneric.setLanguage("");
        assertFalse(languageValidator.isLanguageLowerCase(tlGeneric));

        tlGeneric.setLanguage("bla");
        assertTrue(languageValidator.isLanguageLowerCase(tlGeneric));

        tlGeneric.setLanguage("BLA");
        assertFalse(languageValidator.isLanguageLowerCase(tlGeneric));
    }

    @Test
    public void isAllowedLanguage() {

        TLGeneric tlGeneric = null;
        assertFalse(languageValidator.isAllowedLanguage((TLGeneric) null));

        tlGeneric = new TLName();
        assertFalse(languageValidator.isAllowedLanguage(tlGeneric));

        tlGeneric.setLanguage("");
        assertFalse(languageValidator.isAllowedLanguage(tlGeneric));

        tlGeneric.setLanguage("bla");
        assertFalse(languageValidator.isAllowedLanguage(tlGeneric));

        tlGeneric.setLanguage("BLA");
        assertFalse(languageValidator.isAllowedLanguage(tlGeneric));

        tlGeneric.setLanguage("fr");
        assertTrue(languageValidator.isAllowedLanguage(tlGeneric));
    }

    @Test
    public void isLanguagesContainsEnglish() {

        List<TLGeneric> generics = null;
        assertTrue(languageValidator.isLanguagesContainsEnglish(null));

        generics = new ArrayList<>();
        assertTrue(languageValidator.isLanguagesContainsEnglish(generics));

        generics.add(new TLGeneric() {
        });
        assertFalse(languageValidator.isLanguagesContainsEnglish(generics));

        TLGeneric fr = new TLName();
        fr.setLanguage("fr");
        generics.add(fr);
        assertFalse(languageValidator.isLanguagesContainsEnglish(generics));

        TLGeneric en = new TLName();
        fr.setLanguage("en");
        generics.add(en);
        assertTrue(languageValidator.isLanguagesContainsEnglish(generics));
    }

}
