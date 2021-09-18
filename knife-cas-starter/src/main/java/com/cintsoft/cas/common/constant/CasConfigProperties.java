package com.cintsoft.cas.common.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author 胡昊
 * Description:
 * Date: 2021/9/18
 * Time: 11:00
 * Mail: huhao9277@gmail.com
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "knife.security.cas")
public class CasConfigProperties {

    private Boolean enable = false;
    private String casServerUrlPrefix;
    private String service;
    private String userField = "user";
}
