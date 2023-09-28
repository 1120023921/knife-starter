package com.wingice.msg.dingtalk.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 钉钉账户信息
 * </p>
 *
 * @author 胡昊
 * @since 2023-01-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("msg_dingtalk_account")
@Schema(name = "钉钉账户信息")
public class DingtalkAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "id")
    private String id;

    @Schema(description = "appKey")
    private String appKey;

    @Schema(description = "appSecret")
    private String appSecret;

    @Schema(description = "accessToken")
    private String accessToken;

    @Schema(description = "accessToken过期时间")
    private Long accessTokenExpires;

    @Schema(description = "权重")
    private Integer weight;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private Long createTime;

    @TableField(fill = FieldFill.UPDATE)
    @Schema(description = "更新时间")
    private Long updateTime;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建者")
    private String createBy;

    @TableField(fill = FieldFill.UPDATE)
    @Schema(description = "更新者")
    private String updateBy;

    @Version
    @Schema(description = "版本")
    private Long version;

    @TableLogic
    @Schema(description = "是否有效 0-未删除 1-已删除")
    private Integer deleted;

    @Schema(description = "额外信息")
    private String extra;

    @Schema(description = "租户id")
    private String tenantId;
}
