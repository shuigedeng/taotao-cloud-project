package com.taotao.cloud.seckill.biz.common.encrypt;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
/**
 * AES对称加密
 * 创建者 张志朋
 * 创建时间	2017年11月22日
 *
 */
public class SecurityAES {
	private final static String encoding = "UTF-8"; 
	private static String PASSWORD = "qwedcxza";
	
	
	/**
	 * AES加密
	 * @Author	张志朋
	 * @param content
	 * @param password
	 * @return  String
	 * @Date	2015年2月7日
	 * 更新日志
	 * 2015年2月7日 张志朋  首次创建
	 *
	 */
	public static String encryptAES(String content) {
		byte[] encryptResult = encrypt(content);
		String encryptResultStr = parseByte2HexStr(encryptResult);
		// BASE64位加密
		encryptResultStr = ebotongEncrypto(encryptResultStr);
		return encryptResultStr;
	}
	/**
	 * AES解密
	 * @Author	张志朋
	 * @param encryptResultStr
	 * @param password
	 * @return  String
	 * @Date	2015年2月7日
	 * 更新日志
	 * 2015年2月7日 张志朋  首次创建
	 *
	 */
	public static String decrypt(String encryptResultStr) {
		// BASE64位解密
		String decrpt = ebotongDecrypto(encryptResultStr);
		byte[] decryptFrom = parseHexStr2Byte(decrpt);
		byte[] decryptResult = decrypt(decryptFrom);
		return new String(decryptResult);
	}
	/**
	 * 加密字符串
	 * @Author	张志朋
	 * @param str
	 * @return  String
	 * @Date	2015年2月7日
	 * 更新日志
	 * 2015年2月7日 张志朋  首次创建
	 *
	 */
	public static String ebotongEncrypto(String str) {
		String result = str;
		if (str != null && str.length() > 0) {
			try {
				byte[] encodeByte = str.getBytes(encoding);
				//阿里巴巴
				//result = Base64.byteArrayToBase64(encodeByte);
				result  = new String(Base64.encodeBase64(encodeByte),"Utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//base64加密超过一定长度会自动换行 需要去除换行符
		return result.replaceAll("\r\n", "").replaceAll("\r", "").replaceAll("\n", "");
	}
	/**
	 * 解密字符串
	 * @Author	张志朋
	 * @param str
	 * @return  String
	 * @Date	2015年2月7日
	 * 更新日志
	 * 2015年2月7日 张志朋  首次创建
	 *
	 */
	public static String ebotongDecrypto(String str) {
		try {
			//byte[] encodeByte  = Base64.base64ToByteArray(str);//阿里巴巴
			byte[] encodeByte  = Base64.decodeBase64(str.getBytes("Utf-8"));
			
			return new String(encodeByte);
		} catch (Exception e) {
			e.printStackTrace();
			return str;
		}
	}
	/**
	 * 加密  
	 * @Author	张志朋
	 * @param content
	 * @param password
	 * @return  byte[]
	 * @Date	2015年2月7日
	 * 更新日志
	 * 2015年2月7日 张志朋  首次创建
	 *
	 */
	private static byte[] encrypt(String content) {   
		try {              
			KeyGenerator kgen = KeyGenerator.getInstance("AES"); 
			//防止linux下 随机生成key
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );   
			secureRandom.setSeed(PASSWORD.getBytes());   
			kgen.init(128, secureRandom);
			SecretKey secretKey = kgen.generateKey();   
			byte[] enCodeFormat = secretKey.getEncoded();   
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");   
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器   
			byte[] byteContent = content.getBytes("utf-8");   
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化   
			byte[] result = cipher.doFinal(byteContent);   
			return result; // 加密   
		} catch (NoSuchAlgorithmException e) {   
			e.printStackTrace();   
		} catch (NoSuchPaddingException e) {   
			e.printStackTrace();   
		} catch (InvalidKeyException e) {   
			e.printStackTrace();   
		} catch (UnsupportedEncodingException e) {   
			e.printStackTrace();   
		} catch (IllegalBlockSizeException e) {   
			e.printStackTrace();   
		} catch (BadPaddingException e) {   
			e.printStackTrace();   
		}   
		return null;   
	}  
	/**
	 * 解密
	 * @Author	张志朋
	 * @param content
	 * @param password
	 * @return  byte[]
	 * @Date	2015年2月7日
	 * 更新日志
	 * 2015年2月7日 张志朋  首次创建
	 *
	 */
	private static byte[] decrypt(byte[] content) {   
		try {   
			KeyGenerator kgen = KeyGenerator.getInstance("AES"); 
			//防止linux下 随机生成key
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );   
			secureRandom.setSeed(PASSWORD.getBytes());   
			kgen.init(128, secureRandom);
			SecretKey secretKey = kgen.generateKey();   
			byte[] enCodeFormat = secretKey.getEncoded();   
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");               
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器   
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化   
			byte[] result = cipher.doFinal(content);   
			return result; // 加密   
		} catch (NoSuchAlgorithmException e) {   
			e.printStackTrace();   
		} catch (NoSuchPaddingException e) {   
			e.printStackTrace();   
		} catch (InvalidKeyException e) {   
			e.printStackTrace();   
		} catch (IllegalBlockSizeException e) {   
			e.printStackTrace();   
		} catch (BadPaddingException e) {   
			e.printStackTrace();   
		}   
		return null;   
	}  
	/**
	 * 将二进制转换成16进制  
	 * @Author	张志朋
	 * @param buf
	 * @return  String
	 * @Date	2015年2月7日
	 * 更新日志
	 * 2015年2月7日 张志朋  首次创建
	 *
	 */
	public static String parseByte2HexStr(byte buf[]) {   
		StringBuffer sb = new StringBuffer();   
		for (int i = 0; i < buf.length; i++) {   
			String hex = Integer.toHexString(buf[i] & 0xFF);   
			if (hex.length() == 1) {   
				hex = '0' + hex;   
			}   
			sb.append(hex.toUpperCase());   
		}   
		return sb.toString();   
	}  
	/**
	 * 将16进制转换为二进制  
	 * @Author	张志朋
	 * @param hexStr
	 * @return  byte[]
	 * @Date	2015年2月7日
	 * 更新日志
	 * 2015年2月7日 张志朋  首次创建
	 *
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {   
		if (hexStr.length() < 1)   
			return null;   
		byte[] result = new byte[hexStr.length()/2];   
		for (int i = 0;i< hexStr.length()/2; i++) {   
			int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);   
			int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);   
			result[i] = (byte) (high * 16 + low);   
		}   
		return result;   
	}
	public static void main(String[] args) {
		String str = encryptAES("1234567890");
		System.out.println(str);
		str = decrypt(str);
		System.out.println(str);
	}
}
