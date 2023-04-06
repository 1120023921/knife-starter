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

    /**
     * @param captchaKey 验证码key
     * @param code       用户输入的验证码
     * @description 验证码校验
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2023/4/6 9:29
     */
    Boolean verifyCaptcha(String captchaKey, String code);
}
