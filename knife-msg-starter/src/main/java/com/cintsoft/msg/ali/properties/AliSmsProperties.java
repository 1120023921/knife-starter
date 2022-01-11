package com.cintsoft.msg.ali.properties;

import com.cintsoft.msg.ali.config.AliSmsConfigStorage;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

/**
 * @author 胡昊
 * Description:
 * Date: 2021/12/30
 * Time: 14:58
 * Mail: huhao9277@gmail.com
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "sms.ali")
public class AliSmsProperties {

    private List<AliSmsConfigStorage> aliSmsConfigStorageList = Collections.emptyList();
}
