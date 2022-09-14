package com.taotao.cloud.sensitive.sensitive.word.bs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SensitiveWordBsEmailTest {

    /**
     * 邮箱测试
     */
    @Test
    public void emailEnglishTest() {
        final String text = "楼主好人，邮箱 sensitiveword@xx.com";

        List<String> wordList = SensitiveWordBs.newInstance().findAll(text);
        Assertions.assertEquals("[邮箱, sensitiveword@xx.com]", wordList.toString());
    }

    /**
     * 邮箱测试
     */
    @Test
    public void emailNumberTest() {
        final String text = "楼主好人，邮箱 123456789@xx.com";

        List<String> wordList = SensitiveWordBs.newInstance().findAll(text);
        Assertions.assertEquals("[邮箱, 123456789, xx.com]", wordList.toString());
    }

}
