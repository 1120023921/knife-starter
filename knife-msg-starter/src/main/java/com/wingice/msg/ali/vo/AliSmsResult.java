package com.wingice.msg.ali.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author 胡昊
 * Description:
 * Date: 2022/1/13
 * Time: 14:12
 * Mail: huhao9277@gmail.com
 */
@Data
@Builder
public class AliSmsResult {

    private String msgId;
    private String code;
    private String errMsg;

}
