package com.taotao.cloud.core.compress;

import java.util.Random;

public class CompressTest {

	public static final String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static String generateString(int length) {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(allChar.charAt(random.nextInt(allChar.length())));
		}

		return sb.toString();
	}

	public static void main(String[] args) {
		try {
			Compress compress = CompressFactory.CF.getExtension("gzip");

			String data = generateString(1024 * 10);
			System.out.println("压缩前数据内容：" + data);
			byte[] dataBytes = data.getBytes();
			System.out.println("压缩前数据大小：" + dataBytes.length);

			byte[] resultBytes = compress.compress(dataBytes);
			System.out.println("压缩后数据大小:" + resultBytes.length);
			
			byte[] uncompressBytes = compress.uncompress(resultBytes);
			System.out.println("解压后数据大小：" + uncompressBytes.length);
			String result = new String(uncompressBytes);
			System.out.println("解压后数据内容：" + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
