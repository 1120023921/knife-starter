package com.wingice.quartz.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author 胡昊
 * Description:
 * Date: 2022/2/11
 * Time: 10:24
 * Mail: huhao9277@gmail.com
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "knife.quartz")
public class KnifeQuartzProperties {

    //是否启用默认API
    private Boolean quartzApiEnable = false;
}
