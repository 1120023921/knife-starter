package com.wingice.test.service;


import com.wingice.test.model.MailAccount;

/**
 * 邮件账户服务接口
 */
public interface MailAccountService {

    /**
     * 根据用户名获取指定邮件账户信息
     */
    MailAccount loadMailAccountByUsername(String username);
}
