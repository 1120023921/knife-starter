package com.cintsoft.msg.wechat.mp.vo;

import cn.hutool.json.JSONObject;
import lombok.*;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 胡昊
 * Description:
 * Date: 2022/1/14
 * Time: 15:36
 * Mail: huhao9277@gmail.com
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WechatMpTemplateMessage {

    private String msgId;

    /**
     * 接收者openid.
     */
    private String toUser;

    /**
     * 模板ID.
     */
    private String templateId;

    /**
     * 模板跳转链接.
     * <pre>
     * url和miniprogram都是非必填字段，若都不传则模板无跳转；若都传，会优先跳转至小程序。
     * 开发者可根据实际需要选择其中一种跳转方式即可。当用户的微信客户端版本不支持跳小程序时，将会跳转至url。
     * </pre>
     */
    private String url;

    /**
     * 跳小程序所需数据，不需跳小程序可不用传该数据.
     *
     * @see #url
     */
    private MiniProgram miniProgram;

    /**
     * 模板数据.
     */
    @Builder.Default
    private List<WechatMpTemplateData> data = new ArrayList<>();

    public WechatMpTemplateMessage addData(WechatMpTemplateData datum) {
        if (this.data == null) {
            this.data = new ArrayList<>();
        }
        this.data.add(datum);
        return this;
    }

    public String toWechatJson() {
        final JSONObject templateMessage = new JSONObject();
        templateMessage.set("touser", this.getToUser());
        templateMessage.set("template_id", this.getTemplateId());
        if (StringUtils.hasText(this.getUrl())) {
            templateMessage.set("url", this.getUrl());
        }
        if (this.miniProgram != null) {
            final JSONObject miniprogram = new JSONObject();
            miniprogram.set("appid", this.getMiniProgram().getAppid());
            miniprogram.set("pagepath", this.getMiniProgram().getPagePath());
            templateMessage.set("miniprogram", miniprogram);
        }
        final JSONObject data = new JSONObject();
        this.getData().forEach(wechatMpTemplateData -> {
            final JSONObject item = new JSONObject();
            item.set("value", wechatMpTemplateData.getValue());
            if (StringUtils.hasText(wechatMpTemplateData.getColor())) {
                item.set("color", wechatMpTemplateData.getColor());
            }
            data.set(wechatMpTemplateData.getName(), item);
        });
        templateMessage.set("data", data);
        return templateMessage.toString();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MiniProgram implements Serializable {

        private String appid;
        private String pagePath;

        /**
         * 是否使用path，否则使用pagepath.
         * 加入此字段是基于微信官方接口变化多端的考虑
         */
        private boolean usePath = false;
    }
}
