package com.taotao.cloud.standalone.security;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @Author 李号东
 * @Description 用户手机号和账号密码 身份权限认证类 登陆身份认证
 * @since 2019-05-07 09:11
 * @Param
 * @return
 **/
@Setter
@Getter
@Accessors(chain = true)
public class PreSecurityUser implements UserDetails {

    private static final long serialVersionUID = 1L;


    private LoginType loginType = LoginType.normal;
    private Integer userId;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;


    public PreSecurityUser(Integer userId, String username, String password, Collection<? extends GrantedAuthority> authorities, LoginType loginType) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.loginType = loginType;

    }

    /**
     * 返回分配给用户的角色列表
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * 账户是否未过期,过期无法验证
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 指定用户是否解锁,锁定的用户无法进行身份验证
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 指示是否已过期的用户的凭据(密码),过期的凭据防止认证
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否可用 ,禁用的用户不能身份验证
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
