package com.wingice.log.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wingice.common.page.EntityPageBean;
import com.wingice.common.web.ErrorCodeInfo;
import com.wingice.common.web.ResultBean;
import com.wingice.log.model.SysOperationLog;
import com.wingice.log.service.SysOperationLogService;
import com.wingice.log.vo.SysOperationLogQueryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 胡昊
 * Description:
 * Date: 2022/1/19
 * Time: 10:19
 * Mail: huhao9277@gmail.com
 */
@Api(value = "/sysOperationLog", tags = "操作日志管理")
@RequestMapping("/sysOperationLog")
public class SysOperationLogController {

    private final SysOperationLogService sysOperationLogService;

    public SysOperationLogController(SysOperationLogService sysOperationLogService) {
        this.sysOperationLogService = sysOperationLogService;
    }

    /**
     * @param sysOperationLogQueryVO 删除条件
     * @description 删除操作日志
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/19 10:52
     */
    @ApiOperation("删除操作日志")
    @PostMapping("/deleteSysOperationLog")
    @ResponseBody
    public ResultBean<Boolean> deleteSysOperationLog(@RequestBody SysOperationLogQueryVO sysOperationLogQueryVO) {
        return ResultBean.restResult(sysOperationLogService.deleteSysOperationLog(sysOperationLogQueryVO), ErrorCodeInfo.OK);
    }

    /**
     * @param entityPageBean 查询条件
     * @description 分页查询操作日志
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/1/19 10:26
     */
    @ApiOperation("分页查询操作日志")
    @PostMapping("/pageSysOperationLog")
    @ResponseBody
    public ResultBean<Page<SysOperationLog>> pageSysOperationLog(@RequestBody EntityPageBean<SysOperationLogQueryVO> entityPageBean) {
        return ResultBean.restResult(sysOperationLogService.pageSysOperationLog(entityPageBean), ErrorCodeInfo.OK);
    }
}
