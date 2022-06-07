package com.wingice.common.page;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class PageBean {

    private Long pageNum;
    private Long total;
    private Long size;
    private Long current;
    private List<String> ascs = Collections.emptyList();
    private List<String> descs = Collections.emptyList();
    private Boolean optimizeCountSql;
}
