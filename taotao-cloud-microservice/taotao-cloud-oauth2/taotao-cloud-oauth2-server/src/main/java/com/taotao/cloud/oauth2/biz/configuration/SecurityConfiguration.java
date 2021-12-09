package com.taotao.cloud.oauth2.biz.configuration;

import static org.springframework.security.config.Customizer.withDefaults;

import com.taotao.cloud.oauth2.biz.service.CloudUserDetailsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@EnableGlobalMethodSecurity(
	prePostEnabled = true,
	order = 0,
	mode = AdviceMode.PROXY,
	proxyTargetClass = false
)
@EnableWebSecurity
public class SecurityConfiguration {

	private static final Logger LOGGER = LogManager.getLogger(SecurityConfiguration.class);


	@Bean
	public UserDetailsService cloudUserDetailsService() {
		return new CloudUserDetailsService();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	protected void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
		builder.userDetailsService(cloudUserDetailsService())
			.passwordEncoder(passwordEncoder())
			.and()
			.eraseCredentials(true);
	}

	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeRequests(
				authorizeRequests -> authorizeRequests
					.requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
					.anyRequest().authenticated()
			)
			.oauth2Login()
			.and()
			.formLogin(withDefaults())
			.csrf().disable().headers().frameOptions().sameOrigin();

		return http.build();
	}


}
