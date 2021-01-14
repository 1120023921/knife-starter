package com.cintsoft.spring.security.oauth.service.impl;

import com.cintsoft.common.exception.BusinessException;
import com.cintsoft.mybatis.plus.tenant.TenantContextHolder;
import com.cintsoft.spring.security.common.constant.AceSecurityConfigProperties;
import com.cintsoft.spring.security.common.constant.SecurityConstants;
import com.cintsoft.spring.security.model.AceOAuth2AccessToken;
import com.cintsoft.spring.security.model.AceUser;
import com.cintsoft.spring.security.oauth.AceOAuthConfigProperties;
import com.cintsoft.spring.security.oauth.common.bean.AceAuthorizeParams;
import com.cintsoft.spring.security.oauth.common.constant.AceOAuthConstant;
import com.cintsoft.spring.security.oauth.common.constant.SysOAuthCode;
import com.cintsoft.spring.security.oauth.model.AceOAuthClientDetails;
import com.cintsoft.spring.security.oauth.service.AceOAuthService;
import com.cintsoft.spring.security.oauth.service.AceOAuthClientDetailsService;
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
public class AceOAuthServiceTenantImpl implements AceOAuthService {

    private final UserDetailsService userDetailsService;
    private final RedisTemplate<String, AceUser> userDetailRedisTemplate;
    private final RedisTemplate<String, AceOAuth2AccessToken> tokenRedisTemplate;
    private final AceSecurityConfigProperties aceSecurityConfigProperties;
    private final AceOAuthConfigProperties aceOAuthConfigProperties;
    private final AuthenticationManager authenticationManager;
    private final AceOAuthClientDetailsService aceOAuthClientDetailsService;

    @SneakyThrows
    @Override
    public String authorize(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session, AceAuthorizeParams aceAuthorizeParams) {
        if (StringUtils.isEmpty(aceAuthorizeParams.getTenantId())) {
            aceAuthorizeParams.setTenantId("1");
        }
        TenantContextHolder.setTenantId(aceAuthorizeParams.getTenantId());

        final String username = (String) session.getAttribute("username");
        final String tenantId = (String) session.getAttribute("tenantId");
        if (StringUtils.isEmpty(username) || !aceAuthorizeParams.getTenantId().equals(tenantId)) {
            model.addAttribute("aceAuthorizeParams", aceAuthorizeParams);
            return "login";
        }

        final AceOAuth2AccessToken aceOAuth2AccessToken = getAceOAuth2AccessToken(username);
        final AceOAuthClientDetails clientDetails = aceOAuthClientDetailsService.getAceOauthClientDetails(aceAuthorizeParams.getClientId());

        if (clientDetails == null || aceOAuth2AccessToken == null) {
            session.setAttribute("errMsg", SysOAuthCode.CLIENT_INFO_ERROR.getBusinessCode().getMsg());
            return "redirect:" + aceOAuthConfigProperties.getLoginPage() + "?grantType=" + aceAuthorizeParams.getGrantType() + "&state=" + aceAuthorizeParams.getState() + "&clientId=" + aceAuthorizeParams.getClientId() + "&tenantId=" + aceAuthorizeParams.getTenantId();
        }

        if (AceOAuthConstant.GRANT_TYPE_CODE.equals(aceAuthorizeParams.getGrantType())) {
            final String code = UUID.randomUUID().toString();
            tokenRedisTemplate.opsForValue().set(String.format(AceOAuthConstant.CODE_PREFIX_TENANT_ID, aceAuthorizeParams.getTenantId(), code), aceOAuth2AccessToken, aceOAuthConfigProperties.getCodeExpire(), TimeUnit.SECONDS);
            response.sendRedirect(String.format(AceOAuthConstant.AUTHORIZATION_CODE_URL_TENANT_ID, clientDetails.getWebServerRedirectUri(), code, aceAuthorizeParams.getTenantId(), aceAuthorizeParams.getState()));
        } else if (AceOAuthConstant.GRANT_TYPE_TOKEN.equals(aceAuthorizeParams.getGrantType())) {
            response.sendRedirect(String.format(AceOAuthConstant.IMPLICIT_REDIRECT_URL_TENANT_ID, clientDetails.getWebServerRedirectUri(), aceOAuth2AccessToken.getValue(), aceAuthorizeParams.getTenantId(), aceAuthorizeParams.getState()));
        }
        return null;
    }

