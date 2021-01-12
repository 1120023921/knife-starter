package com.cintsoft.spring.security.oauth.controller;

import com.cintsoft.common.web.ErrorCodeInfo;
import com.cintsoft.common.web.ResultBean;
import com.cintsoft.spring.security.model.AceOAuth2AccessToken;
import com.cintsoft.spring.security.model.AceUser;
import com.cintsoft.spring.security.oauth.AceOAuthConfigProperties;
import com.cintsoft.spring.security.oauth.common.bean.AceAuthorizeParams;
import com.cintsoft.spring.security.oauth.service.AceOAuthService;
import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author 胡昊
 * Description:
 * Date: 2021/1/11
 * Time: 19:43
 * Mail: huhao9277@gmail.com
 */
@AllArgsConstructor
@RequestMapping("/oauth")
public class AceOAuthController {

    private final AceOAuthService aceOAuthService;
    private final AceOAuthConfigProperties aceOAuthConfigProperties;

    @GetMapping("/authorize")
    public String authorize(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session, AceAuthorizeParams aceAuthorizeParams) {
        return aceOAuthService.authorize(model, request, response, session, aceAuthorizeParams);
    }

    @GetMapping("/token")
    @ResponseBody
    public ResultBean<AceOAuth2AccessToken> token(String code, String tenantId) {
        return ResultBean.restResult(aceOAuthService.token(code, tenantId), ErrorCodeInfo.OK);
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response, HttpSession session, AceAuthorizeParams aceAuthorizeParams) {
        return aceOAuthService.login(request, response, session, aceAuthorizeParams);
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, String tenantId) {
        aceOAuthService.logout((String) session.getAttribute("username"), tenantId);
        session.removeAttribute("username");
        return "redirect:" + aceOAuthConfigProperties.getLogoutSuccessUrl();
    }

    @GetMapping("/userInfo")
    @ResponseBody
    public ResultBean<AceUser> userInfo(HttpServletRequest request) {
        return ResultBean.restResult(aceOAuthService.userInfo(request.getHeader("Authorization"), request.getHeader("TENANT_ID")), ErrorCodeInfo.OK);
    }
}
