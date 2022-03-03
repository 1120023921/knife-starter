package com.wingice.log.vo;

import com.wingice.log.model.SysOperationErrorLog;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 胡昊
 * Description:
 * Date: 2022/1/19
 * Time: 10:54
 * Mail: huhao9277@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysOperationErrorLogQueryVO extends SysOperationErrorLog {

    @ApiModelProperty("开始时间")
    private Long startTime;
    @ApiModelProperty("结束时间")
    private Long endTime;
}
