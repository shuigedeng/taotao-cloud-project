package com.taotao.cloud.auth.biz.authentication.login.extension.email.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taotao.cloud.auth.biz.authentication.login.extension.email.service.EmailCheckService;
import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.all.entity.SysUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DefaultEmailCheckService implements EmailCheckService {

	@Override
	public boolean check(String email) {
		return false;
	}
}
