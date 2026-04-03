package com.taotao.cloud.tenant.biz.infrastructure.strategy;

import cn.hutool.core.util.StrUtil;
import com.mdframe.forge.starter.auth.domain.LoginRequest;
import com.mdframe.forge.starter.core.session.LoginUser;
import com.mdframe.forge.starter.auth.enums.AuthType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 手机号+验证码认证策略
 */
@Slf4j
@Component
public class PhoneCaptchaAuthStrategy extends AbstractAuthStrategy {

    @Override
    protected void validateRequest(LoginRequest request) {
        validatePhone(request.getPhone());
        if (StrUtil.isBlank(request.getCode())) {
            throw new RuntimeException("验证码不能为空");
        }
    }

    @Override
    protected LoginUser doAuthenticate(LoginRequest request) {
        String phone = request.getPhone();

        // 1. 验证手机验证码
        if (!userLoadService.validatePhoneCode(phone, request.getCode())) {
            throw new RuntimeException("验证码错误或已过期");
        }

        // 2. 根据手机号加载用户信息
        LoginUser loginUser = userLoadService.loadUserByPhone(phone, request.getTenantId());
        if (loginUser == null) {
            throw new RuntimeException("用户不存在");
        }

        return loginUser;
    }

    @Override
    public String getAuthType() {
        return AuthType.PHONE_CAPTCHA.getCode();
    }
}
