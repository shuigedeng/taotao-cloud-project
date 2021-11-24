package com.taotao.cloud.encrypt.annotation;

import com.taotao.cloud.encrypt.configuration.EncryptAutoConfiguration;
import com.taotao.cloud.encrypt.configuration.EncryptFilterAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Enable encrypt of the Spring Application Context
 * 支持res和rsa的加密方式
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({EncryptAutoConfiguration.class, EncryptFilterAutoConfiguration.class})
public @interface EnableTaoTaoCloudEncrypt {
}
