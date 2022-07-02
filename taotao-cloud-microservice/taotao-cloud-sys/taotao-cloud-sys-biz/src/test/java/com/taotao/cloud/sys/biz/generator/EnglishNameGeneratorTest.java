package com.taotao.cloud.sys.biz.generator;

import static org.testng.Assert.assertNotNull;

import com.taotao.cloud.sys.biz.support.generator.EnglishNameGenerator;
import org.testng.annotations.Test;

public class EnglishNameGeneratorTest {

    @Test
    public void testGenerate() {
        String generatedName = EnglishNameGenerator.getInstance().generate();
        assertNotNull(generatedName);
        System.err.println(generatedName);
    }

}
