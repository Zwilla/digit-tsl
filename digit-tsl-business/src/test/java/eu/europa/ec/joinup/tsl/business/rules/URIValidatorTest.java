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
                Future<boolean> futurInvalid = uriValidator.isAccessibleUri("htétépé.toto.tutu");
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
                Future<boolean> futurValid = uriValidator.isAccessibleUri("https://www.google.fr");
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
