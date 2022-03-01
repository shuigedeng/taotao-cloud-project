package com.taotao.cloud.core.generator.bank;

import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;

public class BankCardNumberGeneratorTest {

	@Test
	public void testGenerate_by_bankName() {
		String bankCardNo = BankCardNumberGenerator.generate(BankNameEnum.CR, null);
		System.err.println(bankCardNo);
		assertNotNull(bankCardNo);

		bankCardNo = BankCardNumberGenerator.generate(BankNameEnum.ICBC, BankCardTypeEnum.CREDIT);
		System.err.println(bankCardNo);
		assertNotNull(bankCardNo);

		bankCardNo = BankCardNumberGenerator.generate(BankNameEnum.ICBC, BankCardTypeEnum.DEBIT);
		System.err.println(bankCardNo);
		assertNotNull(bankCardNo);
	}

	@Test
	public void testGenerateByPrefix() {
		String bankCardNo = BankCardNumberGenerator.generateByPrefix(436742);
		System.err.println(bankCardNo);
		assertNotNull(bankCardNo);
	}

	@Test
	public void testGenerate() {
		String bankCardNo = BankCardNumberGenerator.getInstance().generate();
		System.err.println(bankCardNo);
		assertNotNull(bankCardNo);
	}

}
