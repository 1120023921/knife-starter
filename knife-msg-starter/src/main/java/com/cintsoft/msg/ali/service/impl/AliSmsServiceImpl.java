package com.cintsoft.msg.ali.service.impl;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.cintsoft.msg.ali.AliSmsClientStorageHolder;
import com.cintsoft.msg.ali.service.AliSmsService;

import java.util.Map;

/**
 * @author 胡昊
 * Description:
 * Date: 2021/12/30
 * Time: 14:39
 * Mail: huhao9277@gmail.com
 */
public class AliSmsServiceImpl implements AliSmsService {

    public AliSmsServiceImpl(Map<String, Client> aliSmsClientMap) {
        this.aliSmsClientMap = aliSmsClientMap;
    }

    private final Map<String, Client> aliSmsClientMap;

    @Override
    public SendSmsResponse sendAliSms(SendSmsRequest sendSmsRequest) throws Exception {
        return getClient().sendSms(sendSmsRequest);
    }

    public AliSmsServiceImpl switchClient(String accessKeyId) {
        AliSmsClientStorageHolder.set(accessKeyId);
        return this;
    }

    private Client getClient() {
        final String accessKeyId = AliSmsClientStorageHolder.get();
        if (aliSmsClientMap.values().size() == 0) {
            throw new RuntimeException("未配置阿里云短信账户信息");
        }
        if ("default".equals(accessKeyId)) {
            return aliSmsClientMap.get(aliSmsClientMap.keySet().iterator().next());
        }
        final Client client = aliSmsClientMap.get(AliSmsClientStorageHolder.get());
        if (client == null) {
            throw new RuntimeException("未找到账户【" + AliSmsClientStorageHolder.get() + "】");
        }
        return client;
    }
}
