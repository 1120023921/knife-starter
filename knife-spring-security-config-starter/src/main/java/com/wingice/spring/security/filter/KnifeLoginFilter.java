package com.wingice.spring.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wingice.common.web.ErrorCodeInfo;
import com.wingice.common.web.ResultBean;
import com.wingice.spring.security.common.constant.KnifeSecurityConfigProperties;
import com.wingice.spring.security.exception.KnifeAuthenticationException;
import com.wingice.spring.security.model.KnifeOAuth2AccessToken;
import com.wingice.spring.security.model.KnifeUser;
import com.wingice.spring.security.service.CaptchaService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author 胡昊
 * Description: Jwt认证过滤器
 * Date: 2020/7/23
 * Time: 16:10
 * Mail: huhao9277@gmail.com
 */
public class KnifeLoginFilter extends AbstractAuthenticationProcessingFilter {


    private final RedisTemplate<String, KnifeUser> userDetailRedisTemplate;
    private final RedisTemplate<String, KnifeOAuth2AccessToken> tokenRedisTemplate;
    private final RedisTemplate<String, String> userRefreshTokenRedisTemplate;
    private final KnifeSecurityConfigProperties knifeSecurityConfigProperties;
    private final ObjectMapper objectMapper;
    private final Map<String, CaptchaService> captchaServiceMap;

    public KnifeLoginFilter(AuthenticationManager authenticationManager, RedisTemplate<String, KnifeUser> userDetailRedisTemplate, RedisTemplate<String, KnifeOAuth2AccessToken> tokenRedisTemplate, RedisTemplate<String, String> userRefreshTokenRedisTemplate, KnifeSecurityConfigProperties knifeSecurityConfigProperties, ObjectMapper objectMapper, Map<String, CaptchaService> captchaServiceMap) {
        super(new AntPathRequestMatcher("/login", "POST"));
        setAuthenticationManager(authenticationManager);
        this.userDetailRedisTemplate = userDetailRedisTemplate;
        this.tokenRedisTemplate = tokenRedisTemplate;
        this.userRefreshTokenRedisTemplate = userRefreshTokenRedisTemplate;
        this.knifeSecurityConfigProperties = knifeSecurityConfigProperties;
        this.objectMapper = objectMapper;
        this.captchaServiceMap = captchaServiceMap;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        final String username = request.getParameter("username");
        final String password = request.getParameter("password");
        final String captchaKey = request.getParameter("captchaKey");
        final String captchaCode = request.getParameter("captchaCode");
        if (knifeSecurityConfigProperties.getCaptchaEnable()) {
            final CaptchaService captchaService = captchaServiceMap.get(knifeSecurityConfigProperties.getCaptchaMode());
            if (!captchaService.verifyCaptcha(captchaKey, captchaCode)) {
                throw new KnifeAuthenticationException("验证码错误");
            }
        }
        final Authentication authRequest = new UsernamePasswordAuthenticationToken(username, password);
        return getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        final KnifeUser knifeUser = (KnifeUser) authResult.getPrincipal();
        //判断当前用户是否已有token
        KnifeOAuth2AccessToken knifeOAuth2AccessToken = tokenRedisTemplate.opsForValue().get(String.format(knifeSecurityConfigProperties.getAccessTokenPrefix(), knifeUser.getUsername()));
        if (knifeOAuth2AccessToken == null) {
            final String token = UUID.randomUUID().toString();
            userDetailRedisTemplate.opsForValue().set(String.format(knifeSecurityConfigProperties.getUserDetailPrefix(), token), knifeUser, knifeSecurityConfigProperties.getTokenExpire(), TimeUnit.SECONDS);
            knifeOAuth2AccessToken = new KnifeOAuth2AccessToken();
            knifeOAuth2AccessToken.setValue(token);
            knifeOAuth2AccessToken.setExpiration(new Date(System.currentTimeMillis() + knifeSecurityConfigProperties.getTokenExpire() * 1000L));
        }
        String refreshToken = userRefreshTokenRedisTemplate.opsForValue().get(String.format(knifeSecurityConfigProperties.getUserRefreshTokenPrefix(), knifeUser.getUsername()));
        if (StringUtils.isEmpty(refreshToken)) {
            refreshToken = UUID.randomUUID().toString();
        }
        knifeOAuth2AccessToken.setRefreshToken(refreshToken);
        tokenRedisTemplate.opsForValue().set(String.format(knifeSecurityConfigProperties.getAccessTokenPrefix(), knifeUser.getUsername()), knifeOAuth2AccessToken, knifeSecurityConfigProperties.getTokenExpire(), TimeUnit.SECONDS);
        userDetailRedisTemplate.opsForValue().set(String.format(knifeSecurityConfigProperties.getUserDetailPrefix(), knifeOAuth2AccessToken.getValue()), knifeUser, knifeSecurityConfigProperties.getTokenExpire(), TimeUnit.SECONDS);
        userRefreshTokenRedisTemplate.opsForValue().set(String.format(knifeSecurityConfigProperties.getUserRefreshTokenPrefix(), knifeUser.getUsername()), refreshToken, knifeSecurityConfigProperties.getTokenExpire(), TimeUnit.SECONDS);

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "application/json");
        final PrintWriter out = response.getWriter();
        out.write(objectMapper.writeValueAsString(ResultBean.restResult(knifeOAuth2AccessToken.getTokenMap(), ErrorCodeInfo.OK)));
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
        } else if (failed instanceof KnifeAuthenticationException) {
            out.write(objectMapper.writeValueAsString(ResultBean.restResult(failed.getMessage(), ((KnifeAuthenticationException) failed).getCode(), ((KnifeAuthenticationException) failed).getMsg())));
        }
        out.flush();
        out.close();
    }
}
