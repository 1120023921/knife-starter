package com.wingice.mail.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Tolerate;

import java.util.Collections;
import java.util.List;

/**
 * 接收邮件详情信息
 */
@Data
@Builder
@ToString
@EqualsAndHashCode
public class MailInfo {

    @Tolerate
    public MailInfo() {
    }

    //发送人
    private String from;
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
    //邮件正文
    private String body;
    //邮件消息号
    private Integer messageNumber;
    //发送日期
    private Long sentDate;
    //是否已读
    private Boolean seen;
    //附件列表
    @Builder.Default
    private List<MailInfoAttachmentInfo> mailInfoAttachmentInfoList = Collections.emptyList();
}
