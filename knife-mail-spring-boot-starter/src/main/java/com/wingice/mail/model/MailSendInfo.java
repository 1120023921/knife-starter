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
public class MailSendInfo {

    @Tolerate
    public MailSendInfo() {
    }

    //邮件账户名
    private String username;
    //收件人列表
    @Builder.Default
    private List<String> toList = Collections.emptyList();
    //抄送人列表
    @Builder.Default
    private List<String> ccList = Collections.emptyList();
    //密送人列表
    @Builder.Default
    private List<String> bccList = Collections.emptyList();
    //主题
    private String subject;
    //正文
    private String content;
    //附件信息
    @Builder.Default
    private List<MailSendFileInfo> mailSendFileInfoList = Collections.emptyList();
}
