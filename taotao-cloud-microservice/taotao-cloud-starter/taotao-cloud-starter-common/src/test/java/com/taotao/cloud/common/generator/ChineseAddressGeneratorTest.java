package com.taotao.cloud.common.generator;

import com.taotao.cloud.common.support.generator.ChineseAddressGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertNotNull;

public class ChineseAddressGeneratorTest {

    @Test
    public void testGenerate() {
        String generatedAddress = ChineseAddressGenerator.getInstance()
            .generate();
        System.err.println(generatedAddress);
        Assertions.assertNotNull(generatedAddress);
    }

}
