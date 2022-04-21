package com.taotao.cloud.core.pinyin.util;

import com.taotao.cloud.common.utils.pinyin.constant.enums.PinyinToneNumEnum;
import org.junit.Assert;
import org.junit.Test;

/**
 * 音调获取
 */
public class PinyinHelperToneNumTest {

    /**
     * 音调列表测试
     */
    @Test
    public void toneNumListTest() {
        final char ch = '赵';
        final char ch2 = '钱';
        final char ch3 = '孙';
        final char ch4 = '李';

//        Assert.assertEquals("[4]", PinyinHelper.toneNumList(ch).toString());
//        Assert.assertEquals("[2]", PinyinHelper.toneNumList(ch2).toString());
//        Assert.assertEquals("[1]", PinyinHelper.toneNumList(ch3).toString());
//        Assert.assertEquals("[3]", PinyinHelper.toneNumList(ch4).toString());
    }

    /**
     * 音调列表测试
     */
    @Test
    public void toneNumList2Test() {
        final String text = "赵钱孙李";

//        Assert.assertEquals("[4, 2, 1, 3]", PinyinHelper.toneNumList(text).toString());
    }

    @Test
    public void initTest() {
//        String simple = PinyinChineses.simple().toSimple("赵");
//        System.out.println(simple);
//
//        System.out.println(PinyinHelper.toPinyinList('赵'));
    }

    @Test
    public void toneNumCategoryTest() {
        Assert.assertTrue(PinyinToneNumEnum.isPing(1));
        Assert.assertTrue(PinyinToneNumEnum.isPing(2));
        Assert.assertTrue(PinyinToneNumEnum.isZe(3));
        Assert.assertTrue(PinyinToneNumEnum.isZe(4));
        Assert.assertTrue(PinyinToneNumEnum.isSoftly(5));
    }

}
