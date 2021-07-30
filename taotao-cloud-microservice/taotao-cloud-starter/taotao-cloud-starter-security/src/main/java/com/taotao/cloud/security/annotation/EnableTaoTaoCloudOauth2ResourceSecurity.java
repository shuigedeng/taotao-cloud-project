package com.taotao.cloud.security.annotation;

import com.taotao.cloud.security.resource.Oauth2ResourceSecurityConfigurer;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = { java.lang.annotation.ElementType.TYPE })
@Documented
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
@Import({Oauth2ResourceSecurityConfigurer.class})
public @interface EnableTaoTaoCloudOauth2ResourceSecurity {


}
