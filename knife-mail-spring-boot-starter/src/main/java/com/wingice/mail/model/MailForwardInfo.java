package com.wingice.mail.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Tolerate;

import java.util.Collections;
import java.util.List;

/**
 * 转发邮件信息
 */
@Data
@Builder
@ToString
@EqualsAndHashCode
public class MailForwardInfo {

    @Tolerate
    public MailForwardInfo() {
    }

    //账户名
    private String username;
    //邮箱文件夹
    private String folderName;
    //邮件消息号
    private Integer messageNumber;
    //收件人列表
    @Builder.Default
    private List<String> toList = Collections.emptyList();
    //抄送人列表
    @Builder.Default
    private List<String> ccList = Collections.emptyList();
    //密送人列表
    @Builder.Default
    private List<String> bccList = Collections.emptyList();
}
