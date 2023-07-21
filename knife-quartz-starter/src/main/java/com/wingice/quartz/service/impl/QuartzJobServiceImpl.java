package com.wingice.quartz.service.impl;

import cn.hutool.json.JSONUtil;
import com.wingice.quartz.job.BaseJob;
import com.wingice.quartz.job.BaseJobDisallowConcurrent;
import com.wingice.quartz.service.QuartzJobService;
import com.wingice.quartz.vo.QuartzJobVO;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class QuartzJobServiceImpl implements QuartzJobService {

    private final Scheduler scheduler;

    public QuartzJobServiceImpl(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * @param quartzJobVO 添加 Quartz 任务表
     * @description 添加任务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 18:41
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addJob(QuartzJobVO quartzJobVO) throws SchedulerException {
        //是否允许并发执行
        final Class<? extends Job> jobClass = quartzJobVO.getConcurrent().equals(1) ? BaseJob.class : BaseJobDisallowConcurrent.class;

        final String taskName = quartzJobVO.getTaskName();
        final String taskGroup = quartzJobVO.getTaskGroup();

        // 通过任务名称和任务组确定一个任务
        final JobKey jobKey = JobKey.jobKey(taskName, taskGroup);

        //构建job信息
        final JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobKey)
                .withDescription(quartzJobVO.getNote())
                .build();

        // 向 BaseJob 中传递参数
        jobDetail.getJobDataMap().put("quartzJobDetails", JSONUtil.toJsonStr(quartzJobVO));

        //表达式调度构建器(即任务执行的时间)
        final CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(quartzJobVO.getCron());

        final TriggerKey triggerKey = TriggerKey.triggerKey(taskName + "Trigger", taskGroup);

        //按新的cronExpression表达式构建一个新的trigger
        final CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .withSchedule(scheduleBuilder)
                .startNow()
                .build();

        // 把job和触发器注册到任务调度中
        scheduler.scheduleJob(jobDetail, trigger);
        // 启动调度器
        scheduler.start();
    }

    /**
     * @param jobName  类名
     * @param jobGroup 类组名
     * @description 执行任务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 18:41
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void executeJob(String jobName, String jobGroup) throws SchedulerException {
        scheduler.triggerJob(JobKey.jobKey(jobName, jobGroup));
    }

    /**
     * @param jobName  类名
     * @param jobGroup 类组名
     * @description 暂停任务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 18:40
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void pauseJob(String jobName, String jobGroup) throws SchedulerException {
        scheduler.pauseJob(JobKey.jobKey(jobName, jobGroup));
    }

    /**
     * @param jobName  类名
     * @param jobGroup 类组名
     * @description 恢复任务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 18:40
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void resumeJob(String jobName, String jobGroup) throws SchedulerException {
        scheduler.resumeJob(JobKey.jobKey(jobName, jobGroup));
    }

    /**
     * @param jobName  任务名
     * @param jobGroup 类组名
     * @param cron     任务表达式
     * @description 更新任务(只更新 cron 表达式)
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 18:40
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void rescheduleJob(String jobName, String jobGroup, String cron) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName + "Trigger", jobGroup);
        // 表达式调度构建器
        // 1，不触发立即执行
        // 2，等待下次Cron触发频率到达时刻开始按照Cron频率依次执行
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron).withMisfireHandlingInstructionDoNothing();
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        // 按新的cronExpression表达式重新构建trigger
        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
        // 按新的trigger重新设置job执行
        scheduler.rescheduleJob(triggerKey, trigger);
    }

    /**
     * @param jobName  任务类名
     * @param jobGroup 类组名
     * @description 删除任务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 18:39
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteJob(String jobName, String jobGroup) throws SchedulerException {
        scheduler.pauseTrigger(TriggerKey.triggerKey(jobName, jobGroup));
        scheduler.unscheduleJob(TriggerKey.triggerKey(jobName, jobGroup));
        scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));
    }
}
