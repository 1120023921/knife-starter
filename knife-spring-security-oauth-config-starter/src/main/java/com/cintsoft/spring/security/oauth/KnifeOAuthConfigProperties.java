package com.cintsoft.spring.security.oauth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author 胡昊
 * Description:
 * Date: 2021/1/11
 * Time: 21:30
 * Mail: huhao9277@gmail.com
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "knife.security.oauth")
public class KnifeOAuthConfigProperties {

    //是否启用租户模式
    private Boolean tenantEnable = true;
    //统一登录地址
    private String loginPage = "http://localhost:8080/oauth/authorize";
    //注销成功跳转地址
    private String logoutSuccessUrl = "http://cintsoft.com";
    //code有效时间
    private Integer codeExpire = 30;
}
