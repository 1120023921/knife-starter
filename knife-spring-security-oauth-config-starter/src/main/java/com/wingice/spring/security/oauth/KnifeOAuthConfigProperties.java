package com.wingice.spring.security.oauth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author 胡昊
 * Description:
 * Date: 2021/1/11
 * Time: 21:30
 * Mail: huhao9277@gmail.com
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "knife.security.oauth")
public class KnifeOAuthConfigProperties {

    //是否启用租户模式
    private Boolean tenantEnable = true;
    //统一登录地址
    private String loginPage = "http://localhost:8080/oauth/authorize";
    //注销成功跳转地址
    private String logoutSuccessUrl = "http://wingice.com";
    //code有效时间
    private Integer codeExpire = 30;
    //code存储路径（带租户）
    private String codePrefixTenantId = "KNIFE:CODE:%s:%s";
    //code存储路径
    private String codePrefix = "KNIFE:CODE:%s";
}
