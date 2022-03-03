package com.wingice.msg.wechat.mp.exception;

/**
 * @author 胡昊
 * Description:
 * Date: 2022/1/14
 * Time: 14:20
 * Mail: huhao9277@gmail.com
 */
public class WechatMpAccountNotFoundException extends RuntimeException {

    public WechatMpAccountNotFoundException(String message) {
        super(message);
    }
}
