package com.wingice.spring.security.handler;

import com.wingice.common.web.ErrorCodeInfo;
import com.wingice.common.web.ResultBean;
import com.wingice.spring.security.common.constant.KnifeSecurityConfigProperties;
import com.wingice.spring.security.model.KnifeOAuth2AccessToken;
import com.wingice.spring.security.model.KnifeUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author 胡昊
 * Description:
 * Date: 2021/1/10
 * Time: 21:11
 * Mail: huhao9277@gmail.com
 */
public class KnifeLogoutHandler implements LogoutHandler {

    private final RedisTemplate<String, KnifeUser> userDetailRedisTemplate;
    private final RedisTemplate<String, KnifeOAuth2AccessToken> tokenRedisTemplate;
    private final RedisTemplate<String, String> userRefreshTokenRedisTemplate;
    private final KnifeSecurityConfigProperties knifeSecurityConfigProperties;
    private final ObjectMapper objectMapper;

    public KnifeLogoutHandler(RedisTemplate<String, KnifeUser> userDetailRedisTemplate, RedisTemplate<String, KnifeOAuth2AccessToken> tokenRedisTemplate, RedisTemplate<String, String> userRefreshTokenRedisTemplate, KnifeSecurityConfigProperties knifeSecurityConfigProperties, ObjectMapper objectMapper) {
        this.userDetailRedisTemplate = userDetailRedisTemplate;
        this.tokenRedisTemplate = tokenRedisTemplate;
        this.userRefreshTokenRedisTemplate = userRefreshTokenRedisTemplate;
        this.knifeSecurityConfigProperties = knifeSecurityConfigProperties;
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer")) {
            //从token转用户
            final KnifeUser knifeUser = userDetailRedisTemplate.opsForValue().get(String.format(knifeSecurityConfigProperties.getUserDetailPrefix(), header.split(" ")[1]));
            if (knifeUser != null) {
                tokenRedisTemplate.delete(String.format(knifeSecurityConfigProperties.getAccessTokenPrefix(), knifeUser.getUsername()));
                userDetailRedisTemplate.delete(String.format(knifeSecurityConfigProperties.getUserDetailPrefix(), header.split(" ")[1]));
                String refreshToken = userRefreshTokenRedisTemplate.opsForValue().get(String.format(knifeSecurityConfigProperties.getUserRefreshTokenPrefix(), knifeUser.getUsername()));
                if (StringUtils.hasText(refreshToken)) {
                    userDetailRedisTemplate.delete(String.format(knifeSecurityConfigProperties.getRefreshTokenPrefix(), refreshToken));
                    userRefreshTokenRedisTemplate.delete(String.format(knifeSecurityConfigProperties.getUserRefreshTokenPrefix(), knifeUser.getUsername()));
                }
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
