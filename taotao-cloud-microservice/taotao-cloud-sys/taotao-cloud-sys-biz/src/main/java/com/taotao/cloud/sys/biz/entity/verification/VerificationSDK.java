package com.taotao.cloud.sys.biz.entity.verification;

import com.taotao.cloud.common.enums.CachePrefix;
import com.taotao.cloud.redis.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 验证码sdk
 *
 */
@Component
public class VerificationSDK {

    @Autowired
    private RedisRepository redisRepository;

    /**
     * 生成一个token 用于获取校验中心的验证码逻辑
     */
    public boolean checked(String verificationKey, String uuid) {
        //生成校验KEY，在验证码服务做记录
        String key = CachePrefix.VERIFICATION_KEY.getPrefix() + verificationKey;
        //cache.get(key);
        return true;
    }


}
