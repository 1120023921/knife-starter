package com.wingice.msg.dingtalk.exception;

/**
 * @author 胡昊
 * Description:
 * Date: 2022/1/14
 * Time: 14:20
 * Mail: huhao9277@gmail.com
 */
public class DingtalkAccountNotFoundException extends RuntimeException {

    public DingtalkAccountNotFoundException(String message) {
        super(message);
    }
}
