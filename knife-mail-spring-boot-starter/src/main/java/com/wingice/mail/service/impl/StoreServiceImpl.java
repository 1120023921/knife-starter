package com.wingice.mail.service.impl;

import com.wingice.mail.model.MailAccount;
import com.wingice.mail.service.MailAccountService;
import com.wingice.mail.service.StoreService;
import jakarta.mail.*;

import java.util.Properties;

public class StoreServiceImpl implements StoreService {

    private final MailAccountService mailAccountService;

    public StoreServiceImpl(MailAccountService mailAccountService) {
        this.mailAccountService = mailAccountService;
    }

    @Override
    public Store getStore(String username) {
        try {
            final MailAccount mailAccount = mailAccountService.loadMailAccountByUsername(username);
            final Session session = getSession(username);
            final URLName url = new URLName("imap", mailAccount.getImapHost(), mailAccount.getImapPort(), null, mailAccount.getUsername(), mailAccount.getPassword());
            final Store store = session.getStore(url);
            store.connect();
            return store;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Session getSession(String username) {
        MailAccount mailAccount = mailAccountService.loadMailAccountByUsername(username);
        Properties props = new Properties();
        props.put("mail.imap.host", mailAccount.getImapHost());
        props.put("mail.imap.port", mailAccount.getImapPort());
        props.put("mail.smtp.host", mailAccount.getSmtpHost());
        props.put("mail.smtp.port", mailAccount.getSmtpPort());

        if (mailAccount.getAuth().equals(1)) {
            props.put("mail.imap.user", mailAccount.getUsername());
            props.put("mail.imap.pass", mailAccount.getPassword());
            props.put("mail.smtp.user", mailAccount.getUsername());
            props.put("mail.smtp.pass", mailAccount.getPassword());
            props.put("mail.imap.auth", "true");
            props.put("mail.smtp.auth", "true");
        } else {
            props.put("mail.imap.auth", "false");
            props.put("mail.smtp.auth", "false");
        }
        if (mailAccount.getEncryption().equals(1)) {
            props.put("mail.imap.ssl.enable", "false");
            props.put("mail.smtp.ssl.enable", "false");
        } else if (mailAccount.getEncryption().equals(2)) {
            props.put("mail.imap.ssl.enable", "true");
            props.put("mail.imap.ssl.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.imap.ssl.socketFactory.fallback", "false");
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.ssl.socketFactory.fallback", "false");
        } else if (mailAccount.getEncryption().equals(3)) {
            props.put("mail.imap.starttls.enable", "true");
            props.put("mail.smtp.starttls.enable", "true");
        }

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailAccount.getUsername(), mailAccount.getPassword());
            }
        });
    }
}
