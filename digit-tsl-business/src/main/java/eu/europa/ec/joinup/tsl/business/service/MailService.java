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

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.MailDTO;
import eu.europa.ec.joinup.tsl.business.dto.MailFileDTO;

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

    /**
     * Send email from mailDTO. Required params: subject - to - content. Opt: cc - files
     * 
     * @param mailDTO
     */
    public boolean sendMail(MailDTO mailDTO) {
        try {
            Session session = Session.getInstance(getProperties());
            MimeMessage message = new MimeMessage(session);
            Multipart multipart = new MimeMultipart();
            // Subject
            message.setSubject(mailDTO.getSubject());
            // To
            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(mailDTO.getTo()));
            // CC
            if (StringUtils.isNotEmpty(mailDTO.getCc())) {
                message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(mailDTO.getCc()));
            }
            // Content
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(mailDTO.getTemplate(), "text/html;charset=utf-8");
            multipart.addBodyPart(messageBodyPart);
            // File(s) attached
            if (CollectionUtils.isNotEmpty(mailDTO.getFiles())) {
                for (MailFileDTO file : mailDTO.getFiles()) {
                    if (file != null && StringUtils.isNotEmpty(file.getFileName())) {
                        MimeBodyPart fileBodyPart = new MimeBodyPart();
                        DataSource dataSource = new ByteArrayDataSource(file.getFile(), file.getFileType());
                        fileBodyPart.setDataHandler(new DataHandler(dataSource));
                        fileBodyPart.setFileName(file.getFileName());
                        multipart.addBodyPart(fileBodyPart);
                    }
                }
            }
            message.setContent(multipart);
            return performSend(session, message);
        } catch (MessagingException e) {
            LOGGER.error("An error occured during mail sending " + mailDTO.toString(), e);
            return false;
        }
    }

    /**
     * Send email
     * 
     * @param session
     * @param message
     * @throws MessagingException
     */
    private boolean performSend(Session session, MimeMessage message) throws MessagingException {
        Transport transport = null;
        transport = session.getTransport(protocol);
        if (!auth.equalsIgnoreCase("false")) {
            transport.connect(user, pwd);
        } else {
            transport.connect();
        }

        Address[] addresses = message.getAllRecipients();
        if ((addresses == null) || (addresses.length == 0)) {
            LOGGER.error("Mail 'TO' recipients part is null or empty");
            return false;
        } else {
            transport.sendMessage(message, message.getAllRecipients());
            if (transport != null) {
                transport.close();
            }
        }
        return true;
    }

    /**
     * Get mail properties defined in application.properties
     */
    private Properties getProperties() {
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
}
