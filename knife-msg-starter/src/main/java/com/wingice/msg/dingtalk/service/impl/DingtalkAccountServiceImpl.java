package com.wingice.msg.dingtalk.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wingice.msg.dingtalk.entity.DingtalkAccount;
import com.wingice.msg.dingtalk.mapper.DingtalkAccountMapper;
import com.wingice.msg.dingtalk.service.DingtalkAccountService;
import com.wingice.msg.dingtalk.service.KnifeDingtalkSenderContext;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 钉钉账户信息 服务实现类
 * </p>
 *
 * @author 胡昊
 * @since 2023-01-11
 */
public class DingtalkAccountServiceImpl extends ServiceImpl<DingtalkAccountMapper, DingtalkAccount> implements DingtalkAccountService {

    private KnifeDingtalkSenderContext knifeDingtalkSenderContext;

    @Override
    public Boolean saveOrUpdateDingtalkAccount(DingtalkAccount dingtalkAccount) {
        final DingtalkAccount account = getOne(Wrappers.lambdaQuery());
        if (account != null) {
            dingtalkAccount.setId(account.getId());
        }
        dingtalkAccount.setAccessToken(null);
        dingtalkAccount.setAccessTokenExpires(0L);
        final boolean result = saveOrUpdate(dingtalkAccount);
        if (result && knifeDingtalkSenderContext != null) {
            knifeDingtalkSenderContext.refreshDingtalkAccountCache();
        }
        return result;
    }

    @Override
    public Boolean deleteDingtalkAccount() {
        final boolean result = remove(Wrappers.lambdaQuery());
        if (result && knifeDingtalkSenderContext != null) {
            knifeDingtalkSenderContext.refreshDingtalkAccountCache();
        }
        return result;
    }

    @Override
    public void configKnifeDingtalkSenderContext(KnifeDingtalkSenderContext knifeDingtalkSenderContext) {
        this.knifeDingtalkSenderContext = knifeDingtalkSenderContext;
    }
}
