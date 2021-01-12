package com.cintsoft.spring.security.oauth.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 终端信息表
 * </p>
 *
 * @author 胡昊
 * @since 2021-01-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysOauthClientDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    //客户端ID
    @TableId(value = "client_id", type = IdType.ASSIGN_ID)
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

    //创建时间
    private Long createTime;

    //更新时间
    private Long updateTime;

    //创建者
    private String createBy;

    //更新者
    private String updateBy;

    //版本
    @Version
    private Integer version;

    //是否有效 0无效 1有效
    private Integer deleted;

    //额外信息
    private String extra;

    //租户id
    private String tenantId;


}
