package com.cintsoft.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 胡昊
 * Description: 业务异常
 * Date: 2019/7/2
 * Time: 10:20
 * Create: DoubleH
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends RuntimeException {

    private Integer code;
    private String msg;
    private Object data;

    public BusinessException() {
        super();
    }

    public BusinessException(String msg) {
        super(msg);
        this.code = 500;
        this.msg = msg;
    }

    public BusinessException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(Integer code, String msg, Object data) {
        super(msg);
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public BusinessException(BusinessCode businessCode) {
        super(businessCode.getMsg());
        this.code = businessCode.getCode();
        this.msg = businessCode.getMsg();
    }

    public BusinessException(BusinessCode businessCode, Object data) {
        super(businessCode.getMsg());
        this.code = businessCode.getCode();
        this.msg = businessCode.getMsg();
        this.data = data;
    }
}
