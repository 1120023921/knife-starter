package com.wingice.mail.service.impl;

import cn.hutool.core.collection.ListUtil;
import com.wingice.mail.model.OperateMailEnum;
import com.wingice.mail.service.MailOperateService;
import com.wingice.mail.service.StoreService;
import jakarta.mail.Flags;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Store;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class MailOperateServiceImpl implements MailOperateService {

    private final StoreService storeService;

    public MailOperateServiceImpl(StoreService storeService) {
        this.storeService = storeService;
    }

    @Override
    public List<String> getFolderList(String username) {
        try {
            final Store store = storeService.getStore(username);
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
            final Store store = storeService.getStore(username);
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
            final Store store = storeService.getStore(username);
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
    public Boolean mailFlag(String username, String folderName, Integer messageNumber, OperateMailEnum operateMailEnum, Boolean set) {
        try {
            final Store store = storeService.getStore(username);
            final Folder folder = store.getFolder(folderName);
            // 以读写方式打开:
            folder.open(Folder.READ_WRITE);
            final MimeMessage msg = (MimeMessage) folder.getMessage(messageNumber);
            if (operateMailEnum.equals(OperateMailEnum.ANSWERED)) {
                msg.setFlag(Flags.Flag.ANSWERED, set);
            } else if (operateMailEnum.equals(OperateMailEnum.DELETED)) {
                msg.setFlag(Flags.Flag.DELETED, set);
            } else if (operateMailEnum.equals(OperateMailEnum.DRAFT)) {
                msg.setFlag(Flags.Flag.DRAFT, set);
            } else if (operateMailEnum.equals(OperateMailEnum.FLAGGED)) {
                msg.setFlag(Flags.Flag.FLAGGED, set);
            } else if (operateMailEnum.equals(OperateMailEnum.RECENT)) {
                msg.setFlag(Flags.Flag.RECENT, set);
            } else if (operateMailEnum.equals(OperateMailEnum.SEEN)) {
                msg.setFlag(Flags.Flag.SEEN, set);
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
        try {
            final Store store = storeService.getStore(username);
            final Folder originFolder = store.getFolder(originFolderName);
            originFolder.open(Folder.READ_WRITE);
            final Message message = originFolder.getMessage(messageNumber);

            final Folder destFolder = store.getFolder(destFolderName);
            destFolder.open(Folder.READ_WRITE);

            originFolder.copyMessages(new Message[]{message}, destFolder);
//            originFolder.setFlags(new Message[]{message}, new Flags(Flags.Flag.DELETED), true);
            originFolder.close(true);
            destFolder.close(true);
            store.close();
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("移动邮件消息失败");
        }
    }
}
