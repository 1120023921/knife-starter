package com.wingice.msg.wechat.mp.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wingice.msg.wechat.mp.entity.WechatMpAccount;
import com.wingice.msg.wechat.mp.service.KnifeWechatMpMsgService;
import com.wingice.msg.wechat.mp.service.KnifeWechatMpSenderContext;
import com.wingice.msg.wechat.mp.vo.WechatMpMsgResult;
import com.wingice.msg.wechat.mp.vo.WechatMpTemplateMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 胡昊
 * Description:
 * Date: 2022/1/14
 * Time: 15:49
 * Mail: huhao9277@gmail.com
 */
public class KnifeWechatMpMsgServiceImpl implements KnifeWechatMpMsgService {

    private final KnifeWechatMpSenderContext knifeWechatMpSenderContext;

    public KnifeWechatMpMsgServiceImpl(KnifeWechatMpSenderContext knifeWechatMpSenderContext) {
        this.knifeWechatMpSenderContext = knifeWechatMpSenderContext;
    }

    private final static String WECHAT_MP_TEMPLATE_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";

    @Override
    public WechatMpMsgResult sendWechatMpTemplateMessage(WechatMpTemplateMessage wechatMpTemplateMessage) {
        try {
            final WechatMpAccount wechatMpAccount = knifeWechatMpSenderContext.getWechatMpAccount();
            final JSONObject result = JSONUtil.parseObj(HttpUtil.post(String.format(WECHAT_MP_TEMPLATE_MESSAGE_URL, wechatMpAccount.getAccessToken()), wechatMpTemplateMessage.toWechatJson()));
            return WechatMpMsgResult.builder()
                    .msgId(wechatMpTemplateMessage.getMsgId())
                    .code(String.valueOf(result.getInt("errcode")))
                    .errMsg(result.getStr("errmsg"))
                    .build();
        } catch (Exception e) {
            return WechatMpMsgResult.builder()
                    .msgId(wechatMpTemplateMessage.getMsgId())
                    .code("-1")
                    .errMsg(e.getMessage())
                    .build();
        }
    }

    @Override
    public List<WechatMpMsgResult> sendWechatMpTemplateMessageBatch(List<WechatMpTemplateMessage> wechatMpTemplateMessageList) {
        final List<WechatMpMsgResult> wechatMpMsgResultList = new ArrayList<>();
        wechatMpTemplateMessageList.forEach(wechatMpTemplateMessage -> {
            try {
                final WechatMpAccount wechatMpAccount = knifeWechatMpSenderContext.getWechatMpAccount();
                final JSONObject result = JSONUtil.parseObj(HttpUtil.post(String.format(WECHAT_MP_TEMPLATE_MESSAGE_URL, wechatMpAccount.getAccessToken()), wechatMpTemplateMessage.toWechatJson()));
                wechatMpMsgResultList.add(WechatMpMsgResult.builder()
                        .msgId(wechatMpTemplateMessage.getMsgId())
                        .code(String.valueOf(result.getInt("errcode")))
                        .errMsg(result.getStr("errmsg"))
                        .build());
            } catch (Exception e) {
                wechatMpMsgResultList.add(WechatMpMsgResult.builder()
                        .msgId(wechatMpTemplateMessage.getMsgId())
                        .code("-1")
                        .errMsg(e.getMessage())
                        .build());
            }
        });
        return wechatMpMsgResultList;
    }
}
