package com.taotao.cloud.common.generator;

import com.taotao.cloud.common.support.generator.EmailAddressGenerator;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class EmailAddressGeneratorTest {

    @Test
    public void testGenerate() {
        String generatedEmail = EmailAddressGenerator.getInstance().generate();
        System.err.println(generatedEmail);
        assertNotNull(generatedEmail);
    }

}
