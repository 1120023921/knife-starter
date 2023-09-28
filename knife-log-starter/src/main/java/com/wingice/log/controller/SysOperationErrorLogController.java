package com.wingice.log.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wingice.common.page.EntityPageBean;
import com.wingice.common.web.ErrorCodeInfo;
import com.wingice.common.web.ResultBean;
import com.wingice.log.model.SysOperationErrorLog;
import com.wingice.log.service.SysOperationErrorLogService;
import com.wingice.log.vo.SysOperationErrorLogQueryVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 胡昊
 * Description:
 * Date: 2022/1/19
 * Time: 10:53
 * Mail: huhao9277@gmail.com
 */
@RequestMapping("/sysOperationErrorLog")
@Tag(name = "错误日志查询")
public class SysOperationErrorLogController {

    private final SysOperationErrorLogService sysOperationErrorLogService;

    public SysOperationErrorLogController(SysOperationErrorLogService sysOperationErrorLogService) {
        this.sysOperationErrorLogService = sysOperationErrorLogService;
    }

    /**
     * @param sysOperationErrorLogQueryVO 删除条件
     * @description 删除操作日志
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/19 10:52
     */
    @PostMapping("/deleteSysOperationErrorLog")
    @ResponseBody
    @Operation(summary = "删除错误日志")
    public ResultBean<Boolean> deleteSysOperationErrorLog(@RequestBody SysOperationErrorLogQueryVO sysOperationErrorLogQueryVO) {
        return ResultBean.restResult(sysOperationErrorLogService.deleteSysOperationErrorLog(sysOperationErrorLogQueryVO), ErrorCodeInfo.OK);
    }

    /**
     * @param entityPageBean 查询条件
     * @description 分页查询操作日志
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/19 10:26
     */
    @PostMapping("/pageSysOperationErrorLog")
    @ResponseBody
    @Operation(summary = "分页查询错误日志")
    public ResultBean<Page<SysOperationErrorLog>> pageSysOperationErrorLog(@RequestBody EntityPageBean<SysOperationErrorLogQueryVO> entityPageBean) {
        return ResultBean.restResult(sysOperationErrorLogService.pageSysOperationErrorLog(entityPageBean), ErrorCodeInfo.OK);
    }
}
