package com.wingice.msg.dingtalk.service;

import com.wingice.msg.dingtalk.entity.DingtalkAccount;

import java.util.List;

public interface KnifeDingtalkSenderContext {

    /**
     * @description 获取钉钉发送账户信息
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/14 13:47
     */
    DingtalkAccount getDingtalkAccount();

    /**
     * @description 刷新钉钉账户缓存
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/11 15:06
     */
    List<DingtalkAccount> refreshDingtalkAccountCache();

    /**
     * @param dingtalkAccount 过期账户信息
     * @description 刷新token
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/14 14:26
     */
    DingtalkAccount refreshDingtalkAccountAccessToken(DingtalkAccount dingtalkAccount);
}
