package com.wingice.mail.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Tolerate;

/**
 * 邮件发送错误信息
 */
@Data
@Builder
@ToString
@EqualsAndHashCode
public class MailSendResponse {

    @Tolerate
    public MailSendResponse() {
    }

    //返回码 200-成功 500-失败
    private Integer code;
    //错误信息
    private String msg;
}
