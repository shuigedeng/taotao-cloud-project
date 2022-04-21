package com.taotao.cloud.core.sensitive.word.bs;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class SensitiveWordBsUserDefineTest {

    /**
     * 自定义允许和拒绝的文件
     */
    @Test
    public void allowAndDenyTest() {
        final String text = "gender 我们认为应该通过，自定义敏感词我们认为应该拒绝。";

        List<String> wordList = SensitiveWordBs.newInstance().findAll(text);
        Assert.assertEquals("[自定义敏感词]", wordList.toString());
    }

}
