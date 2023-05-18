package com.wingice.quartz;

import com.wingice.quartz.controller.TaskController;
import com.wingice.quartz.controller.TaskLogController;
import com.wingice.quartz.service.QuartzJobService;
import com.wingice.quartz.service.TaskLogService;
import com.wingice.quartz.service.TaskService;
import com.wingice.quartz.service.impl.QuartzJobServiceImpl;
import com.wingice.quartz.service.impl.TaskLogServiceImpl;
import com.wingice.quartz.service.impl.TaskServiceImpl;
import org.quartz.Scheduler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 胡昊
 * Description:
 * Date: 2022/2/11
 * Time: 10:24
 * Mail: huhao9277@gmail.com
 */
@Configuration
public class KnifeQuartzAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    public QuartzJobService quartzJobService(Scheduler scheduler) {
        return new QuartzJobServiceImpl(scheduler);
    }

    @Bean
    @ConditionalOnMissingBean
    public TaskLogService taskLogService() {
        return new TaskLogServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(name = {"quartzJobService", "taskLogService"})
    public TaskService taskService(QuartzJobService quartzJobService, TaskLogService taskLogService) {
        return new TaskServiceImpl(quartzJobService, taskLogService);
    }

//    @Bean
//    @ConditionalOnMissingBean
//    @ConditionalOnProperty(name = "knife.quartz.quartz-api-enable", havingValue = "true")
//    @ConditionalOnBean(name = "taskService")
//    public TaskController taskController(TaskService taskService) {
//        return new TaskController(taskService);
//    }

//    @Bean
//    @ConditionalOnMissingBean
//    @ConditionalOnProperty(name = "knife.quartz.quartz-api-enable", havingValue = "true")
//    @ConditionalOnBean(name = "taskLogService")
//    public TaskLogController taskLogController(TaskLogService taskLogService) {
//        return new TaskLogController(taskLogService);
//    }
}
