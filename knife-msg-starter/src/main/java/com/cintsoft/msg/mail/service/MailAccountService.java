package com.cintsoft.msg.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cintsoft.msg.mail.entity.MailAccount;

/**
 * <p>
 * 邮件账户信息 服务类
 * </p>
 *
 * @author 胡昊
 * @since 2022-01-11
 */
public interface MailAccountService extends IService<MailAccount> {

    /**
     * @param mailAccount 邮件账户信息
     * @description 保存邮件账户信息
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/11 17:07
     */
    Boolean saveOrUpdateMailAccount(MailAccount mailAccount);

    /**
     * @description 删除邮件账户
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/11 17:14
     */
    Boolean deleteMailAccount();

    /**
     * @param knifeMailSenderContext 邮件发送者上下文
     * @description 配置邮件发送者上下文
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/11 20:02
     */
    void configKnifeMailSenderContext(KnifeMailSenderContext knifeMailSenderContext);
}
