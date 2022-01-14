package com.cintsoft.msg.wechat.mp.service;

import com.cintsoft.msg.wechat.mp.vo.WechatMpMsgResult;
import com.cintsoft.msg.wechat.mp.vo.WechatMpTemplateMessage;

import java.util.List;

public interface KnifeWechatMpMsgService {

    /**
     * @param wechatMpTemplateMessage 模板消息内容
     * @description 发送微信公众号模板消息
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/14 15:48
     */
    WechatMpMsgResult sendWechatMpTemplateMessage(WechatMpTemplateMessage wechatMpTemplateMessage);

    /**
     * @param wechatMpTemplateMessageList 模板消息内容列表
     * @description 批量发送微信公众号模板消息
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/14 15:49
     */
    List<WechatMpMsgResult> sendWechatMpTemplateMessageBatch(List<WechatMpTemplateMessage> wechatMpTemplateMessageList);
}
