package com.cintsoft.msg.mail.service;

import com.cintsoft.msg.mail.entity.MailAccount;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;

public interface KnifeMailSenderContext {

    /**
     * @description 获取邮件发送对象
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/11 15:05
     */
    JavaMailSender getJavaMailSender();

    /**
     * @description 刷新邮件账户缓存
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/11 15:06
     */
    List<MailAccount> refreshMailAccountCache();
}
