package com.wingice.common.web;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 胡昊
 * Description: 查询参数对象包装类
 * Date: 2020/9/10
 * Time: 17:28
 * Mail: huhao9277@gmail.com
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class QueryParam<T> {

    private T param;

    private String[] ascs;

    private String[] descs;
}
