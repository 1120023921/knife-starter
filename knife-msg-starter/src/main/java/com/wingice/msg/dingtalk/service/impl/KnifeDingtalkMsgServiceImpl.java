package com.wingice.msg.dingtalk.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wingice.msg.dingtalk.service.KnifeDingtalkMsgService;
import com.wingice.msg.dingtalk.service.KnifeDingtalkSenderContext;
import com.wingice.msg.dingtalk.vo.DingtalkMsgResult;
import com.wingice.msg.dingtalk.vo.msg.DingtalkMsg;

public class KnifeDingtalkMsgServiceImpl implements KnifeDingtalkMsgService {

    private final KnifeDingtalkSenderContext knifeDingtalkSenderContext;

    private final String DINGTALK_MSG_URL = "https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2?access_token=%s";

    public KnifeDingtalkMsgServiceImpl(KnifeDingtalkSenderContext knifeDingtalkSenderContext) {
        this.knifeDingtalkSenderContext = knifeDingtalkSenderContext;
    }

    @Override
    public DingtalkMsgResult sendDingtalkTemplateMessage(DingtalkMsg dingtalkMsg) {
        final JSONObject result = JSONUtil.parseObj(HttpUtil.post(String.format(DINGTALK_MSG_URL, knifeDingtalkSenderContext.getDingtalkAccount().getAccessToken()), JSONUtil.toJsonPrettyStr(dingtalkMsg)));
        return DingtalkMsgResult.builder()
                .code(result.getStr("errcode"))
                .errMsg(result.getStr("errmsg"))
                .msgId(dingtalkMsg.getMsgId())
                .build();
    }
}
