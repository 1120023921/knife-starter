package com.cintsoft.spring.security.oauth.service.impl;

import com.cintsoft.common.exception.BusinessException;
import com.cintsoft.common.mybatis.tenant.TenantContextHolder;
import com.cintsoft.spring.security.common.constant.KnifeSecurityConfigProperties;
import com.cintsoft.spring.security.model.KnifeOAuth2AccessToken;
import com.cintsoft.spring.security.model.KnifeUser;
import com.cintsoft.spring.security.oauth.KnifeOAuthConfigProperties;
import com.cintsoft.spring.security.oauth.common.bean.KnifeAuthorizeParams;
import com.cintsoft.spring.security.oauth.common.constant.KnifeOAuthConstant;
import com.cintsoft.spring.security.oauth.common.constant.SysOAuthCode;
import com.cintsoft.spring.security.oauth.model.KnifeOAuthClientDetails;
import com.cintsoft.spring.security.oauth.service.KnifeOAuthService;
import com.cintsoft.spring.security.oauth.service.KnifeOAuthClientDetailsService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author 胡昊
 * Description:
 * Date: 2021/1/11
 * Time: 21:35
 * Mail: huhao9277@gmail.com
 */
@Slf4j
@AllArgsConstructor
public class KnifeOAuthServiceTenantImpl implements KnifeOAuthService {

    private final UserDetailsService userDetailsService;
    private final RedisTemplate<String, KnifeUser> userDetailRedisTemplate;
    private final RedisTemplate<String, KnifeOAuth2AccessToken> tokenRedisTemplate;
    private final KnifeSecurityConfigProperties knifeSecurityConfigProperties;
    private final KnifeOAuthConfigProperties knifeOAuthConfigProperties;
    private final AuthenticationManager authenticationManager;
    private final KnifeOAuthClientDetailsService knifeOAuthClientDetailsService;

    @SneakyThrows
    @Override
    public String authorize(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session, KnifeAuthorizeParams knifeAuthorizeParams) {
        if (StringUtils.isEmpty(knifeAuthorizeParams.getTenantId())) {
            knifeAuthorizeParams.setTenantId("0");
        }
        TenantContextHolder.setTenantId(knifeAuthorizeParams.getTenantId());

        final String username = (String) session.getAttribute("username");
        final String tenantId = (String) session.getAttribute("tenantId");
        if (StringUtils.isEmpty(username) || !knifeAuthorizeParams.getTenantId().equals(tenantId)) {
            model.addAttribute("knifeAuthorizeParams", knifeAuthorizeParams);
            return "login";
        }

        final KnifeOAuthClientDetails clientDetails = knifeOAuthClientDetailsService.getKnifeOauthClientDetails(knifeAuthorizeParams.getClientId());
        final KnifeOAuth2AccessToken knifeOAuth2AccessToken = getKnifeOAuth2AccessToken(clientDetails, username);

        if (clientDetails == null || knifeOAuth2AccessToken == null) {
            session.setAttribute("errMsg", SysOAuthCode.CLIENT_INFO_ERROR.getBusinessCode().getMsg());
            return "redirect:" + knifeOAuthConfigProperties.getLoginPage() + "?grantType=" + knifeAuthorizeParams.getGrantType() + "&state=" + knifeAuthorizeParams.getState() + "&clientId=" + knifeAuthorizeParams.getClientId() + "&tenantId=" + knifeAuthorizeParams.getTenantId();
        }

        if (KnifeOAuthConstant.GRANT_TYPE_CODE.equals(knifeAuthorizeParams.getGrantType())) {
            final String code = UUID.randomUUID().toString();
            tokenRedisTemplate.opsForValue().set(String.format(knifeOAuthConfigProperties.getCodePrefixTenantId(), knifeAuthorizeParams.getTenantId(), code), knifeOAuth2AccessToken, knifeOAuthConfigProperties.getCodeExpire(), TimeUnit.SECONDS);
            response.sendRedirect(String.format(KnifeOAuthConstant.AUTHORIZATION_CODE_URL_TENANT_ID, clientDetails.getWebServerRedirectUri(), code, knifeAuthorizeParams.getTenantId(), knifeAuthorizeParams.getState()));
        } else if (KnifeOAuthConstant.GRANT_TYPE_TOKEN.equals(knifeAuthorizeParams.getGrantType())) {
            response.sendRedirect(String.format(KnifeOAuthConstant.IMPLICIT_REDIRECT_URL_TENANT_ID, clientDetails.getWebServerRedirectUri(), knifeOAuth2AccessToken.getValue(), knifeAuthorizeParams.getTenantId(), knifeAuthorizeParams.getState()));
        }
        return null;
    }

