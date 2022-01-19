package com.cintsoft.log.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cintsoft.common.page.EntityPageBean;
import com.cintsoft.common.web.ErrorCodeInfo;
import com.cintsoft.common.web.ResultBean;
import com.cintsoft.log.model.SysOperationErrorLog;
import com.cintsoft.log.service.SysOperationErrorLogService;
import com.cintsoft.log.vo.SysOperationErrorLogQueryVO;
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
 * Time: 10:53
 * Mail: huhao9277@gmail.com
 */
@Api(value = "/sysOperationErrorLog", tags = "错误日志查询")
@RequestMapping("/sysOperationErrorLog")
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
    @ApiOperation("删除错误日志")
    @PostMapping("/deleteSysOperationErrorLog")
    @ResponseBody
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
    @ApiOperation("分页查询错误日志")
    @PostMapping("/pageSysOperationErrorLog")
    @ResponseBody
    public ResultBean<Page<SysOperationErrorLog>> pageSysOperationErrorLog(@RequestBody EntityPageBean<SysOperationErrorLogQueryVO> entityPageBean) {
        return ResultBean.restResult(sysOperationErrorLogService.pageSysOperationErrorLog(entityPageBean), ErrorCodeInfo.OK);
    }
}
