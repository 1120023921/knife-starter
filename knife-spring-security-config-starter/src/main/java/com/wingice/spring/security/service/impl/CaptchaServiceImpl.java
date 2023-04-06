package com.wingice.spring.security.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.wingice.spring.security.common.constant.KnifeSecurityConfigProperties;
import com.wingice.spring.security.exception.KnifeAuthenticationException;
import com.wingice.spring.security.model.CaptchaInfo;
import com.wingice.spring.security.service.CaptchaService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CaptchaServiceImpl implements CaptchaService {

    private final KnifeSecurityConfigProperties knifeSecurityConfigProperties;
    private final StringRedisTemplate stringRedisTemplate;

    public CaptchaServiceImpl(KnifeSecurityConfigProperties knifeSecurityConfigProperties, StringRedisTemplate stringRedisTemplate) {
        this.knifeSecurityConfigProperties = knifeSecurityConfigProperties;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public CaptchaInfo generateCaptcha() {
        final ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(knifeSecurityConfigProperties.getCaptchaWidth(), knifeSecurityConfigProperties.getCaptchaHeight(), 4, 4);
        captcha.createCode();
        final String code = captcha.getCode();
        final String captchaKey = UUID.randomUUID().toString();
        final CaptchaInfo captchaInfo = new CaptchaInfo();
        captchaInfo.setCaptchaKey(captchaKey);
        captchaInfo.setPicBase(captcha.getImageBase64());
        stringRedisTemplate.opsForValue().set(String.format(knifeSecurityConfigProperties.getCaptchaPrefix(), captchaKey), code, knifeSecurityConfigProperties.getCaptchaTime(), TimeUnit.SECONDS);
        return captchaInfo;
    }

    @Override
    public Boolean verifyCaptcha(String captchaKey, String userInputCode) {
        //校验验证码
        if (!StringUtils.hasText(userInputCode)) {
            throw new KnifeAuthenticationException("验证码未填写");
        }
        final String code = stringRedisTemplate.opsForValue().get(String.format(knifeSecurityConfigProperties.getCaptchaPrefix(), captchaKey));
        return userInputCode.equalsIgnoreCase(code);
    }
}
