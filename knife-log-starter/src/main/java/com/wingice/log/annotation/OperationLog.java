package com.wingice.log.annotation;

import java.lang.annotation.*;

/**
 * @author weilong
 * @version 1.0
 * @description: 自定义操作日志注解
 * @date 2021-06-24
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {

    // 操作模块
    String operationModule() default "";

    // 操作类型
    String operationType() default "";

    // 操作说明
    String operationDesc() default "";
}
