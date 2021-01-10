package com.cintsoft.spring.security.expression;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author 胡昊
 * Description: 自定义Expression
 * Date: 2020/7/24
 * Time: 10:39
 * Mail: huhao9277@gmail.com
 */
public class CintSecurity {

    public boolean hasPermission(String permission) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            if (permission.equals(grantedAuthority.getAuthority()) || "INNER_USER".equals(grantedAuthority.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}
