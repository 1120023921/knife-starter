package com.cintsoft.msg.wechat.mp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cintsoft.msg.wechat.mp.entity.WechatMpAccount;

/**
 * <p>
 * 表基础信息 服务类
 * </p>
 *
 * @author 胡昊
 * @since 2022-01-14
 */
public interface WechatMpAccountService extends IService<WechatMpAccount> {

    /**
     * @param wechatMpAccount 微信公众号账户信息
     * @description 保存微信公众号账户信息
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/11 17:07
     */
    Boolean saveOrUpdateWechatMpAccount(WechatMpAccount wechatMpAccount);

    /**
     * @description 删除微信公众号账户
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/11 17:14
     */
    Boolean deleteWechatMpAccount();

    /**
     * @param knifeWechatMpSenderContext 微信公众号发送者上下文
     * @description 配置微信公众号发送者上下文
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/11 20:02
     */
    void configKnifeWechatMpSenderContext(KnifeWechatMpSenderContext knifeWechatMpSenderContext);
}
