package com.taotao.cloud.standalone.system.modules.security;

import cn.hutool.core.util.ObjectUtil;
import com.taotao.cloud.standalone.security.LoginType;
import com.taotao.cloud.standalone.security.PreSecurityUser;
import com.taotao.cloud.standalone.system.modules.sys.domain.SysUser;
import com.taotao.cloud.standalone.system.modules.sys.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

/**
 * @Classname UserDetailsServiceImpl
 * @Description 用户身份验证
 * @Author 李号东 lihaodongmail@163.com
 * @Date 2019-05-07 20:30
 * @Version 1.0
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService, SocialUserDetailsService {

    @Autowired
    private ISysUserService userService;

    /**
     * 用户名密码登录
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);
        SysUser user = userService.findSecurityUserByUser(sysUser);
        if (ObjectUtil.isNull(user)) {
            log.info("登录用户：" + username + " 不存在.");
            throw new UsernameNotFoundException("登录用户：" + username + " 不存在");
        }
        Collection<? extends GrantedAuthority> authorities = getUserAuthorities(user.getUserId());
        return new PreSecurityUser(user.getUserId(), username, user.getPassword(), authorities, LoginType.normal);
    }

    /**
     * 手机验证码登录
     *
     * @param mobile
     * @return
     * @throws UsernameNotFoundException
     */
    public UserDetails loadUserByMobile(String mobile) throws UsernameNotFoundException {
        SysUser sysUser = new SysUser();
        sysUser.setPhone(mobile);
        //  通过手机号mobile去数据库里查找用户以及用户权限
        SysUser user = userService.findSecurityUserByUser(sysUser);
        if (ObjectUtil.isNull(user)) {
            log.info("登录手机号：" + mobile + " 不存在.");
            throw new UsernameNotFoundException("登录手机号：" + mobile + " 不存在");
        }
        // 获取用户拥有的角色
        Collection<? extends GrantedAuthority> authorities = getUserAuthorities(user.getUserId());
        return new PreSecurityUser(user.getUserId(), user.getUsername(), user.getPassword(), authorities, LoginType.sms);
    }

    /**
     * 社交登录
     *
     * @param userId
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(Integer.valueOf(userId));
        //根据用户名查找用户信息
        SysUser user = userService.findSecurityUserByUser(sysUser);
        if (ObjectUtil.isNull(user)) {
            log.info("社交登录userId:" + userId);
            throw new UsernameNotFoundException("社交登录userId：" + userId + " 不存在");
        }
        Collection<? extends GrantedAuthority> authorities = getUserAuthorities(user.getUserId());
        return new PreSocialUser(user.getUserId(), user.getUsername(), user.getPassword(), authorities);
    }

    /**
     * 封装 根据用户Id获取权限
     *
     * @param userId
     * @return
     */
    private Collection<? extends GrantedAuthority> getUserAuthorities(int userId) {
        // 获取用户拥有的角色
        // 用户权限列表，根据用户拥有的权限标识与如 @PreAuthorize("hasAuthority('sys:menu:view')") 标注的接口对比，决定是否可以调用接口
        // 权限集合
        Set<String> permissions = userService.findPermsByUserId(userId);
        // 角色集合
        Set<String> roleIds = userService.findRoleIdByUserId(userId);
        permissions.addAll(roleIds);
        Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(permissions.toArray(new String[0]));
        return authorities;
    }
}
