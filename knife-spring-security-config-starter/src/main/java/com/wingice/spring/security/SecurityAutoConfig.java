package com.wingice.spring.security;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wingice.spring.security.aspect.KnifeSecurityInnerAspect;
import com.wingice.spring.security.common.constant.KnifeSecurityConfigProperties;
import com.wingice.spring.security.controller.CaptchaController;
import com.wingice.spring.security.expression.KnifeSecurity;
import com.wingice.spring.security.filter.*;
import com.wingice.spring.security.handler.*;
import com.wingice.spring.security.model.KnifeOAuth2AccessToken;
import com.wingice.spring.security.model.KnifeUser;
import com.wingice.spring.security.provider.KnifeDaoAuthenticationProvider;
import com.wingice.spring.security.provider.KnifeInMemoryAuthenticationProvider;
import com.wingice.spring.security.provider.KnifeSocialAuthenticationProvider;
import com.wingice.spring.security.service.CaptchaService;
import com.wingice.spring.security.service.impl.CaptchaServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    public RedisTemplate<String, KnifeUser> userDetailRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        final RedisTemplate<String, KnifeUser> redisTemplate = new RedisTemplate<>();
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
    public RedisTemplate<String, KnifeOAuth2AccessToken> tokenRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        final RedisTemplate<String, KnifeOAuth2AccessToken> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @ConditionalOnMissingBean(name = {"userRefreshTokenRedisTemplate"})
    @Bean("userRefreshTokenRedisTemplate")
    public RedisTemplate<String, String> userRefreshTokenRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        final RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
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
    public KnifeSecurityInnerAspect knifeSecurityInnerAspect(HttpServletRequest request) {
        return new KnifeSecurityInnerAspect(request);
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
    public KnifeDaoAuthenticationProvider knifeDaoAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder bCryptPasswordEncoder) {
        return new KnifeDaoAuthenticationProvider(userDetailsService, bCryptPasswordEncoder);
    }

    /**
     * @param bCryptPasswordEncoder 密码加密器
     * @description 默认内存认证
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/10 21:49
     */
    @ConditionalOnProperty(name = "knife.security.default-inMemory-enable", havingValue = "true")
    @ConditionalOnMissingBean
    @Bean
    public KnifeInMemoryAuthenticationProvider knifeInMemoryAuthenticationProvider(PasswordEncoder bCryptPasswordEncoder) {
        return new KnifeInMemoryAuthenticationProvider(bCryptPasswordEncoder);
    }

    /**
     * @description 默认第三方认证
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/11 10:01
     */
    @ConditionalOnMissingBean
    @Bean
    public KnifeSocialAuthenticationProvider knifeSocialAuthenticationProvider(Map<String, KnifeSocialLoginHandler> knifeSocialLoginHandlerMap) {
        return new KnifeSocialAuthenticationProvider(knifeSocialLoginHandlerMap);
    }

    /**
     * @param knifeDaoAuthenticationProvider      默认数据库认证
     * @param knifeInMemoryAuthenticationProvider 默认内存认证
     * @description
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/10 21:47
     */
    @ConditionalOnMissingBean
    @Bean
    public List<AuthenticationProvider> authenticationProviderList(KnifeDaoAuthenticationProvider knifeDaoAuthenticationProvider, KnifeSocialAuthenticationProvider knifeSocialAuthenticationProvider, KnifeInMemoryAuthenticationProvider knifeInMemoryAuthenticationProvider) {
        final List<AuthenticationProvider> authenticationProviderList = new ArrayList<>();
        if (knifeDaoAuthenticationProvider != null) {
            authenticationProviderList.add(knifeDaoAuthenticationProvider);
        }
        if (knifeSocialAuthenticationProvider != null) {
            authenticationProviderList.add(knifeSocialAuthenticationProvider);
        }
        if (knifeInMemoryAuthenticationProvider != null) {
            authenticationProviderList.add(knifeInMemoryAuthenticationProvider);
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
    @ConditionalOnMissingBean(name = "knifeLoginFilter")
    @Bean("knifeLoginFilter")
    public AbstractAuthenticationProcessingFilter knifeLoginFilter(AuthenticationManager authenticationManager, RedisTemplate<String, KnifeUser> userDetailRedisTemplate, RedisTemplate<String, KnifeOAuth2AccessToken> tokenRedisTemplate, RedisTemplate<String, String> userRefreshTokenRedisTemplate, KnifeSecurityConfigProperties knifeSecurityConfigProperties, ObjectMapper objectMapper, StringRedisTemplate stringRedisTemplate) {
        if (knifeSecurityConfigProperties.getTenantEnable()) {
            return new KnifeLoginTenantFilter(authenticationManager, userDetailRedisTemplate, tokenRedisTemplate, userRefreshTokenRedisTemplate, knifeSecurityConfigProperties, objectMapper, stringRedisTemplate);
        } else {
            return new KnifeLoginFilter(authenticationManager, userDetailRedisTemplate, tokenRedisTemplate, userRefreshTokenRedisTemplate, knifeSecurityConfigProperties, objectMapper, stringRedisTemplate);
        }
    }

    /**
     * @description 默认第三方登录过滤器
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/11 10:13
     */
    @ConditionalOnMissingBean(name = "knifeSocialLoginFilter")
    @Bean("knifeSocialLoginFilter")
    public AbstractAuthenticationProcessingFilter knifeSocialLoginFilter(AuthenticationManager authenticationManager, RedisTemplate<String, KnifeUser> userDetailRedisTemplate, RedisTemplate<String, KnifeOAuth2AccessToken> tokenRedisTemplate, RedisTemplate<String, String> userRefreshTokenRedisTemplate, KnifeSecurityConfigProperties knifeSecurityConfigProperties, ObjectMapper objectMapper) {
        if (knifeSecurityConfigProperties.getTenantEnable()) {
            return new KnifeSocialLoginTenantFilter(authenticationManager, userDetailRedisTemplate, tokenRedisTemplate, userRefreshTokenRedisTemplate, knifeSecurityConfigProperties, objectMapper);
        } else {
            return new KnifeSocialLoginFilter(authenticationManager, userDetailRedisTemplate, tokenRedisTemplate, userRefreshTokenRedisTemplate, knifeSecurityConfigProperties, objectMapper);
        }
    }

    /**
     * @description 默认Token校验器
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/10 21:50
     */
    @ConditionalOnMissingBean(name = "knifeVerifyFilter")
    @Bean("knifeVerifyFilter")
    public OncePerRequestFilter knifeVerifyFilter(KnifeSecurityConfigProperties knifeSecurityConfigProperties, RedisTemplate<String, KnifeUser> userDetailRedisTemplate, ObjectMapper objectMapper) {
        if (knifeSecurityConfigProperties.getTenantEnable()) {
            return new KnifeVerifyTenantFilter(userDetailRedisTemplate, knifeSecurityConfigProperties, objectMapper);
        } else {
            return new KnifeVerifyFilter(userDetailRedisTemplate, knifeSecurityConfigProperties, objectMapper);
        }
    }

    /**
     * @description 默认注销过滤器
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/10 21:51
     */
    @ConditionalOnMissingBean(name = "knifeLogoutHandler")
    @Bean("knifeLogoutHandler")
    public LogoutHandler knifeLogoutHandler(KnifeSecurityConfigProperties knifeSecurityConfigProperties, RedisTemplate<String, KnifeUser> userDetailRedisTemplate, RedisTemplate<String, String> userRefreshTokenRedisTemplate, RedisTemplate<String, KnifeOAuth2AccessToken> tokenRedisTemplate, ObjectMapper objectMapper) {
        if (knifeSecurityConfigProperties.getTenantEnable()) {
            return new KnifeLogoutTenantHandler(userDetailRedisTemplate, tokenRedisTemplate, userRefreshTokenRedisTemplate, knifeSecurityConfigProperties, objectMapper);
        } else {
            return new KnifeLogoutHandler(userDetailRedisTemplate, tokenRedisTemplate, userRefreshTokenRedisTemplate, knifeSecurityConfigProperties, objectMapper);
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
    public KnifeAccessDeniedHandler knifeAccessDeniedHandler(ObjectMapper objectMapper) {
        return new KnifeAccessDeniedHandler(objectMapper);
    }

    /**
     * @description 默认为认证处理器
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/10 21:51
     */
    @ConditionalOnMissingBean
    @Bean
    public KnifeAuthenticationFailureHandler knifeAuthenticationFailureHandler(ObjectMapper objectMapper) {
        return new KnifeAuthenticationFailureHandler(objectMapper);
    }

    /**
     * @description 第三方OpenId认证处理器集合
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/11 9:57
     */
    @ConditionalOnMissingBean
    @Bean
    public Map<String, KnifeSocialLoginHandler> knifeSocialLoginHandlerMap() {
        return Collections.emptyMap();
    }

    @ConditionalOnMissingBean
    @Bean
    public KnifeSecurity knifeSecurity() {
        return new KnifeSecurity();
    }

    /**
     * @param knifeSecurityConfigProperties 配置信息
     * @param stringRedisTemplate           缓存redis
     * @description 验证码服务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/4/12 11:10
     */
    @ConditionalOnMissingBean
    @Bean
    public CaptchaService captchaService(KnifeSecurityConfigProperties knifeSecurityConfigProperties, StringRedisTemplate stringRedisTemplate) {
        return new CaptchaServiceImpl(knifeSecurityConfigProperties, stringRedisTemplate);
    }

    /**
     * @param captchaService 验证码service
     * @description 验证码controller
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/4/12 11:14
     */
    @ConditionalOnMissingBean
    @Bean
    public CaptchaController captchaController(CaptchaService captchaService) {
        return new CaptchaController(captchaService);
    }
}
