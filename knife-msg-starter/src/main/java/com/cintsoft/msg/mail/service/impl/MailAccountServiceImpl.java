package com.cintsoft.msg.mail.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cintsoft.msg.mail.entity.MailAccount;
import com.cintsoft.msg.mail.mapper.MailAccountMapper;
import com.cintsoft.msg.mail.service.KnifeMailSenderContext;
import com.cintsoft.msg.mail.service.MailAccountService;

/**
 * <p>
 * 邮件账户信息 服务实现类
 * </p>
 *
 * @author 胡昊
 * @since 2022-01-11
 */
public class MailAccountServiceImpl extends ServiceImpl<MailAccountMapper, MailAccount> implements MailAccountService {

    private KnifeMailSenderContext knifeMailSenderContext;

    @Override
    public Boolean saveOrUpdateMailAccount(MailAccount mailAccount) {
        final MailAccount account = getOne(Wrappers.lambdaQuery());
        if (account != null) {
            mailAccount.setId(account.getId());
        }
        final boolean result = saveOrUpdate(mailAccount);
        if (result && knifeMailSenderContext != null) {
            knifeMailSenderContext.refreshMailAccountCache();
        }
        return result;
    }

    @Override
    public Boolean deleteMailAccount() {
        final boolean result = remove(Wrappers.lambdaQuery());
        if (result && knifeMailSenderContext != null) {
            knifeMailSenderContext.refreshMailAccountCache();
        }
        return result;
    }

    @Override
    public void configKnifeMailSenderContext(KnifeMailSenderContext knifeMailSenderContext) {
        this.knifeMailSenderContext = knifeMailSenderContext;
    }
}
