package com.taotao.cloud.core.pinyin.util;

import com.taotao.cloud.common.utils.pinyin.util.PinyinHelper;
import org.junit.Assert;
import org.junit.Test;

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
