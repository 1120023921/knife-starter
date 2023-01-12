package com.wingice.msg.dingtalk.validator;

import com.wingice.common.exception.ParameterValidateException;
import com.wingice.msg.dingtalk.entity.DingtalkAccount;
import org.springframework.util.StringUtils;

/**
 * @author 胡昊
 * Description:
 * Date: 2022/1/14
 * Time: 16:25
 * Mail: huhao9277@gmail.com
 */
public class DingtalkAccountValidator {

    public static void saveOrUpdateDingtalkAccount(DingtalkAccount dingtalkAccount) {
        if (!StringUtils.hasText(dingtalkAccount.getAppKey())) {
            throw new ParameterValidateException("appKey不能为空");
        }
        if (!StringUtils.hasText(dingtalkAccount.getAppSecret())) {
            throw new ParameterValidateException("appSecret");
        }
    }
}
