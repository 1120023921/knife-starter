package com.cintsoft.spring.security.handler;

import com.cintsoft.common.web.ErrorCodeInfo;
import com.cintsoft.common.web.ResultBean;
import com.cintsoft.spring.security.common.constant.SecurityConstants;
import com.cintsoft.spring.security.model.KnifeOAuth2AccessToken;
import com.cintsoft.spring.security.model.KnifeUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author 胡昊
 * Description:
 * Date: 2021/1/10
 * Time: 21:11
 * Mail: huhao9277@gmail.com
 */
public class KnifeLogoutTenantHandler implements LogoutHandler {

    private final RedisTemplate<String, KnifeUser> userDatailRedisTemplate;
    private final RedisTemplate<String, KnifeOAuth2AccessToken> tokenRedisTemplate;
    private final ObjectMapper objectMapper;

    public KnifeLogoutTenantHandler(RedisTemplate<String, KnifeUser> userDatailRedisTemplate, RedisTemplate<String, KnifeOAuth2AccessToken> tokenRedisTemplate, ObjectMapper objectMapper) {
        this.userDatailRedisTemplate = userDatailRedisTemplate;
        this.tokenRedisTemplate = tokenRedisTemplate;
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String header = request.getHeader("Authorization");
        final String tenantId = request.getHeader("TENANT_ID");
        if (header != null && header.startsWith("Bearer")) {
            //从token转用户
            final KnifeUser knifeUser = userDatailRedisTemplate.opsForValue().get(String.format(SecurityConstants.USER_DETAIL_PREFIX_TENANT_ID, tenantId, header.split(" ")[1]));
            if (knifeUser != null) {
                //缓存删除用户
                userDatailRedisTemplate.delete(String.format(SecurityConstants.USER_DETAIL_PREFIX_TENANT_ID, tenantId, header.split(" ")[1]));
                //缓存删除Token
                tokenRedisTemplate.delete(String.format(SecurityConstants.ACCESS_TOKEN_PREFIX_TENANT_ID, knifeUser.getTenantId(), knifeUser.getUsername()));
            }
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json");
            final PrintWriter out = response.getWriter();
            out.write(objectMapper.writeValueAsString(ResultBean.restResult("注销成功", ErrorCodeInfo.OK)));
            out.flush();
            out.close();
        }
    }
}