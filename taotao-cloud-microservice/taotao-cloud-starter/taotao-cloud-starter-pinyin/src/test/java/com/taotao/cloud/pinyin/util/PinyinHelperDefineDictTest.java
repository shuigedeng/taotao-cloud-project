package com.taotao.cloud.pinyin.util;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分词测试
 */
public class PinyinHelperDefineDictTest {

    @Test
    public void charMapPutTest() {
        Map<String, List<String>> map = new HashMap<>();
        map.put("噯", Arrays.asList("āi", "ǎi", "ài"));
        map.put("噯", Arrays.asList("ài"));

        Assert.assertEquals("[ài]", map.get("噯").toString());
    }

    /**
     * 自定义词典测试
     */
    @Test
    public void defineCharDictTest() {
        String pinyin = PinyinHelper.toPinyin("莪");
        Assert.assertEquals("wǒ", pinyin);

        String pinyin2 = PinyinHelper.toPinyin("噯");
        Assert.assertEquals("ài", pinyin2);
    }

    /**
     * 自定义词典测试
     */
    @Test
    public void definePhraseDictTest() {
        String pinyin = PinyinHelper.toPinyin("褈慶炎鍋");
        Assert.assertEquals("chóng qìng huǒ guō", pinyin);
    }

    /**
     * 自定义字典测试
     */
    @Test
    public void defineDictTest() {
        String pinyin = PinyinHelper.toPinyin("莪噯褈慶炎鍋");
        Assert.assertEquals("wǒ ài chóng qìng huǒ guō", pinyin);
    }

}
