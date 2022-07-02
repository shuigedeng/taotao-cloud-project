package com.taotao.cloud.sys.biz.generator;

import static org.testng.Assert.assertNotNull;

import com.taotao.cloud.sys.biz.support.generator.EmailAddressGenerator;
import org.testng.annotations.Test;

public class EmailAddressGeneratorTest {

    @Test
    public void testGenerate() {
        String generatedEmail = EmailAddressGenerator.getInstance().generate();
        System.err.println(generatedEmail);
        assertNotNull(generatedEmail);
    }

}
