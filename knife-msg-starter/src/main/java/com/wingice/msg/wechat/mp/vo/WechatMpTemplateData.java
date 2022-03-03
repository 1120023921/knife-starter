package com.wingice.msg.wechat.mp.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class WechatMpTemplateData implements Serializable {

    private String name;
    private String value;
    private String color;

    public WechatMpTemplateData() {
    }

    public WechatMpTemplateData(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public WechatMpTemplateData(String name, String value, String color) {
        this.name = name;
        this.value = value;
        this.color = color;
    }

}