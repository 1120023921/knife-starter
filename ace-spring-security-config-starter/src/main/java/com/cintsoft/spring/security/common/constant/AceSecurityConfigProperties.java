package com.cintsoft.spring.security.common.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author 胡昊
 * Description:
 * Date: 2021/1/10
 * Time: 19:28
 * Mail: huhao9277@gmail.com
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "ace.security")
public class AceSecurityConfigProperties {

    //Token过期时间
    private Integer tokenExpire = 7200;
    //默认内存用户启用状态
    private Boolean defaultInMemoryEnable = false;
    //租户模式
    private Boolean tenantEnable = true;
}
