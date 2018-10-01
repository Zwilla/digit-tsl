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
package eu.europa.ec.joinup.tsl.business.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.transaction.Transactional;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.model.DBNotification;
import eu.europa.esig.dss.DSSDocument;

/**
 * Mail configurator & MailUtils method
 */
@Service
@Transactional
public class MailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

    @Value("${mail.transport.protocol}")
    private String protocol;

    @Value("${mail.smtp.host}")
    private String host;

    @Value("${mail.smtp.user}")
    private String user;

    @Value("${mail.from}")
    private String from;

    @Value("${mail.smtp.starttls.enable}")
    private String tls;

    @Value("${mail.smtp.port}")
    private String port;

    @Value("${mail.smtp.auth}")
    private String auth;

    @Value("${mail.smtp.user.password}")
    private String pwd;

    @Autowired
    private FileService fileService;

    @Autowired
    private TLService tlService;

    @Autowired
    private PDFReportService pdfReportService;

    /**
     * Send email
     *
     * @param session
     * @param message
     */
    public void send(Session session, MimeMessage message) {
        Transport transport = null;
        try {
            transport = session.getTransport(protocol);
            if (!auth.equalsIgnoreCase("false")) {
                transport.connect(user, pwd);
            } else {
                transport.connect();
            }

            Address[] addresses = message.getAllRecipients();
            if ((addresses == null) || (addresses.length == 0)) {
                LOGGER.error("Mail 'TO' recipients part is null or empty");
                return;
            }
            transport.sendMessage(message, message.getAllRecipients());
        } catch (MessagingException e) {
            LOGGER.error("Error Send" + e);
        } finally {
            try {
                if (transport != null) {
                    transport.close();
                }
            } catch (MessagingException e) {
                LOGGER.error("Send Transport MessagingException." + e);
            }
        }
    }

    /**
     * Get mail properties defined in application.properties
     */
    public Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", protocol);
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.user", user);
        properties.setProperty("mail.from", from);
        properties.setProperty("mail.smtp.starttls.enable", tls);
        properties.setProperty("mail.smtp.port", port);
        properties.setProperty("mail.smtp.auth", auth);
        return properties;
    }

    /**
     * Init notification mail with notification report & content
     *
     * @param dbNotif
     * @param pdfReport
     * @param content
     * @throws MessagingException
     */
    public Multipart getMultipartNotificationArt23(DBNotification dbNotif, DSSDocument pdfReport, String content) throws MessagingException {

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(content, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        // XML FILE
        messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(fileService.getFilePath(dbNotif.getNotificationFile()));
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(dbNotif.getIdentifier() + ".xml");
        multipart.addBodyPart(messageBodyPart);

        // PDF REPORT
        try {
            byte[] ba = IOUtils.toByteArray(pdfReport.openStream());
            DataSource dataSource = new ByteArrayDataSource(ba, "application/pdf");
            MimeBodyPart pdfBodyPart = new MimeBodyPart();
            pdfBodyPart.setDataHandler(new DataHandler(dataSource));
            pdfBodyPart.setFileName(dbNotif.getIdentifier() + ".pdf");
            multipart.addBodyPart(pdfBodyPart);
        } catch (IOException e) {
            LOGGER.error("Error getMultipartNotificationArt23" + e);
        }

        return multipart;
    }

    /**
     * Init mail with content and TL report attached
     *
     * @param tlId
     * @param content
     * @return
     */
    public Multipart getMultipartNewTl(int tlId, String content) {
        TL tl = tlService.getTL(tlId);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Multipart multipart = new MimeMultipart();
        try {
            pdfReportService.generateTLReport(tl, os);
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(content, "text/html;charset=utf-8");

            multipart.addBodyPart(messageBodyPart);

            byte[] ba = os.toByteArray();
            DataSource dataSource = new ByteArrayDataSource(ba, "application/pdf");
            MimeBodyPart pdfBodyPart = new MimeBodyPart();
            pdfBodyPart.setDataHandler(new DataHandler(dataSource));
            pdfBodyPart.setFileName("Report_" + tl.getDbName() + ".pdf");
            multipart.addBodyPart(pdfBodyPart);
        } catch (Exception e) {
            LOGGER.error("Get Multipart new TL" + e);
        }
        return multipart;
    }

}
