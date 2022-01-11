package com.cintsoft.msg.mail.entity;

import lombok.Data;

/**
 * @author 胡昊
 * Description:
 * Date: 2022/1/11
 * Time: 14:44
 * Mail: huhao9277@gmail.com
 */
@Data
public class MailInfo {

    private String id;//邮件id
    private String from;//邮件发送人
    private String to;//邮件接收人（多个邮箱则用逗号","隔开）
    private String subject;//邮件主题
    private String text;//邮件内容
    private Long sentDate;//发送时间
    private String cc;//抄送（多个邮箱则用逗号","隔开）
    private String bcc;//密送（多个邮箱则用逗号","隔开）
    private Boolean multipart = true;//是否启用html

    public MailInfo() {
    }

    public MailInfo(String id, String from, String to, String subject, String text, Long sentDate, String cc, String bcc, Boolean multipart) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.text = text;
        this.sentDate = sentDate;
        this.cc = cc;
        this.bcc = bcc;
        this.multipart = multipart;
    }
}