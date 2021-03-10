package com.cintsoft.spring.security.filter;

import com.cintsoft.common.web.ResultBean;
import com.cintsoft.spring.security.common.constant.KnifeSecurityConfigProperties;
import com.cintsoft.spring.security.common.constant.SecurityConstants;
import com.cintsoft.spring.security.model.KnifeOAuth2AccessToken;
import com.cintsoft.spring.security.model.KnifeSocialAuthenticationToken;
import com.cintsoft.spring.security.model.KnifeUser;
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
public class KnifeSocialLoginTenantFilter extends AbstractAuthenticationProcessingFilter {


    private final RedisTemplate<String, KnifeUser> userDetailRedisTemplate;
    private final RedisTemplate<String, KnifeOAuth2AccessToken> tokenRedisTemplate;
    private final KnifeSecurityConfigProperties knifeSecurityConfigProperties;
    private final ObjectMapper objectMapper;

    public KnifeSocialLoginTenantFilter(AuthenticationManager authenticationManager, RedisTemplate<String, KnifeUser> userDetailRedisTemplate, RedisTemplate<String, KnifeOAuth2AccessToken> tokenRedisTemplate, KnifeSecurityConfigProperties knifeSecurityConfigProperties, ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher("/social/login", "POST"));
        setAuthenticationManager(authenticationManager);
        this.userDetailRedisTemplate = userDetailRedisTemplate;
        this.tokenRedisTemplate = tokenRedisTemplate;
        this.knifeSecurityConfigProperties = knifeSecurityConfigProperties;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        final String source = request.getParameter("source");
        final String code = request.getParameter("code");
        final Authentication authRequest = new KnifeSocialAuthenticationToken(source, code);
        return getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        final KnifeUser knifeUser = (KnifeUser) authResult.getPrincipal();
        //判断当前用户是否已有token
        KnifeOAuth2AccessToken knifeOAuth2AccessToken = tokenRedisTemplate.opsForValue().get(String.format(SecurityConstants.ACCESS_TOKEN_PREFIX_TENANT_ID, knifeUser.getTenantId(), knifeUser.getUsername()));
        if (knifeOAuth2AccessToken == null) {
            final String token = UUID.randomUUID().toString();
            userDetailRedisTemplate.opsForValue().set(String.format(SecurityConstants.USER_DETAIL_PREFIX_TENANT_ID, knifeUser.getTenantId(), token), knifeUser, knifeSecurityConfigProperties.getTokenExpire(), TimeUnit.SECONDS);
            knifeOAuth2AccessToken = new KnifeOAuth2AccessToken();
            knifeOAuth2AccessToken.setValue(token);
            knifeOAuth2AccessToken.setExpiration(new Date(System.currentTimeMillis() + knifeSecurityConfigProperties.getTokenExpire()*1000L));
            tokenRedisTemplate.opsForValue().set(String.format(SecurityConstants.ACCESS_TOKEN_PREFIX_TENANT_ID, knifeUser.getTenantId(), knifeUser.getUsername()), knifeOAuth2AccessToken, knifeSecurityConfigProperties.getTokenExpire(), TimeUnit.SECONDS);
        }
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "application/json");
        final PrintWriter out = response.getWriter();
        out.write(objectMapper.writeValueAsString(knifeOAuth2AccessToken.getTokenMap()));
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