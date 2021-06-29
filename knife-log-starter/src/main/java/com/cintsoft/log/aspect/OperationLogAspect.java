package com.cintsoft.log.aspect;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.cintsoft.log.annotation.OperationLog;
import com.cintsoft.log.model.SysOperationErrorLog;
import com.cintsoft.log.model.SysOperationLog;
import com.cintsoft.log.properties.KnifeLogProperties;
import com.cintsoft.log.service.SysOperationErrorLogService;
import com.cintsoft.log.service.SysOperationLogService;
import com.cintsoft.spring.security.common.utils.SecurityUtils;
import com.cintsoft.spring.security.model.KnifeUser;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author proven
 * @version 1.0
 * @description: 切面处理类，操作日志异常日志记录处理
 * @date 2021/6/24 17:51
 */
@Aspect
public class OperationLogAspect {

    private final HttpServletRequest request;

    private final KnifeLogProperties knifeLogProperties;

    private final SysOperationErrorLogService sysOperationErrorLogService;

    private final SysOperationLogService sysOperationLogService;

    public OperationLogAspect(HttpServletRequest request, KnifeLogProperties knifeLogProperties, SysOperationErrorLogService sysOperationErrorLogService, SysOperationLogService sysOperationLogService) {
        this.request = request;
        this.knifeLogProperties = knifeLogProperties;
        this.sysOperationErrorLogService = sysOperationErrorLogService;
        this.sysOperationLogService = sysOperationLogService;
    }

    /**
     * @description: 正常返回通知，拦截用户操作日志，连接点正常执行完成后执行， 如果连接点抛出异常，则不会执行
     * @author proven
     * @date 2021/6/24 18:27
     */
    @AfterReturning(pointcut = "@annotation(operationLog)", returning = "result")
    public void saveOperationLog(JoinPoint joinPoint, OperationLog operationLog, String result) {

        SysOperationLog sysOperationLog = new SysOperationLog();

        try {
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            // 获取切入的方法
            Method method = methodSignature.getMethod();

            if (ObjectUtil.isNotNull(operationLog)) {
                sysOperationLog.setOperationModule(operationLog.operationModule());
                sysOperationLog.setOperationType(operationLog.operationType());
                sysOperationLog.setOperationDesc(operationLog.operationDesc());
            }

            // 获取请求的类型
            String className = joinPoint.getTarget().getClass().getName();
            // 获取请求的方法名
            String methodName = method.getName();
            methodName = className + "." + methodName;
            sysOperationLog.setOperationMethod(methodName);

            // 请求的参数
            assert request != null;
            Map<String, String> paramMap = converMap(request.getParameterMap());
            String params = JSONUtil.parseObj(paramMap).toString();
            sysOperationLog.setOperationRequestParam(params);

            // 返回的参数
            sysOperationLog.setOperationResponseParam(result);

            // 用户信息
            final KnifeUser user = SecurityUtils.getUser();
            if (ObjectUtil.isNotNull(user)) {
                sysOperationLog.setOperationUserId(user.getId());
                sysOperationLog.setOperationUserName(user.getName());
            }
            // 设置ip
            sysOperationLog.setOperationIp(request.getRemoteAddr());
            sysOperationLog.setOperationUrl(request.getRequestURI());

            // 设置时间
            sysOperationLog.setOperationCreateTime(System.currentTimeMillis());
            // 设置版本
            sysOperationLog.setOperationVersion(knifeLogProperties.getVersion());
            sysOperationLogService.save(sysOperationLog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @description: 转换request 请求参数
     * @author proven
     * @date 2021/6/24 18:40
     */
    private Map<String, String> converMap(Map<String, String[]> parameterMap) {
        Map<String, String> rtnMap = new HashMap<>();
        for (String key : parameterMap.keySet()) {
            rtnMap.put(key, parameterMap.get(key)[0]);
        }
        return rtnMap;
    }

    /**
     * @description: 异常返回通知，用于拦截异常日志信息 连接点抛出异常后执行
     * @author proven
     * @date 2021/6/24 18:53
     */
    @AfterThrowing(pointcut = "@annotation(operationLog)", throwing = "e")
    public void saveExceptionLog(JoinPoint joinPoint, Throwable e, OperationLog operationLog) {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        if (ObjectUtil.isNull(requestAttributes)) {
            return;
        }
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        SysOperationErrorLog sysOperationLog = new SysOperationErrorLog();

        try {
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            // 获取切入的方法
            Method method = methodSignature.getMethod();
            // 获取操作
            if (ObjectUtil.isNotNull(operationLog)) {
                sysOperationLog.setOperationModule(operationLog.operationModule());
                sysOperationLog.setOperationType(operationLog.operationType());
                sysOperationLog.setOperationDesc(operationLog.operationDesc());
            }

            // 获取请求的类型
            String className = joinPoint.getTarget().getClass().getName();
            // 获取请求的方法名
            String methodName = method.getName();
            methodName = className + "." + methodName;
            sysOperationLog.setOperationMethod(methodName);
            // 请求的参数
            assert request != null;
            Map<String, String> paramMap = converMap(request.getParameterMap());
            String params = JSONUtil.parseObj(paramMap).toString();
            sysOperationLog.setOperationRequestParam(params);
            sysOperationLog.setOperationErrorMsg(stackTraceToString(e.getClass().getName(), e.getMessage(), e.getStackTrace()));
            // 用户信息
            final KnifeUser user = SecurityUtils.getUser();
            if (ObjectUtil.isNotNull(user)) {
                sysOperationLog.setOperationUserId(user.getId());
                sysOperationLog.setOperationUserName(user.getName());
            }
            // 设置ip
            sysOperationLog.setOperationIp(request.getRemoteAddr());
            sysOperationLog.setOperationUrl(request.getRequestURI());

            // 设置时间
            sysOperationLog.setOperationCreateTime(System.currentTimeMillis());
            // 设置版本
            sysOperationLog.setOperationVersion(knifeLogProperties.getVersion());
            sysOperationErrorLogService.save(sysOperationLog);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * @description: 转换异常信息为字符串
     * @author proven
     * @date 2021/6/24 18:56
     */
    public String stackTraceToString(String exceptionName, String exceptionMessage, StackTraceElement[] elements) {
        StringBuilder strbuff = new StringBuilder();
        for (StackTraceElement stet : elements) {
            strbuff.append(stet).append("\n");
        }
        return exceptionName + ":" + exceptionMessage + "\n\t" + strbuff;
    }
}
