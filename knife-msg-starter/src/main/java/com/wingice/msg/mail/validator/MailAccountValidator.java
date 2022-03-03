package com.wingice.msg.mail.validator;

import com.wingice.common.exception.ParameterValidateException;
import com.wingice.msg.mail.entity.MailAccount;
import org.springframework.util.StringUtils;

/**
 * @author 胡昊
 * Description:
 * Date: 2022/1/11
 * Time: 16:40
 * Mail: huhao9277@gmail.com
 */
public class MailAccountValidator {

    public static void saveOrUpdateMailAccount(MailAccount mailAccount) {
        if (!StringUtils.hasText(mailAccount.getHost())) {
            throw new ParameterValidateException("服务器地址不能为空");
        }
        if (mailAccount.getPort() == null) {
            throw new ParameterValidateException("服务器端口不能为空");
        }
        if (!StringUtils.hasText(mailAccount.getMailFrom())) {
            throw new ParameterValidateException("邮件发信人不能为空");
        }
        if (mailAccount.getEncryption() == null) {
            throw new ParameterValidateException("加密方式不能为空");
        }
        if (mailAccount.getAuth() == null) {
            throw new ParameterValidateException("加密方式不能为空");
        }
        if (mailAccount.getAuth() != 0) {
            if (!StringUtils.hasText(mailAccount.getUsername())) {
                throw new ParameterValidateException("用户名不能为空");
            }
            if (!StringUtils.hasText(mailAccount.getPassword())) {
                throw new ParameterValidateException("密码不能为空");
            }
        }
    }
}
