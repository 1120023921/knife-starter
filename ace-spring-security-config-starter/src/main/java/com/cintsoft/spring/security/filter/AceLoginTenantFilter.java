package com.cintsoft.spring.security.filter;

import com.cintsoft.common.web.ResultBean;
import com.cintsoft.spring.security.common.constant.AceSecurityConfigProperties;
import com.cintsoft.spring.security.common.constant.SecurityConstants;
import com.cintsoft.spring.security.model.AceOAuth2AccessToken;
import com.cintsoft.spring.security.model.AceUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author 胡昊
 * Description: Jwt认证过滤器
 * Date: 2020/7/23
 * Time: 16:10
 * Mail: huhao9277@gmail.com
 */
public class AceLoginTenantFilter extends AbstractAuthenticationProcessingFilter {


    private final RedisTemplate<String, AceUser> userDetailRedisTemplate;
    private final RedisTemplate<String, AceOAuth2AccessToken> tokenRedisTemplate;
    private final AceSecurityConfigProperties aceSecurityConfigProperties;
    private final ObjectMapper objectMapper;

    public AceLoginTenantFilter(AuthenticationManager authenticationManager, RedisTemplate<String, AceUser> userDetailRedisTemplate, RedisTemplate<String, AceOAuth2AccessToken> tokenRedisTemplate, AceSecurityConfigProperties aceSecurityConfigProperties, ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher("/login", "POST"));
        setAuthenticationManager(authenticationManager);
        this.userDetailRedisTemplate = userDetailRedisTemplate;
        this.tokenRedisTemplate = tokenRedisTemplate;
        this.aceSecurityConfigProperties = aceSecurityConfigProperties;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        final String username = request.getParameter("username");
        final String password = request.getParameter("password");
        final Authentication authRequest = new UsernamePasswordAuthenticationToken(username, password);
        return getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        final AceUser aceUser = (AceUser) authResult.getPrincipal();
        //判断当前用户是否已有token
        AceOAuth2AccessToken aceOAuth2AccessToken = tokenRedisTemplate.opsForValue().get(String.format(SecurityConstants.TOKEN_PREFIX_TENANT_ID, aceUser.getTenantId(), aceUser.getUsername()));
        if (aceOAuth2AccessToken == null) {
            final String token = UUID.randomUUID().toString();
            userDetailRedisTemplate.opsForValue().set(String.format(SecurityConstants.USER_DETAIL_PREFIX_TENANT_ID, aceUser.getTenantId(), token), aceUser, aceSecurityConfigProperties.getTokenExpire(), TimeUnit.SECONDS);
            aceOAuth2AccessToken = new AceOAuth2AccessToken();
            aceOAuth2AccessToken.setValue(token);
            aceOAuth2AccessToken.setExpiration(new Date(System.currentTimeMillis() + aceSecurityConfigProperties.getTokenExpire()*1000L));
            tokenRedisTemplate.opsForValue().set(String.format(SecurityConstants.TOKEN_PREFIX_TENANT_ID, aceUser.getTenantId(), aceUser.getUsername()), aceOAuth2AccessToken, aceSecurityConfigProperties.getTokenExpire(), TimeUnit.SECONDS);
        }
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "application/json");
        final PrintWriter out = response.getWriter();
        out.write(objectMapper.writeValueAsString(aceOAuth2AccessToken.getTokenMap()));
        out.flush();
        out.close();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "application/json");
        final PrintWriter out = response.getWriter();
        if (failed instanceof BadCredentialsException || failed instanceof ProviderNotFoundException) {
            out.write(objectMapper.writeValueAsString(ResultBean.restResult(failed.getMessage(), 500, "用户名密码错误")));
        } else if (failed instanceof LockedException) {
            out.write(objectMapper.writeValueAsString(ResultBean.restResult(failed.getMessage(), 500, "账户已锁定")));
        } else if (failed instanceof DisabledException) {
            out.write(objectMapper.writeValueAsString(ResultBean.restResult(failed.getMessage(), 500, "账户已禁用")));
        } else if (failed instanceof AccountExpiredException) {
            out.write(objectMapper.writeValueAsString(ResultBean.restResult(failed.getMessage(), 500, "用户名已过期")));
        } else if (failed instanceof CredentialsExpiredException) {
            out.write(objectMapper.writeValueAsString(ResultBean.restResult(failed.getMessage(), 500, "密码已过期")));
        }
        out.flush();
        out.close();
    }
}
