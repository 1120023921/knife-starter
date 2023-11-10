package com.wingice.mail.service.impl;

import com.wingice.mail.model.*;
import com.wingice.mail.service.MailAccountService;
import com.wingice.mail.service.MailSendService;
import com.wingice.mail.service.StoreService;
import jakarta.activation.DataHandler;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MailSendServiceImpl implements MailSendService {

    private final MailAccountService mailAccountService;
    private final StoreService storeService;

    public MailSendServiceImpl(StoreService storeService, MailAccountService mailAccountService) {
        this.storeService = storeService;
        this.mailAccountService = mailAccountService;
    }

    @Override
    public MailSendResponse sendMail(MailSendInfo mailSendInfo) {
        try {
            final MailAccount mailAccount = mailAccountService.loadMailAccountByUsername(mailSendInfo.getUsername());
            final MimeMessage message = new MimeMessage(storeService.getSession(mailSendInfo.getUsername()));
            // 设置发送方地址:
            message.setFrom(new InternetAddress(MimeUtility.encodeWord(mailAccount.getFrom())));
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
            return MailSendResponse.builder()
                    .code(200)
                    .msg("发送成功")
                    .build();
        } catch (Exception e) {
            log.error("邮件发送失败", e);
            return MailSendResponse.builder()
                    .code(500)
                    .msg(e.getMessage())
                    .build();
        }
    }

    @Override
    public MailSendResponse replyMail(MailReplyInfo mailReplyInfo) {
        try {
            final MailAccount mailAccount = mailAccountService.loadMailAccountByUsername(mailReplyInfo.getUsername());
            final Store store = storeService.getStore(mailReplyInfo.getUsername());
            final Folder folder = store.getFolder(mailReplyInfo.getFolderName());
            folder.open(Folder.READ_ONLY);
            final Message message = folder.getMessage(mailReplyInfo.getMessageNumber());
            Message reply = message.reply(mailReplyInfo.getOnlyFrom());
            reply.setFrom(new InternetAddress(mailAccount.getFrom()));
            Multipart multipart = new MimeMultipart();

            //添加回复正文:
            BodyPart contentReplyPart = new MimeBodyPart();
            contentReplyPart.setContent(mailReplyInfo.getContent(), "text/html;charset=utf-8");
            multipart.addBodyPart(contentReplyPart);

            //添加回复附件
            mailReplyInfo.getMailSendFileInfoList().forEach(mailSendFileInfo -> {
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

            //添加原始邮件内容
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent("<div>------------------------------------------------------------------<br/>" +
                    "发件人：" + addressToTextList(message.getFrom()) + "<br/>" +
                    "发送时间：" + message.getSentDate() + "<br/>" +
                    "收件人：" + addressToTextList(message.getRecipients(Message.RecipientType.TO)) + "<br/>" +
                    "主　题：" + message.getSubject() + "<br/></div>", "text/html;charset=utf-8");
            multipart.addBodyPart(contentPart);

            BodyPart contentPart2 = new MimeBodyPart();
            contentPart2.setDataHandler(message.getDataHandler());
//            contentPart2.setContent(message.getContent(), "text/html;charset=utf-8");
            multipart.addBodyPart(contentPart2);

            reply.setContent(multipart);
            Transport.send(reply);

            return MailSendResponse.builder()
                    .code(200)
                    .msg("发送成功")
                    .build();
        } catch (Exception e) {
            log.error("邮件发送失败", e);
            return MailSendResponse.builder()
                    .code(500)
                    .msg(e.getMessage())
                    .build();
        }
    }

    @Override
    public MailSendResponse mailForward(MailForwardInfo mailForwardInfo) {
        try {
            final MailAccount mailAccount = mailAccountService.loadMailAccountByUsername(mailForwardInfo.getUsername());
            final Store store = storeService.getStore(mailForwardInfo.getUsername());
            final Folder folder = store.getFolder(mailForwardInfo.getFolderName());
            folder.open(Folder.READ_ONLY);
            final Message message = folder.getMessage(mailForwardInfo.getMessageNumber());
            final MimeMessage mimeMessage = new MimeMessage(storeService.getSession(mailForwardInfo.getUsername()));
            // 设置发送方地址:
            mimeMessage.setFrom(new InternetAddress(MimeUtility.encodeWord(mailAccount.getFrom())));
            // 设置接收方地址:
            final Address[] toAddresses = new Address[mailForwardInfo.getToList().size()];
            for (int i = 0; i < mailForwardInfo.getToList().size(); i++) {
                toAddresses[i] = new InternetAddress(mailForwardInfo.getToList().get(i));
            }
            mimeMessage.setRecipients(Message.RecipientType.TO, toAddresses);
            // 设置抄送方地址:
            final Address[] ccAddresses = new Address[mailForwardInfo.getCcList().size()];
            for (int i = 0; i < mailForwardInfo.getCcList().size(); i++) {
                ccAddresses[i] = new InternetAddress(mailForwardInfo.getCcList().get(i));
            }
            mimeMessage.setRecipients(Message.RecipientType.CC, ccAddresses);
            // 设置密送方地址:
            final Address[] bccAddresses = new Address[mailForwardInfo.getBccList().size()];
            for (int i = 0; i < mailForwardInfo.getBccList().size(); i++) {
                bccAddresses[i] = new InternetAddress(mailForwardInfo.getCcList().get(i));
            }
            mimeMessage.setRecipients(Message.RecipientType.BCC, bccAddresses);
            mimeMessage.setSubject("转发：" + message.getSubject(), "UTF-8");

            final Multipart multipart = new MimeMultipart();

            BodyPart contentPart = new MimeBodyPart();
            contentPart.setDataHandler(message.getDataHandler());

            multipart.addBodyPart(contentPart);

            mimeMessage.setContent(multipart);

            Transport.send(mimeMessage);

            return MailSendResponse.builder()
                    .code(200)
                    .msg("发送成功")
                    .build();

        } catch (Exception e) {
            log.error("邮件转发失败", e);
            return MailSendResponse.builder()
                    .code(500)
                    .msg(e.getMessage())
                    .build();
        }
    }

    private String addressToTextList(Address[] addresses) throws UnsupportedEncodingException {
        final List<String> addressList = new ArrayList<>();
        if (addresses != null) {
            for (Address address : addresses) {
                final InternetAddress internetAddress = (InternetAddress) address;
                String person = internetAddress.getPersonal();
                if (person != null) {
                    person = MimeUtility.decodeText(person) + " ";
                } else {
                    person = "";
                }
                addressList.add(person + internetAddress.getAddress());
            }
        }
        return String.join(",", addressList);
    }
}
