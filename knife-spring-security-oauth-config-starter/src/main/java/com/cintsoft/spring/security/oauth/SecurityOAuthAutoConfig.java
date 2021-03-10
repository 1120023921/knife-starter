package com.cintsoft.spring.security.oauth;

import com.cintsoft.spring.security.common.constant.KnifeSecurityConfigProperties;
import com.cintsoft.spring.security.model.KnifeOAuth2AccessToken;
import com.cintsoft.spring.security.model.KnifeUser;
import com.cintsoft.spring.security.oauth.controller.KnifeOAuthController;
import com.cintsoft.spring.security.oauth.service.KnifeOAuthClientDetailsService;
import com.cintsoft.spring.security.oauth.service.KnifeOAuthService;
import com.cintsoft.spring.security.oauth.service.impl.KnifeOAuthServiceImpl;
import com.cintsoft.spring.security.oauth.service.impl.KnifeOAuthServiceTenantImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author 胡昊
 * Description:
 * Date: 2021/1/12
 * Time: 16:11
 * Mail: huhao9277@gmail.com
 */
@Configuration
public class SecurityOAuthAutoConfig {

    @Bean
    public KnifeOAuthService knifeOAuthService(UserDetailsService userDetailsService, RedisTemplate<String, KnifeUser> userDetailRedisTemplate, RedisTemplate<String, KnifeOAuth2AccessToken> tokenRedisTemplate, KnifeSecurityConfigProperties knifeSecurityConfigProperties, KnifeOAuthConfigProperties knifeOAuthConfigProperties, AuthenticationManager authenticationManager, KnifeOAuthClientDetailsService knifeOAuthClientDetailsService) {
        if (!knifeOAuthConfigProperties.getTenantEnable()) {
            return new KnifeOAuthServiceImpl(userDetailsService, userDetailRedisTemplate, tokenRedisTemplate, knifeSecurityConfigProperties, knifeOAuthConfigProperties, authenticationManager, knifeOAuthClientDetailsService);
        }
        return new KnifeOAuthServiceTenantImpl(userDetailsService, userDetailRedisTemplate, tokenRedisTemplate, knifeSecurityConfigProperties, knifeOAuthConfigProperties, authenticationManager, knifeOAuthClientDetailsService);
    }

    @ConditionalOnMissingBean
    @Bean
    public KnifeOAuthController knifeOAuthController(KnifeOAuthService knifeOAuthService, KnifeOAuthConfigProperties knifeOAuthConfigProperties) {
        return new KnifeOAuthController(knifeOAuthService, knifeOAuthConfigProperties);
    }
}
