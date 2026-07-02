package com.wingice.log.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "knife.log")
public class KnifeLogProperties {

    //日志版本
    private String version = "1.0.0";
    //是否启用默认API
    private Boolean logApiEnable = false;
    /**
     * 可信代理 IP 列表
     * 只有 remoteAddr 在此列表中的请求，才信任其代理头；否则忽略所有代理头，回退到 request.getRemoteAddr()
     */
    private List<String> trustedProxyIpList = Collections.emptyList();
}
