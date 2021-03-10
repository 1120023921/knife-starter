package com.cintsoft.spring.security.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 用户信息
 * </p>
 *
 * @author 胡昊
 * @since 2020-07-23
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class KnifeUser implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;

    private String id;

    private String username;

    private String name;

    private String password;

    private Integer gender;

    private String phone;

    private String mail;

    private String headerUrl;

    private String userSource;

    private Boolean isAccountNonExpired = true;

    private Boolean isAccountNonLocked = true;

    private Boolean isCredentialsNonExpired = true;

    private Boolean isEnabled = true;

    private Integer weight;

    private String extra;

    private String tenantId;

    private List<String> permissionList;

    private List<String> roleKeyList;

    private List<? extends GrantedAuthority> sysResourceList = Collections.emptyList();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return sysResourceList;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
