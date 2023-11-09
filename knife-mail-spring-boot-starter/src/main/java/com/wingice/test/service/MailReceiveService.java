package com.wingice.test.service;


import com.wingice.test.model.MailInfo;
import com.wingice.test.model.OperateMailEnum;

import java.io.InputStream;
import java.util.List;

public interface MailReceiveService {

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
     * 获取邮箱文件夹邮件总数
     */
    Integer getFolderMessageCount(String username, String folderName);

    /**
     * 分页查询邮件
     */
    List<MailInfo> getMailInfoList(String username, String folderName, Integer pageNum, Integer pageSize);

    /**
     * 查询邮件详情
     */
    MailInfo getMailInfo(String username, String folderName, Integer messageNumber);

    /**
     * 获取指定附件输入流
     */
    InputStream getMailInfoAttachment(String username, String folderName, Integer messageNumber, Integer bodyPartNum);

    /**
     * 操作邮件
     */
    Boolean operateMail(String username, String folderName, Integer messageNumber, OperateMailEnum operateMailEnum);

    /**
     * 移动邮件至其他文件夹
     */
    Boolean moveMail(String username, Integer messageNumber, String originFolderName, String destFolderName);
}
