package com.taotao.cloud.core.heaven.util.secrect;

import com.taotao.cloud.core.heaven.response.exception.CommonRuntimeException;
import com.taotao.cloud.core.heaven.util.common.ArgUtil;
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * AES 加解密工具类
 *
 */
public final class AesUtil {

    private AesUtil() {
    }

    /**
     * 加密
     *
     * @param sourceText 原始文本
     * @param key        密匙
     * @return 结果
     * @since 0.1.98
     */
    public static String encrypt(String sourceText, String key) {
        //ArgUtil.notEmpty(sourceText, "sourceText");
        //ArgUtil.notEmpty(key, "key");
		//
        //try {
        //    byte[] raw = key.getBytes();
        //    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        //    //"算法/模式/补码方式"
        //    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        //    //使用CBC模式，需要一个向量iv，可增加加密算法的强度
        //    IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
        //    cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        //    byte[] encrypted = cipher.doFinal(sourceText.getBytes());
		//
        //    //此处使用BASE64做转码功能，同时能起到2次加密的作用。
        //    return new BASE64Encoder().encode(encrypted);
        //} catch (Exception e) {
        //    throw new CommonRuntimeException(e);
        //}
	    return sourceText;
    }

    /**
     * 解密
     *
     * @param sourceText 原始文本
     * @param key        密匙
     * @return 结果
     * @since 0.1.98
     */
    public static String decrypt(String sourceText, String key) {
        //ArgUtil.notEmpty(sourceText, "sourceText");
        //ArgUtil.notEmpty(key, "key");
		//
        //try {
        //    byte[] raw = key.getBytes(StandardCharsets.UTF_8);
        //    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        //    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        //    IvParameterSpec iv = new IvParameterSpec("0102030405060708"
        //            .getBytes());
        //    cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        //    //先用base64解密
        //    byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sourceText);
        //    byte[] original = cipher.doFinal(encrypted1);
		//
        //    return new String(original);
        //} catch (Exception ex) {
        //    throw new CommonRuntimeException(ex);
        //}
	    return sourceText;
    }

}
