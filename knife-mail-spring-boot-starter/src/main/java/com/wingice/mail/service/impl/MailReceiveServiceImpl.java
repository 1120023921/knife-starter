package com.wingice.mail.service.impl;

import com.sun.mail.imap.IMAPBodyPart;
import com.wingice.mail.model.MailInfo;
import com.wingice.mail.model.MailInfoAttachmentInfo;
import com.wingice.mail.service.MailReceiveService;
import com.wingice.mail.service.StoreService;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;
import jakarta.mail.search.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Slf4j
@Service
public class MailReceiveServiceImpl implements MailReceiveService {

    private final StoreService storeService;

    public MailReceiveServiceImpl(StoreService storeService) {
        this.storeService = storeService;
    }

    @Override
    public Integer getFolderMessageCount(String username, String folderName) {
        try {
            final Store store = storeService.getStore(username);
            final Folder folder = store.getFolder(folderName);
            // 以读写方式打开:
            folder.open(Folder.READ_ONLY);
            return folder.getMessageCount();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("获取邮件文件夹消息总数失败");
        }
    }

    @Override
    public List<MailInfo> getMailInfoList(String username, String folderName, Integer pageNum, Integer pageSize) {
        try {
            final Store store = storeService.getStore(username);
            final Folder folder = store.getFolder(folderName);
            // 以读写方式打开:
            folder.open(Folder.READ_ONLY);
            final List<MailInfo> mailInfoList = new ArrayList<>();
            int total = folder.getMessageCount();
            int maxPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
            if (pageNum > maxPage) {
                pageNum = maxPage;
            }
            int end = total - ((pageNum - 1) * pageSize);
            int start = Math.max(end - pageSize + 1, 1);
            final Message[] messageList = folder.getMessages(start, end);
            for (Message message : messageList) {
                mailInfoList.add(messageToMailInfo(message));
            }
            Collections.reverse(mailInfoList);
            return mailInfoList;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("获取邮件文件夹消息列表失败");
        }
    }

    @Override
    public Integer getUnSeenMailNum(String username, String folderName) {
        try {
            final Store store = storeService.getStore(username);
            final Folder folder = store.getFolder(folderName);
            // 以读写方式打开:
            folder.open(Folder.READ_ONLY);
            return folder.getUnreadMessageCount();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("获取未读邮件消息数量失败");
        }
    }

