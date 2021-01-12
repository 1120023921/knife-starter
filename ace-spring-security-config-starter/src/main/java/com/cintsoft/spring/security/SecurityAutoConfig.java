package com.cintsoft.spring.security;

import com.cintsoft.spring.security.aspect.AceSecurityInnerAspect;
import com.cintsoft.spring.security.common.constant.AceSecurityConfigProperties;
import com.cintsoft.spring.security.expression.CintSecurity;
import com.cintsoft.spring.security.filter.*;
import com.cintsoft.spring.security.handler.*;
import com.cintsoft.spring.security.model.AceOAuth2AccessToken;
import com.cintsoft.spring.security.model.AceUser;
import com.cintsoft.spring.security.provider.AceDaoAuthenticationProvider;
import com.cintsoft.spring.security.provider.AceInMemoryAuthenticationProvider;
import com.cintsoft.spring.security.provider.AceSocialAuthenticationProvider;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author 胡昊
 * Description:
 * Date: 2021/1/10
 * Time: 18:19
 * Mail: huhao9277@gmail.com
 */
@Configuration
public class SecurityAutoConfig {

    /**
     * @param redisConnectionFactory redis连接
     * @description token内用户信息序列化
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/10 21:43
     */
    @ConditionalOnMissingBean(name = {"userDetailRedisTemplate"})
    @Bean("userDetailRedisTemplate")
    public RedisTemplate<String, AceUser> userDetailRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        RedisTemplate<String, AceUser> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * @param redisConnectionFactory redis连接
     * @description token序列化
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/10 21:43
     */
    @ConditionalOnMissingBean(name = {"tokenRedisTemplate"})
    @Bean("tokenRedisTemplate")
    public RedisTemplate<String, AceOAuth2AccessToken> tokenRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        final RedisTemplate<String, AceOAuth2AccessToken> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * @description 内部调用拦截器
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/12 19:13
     */
    @ConditionalOnMissingBean
    @Bean
    public AceSecurityInnerAspect aceSecurityInnerAspect(HttpServletRequest request) {
        return new AceSecurityInnerAspect(request);
    }

