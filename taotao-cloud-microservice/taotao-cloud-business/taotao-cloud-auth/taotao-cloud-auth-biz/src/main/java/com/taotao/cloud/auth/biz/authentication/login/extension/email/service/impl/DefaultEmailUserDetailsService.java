package com.taotao.cloud.auth.biz.authentication.login.extension.email.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taotao.cloud.auth.biz.authentication.login.extension.email.service.EmailUserDetailsService;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.all.entity.SysUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DefaultEmailUserDetailsService implements EmailUserDetailsService {


//	@Autowired
//	private SysUserMapper sysUserMapper;
//
//	@Autowired
//	private SysRoleMapper sysRoleMapper;

	@Override
	public UserDetails loadUserByEmail(String email) {

//		SysUser user = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getEmail, email));
//		if (Objects.isNull(user)) {
//			return null;
//		}
//		// 获得用户角色信息
//		List<String> roles = sysRoleMapper.selectByRoleId(user.getRoleId());
//		// 构建 SimpleGrantedAuthority 对象
//		List<SimpleGrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
//		return new SysUserDetails(user, authorities);
		return null;
	}
}
