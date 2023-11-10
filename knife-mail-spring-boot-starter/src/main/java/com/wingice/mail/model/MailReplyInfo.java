package com.wingice.mail.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Tolerate;

import java.util.Collections;
import java.util.List;

/**
 * 发送邮件信息
 */
@Data
@Builder
@ToString
@EqualsAndHashCode
public class MailReplyInfo {

    @Tolerate
    public MailReplyInfo() {
    }

    //账户名
    private String username;
    //邮箱文件夹
    private String folderName;
    //邮件消息号
    private Integer messageNumber;
    //是否仅回复发件人
    private Boolean onlyFrom;
    //正文
    private String content;
    //附件信息
    @Builder.Default
    private List<MailSendFileInfo> mailSendFileInfoList = Collections.emptyList();
}
