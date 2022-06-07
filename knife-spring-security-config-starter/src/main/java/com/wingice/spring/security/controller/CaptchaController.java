package com.wingice.spring.security.controller;

import com.wingice.common.web.ErrorCodeInfo;
import com.wingice.common.web.ResultBean;
import com.wingice.spring.security.model.CaptchaInfo;
import com.wingice.spring.security.service.CaptchaService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@AllArgsConstructor
@RequestMapping("/captcha")
public class CaptchaController {

    private final CaptchaService captchaService;

    /**
     * @description 生成验证码
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/4/12 11:13
     */
    @GetMapping("/generateCaptcha")
    @ResponseBody
    public ResultBean<CaptchaInfo> generateCaptcha() {
        return ResultBean.restResult(captchaService.generateCaptcha(), ErrorCodeInfo.OK);
    }
}
