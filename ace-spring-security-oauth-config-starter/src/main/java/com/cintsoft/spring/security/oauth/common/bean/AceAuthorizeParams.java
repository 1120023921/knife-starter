package com.cintsoft.spring.security.oauth.common.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 胡昊
 * Description:
 * Date: 2021/1/12
 * Time: 10:47
 * Mail: huhao9277@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AceAuthorizeParams {

    private String grantType;
    private String clientId;
    private String clientSecret;
    private String username;
    private String password;
    private String redirectUri;
    private String scope;
    private String state;
    private String tenantId;
    private String code;
}
