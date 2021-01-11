package com.cintsoft.spring.security.provider;

import com.cintsoft.spring.security.handler.AceSocialLoginHandler;
import com.cintsoft.spring.security.model.AceSocialAuthenticationToken;
import com.cintsoft.spring.security.model.AceUser;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Map;

/**
 * @author 胡昊
 * Description: 数据库认证
 * Date: 2020/7/27
 * Time: 10:43
 * Mail: huhao9277@gmail.com
 */
public class AceSocialAuthenticationProvider implements AuthenticationProvider {

    private final Map<String, AceSocialLoginHandler> aceSocialLoginHandlerMap;

    public AceSocialAuthenticationProvider(Map<String, AceSocialLoginHandler> aceSocialLoginHandlerMap) {
        this.aceSocialLoginHandlerMap = aceSocialLoginHandlerMap;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String source = (String) authentication.getPrincipal();
        final String code = (String) authentication.getCredentials();
        final AceSocialLoginHandler aceSocialLoginHandler = aceSocialLoginHandlerMap.get(source);
        if (aceSocialLoginHandler != null) {
            final AceUser aceUser = aceSocialLoginHandler.authenticate(code);
            if (aceUser != null) {
                return new UsernamePasswordAuthenticationToken(aceUser, aceUser.getPassword(), aceUser.getAuthorities());
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AceSocialAuthenticationToken.class == authentication;
    }

}
