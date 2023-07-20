package com.wingice.msg.mail.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "邮件账户信息")
public class MailAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "id")
    private String id;

    @Schema(description = "SMTP服务器地址")
    private String host;

    @Schema(description = "端口")
    private Integer port;

    @Schema(description = "登陆账号")
    private String username;

    @Schema(description = "登陆密码（或授权码）")
    private String password;

    @Schema(description = "邮件发信人（即真实邮箱）")
    private String mailFrom;

    @Schema(description = "加密方式 1-不加密 2-ssl 3-tls")
    private Integer encryption;

    @Schema(description = "是否认证 0-无需认证 1-需要认证")
    private Integer auth;

    @Schema(description = "权重")
    private Integer weight;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private Long createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private Long updateTime;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建者")
    private String createBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新者")
    private String updateBy;

    @Version
    @Schema(description = "版本")
    private Integer version;

    @Schema(description = "是否有效 0-未删除 1-已删除")
    private Integer deleted;

    @Schema(description = "额外信息")
    private String extra;

    @Schema(description = "租户id")
    private String tenantId;


}
