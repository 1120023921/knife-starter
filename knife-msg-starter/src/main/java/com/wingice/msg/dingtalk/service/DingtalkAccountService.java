package com.wingice.msg.dingtalk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wingice.msg.dingtalk.entity.DingtalkAccount;

/**
 * <p>
 * 钉钉账户信息 服务类
 * </p>
 *
 * @author 胡昊
 * @since 2023-01-11
 */
public interface DingtalkAccountService extends IService<DingtalkAccount> {

    /**
     * @param dingtalkAccount 钉钉账户信息
     * @description 保存钉钉账户信息
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/11 17:07
     */
    Boolean saveOrUpdateDingtalkAccount(DingtalkAccount dingtalkAccount);

    /**
     * @description 删除钉钉账户
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/11 17:14
     */
    Boolean deleteDingtalkAccount();

    /**
     * @param knifeDingtalkSenderContext 钉钉发送者上下文
     * @description 配置钉钉发送者上下文
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/11 20:02
     */
    void configKnifeDingtalkSenderContext(KnifeDingtalkSenderContext knifeDingtalkSenderContext);
}
