package com.cintsoft.msg.wechat.mp.service;

import com.cintsoft.msg.wechat.mp.entity.WechatMpAccount;

import java.util.List;

public interface KnifeWechatMpSenderContext {

    /**
     * @description 获取微信发送账户信息
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/14 13:47
     */
    WechatMpAccount getWechatMpAccount();

    /**
     * @description 刷新微信公众号账户缓存
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/11 15:06
     */
    List<WechatMpAccount> refreshWechatMpAccountCache();

    /**
     * @param wechatMpAccount 过期账户信息
     * @description 刷新token
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/14 14:26
     */
    WechatMpAccount refreshWechatMpAccountAccessToken(WechatMpAccount wechatMpAccount);
}
