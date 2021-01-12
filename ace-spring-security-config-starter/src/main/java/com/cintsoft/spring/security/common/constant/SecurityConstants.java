package com.cintsoft.spring.security.common.constant;

/**
 * @author 胡昊
 * Description:
 * Date: 2021/1/10
 * Time: 18:54
 * Mail: huhao9277@gmail.com
 */
public class SecurityConstants {

    //TOKEN存储前缀（带租户）
    public final static String TOKEN_PREFIX_TENANT_ID = "ACE:TOKEN:%s:%s";
    //TOKEN存储前缀
    public final static String TOKEN_PREFIX = "ACE:TOKEN:%s";
    //用户信息存储前缀（带租户）
    public final static String USER_DETAIL_PREFIX_TENANT_ID = "ACE:USER_DETAIL:%s:%s";
    //用户信息存储前缀
    public final static String USER_DETAIL_PREFIX = "ACE:USER_DETAIL:%s";
    //客户端内部调用标志
    public final static String FROM_IN = "Y";
    //服务端请求严重头
    public final static String FROM = "from";
}
