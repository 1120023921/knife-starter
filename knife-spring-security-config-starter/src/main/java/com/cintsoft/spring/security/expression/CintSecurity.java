package com.cintsoft.spring.security.expression;

import com.cintsoft.spring.security.model.KnifeUser;
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
        if (authentication == null || authentication.getPrincipal() == null || "anonymousUser".equals(authentication.getPrincipal())) {
            return false;
        }
        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            if (permission.equals(grantedAuthority.getAuthority()) || "INNER_USER".equals(grantedAuthority.getAuthority())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasRole(String roleKey) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null || "anonymousUser".equals(authentication.getPrincipal())) {
            return false;
        }
        final KnifeUser knifeUser = (KnifeUser) authentication.getPrincipal();
        for (String item : knifeUser.getRoleKeyList()) {
            if (roleKey.equals(item) || "CINT_SUPER_ADMIN".equals(roleKey)) {
                return true;
            }
        }
        return false;
    }
}
