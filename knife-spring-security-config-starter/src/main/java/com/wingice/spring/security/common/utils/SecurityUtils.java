package com.wingice.spring.security.common.utils;

import com.wingice.spring.security.model.KnifeUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author 胡昊
 * Description:
 * Date: 2021/1/10
 * Time: 20:57
 * Mail: huhao9277@gmail.com
 */
public class SecurityUtils {

    public static KnifeUser getUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null && !"anonymousUser".equals(authentication.getPrincipal())) {
            return (KnifeUser) authentication.getPrincipal();
        }
        return null;
    }
}
