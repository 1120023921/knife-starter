package com.wingice.spring.security.common.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author 胡昊
 * Description:
 * Date: 2021/1/10
 * Time: 19:28
 * Mail: huhao9277@gmail.com
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "knife.security")
public class KnifeSecurityConfigProperties {

    //Token过期时间
    private Integer tokenExpire = 7200;
    //RefreshToken过期时间
    private Integer refreshTokenExpire = 7200;
    //默认内存用户启用状态
    private Boolean defaultInMemoryEnable = false;
    //租户模式
    private Boolean tenantEnable = true;
    //ACCESS_TOKEN存储前缀（带租户）
    private String accessTokenPrefixTenantId = "KNIFE:ACCESS_TOKEN:%s:%s";
    //ACCESS_TOKEN存储前缀
    private String accessTokenPrefix = "KNIFE:ACCESS_TOKEN:%s";
    //REFRESH_TOKEN存储前缀（带租户）
    private String refreshTokenPrefixTenantId = "KNIFE:REFRESH_TOKEN:%s:%s";
    //REFRESH_TOKEN存储前缀
    private String refreshTokenPrefix = "KNIFE:REFRESH_TOKEN:%s";
    //用户信息存储前缀（带租户）
    private String userDetailPrefixTenantId = "KNIFE:USER_DETAIL:%s:%s";
    //用户信息存储前缀
    private String userDetailPrefix = "KNIFE:USER_DETAIL:%s";
    //USER_REFRESH_TOKEN存储前缀（带租户）
    private String userRefreshTokenPrefixTenantId = "KNIFE:USER_REFRESH_TOKEN:%s:%s";
    //USER_REFRESH_TOKEN存储前缀
    private String userRefreshTokenPrefix = "KNIFE:USER_REFRESH_TOKEN:%s";
    //是否密码加密
    private Boolean passwordEncrypt = false;
    //是否启用验证码
    private Boolean captchaEnable = false;
    //验证码存储前缀
    private String captchaPrefix = "KNIFE:USER_CAPTCHA:%s";
    //验证码有效期（秒）
    private Long captchaTime = 120L;
    //验证码宽度
    private Integer captchaWidth = 250;
    //验证码高度
    private Integer captchaHeight = 100;
    //验证码模式
    private String captchaMode = "defaultCaptchaService";
}
