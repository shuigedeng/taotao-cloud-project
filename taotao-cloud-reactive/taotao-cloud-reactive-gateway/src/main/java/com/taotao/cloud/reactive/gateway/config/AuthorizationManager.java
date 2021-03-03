/**
 * Project Name: projects
 * Package Name: com.taotao.cloud.reactive.gateway.config
 * Date: 2020/9/10 15:15
 * Author: dengtao
 */
package com.taotao.cloud.reactive.gateway.config;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * <br>
 *
 * @author dengtao
 * @version v1.0.0
 * @date 2020/9/10 15:15
 */
@Component
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono,
                                             AuthorizationContext authorizationContext) {
        //从Redis中获取当前路径可访问角色列表
//        URI uri = authorizationContext.getExchange().getRequest().getURI();
//        Object obj = redisTemplate.opsForHash().get(RedisConstant.RESOURCE_ROLES_MAP, uri.getPath());
//        List<String> authorities = Convert.toList(String.class, obj);
        List<String> authorities = new ArrayList<>();
//        authorities = authorities.stream().map(i -> i = AuthConstant.AUTHORITY_PREFIX + i).collect(Collectors.toList());

        //认证通过且角色匹配的用户可访问当前路径
        return mono
                .filter(Authentication::isAuthenticated)
                .flatMapIterable(Authentication::getAuthorities)
                .map(GrantedAuthority::getAuthority)
                .any(authorities::contains)
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(false));
    }
}
