package com.wingice.msg.mail.exception;

/**
 * @author 胡昊
 * Description:
 * Date: 2022/1/11
 * Time: 15:37
 * Mail: huhao9277@gmail.com
 */
public class MailAccountNotFoundException extends RuntimeException {

    public MailAccountNotFoundException(String message) {
        super(message);
    }
}
