package com.taotao.cloud.security.perm;

import com.taotao.cloud.cache.redis.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class VipSecurityOauthService {

	@Autowired
	private RedisRepository redisRepository;

	/**
	 * 动态加载权限-角色信息
	 **/
	public Set<PermRoleEntity> loadPerms() {
		Set<PermRoleEntity> permRoleEntitySet = new HashSet<>();
		//permRoleEntitySet.add(
		//	new PermRoleEntity().setAccessUri("/demo/admin")
		//		.setConfigAttributeList(
		//		SecurityConfig.createList("admin")));
		//permRoleEntitySet.add(new PermRoleEntity().setAccessUri("/auth/**")
		//	.setConfigAttributeList(SecurityConfig.createList("admin")));
		//permRoleEntitySet.add(new PermRoleEntity().setAccessUri("/demo/sp-admin")
		//	.setConfigAttributeList(SecurityConfig.createList("sp_admin")));
		return permRoleEntitySet;
	}
}
