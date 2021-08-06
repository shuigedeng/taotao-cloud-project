package com.taotao.cloud.security.annotation;


import com.taotao.cloud.security.login.CustomLogoutSuccessHandler;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = {java.lang.annotation.ElementType.TYPE})
@Documented
@Import({
//	Oauth2LoginSecurityConfigurer.class,
//	CustomOAuth2AuthenticationSuccessHandler.class,
	CustomLogoutSuccessHandler.class
})
public @interface EnableTaoTaoCloudOAuth2LoginSecurity {

	String value() default "";
}
