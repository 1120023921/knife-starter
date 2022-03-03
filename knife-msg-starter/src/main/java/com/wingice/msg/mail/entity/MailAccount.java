package com.wingice.msg.mail.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 邮件账户信息
 * </p>
 *
 * @author 胡昊
 * @since 2022-01-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("msg_mail_account")
@ApiModel(value = "MailAccount对象", description = "邮件账户信息")
public class MailAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "SMTP服务器地址")
    private String host;

    @ApiModelProperty(value = "端口")
    private Integer port;

    @ApiModelProperty(value = "登陆账号")
    private String username;

    @ApiModelProperty(value = "登陆密码（或授权码）")
    private String password;

    @ApiModelProperty(value = "邮件发信人（即真实邮箱）")
    private String mailFrom;

    @ApiModelProperty(value = "加密方式 1-不加密 2-ssl 3-tls")
    private Integer encryption;

    @ApiModelProperty(value = "是否认证 0-无需认证 1-需要认证")
    private Integer auth;

    @ApiModelProperty(value = "权重")
    private Integer weight;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Long createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;

    @ApiModelProperty(value = "创建者")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty(value = "更新者")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @ApiModelProperty(value = "版本")
    @Version
    private Integer version;

    @ApiModelProperty(value = "是否有效 0-未删除 1-已删除")
    private Integer deleted;

    @ApiModelProperty(value = "额外信息")
    private String extra;

    @ApiModelProperty(value = "租户id")
    private String tenantId;


}
