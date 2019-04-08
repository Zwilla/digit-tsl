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
        assertFalse(languageValidator.isLanguagePresent(tlGeneric));

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
        assertFalse(languageValidator.isLanguageLowerCase(tlGeneric));

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
        assertFalse(languageValidator.isAllowedLanguage(tlGeneric));

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
        assertTrue(languageValidator.isLanguagesContainsEnglish(generics));

        generics = new ArrayList<TLGeneric>();
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
