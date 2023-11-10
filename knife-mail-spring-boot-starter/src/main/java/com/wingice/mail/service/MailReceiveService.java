package com.wingice.mail.service;


import com.wingice.mail.model.MailInfo;

import java.io.InputStream;
import java.util.List;

public interface MailReceiveService {

    /**
     * 获取邮箱文件夹邮件总数
     */
    Integer getFolderMessageCount(String username, String folderName);

    /**
     * 分页查询邮件
     */
    List<MailInfo> getMailInfoList(String username, String folderName, Integer pageNum, Integer pageSize);

    /**
     * 获取未读邮件数量
     */
    Integer getUnSeenMailNum(String username, String folderName);

    /**
     * 搜索邮件
     */
    List<MailInfo> searchMailInfoList(String username, String searchText);

    /**
     * 查询邮件详情
     */
    MailInfo getMailInfo(String username, String folderName, Integer messageNumber);

    /**
     * 获取指定附件输入流
     */
    InputStream getMailInfoAttachment(String username, String folderName, Integer messageNumber, Integer bodyPartNum);
}
