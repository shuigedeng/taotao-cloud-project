package com.taotao.cloud.core.generator;

import com.taotao.cloud.core.generator.base.GenericGenerator;
import org.apache.commons.lang3.RandomStringUtils;

public class EmailAddressGenerator extends GenericGenerator {
    private static GenericGenerator instance = new EmailAddressGenerator();

    private EmailAddressGenerator() {
    }

    public static GenericGenerator getInstance() {
        return instance;
    }

    @Override
    public String generate() {
        StringBuilder result = new StringBuilder();
        result.append(RandomStringUtils.randomAlphanumeric(10));
        result.append("@");
        result.append(RandomStringUtils.randomAlphanumeric(5));
        result.append(".");
        result.append(RandomStringUtils.randomAlphanumeric(3));

        return result.toString().toLowerCase();
    }
}
