package com.cintsoft.log.model;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "SysOperationErrorLog对象", description = "异常操作日志")
public class SysOperationErrorLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "功能模块")
    private String operationModule;

    @ApiModelProperty(value = "操作类型")
    private String operationType;

    @ApiModelProperty(value = "操作描述")
    private String operationDesc;

    @ApiModelProperty(value = "请求参数")
    private String operationRequestParam;

    @ApiModelProperty(value = "返回参数")
    private String operationErrorMsg;

    @ApiModelProperty(value = "操作人id")
    private String operationUserId;

    @ApiModelProperty(value = "操作人名称")
    private String operationUserName;

    @ApiModelProperty(value = "操作方法")
    private String operationMethod;

    @ApiModelProperty(value = "请求url")
    private String operationUrl;

    @ApiModelProperty(value = "请求ip")
    private String operationIp;

    @ApiModelProperty(value = "操作时间")
    private Long operationCreateTime;

    @ApiModelProperty(value = "操作版本号")
    private String operationVersion;

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
    private Long version;

    @ApiModelProperty(value = "额外信息")
    private String extra;


}
