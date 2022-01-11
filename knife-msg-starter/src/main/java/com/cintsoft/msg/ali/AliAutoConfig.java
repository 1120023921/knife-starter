package com.cintsoft.msg.ali;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import com.cintsoft.msg.ali.config.AliSmsConfigStorage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 胡昊
 * Description:
 * Date: 2021/12/30
 * Time: 14:16
 * Mail: huhao9277@gmail.com
 */
@Configuration
public class AliAutoConfig {

    @Bean
    @ConditionalOnBean(name = "aliSmsConfigStorageList")
    public Map<String, Client> aliSmsClientMap(List<AliSmsConfigStorage> aliSmsConfigStorageList) {
        final Map<String, Client> aliSmsClientMap = new HashMap<>();
        aliSmsConfigStorageList.forEach(aliSmsConfigStorage -> {
            final Config config = new Config()
                    // 您的AccessKey ID
                    .setAccessKeyId(aliSmsConfigStorage.getAccessKeyId())
                    // 您的AccessKey Secret
                    .setAccessKeySecret(aliSmsConfigStorage.getAccessKeySecret());
            // 访问的域名
            config.endpoint = "dysmsapi.aliyuncs.com";
            try {
                aliSmsClientMap.put(aliSmsConfigStorage.getAccessKeyId(), new Client(config));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return aliSmsClientMap;
    }
}
