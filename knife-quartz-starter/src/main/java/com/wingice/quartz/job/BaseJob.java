package com.wingice.quartz.job;


import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.wingice.quartz.exception.KnifeQuartzException;
import com.wingice.quartz.vo.QuartzJobVO;
import com.wingice.quartz.vo.TaskExecuteVO;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.BeanUtils;

public class BaseJob implements Job {

    @Override
    public void execute(JobExecutionContext context) {
        // 获取 Job 中定义属性 （调用任务的时候传递进来的参数）
        final JobDataMap data = context.getJobDetail().getJobDataMap();

        // 获取创建任务时传递过来的参数
        final QuartzJobVO quartzJobDetails = JSONUtil.toBean((String) data.get("quartzJobDetails"), QuartzJobVO.class);

        try {
            // 获取自定义任务的类
            final Class<?> clazz = Class.forName(quartzJobDetails.getTaskClass());
            // 获取自定义任务实例，自定义任务全部继承 TaskExecute

            final BaseTaskExecute baseTaskExecute = (BaseTaskExecute) SpringUtil.getBean(clazz);

            final TaskExecuteVO taskExecuteVO = TaskExecuteVO.builder().build();
            BeanUtils.copyProperties(quartzJobDetails, taskExecuteVO);

            baseTaskExecute.setTaskExecuteVO(taskExecuteVO);
            baseTaskExecute.execute();
        } catch (Exception e) {
            final String errorMessage = String.format("任务: [%s] 未执行成功，请检查执行类是否配置正确！！！", quartzJobDetails.getTaskName());
            throw new KnifeQuartzException(errorMessage);
        }
    }
}