    @Override
    public KnifeOAuth2AccessToken passwordToken(String clientId, String username, String password) {
        final KnifeOAuthClientDetails clientDetails = knifeOAuthClientDetailsService.getKnifeOauthClientDetails(clientId);
        if (clientDetails == null) {
            throw new BusinessException(SysOAuthCode.CLIENT_INFO_ERROR.getBusinessCode());
        }
        if (!Arrays.asList(clientDetails.getAuthorizedGrantTypes().split(",")).contains(KnifeOAuthConstant.GRANT_TYPE_PASSWORD)) {
            throw new BusinessException(SysOAuthCode.AUTHORIZED_GRANT_TYPE_NOE_ALLOW.getBusinessCode());
        }
        return getKnifeOAuth2AccessToken(clientDetails, username, password);
    }

    @Override
    public KnifeOAuth2AccessToken clientCredentialsToken(String clientId, String clientSecret) {
        final KnifeOAuthClientDetails clientDetails = knifeOAuthClientDetailsService.getKnifeOauthClientDetails(clientId);
        if (clientDetails == null) {
            throw new BusinessException(SysOAuthCode.CLIENT_INFO_ERROR.getBusinessCode());
        }
        if (!Arrays.asList(clientDetails.getAuthorizedGrantTypes().split(",")).contains(KnifeOAuthConstant.GRANT_TYPE_CLIENT_CREDENTIALS)) {
            throw new BusinessException(SysOAuthCode.AUTHORIZED_GRANT_TYPE_NOE_ALLOW.getBusinessCode());
        }
        final KnifeUser knifeUser = knifeOAuthClientDetailsService.clientCredentialsAuthorize(clientId, clientSecret);
        return getKnifeOAuth2AccessToken(clientDetails, knifeUser);
    }

    @Override
    public KnifeOAuth2AccessToken refreshToken(KnifeAuthorizeParams knifeAuthorizeParams) {
        final KnifeOAuthClientDetails clientDetails = knifeOAuthClientDetailsService.getKnifeOauthClientDetails(knifeAuthorizeParams.getClientId());
        if (clientDetails == null) {
            throw new BusinessException(SysOAuthCode.CLIENT_INFO_ERROR.getBusinessCode());
        }
        if (!Arrays.asList(clientDetails.getAuthorizedGrantTypes().split(",")).contains(KnifeOAuthConstant.GRANT_TYPE_PASSWORD)) {
            throw new BusinessException(SysOAuthCode.AUTHORIZED_GRANT_TYPE_NOE_ALLOW.getBusinessCode());
        }
        final KnifeUser knifeUser = userDetailRedisTemplate.opsForValue().get(String.format(knifeSecurityConfigProperties.getRefreshTokenPrefixTenantId(), knifeAuthorizeParams.getTenantId(), knifeAuthorizeParams.getRefreshToken()));
        if (knifeUser == null) {
            return null;
        }
        userDetailRedisTemplate.delete(String.format(knifeSecurityConfigProperties.getRefreshTokenPrefixTenantId(), knifeAuthorizeParams.getTenantId(), knifeAuthorizeParams.getRefreshToken()));
        return getKnifeOAuth2AccessToken(clientDetails, knifeUser);
    }