    @Override
    public AceOAuth2AccessToken passwordToken(String clientId, String username, String password) {
        final AceOAuthClientDetails clientDetails = aceOAuthClientDetailsService.getAceOauthClientDetails(clientId);
        if (clientDetails == null) {
            throw new BusinessException(SysOAuthCode.CLIENT_INFO_ERROR.getBusinessCode());
        }
        if (!Arrays.asList(clientDetails.getAuthorizedGrantTypes().split(",")).contains(AceOAuthConstant.GRANT_TYPE_PASSWORD)) {
            throw new BusinessException(SysOAuthCode.AUTHORIZED_GRANT_TYPE_NOE_ALLOW.getBusinessCode());
        }
        return getAceOAuth2AccessToken(username, password);
    }

    @Override
    public AceOAuth2AccessToken clientCredentialsToken(String clientId, String clientSecret) {
        final AceOAuthClientDetails clientDetails = aceOAuthClientDetailsService.getAceOauthClientDetails(clientId);
        if (clientDetails == null) {
            throw new BusinessException(SysOAuthCode.CLIENT_INFO_ERROR.getBusinessCode());
        }
        if (!Arrays.asList(clientDetails.getAuthorizedGrantTypes().split(",")).contains(AceOAuthConstant.GRANT_TYPE_CLIENT_CREDENTIALS)) {
            throw new BusinessException(SysOAuthCode.AUTHORIZED_GRANT_TYPE_NOE_ALLOW.getBusinessCode());
        }
        final AceUser aceUser = aceOAuthClientDetailsService.clientCredentialsAuthorize(clientId, clientSecret);
        return getAceOAuth2AccessToken(aceUser);
    }

    @Override
    public Map<String, Object> token(AceAuthorizeParams aceAuthorizeParams) {
        AceOAuth2AccessToken aceOAuth2AccessToken = null;
        if (AceOAuthConstant.GRANT_TYPE_CODE.equals(aceAuthorizeParams.getGrantType())) {
            aceOAuth2AccessToken = tokenRedisTemplate.opsForValue().get(String.format(AceOAuthConstant.CODE_PREFIX_TENANT_ID, aceAuthorizeParams.getTenantId(), aceAuthorizeParams.getCode()));
            if (aceOAuth2AccessToken != null) {
                tokenRedisTemplate.delete(String.format(AceOAuthConstant.CODE_PREFIX_TENANT_ID, aceAuthorizeParams.getTenantId(), aceAuthorizeParams.getCode()));
            }
        } else if (AceOAuthConstant.GRANT_TYPE_PASSWORD.equals(aceAuthorizeParams.getGrantType())) {
            aceOAuth2AccessToken = passwordToken(aceAuthorizeParams.getClientId(), aceAuthorizeParams.getUsername(), aceAuthorizeParams.getPassword());
        } else if (AceOAuthConstant.GRANT_TYPE_CLIENT_CREDENTIALS.equals(aceAuthorizeParams.getGrantType())) {
            aceOAuth2AccessToken = clientCredentialsToken(aceAuthorizeParams.getClientId(), aceAuthorizeParams.getClientSecret());
        }
        return aceOAuth2AccessToken == null ? null : aceOAuth2AccessToken.getTokenMap();
    }

