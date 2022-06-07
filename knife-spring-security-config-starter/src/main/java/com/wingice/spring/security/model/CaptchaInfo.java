package com.wingice.spring.security.model;

import lombok.Data;

@Data
public class CaptchaInfo {

    //验证码key
    private String captchaKey;
    //图片base64
    private String picBase;
}
