package com.taotao.cloud.sys.biz.entity.verification.aop;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.sys.biz.entity.verification.aop.annotation.Verification;
import com.taotao.cloud.sys.biz.entity.verification.entity.enums.VerificationEnums;
import com.taotao.cloud.sys.biz.entity.verification.service.VerificationService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

/**
 * 验证码验证拦截
 *
 */
@Aspect
@Configuration
public class VerificationInterceptor {

    @Autowired
    private VerificationService verificationService;

    @Before("@annotation(cn.lili.modules.verification.aop.annotation.Verification)")
    public void interceptor(JoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Verification verificationAnnotation = method.getAnnotation(Verification.class);
        VerificationEnums verificationEnums = verificationAnnotation.type();
        String uuid = verificationAnnotation.uuid();
        boolean result = verificationService.check(uuid, verificationEnums);
        if (result) {
            return;
        }
        throw new BusinessException(ResultEnum.VERIFICATION_ERROR);
    }
}