    @Override
    public String login(HttpServletRequest request, HttpServletResponse response, HttpSession session, AceAuthorizeParams aceAuthorizeParams) {
        if (StringUtils.hasText(aceAuthorizeParams.getTenantId())) {
            TenantContextHolder.setTenantId(aceAuthorizeParams.getTenantId());
        }
        final AceOAuthClientDetails clientDetails = aceOAuthClientDetailsService.getAceOauthClientDetails(aceAuthorizeParams.getClientId());
        if (clientDetails == null) {
            session.setAttribute("errMsg", SysOAuthCode.CLIENT_INFO_ERROR.getBusinessCode().getMsg());
            return "redirect:" + aceOAuthConfigProperties.getLoginPage() + "?grantType=" + aceAuthorizeParams.getGrantType() + "&state=" + aceAuthorizeParams.getState() + "&clientId=" + aceAuthorizeParams.getClientId() + "&tenantId=" + aceAuthorizeParams.getTenantId();
        }
        if (!Arrays.asList(clientDetails.getAuthorizedGrantTypes().split(",")).contains(aceAuthorizeParams.getGrantType())) {
            session.setAttribute("errMsg", SysOAuthCode.AUTHORIZED_GRANT_TYPE_NOE_ALLOW.getBusinessCode().getMsg());
            return "redirect:" + aceOAuthConfigProperties.getLoginPage() + "?grantType=" + aceAuthorizeParams.getGrantType() + "&state=" + aceAuthorizeParams.getState() + "&clientId=" + aceAuthorizeParams.getClientId() + "&tenantId=" + aceAuthorizeParams.getTenantId();
        }
        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(aceAuthorizeParams.getUsername(), aceAuthorizeParams.getPassword());
        try {
            final Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            if (authenticate != null) {
                final AceUser aceUser = (AceUser) authenticate.getPrincipal();
                session.setAttribute("username", aceUser.getUsername());
                session.setAttribute("tenantId", aceAuthorizeParams.getTenantId());
                session.removeAttribute("errMsg");
            }
        } catch (AuthenticationException e) {
            session.setAttribute("errMsg", "认证失败");
        }
        return "redirect:" + aceOAuthConfigProperties.getLoginPage() + "?grantType=" + aceAuthorizeParams.getGrantType() + "&state=" + aceAuthorizeParams.getState() + "&clientId=" + aceAuthorizeParams.getClientId() + "&tenantId=" + aceAuthorizeParams.getTenantId();
    }

    @Override
    public AceUser userInfo(String token, String tenantId) {
        return userDetailRedisTemplate.opsForValue().get(String.format(SecurityConstants.USER_DETAIL_PREFIX_TENANT_ID, tenantId, token));
    }

    @Override
    public void logout(String username, String tenantId) {
        final AceOAuth2AccessToken aceOAuth2AccessToken = tokenRedisTemplate.opsForValue().get(String.format(SecurityConstants.TOKEN_PREFIX_TENANT_ID, tenantId, username));
        if (aceOAuth2AccessToken != null) {
            tokenRedisTemplate.delete(String.format(SecurityConstants.TOKEN_PREFIX_TENANT_ID, tenantId, username));
            userDetailRedisTemplate.delete(String.format(SecurityConstants.USER_DETAIL_PREFIX_TENANT_ID, tenantId, aceOAuth2AccessToken.getValue()));
        }
    }

    private AceOAuth2AccessToken getAceOAuth2AccessToken(String username, String password) {
        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        final Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if (authenticate != null) {
            final AceUser aceUser = (AceUser) authenticate.getPrincipal();
            return getAceOAuth2AccessToken(aceUser);
        }
        throw new BadCredentialsException("用户名密码错误");
    }

    private AceOAuth2AccessToken getAceOAuth2AccessToken(String username) {
        final AceUser aceUser = (AceUser) userDetailsService.loadUserByUsername(username);
        if (aceUser != null) {
            return getAceOAuth2AccessToken(aceUser);
        }
        throw new BadCredentialsException("用户名密码错误");
    }

    public AceOAuth2AccessToken getAceOAuth2AccessToken(AceUser aceUser) {
        //判断当前用户是否已有token
        AceOAuth2AccessToken aceOAuth2AccessToken = tokenRedisTemplate.opsForValue().get(String.format(SecurityConstants.TOKEN_PREFIX_TENANT_ID, aceUser.getTenantId(), aceUser.getUsername()));
        String token;
        if (aceOAuth2AccessToken == null) {
            token = UUID.randomUUID().toString();
        } else {
            token = aceOAuth2AccessToken.getValue();
        }
        userDetailRedisTemplate.opsForValue().set(String.format(SecurityConstants.USER_DETAIL_PREFIX_TENANT_ID, aceUser.getTenantId(), token), aceUser, aceSecurityConfigProperties.getTokenExpire(), TimeUnit.SECONDS);
        aceOAuth2AccessToken = new AceOAuth2AccessToken();
        aceOAuth2AccessToken.setValue(token);
        aceOAuth2AccessToken.setExpiration(new Date(System.currentTimeMillis() + aceSecurityConfigProperties.getTokenExpire() * 1000L));
        tokenRedisTemplate.opsForValue().set(String.format(SecurityConstants.TOKEN_PREFIX_TENANT_ID, aceUser.getTenantId(), aceUser.getUsername()), aceOAuth2AccessToken, aceSecurityConfigProperties.getTokenExpire(), TimeUnit.SECONDS);
        return aceOAuth2AccessToken;
    }
}
