package com.cintsoft.spring.security.common.constant;

/**
 * @author 胡昊
 * Description:
 * Date: 2021/1/10
 * Time: 18:54
 * Mail: huhao9277@gmail.com
 */
public class SecurityConstants {

    //ACCESS_TOKEN存储前缀（带租户）
    public final static String ACCESS_TOKEN_PREFIX_TENANT_ID = "ACE2:ACCESS_TOKEN:%s:%s";
    //ACCESS_TOKEN存储前缀
    public final static String ACCESS_TOKEN_PREFIX = "ACE2:ACCESS_TOKEN:%s";
    //REFRESH_TOKEN存储前缀（带租户）
    public final static String REFRESH_TOKEN_PREFIX_TENANT_ID = "ACE2:REFRESH_TOKEN:%s:%s";
    //REFRESH_TOKEN存储前缀
    public final static String REFRESH_TOKEN_PREFIX = "ACE2:REFRESH_TOKEN:%s";
    //用户信息存储前缀（带租户）
    public final static String USER_DETAIL_PREFIX_TENANT_ID = "ACE2:USER_DETAIL:%s:%s";
    //用户信息存储前缀
    public final static String USER_DETAIL_PREFIX = "ACE2:USER_DETAIL:%s";
    //客户端内部调用标志
    public final static String FROM_IN = "Y";
    //服务端请求严重头
    public final static String FROM = "from";
}
