package eu.europa.ec.joinup.tsl.business.dto.contact;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;

import eu.europa.ec.joinup.tsl.business.dto.contact.ContactList.TLSOContact;

public class ContactJaxbTest {

    @Test
    public void testMarshal() throws Exception {

        File file = new File("src/test/resources/contact-marshal.xml");
        JAXBContext jaxbContext = JAXBContext.newInstance(ContactList.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        ContactList contactList = (ContactList) jaxbUnmarshaller.unmarshal(file);
        for (TLSOContact tlso : contactList.getTLSOContact()) {
            Assert.assertNotNull(tlso);
            System.out.println(tlso);
        }
        Assert.assertEquals(32, contactList.getTLSOContact().size());
    }
}
