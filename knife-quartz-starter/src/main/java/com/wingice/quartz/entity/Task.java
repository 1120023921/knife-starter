package com.wingice.quartz.entity;

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
 * @author 胡昊
 * @since 2022-02-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("qrtz_task")
@ApiModel(value = "Task对象", description = "")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "任务名")
    private String taskName;

    @ApiModelProperty(value = "任务组")
    private String taskGroup;

    @ApiModelProperty(value = "执行类")
    private String taskClass;

    @ApiModelProperty(value = "任务说明")
    private String note;

    @ApiModelProperty(value = "定时规则")
    private String cron;

    @ApiModelProperty(value = "执行参数")
    private String execParams;

    @ApiModelProperty(value = "执行时间")
    private Long execDate;

    @ApiModelProperty(value = "执行结果（成功:1、失败:0、正在执行：-1)")
    private Integer execResult;

    @ApiModelProperty(value = "是否允许并发，0(false)：不允许 1（true）：允许")
    private Integer concurrent;

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

//    @ApiModelProperty(value = "是否有效 0-未删除 1-已删除")
//    private Integer deleted;

    @ApiModelProperty(value = "额外信息")
    private String extra;

    @ApiModelProperty(value = "租户id")
    private String tenantId;


}
