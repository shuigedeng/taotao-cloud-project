package com.taotao.cloud.core.pinyin.bs;

import com.taotao.cloud.core.pinyin.support.style.PinyinToneStyles;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author binbin.hou
 * @since 0.0.1
 */
public class PinyinBsTest {

    /**
     * 拼音转换测试
     * @since 0.0.1
     */
    @Test
    public void toPinyinTest() {
        String text = "我爱中文";
        PinyinBs pinyinBs = PinyinBs.newInstance();
        String pinyin = pinyinBs.toPinyin(text);
        Assert.assertEquals("wǒ ài zhōng wén", pinyin);

        String text2 = "也爱编程";
        String pinyin2 = pinyinBs.toPinyin(text2);
        Assert.assertEquals("yě ài biān chéng", pinyin2);
    }

    /**
     * 返回多音字列表测试
     * @since 0.0.2
     */
    @Test
    public void toPinyinListTest() {
        final char c = '重';
        List<String> pinyinList = PinyinBs.newInstance().toPinyinList(c);

        Assert.assertEquals("[zhòng, chóng, tóng]", pinyinList.toString());
    }

    /**
     * 普通格式
     * @since 0.0.3
     */
    @Test
    public void normalStyleTest() {
        final String text = "我爱中文";

        String pinyin = PinyinBs.newInstance()
                .style(PinyinToneStyles.normal()).toPinyin(text);

        Assert.assertEquals("wo ai zhong wen", pinyin);
    }

    /**
     * 首字母格式
     * @since 0.0.3
     */
    @Test
    public void firstLetterStyleTest() {
        final String text = "我爱中文";

        String pinyin = PinyinBs.newInstance()
                .style(PinyinToneStyles.firstLetter()).toPinyin(text);

        Assert.assertEquals("w a z w", pinyin);
    }

}
