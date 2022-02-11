package com.cintsoft.quartz.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cintsoft.common.page.EntityPageBean;
import com.cintsoft.quartz.entity.TaskLog;
import com.cintsoft.quartz.quartzenum.TaskExecResultEnum;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 胡昊
 * @since 2022-02-10
 */
public interface TaskLogService extends IService<TaskLog> {

    /**
     * @param entityPageBean 查询条件
     * @description 分页查询任务执行日志
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 20:29
     */
    Page<TaskLog> pageTaskLog(EntityPageBean<TaskLog> entityPageBean);

    /**
     * @param taskLog 任务日志信息
     * @description 添加一条任务执行日志
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 20:30
     */
    String addLog(TaskLog taskLog);

    /**
     * @param logId              日志id
     * @param taskExecResultEnum 执行结果
     * @param execResultText     日志文本
     * @description 修改任务执行结果、成功信息或抛出的异常信息.
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 20:31
     */
    boolean updateExecResultAndExecResultText(String logId, TaskExecResultEnum taskExecResultEnum, String execResultText);

    /**
     * @param logId 日志id
     * @param e     异常信息
     * @description 将执行结果改为失败并记录异常信息
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 20:32
     */
    boolean updateExecResultToFail(String logId, Throwable e);

    /**
     * @param taskId 任务id
     * @description 删除任务下日志
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 20:33
     */
    boolean deleteByTaskId(String taskId);
}
