package com.cintsoft.mybatis.plus.tenant;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class TenantContextHolderFilter extends GenericFilterBean {

    @Override
    @SneakyThrows
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        final String tenantId = request.getHeader("TENANT_ID");
        log.debug("当前header中的租户id=" + tenantId);

        if (StringUtils.hasText(tenantId)) {
            TenantContextHolder.setTenantId(tenantId);
        } else {
            TenantContextHolder.setTenantId("0");
        }
        filterChain.doFilter(request, response);
        TenantContextHolder.clear();
    }
}
