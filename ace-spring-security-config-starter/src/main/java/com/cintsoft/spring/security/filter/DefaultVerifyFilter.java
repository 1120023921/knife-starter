package com.cintsoft.spring.security.filter;

import com.cintsoft.common.web.ErrorCodeInfo;
import com.cintsoft.common.web.ResultBean;
import com.cintsoft.spring.security.common.constant.SecurityConstants;
import com.cintsoft.spring.security.model.AceUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author 胡昊
 * Description:
 * Date: 2020/7/22
 * Time: 10:39
 * Mail: huhao9277@gmail.com
 */
public class DefaultVerifyFilter extends OncePerRequestFilter {

    private final RedisTemplate<String, AceUser> userDetailRedisTemplate;
    private final ObjectMapper objectMapper;

    public DefaultVerifyFilter(RedisTemplate<String, AceUser> userDetailRedisTemplate, ObjectMapper objectMapper) {
        this.userDetailRedisTemplate = userDetailRedisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String header = request.getHeader("Authorization");
        final String tenantId = request.getHeader("TENANT_ID");
        if (header != null && header.startsWith("Bearer")) {
            //从token转用户
            final AceUser aceUser = userDetailRedisTemplate.opsForValue().get(String.format(SecurityConstants.USER_DETAIL_PREFIX, tenantId, header.split(" ")[1]));
            if (aceUser != null) {
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(aceUser, null, aceUser.getAuthorities()));
            } else {
                //token无效
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json");
                final PrintWriter out = response.getWriter();
                out.write(objectMapper.writeValueAsString(ResultBean.restResult("未登录", ErrorCodeInfo.UNAUTHORIZED)));
                out.flush();
                out.close();
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
