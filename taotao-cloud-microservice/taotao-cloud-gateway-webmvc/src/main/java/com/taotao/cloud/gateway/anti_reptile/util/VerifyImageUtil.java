/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.gateway.anti_reptile.util;

import com.taotao.boot.captcha.captcha.utils.CaptchaUtil;
import com.taotao.cloud.gateway.anti_reptile.module.VerifyImageDTO;

import java.io.ByteArrayOutputStream;
import java.time.Duration;
import java.util.Base64;
import java.util.UUID;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

/**
 * VerifyImageUtil
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class VerifyImageUtil {

    private static final String VERIFY_CODE_KEY = "tt_antireptile_verifycdoe_";

    private final RedissonClient redissonClient;

    public VerifyImageUtil( RedissonClient redissonClient ) {
        this.redissonClient = redissonClient;
    }

    public VerifyImageDTO generateVerifyImg() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String result = CaptchaUtil.out(outputStream);
        String base64Image =
                "data:image/jpeg;base64,"
                        + Base64.getEncoder().encodeToString(outputStream.toByteArray());
        String verifyId = UUID.randomUUID().toString();
        return new VerifyImageDTO(verifyId, null, base64Image, result);
    }

    public void saveVerifyCodeToRedis( VerifyImageDTO verifyImage ) {
        RBucket<String> rBucket =
                redissonClient.getBucket(VERIFY_CODE_KEY + verifyImage.getVerifyId());
        rBucket.set(verifyImage.getResult(), Duration.ofSeconds(60L));
    }

    public void deleteVerifyCodeFromRedis( String verifyId ) {
        RBucket<String> rBucket = redissonClient.getBucket(VERIFY_CODE_KEY + verifyId);
        rBucket.delete();
    }

    public String getVerifyCodeFromRedis( String verifyId ) {
        String result = null;
        RBucket<String> rBucket = redissonClient.getBucket(VERIFY_CODE_KEY + verifyId);
        result = rBucket.get();
        return result;
    }
}
