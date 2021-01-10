package com.cintsoft.spring.security.provider;

import com.cintsoft.spring.security.model.AceUser;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author 胡昊
 * Description: 数据库认证
 * Date: 2020/7/27
 * Time: 10:43
 * Mail: huhao9277@gmail.com
 */
public class AceDaoAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public AceDaoAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = (String) authentication.getPrincipal();
        final String password = (String) authentication.getCredentials();
        final AceUser aceUser = (AceUser) userDetailsService.loadUserByUsername(username);
        if (aceUser != null) {
            if (!aceUser.getIsAccountNonLocked()) {
                throw new LockedException("User account is locked");
            }
            if (!aceUser.getIsEnabled()) {
                throw new DisabledException("User is disabled");
            }
            if (!aceUser.getIsAccountNonExpired()) {
                throw new AccountExpiredException("User account has expired");
            }
            if (!aceUser.getIsCredentialsNonExpired()) {
                throw new CredentialsExpiredException("User credentials has expired");
            }
            if (!passwordEncoder.matches(password, aceUser.getPassword())) {
                throw new BadCredentialsException("Bad credentials");
            }
            return new UsernamePasswordAuthenticationToken(aceUser, aceUser.getPassword(), aceUser.getAuthorities());
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class == authentication;
    }

}