    /**
     * @description JSON处理器
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/10 22:14
     */
    @ConditionalOnMissingBean
    @Bean
    public ObjectMapper objectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    /**
     * @description 密码加密编码器
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/10 21:44
     */
    @ConditionalOnMissingBean
    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * @param userDetailsService    用户service
     * @param bCryptPasswordEncoder 密码加密器
     * @description 默认数据库认证
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/10 21:48
     */
    @ConditionalOnBean(UserDetailsService.class)
    @Bean
    public AceDaoAuthenticationProvider aceDaoAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder bCryptPasswordEncoder) {
        return new AceDaoAuthenticationProvider(userDetailsService, bCryptPasswordEncoder);
    }

    /**
     * @param bCryptPasswordEncoder 密码加密器
     * @description 默认内存认证
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/10 21:49
     */
    @ConditionalOnProperty(name = "ace.security.default-inMemory-enable", havingValue = "true")
    @ConditionalOnMissingBean
    @Bean
    public AceInMemoryAuthenticationProvider aceInMemoryAuthenticationProvider(PasswordEncoder bCryptPasswordEncoder) {
        return new AceInMemoryAuthenticationProvider(bCryptPasswordEncoder);
    }

    /**
     * @description 默认第三方认证
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/11 10:01
     */
    @ConditionalOnMissingBean
    @Bean
    public AceSocialAuthenticationProvider aceSocialAuthenticationProvider(Map<String, AceSocialLoginHandler> aceSocialLoginHandlerMap) {
        return new AceSocialAuthenticationProvider(aceSocialLoginHandlerMap);
    }

    /**
     * @param aceDaoAuthenticationProvider      默认数据库认证
     * @param aceInMemoryAuthenticationProvider 默认内存认证
     * @description
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/10 21:47
     */
    @ConditionalOnMissingBean
    @Bean
    public List<AuthenticationProvider> authenticationProviderList(AceDaoAuthenticationProvider aceDaoAuthenticationProvider, AceSocialAuthenticationProvider aceSocialAuthenticationProvider, AceInMemoryAuthenticationProvider aceInMemoryAuthenticationProvider) {
        final List<AuthenticationProvider> authenticationProviderList = new ArrayList<>();
        if (aceDaoAuthenticationProvider != null) {
            authenticationProviderList.add(aceDaoAuthenticationProvider);
        }
        if (aceSocialAuthenticationProvider != null) {
            authenticationProviderList.add(aceSocialAuthenticationProvider);
        }
        if (aceInMemoryAuthenticationProvider != null) {
            authenticationProviderList.add(aceInMemoryAuthenticationProvider);
        }
        return authenticationProviderList;
    }

    /**
     * @param authenticationProviderList 认证处理器列表
     * @description 认证管理器
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/10 21:44
     */
    @ConditionalOnMissingBean
    @Bean
    public AuthenticationManager authenticationManager(List<AuthenticationProvider> authenticationProviderList) {
        return new ProviderManager(authenticationProviderList);
    }

    /**
     * @description 默认登录处理过滤器
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/10 21:49
     */
    @ConditionalOnMissingBean(name = "aceLoginFilter")
    @Bean("aceLoginFilter")
    public AbstractAuthenticationProcessingFilter aceLoginFilter(AuthenticationManager authenticationManager, RedisTemplate<String, AceUser> userDetailRedisTemplate, RedisTemplate<String, AceOAuth2AccessToken> tokenRedisTemplate, AceSecurityConfigProperties aceSecurityConfigProperties, ObjectMapper objectMapper) {
        if (aceSecurityConfigProperties.getTenantEnable()) {
            return new AceLoginTenantFilter(authenticationManager, userDetailRedisTemplate, tokenRedisTemplate, aceSecurityConfigProperties, objectMapper);
        } else {
            return new AceLoginFilter(authenticationManager, userDetailRedisTemplate, tokenRedisTemplate, aceSecurityConfigProperties, objectMapper);
        }
    }

    /**
     * @description 默认第三方登录过滤器
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/11 10:13
     */
    @ConditionalOnMissingBean(name = "aceSocialLoginFilter")
    @Bean("aceSocialLoginFilter")
    public AbstractAuthenticationProcessingFilter aceSocialLoginFilter(AuthenticationManager authenticationManager, RedisTemplate<String, AceUser> userDetailRedisTemplate, RedisTemplate<String, AceOAuth2AccessToken> tokenRedisTemplate, AceSecurityConfigProperties aceSecurityConfigProperties, ObjectMapper objectMapper) {
        if (aceSecurityConfigProperties.getTenantEnable()) {
            return new AceSocialLoginTenantFilter(authenticationManager, userDetailRedisTemplate, tokenRedisTemplate, aceSecurityConfigProperties, objectMapper);
        } else {
            return new AceSocialLoginFilter(authenticationManager, userDetailRedisTemplate, tokenRedisTemplate, aceSecurityConfigProperties, objectMapper);
        }
    }

    /**
     * @description 默认Token校验器
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/10 21:50
     */
    @ConditionalOnMissingBean(name = "aceVerifyFilter")
    @Bean("aceVerifyFilter")
    public OncePerRequestFilter aceVerifyFilter(AceSecurityConfigProperties aceSecurityConfigProperties, RedisTemplate<String, AceUser> userDetailRedisTemplate, ObjectMapper objectMapper) {
        if (aceSecurityConfigProperties.getTenantEnable()) {
            return new AceVerifyTenantFilter(userDetailRedisTemplate, objectMapper);
        } else {
            return new AceVerifyFilter(userDetailRedisTemplate, objectMapper);
        }
    }

    /**
     * @description 默认注销过滤器
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/10 21:51
     */
    @ConditionalOnMissingBean(name = "aceLogoutHandler")
    @Bean("aceLogoutHandler")
    public LogoutHandler aceLogoutHandler(AceSecurityConfigProperties aceSecurityConfigProperties, RedisTemplate<String, AceUser> userDetailRedisTemplate, RedisTemplate<String, AceOAuth2AccessToken> tokenRedisTemplate, ObjectMapper objectMapper) {
        if (aceSecurityConfigProperties.getTenantEnable()) {
            return new AceLogoutTenantHandler(userDetailRedisTemplate, tokenRedisTemplate, objectMapper);
        } else {
            return new AceLogoutHandler(userDetailRedisTemplate, tokenRedisTemplate, objectMapper);
        }
    }

    /**
     * @description 默认无权限处理器
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/10 21:51
     */
    @ConditionalOnMissingBean
    @Bean
    public AceAccessDeniedHandler aceAccessDeniedHandler(ObjectMapper objectMapper) {
        return new AceAccessDeniedHandler(objectMapper);
    }

    /**
     * @description 默认为认证处理器
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/10 21:51
     */
    @ConditionalOnMissingBean
    @Bean
    public AceAuthenticationFailureHandler aceAuthenticationFailureHandler(ObjectMapper objectMapper) {
        return new AceAuthenticationFailureHandler(objectMapper);
    }

    /**
     * @description 第三方OpenId认证处理器集合
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/11 9:57
     */
    @ConditionalOnMissingBean
    @Bean
    public Map<String, AceSocialLoginHandler> aceSocialLoginHandlerMap() {
        return Collections.emptyMap();
    }

    @ConditionalOnMissingBean
    @Bean
    public CintSecurity cintSecurity() {
        return new CintSecurity();
    }
}
