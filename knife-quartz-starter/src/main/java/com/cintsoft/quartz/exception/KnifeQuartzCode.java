package com.cintsoft.quartz.exception;

/**
 * @author 胡昊
 * Description:
 * Date: 2021/3/5
 * Time: 10:53
 * Mail: huhao9277@gmail.com
 */
public enum KnifeQuartzCode {


//    ADD_QUARTZ_TASK_ERROR(400000, "无权操作角色"),
    ;

    private Integer code;
    private String msg;

    KnifeQuartzCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
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
}
