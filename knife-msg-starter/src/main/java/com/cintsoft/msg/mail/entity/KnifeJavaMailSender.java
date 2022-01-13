package com.cintsoft.msg.mail.entity;

import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * @author 胡昊
 * Description:
 * Date: 2022/1/11
 * Time: 18:16
 * Mail: huhao9277@gmail.com
 */
public class KnifeJavaMailSender extends JavaMailSenderImpl {

    private String from;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
