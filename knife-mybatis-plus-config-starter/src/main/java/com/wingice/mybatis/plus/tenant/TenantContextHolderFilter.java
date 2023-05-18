package com.wingice.mybatis.plus.tenant;

import com.wingice.common.mybatis.tenant.TenantContextHolder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
