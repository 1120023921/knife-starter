package com.cintsoft.common.exception;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 胡昊
 * Description: 业务异常错误码
 * Date: 2019/7/3
 * Time: 10:40
 * Create: DoubleH
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BusinessCode {

    private Integer code;
    private String msg;

    public BusinessCode() {
    }

    public BusinessCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
