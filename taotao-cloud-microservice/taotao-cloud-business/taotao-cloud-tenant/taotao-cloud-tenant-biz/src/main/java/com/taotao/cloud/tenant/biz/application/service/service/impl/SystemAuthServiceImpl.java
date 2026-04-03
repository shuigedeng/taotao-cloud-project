package com.taotao.cloud.tenant.biz.application.service.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.taotao.cloud.tenant.biz.domain.aggregate.*;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.*;
import com.taotao.cloud.tenant.biz.application.service.service.IUserLoadService;
import com.mdframe.forge.starter.config.config.LoginConfig;
import com.mdframe.forge.starter.config.service.ConfigManagerService;
import com.mdframe.forge.starter.core.context.AuthProperties;
import com.mdframe.forge.starter.auth.domain.*;
import com.taotao.cloud.tenant.biz.application.service.service.ISysOnlineUserService;
import com.mdframe.forge.starter.core.session.SessionHelper;
import com.mdframe.forge.starter.auth.service.IAuthService;
import com.mdframe.forge.starter.auth.service.ICaptchaService;
import com.mdframe.forge.starter.auth.strategy.AuthStrategyFactory;
import com.mdframe.forge.starter.auth.strategy.IAuthStrategy;
import com.mdframe.forge.starter.auth.util.PasswordUtil;
import com.mdframe.forge.starter.core.session.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 系统认证服务实现
 * 使用策略模式实现不同认证方式的灵活扩展
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemAuthServiceImpl implements IAuthService {

    private final SysUserMapper userMapper;
    private final ICaptchaService captchaService;
    private final AuthStrategyFactory authStrategyFactory;
    private final IUserLoadService userLoadService;  // 委托给用户加载服务
    private final ISysOnlineUserService onlineUserService;
    private final AuthProperties authProperties;
    private final ConfigManagerService configManagerService;

    // ==================== 核心认证方法 ====================

    @Override
    public LoginResult login(LoginRequest request) {
        // 1. 参数校验
        if (StrUtil.isBlank(request.getAuthType())) {
            request.setAuthType("password"); // 默认用户名密码认证
        }

        // 2. 获取认证策略
        IAuthStrategy strategy = authStrategyFactory.getStrategy(
                request.getAuthType(),
                request.getUserClient()
        );

        log.info("开始认证: authType={}, userClient={}, strategy={}",
                request.getAuthType(),
                request.getUserClient(),
                strategy.getClass().getSimpleName());

        // 3. 执行认证策略（加载用户信息、验证密码/验证码等）
        LoginUser loginUser = strategy.authenticate(request);

        // 4. 处理同一账号登录策略
        handleSameAccountLogin(loginUser.getUserId());

        // 5. 设置登录时间和IP
        loginUser.setLoginTime(System.currentTimeMillis());
        // TODO: 可以从请求中获取IP地址
        // loginUser.setLoginIp(request.getLoginIp());

        // 6. 执行Sa-Token登录
        StpUtil.login(loginUser.getUserId());

        // 7. 将完整的用户信息（包含角色、权限）设置到Session中
        SessionHelper.setLoginUser(loginUser);

        log.info("用户登录成功: username={}, userId={}, roleIds={}, permissions={}",
                loginUser.getUsername(),
                loginUser.getUserId(),
                loginUser.getRoleIds(),
                loginUser.getPermissions());

        // 8. 构建返回结果
        return buildLoginResult(loginUser);
    }

    /**
     * 处理同一账号登录策略
     *
     * @param userId 用户ID
     */
    private void handleSameAccountLogin(Long userId) {
        if (!authProperties.getEnableOnlineUserManagement()) {
            return;
        }

        String strategy = authProperties.getSameAccountLoginStrategy();
        
        switch (strategy) {
            case "allow_concurrent":
                // 允许并发登录,不做任何处理
                log.debug("允许同一账号并发登录: userId={}", userId);
                break;
                
            case "replace_old":
                // 新登录踢出旧登录
                try {
                    String currentToken = StpUtil.getTokenValue();
                    onlineUserService.kickoutAllSessions(userId, currentToken);
                    log.info("同一账号新登录踢出旧登录: userId={}", userId);
                } catch (Exception e) {
                    log.error("踢出旧会话失败: userId={}", userId, e);
                }
                break;
                
            case "reject_new":
                // 拒绝新登录
                if (!onlineUserService.getUserTokens(userId).isEmpty()) {
                    throw new RuntimeException("该账号已在其他地方登录,请先退出后再登录");
                }
                log.info("同一账号拒绝新登录: userId={}", userId);
                break;
                
            default:
                log.warn("未知的同一账号登录策略: {}", strategy);
        }
    }

    @Override
    public void logout() {
        // 清除Session
        SessionHelper.clearSession();
        // 登出
        StpUtil.logout();
    }

    // ==================== 用户信息加载（委托给UserLoadService） ====================

    @Override
    public LoginUser loadUserByUsername(String username, Long tenantId) {
        return userLoadService.loadUserByUsername(username, tenantId);
    }

    @Override
    public LoginUser loadUserByPhone(String phone, Long tenantId) {
        return userLoadService.loadUserByPhone(phone, tenantId);
    }

    @Override
    public LoginUser loadUserByEmail(String email, Long tenantId) {
        return userLoadService.loadUserByEmail(email, tenantId);
    }

    @Override
    public boolean matchPassword(String rawPassword, String encodedPassword) {
        return userLoadService.matchPassword(rawPassword, encodedPassword);
    }

    @Override
    public boolean validateCode(String codeKey, String code) {
        return userLoadService.validateCode(codeKey, code);
    }

    @Override
    public boolean validatePhoneCode(String phone, String code) {
        return userLoadService.validatePhoneCode(phone, code);
    }

    // ==================== 用户注册相关 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginUser register(RegisterRequest request) {
        // 1. 参数校验
        validateRegisterRequest(request);
        
        // 2. 验证验证码
        if (!captchaService.validateAndDelete(request.getCodeKey(), request.getCode())) {
            throw new RuntimeException("验证码错误或已过期");
        }
        
        // 3. 检查用户名是否已存在
        if (checkUsernameExists(request.getUsername(), request.getTenantId())) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 4. 加密密码
        String encodedPassword = PasswordUtil.encrypt(request.getPassword());
        
        // 5. 保存用户信息
        LoginUser loginUser = saveUser(request, encodedPassword);
        
        log.info("用户注册成功: username={}", request.getUsername());
        return loginUser;
    }

    // ==================== 密码管理 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changePassword(String oldPassword, String newPassword) {
        // 1. 获取当前用户
        LoginUser loginUser = SessionHelper.getLoginUser();
        if (loginUser == null) {
            throw new RuntimeException("未登录");
        }
        
        // 2. 验证旧密码
        String currentPassword = userLoadService.getUserPassword(loginUser.getUserId());
        if (!matchPassword(oldPassword, currentPassword)) {
            throw new RuntimeException("旧密码错误");
        }
        
        // 3. 加密新密码
        String encodedPassword = PasswordUtil.encrypt(newPassword);
        
        // 4. 更新密码
        boolean success = updateUserPassword(loginUser.getUserId(), encodedPassword);
        
        if (success) {
            log.info("用户修改密码成功: userId={}", loginUser.getUserId());
        }
        
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean resetPassword(String username, String newPassword, String code, String codeKey) {
        // 1. 验证验证码
        if (!captchaService.validateAndDelete(codeKey, code)) {
            throw new RuntimeException("验证码错误或已过期");
        }
        
        // 2. 查询用户
        LoginUser loginUser = loadUserByUsername(username, null);
        if (loginUser == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 3. 加密新密码
        String encodedPassword = PasswordUtil.encrypt(newPassword);
        
        // 4. 更新密码
        boolean success = updateUserPassword(loginUser.getUserId(), encodedPassword);
        
        if (success) {
            log.info("用户重置密码成功: username={}", username);
        }
        
        return success;
    }

    // ==================== 验证码和Token管理 ====================

    @Override
    public CaptchaResult getCaptcha() {
        // 生成图形验证码
        return captchaService.generateGraphicCaptcha();
    }

    @Override
    public SliderCaptchaResult getSliderCaptcha() {
        // 生成滑块验证码
        return captchaService.generateSliderCaptcha();
    }

    @Override
    public SmsCaptchaResult sendSmsCaptcha(String phone) {
        // 发送短信验证码
        return captchaService.sendSmsCaptcha(phone);
    }

    @Override
    public LoginConfigResult getLoginConfig() {
        // 从配置中心获取登录配置
        LoginConfig config = configManagerService.getLoginConfig();

        return LoginConfigResult.builder()
                .enableCaptcha(config.getEnableCaptcha())
                .captchaType(config.getCaptchaType())
                .enableRememberMe(config.getEnableRememberMe())
                .enableLoginLog(config.getEnableLoginLog())
                .enableIpLimit(config.getEnableIpLimit())
                .build();
    }

    @Override
    public LoginResult refreshToken() {
        // 1. 检查是否登录
        if (!StpUtil.isLogin()) {
            throw new RuntimeException("未登录");
        }
        
        // 2. 获取当前用户
        LoginUser loginUser = SessionHelper.getLoginUser();
        if (loginUser == null) {
            throw new RuntimeException("用户信息不存在");
        }
        
        // 3. 刷新Token（Sa-Token会自动延长过期时间）
        StpUtil.renewTimeout(StpUtil.getTokenTimeout());
        
        // 4. 构建返回结果
        return buildLoginResult(loginUser);
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 校验注册请求参数
     */
    private void validateRegisterRequest(RegisterRequest request) {
        if (StrUtil.isBlank(request.getUsername())) {
            throw new RuntimeException("用户名不能为空");
        }
        if (StrUtil.isBlank(request.getPassword())) {
            throw new RuntimeException("密码不能为空");
        }
        if (StrUtil.isBlank(request.getCode()) || StrUtil.isBlank(request.getCodeKey())) {
            throw new RuntimeException("验证码不能为空");
        }
    }

    /**
     * 检查用户名是否已存在
     */
    private boolean checkUsernameExists(String username, Long tenantId) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        if (tenantId != null) {
            wrapper.eq(SysUser::getTenantId, tenantId);
        }
        return userMapper.selectCount(wrapper) > 0;
    }

    /**
     * 保存用户
     */
    @Transactional(rollbackFor = Exception.class)
    protected LoginUser saveUser(RegisterRequest request, String encodedPassword) {
        // 1. 构建用户实体
        SysUser user = new SysUser();
        user.setUsername(request.getUsername());
        user.setPassword(encodedPassword);
        user.setRealName(request.getRealName());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setTenantId(request.getTenantId());
        user.setUserType(2); // 普通用户
        user.setUserStatus(1); // 正常
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        
        // 2. 保存用户
        userMapper.insert(user);
        
        // 3. 构建返回结果
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(user.getId());
        loginUser.setUsername(user.getUsername());
        loginUser.setRealName(user.getRealName());
        loginUser.setPhone(user.getPhone());
        loginUser.setEmail(user.getEmail());
        loginUser.setTenantId(user.getTenantId());
        loginUser.setUserType(user.getUserType());
        loginUser.setUserStatus(user.getUserStatus());
        
        return loginUser;
    }

    /**
     * 更新用户密码
     */
    @Transactional(rollbackFor = Exception.class)
    protected boolean updateUserPassword(Long userId, String encodedPassword) {
        LambdaUpdateWrapper<SysUser> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysUser::getId, userId)
                .set(SysUser::getPassword, encodedPassword)
                .set(SysUser::getUpdateTime, LocalDateTime.now());
        return userMapper.update(null, wrapper) > 0;
    }

    /**
     * 构建登录结果
     */
    private LoginResult buildLoginResult(LoginUser loginUser) {
        String token = StpUtil.getTokenValue();
        long tokenTimeout = StpUtil.getTokenTimeout();
        
        return LoginResult.builder()
                .accessToken(token)
                .expiresIn(tokenTimeout)
                .tokenType("Bearer")
                .build();
    }
}
