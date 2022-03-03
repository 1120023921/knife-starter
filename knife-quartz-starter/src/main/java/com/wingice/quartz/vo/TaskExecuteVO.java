package com.wingice.quartz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskExecuteVO implements Serializable {
    private String taskId;
    private String taskName;

    /**
     * 执行参数
     */
    private String dataMap;
}
