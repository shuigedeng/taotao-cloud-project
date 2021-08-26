package com.taotao.cloud.seckill.biz.common.encrypt;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
 
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

/**
 * 对称加密
 * 这里面的API里面有很多是调用getInstance方法，这个方法的参数有algorithm或者transformation
 * 一：algorithm：算法
 * 
 * 二：transformation：有两种格式
 * 1：算法/模式/填充方式。如：DES/CBC/PKCS5Padding
 * 2：算法。                              如：DES
 * 
 * 其中，algorithm、transformation的值，不区分大小写
 * 
 * Java加密解密官方参考文档：
 * https://docs.oracle.com/javase/8/docs/technotes/guides/security/index.html
 * https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html
 */
public class SecurityDES {
	/*
	 * 使用KeyGenerator生成key
	 * 
	 * 其中，algorithm支持的算法有：AES、DES、DESEDE、HMACMD5、HMACSHA1、HMACSHA256、RC2等
	 * 全部支持的算法见官方文档
	 * https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#KeyGenerator
	 *  
	 */
	public static Key newKeyByKeyGenerator(String algorithm) throws NoSuchAlgorithmException {
		KeyGenerator kg = KeyGenerator.getInstance(algorithm);
		Key key = kg.generateKey();
		return key;
	}
	
	/**
	 * 使用SecretKeySpec生成key
	 * 一般是从一个文件中读取出key的byte数组，然后根据文件key的算法，构建出key对象
	 */
	public static Key newKeyBySecretKeySpec(byte[] key, String algorithm) throws NoSuchAlgorithmException {
		return new SecretKeySpec(key, algorithm);
	}
	
	/**
	 * 加密，对字符串进行加密，返回结果为byte数组
	 * 保存的时候，可以把byte数组进行base64编码成字符串，或者把byte数组转换成16进制的字符串
	 * 
	 * 其中，transformation支持的全部算法见官方文档：
	 * https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Cipher
	 */
	public static byte[] encrypt(String transformation, Key key, String password) throws Exception {
		Cipher cipher = Cipher.getInstance(transformation);
		//加密模式
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(password.getBytes());
	}
	
	/**
	 * 解密，返回结果为原始字符串
	 * 
	 * 其中，transformation支持的全部算法见官方文档：
	 * https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Cipher
	 */
	public static String decrypt(String transformation, Key key, byte[] data) throws Exception {
		Cipher cipher = Cipher.getInstance(transformation);
		//解密模式
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] result = cipher.doFinal(data);
		String password = new String(result);
		return password;
	}
	
	public static void main(String[] args) throws Exception {
		String password = "123456";
		
		String algorithm = "DES";
		String transformation = algorithm;
		
		//加密解密使用的都是同一个秘钥key
		Key key = newKeyByKeyGenerator(algorithm);
		System.out.println(" 秘钥: " + key);
		//加密
		byte[] passData = encrypt(transformation, key, password);
		//解密
		String pass = decrypt(transformation, key, passData);
		
		System.out.println("解密后的密码 : " + pass);
	}
}
