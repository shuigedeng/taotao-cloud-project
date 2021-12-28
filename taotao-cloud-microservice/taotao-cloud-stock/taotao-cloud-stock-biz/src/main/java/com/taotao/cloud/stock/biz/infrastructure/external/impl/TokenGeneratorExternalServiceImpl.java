package com.taotao.cloud.stock.biz.infrastructure.external.impl;

import com.taotao.cloud.stock.biz.infrastructure.external.TokenGeneratorExternalService;
import com.xtoon.boot.sys.domain.external.TokenGeneratorExternalService;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.util.UUID;

/**
 * 生成Token外部实现类
 *
 * @author haoxin
 * @date 2021-04-23
 **/
@Component
public class TokenGeneratorExternalServiceImpl implements TokenGeneratorExternalService {

    @Override
    public String generateValue() {
        return generateValue(UUID.randomUUID().toString());
    }

    private static final char[] hexCode = "0123456789abcdef".toCharArray();

    public static String toHexString(byte[] data) {
        if(data == null) {
            return null;
        }
        StringBuilder r = new StringBuilder(data.length*2);
        for ( byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }

    public static String generateValue(String param) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(param.getBytes());
            byte[] messageDigest = algorithm.digest();
            return toHexString(messageDigest);
        } catch (Exception e) {
            throw new RuntimeException("生成Token失败", e);
        }
    }
}
