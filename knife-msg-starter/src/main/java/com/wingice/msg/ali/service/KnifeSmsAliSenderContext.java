package com.wingice.msg.ali.service;

import com.aliyun.dysmsapi20170525.Client;
import com.wingice.msg.ali.entity.AliAccount;
import com.wingice.msg.ali.exception.AliAccountNotFoundException;
import com.wingice.msg.ali.exception.MultiAliAccountException;

import java.util.List;

public interface KnifeSmsAliSenderContext {

    /**
     * @description 获取短信发送对象
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/11 15:05
     */
    Client getClient() throws AliAccountNotFoundException, MultiAliAccountException;

    /**
     * @description 刷新邮件账户缓存
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/11 15:06
     */
    List<AliAccount> refreshAliAccountCache();
}
