package com.cintsoft.spring.security.handler;

import com.cintsoft.spring.security.common.bean.ErrorCodeInfo;
import com.cintsoft.spring.security.common.bean.ResultBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author 胡昊
 * Description: 无权处理器
 * Date: 2020/7/25
 * Time: 16:36
 * Mail: huhao9277@gmail.com
 */
public class AceAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public AceAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        //无权限返回403
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "application/json");
        final PrintWriter out = response.getWriter();
        out.write(objectMapper.writeValueAsString(ResultBean.restResult("不允许访问", ErrorCodeInfo.FORBIDDEN)));
        out.flush();
        out.close();
    }
}
