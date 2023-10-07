package com.wingice.spring.security;

import com.wingice.spring.security.handler.KnifeAccessDeniedHandler;
import com.wingice.spring.security.handler.KnifeAuthenticationFailureHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;

/**
 * @author 胡昊
 * Description:
 * Date: 2020/7/23
 * Time: 15:25
 * Mail: huhao9277@gmail.com
 */
@AllArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final LogoutHandler knifeLogoutHandler;
    private final KnifeAccessDeniedHandler knifeAccessDeniedHandler;
    private final KnifeAuthenticationFailureHandler knifeAuthenticationFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .anyRequest().permitAll()
                )
                .exceptionHandling((exceptionHandling) ->
                        exceptionHandling
                                .authenticationEntryPoint(knifeAuthenticationFailureHandler)
                                .accessDeniedHandler(knifeAccessDeniedHandler)
                )
                .logout((logout) ->
                        logout.logoutSuccessUrl("/logout").addLogoutHandler(knifeLogoutHandler)
                )
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        // 构建过滤链并返回
        return http.build();
    }
}
