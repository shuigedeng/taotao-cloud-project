package com.taotao.cloud.seckill.biz.common.encrypt;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
/**
 * =========
 * 适用于数字签名标准（DigitalSignature Standard DSS）
 * 比如支付宝密钥加签方式就是 RSA(SHA1) 或者  RSA(SHA256)
 * 注意，数字签名其实是独立于哈希算法、AES对称加密、RSA非对称加密的，或者说数字签名是它们三者的一种应用，不要以为数字签名就是专属于RSA非对称加密的。
 * =========
 * 创建者 柒
 * 创建时间	2018年11月27日
 */
public class EncrypSHA {
	
	private static String secret = "itstyle";

	@Deprecated
	public byte[] eccryptSHA(String msg) throws NoSuchAlgorithmException{
		MessageDigest md5 = MessageDigest.getInstance("SHA");
		byte[] srcBytes = msg.getBytes();
		//使用srcBytes更新摘要
		md5.update(srcBytes);
		//完成哈希计算，得到result
		byte[] resultBytes = md5.digest();
		return resultBytes;
	}
	public String eccryptSHA2(String msg) throws NoSuchAlgorithmException, InvalidKeyException{
    	Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
	    SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
	    sha256_HMAC.init(secret_key);
	    String hash = Base64.encodeBase64String(sha256_HMAC.doFinal(msg.getBytes()));
	    return hash;
	}
	/**
	 * @param args
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {
		String msg = "爪哇笔记";
		EncrypSHA sha = new EncrypSHA();
		System.out.println(sha.eccryptSHA2(msg));
	}
}

