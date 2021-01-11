package com.cintsoft.spring.security.common.utils;

import com.cintsoft.spring.security.model.AceUser;
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

    public static AceUser getUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return (AceUser) authentication.getPrincipal();
        }
        return null;
    }
}
