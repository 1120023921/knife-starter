package com.cintsoft.log;

import com.cintsoft.log.aspect.OperationLogAspect;
import com.cintsoft.log.properties.KnifeLogProperties;
import com.cintsoft.log.service.SysOperationErrorLogService;
import com.cintsoft.log.service.SysOperationLogService;
import com.cintsoft.log.service.impl.SysOperationErrorLogServiceImpl;
import com.cintsoft.log.service.impl.SysOperationLogServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
}