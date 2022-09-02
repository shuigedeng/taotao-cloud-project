package com.taotao.cloud.common.generator;


import com.taotao.cloud.common.support.generator.ChineseIDCardNumberGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.Assert.assertNotNull;


public class ChineseIDCardNumberGeneratorTest {

    @Test
    public void generateRandomDate() {
        Date randomDate = ChineseIDCardNumberGenerator.randomDate();
        System.err.println(randomDate);
        Assertions.assertNotNull(randomDate);
    }

    @Test
    public void testGenerate() {
        String idCard = ChineseIDCardNumberGenerator.getInstance().generate();
        System.err.println(idCard);
        Assertions.assertNotNull(idCard);
        if (idCard.charAt(idCard.length()-2)%2 == 0){
            System.err.println("女");
        } else {
            System.err.println("男");
        }
    }

    @Test
    public void testGenerateIssueOrg() {
        String issueOrg = ChineseIDCardNumberGenerator.generateIssueOrg();
        System.err.println(issueOrg);
        Assertions.assertNotNull(issueOrg);
    }

    @Test
    public void testGenerateValidPeriod() {
        String result = ChineseIDCardNumberGenerator.generateValidPeriod();
        System.err.println(result);
        Assertions.assertNotNull(result);
    }

}
