package com.wingice.msg.dingtalk.vo;

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
public class DingtalkMsgResult {

    private String msgId;
    private String code;
    private String errMsg;
}
