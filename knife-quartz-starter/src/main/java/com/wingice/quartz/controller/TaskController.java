package com.wingice.quartz.controller;


import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wingice.common.page.EntityPageBean;
import com.wingice.common.web.ErrorCodeInfo;
import com.wingice.common.web.ResultBean;
import com.wingice.quartz.entity.Task;
import com.wingice.quartz.exception.KnifeQuartzException;
import com.wingice.quartz.job.BaseTaskExecute;
import com.wingice.quartz.service.TaskService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.quartz.CronExpression;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 胡昊
 * @since 2022-02-10
 */
@RequestMapping("/task")
@Tag(name = "定时任务管理")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * @param task 任务信息
     * @description 添加任务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/11 9:17
     */
    @PostMapping("/addTask")
    @ResponseBody
    @Operation(summary = "添加任务")
    public ResultBean<Boolean> addTask(@RequestBody Task task) {
        checkTaskClassName(task.getTaskClass());
        checkCron(task.getCron());
        taskService.addTask(task);
        return ResultBean.restResult(true, ErrorCodeInfo.OK);
    }

    /**
     * @param task 任务信息
     * @description 更新任务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/11 9:19
     */
    @PostMapping("/updateTask")
    @ResponseBody
    @Operation(summary = "更新任务")
    public ResultBean<Boolean> updateTask(@RequestBody Task task) {
        checkTaskClassName(task.getTaskClass());
        checkCron(task.getCron());
        taskService.updateTask(task);
        return ResultBean.restResult(true, ErrorCodeInfo.OK);
    }

    /**
     * @param taskName  任务名称
     * @param taskGroup 任务组
     * @description 执行任务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/11 10:03
     */
    @PostMapping("/executeTask")
    @ResponseBody
    @Operation(summary = "执行任务")
    public ResultBean<Boolean> executeTask(String taskName, String taskGroup) {
        taskService.executeTask(taskName, taskGroup);
        return ResultBean.restResult(true, ErrorCodeInfo.OK);
    }


    /**
     * @param taskName  任务名称
     * @param taskGroup 任务组
     * @description 暂停任务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/11 10:04
     */
    @PostMapping("/pauseTask")
    @ResponseBody
    @Operation(summary = "暂停任务")
    public ResultBean<Boolean> pauseTask(String taskName, String taskGroup) {
        taskService.pauseTask(taskName, taskGroup);
        return ResultBean.restResult(true, ErrorCodeInfo.OK);
    }

    /**
     * @param taskName  任务名称
     * @param taskGroup 任务组
     * @description 恢复任务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/11 10:05
     */
    @PostMapping("/resumeTask")
    @ResponseBody
    @Operation(summary = "恢复任务")
    public ResultBean<Boolean> resumeTask(String taskName, String taskGroup) {
        taskService.resumeTask(taskName, taskGroup);
        return ResultBean.restResult(true, ErrorCodeInfo.OK);
    }

    /**
     * @param taskName  任务名称
     * @param taskGroup 任务组
     * @description 删除任务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/11 10:05
     */
    @PostMapping("/deleteTask")
    @ResponseBody
    @Operation(summary = "删除任务")
    public ResultBean<Boolean> deleteTask(String taskName, String taskGroup) {
        taskService.deleteTask(taskName, taskGroup);
        return ResultBean.restResult(true, ErrorCodeInfo.OK);
    }

    /**
     * @param entityPageBean 查询条件
     * @description 分页查询定时任务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/11 10:06
     */
    @PostMapping("/pageTask")
    @ResponseBody
    @Operation(summary = "分页查询定时任务")
    public ResultBean<Page<Task>> pageTask(@RequestBody EntityPageBean<Task> entityPageBean) {
        return ResultBean.restResult(taskService.pageTask(entityPageBean), ErrorCodeInfo.OK);
    }

    /**
     * @param cron 表达式
     * @description 验证表达式格式
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/11 10:06
     */
    private void checkCron(String cron) {
        // 验证表达式格式
        if (!CronExpression.isValidExpression(cron)) {
            throw new KnifeQuartzException("表达式格式错误！");
        }
    }

    /**
     * @param taskClass 执行类名
     * @description 检验任务执行类名时候正确
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/11 10:06
     */
    private void checkTaskClassName(String taskClass) {
        final Class<?> clazz;
        try {
            // 获取自定义任务的类
            clazz = Class.forName(taskClass);
        } catch (ClassNotFoundException e) {
            throw new KnifeQuartzException("找不到执行类，请检查执行类是否配置正确！");
        }
        if (BaseTaskExecute.class.getName().equals(taskClass)) {
            throw new KnifeQuartzException(String.format("执行类不可配置为%s！", BaseTaskExecute.class.getSimpleName()));
        }
        try {
            // 获取自定义任务实例，自定义任务全部继承 BaseTaskExecute
            final Object bean = SpringUtil.getBean(clazz);
            if (!(bean instanceof BaseTaskExecute)) {
                throw new KnifeQuartzException(String.format("请确保执行类继承%s类！", BaseTaskExecute.class.getSimpleName()));
            }
        } catch (NoSuchBeanDefinitionException e) {
            throw new KnifeQuartzException("找不到执行类，请检查执行类是否配置@Component注解！");
        }
    }
}

