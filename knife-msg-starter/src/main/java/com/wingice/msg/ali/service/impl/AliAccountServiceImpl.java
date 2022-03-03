package com.wingice.msg.ali.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wingice.msg.ali.entity.AliAccount;
import com.wingice.msg.ali.mapper.AliAccountMapper;
import com.wingice.msg.ali.service.AliAccountService;
import com.wingice.msg.ali.service.KnifeSmsAliSenderContext;

/**
 * <p>
 * 表基础信息 服务实现类
 * </p>
 *
 * @author 胡昊
 * @since 2022-01-13
 */
public class AliAccountServiceImpl extends ServiceImpl<AliAccountMapper, AliAccount> implements AliAccountService {

    private KnifeSmsAliSenderContext knifeSmsAliSenderContext;

    @Override
    public Boolean saveOrUpdateAliAccount(AliAccount aliAccount) {
        final AliAccount account = getOne(Wrappers.lambdaQuery());
        if (account != null) {
            aliAccount.setId(account.getId());
        }
        final boolean result = saveOrUpdate(aliAccount);
        if (result && knifeSmsAliSenderContext != null) {
            knifeSmsAliSenderContext.refreshAliAccountCache();
        }
        return result;
    }

    @Override
    public Boolean deleteAliAccount() {
        final boolean result = remove(Wrappers.lambdaQuery());
        if (result && knifeSmsAliSenderContext != null) {
            knifeSmsAliSenderContext.refreshAliAccountCache();
        }
        return result;
    }

    @Override
    public void configKnifeSmsAliSenderContext(KnifeSmsAliSenderContext knifeSmsAliSenderContext) {
        this.knifeSmsAliSenderContext = knifeSmsAliSenderContext;
    }
}
