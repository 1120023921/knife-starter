package com.wingice.log;

import com.wingice.log.aspect.OperationLogAspect;
import com.wingice.log.controller.SysOperationErrorLogController;
import com.wingice.log.controller.SysOperationLogController;
import com.wingice.log.properties.KnifeLogProperties;
import com.wingice.log.service.SysOperationErrorLogService;
import com.wingice.log.service.SysOperationLogService;
import com.wingice.log.service.impl.SysOperationErrorLogServiceImpl;
import com.wingice.log.service.impl.SysOperationLogServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 胡昊
 * Description: Mybatis配置
 * Date: 2020/7/23
 * Time: 10:57
 * Mail: huhao9277@gmail.com
 */
@Configuration
public class KnifeLogAutoConfig {

    /**
     * @description 自动填充默认配置
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/1/10 11:41
     */
    @Bean
    @ConditionalOnMissingBean(SysOperationLogService.class)
    public SysOperationLogService sysOperationLogService() {
        return new SysOperationLogServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public SysOperationErrorLogService sysOperationErrorLogService() {
        return new SysOperationErrorLogServiceImpl();
    }

    /**
     * @param request 请求信息
     * @description 日志记录拦截器
     * @author 胡昊
     * @email huhao9277@gmail.com
     * @date 2021/6/29 16:57
     */
    @ConditionalOnMissingBean
    @Bean
    public OperationLogAspect operationLogAspect(KnifeLogProperties knifeLogProperties, SysOperationLogService sysOperationLogService, SysOperationErrorLogService sysOperationErrorLogService, HttpServletRequest request) {
        return new OperationLogAspect(request, knifeLogProperties, sysOperationErrorLogService, sysOperationLogService);
    }

    @Bean
    @ConditionalOnProperty(name = "knife.log.log-api-enable", havingValue = "true")
    @ConditionalOnBean(name = {"sysOperationLogService"})
    public SysOperationLogController sysOperationLogController(SysOperationLogService sysOperationLogService) {
        return new SysOperationLogController(sysOperationLogService);
    }

    @Bean
    @ConditionalOnProperty(name = "knife.log.log-api-enable", havingValue = "true")
    @ConditionalOnBean(name = {"sysOperationErrorLogService"})
    public SysOperationErrorLogController sysOperationErrorLogController(SysOperationErrorLogService sysOperationErrorLogService) {
        return new SysOperationErrorLogController(sysOperationErrorLogService);
    }
}
