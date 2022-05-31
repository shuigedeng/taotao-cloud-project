package com.taotao.cloud.member.biz.connect.token;

import com.google.gson.Gson;
import com.taotao.cloud.common.enums.CachePrefix;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.enums.UserEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.redis.repository.RedisRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * TokenUtil
 */
public class TokenUtil {
	@Autowired
	private JWTTokenProperties tokenProperties;
	@Autowired
	private RedisRepository redisRepository;

	/**
	 * 构建token
	 *
	 * @param username 主体
	 * @param claim    私有声明
	 * @param longTerm 长时间特殊token 如：移动端，微信小程序等
	 * @param userEnum 用户枚举
	 * @return TOKEN
	 */
	public Token createToken(String username, Object claim, boolean longTerm, UserEnum userEnum) {
		Token token = new Token();
		//访问token
		String accessToken = createToken(username, claim, tokenProperties.getTokenExpireTime());

		redisRepository.setExpire(CachePrefix.ACCESS_TOKEN.getPrefix(userEnum) + accessToken, 1,
			tokenProperties.getTokenExpireTime(), TimeUnit.MINUTES);
		//刷新token生成策略：如果是长时间有效的token（用于app），则默认15天有效期刷新token。如果是普通用户登录，则刷新token为普通token2倍数
		Long expireTime = longTerm ? 15 * 24 * 60L : tokenProperties.getTokenExpireTime() * 2;
		String refreshToken = createToken(username, claim, expireTime);

		redisRepository.setExpire(CachePrefix.REFRESH_TOKEN.getPrefix(userEnum) + refreshToken, 1, expireTime, TimeUnit.MINUTES);

		token.setAccessToken(accessToken);
		token.setRefreshToken(refreshToken);
		return token;
	}

	/**
	 * 刷新token
	 *
	 * @param oldRefreshToken 刷新token
	 * @param userEnum        用户枚举
	 * @return token
	 */
	public Token refreshToken(String oldRefreshToken, UserEnum userEnums) {
		Claims claims;
		try {
			claims = Jwts.parser()
				.setSigningKey(SecretKeyUtil.generalKeyByDecoders())
				.parseClaimsJws(oldRefreshToken).getBody();
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException |
				 IllegalArgumentException e) {
			//token 过期 认证失败等
			throw new BusinessException(ResultEnum.USER_AUTH_EXPIRED);
		}

		//获取存储在claims中的用户信息
		String json = claims.get(SecurityEnum.USER_CONTEXT.getValue()).toString();
		SecurityUser authUser = new Gson().fromJson(json, SecurityUser.class);


		String username = authUser.getUsername();
		//获取是否长期有效的token
		boolean longTerm = authUser.getLongTerm();


		//如果缓存中有刷新token &&
		if (redisRepository.hasKey(CachePrefix.REFRESH_TOKEN.getPrefix(userEnums) + oldRefreshToken)) {
			Token token = new Token();
			//访问token
			String accessToken = createToken(username, authUser, tokenProperties.getTokenExpireTime());
			redisRepository.setExpire(CachePrefix.ACCESS_TOKEN.getPrefix(userEnums) + accessToken, 1, tokenProperties.getTokenExpireTime(), TimeUnit.MINUTES);

			//如果是信任登录设备，则刷新token长度继续延长
			Long expirationTime = tokenProperties.getTokenExpireTime() * 2;
			if (longTerm) {
				expirationTime = 60 * 24 * 15L;
			}

			//刷新token生成策略：如果是长时间有效的token（用于app），则默认15天有效期刷新token。如果是普通用户登录，则刷新token为普通token2倍数
			String refreshToken = createToken(username, authUser, expirationTime);

			redisRepository.setExpire(CachePrefix.REFRESH_TOKEN.getPrefix(userEnums) + refreshToken, 1, expirationTime, TimeUnit.MINUTES);
			token.setAccessToken(accessToken);
			token.setRefreshToken(refreshToken);
			redisRepository.del(CachePrefix.REFRESH_TOKEN.getPrefix(userEnums) + oldRefreshToken);
			return token;
		} else {
			throw new BusinessException(ResultEnum.USER_AUTH_EXPIRED);
		}
	}

	/**
	 * 生成token
	 *
	 * @param username       主体
	 * @param claim          私有神明内容
	 * @param expirationTime 过期时间（分钟）
	 * @return token字符串
	 */
	private String createToken(String username, Object claim, Long expirationTime) {
		//JWT 生成
		return Jwts.builder()
			//jwt 私有声明
			.claim(SecurityEnum.USER_CONTEXT.getValue(), new Gson().toJson(claim))
			//JWT的主体
			.setSubject(username)
			//失效时间 当前时间+过期分钟
			.setExpiration(new Date(System.currentTimeMillis() + expirationTime * 60 * 1000))
			//签名算法和密钥
			//.signWith(SecretKeyUtil.generalKey())
			.compact();
	}
}
