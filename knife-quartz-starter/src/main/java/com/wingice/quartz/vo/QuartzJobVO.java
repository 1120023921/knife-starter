package com.wingice.quartz.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuartzJobVO implements Serializable {

    private String taskId;
    private String taskName;
    private String taskGroup;
    private String taskClass;
    private String note;
    private String cron;
    //是否允许并发执行
    private Integer concurrent;
    //执行参数
    private Map<String, Object> dataMap;


    public void transExecParams(String execParams) {
        if (!StringUtils.hasText(execParams)) {
            return;
        }
        dataMap = new HashMap<>();
        final String[] paramList = execParams.split(";");
        for (String param : paramList) {
            dataMap.put(param.split("=")[0], param.split("=")[1]);
        }
    }
}
