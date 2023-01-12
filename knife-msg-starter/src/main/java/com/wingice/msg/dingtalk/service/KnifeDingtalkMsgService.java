package com.wingice.msg.dingtalk.service;

import com.wingice.msg.dingtalk.vo.DingtalkMsgResult;
import com.wingice.msg.dingtalk.vo.msg.DingtalkMsg;

public interface KnifeDingtalkMsgService {

    /**
     * @param dingtalkMsg 消息内容
     * @description 发送钉钉消息
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/14 15:48
     */
    DingtalkMsgResult sendDingtalkTemplateMessage(DingtalkMsg dingtalkMsg);
}
