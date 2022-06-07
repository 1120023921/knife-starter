package com.wingice.spring.security.oauth.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 胡昊
 * Description:
 * Date: 2021/1/13
 * Time: 10:56
 * Mail: huhao9277@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class KnifeOAuthClientDetails {

    private static final long serialVersionUID = 1L;

    private String id;

    //客户端ID
    private String clientId;

    //资源列表
    private String resourceIds;

    //客户端secret
    private String clientSecret;

    //域
    private String scope;

    //授权类型
    private String authorizedGrantTypes;

    //回调地址
    private String webServerRedirectUri;

    //授权
    private String authorities;

    //token失效时间
    private Integer accessTokenValidity;

    //refresh_token失效时间
    private Integer refreshTokenValidity;

    //附加信息
    private String additionalInformation;

    //自动接受
    private String autoapprove;

    //客户端名称
    private String clientName;

    //前缀
    private String prefix;

    //私钥
    private String privateKey;

    //公钥
    private String publicKey;

    //创建时间
    private Long createTime;

    //更新时间
    private Long updateTime;

    //创建者
    private String createBy;

    //更新者
    private String updateBy;

    //版本
    private Integer version;

    //是否有效 0无效 1有效
    private Integer deleted;

    //额外信息
    private String extra;

    //租户id
    private String tenantId;
}
