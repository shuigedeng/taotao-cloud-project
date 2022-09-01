package com.taotao.cloud.core.sensitive.word.bs;

import org.junit.Assert;
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
        Assert.assertEquals("[邮箱, sensitiveword@xx.com]", wordList.toString());
    }

    /**
     * 邮箱测试
     */
    @Test
    public void emailNumberTest() {
        final String text = "楼主好人，邮箱 123456789@xx.com";

        List<String> wordList = SensitiveWordBs.newInstance().findAll(text);
        Assert.assertEquals("[邮箱, 123456789, xx.com]", wordList.toString());
    }

}
