package com.cintsoft.spring.security.oauth.common.constant;


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
    AUTHORIZE_FAILED(10002, "认证失败");

    private final com.cintsoft.common.exception.BusinessCode businessCode = new com.cintsoft.common.exception.BusinessCode();

    SysOAuthCode(Integer code, String msg) {
        this.businessCode.setCode(code);
        this.businessCode.setMsg(msg);
    }

    public com.cintsoft.common.exception.BusinessCode getBusinessCode() {
        return businessCode;
    }
}
