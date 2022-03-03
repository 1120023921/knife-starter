package com.wingice.quartz.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wingice.common.page.EntityPageBean;
import com.wingice.common.web.ErrorCodeInfo;
import com.wingice.common.web.ResultBean;
import com.wingice.quartz.entity.TaskLog;
import com.wingice.quartz.service.TaskLogService;
import com.wingice.quartz.utils.transfer.TransferUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 胡昊
 * @since 2022-02-10
 */
@Api(value = "/taskLog", tags = "定时任务日志")
@RequestMapping("/taskLog")
public class TaskLogController {

    private final TaskLogService taskLogService;

    public TaskLogController(TaskLogService taskLogService) {
        this.taskLogService = taskLogService;
    }

    /**
     * @param entityPageBean 查找条件
     * @description 分页查询任务日志
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/11 10:13
     */
    @ApiOperation("分页查询任务日志")
    @PostMapping("/pageTaskLog")
    @ResponseBody
    public ResultBean<Page<TaskLog>> pageTaskLog(@RequestBody EntityPageBean<TaskLog> entityPageBean) {
        return ResultBean.restResult(taskLogService.page(TransferUtils.pageEntityToPage(entityPageBean), Wrappers.lambdaQuery(entityPageBean.getEntity())), ErrorCodeInfo.OK);
    }

    /**
     * @param idList 待删除id列表
     * @description 删除任务日志
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/11 10:15
     */
    @ApiOperation("删除任务日志")
    @PostMapping("/deleteTaskLog")
    @ResponseBody
    public ResultBean<Boolean> deleteTaskLog(@RequestBody List<String> idList) {
        return ResultBean.restResult(taskLogService.removeByIds(idList), ErrorCodeInfo.OK);
    }
}

