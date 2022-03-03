package com.wingice.msg.wechat.mp.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author 胡昊
 * Description:
 * Date: 2022/1/14
 * Time: 15:47
 * Mail: huhao9277@gmail.com
 */
@Data
@Builder
public class WechatMpMsgResult {

    private String msgId;
    private String code;
    private String errMsg;
}
