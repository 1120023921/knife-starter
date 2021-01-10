package com.cintsoft.spring.security;

import com.cintsoft.spring.security.common.constant.AceSecurityConfigProperties;
import com.cintsoft.spring.security.expression.CintSecurity;
import com.cintsoft.spring.security.filter.DefaultLoginFilter;
import com.cintsoft.spring.security.filter.DefaultVerifyFilter;
import com.cintsoft.spring.security.handler.AceAccessDeniedHandler;
import com.cintsoft.spring.security.handler.AceAuthenticationFailureHandler;
import com.cintsoft.spring.security.handler.AceLogoutHandler;
import com.cintsoft.spring.security.model.AceOAuth2AccessToken;
import com.cintsoft.spring.security.model.AceUser;
import com.cintsoft.spring.security.provider.AceDaoAuthenticationProvider;
import com.cintsoft.spring.security.provider.AceInMemoryAuthenticationProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.Filter;
import java.util.Arrays;
import java.util.List;

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
        final RedisTemplate<String, AceUser> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
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
        return redisTemplate;
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
        return new ObjectMapper();
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
    @ConditionalOnMissingBean
    @Bean
    public AceInMemoryAuthenticationProvider aceInMemoryAuthenticationProvider(PasswordEncoder bCryptPasswordEncoder) {
        return new AceInMemoryAuthenticationProvider(bCryptPasswordEncoder);
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
    public List<AuthenticationProvider> authenticationProviderList(AceDaoAuthenticationProvider aceDaoAuthenticationProvider, AceInMemoryAuthenticationProvider aceInMemoryAuthenticationProvider) {
        return Arrays.asList(aceDaoAuthenticationProvider, aceInMemoryAuthenticationProvider);
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
    @ConditionalOnMissingBean
    @Bean
    public DefaultLoginFilter defaultLoginFilter(AuthenticationManager authenticationManager, RedisTemplate<String, AceUser> userDetailRedisTemplate, RedisTemplate<String, AceOAuth2AccessToken> tokenRedisTemplate, AceSecurityConfigProperties aceSecurityConfigProperties, ObjectMapper objectMapper) {
        return new DefaultLoginFilter(authenticationManager, userDetailRedisTemplate, tokenRedisTemplate, aceSecurityConfigProperties, objectMapper);
    }

    /**
     * @description 默认Token校验器
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/10 21:50
     */
    @ConditionalOnMissingBean
    @Bean
    public DefaultVerifyFilter defaultVerifyFilter(RedisTemplate<String, AceUser> userDetailRedisTemplate, ObjectMapper objectMapper) {
        return new DefaultVerifyFilter(userDetailRedisTemplate, objectMapper);
    }

    /**
     * @description 默认注销过滤器
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/10 21:51
     */
    @ConditionalOnMissingBean
    @Bean
    public AceLogoutHandler aceLogoutHandler(RedisTemplate<String, AceUser> userDetailRedisTemplate, RedisTemplate<String, AceOAuth2AccessToken> tokenRedisTemplate, ObjectMapper objectMapper) {
        return new AceLogoutHandler(userDetailRedisTemplate, tokenRedisTemplate, objectMapper);
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

    @ConditionalOnMissingBean
    @Bean
    public CintSecurity cintSecurity() {
        return new CintSecurity();
    }
}
