package eu.europa.ec.joinup.tsl.business.rules;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.service.PropertiesService;

import static org.junit.Assert.*;

public class GenericValidatorTest extends AbstractSpringTest {

    @Autowired
    private GenericValidator genericValidator;

    @Autowired
    private PropertiesService propertiesService;

    @Test
    public void testPolicyLabel() {
        String englishLegalNoticeText = propertiesService.getEnglishLegalNoticeText();

        assertFalse(genericValidator.isMatch(englishLegalNoticeText, "bla"));

        assertEquals("The applicable legal framework for the present trusted list is Regulation (EU) No 910/2014 of the European Parliament and of the Council of 23 July 2014 on electronic identification and trust services for electronic transactions in the internal market and repealing Directive 1999/93/EC.", englishLegalNoticeText);
    }

}
