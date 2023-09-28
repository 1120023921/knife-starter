package com.wingice.quartz.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wingice.common.page.EntityPageBean;
import com.wingice.quartz.entity.TaskLog;
import com.wingice.quartz.mapper.TaskLogMapper;
import com.wingice.quartz.quartzenum.TaskExecResultEnum;
import com.wingice.quartz.service.TaskLogService;
import com.wingice.quartz.utils.transfer.TransferUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 胡昊
 * @since 2022-02-10
 */
public class TaskLogServiceImpl extends ServiceImpl<TaskLogMapper, TaskLog> implements TaskLogService {

    /**
     * @param entityPageBean 查询条件
     * @description 分页查询任务执行日志
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 20:29
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Page<TaskLog> pageTaskLog(EntityPageBean<TaskLog> entityPageBean) {
        return page(TransferUtils.pageEntityToPage(entityPageBean), Wrappers.lambdaQuery(entityPageBean.getEntity()));
    }

    /**
     * @param taskLog 任务日志信息
     * @description 添加一条任务执行日志
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 20:30
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String addLog(TaskLog taskLog) {
        // 添加任务执行日志
        save(taskLog);
        return taskLog.getId();
    }

    /**
     * @param logId              日志id
     * @param taskExecResultEnum 执行结果
     * @param execResultText     日志文本
     * @description 修改任务执行结果、成功信息或抛出的异常信息.
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 20:31
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateExecResultAndExecResultText(String logId, TaskExecResultEnum taskExecResultEnum, String execResultText) {
        return this.update(Wrappers.<TaskLog>lambdaUpdate()
                .set(TaskLog::getExecResult, taskExecResultEnum.getValue())
                .set(TaskLog::getExecResultText, execResultText)
                .eq(TaskLog::getId, logId)
        );
    }

    /**
     * @param logId 日志id
     * @param e     异常信息
     * @description 将执行结果改为失败并记录异常信息
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 20:32
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateExecResultToFail(String logId, Throwable e) {
        return this.update(Wrappers.<TaskLog>lambdaUpdate()
                .set(TaskLog::getExecResult, TaskExecResultEnum.FAILURE.getValue())
                .set(TaskLog::getExecResultText, e.getMessage())
                .eq(TaskLog::getId, logId)
        );
    }


    /**
     * @param taskId 任务id
     * @description 删除任务下日志
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 20:33
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteByTaskId(String taskId) {
        return this.remove(Wrappers.<TaskLog>lambdaUpdate()
                .eq(TaskLog::getTaskId, taskId));
    }
}
