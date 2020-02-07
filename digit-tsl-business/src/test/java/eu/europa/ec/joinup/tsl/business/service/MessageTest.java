package eu.europa.ec.joinup.tsl.business.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;

public class MessageTest extends AbstractSpringTest {

    @Autowired
    private MessagesService messageService;

    @Test
    public void testService() {
        assertNotNull(messageService.getMessagesBundle());
        assertTrue(messageService.getMessagesBundle().length() > 0);

    }
}
