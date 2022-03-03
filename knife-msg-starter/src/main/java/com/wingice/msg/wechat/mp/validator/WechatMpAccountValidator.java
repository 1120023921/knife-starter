package com.wingice.msg.wechat.mp.validator;

import com.wingice.common.exception.ParameterValidateException;
import com.wingice.msg.wechat.mp.entity.WechatMpAccount;
import org.springframework.util.StringUtils;

/**
 * @author 胡昊
 * Description:
 * Date: 2022/1/14
 * Time: 16:25
 * Mail: huhao9277@gmail.com
 */
public class WechatMpAccountValidator {

    public static void saveOrUpdateWechatMpAccount(WechatMpAccount wechatMpAccount) {
        if (!StringUtils.hasText(wechatMpAccount.getAppid())) {
            throw new ParameterValidateException("appid不能为空");
        }
        if (!StringUtils.hasText(wechatMpAccount.getSecret())) {
            throw new ParameterValidateException("secret不能为空");
        }
    }
}
