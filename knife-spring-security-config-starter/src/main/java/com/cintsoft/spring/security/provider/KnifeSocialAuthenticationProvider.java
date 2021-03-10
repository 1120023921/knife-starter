package com.cintsoft.spring.security.provider;

import com.cintsoft.spring.security.handler.KnifeSocialLoginHandler;
import com.cintsoft.spring.security.model.KnifeSocialAuthenticationToken;
import com.cintsoft.spring.security.model.KnifeUser;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Map;

/**
 * @author 胡昊
 * Description: 第三方认证
 * Date: 2020/7/27
 * Time: 10:43
 * Mail: huhao9277@gmail.com
 */
public class KnifeSocialAuthenticationProvider implements AuthenticationProvider {

    private final Map<String, KnifeSocialLoginHandler> knifeSocialLoginHandlerMap;

    public KnifeSocialAuthenticationProvider(Map<String, KnifeSocialLoginHandler> knifeSocialLoginHandlerMap) {
        this.knifeSocialLoginHandlerMap = knifeSocialLoginHandlerMap;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String source = (String) authentication.getPrincipal();
        final String code = (String) authentication.getCredentials();
        final KnifeSocialLoginHandler knifeSocialLoginHandler = knifeSocialLoginHandlerMap.get(source);
        if (knifeSocialLoginHandler != null) {
            final KnifeUser knifeUser = knifeSocialLoginHandler.authenticate(code);
            if (knifeUser != null) {
                return new UsernamePasswordAuthenticationToken(knifeUser, knifeUser.getPassword(), knifeUser.getAuthorities());
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return KnifeSocialAuthenticationToken.class == authentication;
    }

}