    @Override
    public Map<String, Object> token(KnifeAuthorizeParams knifeAuthorizeParams) {
        KnifeOAuth2AccessToken knifeOAuth2AccessToken = null;
        if (KnifeOAuthConstant.GRANT_TYPE_CODE.equals(knifeAuthorizeParams.getGrantType())) {
            knifeOAuth2AccessToken = tokenRedisTemplate.opsForValue().get(String.format(knifeOAuthConfigProperties.getCodePrefixTenantId(), knifeAuthorizeParams.getTenantId(), knifeAuthorizeParams.getCode()));
            if (knifeOAuth2AccessToken != null) {
                tokenRedisTemplate.delete(String.format(knifeOAuthConfigProperties.getCodePrefixTenantId(), knifeAuthorizeParams.getTenantId(), knifeAuthorizeParams.getCode()));
            }
        } else if (KnifeOAuthConstant.GRANT_TYPE_PASSWORD.equals(knifeAuthorizeParams.getGrantType())) {
            knifeOAuth2AccessToken = passwordToken(knifeAuthorizeParams.getClientId(), knifeAuthorizeParams.getUsername(), knifeAuthorizeParams.getPassword());
        } else if (KnifeOAuthConstant.GRANT_TYPE_CLIENT_CREDENTIALS.equals(knifeAuthorizeParams.getGrantType())) {
            knifeOAuth2AccessToken = clientCredentialsToken(knifeAuthorizeParams.getClientId(), knifeAuthorizeParams.getClientSecret());
        } else if (KnifeOAuthConstant.GRANT_TYPE_REFRESH_TOKEN.equals(knifeAuthorizeParams.getGrantType())) {
            knifeOAuth2AccessToken = refreshToken(knifeAuthorizeParams);
        }
        return knifeOAuth2AccessToken == null ? null : knifeOAuth2AccessToken.getTokenMap();
    }

    @Override
    public String login(HttpServletRequest request, HttpServletResponse response, HttpSession session, KnifeAuthorizeParams knifeAuthorizeParams) {
        if (StringUtils.hasText(knifeAuthorizeParams.getTenantId())) {
            TenantContextHolder.setTenantId(knifeAuthorizeParams.getTenantId());
        }
        final KnifeOAuthClientDetails clientDetails = knifeOAuthClientDetailsService.getKnifeOauthClientDetails(knifeAuthorizeParams.getClientId());
        if (clientDetails == null) {
            session.setAttribute("errMsg", SysOAuthCode.CLIENT_INFO_ERROR.getBusinessCode().getMsg());
            return "redirect:" + knifeOAuthConfigProperties.getLoginPage() + "?grantType=" + knifeAuthorizeParams.getGrantType() + "&state=" + knifeAuthorizeParams.getState() + "&clientId=" + knifeAuthorizeParams.getClientId() + "&tenantId=" + knifeAuthorizeParams.getTenantId();
        }
        if (!Arrays.asList(clientDetails.getAuthorizedGrantTypes().split(",")).contains(knifeAuthorizeParams.getGrantType())) {
            session.setAttribute("errMsg", SysOAuthCode.AUTHORIZED_GRANT_TYPE_NOE_ALLOW.getBusinessCode().getMsg());
            return "redirect:" + knifeOAuthConfigProperties.getLoginPage() + "?grantType=" + knifeAuthorizeParams.getGrantType() + "&state=" + knifeAuthorizeParams.getState() + "&clientId=" + knifeAuthorizeParams.getClientId() + "&tenantId=" + knifeAuthorizeParams.getTenantId();
        }
        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(knifeAuthorizeParams.getUsername(), knifeAuthorizeParams.getPassword());
        try {
            final Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            if (authenticate != null) {
                final KnifeUser knifeUser = (KnifeUser) authenticate.getPrincipal();
                session.setAttribute("username", knifeUser.getUsername());
                session.setAttribute("tenantId", knifeAuthorizeParams.getTenantId());
                session.removeAttribute("errMsg");
            }
        } catch (AuthenticationException e) {
            session.setAttribute("errMsg", "认证失败");
        }
        return "redirect:" + knifeOAuthConfigProperties.getLoginPage() + "?grantType=" + knifeAuthorizeParams.getGrantType() + "&state=" + knifeAuthorizeParams.getState() + "&clientId=" + knifeAuthorizeParams.getClientId() + "&tenantId=" + knifeAuthorizeParams.getTenantId();
    }

    @Override
    public KnifeUser userInfo(String token, String tenantId) {
        return userDetailRedisTemplate.opsForValue().get(String.format(knifeSecurityConfigProperties.getUserDetailPrefixTenantId(), tenantId, token));
    }

