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

import java.util.concurrent.Future;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;

public class URIValidatorTest extends AbstractSpringTest {

    @Autowired
    private URIValidator uriValidator;

    @Test
    public void isSecureURI() {
        assertFalse(uriValidator.isSecureURI("www.google.be"));

        assertFalse(uriValidator.isSecureURI("http.google.be"));

        assertTrue(uriValidator.isSecureURI("https.google.fr"));
    }

    @Test
    public void isAccessibleUri() throws Exception {
        boolean result = false;
        try {
            while (true) {
                Future<Boolean> futurInvalid = uriValidator.isAccessibleUri("htétépé.toto.tutu");
                if (futurInvalid.isDone()) {
                    result = futurInvalid.get();
                    assertFalse(result);
                    break;
                }
                Thread.sleep(100);
            }
        } catch (Exception e) {
            throw new Exception("JUnit Test isAccessibleURI 'htétépé.toto.tutu' error.");
        }

        try {
            while (true) {
                Future<Boolean> futurValid = uriValidator.isAccessibleUri("https://www.google.fr");
                if (futurValid.isDone()) {
                    result = futurValid.get();
                    assertTrue(result);
                    break;
                }
                Thread.sleep(100);
            }
        } catch (Exception e) {
            throw new Exception("JUnit Test isAccessibleURI 'https://www.google.fr' error.");
        }
    }

    @Test
    public void isCorrectUrl() {
        assertFalse(uriValidator.isCorrectUrl("www.go ogle.be"));

        assertFalse(uriValidator.isCorrectUrl("htp.google.be"));

        assertFalse(uriValidator.isCorrectUrl("http.googlenet"));

        assertFalse(uriValidator.isCorrectUrl("http://.facebook.com"));

        assertTrue(uriValidator.isCorrectUrl("https://www.google.fr"));
    }

}
