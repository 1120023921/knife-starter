package com.cintsoft.msg.ali.vo;

import lombok.Data;

@Data
public class AliSms {

    private String msgId;
    private String phoneNumbers;
    private String signName;
    private String templateCode;
    private String templateParam;
    private String outId;
    private String smsUpExtendCode;

}