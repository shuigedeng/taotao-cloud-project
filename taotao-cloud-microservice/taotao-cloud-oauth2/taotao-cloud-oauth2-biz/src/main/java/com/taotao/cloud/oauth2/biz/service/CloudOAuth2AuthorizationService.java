package com.taotao.cloud.oauth2.biz.service;

import static com.taotao.cloud.oauth2.biz.models.AuthorizationServerConstant.COLON;
import static com.taotao.cloud.oauth2.biz.models.AuthorizationServerConstant.PREFIX_AUTHORIZATION;
import static org.springframework.security.oauth2.jwt.JwtClaimNames.EXP;

import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.redis.repository.RedisRepository;
import java.time.Instant;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.core.OAuth2TokenType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

public class CloudOAuth2AuthorizationService extends JdbcOAuth2AuthorizationService {

	private final RedisRepository redisRepository;
	private final JwtDecoder jwtDecoder;

	public CloudOAuth2AuthorizationService(JdbcTemplate jdbcTemplate,
		RegisteredClientRepository registeredClientRepository,
		RedisRepository redisRepository,
		JwtDecoder jwtDecoder) {
		super(jdbcTemplate, registeredClientRepository);

		this.redisRepository = redisRepository;
		this.jwtDecoder = jwtDecoder;
	}

	/**
	 * Save {@link OAuth2Authorization} for reuse purpose. We can reuse the oauth2 info even if user
	 * login multiple times.
	 * <b>cacheKey</b>: oauth2:authorization:{client_id}:{username}
	 *
	 * @param authorization {@link OAuth2Authorization}
	 */
	@Override
	public void save(OAuth2Authorization authorization) {
		super.save(authorization);

		String clientId = authorization.getId();
		String username = authorization.getPrincipalName();
		Instant expiresAt = authorization.getAttribute(EXP);

		boolean res = redisRepository.set(PREFIX_AUTHORIZATION + clientId + COLON + username,
			authorization, 10000L);
		if (!res) {
			LogUtil.info("OAuth2Authorization saved failed...");
		}
	}

	@Override
	public void remove(OAuth2Authorization authorization) {
		super.remove(authorization);

		String username = authorization.getPrincipalName();
		String clientId = authorization.getRegisteredClientId();
		redisRepository.del(PREFIX_AUTHORIZATION + clientId + COLON + username);
	}

	@Override
	public OAuth2Authorization findById(String id) {
		OAuth2Authorization oAuth2Authorization = super.findById(id);
		//if (Objects.isNull(oAuth2Authorization)) {
		//	throw new UnsupportedOperationException("Find by ID is not supported...");
		//}
		return oAuth2Authorization;
	}

	@Override
	public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
		//return super.findByToken(token, tokenType);

		Jwt jwt = jwtDecoder.decode(token);
		String username = jwt.getSubject();
		String client = jwt.getAudience().get(0);

		OAuth2Authorization oAuth2Authorization = (OAuth2Authorization) redisRepository.get(
			PREFIX_AUTHORIZATION + client + COLON + username);

		if (Objects.isNull(oAuth2Authorization)) {
			return super.findByToken(token, tokenType);
		}

		return oAuth2Authorization;
	}
}
