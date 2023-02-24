package com.taotao.cloud.gateway.anti_reptile.util;

import com.taotao.cloud.captcha.captcha.utils.CaptchaUtil;
import com.taotao.cloud.gateway.anti_reptile.module.VerifyImageDTO;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

public class VerifyImageUtil {

	private static final String VERIFY_CODE_KEY = "tt_antireptile_verifycdoe_";

	private final RedissonClient redissonClient;

	public VerifyImageUtil(RedissonClient redissonClient) {
		this.redissonClient = redissonClient;
	}

	public VerifyImageDTO generateVerifyImg() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		String result = CaptchaUtil.out(outputStream);
		String base64Image = "data:image/jpeg;base64," + Base64.getEncoder()
			.encodeToString(outputStream.toByteArray());
		String verifyId = UUID.randomUUID().toString();
		return new VerifyImageDTO(verifyId, null, base64Image, result);
	}

	public void saveVerifyCodeToRedis(VerifyImageDTO verifyImage) {
		RBucket<String> rBucket = redissonClient.getBucket(
			VERIFY_CODE_KEY + verifyImage.getVerifyId());
		rBucket.set(verifyImage.getResult(), 60, TimeUnit.SECONDS);
	}

	public void deleteVerifyCodeFromRedis(String verifyId) {
		RBucket<String> rBucket = redissonClient.getBucket(VERIFY_CODE_KEY + verifyId);
		rBucket.delete();
	}

	public String getVerifyCodeFromRedis(String verifyId) {
		String result = null;
		RBucket<String> rBucket = redissonClient.getBucket(VERIFY_CODE_KEY + verifyId);
		result = rBucket.get();
		return result;
	}
}
