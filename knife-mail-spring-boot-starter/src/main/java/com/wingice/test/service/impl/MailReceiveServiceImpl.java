package com.wingice.test.service.impl;

import cn.hutool.core.collection.ListUtil;
import com.sun.mail.imap.IMAPBodyPart;
import com.wingice.test.model.MailAccount;
import com.wingice.test.model.MailInfo;
import com.wingice.test.model.MailInfoAttachmentInfo;
import com.wingice.test.model.OperateMailEnum;
import com.wingice.test.service.MailAccountService;
import com.wingice.test.service.MailReceiveService;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MailReceiveServiceImpl implements MailReceiveService {

    private final MailAccountService mailAccountService;

    public MailReceiveServiceImpl(MailAccountService mailAccountService) {
        this.mailAccountService = mailAccountService;
    }

    @Override
    public List<String> getFolderList(String username) {
        try {
            final Store store = getStore(username);
            final Folder[] folderList = store.getDefaultFolder().list();
            return ListUtil.toList(folderList).stream().map(Folder::getFullName).collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("获取邮件文件夹失败");
        }
    }

    @Override
    public Boolean createFolder(String username, String folderName) {
        try {
            final Store store = getStore(username);
            Folder folder = store.getDefaultFolder();
            Folder folderNew = folder.getFolder(folderName);
            if (!folderNew.exists()) {
                return folderNew.create(Folder.HOLDS_MESSAGES);
            } else {
                return true;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("创建邮件文件夹失败");
        }
    }

    @Override
    public Boolean deleteFolder(String username, String folderName) {
        try {
            final Store store = getStore(username);
            Folder folder = store.getDefaultFolder();
            Folder folderNew = folder.getFolder(folderName);
            if (folderNew.exists()) {
                return folderNew.delete(true);
            } else {
                return true;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("创建邮件文件夹失败");
        }
    }

    @Override
    public Integer getFolderMessageCount(String username, String folderName) {
        try {
            final Store store = getStore(username);
            final Folder folder = store.getFolder(folderName);
            // 以读写方式打开:
            folder.open(Folder.READ_WRITE);
            return folder.getMessageCount();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("获取邮件文件夹消息总数失败");
        }
    }

    @Override
    public List<MailInfo> getMailInfoList(String username, String folderName, Integer pageNum, Integer pageSize) {
        try {
            final Store store = getStore(username);
            final Folder folder = store.getFolder(folderName);
            // 以读写方式打开:
            folder.open(Folder.READ_WRITE);
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
                final MimeMessage msg = (MimeMessage) message;
                final MailInfo mailInfo = new MailInfo();
                // 邮件主题:
                mailInfo.setSubject(MimeUtility.decodeText(msg.getSubject()));
                // 发件人:
                Address[] froms = msg.getFrom();
                InternetAddress address = (InternetAddress) froms[0];
                String personal = address.getPersonal();
                String from = personal == null ? address.getAddress() : (MimeUtility.decodeText(personal) + " <" + address.getAddress() + ">");
                mailInfo.setFrom(from);
                //发送时间
                mailInfo.setSentDate(msg.getSentDate().getTime());
                mailInfo.setMessageNumber(msg.getMessageNumber());
                mailInfoList.add(mailInfo);
            }
            Collections.reverse(mailInfoList);
            return mailInfoList;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("获取邮件文件夹消息列表失败");
        }
    }

    @Override
    public MailInfo getMailInfo(String username, String folderName, Integer messageNumber) {
        try {
            final Store store = getStore(username);
            final Folder folder = store.getFolder(folderName);
            // 以读写方式打开:
            folder.open(Folder.READ_WRITE);
            final MimeMessage msg = (MimeMessage) folder.getMessage(messageNumber);
            final MailInfo mailInfo = new MailInfo();
            // 邮件主题:
            mailInfo.setSubject(MimeUtility.decodeText(msg.getSubject()));
            // 发件人:
            Address[] froms = msg.getFrom();
            InternetAddress address = (InternetAddress) froms[0];
            String personal = address.getPersonal();
            String from = personal == null ? address.getAddress() : (MimeUtility.decodeText(personal) + " <" + address.getAddress() + ">");
            mailInfo.setFrom(from);
            //发送时间
            mailInfo.setSentDate(msg.getSentDate().getTime());
            mailInfo.setMessageNumber(msg.getMessageNumber());
            StringBuffer sb = new StringBuffer();
            getMailTextContent(msg, sb);
            mailInfo.setBody(sb.toString());
            List<MailInfoAttachmentInfo> mailAttachmentInfoList = new ArrayList<>();
            getAttachment(msg, mailAttachmentInfoList);
            mailInfo.setMailInfoAttachmentInfoList(mailAttachmentInfoList);
            return mailInfo;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("获取邮件消息详情失败");
        }
    }

    @Override
    public InputStream getMailInfoAttachment(String username, String folderName, Integer messageNumber, Integer bodyPartNum) {
        try {
            final Store store = getStore(username);
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

    @Override
    public Boolean operateMail(String username, String folderName, Integer messageNumber, OperateMailEnum operateMailEnum) {
        try {
            final Store store = getStore(username);
            final Folder folder = store.getFolder(folderName);
            // 以读写方式打开:
            folder.open(Folder.READ_WRITE);
            final MimeMessage msg = (MimeMessage) folder.getMessage(messageNumber);
            if (operateMailEnum.equals(OperateMailEnum.SEEN)) {
                msg.setFlag(Flags.Flag.SEEN, true);
            } else if (operateMailEnum.equals(OperateMailEnum.DELETED)) {
                msg.setFlag(Flags.Flag.DELETED, true);
            }
            folder.close(true);
            store.close();
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("操作邮件消息失败");
        }
    }

    @Override
    public Boolean moveMail(String username, Integer messageNumber, String originFolderName, String destFolderName) {
        try{
            final Store store = getStore(username);
            final Folder originFolder = store.getFolder(originFolderName);
            originFolder.open(Folder.READ_WRITE);
            final Message message = originFolder.getMessage(messageNumber);

            final Folder destFolder = store.getFolder(destFolderName);
            destFolder.open(Folder.READ_WRITE);

            originFolder.copyMessages(new Message[]{message},destFolder);
//            originFolder.setFlags(new Message[]{message}, new Flags(Flags.Flag.DELETED), true);
            originFolder.close(true);
            destFolder.close(true);
            store.close();
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException("移动邮件消息失败");
        }
    }

    public void getMailTextContent(Part part, StringBuffer content) throws MessagingException, IOException {
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

    public void getAttachment(Part part, List<MailInfoAttachmentInfo> mailAttachmentInfoList) throws MessagingException, IOException {
        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();    //复杂体邮件
            //复杂体邮件包含多个邮件体
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                //获得复杂体邮件中其中一个邮件体
                BodyPart bodyPart = multipart.getBodyPart(i);
                //某一个邮件体也有可能是由多个邮件体组成的复杂体
                String disp = bodyPart.getDisposition();
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                    MailInfoAttachmentInfo mailInfoAttachmentInfo = new MailInfoAttachmentInfo();
                    mailInfoAttachmentInfo.setFileName(MimeUtility.decodeText(bodyPart.getFileName()));
                    mailInfoAttachmentInfo.setContentId(((IMAPBodyPart) bodyPart).getContentID());
                    mailInfoAttachmentInfo.setBodyPartNum(i);
                    mailAttachmentInfoList.add(mailInfoAttachmentInfo);
                } else if (bodyPart.isMimeType("multipart/*")) {
                    getAttachment(bodyPart, mailAttachmentInfoList);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.contains("name") || contentType.contains("application")) {
                        MailInfoAttachmentInfo mailInfoAttachmentInfo = new MailInfoAttachmentInfo();
                        mailInfoAttachmentInfo.setFileName(MimeUtility.decodeText(bodyPart.getFileName()));
                        mailInfoAttachmentInfo.setContentId(((IMAPBodyPart) bodyPart).getContentID());
                        mailInfoAttachmentInfo.setBodyPartNum(i);
                        mailAttachmentInfoList.add(mailInfoAttachmentInfo);
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            getAttachment((Part) part.getContent(), mailAttachmentInfoList);
        }
    }

    public Store getStore(String username) throws Exception {
        final MailAccount mailAccount = mailAccountService.loadMailAccountByUsername(username);

        // imap配置，可保存到properties文件，读取
        Properties props = new Properties();
        props.put("mail.imap.host", mailAccount.getHost());
        props.put("mail.imap.port", mailAccount.getPort());

        if (mailAccount.getAuth().equals(1)) {
            props.put("mail.imap.user", mailAccount.getUsername());
            props.put("mail.imap.pass", mailAccount.getPassword());
            props.put("mail.smtp.auth", "true");
        } else {
            props.put("mail.smtp.auth", "false");
        }
        if (mailAccount.getEncryption().equals(1)) {
            props.put("mail.smtp.ssl.enable", "false");
        } else if (mailAccount.getEncryption().equals(2)) {
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.imap.ssl.socketFactory.fallback", "false");
        } else if (mailAccount.getEncryption().equals(3)) {
            props.put("mail.smtp.starttls.enable", "true");
        }

        Session session = Session.getInstance(props, null);
        URLName url = new URLName("imap", mailAccount.getHost(), mailAccount.getPort(), null, mailAccount.getUsername(), mailAccount.getPassword());
        Store store = session.getStore(url);
        store.connect();
        return store;
    }
}
