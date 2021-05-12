package com.taotao.cloud.security.taox.annotation;


import com.taotao.cloud.security.taox.oauth.login.CustomLogoutSuccessHandler;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = {java.lang.annotation.ElementType.TYPE})
@Documented
@Import({
//	Oauth2LoginSecurityConfigurer.class,
//	CustomOAuth2AuthenticationSuccessHandler.class,
	CustomLogoutSuccessHandler.class
})
@EnableWebSecurity
public @interface EnableOAuth2LoginSecurity {

	String value() default "";
}
