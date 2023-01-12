package com.wingice.msg.dingtalk.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "DingtalkAccount对象", description = "钉钉账户信息")
public class DingtalkAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("appKey")
    private String appKey;

    @ApiModelProperty("appSecret")
    private String appSecret;

    @ApiModelProperty(value = "accessToken")
    private String accessToken;

    @ApiModelProperty(value = "accessToken过期时间")
    private Long accessTokenExpires;

    @ApiModelProperty("权重")
    private Integer weight;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Long createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateTime;

    @ApiModelProperty("创建者")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty("更新者")
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    @ApiModelProperty("版本")
    @Version
    private Long version;

    @ApiModelProperty("是否有效 0-未删除 1-已删除")
    @TableLogic
    private Integer deleted;

    @ApiModelProperty("额外信息")
    private String extra;

    @ApiModelProperty("租户id")
    private String tenantId;
}
