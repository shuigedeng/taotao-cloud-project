package com.taotao.cloud.core.sensitive.word.bs;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class SensitiveWordBsNumTest {

    /**
     * 返回所有敏感词
     */
    @Test
    public void findAllTest() {
        final String text = "这个是我的微信：9989123456";

        List<String> wordList = SensitiveWordBs.newInstance().findAll(text);
        Assert.assertEquals("[微信, 9989123456]", wordList.toString());
    }

    /**
     * 返回所有敏感词
     */
    @Test
    public void ignoreNumStyleTest() {
        final String text = "这个是我的微信：9⓿二肆⁹₈③⑸⒋➃㈤㊄";

        List<String> wordList = SensitiveWordBs.newInstance().findAll(text);
        Assert.assertEquals("[微信, 9⓿二肆⁹₈③⑸⒋➃㈤㊄]", wordList.toString());
    }

}
