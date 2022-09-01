package com.taotao.cloud.core.sensitive.word.bs;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.List;


public class SensitiveWordBsRepeatTest {

    /**
     * 忽略重复词
     */
    @Test
    public void ignoreChineseStyleTest() {
        final String text = "ⒻⒻⒻfⓤuⓤ⒰cⓒ⒦ the bad words";

        List<String> wordList = SensitiveWordBs.newInstance()
                .ignoreRepeat(true)
                .findAll(text);
        Assert.assertEquals("[ⒻⒻⒻfⓤuⓤ⒰cⓒ⒦]", wordList.toString());
    }

}
