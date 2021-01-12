package com.cintsoft.spring.security.oauth.service;

import com.cintsoft.spring.security.model.AceOAuth2AccessToken;
import com.cintsoft.spring.security.model.AceUser;
import com.cintsoft.spring.security.oauth.common.bean.AceAuthorizeParams;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public interface AceOAuthService {

    /**
     * @param aceAuthorizeParams OAuth2认证参数
     * @description 授权码和隐藏式认证
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/11 21:32
     */
    String authorize(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session, AceAuthorizeParams aceAuthorizeParams);

    /**
     * @param clientId 客户端id
     * @param username 用户名
     * @param password 密码
     * @description 密码模式认证
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/12 10:41
     */
    AceOAuth2AccessToken passwordToken(HttpServletResponse response, String clientId, String username, String password);

    /**
     * @param clientId     客户端id
     * @param clientSecret 客户端密钥
     * @description 客户端认证
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/12 10:32
     */
    AceOAuth2AccessToken clientCredentialsToken(HttpServletResponse response, String clientId, String clientSecret);

    AceOAuth2AccessToken token(String code, String tenantId);

    String login(HttpServletRequest request, HttpServletResponse response, HttpSession session, AceAuthorizeParams aceAuthorizeParams);

    /**
     * @description 获取用户信息
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/12 15:30
     */
    AceUser userInfo(String token, String tenantId);

    void logout(String username, String tenantId);
}
