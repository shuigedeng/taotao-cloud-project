package com.taotao.cloud.tenant.biz.infrastructure.strategy;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.tenant.biz.application.service.service.IUserLoadService;
import com.mdframe.forge.starter.core.context.AuthProperties;
import com.mdframe.forge.starter.auth.domain.LoginRequest;
import com.mdframe.forge.starter.core.session.LoginUser;
import com.mdframe.forge.starter.auth.exception.AccountLockedException;
import com.mdframe.forge.starter.auth.service.ILoginLockService;
import com.mdframe.forge.starter.auth.strategy.IAuthStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 认证策略抽象基类
 * 封装公共的认证逻辑，包括：
 * - 账号锁定检查
 * - 登录失败记录
 * - 用户状态检查
 * - 登录成功处理
 */
@Slf4j
public abstract class AbstractAuthStrategy implements IAuthStrategy {

    @Autowired
    protected IUserLoadService userLoadService;

    @Autowired(required = false)
    protected ILoginLockService loginLockService;

    @Autowired
    protected AuthProperties authProperties;

    /**
 * 认证模板方法
     * 定义认证的标准流程，子类实现具体的认证逻辑
     */
    @Override
    public LoginUser authenticate(LoginRequest request) {
        // 1. 子类自定义参数校验
        validateRequest(request);

        // 2. 执行具体的认证逻辑（由子类实现）
        LoginUser loginUser = doAuthenticate(request);

        // 3. 检查用户状态
        checkUserStatus(loginUser);

        // 4. 登录成功后处理
        handleLoginSuccess(loginUser);

        return loginUser;
    }

    /**
     * 参数校验（由子类实现）
     *
     * @param request 登录请求
     */
    protected abstract void validateRequest(LoginRequest request);

    /**
     * 执行具体的认证逻辑（由子类实现）
     *
     * @param request 登录请求
     * @return 登录用户信息
     */
    protected abstract LoginUser doAuthenticate(LoginRequest request);

    /**
     * 检查账号是否被锁定
     *
     * @param loginUser 用户信息
     */
    protected void checkAccountLocked(LoginUser loginUser) {
        if (!isLoginLockEnabled() || loginUser == null) {
            return;
        }

        if (loginLockService.isLocked(loginUser)) {
            long remainingTime = loginLockService.getLockRemainingTime(loginUser);
            long minutes = remainingTime / 60;
            throw new AccountLockedException(
                    String.format("账号已被锁定，请在 %d 分钟后重试", minutes),
                    remainingTime
            );
        }
    }

    /**
     * 记录登录失败
     *
     * @param loginUser 用户信息（可能为null）
     * @param errorMessage 错误信息
     */
    protected void recordLoginFailure(LoginUser loginUser, String errorMessage) {
        if (!isLoginLockEnabled() || loginUser == null) {
            throw new RuntimeException(errorMessage);
        }

        int remaining = loginLockService.recordLoginFailure(loginUser);
        if (remaining > 0) {
            throw new RuntimeException(String.format("%s，还剩 %d 次尝试机会", errorMessage, remaining));
        } else {
            long lockMinutes = authProperties.getLockDuration();
            throw new RuntimeException(String.format("%s，账号已被锁定 %d 分钟", errorMessage, lockMinutes));
        }
    }

    /**
     * 检查用户状态
     *
     * @param loginUser 登录用户
     */
    protected void checkUserStatus(LoginUser loginUser) {
        if (loginUser == null) {
            throw new RuntimeException("用户不存在");
        }

        if (loginUser.getUserStatus() == null || loginUser.getUserStatus() != 1) {
            throw new RuntimeException("用户已被禁用或锁定");
        }
    }

    /**
     * 登录成功后处理
     *
     * @param loginUser 登录用户
     */
    protected void handleLoginSuccess(LoginUser loginUser) {
        // 清除登录失败记录
        if (isLoginLockEnabled() && loginUser != null) {
            loginLockService.clearLoginFailure(loginUser.getUserId());
        }

        log.info("{}认证成功: username={}, userId={}",
                getAuthType(), loginUser.getUsername(), loginUser.getUserId());
    }

    /**
     * 校验用户名参数
     *
     * @param username 用户名
     */
    protected void validateUsername(String username) {
        if (StrUtil.isBlank(username)) {
            throw new RuntimeException("用户名不能为空");
        }
    }

    /**
     * 校验密码参数
     *
     * @param password 密码
     */
    protected void validatePassword(String password) {
        if (StrUtil.isBlank(password)) {
            throw new RuntimeException("密码不能为空");
        }
    }

    /**
     * 校验验证码参数
     *
     * @param code 验证码
     * @param codeKey 验证码key
     */
    protected void validateCaptcha(String code, String codeKey) {
        if (StrUtil.isBlank(code) || StrUtil.isBlank(codeKey)) {
            throw new RuntimeException("验证码不能为空");
        }
    }

    /**
     * 校验手机号参数
     *
     * @param phone 手机号
     */
    protected void validatePhone(String phone) {
        if (StrUtil.isBlank(phone)) {
            throw new RuntimeException("手机号不能为空");
        }
    }

    /**
     * 判断是否启用了登录锁定功能
     *
     * @return 是否启用
     */
    protected boolean isLoginLockEnabled() {
        return authProperties.getEnableLoginLock() != null
                && authProperties.getEnableLoginLock()
                && loginLockService != null;
    }
}
