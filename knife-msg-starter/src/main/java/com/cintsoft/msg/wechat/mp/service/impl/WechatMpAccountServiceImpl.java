package com.cintsoft.msg.wechat.mp.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cintsoft.msg.wechat.mp.entity.WechatMpAccount;
import com.cintsoft.msg.wechat.mp.mapper.WechatMpAccountMapper;
import com.cintsoft.msg.wechat.mp.service.KnifeWechatMpSenderContext;
import com.cintsoft.msg.wechat.mp.service.WechatMpAccountService;

/**
 * <p>
 * 表基础信息 服务实现类
 * </p>
 *
 * @author 胡昊
 * @since 2022-01-14
 */
public class WechatMpAccountServiceImpl extends ServiceImpl<WechatMpAccountMapper, WechatMpAccount> implements WechatMpAccountService {

    private KnifeWechatMpSenderContext knifeWechatMpSenderContext;

    @Override
    public Boolean saveOrUpdateWechatMpAccount(WechatMpAccount wechatMpAccount) {
        final WechatMpAccount account = getOne(Wrappers.lambdaQuery());
        if (account != null) {
            wechatMpAccount.setId(account.getId());
        }
        wechatMpAccount.setAccessToken(null);
        wechatMpAccount.setAccessTokenExpires(0L);
        final boolean result = saveOrUpdate(wechatMpAccount);
        if (result && knifeWechatMpSenderContext != null) {
            knifeWechatMpSenderContext.refreshWechatMpAccountCache();
        }
        return result;
    }

    @Override
    public Boolean deleteWechatMpAccount() {
        final boolean result = remove(Wrappers.lambdaQuery());
        if (result && knifeWechatMpSenderContext != null) {
            knifeWechatMpSenderContext.refreshWechatMpAccountCache();
        }
        return result;
    }

    @Override
    public void configKnifeWechatMpSenderContext(KnifeWechatMpSenderContext knifeWechatMpSenderContext) {
        this.knifeWechatMpSenderContext = knifeWechatMpSenderContext;
    }
}
