package com.wingice.spring.security.oauth.common.constant;


import com.wingice.common.exception.BusinessCode;

/**
 * @author 胡昊
 * Description: 业务异常错误码
 * Date: 2019/7/3
 * Time: 10:40
 * Create: DoubleH
 */
public enum SysOAuthCode {


    AUTHORIZED_GRANT_TYPE_NOE_ALLOW(10000, "AUTHORIZED_GRANT_TYPE未授权"),
    CLIENT_INFO_ERROR(10001, "客户端信息有误"),
    CLIENT_SECRET_INFO_ERROR(10002, "客户端Secret信息有误"),
    AUTHORIZE_FAILED(10003, "认证失败");

    private final BusinessCode businessCode = new BusinessCode();

    SysOAuthCode(Integer code, String msg) {
        this.businessCode.setCode(code);
        this.businessCode.setMsg(msg);
    }

    public BusinessCode getBusinessCode() {
        return businessCode;
    }
}
