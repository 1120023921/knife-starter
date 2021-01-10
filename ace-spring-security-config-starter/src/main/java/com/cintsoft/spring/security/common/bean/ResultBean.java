package com.cintsoft.spring.security.common.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 胡昊
 * Description: 返回结果
 * Date: 2020/7/23
 * Time: 16:24
 * Mail: huhao9277@gmail.com
 */
@Data
public class ResultBean<T> implements Serializable {

    private int code;
    private String msg;
    private T data;

    public static <T> ResultBean<T> restResult(T data, int code, String msg) {
        ResultBean<T> resultBean = new ResultBean<>();
        resultBean.setCode(code);
        resultBean.setData(data);
        resultBean.setMsg(msg);
        return resultBean;
    }

    public static <T> ResultBean<T> restResult(T data, ErrorCodeInfo errorCode) {
        return restResult(data, errorCode.getCode(), errorCode.getMsg());
    }
}
