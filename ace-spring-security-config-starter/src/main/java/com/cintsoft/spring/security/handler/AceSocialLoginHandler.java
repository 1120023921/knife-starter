package com.cintsoft.spring.security.handler;

import com.cintsoft.spring.security.model.AceUser;

/**
 * @author 胡昊
 * Description:
 * Date: 2021/1/11
 * Time: 9:53
 * Mail: huhao9277@gmail.com
 */
public interface AceSocialLoginHandler {

    /**
     * @param code 第三方code
     * @description 第三方code认证用户
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/11 10:16
     */
    AceUser authenticate(String code);
}
