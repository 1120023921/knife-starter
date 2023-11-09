package com.wingice.test.service.impl;

import com.wingice.test.model.MailAccount;
import com.wingice.test.model.MailSendInfo;
import com.wingice.test.model.MailSendResponse;
import com.wingice.test.service.MailAccountService;
import com.wingice.test.service.MailSendService;
import jakarta.activation.DataHandler;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.mail.util.ByteArrayDataSource;

import java.util.Properties;

public class MailSendServiceImpl implements MailSendService {

    private final MailAccountService mailAccountService;

    public MailSendServiceImpl(MailAccountService mailAccountService) {
        this.mailAccountService = mailAccountService;
    }

    @Override
    public MailSendResponse sendMail(MailSendInfo mailSendInfo) {
        try {
            MimeMessage message = new MimeMessage(getSession());
            // 设置发送方地址:
            message.setFrom(new InternetAddress(MimeUtility.encodeWord(mailSendInfo.getFrom())));
            // 设置接收方地址:
            final Address[] toAddresses = new Address[mailSendInfo.getToList().size()];
            for (int i = 0; i < mailSendInfo.getToList().size(); i++) {
                toAddresses[i] = new InternetAddress(mailSendInfo.getToList().get(i));
            }
            message.setRecipients(Message.RecipientType.TO, toAddresses);
            // 设置抄送方地址:
            final Address[] ccAddresses = new Address[mailSendInfo.getCcList().size()];
            for (int i = 0; i < mailSendInfo.getCcList().size(); i++) {
                ccAddresses[i] = new InternetAddress(mailSendInfo.getCcList().get(i));
            }
            message.setRecipients(Message.RecipientType.CC, ccAddresses);
            // 设置密送方地址:
            final Address[] bccAddresses = new Address[mailSendInfo.getBccList().size()];
            for (int i = 0; i < mailSendInfo.getBccList().size(); i++) {
                bccAddresses[i] = new InternetAddress(mailSendInfo.getCcList().get(i));
            }
            message.setRecipients(Message.RecipientType.BCC, bccAddresses);

            // 设置邮件主题:
            message.setSubject(mailSendInfo.getSubject(), "UTF-8");

            Multipart multipart = new MimeMultipart();
            // 添加正文:
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(mailSendInfo.getContent(), "text/html;charset=utf-8");
            multipart.addBodyPart(contentPart);

            mailSendInfo.getMailSendFileInfoList().forEach(mailSendFileInfo -> {
                try {
                    // 添加附件:
                    BodyPart part = new MimeBodyPart();
                    part.setFileName(mailSendFileInfo.getFileName());
                    part.setDataHandler(new DataHandler(new ByteArrayDataSource(mailSendFileInfo.getInputStream(), mailSendFileInfo.getType())));
                    // 设置文件头:
                    mailSendFileInfo.getHeader().forEach((k, v) -> {
                        try {
                            part.setHeader(k, v);
                        } catch (MessagingException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    multipart.addBodyPart(part);
                } catch (Exception e) {
                    throw new RuntimeException("附件创建失败");
                }
            });

            message.setContent(multipart);
            // 发送:
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Session getSession() {
        MailAccount mailAccount = mailAccountService.loadMailAccountByUsername("");
        final Properties properties = new Properties();
        properties.put("mail.smtp.host", mailAccount.getHost());
        properties.put("mail.smtp.port", mailAccount.getPort());
        properties.put("mail.smtp.timeout", "10000");
        properties.put("mail.smtp.connectiontimeout", "10000");
        properties.put("mail.smtp.writetimeout", "10000");
        if (mailAccount.getAuth().equals(1)) {
            properties.put("mail.smtp.auth", "true");
        } else {
            properties.put("mail.smtp.auth", "false");
        }
        if (mailAccount.getEncryption().equals(1)) {
            properties.put("mail.smtp.ssl.enable", "false");
        }
        if (mailAccount.getEncryption().equals(2)) {
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.ssl.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.put("mail.imap.ssl.socketFactory.fallback", "false");
        }
        if (mailAccount.getEncryption().equals(3)) {
            properties.put("mail.smtp.starttls.enable", "true");
        }
        // 获取Session实例:
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailAccount.getUsername(), mailAccount.getPassword());
            }
        });
        // 设置debug模式便于调试:
        session.setDebug(true);
        return session;
    }
}
