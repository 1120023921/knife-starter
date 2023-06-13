package com.wingice.spring.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author 胡昊
 * Description:
 * Date: 2021/6/3
 * Time: 8:56
 * Mail: huhao9277@gmail.com
 */
public class KnifeAuthenticationException extends AuthenticationException {

    private int code;
    private String msg;

    public KnifeAuthenticationException(int code, String data, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public KnifeAuthenticationException(String msg, Throwable t) {
        super(msg, t);
        this.msg = msg;
    }

    public KnifeAuthenticationException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
