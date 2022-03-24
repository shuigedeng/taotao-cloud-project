package com.taotao.cloud.core.pinyin.util;

import com.taotao.cloud.common.utils.pinyin.util.PinyinHelper;
import com.taotao.cloud.core.pinyin.constant.enums.PinyinStyleEnum;
import org.junit.Assert;
import org.junit.Test;

public class PinyinHelperStyleTest {

    /**
     * 默认格式-中文测试
     * @since 0.0.3
     */
    @Test
    public void toPinyinDefaultTest() {
        String pinyin = PinyinHelper.toPinyin("我爱中文");
        Assert.assertEquals("wǒ ài zhōng wén", pinyin);
    }

    /**
     * 默认格式-中文测试
     * @since 0.0.3
     */
    @Test
    public void toPinyinDefault2Test() {
        String pinyin = PinyinHelper.toPinyin("我爱中文", PinyinStyleEnum.DEFAULT);
        Assert.assertEquals("wǒ ài zhōng wén", pinyin);
    }

    /**
     * 普通格式-中文
     * @since 0.0.9
     */
    @Test
    public void toPinyinPhraseTest() {
        String pinyin = PinyinHelper.toPinyin("单一");
        Assert.assertEquals("dān yī", pinyin);
    }


    /**
     * 普通格式-中文测试
     * @since 0.0.3
     */
    @Test
    public void toPinyinNormalTest() {
        String pinyin = PinyinHelper.toPinyin("我爱中文", PinyinStyleEnum.NORMAL);
        Assert.assertEquals("wo ai zhong wen", pinyin);
    }

    /**
     * 普通格式-中文
     * @since 0.0.9
     */
    @Test
    public void toPinyinPhraseNormalTest() {
        String pinyin = PinyinHelper.toPinyin("单一", PinyinStyleEnum.NORMAL);
        Assert.assertEquals("dan yi", pinyin);
    }

    /**
     * 数字标注在最后格式-中文测试
     * @since 0.0.3
     */
    @Test
    public void toPinyinNumLastTest() {
        String pinyin = PinyinHelper.toPinyin("我爱中文", PinyinStyleEnum.NUM_LAST);
        Assert.assertEquals("wo3 ai4 zhong1 wen2", pinyin);
    }

    /**
     * num-last 格式-中文
     * @since 0.0.9
     */
    @Test
    public void toPinyinPhraseNumLastTest() {
        String pinyin = PinyinHelper.toPinyin("单一", PinyinStyleEnum.NUM_LAST);
        Assert.assertEquals("dan1 yi1", pinyin);
    }

    /**
     * 首字母格式-中文测试
     * @since 0.0.3
     */
    @Test
    public void toPinyinFirstLetterTest() {
        String pinyin = PinyinHelper.toPinyin("我爱中文", PinyinStyleEnum.FIRST_LETTER);
        Assert.assertEquals("w a z w", pinyin);
    }

    /**
     * 首字母格式-中文测试
     * @since 0.0.9
     */
    @Test
    public void toPinyinPhraseFirstLetterTest() {
        String pinyin = PinyinHelper.toPinyin("单一", PinyinStyleEnum.FIRST_LETTER);
        Assert.assertEquals("d y", pinyin);
    }

    @Test
    public void inputTest() {
        String text = "女孩";
        String pinyin3 = PinyinHelper.toPinyin(text, PinyinStyleEnum.INPUT);
        Assert.assertEquals("nv hai", pinyin3);
    }


}