    @Override
    public List<MailInfo> searchMailInfoList(String username, String searchText) {
        try {
            final Store store = storeService.getStore(username);
            final Folder folder = store.getFolder("INBOX");
            // 以读写方式打开:
            folder.open(Folder.READ_WRITE);
            Message[] messageList = folder.search(new SearchTerm() {
                @Override
                public boolean match(Message msg) {
                    try {
                        final StringBuffer sb = new StringBuffer();
                        getMailTextContent(msg, sb);
                        return msg.getSubject().contains(searchText) || sb.toString().contains(searchText);
                    } catch (MessagingException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            final List<MailInfo> mailInfoList = new ArrayList<>();
            for (Message message : messageList) {
                mailInfoList.add(messageToMailInfo(message));
            }
            Collections.reverse(mailInfoList);
            return mailInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new RuntimeException("搜索邮件失败");
        }
    }

    @Override
    public MailInfo getMailInfo(String username, String folderName, Integer messageNumber) {
        try {
            final Store store = storeService.getStore(username);
            final Folder folder = store.getFolder(folderName);
            // 以读写方式打开:
            folder.open(Folder.READ_WRITE);
            final Message msg = folder.getMessage(messageNumber);
            return messageToMailInfo(msg);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("获取邮件消息详情失败");
        }
    }

    @Override
    public InputStream getMailInfoAttachment(String username, String folderName, Integer messageNumber, Integer bodyPartNum) {
        try {
            final Store store = storeService.getStore(username);
            final Folder folder = store.getFolder(folderName);
            // 以读写方式打开:
            folder.open(Folder.READ_WRITE);
            final MimeMessage msg = (MimeMessage) folder.getMessage(messageNumber);
            Multipart multipart = (Multipart) msg.getContent();
            BodyPart bodyPart = multipart.getBodyPart(bodyPartNum);
            return bodyPart.getInputStream();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("获取邮件附件文件流失败");
        }
    }

    private void getMailTextContent(Part part, StringBuffer content) throws MessagingException, IOException {
        //如果是文本类型的附件，通过getContent方法可以取到文本内容，但这不是我们需要的结果，所以在这里要做判断
        boolean isContainTextAttach = part.getContentType().indexOf("name") > 0;
        if (part.isMimeType("text/*") && !isContainTextAttach) {
            content.append(part.getContent().toString());
        } else if (part.isMimeType("message/rfc822")) {
            getMailTextContent((Part) part.getContent(), content);
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                getMailTextContent(bodyPart, content);
            }
        }
    }

    private void getAttachment(Part part, List<MailInfoAttachmentInfo> mailAttachmentInfoList) throws MessagingException, IOException {
        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();    //复杂体邮件
            //复杂体邮件包含多个邮件体
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                //获得复杂体邮件中其中一个邮件体
                BodyPart bodyPart = multipart.getBodyPart(i);
                //某一个邮件体也有可能是由多个邮件体组成的复杂体
                String disp = bodyPart.getDisposition();
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT))) {
                    MailInfoAttachmentInfo mailInfoAttachmentInfo = new MailInfoAttachmentInfo();
                    mailInfoAttachmentInfo.setFileName(MimeUtility.decodeText(bodyPart.getFileName()));
                    mailInfoAttachmentInfo.setContentId(((IMAPBodyPart) bodyPart).getContentID());
                    mailInfoAttachmentInfo.setBodyPartNum(i);
                    mailAttachmentInfoList.add(mailInfoAttachmentInfo);
                } else if (bodyPart.isMimeType("multipart/*")) {
                    getAttachment(bodyPart, mailAttachmentInfoList);
                } else {
                    //可能是邮件内嵌文件，不处理
//                    String contentType = bodyPart.getContentType();
//                    if (contentType.contains("name") || contentType.contains("application")) {
//                        MailInfoAttachmentInfo mailInfoAttachmentInfo = new MailInfoAttachmentInfo();
//                        mailInfoAttachmentInfo.setFileName(MimeUtility.decodeText(bodyPart.getFileName()));
//                        mailInfoAttachmentInfo.setContentId(((IMAPBodyPart) bodyPart).getContentID());
//                        mailInfoAttachmentInfo.setBodyPartNum(i);
//                        mailAttachmentInfoList.add(mailInfoAttachmentInfo);
//                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            getAttachment((Part) part.getContent(), mailAttachmentInfoList);
        }
    }

    private List<String> addressToTextList(Address[] addresses) throws MessagingException, UnsupportedEncodingException {
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
                addressList.add(person + "<" + internetAddress.getAddress() + ">");
            }
        }
        return addressList;
    }

    private MailInfo messageToMailInfo(Message message) {
        try {
            final MailInfo mailInfo = new MailInfo();
            // 邮件主题:
            mailInfo.setSubject(MimeUtility.decodeText(message.getSubject()));
            // 发件人:
            Address[] froms = message.getFrom();
            InternetAddress address = (InternetAddress) froms[0];
            String personal = address.getPersonal();
            String from = personal == null ? address.getAddress() : (MimeUtility.decodeText(personal) + " <" + address.getAddress() + ">");
            mailInfo.setFrom(from);
            mailInfo.setToList(addressToTextList(message.getRecipients(Message.RecipientType.TO)));
            mailInfo.setCcList(addressToTextList(message.getRecipients(Message.RecipientType.CC)));
            //发送时间
            mailInfo.setSentDate(message.getReceivedDate().getTime());
            mailInfo.setMessageNumber(message.getMessageNumber());
            StringBuffer sb = new StringBuffer();
            getMailTextContent(message, sb);
            mailInfo.setBody(sb.toString());
            mailInfo.setSeen(message.getFlags().contains(Flags.Flag.SEEN));
            List<MailInfoAttachmentInfo> mailAttachmentInfoList = new ArrayList<>();
            getAttachment(message, mailAttachmentInfoList);
            mailInfo.setMailInfoAttachmentInfoList(mailAttachmentInfoList);
            return mailInfo;
        } catch (Exception e) {
            log.error("邮件转换失败", e);
            return null;
        }
    }
}
