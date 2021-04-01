package com.cintsoft.spring.security.oauth.controller;

import com.cintsoft.common.web.ErrorCodeInfo;
import com.cintsoft.common.web.ResultBean;
import com.cintsoft.spring.security.model.KnifeUser;
import com.cintsoft.spring.security.oauth.KnifeOAuthConfigProperties;
import com.cintsoft.spring.security.oauth.common.bean.KnifeAuthorizeParams;
import com.cintsoft.spring.security.oauth.service.KnifeOAuthService;
import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author 胡昊
 * Description:
 * Date: 2021/1/11
 * Time: 19:43
 * Mail: huhao9277@gmail.com
 */
@AllArgsConstructor
@RequestMapping("/oauth")
public class KnifeOAuthController {

    private final KnifeOAuthService knifeOAuthService;
    private final KnifeOAuthConfigProperties knifeOAuthConfigProperties;

    @GetMapping("/authorize")
    public String authorize(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session, KnifeAuthorizeParams knifeAuthorizeParams) {
        return knifeOAuthService.authorize(model, request, response, session, knifeAuthorizeParams);
    }

    @GetMapping("/token")
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
        if (StringUtils.isEmpty(tenantId)) {
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
