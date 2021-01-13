package com.cintsoft.spring.security.oauth;

import com.cintsoft.spring.security.common.constant.AceSecurityConfigProperties;
import com.cintsoft.spring.security.model.AceOAuth2AccessToken;
import com.cintsoft.spring.security.model.AceUser;
import com.cintsoft.spring.security.oauth.controller.AceOAuthController;
import com.cintsoft.spring.security.oauth.service.AceOAuthClientDetailsService;
import com.cintsoft.spring.security.oauth.service.AceOAuthService;
import com.cintsoft.spring.security.oauth.service.impl.AceOAuthServiceImpl;
import com.cintsoft.spring.security.oauth.service.impl.AceOAuthServiceTenantImpl;
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
    public AceOAuthService aceOAuthService(UserDetailsService userDetailsService, RedisTemplate<String, AceUser> userDetailRedisTemplate, RedisTemplate<String, AceOAuth2AccessToken> tokenRedisTemplate, AceSecurityConfigProperties aceSecurityConfigProperties, AceOAuthConfigProperties aceOAuthConfigProperties, AuthenticationManager authenticationManager, AceOAuthClientDetailsService aceOAuthClientDetailsService) {
        if (!aceOAuthConfigProperties.getTenantEnable()) {
            return new AceOAuthServiceImpl(userDetailsService, userDetailRedisTemplate, tokenRedisTemplate, aceSecurityConfigProperties, aceOAuthConfigProperties, authenticationManager, aceOAuthClientDetailsService);
        }
        return new AceOAuthServiceTenantImpl(userDetailsService, userDetailRedisTemplate, tokenRedisTemplate, aceSecurityConfigProperties, aceOAuthConfigProperties, authenticationManager, aceOAuthClientDetailsService);
    }

    @ConditionalOnMissingBean
    @Bean
    public AceOAuthController aceOAuthController(AceOAuthService aceOAuthService, AceOAuthConfigProperties aceOAuthConfigProperties) {
        return new AceOAuthController(aceOAuthService, aceOAuthConfigProperties);
    }
}
