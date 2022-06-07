package com.wingice.spring.security.service;

import com.wingice.spring.security.model.CaptchaInfo;

public interface CaptchaService {

    /**
     * @description 生成验证码
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/4/12 10:43
     */
    CaptchaInfo generateCaptcha();

}
