package com.wingice.msg.mail.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author 胡昊
 * Description:
 * Date: 2022/1/13
 * Time: 14:29
 * Mail: huhao9277@gmail.com
 */
@Data
@Builder
public class MailResult {

    private String msgId;
    private String code;
    private String errMsg;
}
