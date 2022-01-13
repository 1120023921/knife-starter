package com.cintsoft.msg.ali.validator;

import com.cintsoft.common.exception.ParameterValidateException;
import com.cintsoft.msg.ali.entity.AliAccount;
import org.springframework.util.StringUtils;

/**
 * @author 胡昊
 * Description:
 * Date: 2022/1/13
 * Time: 14:35
 * Mail: huhao9277@gmail.com
 */
public class AliAccountValidator {
    public static void saveOrUpdateAliAccount(AliAccount aliAccount) {
        if (!StringUtils.hasText(aliAccount.getAccessKeyId())) {
            throw new ParameterValidateException("AccessKeyId不能为空");
        }
        if (!StringUtils.hasText(aliAccount.getAccessSecret())) {
            throw new ParameterValidateException("AccessSecret不能为空");
        }
    }
}
