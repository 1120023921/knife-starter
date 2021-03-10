package com.cintsoft.mybatis.plus.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "knife.tenant")
@ConditionalOnProperty(name = "knife.tenant.enable", havingValue = "true")
public class KnifeTenantConfigProperties {

    //租户启用标志
    private Boolean enable = false;

    //租户列明
    private String column = "tenant_id";

    //忽略租户限制表集合
    private List<String> tables = new ArrayList<>();
}
