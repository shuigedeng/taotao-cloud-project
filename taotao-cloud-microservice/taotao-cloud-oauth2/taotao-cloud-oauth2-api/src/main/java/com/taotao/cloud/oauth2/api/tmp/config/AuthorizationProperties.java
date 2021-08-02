package com.taotao.cloud.oauth2.api.tmp.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth2.authorization")
public class AuthorizationProperties {

	private List<Client> client = new ArrayList<>();

	static class Client {

		private String clientId;

		private String clientSecret;

		private String redirectUri;

		private Set<String> scope = new HashSet<>();
	}
}
