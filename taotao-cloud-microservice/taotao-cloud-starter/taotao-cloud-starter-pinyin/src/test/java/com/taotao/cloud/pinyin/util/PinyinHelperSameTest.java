package com.taotao.cloud.pinyin.util;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 * 同音字相关
 */
public class PinyinHelperSameTest {

    /**
     * 同音字判断
     */
    @Test
    public void hasSamePinyinTest() {
        char one = '花';
        char two = '重';
        char three = '中';
        char four = '虫';

        Assert.assertFalse(PinyinHelper.hasSamePinyin(one, three));
        Assert.assertTrue(PinyinHelper.hasSamePinyin(two, three));
        Assert.assertTrue(PinyinHelper.hasSamePinyin(two, four));
    }

}
