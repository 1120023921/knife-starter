package com.wingice.log.model;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author proven
 * @since 2021-06-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "普通操作日志")
public class SysOperationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "id")
    private String id;

    @Schema(description = "功能模块")
    private String operationModule;

    @Schema(description = "操作类型")
    private String operationType;

    @Schema(description = "操作描述")
    private String operationDesc;

    @Schema(description = "请求参数")
    private String operationRequestParam;

    @Schema(description = "返回参数")
    private String operationResponseParam;

    @Schema(description = "操作人id")
    private String operationUserId;

    @Schema(description = "操作人名称")
    private String operationUserName;

    @Schema(description = "操作方法")
    private String operationMethod;

    @Schema(description = "请求url")
    private String operationUrl;

    @Schema(description = "请求ip")
    private String operationIp;

    @Schema(description = "操作时间")
    private Long operationCreateTime;

    @Schema(description = "操作版本号")
    private String operationVersion;

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
    private Long version;

    @Schema(description = "额外信息")
    private String extra;


}
