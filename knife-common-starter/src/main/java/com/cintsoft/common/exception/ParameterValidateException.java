package com.cintsoft.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 胡昊
 * Description: 参数校验异常
 * Date: 2019/7/2
 * Time: 10:20
 * Create: DoubleH
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ParameterValidateException extends RuntimeException {

    private Integer code;
    private String msg;

    public ParameterValidateException() {
        super();
    }

    public ParameterValidateException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public ParameterValidateException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public ParameterValidateException(BusinessCode businessCode) {
        super(businessCode.getMsg());
        this.code = businessCode.getCode();
        this.msg = businessCode.getMsg();
    }
}
