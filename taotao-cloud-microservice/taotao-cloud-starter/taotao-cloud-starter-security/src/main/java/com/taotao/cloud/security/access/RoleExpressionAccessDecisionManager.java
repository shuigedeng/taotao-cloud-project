package com.taotao.cloud.security.access;

import com.taotao.cloud.cache.redis.repository.RedisRepository;
import com.taotao.cloud.common.utils.log.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AbstractAccessDecisionManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class RoleExpressionAccessDecisionManager extends AbstractAccessDecisionManager {

	private ScriptEngineManager manager = new ScriptEngineManager();
	private ScriptEngine engine = manager.getEngineByName("js");

	@Autowired
	private RedisRepository redisRepository;

	public RoleExpressionAccessDecisionManager(List<AccessDecisionVoter<?>> decisionVoters) {
		super(decisionVoters);
	}

	/**
	 * 暂时使用替换然后用 js 引擎解析来解决逻辑问题, 其实是有漏洞的, 如果某个角色是另一个的前缀
	 *
	 * @param authentication   当前用户凭证
	 * @param object           当前请求路径
	 * @param configAttributes 当前请求路径所需要的角色列表 -- > 从 CustomFilterInvocationSecurityMetadataSource
	 *                         返回
	 * @throws AccessDeniedException
	 * @throws InsufficientAuthenticationException
	 */
	@Override
	public void decide(Authentication authentication, Object object,
					   Collection<ConfigAttribute> configAttributes)
		throws AccessDeniedException, InsufficientAuthenticationException {
		final ConfigAttribute configAttribute = configAttributes.iterator().next();
		String attribute = configAttribute.getAttribute();
		final String originScript = attribute;

		// anon 为不需要登录的权限
		if (attribute.contains("anon")) {
			return;
		}
		// authc 为只要登录了就有权限
		if (attribute.contains("authc")) {
			if (authentication == null || authentication.getPrincipal() == null
				|| "anonymousUser".equals(authentication.getPrincipal())) {
				throw new AccessDeniedException("需要登录");
			}
			return;
		}

		final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		for (GrantedAuthority authority : authorities) {
			final String userHasRole = authority.getAuthority();
			attribute = attribute.replaceAll(userHasRole, "true");
		}

		//final List<String> allRoles = roleService.findRoles().stream().collect(Collectors.toList());
		final List<String> allRoles = new ArrayList<>();
		for (String role : allRoles) {
			attribute = attribute.replaceAll(role, "false");
		}
		try {
			final Object eval = engine.eval(attribute);
			if (!(eval instanceof Boolean)) {
				throw new AccessDeniedException(Objects.toString(eval));
			}
			if (!((Boolean) eval)) {
				throw new AccessDeniedException("权限不足2");
			}
		} catch (ScriptException e) {
			LogUtils.error("脚本执行错误:[origin:{}][parse:{}][message:{}]", originScript, attribute,
				e.getMessage());
			throw new AccessDeniedException("权限不足");
		}
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return false;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return false;
	}

//    public static void main(String[] args) throws ScriptException {
//        ScriptEngineManager manager = new ScriptEngineManager();
//        ScriptEngine engine = manager.getEngineByName("js");
////        engine.put("true",true);
////        engine.put("false",false);
//        String a = "true && (false || true)";
//        System.out.println(engine.eval(a));
//    }
}
