package com.cintsoft.spring.security;

import com.cintsoft.spring.security.handler.AceAccessDeniedHandler;
import com.cintsoft.spring.security.handler.AceAuthenticationFailureHandler;
import com.cintsoft.spring.security.handler.AceLogoutHandler;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

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
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationManager authenticationManager;
    private final AceLogoutHandler aceLogoutHandler;
    private final AceAccessDeniedHandler aceAccessDeniedHandler;
    private final AceAuthenticationFailureHandler aceAuthenticationFailureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers("/login", "/logout", "/anon/**")
                .permitAll()
                .anyRequest()
                .permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(aceAuthenticationFailureHandler)
                .accessDeniedHandler(aceAccessDeniedHandler)
                .and()
                .logout().logoutSuccessUrl("/logout").addLogoutHandler(aceLogoutHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public AuthenticationManager authenticationManager() {
        return authenticationManager;
    }
}
