package com.taotao.cloud.tenant.biz.infrastructure.strategy;

import com.mdframe.forge.starter.auth.domain.LoginRequest;
import com.mdframe.forge.starter.core.session.LoginUser;
import com.mdframe.forge.starter.auth.enums.AuthType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 用户名+密码认证策略
 */
@Slf4j
@Component
public class UsernamePasswordAuthStrategy extends AbstractAuthStrategy {

    @Override
    protected void validateRequest(LoginRequest request) {
        validateUsername(request.getUsername());
        validatePassword(request.getPassword());
    }

    @Override
    protected LoginUser doAuthenticate(LoginRequest request) {
        String username = request.getUsername();

        // 1. 加载用户信息
        LoginUser loginUser = userLoadService.loadUserByUsername(username, request.getTenantId());

        // 2. 检查账号是否被锁定
        checkAccountLocked(loginUser);

        // 3. 校验用户是否存在
        if (loginUser == null) {
            recordLoginFailure(null, "用户不存在");
        }

        // 4. 验证密码
        String encodedPassword = userLoadService.getUserPassword(loginUser.getUserId());
        if (!userLoadService.matchPassword(request.getPassword(), encodedPassword)) {
            recordLoginFailure(loginUser, "密码错误");
        }

        return loginUser;
    }

    @Override
    public String getAuthType() {
        return AuthType.PASSWORD.getCode();
    }
}
