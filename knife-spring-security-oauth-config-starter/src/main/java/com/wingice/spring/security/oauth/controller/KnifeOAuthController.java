package com.wingice.spring.security.oauth.controller;

import cn.hutool.core.util.StrUtil;
import com.wingice.common.web.ErrorCodeInfo;
import com.wingice.common.web.ResultBean;
import com.wingice.spring.security.model.KnifeUser;
import com.wingice.spring.security.oauth.KnifeOAuthConfigProperties;
import com.wingice.spring.security.oauth.common.bean.KnifeAuthorizeParams;
import com.wingice.spring.security.oauth.service.KnifeOAuthService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author 胡昊
 * Description:
 * Date: 2021/1/11
 * Time: 19:43
 * Mail: huhao9277@gmail.com
 */
@AllArgsConstructor
@Controller
@RequestMapping("/oauth")
public class KnifeOAuthController {

    private final KnifeOAuthService knifeOAuthService;
    private final KnifeOAuthConfigProperties knifeOAuthConfigProperties;

    @GetMapping("/authorize")
    public String authorize(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session, KnifeAuthorizeParams knifeAuthorizeParams) {
        return knifeOAuthService.authorize(model, request, response, session, knifeAuthorizeParams);
    }

    @PostMapping("/token")
    @ResponseBody
    public Map<String, Object> token(KnifeAuthorizeParams knifeAuthorizeParams) {
        return knifeOAuthService.token(knifeAuthorizeParams);
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response, HttpSession session, KnifeAuthorizeParams knifeAuthorizeParams) {
        return knifeOAuthService.login(request, response, session, knifeAuthorizeParams);
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, String tenantId) {
        knifeOAuthService.logout((String) session.getAttribute("username"), null, tenantId);
        session.removeAttribute("username");
        return "redirect:" + knifeOAuthConfigProperties.getLogoutSuccessUrl();
    }

    @GetMapping("/logout/pwd")
    @ResponseBody
    public ResultBean<Boolean> logout(HttpServletRequest request, String tenantId) {
        final String token = request.getHeader("Authorization") == null ? null : request.getHeader("Authorization").split(" ")[1];
        if (StrUtil.isBlank(tenantId)) {
            tenantId = request.getHeader("TENANT_ID");
        }
        knifeOAuthService.logout(null, token, tenantId);
        return ResultBean.restResult(true, ErrorCodeInfo.OK);
    }

    @GetMapping("/userInfo")
    @ResponseBody
    public ResultBean<KnifeUser> userInfo(HttpServletRequest request) {
        return ResultBean.restResult(knifeOAuthService.userInfo(request.getHeader("Authorization").split(" ")[1], request.getHeader("TENANT_ID")), ErrorCodeInfo.OK);
    }
}
