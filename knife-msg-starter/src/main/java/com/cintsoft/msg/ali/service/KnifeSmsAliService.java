package com.cintsoft.msg.ali.service;

import com.cintsoft.msg.ali.vo.AliSms;
import com.cintsoft.msg.ali.vo.AliSmsResult;

import java.util.List;

public interface KnifeSmsAliService {

    /**
     * @param aliSms 短信信息
     * @description 发送短信
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/11 14:48
     */
    AliSmsResult sendSms(AliSms aliSms);

    /**
     * @param aliSmsList 发送失败短信列表
     * @description 批量发送短信
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/11 14:48
     */
    List<AliSmsResult> sendSmsBatch(List<AliSms> aliSmsList);
}
