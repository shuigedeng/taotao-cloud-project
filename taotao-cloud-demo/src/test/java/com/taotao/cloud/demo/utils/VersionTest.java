package com.taotao.cloud.demo.utils;

import com.taotao.cloud.common.utils.Version;
import org.junit.Test;

public class VersionTest {

	@Test
	public void test() {
		Version.of(null).incomplete().eq(null);
	}

	public static void main(String[] args) {
		System.out.println();
	}
}
