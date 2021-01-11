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

    AceUser authenticate(String code);
}
