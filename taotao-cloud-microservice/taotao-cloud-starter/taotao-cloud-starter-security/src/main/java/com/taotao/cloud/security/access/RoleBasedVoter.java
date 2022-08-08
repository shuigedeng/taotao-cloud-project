package com.taotao.cloud.security.access;

import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.redis.repository.RedisRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;

public class RoleBasedVoter implements AccessDecisionVoter<Object> {

	@Autowired
	private UrlSecurityPermsLoad urlPermsLoad;
	@Autowired
	private RedisRepository redisRepository;

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.isAssignableFrom(FilterInvocation.class);
	}

	private ScriptEngineManager manager = new ScriptEngineManager();
	private ScriptEngine engine = manager.getEngineByName("js");
	public static final Pattern pattern = Pattern.compile("(\\w+)");

	@Override
	public int vote(Authentication authentication, Object object,
		Collection<ConfigAttribute> attributes) {
		if (authentication == null) {
			return ACCESS_DENIED;
		}
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		for (ConfigAttribute attribute : attributes) {
			if (attribute.getAttribute() == null) {
				continue;
			}
			if (this.supports(attribute)) {

				// Attempt to find a matching granted authority
				for (GrantedAuthority authority : authorities) {
					if (attribute.getAttribute().equals(authority.getAuthority())) {
						return ACCESS_GRANTED;
					}
				}
			}
		}

		// 自定义的权限控制
		FilterInvocation fi = (FilterInvocation) object;
		String url = fi.getRequestUrl();
		String matchRoles = urlPermsLoad.findMatchRoles(url);
		if (StringUtils.isBlank(matchRoles)) {
			return ACCESS_ABSTAIN;
		}
		if (matchRoles.contains("anon")) {
			return ACCESS_GRANTED;
		}

		// 如果路径没有角色配置,则默认是可以被访问的, 只要登录了就行
		if (matchRoles.contains("authc") && "anonymousUser".equals(authentication.getPrincipal())) {
			return ACCESS_DENIED;
		}

		final String originScript = matchRoles;

		// 权限解析, 用户有的替换为 true, 没有的替换成 false
		for (GrantedAuthority authority : authorities) {
			final String userHasRole = authority.getAuthority();
			matchRoles = matchRoles.replaceAll(userHasRole, "true");
		}

		// 用户没有的权限替换为 flase
		//final List<String> allRoles = roleService.findRoles().stream().collect(Collectors.toList());
		final List<String> allRoles = new ArrayList<>();
		for (String role : allRoles) {
			matchRoles = matchRoles.replaceAll(role, "false");
		}

		// 找不到的替换为 undefined
		try {
			final Matcher matcher = pattern.matcher(originScript);
			while (matcher.find()) {
				for (int i = 0; i < matcher.groupCount(); i++) {
					matchRoles = matchRoles.replaceAll(matcher.group(i), "undefined");
				}
			}
		} catch (Exception e) {
			LogUtil.warn("正则匹配错误,可能脚本存在问题:{}", e.getMessage());
		}

		try {
			final Object eval = engine.eval(matchRoles);
			if (!(eval instanceof Boolean)) {
				LogUtil.error("脚本执行失败,返回值非 Bool 类型,默认是禁止访问:{},{}", originScript,
					matchRoles);
				return ACCESS_DENIED;
			}
			if (((Boolean) eval)) {

			}
			return ((Boolean) eval) ? ACCESS_GRANTED : ACCESS_DENIED;
		} catch (ScriptException e) {
			LogUtil.error("脚本执行错误:[origin:{}][parse:{}][message:{}]", originScript,
				matchRoles, e.getMessage());
			return ACCESS_DENIED;
		}
	}
}
