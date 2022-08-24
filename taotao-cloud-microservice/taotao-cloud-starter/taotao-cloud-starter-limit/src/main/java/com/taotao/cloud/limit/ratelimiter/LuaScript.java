package com.taotao.cloud.limit.ratelimiter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

public final class LuaScript {

	private LuaScript() {
	}

	private static final Logger log = LoggerFactory.getLogger(LuaScript.class);
	private static final String RATE_LIMITER_FILE_PATH = "META-INF/ratelimiter-spring-boot-starter-rateLimit.lua";
	private static String rateLimiterScript;

	static {
		InputStream in = Thread.currentThread().getContextClassLoader()
			.getResourceAsStream(RATE_LIMITER_FILE_PATH);
		try {
			rateLimiterScript =  StreamUtils.copyToString(in, StandardCharsets.UTF_8);
		} catch (IOException ignored) {

		}
	}


	public static String getRateLimiterScript() {
		//return rateLimiterScript;

		return """
			local rateLimitKey = KEYS[1];
			local rate = tonumber(KEYS[2]);
			local rateInterval = tonumber(KEYS[3]);
			local limitResult = 0;
			local ttlResult = 0;
			local currValue = redis.call('incr', rateLimitKey);
			
			if (currValue == 1) then
			    redis.call('expire', rateLimitKey, rateInterval);
			    limitResult = 0;
			else
			    if (currValue > rate) then
			        limitResult = 1;
			        ttlResult = redis.call('ttl', rateLimitKey);
			    end
			end
			
			return { limitResult, ttlResult }
			""";
	}
}
