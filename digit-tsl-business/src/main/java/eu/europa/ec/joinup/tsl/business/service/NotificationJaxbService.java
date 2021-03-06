package eu.europa.ec.joinup.tsl.business.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.XmlMappingException;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.contact.ContactList;
import eu.europa.ec.joinup.tsl.business.dto.notification.MemberStateNotificationV5;

/**
 * JaxB Notification marshall/unmarshall
 */
@Service
public class NotificationJaxbService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationJaxbService.class);

    // Unmarshall
    public MemberStateNotificationV5 unmarshallNotification(InputStream is) throws Exception {
        JAXBContext jaxbContext = createJaxbContext();
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        XMLInputFactory xif = XMLInputFactory.newInstance();
        xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        XMLStreamReader xsr = xif.createXMLStreamReader(new StreamSource(is));
        MemberStateNotificationV5 notification = (MemberStateNotificationV5) unmarshaller.unmarshal(xsr);
        return notification;
    }

    private JAXBContext createJaxbContext() throws JAXBException {
        return JAXBContext.newInstance(MemberStateNotificationV5.class, ContactList.class, eu.europa.esig.jaxb.v5.tsl.ObjectFactory.class, eu.europa.esig.jaxb.v5.tslx.ObjectFactory.class,
                eu.europa.esig.jaxb.v5.ecc.ObjectFactory.class, eu.europa.esig.jaxb.v5.xades.ObjectFactory.class, eu.europa.esig.jaxb.v5.xades141.ObjectFactory.class,
                eu.europa.esig.jaxb.v5.xmldsig.ObjectFactory.class);
    }

    public MemberStateNotificationV5 unmarshallNotification(byte[] content) throws Exception {
        MemberStateNotificationV5 notification = null;
        InputStream is = null;
        try {
            is = new ByteArrayInputStream(content);
            notification = unmarshallNotification(is);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return notification;
    }

    // Marshall notification jaxb class
    public void marshallNotification(MemberStateNotificationV5 notification, OutputStream os) throws XmlMappingException {
        try {
            JAXBContext jaxbContext = createJaxbContext();
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(notification, new StreamResult(os));
        } catch (JAXBException e) {
            LOGGER.error("Marshal Notification error");
        }
    }

}
