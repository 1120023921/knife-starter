package com.cintsoft.common.page;

import lombok.Data;

import java.util.List;

@Data
public class PageBean<T> {

    private Long pageNum;
    private Long total;
    private Long size;
    private Long current;
    private List<String> ascs;
    private List<String> descs;
    private Boolean optimizeCountSql;
}
