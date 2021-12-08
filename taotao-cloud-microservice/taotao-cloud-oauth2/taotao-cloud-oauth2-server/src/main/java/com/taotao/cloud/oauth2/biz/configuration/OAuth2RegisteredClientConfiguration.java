package com.taotao.cloud.oauth2.biz.configuration;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.util.StringUtils;

@Configuration
@PropertySource("classpath:oauth2-registered-client.properties")
public class OAuth2RegisteredClientConfiguration {

	private static final Logger LOGGER = LogManager.getLogger(
		OAuth2RegisteredClientConfiguration.class);

	private static final String OAUTH2_REGISTERD_CLIENT = "oauth2.registered.client";
	private static final String ID = "id";
	private static final String SECRET = "secret";
	private static final String REDIRECT_URI = "redirect.uri";

	private static final String CLIENT_CREDENTIALS_CLIENT = "client.credentials";
	private static final String AUTHORIZATION_CODE_CLIENT = "authorization.code";
	private static final String PASSWORD_CLIENT = "password";

	@Value("${oauth2.access.token.time:1}")
	private long accessTokenTime;

	@Value("${oauth2.access.token.time.unit:day}")
	private String accessTokenTimeUnit;

	@Value("${oauth2.refresh.token.time:4}")
	private long refreshTokenTime;

	@Value("${oauth2.refresh.token.time.unit:day}")
	private String refreshTokenTimeUnit;

	@Autowired
	private Environment env;

	@Bean
	public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
		LOGGER.debug("in registeredClientRepository");

		RegisteredClient clientCredentialsRegisteredClient = clientCredentialsClientRegistration();
		RegisteredClient authorizationCodeRegisteredClient = authorizationCodeClientRegistration();
		RegisteredClient passwordRegisteredClient = passwordClientRegistration();

		List<RegisteredClient> registeredClients = new ArrayList<>();
		registeredClients.add(clientCredentialsRegisteredClient);
		registeredClients.add(authorizationCodeRegisteredClient);
		registeredClients.add(passwordRegisteredClient);

		JdbcRegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(
			jdbcTemplate);
		registeredClients.forEach(registeredClient -> {
			String id = registeredClient.getId();
			String clientId = registeredClient.getClientId();
			RegisteredClient dbRegisteredClient = registeredClientRepository.findById(id);
			if (dbRegisteredClient == null) {
				dbRegisteredClient = registeredClientRepository.findByClientId(clientId);
			}

			if (dbRegisteredClient == null) {
				registeredClientRepository.save(registeredClient);
			}
		});

		return registeredClientRepository;
	}

	private RegisteredClient clientCredentialsClientRegistration() {

		LOGGER.debug("in clientCredentialsClientRegistration");

		String clientCredentialsClientId = getClientProperty(CLIENT_CREDENTIALS_CLIENT, ID);
		String clientCredentialsClientSecret = getClientProperty(CLIENT_CREDENTIALS_CLIENT, SECRET);

		TokenSettings tokenSetting = getTokenSettings();

		RegisteredClient messagingRegisteredClient = RegisteredClient.withId("1")
			.clientId(clientCredentialsClientId)
			.clientName("client-credentials")
			.clientSecret(clientCredentialsClientSecret)
			.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
			.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
			.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
			.tokenSettings(tokenSetting)
			.scope("message.read")
			.scope("message.write")
			.build();

		return messagingRegisteredClient;
	}

	private RegisteredClient authorizationCodeClientRegistration() {

		LOGGER.debug("in authorizationCodeClientRegistration");

		String authorizationCodeClientId = getClientProperty(AUTHORIZATION_CODE_CLIENT, ID);
		String authorizationCodeClientSecret = getClientProperty(AUTHORIZATION_CODE_CLIENT, SECRET);
		String authorizationCodeClientRedirectUri = getClientProperty(AUTHORIZATION_CODE_CLIENT,
			REDIRECT_URI);

		TokenSettings tokenSetting = getTokenSettings();

		ClientSettings clientSettings = ClientSettings.builder().requireAuthorizationConsent(false)
			.build();

		RegisteredClient zapierRegisteredClient = RegisteredClient.withId("2")
			.clientId(authorizationCodeClientId)
			.clientName("authorization-code")
			.clientSecret(authorizationCodeClientSecret)
			.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
			.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
			.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
			.redirectUri(authorizationCodeClientRedirectUri)
			.tokenSettings(tokenSetting)
			.clientSettings(clientSettings)
			.build();

		return zapierRegisteredClient;
	}

	private RegisteredClient passwordClientRegistration() {

		LOGGER.debug("in passwordClientRegistration");

		String passwordClientId = getClientProperty(PASSWORD_CLIENT, ID);
		String passwordClientSecret = getClientProperty(PASSWORD_CLIENT, SECRET);

		TokenSettings tokenSetting = getTokenSettings();

		RegisteredClient zapierRegisteredClient = RegisteredClient.withId("3")
			.clientId(passwordClientId)
			.clientName("password")
			.clientSecret(passwordClientSecret)
			.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
			.authorizationGrantType(AuthorizationGrantType.PASSWORD)
			.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
			.tokenSettings(tokenSetting)
			.build();

		return zapierRegisteredClient;
	}

	private String getClientProperty(String client, String property) {

		LOGGER.debug("in getClientProperty");

		// oauth2.registered.client.authorization.code.id
		String propertyName = String.format("%s.%s.%s", OAUTH2_REGISTERD_CLIENT, client, property);
		String propertyValue = env.getProperty(propertyName);
		return propertyValue;
	}

	private TokenSettings getTokenSettings() {

		Duration accessTokenDuration = setTokenTime(accessTokenTimeUnit, accessTokenTime, 5);
		Duration refreshTokenDuration = setTokenTime(refreshTokenTimeUnit, refreshTokenTime, 60);

		TokenSettings.Builder tokenSettingsBuilder = TokenSettings.builder()
			.accessTokenTimeToLive(accessTokenDuration)
			.refreshTokenTimeToLive(refreshTokenDuration);
		TokenSettings tokenSetting = tokenSettingsBuilder.build();
		return tokenSetting;

	}

	private Duration setTokenTime(String tokenTimeUnit, long tokenTime, long durationInMinutes) {

		Duration duration = Duration.ofMinutes(durationInMinutes);

		if (StringUtils.hasText(tokenTimeUnit)) {

			switch (tokenTimeUnit.toUpperCase()) {
				case "M":
				case "MINUTE":
				case "MINUTES":
					duration = Duration.ofMinutes(tokenTime);
					break;
				case "H":
				case "HOUR":
				case "HOURS":
					duration = Duration.ofHours(tokenTime);
					break;
				case "D":
				case "DAY":
				case "DAYS":
					duration = Duration.ofDays(tokenTime);
					break;
				case "W":
				case "WEEK":
				case "WEEKS":
					duration = Duration.of(tokenTime, ChronoUnit.WEEKS);
					break;
			}
		}

		return duration;
	}

}
