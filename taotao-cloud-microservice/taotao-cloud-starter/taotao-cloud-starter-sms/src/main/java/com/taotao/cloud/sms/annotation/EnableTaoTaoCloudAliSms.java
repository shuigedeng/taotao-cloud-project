package com.taotao.cloud.sms.annotation;

import com.taotao.cloud.sms.configuration.AliSmsConfiguration;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

/**
 * TaoTaoCloudApplication
 *
 * @author shuigedeng
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({AliSmsConfiguration.class})
public @interface EnableTaoTaoCloudAliSms {

}
