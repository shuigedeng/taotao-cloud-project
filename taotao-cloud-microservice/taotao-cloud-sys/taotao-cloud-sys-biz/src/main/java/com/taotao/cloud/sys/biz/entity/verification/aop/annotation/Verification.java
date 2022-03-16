package com.taotao.cloud.sys.biz.entity.verification.aop.annotation;



import com.taotao.cloud.sys.biz.entity.verification.entity.enums.VerificationEnums;
import java.lang.annotation.*;

/**
 * 限流注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Verification {
    /**
     * uuid
     *
     * @return String
     */
    String uuid();

    /**
     * 验证类型
     *
     * @return
     */
    VerificationEnums type();
}
