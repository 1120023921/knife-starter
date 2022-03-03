package com.wingice.quartz.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wingice.common.page.EntityPageBean;
import com.wingice.quartz.entity.Task;
import com.wingice.quartz.exception.KnifeQuartzException;
import com.wingice.quartz.mapper.TaskMapper;
import com.wingice.quartz.quartzenum.TaskExecResultEnum;
import com.wingice.quartz.service.QuartzJobService;
import com.wingice.quartz.service.TaskLogService;
import com.wingice.quartz.service.TaskService;
import com.wingice.quartz.utils.transfer.TransferUtils;
import com.wingice.quartz.vo.QuartzJobVO;
import org.quartz.SchedulerException;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 胡昊
 * @since 2022-02-10
 */
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {

    private final QuartzJobService quartzJobService;
    private final TaskLogService taskLogService;

    public TaskServiceImpl(QuartzJobService quartzJobService, TaskLogService taskLogService) {
        this.quartzJobService = quartzJobService;
        this.taskLogService = taskLogService;
    }

    /**
     * @param task 任务信息
     * @description 添加任务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 20:01
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addTask(Task task) {
        checkTaskAlreadyExists(task.getTaskName(), task.getTaskGroup());
        try {
            save(task);
            // 添加 Quartz 任务表
            QuartzJobVO quartzJobVO = QuartzJobVO.builder().build();
            BeanUtils.copyProperties(task, quartzJobVO);
            // 转换执行参数为 Map
            quartzJobVO.transExecParams(task.getExecParams());
            quartzJobVO.setTaskId(task.getId());
            quartzJobService.addJob(quartzJobVO);
        } catch (SchedulerException e) {
            throw new KnifeQuartzException(e.getMessage());
        }
    }


    /**
     * @param task 任务信息
     * @description 修改任务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 20:08
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateTask(Task task) {
        final String taskName = task.getTaskName();
        final String taskGroup = task.getTaskGroup();

        final Task taskDB = getTask(taskName, taskGroup);
        if (taskDB == null) {
            throw new KnifeQuartzException("查询不到此任务 taskName = " + taskName + " taskGroup = " + taskGroup);
        }

        if (!taskDB.getTaskClass().equals(task.getTaskClass())) {
            throw new KnifeQuartzException("执行类不一致 sourceTaskClass = " + taskDB.getTaskClass() + " targetTaskClass = " + task.getTaskClass());
        }
        try {
            // 更新自定义任务表
            this.updateById(task);

            // 更新 Quartz 框架表，只能先删除旧任务，在添加一个新任务
            quartzJobService.deleteJob(taskName, taskGroup);

            QuartzJobVO quartzJobVO = QuartzJobVO.builder().build();
            BeanUtils.copyProperties(task, quartzJobVO);
            // 转换执行参数为 Map
            quartzJobVO.transExecParams(task.getExecParams());
            quartzJobVO.setTaskId(task.getId());
            quartzJobService.addJob(quartzJobVO);

            // 修改任务后手动执行
//            this.pauseTask(quartzJobVO.getTaskName(), quartzJobVO.getTaskGroup());
        } catch (SchedulerException e) {
            throw new KnifeQuartzException(e.getMessage());
        }
    }

    /**
     * @param taskName  任务名称
     * @param taskGroup 任务组
     * @description 执行任务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 20:09
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void executeTask(String taskName, String taskGroup) {
        try {
            quartzJobService.executeJob(taskName, taskGroup);
        } catch (SchedulerException e) {
            throw new KnifeQuartzException(e.getMessage());
        }
    }

    /**
     * @param taskName  任务名称
     * @param taskGroup 任务组
     * @description 暂停任务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 20:10
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void pauseTask(String taskName, String taskGroup) {
        try {
            quartzJobService.pauseJob(taskName, taskGroup);
        } catch (SchedulerException e) {
            throw new KnifeQuartzException(e.getMessage());
        }
    }

    /**
     * @param taskName  任务名称
     * @param taskGroup 任务组
     * @description 恢复任务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 20:10
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void resumeTask(String taskName, String taskGroup) {
        try {
            quartzJobService.resumeJob(taskName, taskGroup);
        } catch (SchedulerException e) {
            throw new KnifeQuartzException(e.getMessage());
        }
    }

    /**
     * @param taskName  任务类名
     * @param taskGroup 类组名
     * @description 删除任务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 20:20
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteTask(String taskName, String taskGroup) {
        try {
            final Task task = getTask(taskName, taskGroup);
            if (task == null) {
                throw new KnifeQuartzException("任务不存在 taskName = " + taskName + " taskGroup = " + taskGroup);
            }
            // 删除自定义任务表
            removeById(task.getId());
            quartzJobService.deleteJob(taskName, taskGroup);
            // 删除对应任务的执行日志
            taskLogService.deleteByTaskId(task.getId());
        } catch (SchedulerException e) {
            throw new KnifeQuartzException(e.getMessage());
        }
    }

    /**
     * @param taskName  任务类名
     * @param taskGroup 类组名
     * @description 获取单个任务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 20:22
     */
    @Transactional(rollbackFor = Exception.class)
    public Task getTask(String taskName, String taskGroup) {
        return getOne(Wrappers.<Task>lambdaQuery()
                .eq(Task::getTaskName, taskName)
                .eq(Task::getTaskGroup, taskGroup));
    }

    /**
     * @param entityPageBean 查询条件
     * @description 分页查询任务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 20:23
     */
    @Override
    public Page<Task> pageTask(EntityPageBean<Task> entityPageBean) {
        return page(TransferUtils.pageEntityToPage(entityPageBean), Wrappers.lambdaQuery(entityPageBean.getEntity()));
    }

    /**
     * @param taskId     taskId
     * @param resultEnum 执行结果
     * @description 修改任务的最近一次执行结果
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 20:21
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateExecResult(String taskId, TaskExecResultEnum resultEnum) {
        return this.update(Wrappers.<Task>lambdaUpdate()
                .set(Task::getExecResult, resultEnum.getValue())
                .eq(Task::getId, taskId)
        );
    }

    /**
     * @param taskId     任务id
     * @param resultEnum 执行结果
     * @description 更新任务的最近一次执行时间、最近一次执行结果字段
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 20:23
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateExecDateAndExecResult(String taskId, TaskExecResultEnum resultEnum) {
        return this.update(Wrappers.<Task>lambdaUpdate()
                .set(Task::getExecDate, System.currentTimeMillis())
                .set(Task::getExecResult, resultEnum.getValue())
                .eq(Task::getId, taskId)
        );
    }

    /**
     * @param taskName  任务类名
     * @param taskGroup 类组名
     * @description 验证该任务组中此任务是否已存在
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 20:21
     */
    @Transactional(rollbackFor = Exception.class)
    public void checkTaskAlreadyExists(String taskName, String taskGroup) {
        // 每个任务组中的任务名称不能重复
        final int count = count(Wrappers.<Task>lambdaQuery()
                .eq(Task::getTaskName, taskName)
                .eq(Task::getTaskGroup, taskGroup)
        );
        if (count > 0) {
            throw new KnifeQuartzException("该任务组中已存在此任务，请勿重复添加!");
        }
    }
}
