package com.wingice.mail.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 邮件账户信息
 */
@Data
@Builder
@ToString
@EqualsAndHashCode
public class MailAccount {

    //邮件服务器地址
    private String imapHost;
    //邮件服务器端口
    private Integer imapPort;
    //邮件服务器地址
    private String smtpHost;
    //邮件服务器端口
    private Integer smtpPort;
    //用户名
    private String username;
    //密码
    private String password;
    //加密方式 1-不加密 2-ssl 3-tls
    private Integer encryption;
    //是否认证 0-无需认证 1-需要认证
    private Integer auth;
    //发件人
    private String from;
}