package com.wingice.spring.security;

import com.wingice.spring.security.handler.KnifeAccessDeniedHandler;
import com.wingice.spring.security.handler.KnifeAuthenticationFailureHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
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
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationManager authenticationManager;
    private final LogoutHandler knifeLogoutHandler;
    private final KnifeAccessDeniedHandler knifeAccessDeniedHandler;
    private final KnifeAuthenticationFailureHandler knifeAuthenticationFailureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .cors()
                .and()
                .authorizeRequests()
//                .antMatchers("/login", "/logout", "/anon/**", "/oauth/logout/pwd", "/oauth/token", "/captcha/generateCaptcha", "/sysOauthClientDetails/getSysOauthClientDetailsByClientIdAndSecret")
//                .permitAll()
                .anyRequest()
                .permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(knifeAuthenticationFailureHandler)
                .accessDeniedHandler(knifeAccessDeniedHandler)
                .and()
                .logout().logoutSuccessUrl("/logout").addLogoutHandler(knifeLogoutHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public AuthenticationManager authenticationManager() {
        return authenticationManager;
    }
}
