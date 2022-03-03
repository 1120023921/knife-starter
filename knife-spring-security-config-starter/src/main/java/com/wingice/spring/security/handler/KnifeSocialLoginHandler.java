package com.wingice.spring.security.handler;

import com.wingice.spring.security.model.KnifeUser;

/**
 * @author 胡昊
 * Description:
 * Date: 2021/1/11
 * Time: 9:53
 * Mail: huhao9277@gmail.com
 */
public interface KnifeSocialLoginHandler {

    /**
     * @param code 第三方code
     * @description 第三方code认证用户
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/11 10:16
     */
    KnifeUser authenticate(String code);
}
