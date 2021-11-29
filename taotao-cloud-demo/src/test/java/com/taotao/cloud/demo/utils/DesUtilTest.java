//package com.taotao.cloud.demo.utils;
//
//import org.junit.Assert;
//import org.junit.Test;
//
///**
// * des 单元测试
// *
// */
//public class DesUtilTest {
//
//	@Test
//	public void test() {
//		String text = "我爱mica";
//		String aesKey = DesUtil.genDesKey();
//		String encryptHex = DesUtil.encryptToHex(text, aesKey);
//		String decryptHex = DesUtil.decryptFormHex(encryptHex, aesKey);
//		Assert.assertEquals(text, decryptHex);
//		String encryptBase64 = DesUtil.encryptToBase64(text, aesKey);
//		String decryptBase64 = DesUtil.decryptFormBase64(encryptBase64, aesKey);
//		Assert.assertEquals(text, decryptBase64);
//	}
//}
