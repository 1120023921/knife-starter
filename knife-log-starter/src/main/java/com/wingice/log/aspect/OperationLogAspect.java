package com.wingice.log.aspect;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.net.NetUtil;
import cn.hutool.json.JSONUtil;
import com.wingice.log.annotation.OperationLog;
import com.wingice.log.model.SysOperationErrorLog;
import com.wingice.log.model.SysOperationLog;
import com.wingice.log.properties.KnifeLogProperties;
import com.wingice.log.service.SysOperationErrorLogService;
import com.wingice.log.service.SysOperationLogService;
import com.wingice.spring.security.common.utils.SecurityUtils;
import com.wingice.spring.security.model.KnifeUser;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

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

    private static final String UNKNOWN = "unknown";

    private static final String[] HEADERS_TO_TRY = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR",
            "X-Real-IP"
    };

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
    public void saveOperationLog(JoinPoint joinPoint, OperationLog operationLog, Object result) {
        try {
            final SysOperationLog sysOperationLog = new SysOperationLog();
            // 从切面织入点处通过反射机制获取织入点处的方法
            final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            // 获取切入的方法
            final Method method = methodSignature.getMethod();
            //填充基础信息
            sysOperationLog.setOperationModule(operationLog.operationModule());
            sysOperationLog.setOperationType(operationLog.operationType());
            sysOperationLog.setOperationDesc(operationLog.operationDesc());

            // 获取请求的类名
            final String className = joinPoint.getTarget().getClass().getName();
            // 获取请求的方法名
            String methodName = method.getName();
            methodName = className + "." + methodName;
            sysOperationLog.setOperationMethod(methodName);

            // 请求的参数
            sysOperationLog.setOperationRequestParam(JSONUtil.toJsonStr(joinPoint.getArgs()));

            // 返回的参数
            sysOperationLog.setOperationResponseParam(JSONUtil.toJsonStr(result));

            // 用户信息
            final KnifeUser user = SecurityUtils.getUser();
            if (user != null) {
                sysOperationLog.setOperationUserId(user.getId());
                sysOperationLog.setOperationUserName(user.getName());
            }
            // 设置ip
            sysOperationLog.setOperationIp(getRemoteIp(request));
            //设置请求URL
            final StringBuilder url = new StringBuilder(request.getRequestURI() + "?");
            request.getParameterMap().forEach((k, v) -> {
                for (String v1 : v) {
                    url.append(k).append("=").append(v1).append("&");
                }
            });
            url.deleteCharAt(url.length() - 1);
            sysOperationLog.setOperationUrl(url.toString());

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
     * @description: 异常返回通知，用于拦截异常日志信息 连接点抛出异常后执行
     * @author proven
     * @date 2021/6/24 18:53
     */
    @AfterThrowing(pointcut = "@annotation(operationLog)", throwing = "e")
    public void saveExceptionLog(JoinPoint joinPoint, Throwable e, OperationLog operationLog) {
        try {
            final SysOperationErrorLog sysOperationLog = new SysOperationErrorLog();
            // 从切面织入点处通过反射机制获取织入点处的方法
            final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            // 获取切入的方法
            final Method method = methodSignature.getMethod();
            // 获取操作
            sysOperationLog.setOperationModule(operationLog.operationModule());
            sysOperationLog.setOperationType(operationLog.operationType());
            sysOperationLog.setOperationDesc(operationLog.operationDesc());

            // 获取请求的类名
            final String className = joinPoint.getTarget().getClass().getName();
            // 获取请求的方法名
            String methodName = method.getName();
            methodName = className + "." + methodName;
            sysOperationLog.setOperationMethod(methodName);
            // 请求的参数
            sysOperationLog.setOperationRequestParam(JSONUtil.toJsonStr(joinPoint.getArgs()));
            sysOperationLog.setOperationErrorMsg(stackTraceToString(e.getClass().getName(), e.getMessage(), e.getStackTrace()));
            // 用户信息
            final KnifeUser user = SecurityUtils.getUser();
            if (user != null) {
                sysOperationLog.setOperationUserId(user.getId());
                sysOperationLog.setOperationUserName(user.getName());
            }
            // 设置ip
            sysOperationLog.setOperationIp(getRemoteIp(request));
            //设置请求URL
            final StringBuilder url = new StringBuilder(request.getRequestURI() + "?");
            request.getParameterMap().forEach((k, v) -> {
                for (String v1 : v) {
                    url.append(k).append("=").append(v1).append("&");
                }
            });
            url.deleteCharAt(url.length() - 1);
            sysOperationLog.setOperationUrl(url.toString());

            // 设置时间
            sysOperationLog.setOperationCreateTime(System.currentTimeMillis());
            // 设置版本
            sysOperationLog.setOperationVersion(knifeLogProperties.getVersion());
            sysOperationErrorLogService.save(sysOperationLog);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private String stackTraceToString(String exceptionName, String exceptionMessage, StackTraceElement[] elements) {
        StringBuilder strbuff = new StringBuilder();
        for (StackTraceElement stet : elements) {
            strbuff.append(stet).append("\n");
        }
        return exceptionName + ":" + exceptionMessage + "\n\t" + strbuff;
    }

    private String getRemoteIp(HttpServletRequest request) {
        for (String header : HEADERS_TO_TRY) {
            String ip = request.getHeader(header);
            if (StringUtils.hasText(ip) && !UNKNOWN.equalsIgnoreCase(ip)) {
                String reverseProxyIp = NetUtil.getMultistageReverseProxyIp(ip);
                if (Validator.isIpv4(reverseProxyIp) || Validator.isIpv6(reverseProxyIp)) {
                    // 判断是否为IP 返回原始IP
                    return ip;
                }
            }
        }
        // 否则返回 空地址
        return request.getRemoteAddr();
    }
}
