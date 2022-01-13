package com.cintsoft.msg.ali.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cintsoft.msg.ali.entity.AliAccount;

/**
 * <p>
 * 表基础信息 服务类
 * </p>
 *
 * @author 胡昊
 * @since 2022-01-13
 */
public interface AliAccountService extends IService<AliAccount> {

    /**
     * @param aliAccount 阿里短信账户信息
     * @description 保存阿里短信账户信息
     * @author 胡昊
     * @email huhao9277@gali.com
     * @date 2022/1/11 17:07
     */
    Boolean saveOrUpdateAliAccount(AliAccount aliAccount);

    /**
     * @description 删除阿里短信账户
     * @author 胡昊
     * @email huhao9277@gali.com
     * @date 2022/1/11 17:14
     */
    Boolean deleteAliAccount();

    /**
     * @param knifeSmsAliSenderContext 阿里短信发送者上下文
     * @description 配置阿里短信发送者上下文
     * @author 胡昊
     * @email huhao9277@gali.com
     * @date 2022/1/11 20:02
     */
    void configKnifeSmsAliSenderContext(KnifeSmsAliSenderContext knifeSmsAliSenderContext);
}
