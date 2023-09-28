package com.wingice.msg.wechat.mp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 表基础信息
 * </p>
 *
 * @author 胡昊
 * @since 2022-01-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("msg_wechat_mp_account")
@Schema(name = "微信公众号账户信息")
public class WechatMpAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "id")
    private String id;

    @Schema(description = "appid")
    private String appid;

    @Schema(description = "secret")
    private String secret;

    @Schema(description = "accessToken")
    private String accessToken;

    @Schema(description = "accessToken过期时间")
    private Long accessTokenExpires;

    @Schema(description = "权重")
    private Integer weight;

    @Schema(description = "创建时间")
    private Long createTime;

    @Schema(description = "更新时间")
    private Long updateTime;

    @Schema(description = "创建者")
    private String createBy;

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
