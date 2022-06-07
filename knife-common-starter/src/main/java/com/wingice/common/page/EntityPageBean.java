package com.wingice.common.page;

import lombok.Data;

@Data
public class EntityPageBean<T> {

    private PageBean pageObject;

    private T entity;
}
