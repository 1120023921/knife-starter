package com.wingice.msg.ali.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author 胡昊
 * Description:
 * Date: 2022/1/11
 * Time: 15:12
 * Mail: huhao9277@gmail.com
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "knife.msg.sms.ali")
public class KnifeAliProperties {

    //启用状态
    private Boolean enable = false;
    //租户模式
    private Boolean tenantEnable = false;
    //邮件账户管理API状态
    private Boolean aliAccountApiEnable = false;
    //缓存路径
    private String cachePrefix = "KNIFE:MSG:SMS:ALI:";
}
