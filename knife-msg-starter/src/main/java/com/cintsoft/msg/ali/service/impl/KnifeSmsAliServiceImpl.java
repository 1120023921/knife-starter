package com.cintsoft.msg.ali.service.impl;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.cintsoft.msg.ali.service.KnifeSmsAliSenderContext;
import com.cintsoft.msg.ali.service.KnifeSmsAliService;
import com.cintsoft.msg.ali.vo.AliSms;
import com.cintsoft.msg.ali.vo.AliSmsResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 胡昊
 * Description:
 * Date: 2022/1/13
 * Time: 13:43
 * Mail: huhao9277@gmail.com
 */
@Slf4j
public class KnifeSmsAliServiceImpl implements KnifeSmsAliService {

    private final KnifeSmsAliSenderContext knifeSmsAliSenderContext;

    public KnifeSmsAliServiceImpl(KnifeSmsAliSenderContext knifeSmsAliSenderContext) {
        this.knifeSmsAliSenderContext = knifeSmsAliSenderContext;
    }

    @Override
    public AliSmsResult sendSms(AliSms aliSms) {
        try {
            final Client client = knifeSmsAliSenderContext.getClient();
            final SendSmsRequest sendSmsRequest = new SendSmsRequest();
            BeanUtils.copyProperties(aliSms, sendSmsRequest);
            final SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);
            return AliSmsResult.builder()
                    .msgId(aliSms.getMsgId())
                    .code(sendSmsResponse.getBody().getCode())
                    .errMsg(sendSmsResponse.getBody().getMessage())
                    .build();
        } catch (Exception e) {
            return AliSmsResult.builder()
                    .msgId(aliSms.getMsgId())
                    .code("-1")
                    .errMsg(e.getMessage())
                    .build();
        }
    }

    @Override
    public List<AliSmsResult> sendSmsBatch(List<AliSms> aliSmsList) {
        final List<AliSmsResult> aliSmsResultList = new ArrayList<>();
        aliSmsList.forEach(aliSms -> aliSmsResultList.add(sendSms(aliSms)));
        return aliSmsResultList;
    }
}
