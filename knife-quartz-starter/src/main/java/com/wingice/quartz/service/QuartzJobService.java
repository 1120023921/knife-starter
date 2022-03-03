package com.wingice.quartz.service;

import com.wingice.quartz.vo.QuartzJobVO;
import org.quartz.SchedulerException;

public interface QuartzJobService {

    /**
     * @param quartzJobVO 添加 Quartz 任务表
     * @description 添加任务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 18:41
     */
    void addJob(QuartzJobVO quartzJobVO) throws SchedulerException;

    /**
     * @param jobName  类名
     * @param jobGroup 类组名
     * @description 执行任务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 18:41
     */
    void executeJob(String jobName, String jobGroup) throws SchedulerException;

    /**
     * @param jobName  类名
     * @param jobGroup 类组名
     * @description 暂停任务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 18:40
     */
    void pauseJob(String jobName, String jobGroup) throws SchedulerException;

    /**
     * @param jobName  类名
     * @param jobGroup 类组名
     * @description 恢复任务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 18:40
     */
    void resumeJob(String jobName, String jobGroup) throws SchedulerException;

    /**
     * @param jobName  任务名
     * @param jobGroup 类组名
     * @param cron     任务表达式
     * @description 更新任务(只更新 cron 表达式)
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 18:40
     */
    void rescheduleJob(String jobName, String jobGroup, String cron) throws SchedulerException;

    /**
     * @param jobName  任务类名
     * @param jobGroup 类组名
     * @description 删除任务
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2022/2/10 18:39
     */
    void deleteJob(String jobName, String jobGroup) throws SchedulerException;
}
