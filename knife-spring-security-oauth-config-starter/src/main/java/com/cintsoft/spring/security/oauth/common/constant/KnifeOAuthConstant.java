package com.cintsoft.spring.security.oauth.common.constant;

/**
 * @author 胡昊
 * Description:
 * Date: 2021/1/11
 * Time: 21:49
 * Mail: huhao9277@gmail.com
 */
public class KnifeOAuthConstant {

    //code存储路径（带租户）
    public final static String CODE_PREFIX_TENANT_ID = "KNIFE:CODE:%s:%s";
    //code存储路径
    public final static String CODE_PREFIX = "KNIFE:CODE:%s";
    //隐藏式重定向地址（带租户）
    public final static String IMPLICIT_REDIRECT_URL_TENANT_ID = "%s?token=%s&tenantId=%s&state=%s";
    //隐藏式重定向地址
    public final static String IMPLICIT_REDIRECT_URL = "%s?token=%s&state=%s";
    //授权码模式重定向地址（带租户）
    public final static String AUTHORIZATION_CODE_URL_TENANT_ID = "%s?code=%s&tenantId=%s&state=%s";
    //授权码模式重定向地址
    public final static String AUTHORIZATION_CODE_URL = "%s?code=%s&state=%s";
    //grantType-code
    public final static String GRANT_TYPE_CODE = "authorization_code";
    //grantType-token
    public final static String GRANT_TYPE_TOKEN = "implicit";
    //grantType-password
    public final static String GRANT_TYPE_PASSWORD = "password";
    //grantType-client_credentials
    public final static String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";
    //grantType-refresh_token
    public final static String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
}
