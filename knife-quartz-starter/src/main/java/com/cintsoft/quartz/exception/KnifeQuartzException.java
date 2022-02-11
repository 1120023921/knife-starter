package com.cintsoft.quartz.exception;


/**
 * @author 胡昊
 * Description:
 * Date: 2022/2/10
 * Time: 19:51
 * Mail: huhao9277@gmail.com
 */
public class KnifeQuartzException extends RuntimeException {


    private Integer code;
    private String msg;
    private String errorMsg;

    public KnifeQuartzException() {
        super();
    }

    public KnifeQuartzException(String msg) {
        super(msg);
        this.code = 500;
        this.msg = msg;
    }

    public KnifeQuartzException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public KnifeQuartzException(Integer code, String msg, String errorMsg) {
        super(msg);
        this.code = code;
        this.msg = msg;
        this.errorMsg = errorMsg;
    }


    public KnifeQuartzException(KnifeQuartzCode knifeQuartzCode) {
        super(knifeQuartzCode.getMsg());
        this.code = knifeQuartzCode.getCode();
        this.msg = knifeQuartzCode.getMsg();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}
