package com.wingice.quartz.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wingice.common.page.EntityPageBean;
import com.wingice.quartz.entity.Task;
import com.wingice.quartz.quartzenum.TaskExecResultEnum;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 胡昊
 * @since 2022-02-10
 */
public interface TaskService extends IService<Task> {

    /**
     * @param task 任务信息
     * @description 添加任务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 20:01
     */
    void addTask(Task task);

    /**
     * @param task 任务信息
     * @description 修改任务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 20:08
     */
    void updateTask(Task task);

    /**
     * @param taskName  任务名称
     * @param taskGroup 任务组
     * @description 执行任务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 20:09
     */
    void executeTask(String taskName, String taskGroup);

    /**
     * @param taskName  任务名称
     * @param taskGroup 任务组
     * @description 暂停任务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 20:10
     */
    void pauseTask(String taskName, String taskGroup);

    /**
     * @param taskName  任务名称
     * @param taskGroup 任务组
     * @description 恢复任务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 20:10
     */
    void resumeTask(String taskName, String taskGroup);

    /**
     * @param taskName  任务类名
     * @param taskGroup 类组名
     * @description 删除任务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 20:20
     */
    void deleteTask(String taskName, String taskGroup);

    /**
     * @param entityPageBean 查询条件
     * @description 分页查询任务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 20:23
     */
    Page<Task> pageTask(EntityPageBean<Task> entityPageBean);

    /**
     * @param taskId     taskId
     * @param resultEnum 执行结果
     * @description 修改任务的最近一次执行结果
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 20:21
     */
    boolean updateExecResult(String taskId, TaskExecResultEnum resultEnum);

    /**
     * @param taskId     任务id
     * @param resultEnum 执行结果
     * @description 更新任务的最近一次执行时间、最近一次执行结果字段
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 20:23
     */
    boolean updateExecDateAndExecResult(String taskId, TaskExecResultEnum resultEnum);
}
