package com.cintsoft.spring.security.annotation;

import java.lang.annotation.*;

/**
 * @author : wangzy
 * <p> 创建时间: 2019-04-02 </p>
 * <p> 描述: </p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Inner {

    /**
     * 是否AOP统一处理
     *
     * @return false, true
     */
    boolean value() default true;

    /**
     * 需要特殊判空的字段(预留)
     *
     * @return {}
     */
    String[] field() default {};
}