    @Override
    public void logout(String username, String token, String tenantId) {
        KnifeOAuth2AccessToken knifeOAuth2AccessToken = null;
        if (!StringUtils.isEmpty(username)) {
            knifeOAuth2AccessToken = tokenRedisTemplate.opsForValue().get(String.format(knifeSecurityConfigProperties.getAccessTokenPrefixTenantId(), tenantId, username));
        } else {
            if (!StringUtils.isEmpty(token)) {
                final KnifeUser knifeUser = userInfo(token, tenantId);
                username = knifeUser.getUsername();
                knifeOAuth2AccessToken = tokenRedisTemplate.opsForValue().get(String.format(knifeSecurityConfigProperties.getAccessTokenPrefixTenantId(), tenantId, knifeUser.getUsername()));
            }
        }
        if (knifeOAuth2AccessToken != null) {
            tokenRedisTemplate.delete(String.format(knifeSecurityConfigProperties.getAccessTokenPrefixTenantId(), tenantId, username));
            userDetailRedisTemplate.delete(String.format(knifeSecurityConfigProperties.getUserDetailPrefixTenantId(), tenantId, knifeOAuth2AccessToken.getValue()));
        }
    }

    private KnifeOAuth2AccessToken getKnifeOAuth2AccessToken(KnifeOAuthClientDetails clientDetails, String username, String password) {
        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        final Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if (authenticate != null) {
            final KnifeUser knifeUser = (KnifeUser) authenticate.getPrincipal();
            return getKnifeOAuth2AccessToken(clientDetails, knifeUser);
        }
        throw new BadCredentialsException("用户名密码错误");
    }

    private KnifeOAuth2AccessToken getKnifeOAuth2AccessToken(KnifeOAuthClientDetails clientDetails, String username) {
        final KnifeUser knifeUser = (KnifeUser) userDetailsService.loadUserByUsername(username);
        if (knifeUser != null) {
            return getKnifeOAuth2AccessToken(clientDetails, knifeUser);
        }
        throw new BadCredentialsException("用户名密码错误");
    }

    public KnifeOAuth2AccessToken getKnifeOAuth2AccessToken(KnifeOAuthClientDetails clientDetails, KnifeUser knifeUser) {
        //判断当前用户是否已有token
        KnifeOAuth2AccessToken knifeOAuth2AccessToken = tokenRedisTemplate.opsForValue().get(String.format(knifeSecurityConfigProperties.getAccessTokenPrefixTenantId(), knifeUser.getTenantId(), knifeUser.getUsername()));
        String token;
        if (knifeOAuth2AccessToken == null) {
            token = UUID.randomUUID().toString();
        } else {
            token = knifeOAuth2AccessToken.getValue();
        }
        final String refreshToken = UUID.randomUUID().toString();
        userDetailRedisTemplate.opsForValue().set(String.format(knifeSecurityConfigProperties.getUserDetailPrefixTenantId(), knifeUser.getTenantId(), token), knifeUser, clientDetails.getAccessTokenValidity(), TimeUnit.SECONDS);
        knifeOAuth2AccessToken = new KnifeOAuth2AccessToken();
        knifeOAuth2AccessToken.setValue(token);
        knifeOAuth2AccessToken.setRefreshToken(refreshToken);
        knifeOAuth2AccessToken.setExpiration(new Date(System.currentTimeMillis() + clientDetails.getAccessTokenValidity() * 1000L));
        tokenRedisTemplate.opsForValue().set(String.format(knifeSecurityConfigProperties.getAccessTokenPrefixTenantId(), knifeUser.getTenantId(), knifeUser.getUsername()), knifeOAuth2AccessToken, clientDetails.getAccessTokenValidity(), TimeUnit.SECONDS);
        userDetailRedisTemplate.opsForValue().set(String.format(knifeSecurityConfigProperties.getRefreshTokenPrefixTenantId(), knifeUser.getTenantId(), refreshToken), knifeUser, clientDetails.getRefreshTokenValidity(), TimeUnit.SECONDS);
        return knifeOAuth2AccessToken;
    }
}
