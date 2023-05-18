package com.wingice.spring.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wingice.common.web.ErrorCodeInfo;
import com.wingice.common.web.ResultBean;
import com.wingice.spring.security.common.constant.KnifeSecurityConfigProperties;
import com.wingice.spring.security.model.KnifeUser;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/**
 * @author 胡昊
 * Description:
 * Date: 2020/7/22
 * Time: 10:39
 * Mail: huhao9277@gmail.com
 */
public class KnifeVerifyFilter extends OncePerRequestFilter {

    private final RedisTemplate<String, KnifeUser> userDetailRedisTemplate;
    private final KnifeSecurityConfigProperties knifeSecurityConfigProperties;

    public KnifeVerifyFilter(RedisTemplate<String, KnifeUser> userDetailRedisTemplate, KnifeSecurityConfigProperties knifeSecurityConfigProperties) {
        this.userDetailRedisTemplate = userDetailRedisTemplate;
        this.knifeSecurityConfigProperties = knifeSecurityConfigProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer")) {
            //从token转用户
            final KnifeUser knifeUser = userDetailRedisTemplate.opsForValue().get(String.format(knifeSecurityConfigProperties.getUserDetailPrefix(), header.split(" ")[1]));
            if (knifeUser != null) {
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(knifeUser, null, knifeUser.getAuthorities()));
            }
        }
        filterChain.doFilter(request, response);
    }
}
