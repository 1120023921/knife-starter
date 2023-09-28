package com.wingice.log.vo;

import com.wingice.log.model.SysOperationLog;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 胡昊
 * Description:
 * Date: 2022/1/19
 * Time: 10:40
 * Mail: huhao9277@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysOperationLogQueryVO extends SysOperationLog {

    @Schema(description = "开始时间")
    private Long startTime;
    @Schema(description = "结束时间")
    private Long endTime;
}
