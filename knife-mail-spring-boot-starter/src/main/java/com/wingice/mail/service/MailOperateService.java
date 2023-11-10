package com.wingice.mail.service;

import com.wingice.mail.model.OperateMailEnum;

import java.util.List;

public interface MailOperateService {

    /**
     * 获取邮箱文件夹
     */
    List<String> getFolderList(String username);

    /**
     * 创建邮件文件夹
     */
    Boolean createFolder(String username, String folderName);

    /**
     * 删除邮件文件夹
     */
    Boolean deleteFolder(String username, String folderName);

    /**
     * 邮件标记
     */
    Boolean mailFlag(String username, String folderName, Integer messageNumber, OperateMailEnum operateMailEnum, Boolean set);

    /**
     * 移动邮件至其他文件夹
     */
    Boolean moveMail(String username, Integer messageNumber, String originFolderName, String destFolderName);
}
