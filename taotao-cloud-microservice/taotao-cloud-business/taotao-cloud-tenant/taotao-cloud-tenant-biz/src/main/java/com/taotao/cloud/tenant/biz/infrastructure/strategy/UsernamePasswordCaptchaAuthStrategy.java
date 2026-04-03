package com.taotao.cloud.tenant.biz.infrastructure.strategy;

import cn.hutool.core.util.StrUtil;
import com.mdframe.forge.starter.auth.domain.LoginRequest;
import com.mdframe.forge.starter.auth.service.ICaptchaService;
import com.mdframe.forge.starter.config.config.LoginConfig;
import com.mdframe.forge.starter.config.service.ConfigManagerService;
import com.mdframe.forge.starter.core.exception.BusinessException;
import com.mdframe.forge.starter.core.session.LoginUser;
import com.mdframe.forge.starter.auth.enums.AuthType;
import com.mdframe.forge.starter.crypto.keyexchange.RsaKeyPairHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户名+密码+验证码认证策略
 * 支持多种验证码类型：图形验证码、滑块验证码、短信验证码
 * 验证码类型从配置中心读取
 */
@Slf4j
@Component
public class UsernamePasswordCaptchaAuthStrategy extends AbstractAuthStrategy {

    @Autowired
    private RsaKeyPairHolder rsaKeyPairHolder;

    @Autowired
    private ICaptchaService captchaService;

    @Autowired
    private ConfigManagerService configManagerService;

    @Override
    protected void validateRequest(LoginRequest request) {
        validateUsername(request.getUsername());
        validatePassword(request.getPassword());

        // 根据验证码类型校验不同参数
        LoginConfig loginConfig = configManagerService.getLoginConfig();
        String captchaType = loginConfig.getCaptchaType();

        if ("sms".equals(captchaType)) {
            // 短信验证码需要手机号和验证码
            if (StrUtil.isBlank(request.getPhone())) {
                throw new RuntimeException("手机号不能为空");
            }
            if (StrUtil.isBlank(request.getCode())) {
                throw new RuntimeException("验证码不能为空");
            }
        } else if ("slider".equals(captchaType)) {
            // 滑块验证码：前端已验证，后端只需检查是否已验证标记
            // 为了安全，可以添加一个临时的token验证
            if (StrUtil.isBlank(request.getCode())) {
                throw new RuntimeException("请完成滑块验证");
            }
        } else {
            // 图形验证码需要codeKey和code
            validateCaptcha(request.getCode(), request.getCodeKey());
        }
    }

    @Override
    protected LoginUser doAuthenticate(LoginRequest request) {
        String username = request.getUsername();

        // 1. 获取登录配置，确定验证码类型
        LoginConfig loginConfig = configManagerService.getLoginConfig();
        String captchaType = loginConfig.getCaptchaType();

        // 2. 根据验证码类型进行验证
        boolean captchaValid = validateCaptchaByType(request, captchaType);
        if (!captchaValid) {
            throw new BusinessException("验证码错误或已过期");
        }

        // 3. 加载用户信息
        LoginUser loginUser = userLoadService.loadUserByUsername(username, request.getTenantId());

        // 4. 检查账号是否被锁定
        checkAccountLocked(loginUser);

        // 5. 校验用户是否存在
        if (loginUser == null) {
            recordLoginFailure(null, "用户不存在");
        }

        // 6. 验证密码
        String encodedPassword = userLoadService.getUserPassword(loginUser.getUserId());
        String decrypt = rsaKeyPairHolder.decryptByPrivateKeyNoBase64(request.getPassword());
        if (!userLoadService.matchPassword(decrypt, encodedPassword)) {
            recordLoginFailure(loginUser, "密码错误");
        }

        return loginUser;
    }

    /**
     * 根据验证码类型进行验证
     *
     * @param request     登录请求
     * @param captchaType 验证码类型
     * @return 是否验证通过
     */
    private boolean validateCaptchaByType(LoginRequest request, String captchaType) {
        if (captchaType == null) {
            captchaType = "graphical"; // 默认图形验证码
        }

        switch (captchaType) {
            case "slider":
                // 滑块验证码：前端组件已验证，后端只需检查验证标记
                // "verified" 表示前端验证通过
                return "verified".equals(request.getCode());
            case "sms":
                // 短信验证码：使用手机号验证
                return captchaService.validateAndDeleteSmsCaptcha(request.getPhone(), request.getCode());
            case "graphical":
            default:
                // 图形验证码
                return userLoadService.validateCode(request.getCodeKey(), request.getCode());
        }
    }

    @Override
    public String getAuthType() {
        return AuthType.PASSWORD_CAPTCHA.getCode();
    }
}
