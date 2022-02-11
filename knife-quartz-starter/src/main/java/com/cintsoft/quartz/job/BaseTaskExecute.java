package com.cintsoft.quartz.job;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.cintsoft.quartz.entity.TaskLog;
import com.cintsoft.quartz.quartzenum.TaskExecResultEnum;
import com.cintsoft.quartz.service.TaskLogService;
import com.cintsoft.quartz.service.TaskService;
import com.cintsoft.quartz.utils.transfer.DateTimeUtils;
import com.cintsoft.quartz.vo.TaskExecuteVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

@Service
public abstract class BaseTaskExecute {

    private TaskExecuteVO taskExecuteVO;
    @Resource
    private TaskService taskService;
    @Resource
    private TaskLogService taskLogService;

    public BaseTaskExecute() {
    }

    public void setTaskExecuteVO(TaskExecuteVO taskExecuteVO) {
        this.taskExecuteVO = taskExecuteVO;
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
        taskLog.setTaskName(taskExecuteVO.getTaskName());
        taskLog.setExecDate(System.currentTimeMillis());
        taskLog.setExecResult(TaskExecResultEnum.EXECUTING.getValue());
        taskLog.setExecResultText("执行中");
        taskLog.setTaskId(taskExecuteVO.getTaskId());

        final String logId = taskLogService.addLog(taskLog);

        // 更新任务的最近一次执行时间、最近一次执行结果字段，默认执行中
        taskService.updateExecDateAndExecResult(taskExecuteVO.getTaskId(), TaskExecResultEnum.EXECUTING);

        try {
            // 获取任务携带的参数
            final Map<String, Object> dataMap = JSONUtil.toBean(taskExecuteVO.getDataMap(), new TypeReference<Map<String, Object>>() {
            }, false);

            final long startTime = System.currentTimeMillis();
            // 调用子类的任务
            execute(dataMap);
            final long endTime = System.currentTimeMillis();


            /* 任务执行成功后的操作 */
            // 更新任务的最近一次执行结果为成功
            taskService.updateExecResult(taskExecuteVO.getTaskId(), TaskExecResultEnum.SUCCESS);

            // 记录任务执行耗时
            final String format = "任务执行成功：开始时间：[%s]，结束时间：[%s]，共耗时：[%s]";
            taskLogService.updateExecResultAndExecResultText(logId, TaskExecResultEnum.SUCCESS,
                    String.format(format, DateTimeUtils.longToString(startTime), DateTimeUtils.longToString(endTime), endTime - startTime));
        } catch (Exception e) {
            // 将执行结果改为失败并记录异常信息
            taskLogService.updateExecResultToFail(logId, e);
            // 将任务最近一次执行结果改为失败
            taskService.updateExecResult(taskExecuteVO.getTaskId(), TaskExecResultEnum.FAILURE);
        }
    }
}
