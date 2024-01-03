package com.wingice.quartz.job;

import com.wingice.quartz.entity.TaskLog;
import com.wingice.quartz.quartzenum.TaskExecResultEnum;
import com.wingice.quartz.service.TaskLogService;
import com.wingice.quartz.service.TaskService;
import com.wingice.quartz.utils.transfer.DateTimeUtils;
import com.wingice.quartz.vo.TaskExecuteVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public abstract class BaseTaskExecute {

    final private ThreadLocal<TaskExecuteVO> taskExecuteVOThreadLocal = new ThreadLocal<>();
    @Resource
    private TaskService taskService;
    @Resource
    private TaskLogService taskLogService;

    public BaseTaskExecute() {
    }

    public void setTaskExecuteVO(TaskExecuteVO taskExecuteVO) {
        this.taskExecuteVOThreadLocal.set(taskExecuteVO);
    }



    /**
     * 自定义任务子类需实现此方法
     *
     * @param dataMap 添加任务时配置的参数，如：aaa=111;bbb=222
     * @throws Exception the exception
     */
    public abstract void execute(Map<String, Object> dataMap) throws Exception;


    @Transactional(rollbackFor = Exception.class)
    public void execute() {
        // 添加任务执行日志，默认执行中
        final TaskLog taskLog = new TaskLog();
        taskLog.setTaskName(taskExecuteVOThreadLocal.get().getTaskName());
        taskLog.setExecDate(System.currentTimeMillis());
        taskLog.setExecResult(TaskExecResultEnum.EXECUTING.getValue());
        taskLog.setExecResultText("执行中");
        taskLog.setTaskId(taskExecuteVOThreadLocal.get().getTaskId());

        final String logId = taskLogService.addLog(taskLog);

        // 更新任务的最近一次执行时间、最近一次执行结果字段，默认执行中
        taskService.updateExecDateAndExecResult(taskExecuteVOThreadLocal.get().getTaskId(), TaskExecResultEnum.EXECUTING);

        try {

            final long startTime = System.currentTimeMillis();
            // 调用子类的任务
            execute(taskExecuteVOThreadLocal.get().getDataMap());
            final long endTime = System.currentTimeMillis();


            /* 任务执行成功后的操作 */
            // 更新任务的最近一次执行结果为成功
            taskService.updateExecResult(taskExecuteVOThreadLocal.get().getTaskId(), TaskExecResultEnum.SUCCESS);

            // 记录任务执行耗时
            final String format = "任务执行成功：开始时间：[%s]，结束时间：[%s]，共耗时：[%s]";
            taskLogService.updateExecResultAndExecResultText(logId, TaskExecResultEnum.SUCCESS,
                    String.format(format, DateTimeUtils.longToString(startTime), DateTimeUtils.longToString(endTime), endTime - startTime));
        } catch (Exception e) {
            // 将执行结果改为失败并记录异常信息
            taskLogService.updateExecResultToFail(logId, e);
            // 将任务最近一次执行结果改为失败
            taskService.updateExecResult(taskExecuteVOThreadLocal.get().getTaskId(), TaskExecResultEnum.FAILURE);
        }
    }
}
