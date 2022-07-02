package com.taotao.cloud.sys.biz.generator;

import static org.testng.Assert.assertNotNull;

import com.taotao.cloud.sys.biz.support.generator.ChineseAddressGenerator;
import org.testng.annotations.Test;

public class ChineseAddressGeneratorTest {

    @Test
    public void testGenerate() {
        String generatedAddress = ChineseAddressGenerator.getInstance()
            .generate();
        System.err.println(generatedAddress);
        assertNotNull(generatedAddress);
    }

}
