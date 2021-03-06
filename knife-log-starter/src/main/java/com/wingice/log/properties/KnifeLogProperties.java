package com.wingice.log.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "knife.log")
public class KnifeLogProperties {

    //日志版本
    private String version = "1.0.0";
    //是否启用默认API
    private Boolean logApiEnable = false;
}
