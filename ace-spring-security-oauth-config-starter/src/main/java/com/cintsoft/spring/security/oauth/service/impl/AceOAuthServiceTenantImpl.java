package com.cintsoft.spring.security.oauth.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cintsoft.common.exception.BusinessException;
import com.cintsoft.common.web.ErrorCodeInfo;
import com.cintsoft.common.web.ResultBean;
import com.cintsoft.mybatis.plus.tenant.TenantContextHolder;
import com.cintsoft.spring.security.common.constant.AceSecurityConfigProperties;
import com.cintsoft.spring.security.common.constant.SecurityConstants;
import com.cintsoft.spring.security.model.AceOAuth2AccessToken;
import com.cintsoft.spring.security.model.AceUser;
import com.cintsoft.spring.security.oauth.AceOAuthConfigProperties;
import com.cintsoft.spring.security.oauth.common.bean.AceAuthorizeParams;
import com.cintsoft.spring.security.oauth.common.constant.AceOAuthConstant;
import com.cintsoft.spring.security.oauth.common.constant.SysOAuthCode;
import com.cintsoft.spring.security.oauth.model.SysOauthClientDetails;
import com.cintsoft.spring.security.oauth.service.AceOAuthService;
import com.cintsoft.spring.security.oauth.service.SysOauthClientDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.Arrays;
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
    private final SysOauthClientDetailsService sysOauthClientDetailsService;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public String authorize(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session, AceAuthorizeParams aceAuthorizeParams) {
        if (StringUtils.isEmpty(aceAuthorizeParams.tenantId)) {
            aceAuthorizeParams.tenantId = "1";
        }
        TenantContextHolder.setTenantId(aceAuthorizeParams.tenantId);

        final String username = (String) session.getAttribute("username");
        final String tenantId = (String) session.getAttribute("tenantId");
        if (StringUtils.isEmpty(username) || !aceAuthorizeParams.tenantId.equals(tenantId)) {
            model.addAttribute("aceAuthorizeParams", aceAuthorizeParams);
            return "login";
        }

        AceOAuth2AccessToken aceOAuth2AccessToken = null;
        SysOauthClientDetails clientDetails = null;


        if (AceOAuthConstant.GRANT_TYPE_CODE.equals(aceAuthorizeParams.responseType) || AceOAuthConstant.GRANT_TYPE_TOKEN.equals(aceAuthorizeParams.responseType)) {
            aceOAuth2AccessToken = getAceOAuth2AccessToken(username);
            clientDetails = sysOauthClientDetailsService.getOne(Wrappers.<SysOauthClientDetails>lambdaQuery().eq(SysOauthClientDetails::getClientId, aceAuthorizeParams.clientId));
            if (clientDetails == null) {
                session.setAttribute("errMsg", SysOAuthCode.CLIENT_INFO_ERROR.getBusinessCode().getMsg());
                return "redirect:" + aceOAuthConfigProperties.getLoginPage() + "?responseType=" + aceAuthorizeParams.responseType + "&state=" + aceAuthorizeParams.state + "&clientId=" + aceAuthorizeParams.clientId + "&tenantId=" + aceAuthorizeParams.tenantId;
            }
            assert aceOAuth2AccessToken != null;
        }
        if (AceOAuthConstant.GRANT_TYPE_CODE.equals(aceAuthorizeParams.responseType)) {
            final String code = UUID.randomUUID().toString();
            tokenRedisTemplate.opsForValue().set(String.format(AceOAuthConstant.CODE_PREFIX_TENANT_ID, aceAuthorizeParams.tenantId, code), aceOAuth2AccessToken, aceOAuthConfigProperties.getCodeExpire(), TimeUnit.SECONDS);
            response.sendRedirect(String.format(AceOAuthConstant.AUTHORIZATION_CODE_URL_TENANT_ID, clientDetails.getWebServerRedirectUri(), code, aceAuthorizeParams.tenantId, aceAuthorizeParams.state));
        } else if (AceOAuthConstant.GRANT_TYPE_TOKEN.equals(aceAuthorizeParams.responseType)) {
            response.sendRedirect(String.format(AceOAuthConstant.IMPLICIT_REDIRECT_URL_TENANT_ID, clientDetails.getWebServerRedirectUri(), aceOAuth2AccessToken.getAccessToken(), aceAuthorizeParams.tenantId, aceAuthorizeParams.state));
        } else if (AceOAuthConstant.GRANT_TYPE_PASSWORD.equals(aceAuthorizeParams.responseType)) {
            final AceOAuth2AccessToken passwordToken = passwordToken(response, aceAuthorizeParams.clientId, aceAuthorizeParams.username, aceAuthorizeParams.password);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json");
            final PrintWriter out = response.getWriter();
            out.write(objectMapper.writeValueAsString(ResultBean.restResult(passwordToken, ErrorCodeInfo.OK)));
            out.flush();
            out.close();
        } else if (AceOAuthConstant.GRANT_TYPE_CLIENT_CREDENTIALS.equals(aceAuthorizeParams.responseType)) {
            final AceOAuth2AccessToken clientCredentialsToken = clientCredentialsToken(response, aceAuthorizeParams.clientId, aceAuthorizeParams.clientSecret);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json");
            final PrintWriter out = response.getWriter();
            out.write(objectMapper.writeValueAsString(ResultBean.restResult(clientCredentialsToken, ErrorCodeInfo.OK)));
            out.flush();
            out.close();
        }
        return null;
    }

    @Override
    public AceOAuth2AccessToken passwordToken(HttpServletResponse response, String clientId, String username, String password) {
        final SysOauthClientDetails clientDetails = sysOauthClientDetailsService.getOne(Wrappers.<SysOauthClientDetails>lambdaQuery().eq(SysOauthClientDetails::getClientId, clientId));
        if (clientDetails == null) {
            throw new BusinessException(SysOAuthCode.AUTHORIZED_GRANT_TYPE_NOE_ALLOW.getBusinessCode());
        }
        final AceOAuth2AccessToken aceOAuth2AccessToken = getAceOAuth2AccessToken(username, password);
        if (aceOAuth2AccessToken == null) {
            try {
                final ResultBean<Boolean> resultBean = ResultBean.restResult(false, SysOAuthCode.AUTHORIZE_FAILED.getBusinessCode());
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json");
                final PrintWriter out = response.getWriter();
                out.write(objectMapper.writeValueAsString(ResultBean.restResult(resultBean, ErrorCodeInfo.OK)));
                out.flush();
                out.close();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return aceOAuth2AccessToken;
    }

    @Override
    public AceOAuth2AccessToken clientCredentialsToken(HttpServletResponse response, String clientId, String clientSecret) {
        return null;
    }

    @Override
    public AceOAuth2AccessToken token(String code, String tenantId) {
        final AceOAuth2AccessToken aceOAuth2AccessToken = tokenRedisTemplate.opsForValue().get(String.format(AceOAuthConstant.CODE_PREFIX_TENANT_ID, tenantId, code));
        if (aceOAuth2AccessToken != null) {
            tokenRedisTemplate.delete(String.format(AceOAuthConstant.CODE_PREFIX_TENANT_ID, tenantId, code));
        }
        return aceOAuth2AccessToken;
    }

    @Override
    public String login(HttpServletRequest request, HttpServletResponse response, HttpSession session, AceAuthorizeParams aceAuthorizeParams) {
        if (StringUtils.hasText(aceAuthorizeParams.tenantId)) {
            TenantContextHolder.setTenantId(aceAuthorizeParams.tenantId);
        }
        final SysOauthClientDetails clientDetails = sysOauthClientDetailsService.getOne(Wrappers.<SysOauthClientDetails>lambdaQuery().eq(SysOauthClientDetails::getClientId, aceAuthorizeParams.clientId));
        if (clientDetails == null) {
            session.setAttribute("errMsg", SysOAuthCode.CLIENT_INFO_ERROR.getBusinessCode().getMsg());
            return "redirect:" + aceOAuthConfigProperties.getLoginPage() + "?responseType=" + aceAuthorizeParams.responseType + "&state=" + aceAuthorizeParams.state + "&clientId=" + aceAuthorizeParams.clientId + "&tenantId=" + aceAuthorizeParams.tenantId;
        }
        if (!Arrays.asList(clientDetails.getAuthorizedGrantTypes().split(",")).contains(aceAuthorizeParams.responseType)) {
            session.setAttribute("errMsg", SysOAuthCode.AUTHORIZED_GRANT_TYPE_NOE_ALLOW.getBusinessCode().getMsg());
            return "redirect:" + aceOAuthConfigProperties.getLoginPage() + "?responseType=" + aceAuthorizeParams.responseType + "&state=" + aceAuthorizeParams.state + "&clientId=" + aceAuthorizeParams.clientId + "&tenantId=" + aceAuthorizeParams.tenantId;
        }
        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(aceAuthorizeParams.username, aceAuthorizeParams.password);
        try {
            final Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            if (authenticate != null) {
                final AceUser aceUser = (AceUser) authenticate.getPrincipal();
                session.setAttribute("username", aceUser.getUsername());
                session.setAttribute("tenantId", aceAuthorizeParams.tenantId);
                session.removeAttribute("errMsg");
            }
        } catch (AuthenticationException e) {
            session.setAttribute("errMsg", "认证失败");
        }
        return "redirect:" + aceOAuthConfigProperties.getLoginPage() + "?responseType=" + aceAuthorizeParams.responseType + "&state=" + aceAuthorizeParams.state + "&clientId=" + aceAuthorizeParams.clientId + "&tenantId=" + aceAuthorizeParams.tenantId;
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
            userDetailRedisTemplate.delete(String.format(SecurityConstants.USER_DETAIL_PREFIX_TENANT_ID, tenantId, aceOAuth2AccessToken.getAccessToken()));
        }
    }

    private AceOAuth2AccessToken getAceOAuth2AccessToken(String username, String password) {
        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        final Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if (authenticate != null) {
            final AceUser aceUser = (AceUser) authenticate.getPrincipal();
            return getAceOAuth2AccessToken(aceUser);
        }
        return null;
    }

    private AceOAuth2AccessToken getAceOAuth2AccessToken(String username) {
        final AceUser aceUser = (AceUser) userDetailsService.loadUserByUsername(username);
        if (aceUser != null) {
            return getAceOAuth2AccessToken(aceUser);
        }
        return null;
    }

    public AceOAuth2AccessToken getAceOAuth2AccessToken(AceUser aceUser) {
        //判断当前用户是否已有token
        AceOAuth2AccessToken aceOAuth2AccessToken = tokenRedisTemplate.opsForValue().get(String.format(SecurityConstants.TOKEN_PREFIX_TENANT_ID, aceUser.getTenantId(), aceUser.getUsername()));
        if (aceOAuth2AccessToken == null) {
            final String token = UUID.randomUUID().toString();
            userDetailRedisTemplate.opsForValue().set(String.format(SecurityConstants.USER_DETAIL_PREFIX_TENANT_ID, aceUser.getTenantId(), token), aceUser, aceSecurityConfigProperties.getTokenExpire(), TimeUnit.SECONDS);
            aceOAuth2AccessToken = new AceOAuth2AccessToken();
            aceOAuth2AccessToken.setAccessToken(token);
            aceOAuth2AccessToken.setExpiresIn(String.valueOf(aceSecurityConfigProperties.getTokenExpire()));
            aceOAuth2AccessToken.setExpiresTime(System.currentTimeMillis() + aceSecurityConfigProperties.getTokenExpire() * 1000L + "");
            tokenRedisTemplate.opsForValue().set(String.format(SecurityConstants.TOKEN_PREFIX_TENANT_ID, aceUser.getTenantId(), aceUser.getUsername()), aceOAuth2AccessToken, aceSecurityConfigProperties.getTokenExpire(), TimeUnit.SECONDS);
        }
        return aceOAuth2AccessToken;
    }
}
