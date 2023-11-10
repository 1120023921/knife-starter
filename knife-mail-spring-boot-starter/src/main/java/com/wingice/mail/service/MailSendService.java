package com.wingice.mail.service;


import com.wingice.mail.model.MailForwardInfo;
import com.wingice.mail.model.MailReplyInfo;
import com.wingice.mail.model.MailSendInfo;
import com.wingice.mail.model.MailSendResponse;

/**
 * 邮件发送服务
 */
public interface MailSendService {

    /**
     * 发送邮件
     */
    MailSendResponse sendMail(MailSendInfo mailSendInfo);

    /**
     * 回复邮件
     */
    MailSendResponse replyMail(MailReplyInfo mailReplyInfo);

    /**
     * 邮件转发
     */
    MailSendResponse mailForward(MailForwardInfo mailForwardInfo);
}
