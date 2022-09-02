package com.taotao.cloud.common.generator;

import com.taotao.cloud.common.support.generator.ChineseMobileNumberGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.Assert.assertNotNull;

public class ChineseMobileNumberGeneratorTest {
	@Test
	public void testGenerate() {
		String generatedMobileNum = ChineseMobileNumberGenerator.getInstance()
			.generate();
		Assertions.assertNotNull(generatedMobileNum);
		System.err.println(generatedMobileNum);
	}
	@Test
	public void testGgenerateFake() {
		String generatedMobileNum = ChineseMobileNumberGenerator.getInstance()
			.generateFake();
		Assertions.assertNotNull(generatedMobileNum);
		System.err.println(generatedMobileNum);
	}
}
