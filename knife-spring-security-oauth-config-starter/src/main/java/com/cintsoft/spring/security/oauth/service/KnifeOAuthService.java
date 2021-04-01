package com.cintsoft.spring.security.oauth.service;

import com.cintsoft.spring.security.model.KnifeOAuth2AccessToken;
import com.cintsoft.spring.security.model.KnifeUser;
import com.cintsoft.spring.security.oauth.common.bean.KnifeAuthorizeParams;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

public interface KnifeOAuthService {

    /**
     * @param knifeAuthorizeParams OAuth2认证参数
     * @description 授权码和隐藏式认证
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/11 21:32
     */
    String authorize(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session, KnifeAuthorizeParams knifeAuthorizeParams);

    /**
     * @param clientId 客户端id
     * @param username 用户名
     * @param password 密码
     * @description 密码模式认证
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/12 10:41
     */
    KnifeOAuth2AccessToken passwordToken(String clientId, String username, String password);

    /**
     * @param clientId     客户端id
     * @param clientSecret 客户端密钥
     * @description 客户端认证
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/12 10:32
     */
    KnifeOAuth2AccessToken clientCredentialsToken(String clientId, String clientSecret);

    /**
     * @param knifeAuthorizeParams 认证参数
     * @description 刷新密钥认证
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/20 20:42
     */
    KnifeOAuth2AccessToken refreshToken(KnifeAuthorizeParams knifeAuthorizeParams);

    Map<String, Object> token(KnifeAuthorizeParams knifeAuthorizeParams);

    String login(HttpServletRequest request, HttpServletResponse response, HttpSession session, KnifeAuthorizeParams knifeAuthorizeParams);

    /**
     * @description 获取用户信息
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/12 15:30
     */
    KnifeUser userInfo(String token, String tenantId);

    void logout(String username, String token, String tenantId);
}
