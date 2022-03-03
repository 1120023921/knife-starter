package com.wingice.msg.ali.exception;

/**
 * @author 胡昊
 * Description:
 * Date: 2022/1/11
 * Time: 15:37
 * Mail: huhao9277@gmail.com
 */
public class AliAccountNotFoundException extends RuntimeException {

    public AliAccountNotFoundException(String message) {
        super(message);
    }
}
