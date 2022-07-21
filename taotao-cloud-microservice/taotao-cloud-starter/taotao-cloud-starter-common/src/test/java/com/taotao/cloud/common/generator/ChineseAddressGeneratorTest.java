package com.taotao.cloud.common.generator;

import com.taotao.cloud.common.support.generator.ChineseAddressGenerator;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ChineseAddressGeneratorTest {

    @Test
    public void testGenerate() {
        String generatedAddress = ChineseAddressGenerator.getInstance()
            .generate();
        System.err.println(generatedAddress);
        assertNotNull(generatedAddress);
    }

}
