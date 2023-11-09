package com.wingice.test.service;


import com.wingice.test.model.MailSendInfo;
import com.wingice.test.model.MailSendResponse;

/**
 * 邮件发送服务
 */
public interface MailSendService {

    /**
     * 发送邮件
     */
    MailSendResponse sendMail(MailSendInfo mailSendInfo);
}
