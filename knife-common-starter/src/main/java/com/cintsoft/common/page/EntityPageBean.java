package com.cintsoft.common.page;

import lombok.Data;

@Data
public class EntityPageBean<T> {

    private PageBean<T> pageObject;

    private T entity;
}