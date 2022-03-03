package com.wingice.quartz.quartzenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务执行结果
 */
@AllArgsConstructor
@Getter
public enum TaskExecResultEnum {

    /**
     * 任务执行成功
     */
    SUCCESS(1),

    /**
     * 任务执行失败
     */
    FAILURE(0),

    /**
     * 正在执行
     */
    EXECUTING(-1),

    ;

    private final Integer value;
}
