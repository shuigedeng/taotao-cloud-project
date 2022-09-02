package com.taotao.cloud.common.generator.bank;

import com.taotao.cloud.common.support.generator.bank.BankCardNumberGenerator;
import com.taotao.cloud.common.support.generator.bank.BankCardTypeEnum;
import com.taotao.cloud.common.support.generator.bank.BankNameEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertNotNull;

public class BankCardNumberGeneratorTest {

	@Test
	public void testGenerate_by_bankName() {
		String bankCardNo = BankCardNumberGenerator.generate(BankNameEnum.CR, null);
		System.err.println(bankCardNo);
		Assertions.assertNotNull(bankCardNo);

		bankCardNo = BankCardNumberGenerator.generate(BankNameEnum.ICBC, BankCardTypeEnum.CREDIT);
		System.err.println(bankCardNo);
		Assertions.assertNotNull(bankCardNo);

		bankCardNo = BankCardNumberGenerator.generate(BankNameEnum.ICBC, BankCardTypeEnum.DEBIT);
		System.err.println(bankCardNo);
		Assertions.assertNotNull(bankCardNo);
	}

	@Test
	public void testGenerateByPrefix() {
		String bankCardNo = BankCardNumberGenerator.generateByPrefix(436742);
		System.err.println(bankCardNo);
		Assertions.assertNotNull(bankCardNo);
	}

	@Test
	public void testGenerate() {
		String bankCardNo = BankCardNumberGenerator.getInstance().generate();
		System.err.println(bankCardNo);
		Assertions.assertNotNull(bankCardNo);
	}

}
