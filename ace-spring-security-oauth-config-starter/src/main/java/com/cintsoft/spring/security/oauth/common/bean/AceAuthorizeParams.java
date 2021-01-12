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

    public String responseType;
    public String clientId;
    public String clientSecret;
    public String username;
    public String password;
    public String redirectUri;
    public String scope;
    public String state;
    public String tenantId;
}
