package com.cintsoft.msg.ali.service;

import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;

public interface AliSmsService {

    /**
     * @param sendSmsRequest 短信信息
     * @description 发送阿里短信
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/12/30 14:36
     */
    SendSmsResponse sendAliSms(SendSmsRequest sendSmsRequest) throws Exception;
}
