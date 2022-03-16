package com.taotao.cloud.member.biz.connect.token;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;

/**
 * SignWithUtil
 *
 * @author Chopper
 * @version v1.0
 * 2020-11-18 17:30
 */
public class SecretKeyUtil {
    public static SecretKey generalKey() {
        //自定义
        //byte[] encodedKey = Base64.decodeBase64("cuAihCz53DZRjZwbsGcZJ2Ai6At+T142uphtJMsk7iQ=");
        //SecretKey key = Keys.hmacShaKeyFor(encodedKey);
        //return key;
	    return null;
    }

    public static SecretKey generalKeyByDecoders() {
        //return Keys.hmacShaKeyFor(Decoders.BASE64.decode("cuAihCz53DZRjZwbsGcZJ2Ai6At+T142uphtJMsk7iQ="));
	    return null;
    }
}
