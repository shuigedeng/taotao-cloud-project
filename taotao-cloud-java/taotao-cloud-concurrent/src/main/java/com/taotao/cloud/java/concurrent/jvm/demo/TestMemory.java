package com.taotao.cloud.java.concurrent.jvm.demo;

import java.util.ArrayList;

/**
 * 64kb/50毫秒
 *
 * @author shuigedeng
 * @since 2020/12/3 下午6:49
 * @version 2022.04
 */
public class TestMemory {
	static class OOMObject {
		public byte[] placeholder = new byte[64 * 1024 * 40];
	}

	public static void fillHeap(int num) throws Exception {
		ArrayList<OOMObject> list = new ArrayList<>();
		for (int i = 0; i < num; i++) {
			Thread.sleep(50);
			list.add(new OOMObject());
		}
		System.gc();
	}

	public static void main(String[] args) throws Exception {
		Thread.sleep(10000);
		fillHeap(100);
		Thread.sleep(20000000);
	}
}
