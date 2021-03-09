package com.taotao.cloud.java.javaweb.p12_myshop.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//MD5 生成器
public class MD5Utils {
	public static String md5(String password){
		//生成一个md5加密器
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			//计算MD5 的值
			md.update(password.getBytes());
			//BigInteger 将8位的字符串 转成16位的字符串 得到的字符串形式是哈希码值
			//BigInteger(参数1,参数2) 参数1 是 1为正数 0为0 -1为负数
			return new BigInteger(1, md.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
