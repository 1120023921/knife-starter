package com.wingice.quartz.entity;

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
 * @author 胡昊
 * @since 2022-02-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("qrtz_task_log")
@Schema(name = "")
public class TaskLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "自增主键")
    private String id;

    @Schema(description = "任务名")
    private String taskName;

    @Schema(description = "执行时间")
    private Long execDate;

    @Schema(description = "执行结果（成功:1、失败:0、正在执行：-1)")
    private Integer execResult;

    @Schema(description = "成功信息或抛出的异常信息")
    private String execResultText;

    @Schema(description = "任务id")
    private String taskId;

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

    @Schema(description = "租户id")
    private String tenantId;


}
