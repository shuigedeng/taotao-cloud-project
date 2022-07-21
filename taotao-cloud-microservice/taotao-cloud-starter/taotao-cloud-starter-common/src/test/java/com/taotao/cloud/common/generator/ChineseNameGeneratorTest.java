package com.taotao.cloud.common.generator;

import com.taotao.cloud.common.support.generator.ChineseNameGenerator;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;


public class ChineseNameGeneratorTest {

    @Test
    public void testGenerate() {
        String generatedName = ChineseNameGenerator.getInstance().generate();
        assertNotNull(generatedName);
        System.err.println(generatedName);
    }

    @Test
    public void testGenerateOdd() {
        String generatedName = ChineseNameGenerator.getInstance().generateOdd();
        assertNotNull(generatedName);
        System.err.println(generatedName);
    }

}
